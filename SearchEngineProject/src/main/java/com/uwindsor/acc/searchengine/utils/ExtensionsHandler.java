package com.uwindsor.acc.searchengine.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExtensionsHandler {

	private static final Logger LOGGER = LogManager.getLogger(ExtensionsHandler.class);
	
	public static String absolutePath(String name){
		
		File f = new File(name);
		String absolutePath = f.getAbsolutePath();
		return absolutePath;
	 }
	
	public static String File_name(){
		final String FILE_NAME = "W3C Web Pages";
		LOGGER.debug("Reading html files from folder : "+FILE_NAME);
		return FILE_NAME;
	}
	
	public static String txtFile(){
		final String TEXT_FILE = "FilesConvertedToTxt";
		LOGGER.debug("Converted text files will be saved in folder : "+TEXT_FILE);
		return TEXT_FILE;
	}
	
	public final static String fileSeparator()
	{
		return File.separator;
	}
	
	public static String makeTxtName(File filename){
		String name = filename.getName();
		String[] temp = name.split("htm");
		return temp[0] + "txt";
	}
	
	public static void writeSearchFiles(String string, String filename) throws IOException{
		
		String  newFileName = absolutePath("Searches") + fileSeparator() + filename;
		// Write the text to a file  
	    BufferedWriter writerTxt = new BufferedWriter(new FileWriter(newFileName));
	    writerTxt.write(string);
	    writerTxt.close();
				
	}

}
