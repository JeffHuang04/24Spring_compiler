import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class myErrorListener extends BaseErrorListener {
	boolean isErr = false;
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
							int line, int charPositionInLine, String msg, RecognitionException e) {
		isErr = true;
		System.out.println("Error type B at Line "+ line +": "+ msg +'.');
	}
	public boolean getError(){
		return isErr;
	}




}