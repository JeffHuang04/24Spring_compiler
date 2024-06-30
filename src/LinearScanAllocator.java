import org.bytedeco.llvm.LLVM.*;

import java.util.*;

import static org.bytedeco.llvm.global.LLVM.*;
public class LinearScanAllocator {
	private LifeTimeAnalysis lifeTimeAnalysis;
	private List<Interval> intervals = new ArrayList<>();
	private Map<LLVMValueRef, String> registerMap = new HashMap<>();
	private SpAllocator spAllocator;
	private static final List<String> AVAILABLE_REGISTERS = Arrays.asList(
			"s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11",
			"a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7",
			"t2", "t3", "t4", "t5", "t6"
	);//t0、t1保留
	private class Interval {
		LLVMValueRef value;
		int start;
		int end;
		Interval(LLVMValueRef value, int start, int end) {
			this.value = value;
			this.start = start;
			this.end = end;
		}
	}
	public LinearScanAllocator(LifeTimeAnalysis LifeTimeAnalysis, SpAllocator spAllocator) {
		this.lifeTimeAnalysis = LifeTimeAnalysis;
		this.spAllocator = spAllocator;
	}
	public void allocateRegisters(LLVMValueRef func){
		genIntervals(func);
		intervals.sort(Comparator.comparingInt(i -> i.start));//按照生命周期起点排序
		List<String> freeRegisters = new ArrayList<>(AVAILABLE_REGISTERS);
		List<Interval> active = new ArrayList<>();//活跃区间
		for (Interval interval: intervals) {
			expireOldIntervals(active, interval,freeRegisters);
			if (active.size() == AVAILABLE_REGISTERS.size()){
				spillAtInterval(interval,active);//溢出到栈上
			}else {
				registerMap.put(interval.value,freeRegisters.remove(0));//从空闲寄存器列表中选择一个
				addIntervalSortedByEndPoint(active,interval);
			}
		}
	}
	/*
	* 获得每一个寄存器的活跃区间
	* */
	public void genIntervals(LLVMValueRef func){
		for (LLVMBasicBlockRef bb = LLVMGetFirstBasicBlock(func); bb != null; bb = LLVMGetNextBasicBlock(bb)) {
			for (LLVMValueRef inst = LLVMGetFirstInstruction(bb); inst != null; inst = LLVMGetNextInstruction(inst)) {
				if (LLVMGetInstructionOpcode(inst) != LLVMStore) {//store的返回值没有用处
					int startPoint = lifeTimeAnalysis.getStartPoint(inst);
					int endPoint = lifeTimeAnalysis.getEndPoint(inst);
					intervals.add(new Interval(inst, startPoint, endPoint));
				}
			}
		}
	}
	/*
	* 移除到期的活跃节点，归还寄存器
	* */
	private void expireOldIntervals(List<Interval> active, Interval interval, List<String> freeRegisters) {
		Iterator<Interval> iterator = active.iterator();
		//添加时满足endIncreasing条件
		while (iterator.hasNext()) {//遍历active列表中的每一个区间
			Interval j = iterator.next();
			if (j.end >= interval.start) {
				return;
			}//如果当中的某一个节点的结束节点大于等于当前的开始节点则退出
			iterator.remove();
			freeRegisters.add(registerMap.get(j.value));//不应该删掉
		}
	}//迭代器使用参考chatgpt

	private void spillAtInterval(Interval interval, List<Interval> active){
		Interval spill = active.get(active.size() - 1);
		if (spill.end > interval.end){
			registerMap.put(interval.value, registerMap.remove(spill.value));
			spAllocator.allocate2(spill.value);
			active.remove(spill);
			addIntervalSortedByEndPoint(active, interval);
		} else {
			spAllocator.allocate2(interval.value);
		}
	}
	/*
	* 将活跃区间按照end升序排列
	* */
	private void addIntervalSortedByEndPoint(List<Interval> active, Interval interval) {
		int index = 0;
		for (int i = 0; i < active.size();i++){
			if (active.get(index).end < interval.end){
				index++;
			}else {
				break;
			}
		}
		active.add(index, interval);
	}

	public String getRegister(LLVMValueRef value) {
		return registerMap.getOrDefault(value, "sp"); // 默认返回栈指针，表示变量在栈上
	}
}
