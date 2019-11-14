package com.uwindsor.acc.searchengine.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.uwindsor.acc.searchengine.models.TSTNode;

/*************************************************************************
 * Compilation: javac TST.java Execution: java TST < words.txt Dependencies:
 * StdIn.java
 *
 * Symbol table with string keys, implemented using a ternary search trie (TST).
 *
 *
 * % java TST < shellsST.txt by 4 sea 6 sells 1 she 0 shells 3 shore 7 the 5
 *
 * 
 * % java TST theory the now is the time for all good men
 * 
 * Remarks -------- - can't use a key that is the empty string ""
 *
 *************************************************************************/

public class TST<Value> {
	private int wordIdxcnt;
	String[] wordTable = new String[Constants.WORDMAX];
	private IndexTable idxTable;

	private static final Logger LOGGER = LogManager.getLogger(TST.class);

	private int N; // size
	private Node root; // root of TST

	private class Node {
		private char c; // character
		private Node left, mid, right; // left, middle, and right subtries
		private Value val; // value associated with string
		public int wordIdx = -1; // Index of the word in the node
	}

	// Constructor
	public TST(IndexTable idxT) {
		idxTable = idxT;
	}

	// return number of key-value pairs
	public int size() {
		return N;
	}

	/**************************************************************
	 * Is string key in the symbol table?
	 **************************************************************/
	public boolean contains(String key) {
		// return get(key) != null;
		return get(key) != -1;
	}

	// public Value get(String key) {
	public int get(String key) {
		if (key == null)
			throw new NullPointerException();
		if (key.length() == 0)
			throw new IllegalArgumentException("key must have length >= 1");
		Node x = get(root, key, 0);
		// if (x == null) return null;
		if (x == null)
			return -1;
		// return x.val;
		return x.wordIdx;
	}

	// return subtrie corresponding to given key
	private Node get(Node x, String key, int d) {
		if (key == null)
			throw new NullPointerException();
		if (key.length() == 0)
			throw new IllegalArgumentException("key must have length >= 1");
		if (x == null)
			return null;
		char c = key.charAt(d);
		if (c < x.c)
			return get(x.left, key, d);
		else if (c > x.c)
			return get(x.right, key, d);
		else if (d < key.length() - 1)
			return get(x.mid, key, d + 1);
		else
			return x;
	}

	/**************************************************************
	 * Insert string s into the symbol table.
	 **************************************************************/
	public void put(String s, Value val) {
		if (!contains(s))
			N++;
		root = put(root, s, val, 0);
	}

	private Node put(Node x, String s, Value val, int d) {
		char c = s.charAt(d);
		if (x == null) {
			x = new Node();
			x.c = c;
		}
		if (c < x.c)
			x.left = put(x.left, s, val, d);
		else if (c > x.c)
			x.right = put(x.right, s, val, d);
		else if (d < s.length() - 1)
			x.mid = put(x.mid, s, val, d + 1);
		// else x.val = val;
		else
			setValue(x, val, s);
		return x;
	}

	// Set the node to the TST
	private void setValue(Node x, Value val, String s) {

		if (x.wordIdx == -1) {
			// Allocate index for the new word
			if (wordIdxcnt < wordTable.length - 1) {
				x.wordIdx = ++wordIdxcnt;
				wordTable[wordIdxcnt] = s;
			}
		}

		x.val = val;
		if (x.wordIdx != -1) {
			idxTable.addToTable((TSTNode) val, x.wordIdx);
		}
	}

	/**************************************************************
	 * Find and return longest prefix of s in TST
	 **************************************************************/
	public String longestPrefixOf(String s) {
		if (s == null || s.length() == 0)
			return null;
		int length = 0;
		Node x = root;
		int i = 0;
		while (x != null && i < s.length()) {
			char c = s.charAt(i);
			if (c < x.c)
				x = x.left;
			else if (c > x.c)
				x = x.right;
			else {
				i++;
				if (x.val != null)
					length = i;
				x = x.mid;
			}
		}
		return s.substring(0, length);
	}

	// all keys in symbol table
	public Iterable<String> keys() {
		Queue<String> queue = new Queue<String>();
		collect(root, "", queue);
		return queue;
	}

	// all keys starting with given prefix
	public Iterable<String> prefixMatch(String prefix) {
		Queue<String> queue = new Queue<String>();
		Node x = get(root, prefix, 0);
		if (x == null)
			return queue;
		if (x.val != null)
			queue.enqueue(prefix);
		collect(x.mid, prefix, queue);
		return queue;
	}

	// all keys in subtrie rooted at x with given prefix
	private void collect(Node x, String prefix, Queue<String> queue) {
		if (x == null)
			return;
		collect(x.left, prefix, queue);
		if (x.val != null)
			queue.enqueue(prefix + x.c);
		collect(x.mid, prefix + x.c, queue);
		collect(x.right, prefix, queue);
	}

	// return all keys matching given wildcard pattern
	public Iterable<String> wildcardMatch(String pat) {
		Queue<String> queue = new Queue<String>();
		collect(root, "", 0, pat, queue);
		return queue;
	}

	private void collect(Node x, String prefix, int i, String pat, Queue<String> q) {
		if (x == null)
			return;
		char c = pat.charAt(i);
		if (c == '.' || c < x.c)
			collect(x.left, prefix, i, pat, q);
		if (c == '.' || c == x.c) {
			if (i == pat.length() - 1 && x.val != null)
				q.enqueue(prefix + x.c);
			if (i < pat.length() - 1)
				collect(x.mid, prefix + x.c, i + 1, pat, q);
		}
		if (c == '.' || c > x.c)
			collect(x.right, prefix, i, pat, q);
	}
}