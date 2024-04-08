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
		System.out.println(node.getSymbol().getType());
		return null;
//		switch (node.getSymbol().getType()){
//
//		}
//		return super.visitTerminal(node);
	}
}
