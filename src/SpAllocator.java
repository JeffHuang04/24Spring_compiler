import org.bytedeco.llvm.LLVM.LLVMBasicBlockRef;
import org.bytedeco.llvm.LLVM.LLVMValueRef;

import static org.bytedeco.llvm.global.LLVM.*;
import static org.bytedeco.llvm.global.LLVM.LLVMStore;

public class SpAllocator implements RegisterAllocator{

	@Override
	public void allocate(LLVMValueRef value) {

	}

	@Override
	public int getStackSize(LLVMValueRef func) {
		int num = 0;
		for (LLVMBasicBlockRef bb = LLVMGetFirstBasicBlock(func); bb != null; bb = LLVMGetNextBasicBlock(bb)) {
			for (LLVMValueRef inst = LLVMGetFirstInstruction(bb); inst != null; inst = LLVMGetNextInstruction(inst)) {
				if (LLVMGetInstructionOpcode(inst) == LLVMRet
						|| LLVMGetInstructionOpcode(inst) == LLVMStore){
					continue;
				}
				num++;
			}
		}
		return num;
	}
}
