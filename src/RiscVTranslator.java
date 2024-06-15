import org.bytedeco.llvm.LLVM.*;
import org.bytedeco.llvm.global.LLVM;

import java.util.Objects;
import java.util.Set;

import static org.bytedeco.llvm.global.LLVM.*;
import static org.bytedeco.llvm.global.LLVM.LLVMGetFirstGlobal;

public class RiscVTranslator {
	private LLVMModuleRef module;
	private AsmBuilder asmBuilder;
	private SpAllocator allocator;
	public RiscVTranslator(LLVMModuleRef module){
		this.module = module;
		this.asmBuilder = new AsmBuilder();
		this.allocator = new SpAllocator();
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
			int spSpace = allocator.getStackSize(func);
			spSpace = spSpace*(-1);
			asmBuilder.addi("sp","sp",String.valueOf(spSpace));
			for (LLVMBasicBlockRef bb = LLVMGetFirstBasicBlock(func); bb != null; bb = LLVMGetNextBasicBlock(bb)) {
				String bbName = LLVMGetBasicBlockName(bb).getString();
				asmBuilder.label(bbName);
				for (LLVMValueRef inst = LLVMGetFirstInstruction(bb); inst != null; inst = LLVMGetNextInstruction(inst)) {
					if (LLVMGetInstructionOpcode(inst) == LLVMRet){
						LLVMValueRef returnValue = LLVMGetOperand(inst,0);
						if (LLVMIsAConstantInt(returnValue) != null) {//ret返回的是立即数
							int returnValueInt = (int) LLVMConstIntGetSExtValue(returnValue);
							asmBuilder.li("a0", returnValueInt);
						}else {
							int value = allocator.findOffset(returnValue);
							asmBuilder.lw("a0", value, "sp");
						}
						asmBuilder.addi("sp","sp",String.valueOf(spSpace*(-1)));
						asmBuilder.li("a7",93);
						asmBuilder.ecall();
					} else if (LLVMGetInstructionOpcode(inst) == LLVMAlloca){
						allocator.allocate(inst);
					} else if (LLVMGetInstructionOpcode(inst) == LLVMStore){//2个参数
						LLVMValueRef op1 = LLVMGetOperand(inst, 0);
						if (LLVMIsAConstantInt(op1) != null){//是立即数
							int ValueInt = (int) LLVMConstIntGetSExtValue(op1);
							asmBuilder.li("t0", ValueInt);
						} else {//是寄存器
							int value1 = allocator.findOffset(op1);
							asmBuilder.lw("t0",value1,"sp");
						}//op1不可能是全局变量，op1相当于右值，而全局变量当右值的时候会先load
						LLVMValueRef op2 = LLVMGetOperand(inst, 1);
						if (LLVMIsAGlobalValue(op2)!=null){//是全局变量
							String globalVarName = LLVMGetValueName(op2).getString();
							asmBuilder.la("t1",globalVarName);
							asmBuilder.sw("t0",0,"t1");
						}else {//是寄存器
							int value = allocator.findOffset(op2);
							asmBuilder.sw("t0", value, "sp");
						}
					} else if (LLVMGetInstructionOpcode(inst) == LLVMLoad) {//1个参数
						allocator.allocate(inst);
						LLVMValueRef op1 = LLVMGetOperand(inst, 0);
						if (LLVMIsAConstantInt(op1) != null){//是立即数
							int ValueInt = (int) LLVMConstIntGetSExtValue(op1);
							asmBuilder.li("t0", ValueInt);
						} else if (LLVMIsAGlobalValue(op1)!=null) {//是全局变量
							String globalVarName = LLVMGetValueName(op1).getString();
							asmBuilder.la("t0", globalVarName);
							asmBuilder.lw("t0", 0, "t0");
						} else {//是寄存器
							int value1 = allocator.findOffset(op1);
							asmBuilder.lw("t0", value1, "sp");
						}
						int valueFinal = allocator.findOffset(inst);
						asmBuilder.sw("t0",valueFinal,"sp");
					} else if (LLVMGetInstructionOpcode(inst) == LLVMAdd||
					LLVMGetInstructionOpcode(inst) == LLVMSub||
					LLVMGetInstructionOpcode(inst) == LLVMMul||
					LLVMGetInstructionOpcode(inst) == LLVMSDiv||
					LLVMGetInstructionOpcode(inst) == LLVMSRem){
						allocator.allocate(inst);
						for(int i = 0; i < 2;i ++){
							LLVMValueRef opi = LLVMGetOperand(inst,i);
							if (LLVMIsAConstantInt(opi) != null){//是立即数
								int ValueInt = (int) LLVMConstIntGetSExtValue(opi);
								asmBuilder.li("t"+i, ValueInt);
							}else {//是寄存器，不可能是全局变量，全局变量先要load成寄存器
								int valuei = allocator.findOffset(opi);
								asmBuilder.lw("t"+i,valuei,"sp");
							}
						}
						if (LLVMGetInstructionOpcode(inst) == LLVMAdd) {
							asmBuilder.op2("add","t0", "t0", "t1");
						} else if (LLVMGetInstructionOpcode(inst) == LLVMSub) {
							asmBuilder.op2("sub","t0", "t0", "t1");
						} else if (LLVMGetInstructionOpcode(inst) == LLVMMul) {
							asmBuilder.op2("mul","t0", "t0", "t1");
						} else if (LLVMGetInstructionOpcode(inst) == LLVMSDiv){
							asmBuilder.op2("sdiv","t0", "t0", "t1");
						} else if (LLVMGetInstructionOpcode(inst) == LLVMSRem) {
							asmBuilder.op2("srem","t0", "t0", "t1");
						}
						int valueFinal = allocator.findOffset(inst);
						asmBuilder.sw("t0",valueFinal,"sp");
					}
				}
			}
		}
	}
	public String getAsm(){
		return asmBuilder.getRiscV();
	}

}
