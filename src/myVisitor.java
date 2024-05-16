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
	public LLVMValueRef visitDecl(SysYParser.DeclContext ctx) {
		String varDeclName = ctx.getText();
		if (ctx.parent instanceof SysYParser.CompUnitContext){//全局变量
			LLVMValueRef globalVar = LLVMAddGlobal(module, i32Type, /*globalVarName:String*/varDeclName);
		}
		return super.visitDecl(ctx);
	}

	@Override
	public LLVMValueRef visitVarDef(SysYParser.VarDefContext ctx) {
		return super.visitVarDef(ctx);
	}

	@Override
	public LLVMValueRef visitConstDecl(SysYParser.ConstDeclContext ctx) {
		return super.visitConstDecl(ctx);
	}
}
