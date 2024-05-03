import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SymbolTable {
	private Map<String,Type> table;
	public SymbolTable(){
		table = new LinkedHashMap<>();
	}//按照插入顺序保存元素
	public Map<String, Type> getTable() {
		return table;
	}
	public void addSymbol(String name, Type type){
		table.put(name,type);
	}
	public Type find(String name){
		return table.get(name);
	}
}//参考Lab讲解课件实现
