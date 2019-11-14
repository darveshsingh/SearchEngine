package com.uwindsor.acc.searchengine.utils;

import java.util.regex.Pattern;

public class Constants {

	// Maximum files to search
	public static final int FILEMAX = 110;
	// Maximum different words contained in the files
	public static final int WORDMAX = 10000;
	//Cut off
	public static final int CUTOFF = 100;
	
	public static final Pattern END_OF_SENTENCE = Pattern.compile("\\.\\s+");


}
