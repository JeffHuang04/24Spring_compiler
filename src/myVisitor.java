import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.llvm.LLVM.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import static org.bytedeco.llvm.global.LLVM.*;

public class myVisitor extends SysYParserBaseVisitor<LLVMValueRef> {
	private static final LLVMModuleRef module = LLVMModuleCreateWithName("module");
	LLVMBuilderRef builder = LLVMCreateBuilder();
	public LLVMTypeRef i32Type = LLVMInt32Type();
	private SymbolTableStack symbolTableStack = new SymbolTableStack();
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
			if (ctx.initVal() != null && ctx.initVal().exp() != null) {
				LLVMValueRef value = visitExp(ctx.initVal().exp());
				LLVMValueRef Var = LLVMAddGlobal(module, i32Type, varName);
				LLVMSetInitializer(Var, value);
				IntType globalVar = new IntType();
				symbolTableStack.put(varName,globalVar);
			}else {//没有初始化
				return null;
			}
		}else {
			if (ctx.initVal() != null && ctx.initVal().exp() != null) {
				String name = ctx.IDENT().getText();
				LLVMValueRef pointer = LLVMBuildAlloca(builder, i32Type, name);
				LLVMValueRef value = visitExp(ctx.initVal().exp());
				LLVMBuildStore(builder, value, pointer);
				//LLVMValueRef LoadValue = LLVMBuildLoad(builder, pointer, varName);
				IntType scopeVar = new IntType();
				scopeVar.pointer = pointer;
				symbolTableStack.put(varName,scopeVar);
			}else {//没有初始化
				return null;
			}
		}
		return null;
	}

	@Override
	public LLVMValueRef visitConstDef(SysYParser.ConstDefContext ctx) {
		String constVarName = ctx.IDENT().getText();
		if (ctx.parent.parent.parent instanceof SysYParser.CompUnitContext){//全局变量
			if (ctx.constInitVal() != null && ctx.constInitVal().constExp() != null) {
				LLVMValueRef value = visitExp(ctx.constInitVal().constExp().exp());
				LLVMValueRef Var = LLVMAddGlobal(module, i32Type, constVarName);
				LLVMSetInitializer(Var, value);
				IntType globalVar = new IntType();
				symbolTableStack.put(constVarName,globalVar);
			}else {
				return null;
			}
		}else {
			if (ctx.constInitVal() != null && ctx.constInitVal().constExp() != null) {
				String name = ctx.IDENT().getText();
				LLVMValueRef pointer = LLVMBuildAlloca(builder, i32Type, name);
				LLVMValueRef value = visitExp(ctx.constInitVal().constExp().exp());
				LLVMBuildStore(builder, value, pointer);
				//LLVMValueRef LoadValue = LLVMBuildLoad(builder, pointer, constVarName);
				IntType scopeVar = new IntType();
				scopeVar.pointer = pointer;
				symbolTableStack.put(constVarName,scopeVar);
			}else {//没有初始化
				return null;
			}
		}
		return null;
	}

	@Override
	public LLVMValueRef visitFuncDef(SysYParser.FuncDefContext ctx) {
		String funcName = ctx.IDENT().getText();
		LLVMTypeRef returnType = i32Type;
		int funcFParamNum = 0;
		if (ctx.funcFParams() != null){
			funcFParamNum = ctx.funcFParams().funcFParam().size();
			symbolTableStack.pushScope();//给形参添加作用域
		}
		PointerPointer<Pointer> argumentTypes = new PointerPointer<>(funcFParamNum);
		for (int i = 0; i<funcFParamNum; i++){
			argumentTypes.put(i,i32Type);//默认函数的形参只有int类型
		}
		LLVMTypeRef ft = LLVMFunctionType(returnType, argumentTypes, funcFParamNum, 0);//默认只有main且main无参数
		LLVMValueRef function = LLVMAddFunction(module, funcName, ft);
		LLVMBasicBlockRef fucBlock = LLVMAppendBasicBlock(function,funcName+"Entry");
		LLVMPositionBuilderAtEnd(builder, fucBlock);
		ArrayList<LLVMValueRef> paramPointers = new ArrayList<>(funcFParamNum);
		for (int i = 0; i<funcFParamNum; i++){
			String name = ctx.funcFParams().funcFParam().get(i).IDENT().getText();
			IntType funcFParam = new IntType();//默认函数的形参只有int类型
			LLVMValueRef pointer = LLVMBuildAlloca(builder, i32Type, name);
			funcFParam.pointer = pointer;
			paramPointers.add(pointer);
			symbolTableStack.put(name,funcFParam);
		}
		for (int i = 0; i < funcFParamNum;i++){
			LLVMBuildStore(builder, LLVMGetParam(function,i), paramPointers.get(i));
		}
		visit(ctx.block());
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
			return LLVMBuildCall(builder,func,argumentTypes,funcRParamNum,funcName);
		}else if (ctx.unaryOp()!=null) {
			if (Objects.equals(ctx.unaryOp().getText(), "-")){
				//LLVMValueRef zero = LLVMConstInt(i32Type, 0, /* signExtend */ 0);
				LLVMValueRef right = visitExp(ctx.exp(0));
				return LLVMBuildNeg(builder,right,"Neg");
				//return LLVMBuildSub(builder,zero,right,"minus");
			} else if (Objects.equals(ctx.unaryOp().getText(), "!")) {
				LLVMValueRef right = visitExp(ctx.exp(0));
				LLVMValueRef zero = LLVMConstInt(i32Type, 0, /* signExtend */ 0);
				//LLVMValueRef one = LLVMConstInt(i32Type, 1, /* signExtend */ 0);
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
			if (ctx.exp()!=null){
				LLVMValueRef result = visitExp(ctx.exp());
				LLVMBuildRet(builder, result);
			}
			return null;
		} else if (ctx.lVal() != null) {
			String varName = ctx.lVal().IDENT().getText();
			Type varTy = symbolTableStack.findAll(varName);
			LLVMValueRef pointer;
//			if (varTy instanceof IntType){//将形参赋给局部变量，之后将形参当作局部变量来处理
//				if (((IntType) varTy).isFuncFParam && ((IntType) varTy).pointer == null){
//					LLVMValueRef pointerParam = LLVMBuildAlloca(builder, i32Type,varName);
//					LLVMBuildStore(builder, ((IntType) varTy).FuncFParamPointer, pointerParam);
//					((IntType) varTy).pointer = pointerParam;
//				}
//			}else {
//				return null;
//			}
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
		}
		return super.visitStmt(ctx);
	}

	@Override
	public LLVMValueRef visitLVal(SysYParser.LValContext ctx) {
		String lValName = ctx.IDENT().getText();
		Type lValTy = symbolTableStack.findAll(lValName);
//		if (lValTy instanceof IntType){//将形参赋给局部变量，之后将形参当作局部变量来处理
//			if (((IntType) lValTy).isFuncFParam && ((IntType) lValTy).pointer == null){
//				LLVMValueRef pointer = LLVMBuildAlloca(builder, i32Type,lValName);
//				LLVMBuildStore(builder, ((IntType) lValTy).FuncFParamPointer, pointer);
//				((IntType) lValTy).pointer = pointer;
//			}
//		}else {
//			return null;
//		}
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
}
