import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import java.io.IOException;
import java.rmi.server.ExportException;

import org.bytedeco.javacpp.BytePointer;

import static org.bytedeco.llvm.global.LLVM.LLVMDisposeMessage;
import static org.bytedeco.llvm.global.LLVM.LLVMPrintModuleToFile;

public class Main
{
    public static final BytePointer error = new BytePointer();
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("input path is required");
        }
        String source = args[0];
        String output = args[1];
        CharStream input = CharStreams.fromFileName(source);
        SysYLexer sysYLexer = new SysYLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(sysYLexer);
        SysYParser sysYParser = new SysYParser(tokens);
        ParseTree tree = sysYParser.program();
        myVisitor visitor = new myVisitor();
        visitor.visit(tree);
//        throw new Exception(tree.getText());
        if (LLVMPrintModuleToFile(myVisitor.getModule(), output, error) != 0) {    // module是你自定义的LLVMModuleRef对象
            LLVMDisposeMessage(error);
        }

    }
}