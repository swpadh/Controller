public class ProcessNode implements Comparable {

	private boolean started;
	private String name;

	public ProcessNode(String name) {
		this.name = name;
		this.started = false;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(" Name: ");
		strBuilder.append(name);
		strBuilder.append(" Started: ");
		strBuilder.append(started);
		return strBuilder.toString();
	}

	@Override
	public int compareTo(Object arg0) {
		return this.name.compareTo(((ProcessNode) arg0).name);
	}

	@Override
	public boolean equals(Object arg0) {
		return this.name.equals(((ProcessNode) arg0).name);
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

}
