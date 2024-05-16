import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
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
	public LLVMValueRef visitVarDef(SysYParser.VarDefContext ctx) {
		String varName = ctx.IDENT().getText();
		if (ctx.parent.parent.parent.parent instanceof SysYParser.CompUnitContext){//全局变量

		}
		return super.visitVarDef(ctx);
	}

	@Override
	public LLVMValueRef visitConstDef(SysYParser.ConstDefContext ctx) {

		return super.visitConstDef(ctx);
	}

	@Override
	public LLVMValueRef visitFuncDef(SysYParser.FuncDefContext ctx) {
		String funcName = ctx.IDENT().getText();
		LLVMTypeRef returnType = i32Type;
		LLVMTypeRef ft = LLVMFunctionType(returnType, (LLVMTypeRef) null, 0, 0);//默认只有main且main无参数
		LLVMValueRef function = LLVMAddFunction(module, funcName, ft);
		visit(ctx.block());
		return null;
	}

	@Override
	public LLVMValueRef visitBlock(SysYParser.BlockContext ctx) {

		return null;
	}
}
