public class ArrayType extends Type{
	private Type contained;
	private int numElements;
	public ArrayType(Type contained, int numElements){
		this.contained = contained;
		this.numElements = numElements;
	}
	public int getNumElements() {
		return numElements;
	}
	public Type getContained() {
		return contained;
	}
}
