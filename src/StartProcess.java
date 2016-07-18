import java.util.function.Consumer;


public class StartProcess implements Consumer<ProcessNode> {

	@Override
	public void accept(ProcessNode pnode) {
		if (pnode.isStarted() == false) {
			pnode.setStarted(true);
		}
		
	}

}
