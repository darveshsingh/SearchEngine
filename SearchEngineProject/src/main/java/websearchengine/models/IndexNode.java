package websearchengine.models;

public class IndexNode {
	public int line = -1;
	public int cnt = 0;
	/**
	 * @return the line
	 */
	public int getLine() {
		return line;
	}
	/**
	 * @param line the line to set
	 */
	public void setLine(int line) {
		this.line = line;
	}
	/**
	 * @return the cnt
	 */
	public int getCnt() {
		return cnt;
	}
	/**
	 * @param cnt the cnt to set
	 */
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public IndexNode() {
		super();
	}
	public IndexNode(int line, int cnt) {
		super();
		this.line = line;
		this.cnt = cnt;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "IndexNode [line=" + line + ", cnt=" + cnt + "]";
	}
	
}
