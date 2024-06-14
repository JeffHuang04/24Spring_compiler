import javax.print.DocFlavor;

public class AsmBuilder {
	private StringBuffer buffer;

	public AsmBuilder() {
		buffer = new StringBuffer();
	}

	public void op2(String op, String dest, String lhs, String rhs) {
		buffer.append(String.format("  %s %s, %s, %s\n", op, dest, lhs, rhs));
	}

	public void op1(String op, String dest, String src) {
		buffer.append(String.format("  %s %s, %s\n", op, dest, src));
	}

	public void op0(String op) {
		buffer.append(String.format("  %s\n", op));
	}

	public void label(String lbl) {
		buffer.append(String.format("%s:\n", lbl));
	}

	public void string(String str){
		buffer.append(String.format("%s\n",str));
	}

	public void data(){
		buffer.append("  .data\n");
	}

	public void text(){
		buffer.append("  .text\n");
	}

	public void global(String fucName) {
		buffer.append(String.format("  .globl %s\n", fucName));
	}

	public void nextLine(){
		buffer.append("\n");
	}

	public void word(int value){
		buffer.append(String.format("  .word  %d\n",value));
	}

	public String getRiscV() {
		return buffer.toString();
	}


}
