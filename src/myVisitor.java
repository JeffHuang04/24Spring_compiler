import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.llvm.LLVM.LLVMBuilderRef;
import org.bytedeco.llvm.LLVM.LLVMModuleRef;
import org.bytedeco.llvm.LLVM.LLVMTypeRef;
import org.bytedeco.llvm.LLVM.LLVMValueRef;
import static org.bytedeco.llvm.global.LLVM.*;


public class myVisitor extends SysYParserBaseVisitor<LLVMValueRef> {
	private static final LLVMModuleRef module = LLVMModuleCreateWithName("module");
	LLVMBuilderRef builder = LLVMCreateBuilder();
	LLVMTypeRef i32Type = LLVMInt32Type();

	myVisitor(){
		//初始化LLVM
		LLVMInitializeCore(LLVMGetGlobalPassRegistry());
		LLVMLinkInMCJIT();
		LLVMInitializeNativeAsmPrinter();
		LLVMInitializeNativeAsmParser();
		LLVMInitializeNativeTarget();
	}
	public static LLVMModuleRef getModule(){
		return module;
	}

	@Override
	public LLVMValueRef visitTerminal(TerminalNode node) {
        return null;
	}

	@Override
	public LLVMValueRef visitProgram(SysYParser.ProgramContext ctx) {
        return null;
	}

	@Override
	public LLVMValueRef visitCompUnit(SysYParser.CompUnitContext ctx) {
		return null;
	}

	@Override
	public LLVMValueRef visitDecl(SysYParser.DeclContext ctx) {
		return super.visitDecl(ctx);
	}
}
