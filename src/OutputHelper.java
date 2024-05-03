public class OutputHelper {
	private boolean isErr;
	public void outputErr(int errorTypeNo, int lineNo, String errorMessage){
		isErr = true;
		System.err.println("Error type "+errorTypeNo+" at Line "+lineNo+": "+errorMessage);
	}
	public void outputTrue(){
		System.err.println("No semantic errors in the program!");
	}
	public boolean getIsErr() {
		return isErr;
	}
}
