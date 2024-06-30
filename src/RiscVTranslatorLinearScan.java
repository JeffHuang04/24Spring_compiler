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
						LLVMValueRef returnValue = LLVMGetOperand(inst,0);
						if (LLVMIsAConstantInt(returnValue) != null) {//ret返回的是立即数
							int returnValueInt = (int) LLVMConstIntGetSExtValue(returnValue);
							asmBuilder.li("a0", returnValueInt);
						}else {//是寄存器
							String reg = allocatorLs.getRegister(returnValue);
							int regValue = 0;
							if (reg.equals("sp")){
								regValue = allocatorSp.findOffset2(returnValue);
							}
							if (reg.equals("sp")){
								asmBuilder.lw("a0", regValue, "sp");
							}else {
								asmBuilder.mv("a0",reg);
							}
						}
						asmBuilder.addi("sp","sp",String.valueOf(spSpace*(-1)));
						asmBuilder.li("a7",93);
						asmBuilder.ecall();
					} else if (LLVMGetInstructionOpcode(inst) == LLVMStore) {//两个参数
						LLVMValueRef op1 = LLVMGetOperand(inst, 0);
						LLVMValueRef op2 = LLVMGetOperand(inst, 1);
						if (LLVMIsAConstantInt(op1) != null){//op1是立即数
							int ValueInt = (int) LLVMConstIntGetSExtValue(op1);
							asmBuilder.li("t0", ValueInt);
							if (LLVMIsAGlobalValue(op2)!=null){//op2是全局变量
								String globalVarName = LLVMGetValueName(op2).getString();
								asmBuilder.la("t1",globalVarName);
								asmBuilder.sw("t0",0,"t1");
							}else {//op2是寄存器
								String reg2 = allocatorLs.getRegister(op2);
								int reg2Value = 0;
								if (reg2.equals("sp")){
									reg2Value = allocatorSp.findOffset2(op2);
								}
								if (reg2.equals("sp")){
									asmBuilder.sw("t0",reg2Value,"sp");
								}else {
									asmBuilder.mv(reg2,"t0");
								}
							}
						}else {//op1是寄存器
							String reg1 = allocatorLs.getRegister(op1);
							int reg1Value = 0;
							if (reg1.equals("sp")){
								reg1Value = allocatorSp.findOffset2(op1);
							}
							if (LLVMIsAGlobalValue(op2)!=null){//op2是全局变量
								String globalVarName = LLVMGetValueName(op2).getString();
								if (reg1.equals("sp")){
									asmBuilder.lw("t0",reg1Value,"sp");
									asmBuilder.la("t1",globalVarName);
									asmBuilder.sw("t0",0,"t1");
								}else {
									asmBuilder.la("t1",globalVarName);
									asmBuilder.sw(reg1,0,"t1");
								}
							}else {//op2是寄存器
								String reg2 = allocatorLs.getRegister(op2);
								int reg2Value = 0;
								if (reg2.equals("sp")){
									reg2Value = allocatorSp.findOffset2(op2);
								}
								if (reg1.equals("sp") && reg2.equals("sp")){
									asmBuilder.lw("t0", reg1Value,"sp");
									asmBuilder.sw("t0", reg2Value, "sp");
								} else if (reg1.equals("sp") && !reg2.equals("sp")) {
									asmBuilder.lw("t0", reg1Value,"sp");
									asmBuilder.mv(reg2,"t0");
								} else if (!reg1.equals("sp") && reg2.equals("sp")) {
									asmBuilder.sw(reg1, reg2Value,"sp");
								} else if (!reg1.equals("sp") && !reg2.equals("sp")){
									asmBuilder.mv(reg2,reg1);
								}
							}
						}//op1不可能是全局变量，op1相当于右值，而全局变量当右值的时候会先load
					} else if (LLVMGetInstructionOpcode(inst) == LLVMLoad) {
						String regInst = allocatorLs.getRegister(inst);
						int regInstValue = 0;
						if (regInst.equals("sp")){
							regInstValue = allocatorSp.findOffset2(inst);
						}
						LLVMValueRef op1 = LLVMGetOperand(inst, 0);
						if (LLVMIsAGlobalValue(op1)!=null) {//是全局变量
							String globalVarName = LLVMGetValueName(op1).getString();
							asmBuilder.la("t0", globalVarName);
							asmBuilder.lw("t0", 0, "t0");
							if (regInst.equals("sp")){
								asmBuilder.sw("t0",regInstValue,"sp");
							}else {
								asmBuilder.mv(regInst,"t0");
							}
						}else {//是寄存器
							String reg1 = allocatorLs.getRegister(op1);
							int reg1Value = 0;
							if (reg1.equals("sp")){
								reg1Value = allocatorSp.findOffset2(op1);
							}
							if (regInst.equals("sp")&&reg1.equals("sp")){
								asmBuilder.lw("t0", reg1Value, "sp");
								asmBuilder.sw("t0", regInstValue,"sp");
							} else if (regInst.equals("sp")&&!reg1.equals("sp")) {
								asmBuilder.sw(reg1,regInstValue,"sp");
							} else if (!regInst.equals("sp")&&reg1.equals("sp")) {
								asmBuilder.lw("t0", reg1Value, "sp");
								asmBuilder.mv(regInst,"t0");
							} else if (!regInst.equals("sp")&&!reg1.equals("sp")){
								asmBuilder.mv(regInst,reg1);
							}
						}
					}else if (LLVMGetInstructionOpcode(inst) == LLVMAdd||
							LLVMGetInstructionOpcode(inst) == LLVMSub||
							LLVMGetInstructionOpcode(inst) == LLVMMul||
							LLVMGetInstructionOpcode(inst) == LLVMSDiv||
							LLVMGetInstructionOpcode(inst) == LLVMSRem){
						for(int i = 0; i < 2;i ++){
							LLVMValueRef opi = LLVMGetOperand(inst,i);
							if (LLVMIsAConstantInt(opi) != null){//是立即数
								int ValueInt = (int) LLVMConstIntGetSExtValue(opi);
								asmBuilder.li("t"+i, ValueInt);
							}else {//是寄存器，不可能是全局变量，全局变量先要load成寄存器
								String regi = allocatorLs.getRegister(opi);
								int regiValue = 0;
								if (regi.equals("sp")){
									regiValue = allocatorSp.findOffset2(opi);
								}
								if (regi.equals("sp")) {
									asmBuilder.lw("t" + i, regiValue, "sp");
								}else {
									asmBuilder.mv("t" + i, regi);
								}
							}
						}
						if (LLVMGetInstructionOpcode(inst) == LLVMAdd) {
							asmBuilder.op2("add","t0", "t0", "t1");
						} else if (LLVMGetInstructionOpcode(inst) == LLVMSub) {
							asmBuilder.op2("sub","t0", "t0", "t1");
						} else if (LLVMGetInstructionOpcode(inst) == LLVMMul) {
							asmBuilder.op2("mul","t0", "t0", "t1");
						} else if (LLVMGetInstructionOpcode(inst) == LLVMSDiv){
							asmBuilder.op2("div","t0", "t0", "t1");
						} else if (LLVMGetInstructionOpcode(inst) == LLVMSRem) {
							asmBuilder.op2("rem","t0", "t0", "t1");
						}
						String regInst = allocatorLs.getRegister(inst);
						int regInstValue = 0;
						if (regInst.equals("sp")){
							regInstValue = allocatorSp.findOffset2(inst);
						}
						if (regInst.equals("sp")){
							asmBuilder.sw("t0",regInstValue,"sp");
						}else {
							asmBuilder.mv(regInst,"t0");
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
