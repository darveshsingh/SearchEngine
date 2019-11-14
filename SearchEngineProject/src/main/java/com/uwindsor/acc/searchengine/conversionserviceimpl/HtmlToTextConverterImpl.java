package com.uwindsor.acc.searchengine.conversionserviceimpl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import com.uwindsor.acc.searchengine.conversionservice.HtmlToTextConverter;
import com.uwindsor.acc.searchengine.utils.ExtensionsHandler;

@Service("htmlToTextConverter")
public class HtmlToTextConverterImpl implements HtmlToTextConverter{

	static File[] directoryListing;
	static File file;
	static String txtName;
	
	private static final Logger LOGGER = LogManager.getLogger(HtmlToTextConverterImpl.class);

	public void readHtmlFiles() {
		file = new File(ExtensionsHandler.absolutePath(ExtensionsHandler.File_name()));

		directoryListing = file.listFiles();

		if (directoryListing != null) {
			LOGGER.debug("Starting the conversion of files from HTML to Txt...");
			for (File specificFile : directoryListing) {
				writeTotxt(specificFile);
			}
			LOGGER.debug("Conversion of files done!");
		} else {
			LOGGER.warn("Couldn't find files in directory mentioned!");
		}
	}

	public void writeTotxt(File filename)
	{
		txtName = ExtensionsHandler.absolutePath(ExtensionsHandler.txtFile()) + ExtensionsHandler.fileSeparator()
				+ ExtensionsHandler.makeTxtName(filename);

		LOGGER.debug("Executing the writeTotxt function...");
		LOGGER.debug("Text files location : "+txtName);
		
		try {
			FileReader in = new FileReader(filename);
			in.close();
			Document doc = Jsoup.parse(filename, "UTF-8", "example.htm");
			String textHTML = doc.text();
			BufferedWriter writerTxt = new BufferedWriter(new FileWriter(txtName));
			writerTxt.write(textHTML);
			writerTxt.close();

		} catch (Exception e) {
			LOGGER.debug(e.toString());
		}
	}

}
