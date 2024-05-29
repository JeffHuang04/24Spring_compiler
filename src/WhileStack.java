import org.bytedeco.llvm.LLVM.LLVMBasicBlockRef;

import java.util.Stack;

public class WhileStack {
	private final Stack<WhileTable> stack;
	WhileStack(){
		this.stack = new Stack<>();
	}
	public void pushTable(LLVMBasicBlockRef whileCondBlock,LLVMBasicBlockRef whileEndBlock) {
		WhileTable newTable = new WhileTable();
		newTable.whileCondBlock = whileCondBlock;
		newTable.whileEndBlock = whileEndBlock;
		stack.push(newTable);
	}
	public void popTable() {
		if (!stack.empty()){
			stack.pop();
		}
	}
	public WhileTable peekTable(){
		return stack.peek();
	}
}
