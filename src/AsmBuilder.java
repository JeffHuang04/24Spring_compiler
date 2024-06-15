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

	public void addi(String dest,String lhs,String rhs){
		buffer.append(String.format("  addi %s, %s, %s\n", dest, lhs, rhs));
	}

	public void add(String dest,String lhs,String rhs){
		buffer.append(String.format("  add %s, %s, %s\n", dest, lhs, rhs));
	}

	public void li(String dest,int immediate){
		buffer.append(String.format("  li %s, %d\n", dest,immediate));
	}
	public void la(String dest,String globalName){
		buffer.append(String.format("  la %s, %s\n", dest,globalName));
	}
	public void lw(String dest, int offset,String src ){
		buffer.append(String.format("  lw %s, %d(%s)\n", dest,offset,src));
	}
	public void sw(String dest, int offset,String src ){
		buffer.append(String.format("  sw %s, %d(%s)\n", dest,offset,src));
	}
	public void ecall(){
		buffer.append("  ecall");
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
		buffer.append(String.format("  .word %d\n",value));
	}

	public String getRiscV() {
		return buffer.toString();
	}


}
