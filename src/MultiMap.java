import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class MultiMap {
	// <Vertex, Set of edges>
	private final Map vertices = new LinkedHashMap();

	/**
	 * @param key
	 *            the key
	 * @param val
	 *            the value
	 */
	public void put(Object key, Object val) {
		Set values = (Set) vertices.get(key);
		if (values == null) {
			values = new LinkedHashSet();
			vertices.put(key, values);
		}
		if (val != null)
			values.add(val);
	}

	/**
	 * @param key
	 *            the key
	 * @return the mappings for a key
	 */
	public Set get(Object key) {
		Set values = (Set) vertices.get(key);
		return values == null ? Collections.EMPTY_SET : values;
	}

	public Set keySet() {
		return vertices.keySet();
	}

	/**
	 * Removes all mappings for a key and removes key from the key set.
	 * 
	 * @param key
	 *            the key to remove
	 * @return the removed mappings
	 */
	public Set removeAll(Object key) {
		Set values = (Set) vertices.remove(key);
		return values == null ? Collections.EMPTY_SET : values;
	}

	/**
	 * Removes a mapping from the multimap, but does not remove the key from the
	 * key set.
	 * 
	 * @param key
	 *            the key
	 * @param val
	 *            the value
	 */
	public void remove(Object key, Object val) {
		Set values = (Set) vertices.get(key);
		if (values != null)
			values.remove(val);
	}

	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		vertices.forEach((arg0, arg1) -> {
			strBuilder.append("Key : " + arg0 + " Value : " + arg1);
			strBuilder.append("\n");
		});
		return strBuilder.toString();
	}
}
