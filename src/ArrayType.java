public class ArrayType extends Type{
	private Type contained;//感觉只有Int即可，多重数组直接用维度来算
//	private int numElements;//elements的数量
	private int dimension;//Array的维度
	public ArrayType(Type contained, /*int numElements,*/ int dimension){
		this.contained = contained;
//		this.numElements = numElements;
		this.dimension = dimension;
	}
//	public int getNumElements() {
//		return numElements;
//	}
	public Type getContained() {
		return contained;
	}
}