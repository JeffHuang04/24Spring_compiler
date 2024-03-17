import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class myErrorListener extends BaseErrorListener {
	boolean isErr = false;
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
							int line, int charPositionInLine, String msg, RecognitionException e) {
		isErr = true;
		System.err.println("Error type A at Line "+ line +": "+ msg +'.');
	}//此函数用法google得来
	public boolean getError(){
		return isErr;
	}



}
