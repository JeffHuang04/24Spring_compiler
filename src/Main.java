import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

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
        sysYLexer.removeErrorListeners();
        sysYLexer.addErrorListener(myListener);
        List<? extends Token> myTokens = sysYLexer.getAllTokens();
        if (!myListener.getError()) {
            System.out.println(myTokens);
        }

    }

}