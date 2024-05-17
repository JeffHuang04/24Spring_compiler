import org.bytedeco.llvm.LLVM.*;

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
//		String varName = ctx.IDENT().getText();
//		if (ctx.parent.parent.parent.parent instanceof SysYParser.CompUnitContext){//全局变量
//
//		}
		return super.visitVarDef(ctx);
	}

	@Override
	public LLVMValueRef visitConstDef(SysYParser.ConstDefContext ctx) {

		return super.visitConstDef(ctx);
	}

	@Override
	public LLVMValueRef visitFuncDef(SysYParser.FuncDefContext ctx) {
		String funcName = ctx.IDENT().getText();
		LLVMTypeRef returnType = i32Type;
		LLVMTypeRef ft = LLVMFunctionType(returnType, (LLVMTypeRef) null, 0, 0);//默认只有main且main无参数
		LLVMValueRef function = LLVMAddFunction(module, funcName, ft);
		//通过如下语句在函数中加入基本块，一个函数可以加入多个基本块
		LLVMBasicBlockRef main = LLVMAppendBasicBlock(function,funcName+"Entry");
		LLVMPositionBuilderAtEnd(builder, main);
		visit(ctx.block());
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
			return LLVMConstInt(i32Type, Integer.parseInt(ctx.number().getText()), /* signExtend */ 0);
		} else if (ctx.unaryOp()!=null) {
			if (Objects.equals(ctx.unaryOp().getText(), "-")){
				LLVMValueRef zero = LLVMConstInt(i32Type, 0, /* signExtend */ 0);
				LLVMValueRef right = visitExp(ctx.exp(0));
				return LLVMBuildSub(builder,zero,right,"minus");
			} else if (Objects.equals(ctx.unaryOp().getText(), "!")) {
				LLVMValueRef right = visitExp(ctx.exp(0));
				LLVMValueRef zero = LLVMConstInt(i32Type, 0, /* signExtend */ 0);
				LLVMValueRef one = LLVMConstInt(i32Type, 1, /* signExtend */ 0);
				LLVMValueRef condition = LLVMBuildICmp(builder, LLVMIntNE, right, zero, "condition = n == 0");
				LLVMValueRef xor = LLVMBuildXor(builder, condition, one, "xor");
				return LLVMBuildZExt(builder, xor, i32Type, "zext");
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
		}
		return super.visitStmt(ctx);
	}


}
