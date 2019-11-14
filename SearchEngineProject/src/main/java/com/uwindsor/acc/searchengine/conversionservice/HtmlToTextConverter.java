package com.uwindsor.acc.searchengine.conversionservice;

import java.io.File;

public interface HtmlToTextConverter {
	
	public void readHtmlFiles();
	
	public void writeTotxt(File filename);

}
