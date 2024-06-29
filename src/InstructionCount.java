import org.bytedeco.llvm.LLVM.*;
import java.util.HashMap;
import java.util.Map;

import static org.bytedeco.llvm.global.LLVM.*;
import static org.bytedeco.llvm.global.LLVM.LLVMGetNextInstruction;

public class InstructionCount {
	private Map<LLVMValueRef, Integer> instructionCountMap = new HashMap<>();
	private int currentCount = 1;

	public void countInstructions(LLVMValueRef func) {
		for (LLVMBasicBlockRef bb = LLVMGetFirstBasicBlock(func); bb != null; bb = LLVMGetNextBasicBlock(bb)) {
			for (LLVMValueRef inst = LLVMGetFirstInstruction(bb); inst != null; inst = LLVMGetNextInstruction(inst)) {
				instructionCountMap.put(inst, currentCount++);//给每一条指令进行编号
			}
		}
	}
	public int getInstructionCount(LLVMValueRef inst) {
		return instructionCountMap.getOrDefault(inst, -1);//获得该指令的行号
	}
}
