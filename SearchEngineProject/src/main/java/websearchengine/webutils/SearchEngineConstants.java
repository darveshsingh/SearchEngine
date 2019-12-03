package websearchengine.webutils;

import java.util.regex.Pattern;

public class SearchEngineConstants {

	// Maximum files to search
	public static final int FILEMAX = 1190;
	// Maximum different words contained in the files
	public static final int WORDMAX = 100000;
	//Cut off
	public static final int CUTOFF = 100;
	public static final Pattern END_OF_SENTENCE = Pattern.compile("\\.\\s+");
	public static String HTML_TO_TXT_DIRECTORY = "Html To Text Files";
	public static String HTML_FILES_DIRECTORY = "HTML Pages";
	public static String IDENTIFIER_REGEX = "~!@#$%^&*()_+{}|:\\\"<>?-+[]\\\\;',./`=";
	public static String SPECIAL_CHARACTER_DIGIT ="[^a-zA-Z]";
	public static String EMPTY = " ";


}
