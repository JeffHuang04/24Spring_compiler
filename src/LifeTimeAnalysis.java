import org.bytedeco.llvm.LLVM.*;

import java.util.HashMap;
import java.util.Map;
import static org.bytedeco.llvm.global.LLVM.*;

public class LifeTimeAnalysis {
	private Map<LLVMValueRef, Integer> startPoints = new HashMap<>();
	private Map<LLVMValueRef, Integer> endPoints = new HashMap<>();
	private InstructionCount instructionCount;

	public LifeTimeAnalysis(InstructionCount instructionCount) {
		this.instructionCount = instructionCount;
	}
	public void cmpLifeTime(LLVMValueRef func){
		for (LLVMBasicBlockRef bb = LLVMGetFirstBasicBlock(func); bb != null; bb = LLVMGetNextBasicBlock(bb)) {
			for (LLVMValueRef inst = LLVMGetFirstInstruction(bb); inst != null; inst = LLVMGetNextInstruction(inst)) {
				int count = instructionCount.getInstructionCount(inst);
				for (int i = 0; i < LLVMGetNumOperands(inst);i++){
					LLVMValueRef op = LLVMGetOperand(inst,i);
					startPoints.putIfAbsent(op,count);//只计算第一次的
					endPoints.put(op,count);//永远更新中
				}
				startPoints.putIfAbsent(inst,count);
				endPoints.put(inst,count);
			}
		}
	}

	public int getStartPoint(LLVMValueRef value){
		return startPoints.getOrDefault(value,-1);
	}

	public int getEndPoint(LLVMValueRef value){
		return endPoints.getOrDefault(value,-1);
	}
}
