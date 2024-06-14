import org.bytedeco.llvm.LLVM.LLVMModuleRef;

public class RiscVTranslator {
	private LLVMModuleRef module;
	private AsmBuilder asmBuilder;

	public RiscVTranslator(LLVMModuleRef module){
		this.module = module;
		this.asmBuilder = new AsmBuilder();
	}

	public void translate(){

	}

	public String getAsm(){
		return asmBuilder.getRiscV();
	}

}
