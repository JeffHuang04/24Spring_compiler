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

	public String getRiscV() {
		return buffer.toString();
	}


}
