import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.server.ExportException;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.llvm.LLVM.LLVMModuleRef;

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
        //String llvm = args[2];
        CharStream input = CharStreams.fromFileName(source);
        SysYLexer sysYLexer = new SysYLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(sysYLexer);
        SysYParser sysYParser = new SysYParser(tokens);
        ParseTree tree = sysYParser.program();
        myVisitor visitor = new myVisitor();
        visitor.visit(tree);
//        if (LLVMPrintModuleToFile(myVisitor.getModule(), llvm, error) != 0) {    // module是你自定义的LLVMModuleRef对象
//            LLVMDisposeMessage(error);
//        }
        LLVMModuleRef module = myVisitor.getModule();
        RiscVTranslatorSp translator = new RiscVTranslatorSp(module);//仅用栈来分配变量
        //RiscVTranslatorLinearScan translator = new RiscVTranslatorLinearScan(module);//线性寄存器分配算法
        translator.translate();
        String asm = translator.getAsm();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {
            writer.write(asm);
        }
    }
}