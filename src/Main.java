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
//            System.out.println(myTokens);
            for (Token t : myTokens){
                int typeCode = t.getType();
                String text = t.getText();
                int line = t.getLine();
                String type = sysYLexer.getRuleNames()[typeCode-1];
                if (type.equals("INTEGER_CONST")&&text.length()>1){
                    if ((text.startsWith("0x")||text.startsWith("0X"))){
                        text = String.valueOf(Integer.parseInt(text.substring(2),16));
                    } else if (text.startsWith("0")) {
                        text = String.valueOf(Integer.parseInt(text.substring(1),8));
                    }
                }
                System.out.println(type + " " + text + " at Line " + line + ".");
            }
        }

    }


}