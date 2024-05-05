import java.util.Map;
import java.util.Stack;

public class SymbolTableStack {
	private Stack<SymbolTable> stack;
	public SymbolTableStack() {
		this.stack = new Stack<>();
		pushScope();//建立全局作用域
	}
	public Map<String, Type> getNowTable(){
		return stack.peek().getTable();
	}
	public void pushScope() {
		stack.push(new SymbolTable());
	}
	public void popScope() {
		if (!stack.empty()){
			stack.pop();
		}
	}
	public void put(String name, Type type){
		if (type instanceof FunctionType){
			stack.get(0).addSymbol(name,type);
		}else {
			if (!stack.empty()) {
				stack.peek().addSymbol(name, type);
			}
		}
	}
	public Type findAll(String name){
		for (int i = stack.size()-1; i >= 0;i--){
			Type type = stack.get(i).find(name);
			if (type != null){
				return type;
			}
		}
		return null;
	}//查找该作用域以及父作用域是否有该元素(用于使用)
	public Type findNow(String name){
		return stack.peek().find(name);
	}//查找该作用域是否有该元素(用于定义)
	public Type findNowFuncTy(){
		Map<String,Type> tmp = stack.get(0).getTable();
		Type lastVale = null;
		for (Type value : tmp.values()){
			lastVale = value;
		}
		return lastVale;
	}
	public Type findFuncTy(String name){
		return stack.get(0).find(name);
	}
}
