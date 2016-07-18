import java.util.function.Consumer;


public class StopProcess implements Consumer<ProcessNode> {

	@Override
	public void accept(ProcessNode pnode) {
		if (pnode.isStarted() == true) {
			pnode.setStarted(false);
		}
		
	}

}
