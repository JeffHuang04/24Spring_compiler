import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.IllegalFormatCodePointException;
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

	boolean IsOutput = false;
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
				if (flag == SysYParser.ELSE){
					for (int i = 0; i < retractionNum; i++){
						PrintRetraction();
					}
				}//else因为重新换行所以要重新进行缩进
				System.out.print("\u001B[96m");//Bright Cyan
				System.out.print(text);
				ClearColor(node);
				if (flag == SysYParser.CONST || flag == SysYParser.INT
				|| flag == SysYParser.VOID || flag == SysYParser.IF
				 || flag == SysYParser.WHILE
				){
					PrintSpace();
				}
				if (flag == SysYParser.ELSE){
					ParseTree behindElseStmt = node.getParent().getChild(6);
					//System.out.print(behindElseStmt.getChild(0).getText());
					if (behindElseStmt.getChild(0).getText().equals("if")){
						PrintSpace();
					}
				}//处理else后空格
//				if (flag == SysYParser.ELSE){
//					if (node.getParent() instanceof SysYParser.StmtContext
//					&& JudgeBehindElseSingle((SysYParser.StmtContext) node.getParent())){//如果else语句是后面Single
//						retractionNum++;
//					}
//				}//处理else+else单独single 后缩进、换行问题
				if (flag == SysYParser.ELSE){
					ParseTree behindElseStmt = node.getParent().getChild(6);
					if (node.getParent() instanceof SysYParser.StmtContext
					&& JudgeBehindElseSingle((SysYParser.StmtContext) node.getParent())
					&& !(behindElseStmt.getChild(0).getText().equals("if"))) {//如果else语句是后面Single并且是非if
						retractionNum++;
						PrintLineBreak();
					}
				}//处理else+else单独single 后换行问题
				if (flag == SysYParser.RETURN){
					if (node.getParent() instanceof SysYParser.StmtContext
					&& ((SysYParser.StmtContext) node.getParent()).exp() != null){
						PrintSpace();
					}
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
				boolean IsTwoOp = flag == SysYParser.PLUS || flag == SysYParser.MINUS
						|| flag == SysYParser.MUL || flag == SysYParser.DIV
						|| flag == SysYParser.MOD || flag == SysYParser.ASSIGN
						|| flag == SysYParser.EQ || flag == SysYParser.NEQ
						|| flag == SysYParser.LE || flag == SysYParser.GE
						|| flag == SysYParser.LT || flag == SysYParser.GT
						|| flag == SysYParser.AND || flag == SysYParser.OR;
				if (IsTwoOp){
					if (!(node.getParent() instanceof SysYParser.UnaryOpContext)){
						PrintSpace();
					}
				}
				System.out.print("\u001B[91m"); //Bright Red
				System.out.print(text);
				ClearColor(node);
				if (IsTwoOp){
					if (!(node.getParent() instanceof SysYParser.UnaryOpContext)){
						PrintSpace();
					}
				}
				if(flag == SysYParser.COMMA){
					PrintSpace();
				}
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
					//System.out.print(COLORS[colorIndex]);
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
							if (Objects.equals(text,")")){
								if (node.getParent() instanceof SysYParser.StmtContext
								&& ((((SysYParser.StmtContext) node.getParent()).IF()!=null//是IF语句中的右括号
								&& JudgeBehindIfSingle((SysYParser.StmtContext)node.getParent()))
								|| (((SysYParser.StmtContext) node.getParent()).WHILE()!=null//是WHILE语句中的右括号
								&& JudgeBehindWhileSingle((SysYParser.StmtContext)node.getParent())))){//并且if语句后面是Single
									PrintLineBreak();
								}
							}
						}else {
							//System.out.print(COLORS[(colorIndex-1+COLORS.length)%COLORS.length]);
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
			if (!(ctx.getChild(0) instanceof SysYParser.BlockContext)//如果该语句是block那么不需要缩进
			&& !(ctx.getParent() instanceof SysYParser.StmtContext//简单处理else if，让else 后的if不缩进 此处有问题
			&& ((SysYParser.StmtContext) ctx.getParent()).ELSE() != null
			&& ctx.getChild(0).getText().equals("if"))){
//			&& ctx.getParent().getChild(0) instanceof TerminalNode)) {
				PrintRetraction();
			}
		}
		if (JudgeBehindIfSingle(ctx)){
			retractionNum++;
		}
		if (JudgeBehindWhileSingle(ctx)){
			retractionNum++;
		}
//		else if (JudgeBehindElseSingle(ctx)){
//			retractionNum++;
//		}//会对大stmt的else换行造成错误
		if (!(ctx.getChild(0) instanceof SysYParser.BlockContext)){
			System.out.print("\u001B[97m"); //White
			colorTemp = "\u001B[97m";
			visitChildren(ctx);
			System.out.print("\u001B[39m");
			colorTemp = "\u001B[39m";
		} else {
			visitChildren(ctx);
		}
//		if (JudgeIsIfBackSingle(ctx)
//		&& ctx.getParent() instanceof SysYParser.StmtContext
//		&& ((SysYParser.StmtContext) ctx.getParent()).ELSE() == null){
//			retractionNum--;//只有if没有else的缩进减1 不会影响else//好像和else没有关系呢
//		}//有问题，只有在整个stmt结束之前才会减去缩进//想错了，没事的
		if (JudgeIsIfBackSingle(ctx)){
			retractionNum--;//只要是if+if后single就要减去
		}
		if (JudgeIsWhileBackSingle(ctx)){
			retractionNum--;
		}
		if (JudgeIsElseBackSingle(ctx)){
			retractionNum--;
		}
//		if (JudgeIsIfBackSingle(ctx)){
//			retractionNum--;
//			if (ctx.getParent() instanceof SysYParser.StmtContext
//			&&JudgeBehindElseSingle((SysYParser.StmtContext) ctx.getParent())){
//				retractionNum++;
//			}//有问题，会影响else的输出，应该迁移到else后面判断
//		}
//		if (JudgeIsElseBackSingle(ctx)){
//			retractionNum--;
//		}
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
		IsOutput = true;
		return null;
	}

	@Override
	public Void visitFuncDef(SysYParser.FuncDefContext ctx) {
		//int lineNumber = ctx.getStart().getLine();
//		if (lineNumber != 1){
		if (IsOutput){
			//System.out.println();
			PrintLineBreak();
		}
		visitChildren(ctx);
		IsOutput = true;
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
			}else {//单独代码块中的左花括号
				for (int i = 0; i<retractionNum-1;i++){
					PrintRetraction();
				}
				System.out.print(COLORS[colorIndex]);
				System.out.print(text);
				//System.out.println();
				PrintLineBreak();
			}
		}else {//声明语句的左花括号
			System.out.print(COLORS[colorIndex]);
			System.out.print(text);
		}
	}

	private void RBrace(TerminalNode node){
		if (node.getParent() instanceof SysYParser.BlockContext) {//代码块的右花括号要缩进
			for (int i = 0; i < retractionNum - 1; i++) {
				PrintRetraction();
			}
		}
		String text = node.getText();
		if (node.getParent() instanceof SysYParser.BlockContext
		&& node.getParent().getParent() != null
		&& node.getParent().getParent() instanceof SysYParser.FuncDefContext){//判断是否是函数定义中的括号
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

	private boolean JudgeBehindWhileSingle(SysYParser.StmtContext ctx){//判断while后是否是single
		if (ctx.getChildCount() >= 5 && ctx.getChild(0).getText().equals("while")){//是while语句
			if (ctx.getChild(4) instanceof SysYParser.StmtContext
					&& ((SysYParser.StmtContext) ctx.getChild(4)).block() == null){//是非block语句
				return true;
			}
		}
		return false;
	}

	private boolean JudgeBehindIfSingle(SysYParser.StmtContext ctx){//判断if后是否是single
		if (ctx.getChildCount() >= 5 && ctx.getChild(0).getText().equals("if")){//是if语句
			if (ctx.getChild(4) instanceof SysYParser.StmtContext
			&& ((SysYParser.StmtContext) ctx.getChild(4)).block() == null){//是非block语句
				return true;
			}
		}
		return false;
	}

	private boolean JudgeBehindElseSingle(SysYParser.StmtContext ctx){//判断else后是否是single
		if (ctx.getChildCount() >= 7 && ctx.getChild(5).getText().equals("else")){//是else语句
			if (ctx.getChild(6) instanceof SysYParser.StmtContext
					&& ((SysYParser.StmtContext) ctx.getChild(6)).block() == null){//是非block语句
				return true;
			}
		}
		return false;
	}

	private boolean JudgeIsWhileBackSingle(SysYParser.StmtContext ctx) {//判断是否是while的后single
		if (ctx.block() == null) {//非block
			ParseTree parent = ctx.getParent();
			if (parent instanceof SysYParser.StmtContext
					&& ((SysYParser.StmtContext) parent).WHILE() != null
					&& parent.getChild(4).equals(ctx)) {
				return true;
			}
		}
		return false;
	}

	private boolean JudgeIsIfBackSingle(SysYParser.StmtContext ctx) {//判断是否是if的后single
		if (ctx.block() == null) {//非block
			ParseTree parent = ctx.getParent();
			if (parent instanceof SysYParser.StmtContext
					&& ((SysYParser.StmtContext) parent).IF() != null
					&& parent.getChild(4).equals(ctx)) {
				return true;
			}
		}
		return false;
	}

	private boolean JudgeIsElseBackSingle(SysYParser.StmtContext ctx) {//判断是否是else的后single
		if (ctx.block() == null && ctx.IF() == null) {//非block非if
			ParseTree parent = ctx.getParent();
			if (parent instanceof SysYParser.StmtContext
					&& ((SysYParser.StmtContext) parent).ELSE() != null
					&& parent.getChild(6).equals(ctx)) {
				return true;
			}
		}
		return false;
	}
}
