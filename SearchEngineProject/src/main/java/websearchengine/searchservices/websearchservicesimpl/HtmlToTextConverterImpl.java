package websearchengine.searchservices.websearchservicesimpl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import websearchengine.searchservices.HtmlToTextConverter;
import websearchengine.webutils.SearchEngineConstants;

@Service("htmlToTextConverter")
public class HtmlToTextConverterImpl implements HtmlToTextConverter {

	private static File file;
	private static String TXT_EXTENSION = ".txt";
	private static String REGEX = "[^a-zA-Z0-9]";
	private static String FILE_FORMAT = "UTF-8";
	private static String PARSE_HTML = "example.htm";
	private static final Logger log = LogManager.getLogger(HtmlToTextConverterImpl.class);

	public void readHtmlFiles() {
		createDir(SearchEngineConstants.HTML_TO_TXT_DIRECTORY);
		try (Stream<Path> filesWalk = Files.walk(Paths.get(absolutePath(SearchEngineConstants.HTML_FILES_DIRECTORY)))) {
			List<String> HtmlFileList = filesWalk.filter(Files::isRegularFile).map(htmlFile -> htmlFile.toString())
					.collect(Collectors.toList());
			for (String specificFile : HtmlFileList) {
				writeToText(specificFile);
			}
			if (log.isDebugEnabled())
				log.debug("HTML to Text Complete!!!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param specificFile
	 */
	public void writeToText(String specificFile) {
		try {
			file = createFile(specificFile);
			Document doc = Jsoup.parse(file, FILE_FORMAT, PARSE_HTML);
			String textHTML = doc.text();
			PrintWriter out = new PrintWriter(getTxtFileName(doc));
			out.println(textHTML);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	private static String absolutePath(String fileName) {
		file = createFile(fileName);
		return file.getAbsolutePath();
	}

	/**
	 * 
	 * @param doc
	 * @return
	 */
	private static String getTxtFileName(Document doc) {
		return absolutePath(SearchEngineConstants.HTML_TO_TXT_DIRECTORY) + File.separator + doc.title().replaceAll(REGEX, " ")
				+ TXT_EXTENSION;
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	private static File createFile(String fileName) {
		return new File(fileName);
	}

	/**
	 * 
	 * @param dirName
	 */
	private static void createDir(String dirName) {
		file = createFile(dirName);
		if (!file.exists()) {
			if (file.mkdir()) {
				if (log.isDebugEnabled())
					log.debug("'" + dirName + "'" + " -- Directory is created!");
			} else {
				log.error("Failed to create" + "'" + dirName + "'" + " directory!");
			}
		}
	}

}
