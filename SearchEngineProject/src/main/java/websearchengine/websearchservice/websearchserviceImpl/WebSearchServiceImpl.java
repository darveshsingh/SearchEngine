package websearchengine.websearchservice.websearchserviceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import algorithmDesign.Sequences;
import websearchengine.controller.SearchController;
import websearchengine.models.SearchResults;
import websearchengine.models.SortNode;
import websearchengine.models.TSTNode;
import websearchengine.searchservices.websearchservicesimpl.TSTStringTokenizerImpl;
import websearchengine.websearchservice.WebSearchService;
import websearchengine.websortservice.websortserviceimpl.QuickSortImpl;
import websearchengine.webutils.SearchEngineConstants;
import websearchengine.webutils.IndexTable;
import websearchengine.webutils.TST;

@Service("webSearchService")
public class WebSearchServiceImpl implements WebSearchService {
	private static File file;
	public IndexTable idxTable = new IndexTable();
	public TST<TSTNode> tst = new TST<TSTNode>(idxTable);
	public TSTStringTokenizerImpl token;

	private static final Logger log = LogManager.getLogger(SearchController.class);

	public void displayFile(SortNode sortN, List<SearchResults> searchResults, String keyword) {
		String fileName = token.getFileName(sortN.fileIdx);
		if (log.isDebugEnabled())
			log.debug("html/" + fileName + ".html" + " Count = " + sortN.cnt);

		SearchResults searchResultsObj = new SearchResults();
		searchResultsObj.setFileName(fileName);
		searchResultsObj.setCount(sortN.cnt);

		try {
			file = new File(SearchEngineConstants.HTML_TO_TXT_DIRECTORY + "/" + fileName + ".txt");
			InputStream input = new FileInputStream(file);
			BufferedReader read = new BufferedReader(new InputStreamReader(input));
			String stringline;
			int row_number = 0;
			while ((stringline = read.readLine()) != null) {
				searchResultsObj.setMatchingWords((getMatchingWords(stringline, keyword).toString())
						.replaceAll(SearchEngineConstants.SPECIAL_CHARACTER_DIGIT, SearchEngineConstants.EMPTY));
				row_number++;
				if (row_number < sortN.line) {
					continue;
				} else if (row_number > sortN.line) {
					break;
				} else {
					if (log.isDebugEnabled())
						log.debug(stringline);
					searchResultsObj.setStringLine(getSentence(stringline, keyword));
				}
			}
			read.close();
			input.close();
			searchResults.add(searchResultsObj);

		} catch (FileNotFoundException e) {
			log.warn(e.getMessage());
		} catch (IOException e) {
			log.warn(e.getMessage());
		}
	}

	public List<SearchResults> search(String keyword) {
		WebSearchServiceImpl webS = new WebSearchServiceImpl();
		List<SearchResults> searchResults = new ArrayList<SearchResults>();
		SortNode[] cntArray = null;
		if (log.isDebugEnabled())
			log.debug("Searching Keyword : " + keyword);
		// Read text files and put words to TST
		webS.token = new TSTStringTokenizerImpl(webS.tst);
		webS.token.readFile();

		int wordIdx = webS.tst.get(keyword);
		if (wordIdx != -1) {
			cntArray = webS.idxTable.getCntArray(wordIdx);
			QuickSortImpl.quicksort(cntArray);
		}

		if (cntArray != null) {
			// The sort of cnt is increasing order. We want to display it in decreasing
			// order.
			for (int i = cntArray.length - 1; i >= 0; i--) {
				if (cntArray[i].cnt == 0)
					break;
				webS.displayFile(cntArray[i], searchResults, keyword);
			}
		}

		return searchResults;
	}

	/**
	 * 
	 * @param text
	 * @param word
	 * @return
	 */
	public static String getSentence(String text, String word) {
		final String lcword = word.toLowerCase();
		return SearchEngineConstants.END_OF_SENTENCE.splitAsStream(text)
				.filter(s -> s.toLowerCase().contains(" " + lcword + " ")).findAny().orElse(null);
	}

	/**
	 * 
	 * @param text
	 * @param word
	 * @return
	 */
	public static Map<String, Integer> getMatchingWords(String text, String word) {
		Map<String, Integer> dictMap = new HashMap<String, Integer>();
		String[] searchArray = (text.replaceAll(SearchEngineConstants.SPECIAL_CHARACTER_DIGIT,
				SearchEngineConstants.EMPTY)).split(SearchEngineConstants.EMPTY);
		for (String searchKey : searchArray) {
			int dr = Sequences.editDistance(searchKey, word.toLowerCase());
			if (dr <= 1 && !searchKey.equalsIgnoreCase(word)) {
				dictMap.put(searchKey, 1);
			}
		}
		return dictMap;
	}

}
