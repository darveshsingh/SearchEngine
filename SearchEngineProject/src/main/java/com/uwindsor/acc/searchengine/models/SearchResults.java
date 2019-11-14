package com.uwindsor.acc.searchengine.models;

public class SearchResults {
	private String fileName;
	private int count;
	private String stringLine;
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param cnt the count to set
	 */
	public void setCount(int cnt) {
		this.count = cnt;
	}
	/**
	 * @return the stringLine
	 */
	public String getStringLine() {
		return stringLine;
	}
	/**
	 * @param stringLine the stringLine to set
	 */
	public void setStringLine(String stringLine) {
		this.stringLine = stringLine;
	}
	public SearchResults() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SearchResults(String fileName, int count, String stringLine) {
		super();
		this.fileName = fileName;
		this.count = count;
		this.stringLine = stringLine;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SerchResults [fileName=" + fileName + ", count=" + count + ", stringLine=" + stringLine + "]";
	}
	
}
