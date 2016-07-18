import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DAGEngine {
	private ForkJoinPool pool = ForkJoinPool.commonPool();

	public DAG readProcessDependency() throws IOException {
		DAG processDependency = new DAG();

		InputStream res = DAGEngine.class.getClassLoader().getResourceAsStream(
				"ProcessDependency.txt");

		BufferedReader reader = new BufferedReader(new InputStreamReader(res));
		String line = null;

		while ((line = reader.readLine()) != null) {
			StringTokenizer strTknz = new StringTokenizer(line, ";");
			String origin = strTknz.nextToken();
			while (strTknz.hasMoreTokens()) {
				String target = strTknz.nextToken();
				processDependency.addEdge(new ProcessNode(origin),
						new ProcessNode(target));
			}
		}
		reader.close();
		return processDependency;
	}

	private void commandVertices(DAG processDependency, Set vertices,
			Consumer<ProcessNode> cons) throws IOException {

		CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
			vertices.parallelStream().forEach((t) -> {
				ProcessNode pnode = ((ProcessNode) t);
				cons.accept(pnode);
			});
		}, pool);
		future.join();
	}

	public void comandAllProcesses(DAG processDependency,
			Consumer<ProcessNode> cons) throws IOException {
		Set sinks = processDependency.getSinks();
		commandAllProcessesImpl(processDependency, sinks, cons);
	}

	private void commandAllProcessesImpl(DAG processDependency, Set vertices,
			Consumer<ProcessNode> cons) throws IOException {
		commandVertices(processDependency, vertices, cons);
		Set parents = processDependency.getPreviousLevel(vertices);
		if (parents != null && parents.isEmpty() == false) {
			commandAllProcessesImpl(processDependency, parents, cons);
		}
	}

	public void comandProcess(DAG processDependency, ProcessNode pNode,
			Consumer<ProcessNode> cons) throws IOException {
		Set sinks = processDependency.getSinks();
		commandProcessImpl(processDependency, sinks, pNode, cons);
	}

	private void commandProcessImpl(DAG processDependency, Set vertices,
			ProcessNode pNode, Consumer<ProcessNode> cons) throws IOException {
		if (vertices.contains(pNode)) {		
			vertices.removeIf((t) -> {
					return t.equals(pNode)== false;
				});
			commandVertices(processDependency, vertices, cons);
			
			return;

		} else {
			commandVertices(processDependency, vertices, cons);
			Set parents = processDependency.getPreviousLevel(vertices);
			if (parents != null && parents.isEmpty() == false) {
				commandProcessImpl(processDependency, parents, pNode, cons);
			}
		}
	}

	public static void main(String[] args) {
		try {
			DAGEngine engine = new DAGEngine();

			DAG processDependency = engine.readProcessDependency();
			engine.comandAllProcesses(processDependency, new StartProcess());
			engine.comandAllProcesses(processDependency, new StopProcess());
			engine.comandProcess(processDependency, new ProcessNode("a"), new StartProcess());
			engine.comandProcess(processDependency, new ProcessNode("a"),new StopProcess());
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
