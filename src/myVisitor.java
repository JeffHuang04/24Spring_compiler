import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class myVisitor extends SysYParserBaseVisitor<Void>{
	private enum ErrorType{
		UNDEFINED_VARIABLE(1, "Undefined variable"),
		UNDEFINED_FUNCTION(2, "Undefined function"),
		REDEFINED_VARIABLE(3, "Redefined variable"),
		REDEFINED_FUNCTION(4, "Redefined function"),
		TYPE_MISMATCHED_ASSIGNMENT(5, "Type mismatched for assignment"),
		TYPE_MISMATCHED_OPERANDS(6, "Type mismatched for operands"),
		TYPE_MISMATCHED_RETURN(7, "Type mismatched for return"),
		FUNCTION_NOT_APPLICABLE(8, "Function is not applicable for arguments"),
		NOT_AN_ARRAY(9, "Not an array"),
		NOT_A_FUNCTION(10, "Not a function"),
		ILLEGAL_ASSIGNMENT_TARGET(11, "The left-hand side of an assignment must be a variable");

		private final int code;
		private final String message;
		ErrorType(int code, String message) {
			this.code = code;
			this.message = message;
		}
		public int getCode() {
			return code;
		}
		public String getMessage() {
			return message;
		}
	}
	private OutputHelper outputHelper;
	public void setOutputHelper(OutputHelper outputHelper) {
		this.outputHelper = outputHelper;
	}
	private SymbolTableStack symbolTableStack = new SymbolTableStack();

	@Override
	public Void visitBlock(SysYParser.BlockContext ctx) {
		symbolTableStack.pushScope();//入栈
		//添加形参到这个作用域中
		visitChildren(ctx);
		symbolTableStack.popScope();//出栈
		return null;
	}

	@Override
	public Void visitFuncDef(SysYParser.FuncDefContext ctx) {
		String funcName = ctx.IDENT().getText();
		if (symbolTableStack.findNow(funcName) != null ){
			outputHelper.outputErr(ErrorType.REDEFINED_FUNCTION.getCode(),ctx.IDENT().getSymbol().getLine(),ErrorType.REDEFINED_FUNCTION.getMessage());
			return null;//遇到该错误直接跳过该函数
		}
		String retTyToString = ctx.funcType().getText();
		Type retTy = null;
		if (retTyToString.equals("int")){
			retTy = new IntType();
		} else if (retTyToString.equals("void")) {
			retTy = new VoidType();
		}//一个函数的返回值retTy仅限于int类型，不包含array类型
		ArrayList<Type> paramsType = new ArrayList<>();
		symbolTableStack.pushScope();//建立函数参数作用域(空参数也要建立)
		if (ctx.funcFParams() != null){
			visit(ctx.funcFParams());
			Map<String, Type> paramsTable = symbolTableStack.getNowTable();
			paramsType.addAll(paramsTable.values());
		}
		Type funcType = new FunctionType(retTy,paramsType);
		symbolTableStack.put(funcName,funcType);//如果是函数类型，直接在栈底push
		visit(ctx.block());
		symbolTableStack.popScope();//退出函数参数列表作用域
		return null;
	}

	@Override
	public Void visitFuncFParam(SysYParser.FuncFParamContext ctx) {
		String funcFParamName = ctx.IDENT().getText();
		if (symbolTableStack.findNow(funcFParamName) != null ){
			outputHelper.outputErr(ErrorType.REDEFINED_VARIABLE.getCode(),ctx.IDENT().getSymbol().getLine(),ErrorType.REDEFINED_VARIABLE.getMessage());
            return null;//跳过重名参数
		}
		Type funcParamTy;
		if (!ctx.L_BRACKT().isEmpty()){//表示是Array类型
			funcParamTy = new ArrayType(new IntType(),ctx.L_BRACKT().size());
		}else {//表示是Int类型
			funcParamTy = new IntType();
		}
		symbolTableStack.put(funcFParamName,funcParamTy);
		return null;
	}
	@Override
	public Void visitConstDef(SysYParser.ConstDefContext ctx) {
		String constDefName = ctx.IDENT().getText();
		if (symbolTableStack.findNow(constDefName) != null ){
			outputHelper.outputErr(ErrorType.REDEFINED_VARIABLE.getCode(),ctx.IDENT().getSymbol().getLine(),ErrorType.REDEFINED_VARIABLE.getMessage());
			return null;//跳过重名定义
		}
		Type constDefTy;
		if (!ctx.L_BRACKT().isEmpty()){//表示是Array类型
			constDefTy = new ArrayType(new IntType(),ctx.L_BRACKT().size());
		}else {//表示是Int类型
			visitConstInitVal(ctx.constInitVal());//暂未处理
			constDefTy = new IntType();
		}
		symbolTableStack.put(constDefName,constDefTy);
		return null;
	}
	@Override
	public Void visitVarDef(SysYParser.VarDefContext ctx) {
		String varDefName = ctx.IDENT().getText();
		if (symbolTableStack.findNow(varDefName) != null ){
			outputHelper.outputErr(ErrorType.REDEFINED_VARIABLE.getCode(),ctx.IDENT().getSymbol().getLine(),ErrorType.REDEFINED_VARIABLE.getMessage());
			return null;//跳过重名定义
		}
		Type varDefTy;
		if (!ctx.L_BRACKT().isEmpty()){//表示是Array类型
			varDefTy = new ArrayType(new IntType(),ctx.L_BRACKT().size());
		}else {//表示是Int类型
			if (ctx.ASSIGN() != null){//有定义语句 暂未处理
				visitInitVal(ctx.initVal());
			}
			varDefTy = new IntType();
		}
		symbolTableStack.put(varDefName,varDefTy);
		return null;
	}
	@Override
	public Void visitInitVal(SysYParser.InitValContext ctx) {
		return super.visitInitVal(ctx);
	}
	@Override
	public Void visitConstInitVal(SysYParser.ConstInitValContext ctx) {
		return super.visitConstInitVal(ctx);
	}

	@Override
	public Void visitLVal(SysYParser.LValContext ctx) {
		calLValType(ctx);
		return null;
	}

	@Override
	public Void visitStmt(SysYParser.StmtContext ctx) {
//		if (ctx.RETURN() != null && ctx.exp() != null){
//			Type funcTy = symbolTableStack.findNowFuncTy();
//			Type returnTyExp = ((FunctionType) funcTy).getRetTy();
//			Type returnTyAct = calExpType(ctx.exp());
//			if (returnTyAct instanceof IntType && returnTyExp instanceof IntType){
//				return null;
//			}else if (returnTyAct instanceof ArrayType && returnTyExp instanceof ArrayType){
//				if (((ArrayType) returnTyAct).getDimension() == ((ArrayType) returnTyExp).getDimension()){
//					return null;
//				}else {
//					outputHelper.outputErr(ErrorType.TYPE_MISMATCHED_RETURN.getCode(),ctx.RETURN().getSymbol().getLine(),ErrorType.TYPE_MISMATCHED_RETURN.getMessage());
//					return null;
//				}
//			}else {
//				outputHelper.outputErr(ErrorType.TYPE_MISMATCHED_RETURN.getCode(),ctx.RETURN().getSymbol().getLine(),ErrorType.TYPE_MISMATCHED_RETURN.getMessage());
//				return null;
//			}
//		}
		return super.visitStmt(ctx);
	}

	private Type calExpType(SysYParser.ExpContext ctx){
		if (ctx.L_PAREN() != null && !ctx.exp().isEmpty()){
			return calExpType(ctx.exp(0));
		} else if (ctx.lVal() != null) {
			return calLValType(ctx.lVal());
		} else if (ctx.number() != null){
			return new IntType();
		} else if (ctx.IDENT() != null) {
			String funcName = ctx.IDENT().getText();
			if (symbolTableStack.findAll(funcName) == null){
				outputHelper.outputErr(ErrorType.UNDEFINED_FUNCTION.getCode(),ctx.IDENT().getSymbol().getLine(),ErrorType.UNDEFINED_FUNCTION.getMessage());
				return null;
			}

		}
		return null;
	}
	private Type calLValType(SysYParser.LValContext ctx){
		String LValName = ctx.IDENT().getText();
		Type LValTy = symbolTableStack.findAll(LValName);
		if (LValTy == null){//左值没有函数使用
			outputHelper.outputErr(ErrorType.UNDEFINED_VARIABLE.getCode(), ctx.IDENT().getSymbol().getLine(),ErrorType.UNDEFINED_VARIABLE.getMessage());
			return null;
		} else if ((LValTy instanceof IntType|| LValTy instanceof FunctionType)
				&& !ctx.L_BRACKT().isEmpty()) {
			outputHelper.outputErr(ErrorType.NOT_AN_ARRAY.getCode(), ctx.IDENT().getSymbol().getLine(),ErrorType.NOT_AN_ARRAY.getMessage());
			return null;//对IntType或FuncType使用下标运算符
		} else if (LValTy instanceof FunctionType && ctx.getParent() instanceof SysYParser.StmtContext && ctx.getParent().getChild(1).getText().equals("=")) {
			outputHelper.outputErr(ErrorType.ILLEGAL_ASSIGNMENT_TARGET.getCode(),ctx.IDENT().getSymbol().getLine(),ErrorType.ILLEGAL_ASSIGNMENT_TARGET.getMessage());
			return null;
		}//赋值号左侧是函数名
		if (LValTy instanceof IntType){
			return new IntType();
		} else if (LValTy instanceof ArrayType) {
			int size = ctx.L_BRACKT().size();
			return new ArrayType(new IntType(),((ArrayType) LValTy).getDimension()-size);
		} else if (LValTy instanceof FunctionType) {
			return LValTy;//不确定正确性
		}
		return null;
	}
}
