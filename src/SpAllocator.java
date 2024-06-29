import org.bytedeco.llvm.LLVM.LLVMBasicBlockRef;
import org.bytedeco.llvm.LLVM.LLVMValueRef;

import java.util.HashMap;
import java.util.Map;

import static org.bytedeco.llvm.global.LLVM.*;
import static org.bytedeco.llvm.global.LLVM.LLVMStore;

public class SpAllocator {
	private int stackSize = 0;
	private int offset = 0;
	private int numLs = 0;
	private Map<LLVMValueRef, Integer> stackOffsets = new HashMap<>();
	public void allocate1(LLVMValueRef value) {
		stackOffsets.put(value, offset);
		offset -= 4;
	}
	public void allocate2(LLVMValueRef value) {
		numLs++;
		stackOffsets.put(value, offset);
		offset += 4;
	}
	public int findOffset1(LLVMValueRef value){
		return stackOffsets.get(value);
//		Integer offset = stackOffsets.get(value);
//		if (offset == null) {
//			return -1; // 返回 -1 表示未找到
//		}
//		return offset;
	}
	public int findOffset2(LLVMValueRef value){
		return stackSize - 4 - stackOffsets.get(value);
	}
	public int getStackSize1(LLVMValueRef func) {
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
		int spSpace = num*4;
		if (spSpace % 16 != 0) {
			spSpace = ((spSpace / 16) + 1) * 16;
		}
		stackSize = spSpace;
		offset = stackSize - 4;
		return stackSize;
	}

	public int getStackSize2(LLVMValueRef func) {
		int spSpace = numLs*4;
		if (spSpace % 16 != 0) {
			spSpace = ((spSpace / 16) + 1) * 16;
		}
		stackSize = spSpace;
		return stackSize;
	}
}
