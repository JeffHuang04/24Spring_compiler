import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.llvm.LLVM.*;

import java.util.Objects;
import static org.bytedeco.llvm.global.LLVM.*;

public class myVisitor extends SysYParserBaseVisitor<LLVMValueRef> {
	private static final LLVMModuleRef module = LLVMModuleCreateWithName("module");
	LLVMBuilderRef builder = LLVMCreateBuilder();
	public LLVMTypeRef i32Type = LLVMInt32Type();
	public LLVMTypeRef voidType = LLVMVoidType();
	LLVMValueRef zero = LLVMConstInt(i32Type, 0, 0);
	LLVMValueRef currentFunc;
	boolean hasReturn;
	private SymbolTableStack symbolTableStack = new SymbolTableStack();
	private WhileStack whileStack = new WhileStack();
	myVisitor(){
		//初始化LLVM
		LLVMInitializeCore(LLVMGetGlobalPassRegistry());
		LLVMLinkInMCJIT();
		LLVMInitializeNativeAsmPrinter();
		LLVMInitializeNativeAsmParser();
		LLVMInitializeNativeTarget();
	}
	public static LLVMModuleRef getModule(){
		return module;
	}

	@Override
	public LLVMValueRef visitVarDef(SysYParser.VarDefContext ctx) {
		String varName = ctx.IDENT().getText();
		if (ctx.parent.parent.parent instanceof SysYParser.CompUnitContext){//全局变量
			LLVMValueRef Var = LLVMAddGlobal(module, i32Type, varName);
			if (ctx.initVal() != null && ctx.initVal().exp() != null) {
				LLVMValueRef value = visitExp(ctx.initVal().exp());
				LLVMSetInitializer(Var, value);
			}else {
				LLVMSetInitializer(Var,zero);
			}
			IntType globalVar = new IntType();
			symbolTableStack.put(varName,globalVar);
		}else {
			LLVMValueRef pointer = LLVMBuildAlloca(builder, i32Type, varName);
			if (ctx.initVal() != null && ctx.initVal().exp() != null) {
				LLVMValueRef value = visitExp(ctx.initVal().exp());
				LLVMBuildStore(builder, value, pointer);
			}
			IntType scopeVar = new IntType();
			scopeVar.pointer = pointer;
			symbolTableStack.put(varName,scopeVar);
		}
		return null;
	}

	@Override
	public LLVMValueRef visitConstDef(SysYParser.ConstDefContext ctx) {
		String constVarName = ctx.IDENT().getText();
		if (ctx.parent.parent.parent instanceof SysYParser.CompUnitContext){//全局变量
			LLVMValueRef Var = LLVMAddGlobal(module, i32Type, constVarName);
			if (ctx.constInitVal() != null && ctx.constInitVal().constExp() != null) {
				LLVMValueRef value = visitExp(ctx.constInitVal().constExp().exp());
				LLVMSetInitializer(Var, value);
			}else {
				LLVMSetInitializer(Var,zero);
			}
			IntType globalVar = new IntType();
			symbolTableStack.put(constVarName,globalVar);
		}else {
			LLVMValueRef pointer = LLVMBuildAlloca(builder, i32Type, constVarName);
			if (ctx.constInitVal() != null && ctx.constInitVal().constExp() != null) {
				LLVMValueRef value = visitExp(ctx.constInitVal().constExp().exp());
				LLVMBuildStore(builder, value, pointer);
			}
			IntType scopeVar = new IntType();
			scopeVar.pointer = pointer;
			symbolTableStack.put(constVarName,scopeVar);
		}
		return null;
	}

	@Override
	public LLVMValueRef visitFuncDef(SysYParser.FuncDefContext ctx) {
		String funcName = ctx.IDENT().getText();
		LLVMTypeRef returnType;
		if (ctx.funcType().getText().equals("void")){
			returnType = voidType;
		}else {
			returnType = i32Type;
		}
		int funcFParamNum = 0;
		symbolTableStack.pushScope();//给形参添加作用域(不管有没有参数都要添加)
		if (ctx.funcFParams() != null){
			funcFParamNum = ctx.funcFParams().funcFParam().size();
		}
		PointerPointer<Pointer> argumentTypes = new PointerPointer<>(funcFParamNum);
		for (int i = 0; i<funcFParamNum; i++){
			argumentTypes.put(i,i32Type);//默认函数的形参只有int类型
		}
		LLVMTypeRef ft = LLVMFunctionType(returnType, argumentTypes, funcFParamNum, 0);
		LLVMValueRef function = LLVMAddFunction(module, funcName, ft);
		LLVMBasicBlockRef fucBlock = LLVMAppendBasicBlock(function,funcName+"Entry");
		LLVMPositionBuilderAtEnd(builder, fucBlock);
		currentFunc = function;
		for (int i = 0; i<funcFParamNum; i++){
			String name = ctx.funcFParams().funcFParam().get(i).IDENT().getText();
			IntType funcFParam = new IntType();//默认函数的形参只有int类型
			LLVMValueRef pointer = LLVMBuildAlloca(builder, i32Type, name);
			LLVMBuildStore(builder, LLVMGetParam(function,i), pointer);
			funcFParam.pointer = pointer;
			symbolTableStack.put(name,funcFParam);
		}
		hasReturn = false;
		visit(ctx.block());
		if (returnType.equals(voidType) && !hasReturn){
			LLVMBuildRet(builder,null);
		}
		symbolTableStack.popScope();//弹出形参作用域
		return null;
	}

	@Override
	public LLVMValueRef visitBlock(SysYParser.BlockContext ctx) {
		symbolTableStack.pushScope();
		visitChildren(ctx);
		symbolTableStack.popScope();
		return null;
	}

	@Override
	public LLVMValueRef visitExp(SysYParser.ExpContext ctx) {
		if (ctx.L_PAREN()!=null && ctx.R_PAREN()!=null && ctx.IDENT() == null){
			return visitExp(ctx.exp(0));
		}else if (ctx.lVal()!=null){
			return visitLVal(ctx.lVal());
		}else if (ctx.number()!=null){
			if (ctx.number().getText().startsWith("0x")||ctx.number().getText().startsWith("0X")){
				return LLVMConstInt(i32Type, Integer.parseInt(ctx.number().getText().substring(2),16), /* signExtend */ 0);
			} else if (ctx.number().getText().startsWith("0")&&ctx.number().getText().length()!=1) {
				return LLVMConstInt(i32Type, Integer.parseInt(ctx.number().getText().substring(1),8), /* signExtend */ 0);
			}else {
				return LLVMConstInt(i32Type, Integer.parseInt(ctx.number().getText()), /* signExtend */ 0);
			}
		}else if(ctx.IDENT() != null && ctx.L_PAREN() != null && ctx.R_PAREN() != null){//函数调用
			String funcName = ctx.IDENT().getText();
			LLVMValueRef func = LLVMGetNamedFunction(module,funcName);
			int funcRParamNum = 0;
			if (ctx.funcRParams() != null){
				funcRParamNum = ctx.funcRParams().param().size();
			}
			PointerPointer<Pointer> argumentTypes = new PointerPointer<>(funcRParamNum);
			for (int i = 0; i < funcRParamNum;i++){
				argumentTypes.put(i,visitExp(ctx.funcRParams().param().get(i).exp()));
			}
			LLVMTypeRef returnType = LLVMGetReturnType(LLVMGetElementType(LLVMTypeOf(func)));
			if (returnType.equals(voidType)){
				return LLVMBuildCall(builder,func,argumentTypes,funcRParamNum,"");
			}else {
				return LLVMBuildCall(builder, func, argumentTypes, funcRParamNum, funcName);
			}
		}else if (ctx.unaryOp()!=null) {
			if (Objects.equals(ctx.unaryOp().getText(), "-")){
				LLVMValueRef right = visitExp(ctx.exp(0));
				return LLVMBuildNeg(builder,right,"Neg");
			} else if (Objects.equals(ctx.unaryOp().getText(), "!")) {
				LLVMValueRef right = visitExp(ctx.exp(0));
				LLVMValueRef zero = LLVMConstInt(i32Type, 0, /* signExtend */ 0);
				LLVMValueRef condition = LLVMBuildICmp(builder, LLVMIntNE, right, zero, "condition = n == 0");
				LLVMValueRef xor = LLVMBuildXor(builder, condition, LLVMConstInt(LLVMInt1Type(), 1, 0), "xor");
				return LLVMBuildZExt(builder, xor, i32Type, "zext");
			} else {
				return visitExp(ctx.exp(0));
			}
		} else if (ctx.IDENT() == null){
			LLVMValueRef left = visit(ctx.exp(0));
			LLVMValueRef right = visit(ctx.exp(1));
			if (ctx.MUL() != null){
				return LLVMBuildMul(builder,left,right,"mul");
			} else if (ctx.DIV() != null) {
				return LLVMBuildSDiv(builder,left,right,"div");
			} else if (ctx.MOD() != null) {
				return LLVMBuildSRem(builder,left,right,"mod");
			} else if (ctx.PLUS() != null) {
				return LLVMBuildAdd(builder,left,right,"add");
			} else if (ctx.MINUS() != null) {
				return LLVMBuildSub(builder,left,right,"minus");
			}else {
				return null;
			}
		}
		return null;
	}

	@Override
	public LLVMValueRef visitStmt(SysYParser.StmtContext ctx) {
		if (ctx.RETURN() != null){
			hasReturn = true;
			if (ctx.exp()!=null){
				LLVMValueRef result = visitExp(ctx.exp());
				LLVMBuildRet(builder, result);
				return null;
			}else {
				LLVMBuildRet(builder, null);
				return null;
			}
		} else if (ctx.lVal() != null) {
			String varName = ctx.lVal().IDENT().getText();
			Type varTy = symbolTableStack.findAll(varName);
			LLVMValueRef pointer;
			if (varTy instanceof IntType ){
				if (((IntType) varTy).pointer != null) {
					pointer = ((IntType) varTy).pointer;
				}else {
					pointer = LLVMGetNamedGlobal(module,varName);//全局变量可以被修改!!
				}
			}else {
				return null;
			}
			LLVMValueRef value = visitExp(ctx.exp());
			LLVMBuildStore(builder, value, pointer);
			return null;
		} else if (ctx.IF() != null){
			LLVMValueRef condI32 = visitCond(ctx.cond());
			LLVMValueRef condI1 =  LLVMBuildICmp(builder, LLVMIntNE, condI32, zero, "cond");
			LLVMBasicBlockRef ifTrueBlock = LLVMAppendBasicBlock(currentFunc, "if_true");
			LLVMBasicBlockRef ifFalseBlock = LLVMAppendBasicBlock(currentFunc, "if_false");
			LLVMBasicBlockRef nextBlock = LLVMAppendBasicBlock(currentFunc, "next");
			LLVMBuildCondBr(builder,condI1,ifTrueBlock,ifFalseBlock);
			LLVMPositionBuilderAtEnd(builder, ifTrueBlock);
			visitStmt(ctx.stmt(0));
			LLVMBuildBr(builder,nextBlock);
			LLVMPositionBuilderAtEnd(builder, ifFalseBlock);
			if (ctx.ELSE() != null){
				visitStmt(ctx.stmt(1));
			}
			LLVMBuildBr(builder,nextBlock);
			LLVMPositionBuilderAtEnd(builder, nextBlock);
			return null;
		}else if (ctx.WHILE()!=null){
			LLVMBasicBlockRef whileCondBlock = LLVMAppendBasicBlock(currentFunc, "while_cond");
			LLVMBasicBlockRef whileBodyBlock = LLVMAppendBasicBlock(currentFunc, "while_body");
			LLVMBasicBlockRef whileEndBlock = LLVMAppendBasicBlock(currentFunc, "while_end");
			whileStack.pushTable(whileCondBlock,whileEndBlock);
			LLVMBuildBr(builder,whileCondBlock);
			LLVMPositionBuilderAtEnd(builder, whileCondBlock);
			LLVMValueRef condI32 = visitCond(ctx.cond());
			LLVMValueRef condI1 =  LLVMBuildICmp(builder, LLVMIntNE, condI32, zero, "cond");
			LLVMBuildCondBr(builder,condI1,whileBodyBlock,whileEndBlock);
			LLVMPositionBuilderAtEnd(builder, whileBodyBlock);
			visitStmt(ctx.stmt(0));
			LLVMBuildBr(builder,whileCondBlock);
			LLVMPositionBuilderAtEnd(builder, whileEndBlock);
			whileStack.popTable();
			return null;
		}else if (ctx.BREAK()!=null){
			WhileTable currentWhileTable = whileStack.peekTable();
			LLVMBuildBr(builder,currentWhileTable.whileEndBlock);
			return null;
		} else if (ctx.CONTINUE() != null) {
			WhileTable currentWhileTable = whileStack.peekTable();
			LLVMBuildBr(builder,currentWhileTable.whileCondBlock);
			return null;
		}
		return super.visitStmt(ctx);
	}

	@Override
	public LLVMValueRef visitLVal(SysYParser.LValContext ctx) {
		String lValName = ctx.IDENT().getText();
		Type lValTy = symbolTableStack.findAll(lValName);
		if (lValTy instanceof IntType){
			if (((IntType) lValTy).pointer == null){
				LLVMValueRef globalVarRef = LLVMGetNamedGlobal(module,lValName);
				return LLVMBuildLoad(builder,globalVarRef,lValName);
			}else {
				LLVMValueRef pointer = ((IntType) lValTy).pointer;
				return LLVMBuildLoad(builder, pointer, lValName);
			}
		}else {//默认无其他属性
			return null;
		}
	}

	@Override
	public LLVMValueRef visitCond(SysYParser.CondContext ctx) {
		if (ctx.exp()!=null){
			return visitExp(ctx.exp());
		}else {
			LLVMValueRef left = visitCond(ctx.cond(0));
			LLVMValueRef right = visitCond(ctx.cond(1));
			if (ctx.LT()!=null){
				LLVMValueRef cmpi1 = LLVMBuildICmp(builder,LLVMIntSLT,left,right,"LT");
				return LLVMBuildZExt(builder,cmpi1,i32Type,"LT");
			} else if (ctx.GT() != null) {
				LLVMValueRef cmpI1 = LLVMBuildICmp(builder, LLVMIntSGT, left, right, "GT");
				return LLVMBuildZExt(builder,cmpI1,i32Type,"GT");
			} else if (ctx.LE() != null) {
				LLVMValueRef cmpI1 = LLVMBuildICmp(builder, LLVMIntSLE, left, right, "LE");
				return LLVMBuildZExt(builder,cmpI1,i32Type,"LE");
			} else if (ctx.GE() != null) {
				LLVMValueRef cmpI1 = LLVMBuildICmp(builder, LLVMIntSGE, left, right, "GE");
				return LLVMBuildZExt(builder,cmpI1,i32Type,"GE");
			} else if (ctx.EQ() != null) {
				LLVMValueRef cmpI1 = LLVMBuildICmp(builder, LLVMIntEQ, left, right, "EQ");
				return LLVMBuildZExt(builder,cmpI1,i32Type,"EQ");
			} else if (ctx.NEQ() != null) {
				LLVMValueRef cmpI1 = LLVMBuildICmp(builder, LLVMIntNE, left, right, "NEQ");
				return LLVMBuildZExt(builder,cmpI1,i32Type,"NEQ");
			} else if (ctx.AND() != null){//短路求值phi方法借助chatgpt
				LLVMBasicBlockRef leftBlock = LLVMAppendBasicBlock(currentFunc, "left");
				LLVMBasicBlockRef rightBlock = LLVMAppendBasicBlock(currentFunc, "right");
				LLVMBasicBlockRef result = LLVMAppendBasicBlock(currentFunc, "result");
				LLVMPositionBuilderAtEnd(builder,leftBlock);
				LLVMValueRef leftI1 =  LLVMBuildICmp(builder, LLVMIntNE, left, zero, "cond");
				LLVMBuildCondBr(builder,leftI1,rightBlock,result);
				LLVMPositionBuilderAtEnd(builder,rightBlock);
				LLVMValueRef rightI1 =  LLVMBuildICmp(builder, LLVMIntNE, right, zero, "cond");
				LLVMBuildCondBr(builder,rightI1,result,result);
				LLVMPositionBuilderAtEnd(builder,result);
				LLVMValueRef phiNode = LLVMBuildPhi(builder, LLVMInt1Type(), "andPhi");
				LLVMAddIncoming(phiNode, new PointerPointer<>(leftI1), new PointerPointer<>(leftBlock), 1);
				LLVMAddIncoming(phiNode, new PointerPointer<>(rightI1), new PointerPointer<>(rightBlock), 1);
				return LLVMBuildZExt(builder,phiNode,i32Type,"AND");
//				LLVMValueRef andi1 = LLVMBuildAnd(builder, left, right, "AND");
//				return LLVMBuildZExt(builder,andi1,i32Type,"AND");
			} else if (ctx.OR() != null) {//短路求值phi方法借助chatgpt
				LLVMBasicBlockRef leftBlock = LLVMAppendBasicBlock(currentFunc, "left");
				LLVMBasicBlockRef rightBlock = LLVMAppendBasicBlock(currentFunc, "right");
				LLVMBasicBlockRef result = LLVMAppendBasicBlock(currentFunc, "result");
				LLVMPositionBuilderAtEnd(builder,leftBlock);
				LLVMValueRef leftI1 =  LLVMBuildICmp(builder, LLVMIntNE, left, zero, "cond");
				LLVMBuildCondBr(builder,leftI1,result,rightBlock);
				LLVMPositionBuilderAtEnd(builder,rightBlock);
				LLVMValueRef rightI1 =  LLVMBuildICmp(builder, LLVMIntNE, right, zero, "cond");
				LLVMBuildCondBr(builder,rightI1,result,result);
				LLVMPositionBuilderAtEnd(builder,result);
				LLVMValueRef phiNode = LLVMBuildPhi(builder, LLVMInt1Type(), "orPhi");
				LLVMAddIncoming(phiNode, new PointerPointer<>(leftI1), new PointerPointer<>(leftBlock), 1);
				LLVMAddIncoming(phiNode, new PointerPointer<>(rightI1), new PointerPointer<>(rightBlock), 1);
				return LLVMBuildZExt(builder,phiNode,i32Type,"OR");
//				LLVMValueRef ori1 = LLVMBuildOr(builder, left, right, "OR");
//				return LLVMBuildZExt(builder,ori1,i32Type,"OR");
			}
		}
		return null;
	}
}
