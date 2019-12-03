package websearchengine.searchservices.websearchservicesimpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import websearchengine.models.TSTNode;
import websearchengine.searchservices.TSTStringCreation;
import websearchengine.webutils.SearchEngineConstants;
import websearchengine.webutils.TST;

public class TSTStringTokenizerCreationImpl implements TSTStringCreation {

	String[] fileName;
	private static File file;
	private TST<TSTNode> tst;

	private static final Logger log = LogManager.getLogger(TSTStringTokenizerCreationImpl.class);

	public TSTStringTokenizerCreationImpl(TST<TSTNode> tst) {
		this.tst = tst;
	}

	public String getFileName(int fileId) {
		return fileName[fileId];
	}

	public static String PrefixString(String line) {
		if (SearchEngineConstants.IDENTIFIER_REGEX.indexOf(line.charAt(0)) == -1) {
			return line;
		} else if (line.length() == 1) {
			return null;
		} else {
			return PrefixString(line.substring(1));
		}
	}

	public static String SuffixString(String line) {
		if (SearchEngineConstants.IDENTIFIER_REGEX.indexOf(line.charAt(line.length() - 1)) == -1) {
			return line;
		}
		return SuffixString(line.substring(0, line.length() - 1));
	}

	public void readFile() {
		String[] filelist;
		ArrayList<ArrayList<String>> allFileList = new ArrayList<ArrayList<String>>();
		file = new File(SearchEngineConstants.HTML_TO_TXT_DIRECTORY);
		filelist = file.list();
		fileName = new String[filelist.length];
		for (int i = 0; i < filelist.length; i++) {
			fileName[i] = filelist[i].substring(0, filelist[i].length() - 4);
		}

		try {
			for (int i = 0; i < filelist.length; i++) {
				ArrayList<String> fileList = new ArrayList<String>();
				// Check whether the file is .txt
				if (!filelist[i].substring(filelist[i].length() - 4).equals(".txt"))
					continue;

				file = new File(SearchEngineConstants.HTML_TO_TXT_DIRECTORY + "/" + filelist[i]);
				InputStream input = new FileInputStream(file);
				BufferedReader read = new BufferedReader(new InputStreamReader(input));

				fileList.add(filelist[i]);
				String line = read.readLine();
				int rowNumber = 1;

				while (line != null) {
					StringTokenizer st = new StringTokenizer(line);
					String rowNumberString = "row: " + rowNumber;
					fileList.add(rowNumberString);
					while (st.hasMoreElements()) {
						String temp = st.nextToken();

						if (temp.length() == 1) {
							if ((temp.charAt(0) >= 'a' && temp.charAt(0) <= 'z')
									|| (temp.charAt(0) >= 'A' && temp.charAt(0) <= 'Z')) {
								fileList.add(temp);
								// Add the word to TST
								TSTNode tstNode = new TSTNode(i, rowNumber);
								tst.put(temp, tstNode);
							}
						} else {
							String prefixString = PrefixString(temp);
							if (prefixString != null) {
								String finalKey = SuffixString(prefixString);
								if (finalKey != null) {
									fileList.add(finalKey);
									// Adding the word to TST
									TSTNode tstNode = new TSTNode(i, rowNumber);
									tst.put(temp, tstNode);
								}
							}
						}
					}
					line = read.readLine();
					rowNumber++;
				}
				allFileList.add(fileList);
				read.close();
				input.close();
			}

		} catch (FileNotFoundException e) {
			log.error(e.toString());
		} catch (IOException e) {
			log.error(e.toString());
		}
	}

}
