import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.util.List;

public class Main
{
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("input path is required");
        }
        String source = args[0];
        CharStream input = CharStreams.fromFileName(source);
        SysYLexer sysYLexer = new SysYLexer(input);
        myErrorListener myListener = new myErrorListener();
        CommonTokenStream tokens = new CommonTokenStream(sysYLexer);
        SysYParser sysYParser = new SysYParser(tokens);
        sysYParser.removeErrorListeners();
        sysYParser.addErrorListener(myListener);
        ParseTree tree = sysYParser.program();
        SysYParserBaseVisitor visitor = new SysYParserBaseVisitor();
        visitor.visit(tree);
        if (!myListener.getError()) {

        }

    }


}