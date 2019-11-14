package com.uwindsor.acc.searchengine.models;

public class SortNode implements Comparable<SortNode> {
	public int fileIdx = -1;
	public int cnt;
	public int line;
	
	@Override
	public int compareTo(SortNode o) {
		return cnt - o.cnt;
	}
}
