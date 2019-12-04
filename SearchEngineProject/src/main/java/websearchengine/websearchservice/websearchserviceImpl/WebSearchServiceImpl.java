package websearchengine.websearchservice.websearchserviceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import algorithmDesign.Sequences;
import websearchengine.controller.SearchController;
import websearchengine.models.SearchResults;
import websearchengine.models.SortNode;
import websearchengine.models.TSTNode;
import websearchengine.searchservices.websearchservicesimpl.TSTStringTokenizerCreationImpl;
import websearchengine.websearchservice.WebSearchService;
import websearchengine.websortservice.websortserviceimpl.QuickSortImpl;
import websearchengine.webutils.InvetedIndex;
import websearchengine.webutils.SearchEngineConstants;
import websearchengine.webutils.TST;

@Service("webSearchService")
public class WebSearchServiceImpl implements WebSearchService {
	private static File file;
	public InvetedIndex idxTable = new InvetedIndex();
	public TST<TSTNode> tst = new TST<TSTNode>(idxTable);
	public TSTStringTokenizerCreationImpl token;

	private static final Logger log = LogManager.getLogger(SearchController.class);

	public void displayFile(SortNode sortN, List<SearchResults> searchResults, String keyword, String similarWords) {
		String fileName = token.getFileName(sortN.fileIdx);
		if (log.isDebugEnabled())
			log.debug("html/" + fileName + ".html" + " Count = " + sortN.cnt);

		SearchResults searchResultsObj = new SearchResults();
		searchResultsObj.setFileName(fileName);
		searchResultsObj.setCount(sortN.cnt);
		searchResultsObj.setMatchingWords(similarWords);

		try {
			file = new File(SearchEngineConstants.HTML_TO_TXT_DIRECTORY + "/" + fileName + ".txt");
			InputStream input = new FileInputStream(file);
			BufferedReader read = new BufferedReader(new InputStreamReader(input));
			String stringline;
			int rowNumber = 0;
			while ((stringline = read.readLine()) != null) {
				rowNumber++;
				if (rowNumber < sortN.line) {
					continue;
				} else if (rowNumber > sortN.line) {
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
		WebSearchServiceImpl webSearchServiceImpl = new WebSearchServiceImpl();
		List<SearchResults> searchResults = new ArrayList<SearchResults>();
		SortNode[] countArray = null;
		if (log.isDebugEnabled())
			log.debug("Searching Keyword : " + keyword);
		// Create TST
		webSearchServiceImpl.token = new TSTStringTokenizerCreationImpl(webSearchServiceImpl.tst);
		webSearchServiceImpl.token.readFile();
		String similarWords = "";

		int wordIdx = webSearchServiceImpl.tst.get(keyword);
		if (wordIdx != -1) {
			similarWords = similarWords(webSearchServiceImpl.tst, keyword);
			countArray = webSearchServiceImpl.idxTable.getCntArray(wordIdx);
			QuickSortImpl.quicksort(countArray);
		}

		if (countArray != null) {
			// Display the Search Results in the decreasing order
			for (int i = countArray.length - 1; i >= 0; i--) {
				if (countArray[i].cnt == 0)
					break;
				webSearchServiceImpl.displayFile(countArray[i], searchResults, keyword, similarWords);
			}
		}

		return searchResults;
	}

	/**
	 * 
	 * @param tst
	 * @param keyword
	 * @return
	 */
	private static String similarWords(TST<TSTNode> tst, String keyword) {
		String similarWords = "";
		int numberOfSimilarWords = 10;
		int count = 0;
		for (String iterable_element : tst.keys()) {
			if ((iterable_element.length() <= keyword.length() + 1) && count < numberOfSimilarWords) {
				String tstString = iterable_element.replaceAll(SearchEngineConstants.SPECIAL_CHARACTER_DIGIT, "");
				int dr = Sequences.editDistance(tstString, keyword.toLowerCase());
				if ((dr <= 1 && !iterable_element.equalsIgnoreCase(keyword))
						&&  !similarWords.contains(tstString)) {
					similarWords = similarWords.concat(tstString + " ");
					count++;
				}
			}else if(count == numberOfSimilarWords) {
				break;
			}
		}
		if (similarWords == "")
			similarWords = "No Similar words found !";

		return similarWords;
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

}
