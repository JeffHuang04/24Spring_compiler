import org.bytedeco.llvm.LLVM.LLVMValueRef;

public interface RegisterAllocator {
	void allocate(LLVMValueRef value);

	int getStackSize(LLVMValueRef func);
}
