import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class myVisitor extends SysYParserBaseVisitor<Void>{
	@Override
	public Void visitChildren(RuleNode node) {
		return super.visitChildren(node);
	}

	@Override
	public Void visitTerminal(TerminalNode node) {
		String text = node.getText();
		switch (node.getSymbol().getType()){
			case SysYParser.CONST:
			case SysYParser.INT:
			case SysYParser.VOID:
			case SysYParser.IF:
			case SysYParser.ELSE:
			case SysYParser.WHILE:
			case SysYParser.BREAK:
			case SysYParser.CONTINUE:
			case SysYParser.RETURN:
				System.out.print("\u001B[96m");//Bright Cyan
				System.out.print(text);
				if (NodeInDecl(node)) {
					System.out.print("\u001B[95m");//Bright Magenta
				} else if (NodeInStmtNotBlock(node)) {
					System.out.print("\u001B[97m"); //White
				} else {
					System.out.print("\u001B[39m");
				}
				break;
			case SysYParser.PLUS:
			case SysYParser.MINUS:
			case SysYParser.MUL:
			case SysYParser.DIV:
			case SysYParser.MOD:
			case SysYParser.ASSIGN:
			case SysYParser.EQ:
			case SysYParser.NEQ:
			case SysYParser.LT:
			case SysYParser.GT:
			case SysYParser.LE:
			case SysYParser.GE:
			case SysYParser.NOT:
			case SysYParser.AND:
			case SysYParser.OR:
			case SysYParser.COMMA:
			case SysYParser.SEMICOLON:
				System.out.print("\u001B[91m"); //Bright Red
				System.out.print(text);
				if (NodeInDecl(node)) {
					System.out.print("\u001B[95m");//Bright Magenta
				} else if (NodeInStmtNotBlock(node)) {
					System.out.print("\u001B[97m"); //White
				} else {
					System.out.print("\u001B[39m");
				}
				break;
			case SysYParser.INTEGER_CONST:
				System.out.print("\u001B[35m"); //Magenta
				System.out.print(text);
				if (NodeInDecl(node)) {
					System.out.print("\u001B[95m");//Bright Magenta
				} else if (NodeInStmtNotBlock(node)) {
					System.out.print("\u001B[97m"); //White
				} else {
					System.out.print("\u001B[39m");
				}
				break;
			case SysYParser.IDENT:
				ParseTree parent = node.getParent();
				if (parent instanceof SysYParser.FuncDefContext
					|| parent instanceof SysYParser.ExpContext){//保证是函数名
					System.out.print("\u001B[93m"); //Bright Yellow
					System.out.print(text);
					System.out.print("\u001B[39m");
				}else {
					System.out.print(text);
				}
				break;

			case SysYParser.WS://Lexer中不包括/0
				break;
			default:
				System.out.print(text);
				break;
		}
		return null;
	}

	@Override
	public Void visitStmt(SysYParser.StmtContext ctx) {
		if (ctx.block() == null){
			System.out.print("\u001B[97m"); //White
			visitChildren(ctx);
			System.out.print("\u001B[39m");
		} else {
			visitChildren(ctx);
		}
		return null;
	}

	@Override
	public Void visitDecl(SysYParser.DeclContext ctx) {
		System.out.print("\u001B[95m"); //Bright Magenta
		System.out.print("\u001B[4m"); //Underlined
		visitChildren(ctx);
		System.out.print("\u001B[0m");
		return null;
	}

	private Boolean NodeInDecl(TerminalNode node){
		ParseTree parent = node.getParent();
		while (parent != null){
			if (parent instanceof SysYParser.DeclContext){
				return true;
			}
			parent = parent.getParent();
		}
		return false;
	}

	private Boolean NodeInStmtNotBlock(TerminalNode node){
		ParseTree parent = node.getParent();
		while (parent != null){
			if (parent instanceof SysYParser.StmtContext
			&& ((SysYParser.StmtContext) parent).block() == null){
				return true;
			}
			parent = parent.getParent();
		}
		return false;
	}
}
