import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public final class DAG {
	private final MultiMap fanOut = new MultiMap();
	private final MultiMap fanIn = new MultiMap();

	/**
	 * @param origin
	 *            vertex
	 * @param target
	 *            vertex
	 * @return true if the edge was added, false if cyclic.
	 */
	public boolean addEdge(Object origin, Object target) {
		assert (origin != null);
		assert (target != null);

		if (hasPath(target, origin))
			return false;

		fanOut.put(origin, target);
		fanOut.put(target, null);
		fanIn.put(target, origin);
		fanIn.put(origin, null);
		return true;
	}

	/**
	 * @param vertex
	 *            the new vertex
	 */
	public void addVertex(Object vertex) {
		assert (vertex != null);
		fanOut.put(vertex, null);
		fanIn.put(vertex, null);
	}

	/**
	 * @param vertex
	 *            the vertex to remove
	 */
	public void removeVertex(Object vertex) {
		Set targets = fanOut.removeAll(vertex);
		
		targets.forEach((t) -> {
			fanIn.remove(t, vertex);
		});
		Set origins = fanIn.removeAll(vertex);
		
		origins.forEach((t) -> {
			fanOut.remove(t, vertex);
		});
	}

	/**
	 * @return the sources of the receiver
	 */
	public Set getSources() {
		return computeZeroEdgeVertices(fanIn);
	}

	/**
	 * @return the sinks of the receiver.
	 */
	public Set getSinks() {
		return computeZeroEdgeVertices(fanOut);
	}

	private Set computeZeroEdgeVertices(MultiMap map) {
		Set candidates = map.keySet();
		Set roots = new LinkedHashSet(candidates.size());
		candidates.forEach((candidate) -> {
			if (map.get(candidate).isEmpty())
				roots.add(candidate);
		});
		return roots;
	}

	/**
	 * @return the parents of a vertex.
	 */
	public Set getParents(Object vertex) {
		return fanIn.get(vertex);
	}

	public Set getPreviousLevel(Set sameLevelNode) {
		Set previousLevel = new LinkedHashSet();
		sameLevelNode.forEach((t) -> {
			Set parents = getParents(t);
			previousLevel.addAll(parents);
		});
		return previousLevel;
	}

	/**
	 * Returns the direct children of a vertex.
	 * 
	 * @param vertex
	 *            the parent vertex
	 * @return the direct children of vertex
	 */
	public Set getChildren(Object vertex) {
		return Collections.unmodifiableSet(fanOut.get(vertex));
	}

	private boolean hasPath(Object start, Object end) {
		if (start.equals(end))
			return true;

		Set children = fanOut.get(start);
		for (Iterator it = children.iterator(); it.hasNext();)
			if (hasPath(it.next(), end))
				return true;
		return false;
	}

	public String toString() {
		return "Fan Out: " + fanOut.toString() + "\nFan In: "
				+ fanIn.toString();
	}
}
