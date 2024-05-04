import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class myVisitor extends SysYParserBaseVisitor<Void>{
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
			outputHelper.outputErr(4,ctx.IDENT().getSymbol().getLine(),"Redefined function");
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
		visit(ctx.block());
		symbolTableStack.popScope();//退出函数参数列表作用域
		symbolTableStack.put(funcName,funcType);//在退出函数参数列表作用域后put函数名
		return null;
	}

	@Override
	public Void visitFuncFParam(SysYParser.FuncFParamContext ctx) {
		String funcFParamName = ctx.IDENT().getText();
		if (symbolTableStack.findNow(funcFParamName) != null ){
			outputHelper.outputErr(3,ctx.IDENT().getSymbol().getLine(),"Redefined variable");
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
			outputHelper.outputErr(3,ctx.IDENT().getSymbol().getLine(),"Redefined variable");
			return null;//跳过重名定义
		}
		Type constDefTy;
		if (!ctx.L_BRACKT().isEmpty()){//表示是Array类型
			constDefTy = new ArrayType(new IntType(),ctx.L_BRACKT().size());
		}else {//表示是Int类型
			constDefTy = new IntType();
		}
		symbolTableStack.put(constDefName,constDefTy);
		return null;
	}

	@Override
	public Void visitVarDef(SysYParser.VarDefContext ctx) {
		String varDefName = ctx.IDENT().getText();
		if (symbolTableStack.findNow(varDefName) != null ){
			outputHelper.outputErr(3,ctx.IDENT().getSymbol().getLine(),"Redefined variable");
			return null;//跳过重名定义
		}
		Type varDefTy;
		if (!ctx.L_BRACKT().isEmpty()){//表示是Array类型
			varDefTy = new ArrayType(new IntType(),ctx.L_BRACKT().size());
		}else {//表示是Int类型
			varDefTy = new IntType();
		}
		symbolTableStack.put(varDefName,varDefTy);
		return null;
	}
}
