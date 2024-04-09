import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class myVisitor extends SysYParserBaseVisitor<Void>{
	@Override
	public Void visitChildren(RuleNode node) {
		return super.visitChildren(node);
	}

	String colorTemp = "\u001B[39m";

	int retractionNum = 0;

	String underLineTemp = "";

	Stack<String> stackAllBracket = new Stack<>();
	int colorIndex = 0;
	private static final String[] COLORS =
			{"\u001B[91m", "\u001B[92m", "\u001B[93m",
			"\u001B[94m", "\u001B[95m", "\u001B[96m"};
	//BrightRed,BrightGreen,BrightYellow,
	//BrightBlue,BrightMagenta,BrightCyan
	@Override
	public Void visitTerminal(TerminalNode node) {
		String text = node.getText();
		int flag = node.getSymbol().getType();
		if (Objects.equals(text, "<EOF>")){
			return null;
		}
		switch (flag){
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
				ClearColor(node);
				//PrintSpace();
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
				ClearColor(node);
				break;
			case SysYParser.INTEGER_CONST:
				System.out.print("\u001B[35m"); //Magenta
				System.out.print(text);
				ClearColor(node);
				break;
			case SysYParser.IDENT:
				ParseTree parent = node.getParent();
				if (parent instanceof SysYParser.FuncDefContext
					|| parent instanceof SysYParser.ExpContext){//保证是函数名
					System.out.print("\u001B[93m"); //Bright Yellow
					System.out.print(text);
					ClearColor(node);
				}else {
					System.out.print(text);
				}
				break;
			case SysYParser.L_BRACE:
			case SysYParser.L_PAREN:
			case SysYParser.L_BRACKT:
			case SysYParser.R_BRACE:
			case SysYParser.R_PAREN:
			case SysYParser.R_BRACKT:
				if (Objects.equals(text, "{")
				|| Objects.equals(text, "(")
				|| Objects.equals(text,"[")){
					//System.out.print(COLORS[colorIndex]);//未解之谜，为什么去掉这一行不可以呢
					if (Objects.equals(text, "(")
					|| Objects.equals(text,"[")) {
						System.out.print(COLORS[colorIndex]);
						System.out.print(text);
					}else {
						LBrace(node);
						//System.out.print(text);
					}
					ClearColor(node);
					stackAllBracket.push(text);
					colorIndex = (colorIndex + 1) % COLORS.length;
				}else {
					if ((Objects.equals(text, "}") && stackAllBracket.peek().equals("{"))
							|| (Objects.equals(text, ")") && stackAllBracket.peek().equals("("))
							|| (Objects.equals(text, "]") && stackAllBracket.peek().equals("["))
					){
						stackAllBracket.pop();
						//System.out.print(COLORS[(colorIndex-1+COLORS.length)%COLORS.length]);
						if (Objects.equals(text, ")")
						|| Objects.equals(text, "]")) {
							System.out.print(COLORS[(colorIndex-1+COLORS.length)%COLORS.length]);
							System.out.print(text);
						}else {

							for (int i = 0; i < retractionNum-1; i++){
								PrintRetraction();
							}
							//System.out.print(COLORS[(colorIndex-1+COLORS.length)%COLORS.length]);////未解之谜，为什么去掉这一行不可以呢
							RBrace(node);
							//System.out.print(text);
						}
						ClearColor(node);
						colorIndex = ((colorIndex - 1)+COLORS.length) % COLORS.length;
					}
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
		for (int i = 0; i < retractionNum; i++){
			if (!(ctx.getChild(0) instanceof SysYParser.BlockContext)) {
				PrintRetraction();
			}
		}
		if (!(ctx.getChild(0) instanceof SysYParser.BlockContext)){
			System.out.print("\u001B[97m"); //White
			colorTemp = "\u001B[97m";
			visitChildren(ctx);
			System.out.print("\u001B[39m");
			colorTemp = "\u001B[39m";
		} else {
			visitChildren(ctx);
		}
		if (!JudgeChildrenIsStmt(ctx)){ //判断子节点是否是Stmt
			//System.out.println();
			PrintLineBreak();
		}
		return null;
	}
	@Override
	public Void visitDecl(SysYParser.DeclContext ctx) {
		for (int i = 0; i < retractionNum; i++){
			PrintRetraction();
		}
		System.out.print("\u001B[95m"); //Bright Magenta
		colorTemp = "\u001B[95m";
		System.out.print("\u001B[4m"); //Underlined
		underLineTemp = "\u001B[4m";
		visitChildren(ctx);
		//System.out.println();//decl换行
		PrintLineBreak();
		System.out.print("\u001B[0m");
		colorTemp = "\u001B[39m";
		underLineTemp = "";
		return null;
	}

	@Override
	public Void visitFuncDef(SysYParser.FuncDefContext ctx) {
		int lineNumber = ctx.getStart().getLine();
		if (lineNumber != 1){
			//System.out.println();
			PrintLineBreak();
		}
		visitChildren(ctx);
		return null;
	}

	@Override
	public Void visitBlock(SysYParser.BlockContext ctx) {
		retractionNum++;
		visitChildren(ctx);
		retractionNum--;
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

	private void ClearColor(TerminalNode node){
		if (NodeInDecl(node)) {
			System.out.print("\u001B[95m");//Bright Magenta
		} else if (NodeInStmtNotBlock(node)) {
			System.out.print("\u001B[97m"); //White
		} else {
			System.out.print("\u001B[39m");
		}
	}

	private Boolean JudgeChildrenIsStmt(SysYParser.StmtContext ctx){
		for (ParseTree child: ctx.children){
			if (child instanceof SysYParser.StmtContext){
				return true;
			}
		}
		return false;
	}

	private void LBrace(TerminalNode node){
		String text = node.getText();
		if (node.getParent() instanceof SysYParser.BlockContext){
			if ((node.getParent().getParent() != null
			&& node.getParent().getParent() instanceof SysYParser.FuncDefContext)
			|| (node.getParent().getParent().getParent() != null
			&& node.getParent().getParent() instanceof SysYParser.StmtContext
			&& node.getParent().getParent().getParent() instanceof SysYParser.StmtContext)){
				//System.out.print(' ');
				PrintSpace();
				System.out.print(COLORS[colorIndex]);
				System.out.print(text);//函数声明中、stmt中if、else、while代码块中的作花括号
				//System.out.println();
				PrintLineBreak();
			}else {
				System.out.print(COLORS[colorIndex]);
				System.out.print(text);//单独代码块中的左花括号
				//System.out.println();
				PrintLineBreak();
			}
		}else {//声明语句的左花括号
			System.out.print(COLORS[colorIndex]);
			System.out.print(text);
		}
	}

	private void RBrace(TerminalNode node){
		String text = node.getText();
		if (node.getParent() instanceof SysYParser.BlockContext
		&& node.getParent().getParent() != null
		&& node.getParent().getParent() instanceof SysYParser.FuncDefContext){
			System.out.print(COLORS[(colorIndex-1+COLORS.length)%COLORS.length]);
			System.out.print(text);
			//System.out.println();
			PrintLineBreak();
		}else {
			System.out.print(COLORS[(colorIndex-1+COLORS.length)%COLORS.length]);
			System.out.print(text);
		}
	}

	private void PrintLineBreak() {
		System.out.print("\u001B[0m");//reset
		System.out.println();
		System.out.print(colorTemp);
		if (!Objects.equals(underLineTemp, "")){
			System.out.print(underLineTemp);
		}
	}

	private void PrintSpace() {
		System.out.print("\u001B[0m");//reset
		System.out.print(" ");
		System.out.print(colorTemp);
		if (!Objects.equals(underLineTemp, "")){
			System.out.print(underLineTemp);
		}
	}

	private void PrintRetraction() {
		System.out.print("\u001B[0m");//reset
		System.out.print("    ");
		System.out.print(colorTemp);
		if (!Objects.equals(underLineTemp, "")){
			System.out.print(underLineTemp);
		}
	}
}
