import org.bytedeco.llvm.LLVM.*;
import org.bytedeco.llvm.global.LLVM;

import java.util.Objects;

import static org.bytedeco.llvm.global.LLVM.*;
import static org.bytedeco.llvm.global.LLVM.LLVMGetFirstGlobal;

public class RiscVTranslator {
	private LLVMModuleRef module;
	private AsmBuilder asmBuilder;

	public RiscVTranslator(LLVMModuleRef module){
		this.module = module;
		this.asmBuilder = new AsmBuilder();
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
			String funcName = LLVMGetValueName(func).getString();
			asmBuilder.text();
			if (Objects.equals(funcName, "main")){
				asmBuilder.global(funcName);
			}
			asmBuilder.label(funcName);
			asmBuilder.word(cmpVarNum(func));
			for (LLVMBasicBlockRef bb = LLVMGetFirstBasicBlock(func); bb != null; bb = LLVMGetNextBasicBlock(bb)) {
				String bbName = LLVMGetBasicBlockName(bb).getString();
				asmBuilder.label(bbName);
			}
		}
	}
	private int cmpVarNum(LLVMValueRef func){
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
	public String getAsm(){
		return asmBuilder.getRiscV();
	}

}
