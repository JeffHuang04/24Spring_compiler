import org.bytedeco.llvm.LLVM.*;

import java.util.Objects;

import static org.bytedeco.llvm.global.LLVM.*;
import static org.bytedeco.llvm.global.LLVM.LLVMGetFirstGlobal;
public class RiscVTranslatorLinearScan {
	private LLVMModuleRef module;
	private AsmBuilder asmBuilder;
	private LinearScanAllocator allocatorLs;
	private SpAllocator allocatorSp;
	private InstructionCount instructionCount;
	private LifeTimeAnalysis LifeTimeAnalysis;

	public RiscVTranslatorLinearScan(LLVMModuleRef module){
		this.module = module;
		this.allocatorSp = new SpAllocator();
		this.asmBuilder = new AsmBuilder();
		this.instructionCount = new InstructionCount();
		this.LifeTimeAnalysis = new LifeTimeAnalysis(instructionCount);
		this.allocatorLs = new LinearScanAllocator(LifeTimeAnalysis,allocatorSp);
	}
	public void translate(){
		for (LLVMValueRef value = LLVMGetFirstGlobal(module); value != null; value = LLVMGetNextGlobal(value)) {
			if (LLVMIsAGlobalValue(value) != null){
				String globalVarName = LLVMGetValueName(value).getString();
				LLVMValueRef initializer = LLVMGetInitializer(value);
				int initValue = (initializer != null && LLVMIsAConstantInt(initializer) != null) ?
						(int) LLVMConstIntGetSExtValue(initializer) : 0;//初始化参考了chatgpt
				asmBuilder.data();
				asmBuilder.label(globalVarName);
				asmBuilder.word(initValue);
				asmBuilder.nextLine();
			}
		}
		for (LLVMValueRef func = LLVMGetFirstFunction(module); func != null; func = LLVMGetNextFunction(func)) {
			instructionCount.countInstructions(func);
			LifeTimeAnalysis.cmpLifeTime(func);
			allocatorLs.allocateRegisters(func);//分配好寄存器
			String funcName = LLVMGetValueName(func).getString();
			asmBuilder.text();
			if (Objects.equals(funcName, "main")) {
				asmBuilder.global(funcName);
			}
			asmBuilder.label(funcName);
			int spSpace = allocatorSp.getStackSize2(func);
			spSpace = spSpace * (-1);
			asmBuilder.addi("sp", "sp", String.valueOf(spSpace));
			for (LLVMBasicBlockRef bb = LLVMGetFirstBasicBlock(func); bb != null; bb = LLVMGetNextBasicBlock(bb)) {
				String bbName = LLVMGetBasicBlockName(bb).getString();
				asmBuilder.label(bbName);
				for (LLVMValueRef inst = LLVMGetFirstInstruction(bb); inst != null; inst = LLVMGetNextInstruction(inst)) {
					if (LLVMGetInstructionOpcode(inst) == LLVMRet){
					} else if (LLVMGetInstructionOpcode(inst) == LLVMStore) {//两个参数
						LLVMValueRef op1 = LLVMGetOperand(inst, 0);
						if (LLVMIsAConstantInt(op1) != null){//是立即数
							int ValueInt = (int) LLVMConstIntGetSExtValue(op1);
							asmBuilder.li("t0", ValueInt);
						}

					} else if (LLVMGetInstructionOpcode(inst) == LLVMLoad) {
						LLVMValueRef op1 = LLVMGetOperand(inst, 0);
						if (LLVMIsAConstantInt(op1) != null){//是立即数
							int ValueInt = (int) LLVMConstIntGetSExtValue(op1);
							asmBuilder.li("t0", ValueInt);
						} else if (LLVMIsAGlobalValue(op1)!=null) {//是全局变量
							String globalVarName = LLVMGetValueName(op1).getString();
							asmBuilder.la("t0", globalVarName);
							asmBuilder.lw("t0", 0, "t0");
						}
					}
				}
			}
		}
	}
	public String getAsm(){
		return asmBuilder.getRiscV();
	}
}
