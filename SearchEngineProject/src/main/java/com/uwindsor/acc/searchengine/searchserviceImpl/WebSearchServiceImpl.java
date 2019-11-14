package com.uwindsor.acc.searchengine.searchserviceImpl;

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
import com.uwindsor.acc.searchengine.controllers.SearchController;
import com.uwindsor.acc.searchengine.conversionserviceimpl.TSTStringTokenizerImpl;
import com.uwindsor.acc.searchengine.models.SearchResults;
import com.uwindsor.acc.searchengine.models.SortNode;
import com.uwindsor.acc.searchengine.models.TSTNode;
import com.uwindsor.acc.searchengine.searchservice.WebSearchService;
import com.uwindsor.acc.searchengine.sortserviceimpl.quickSortImpl;
import com.uwindsor.acc.searchengine.utils.Constants;
import com.uwindsor.acc.searchengine.utils.IndexTable;
import com.uwindsor.acc.searchengine.utils.TST;

@Service("webSearchService")
public class WebSearchServiceImpl implements WebSearchService {

	public IndexTable idxTable = new IndexTable();
	public TST<TSTNode> tst = new TST<TSTNode>(idxTable);
	public TSTStringTokenizerImpl token;
	
	private static final Logger LOGGER = LogManager.getLogger(SearchController.class);

	public void displayFile(SortNode sortN, List<SearchResults> searchResults, String keyword) {
		String fileName = token.getFileName(sortN.fileIdx);
		LOGGER.debug("html/" + fileName + ".html");
		LOGGER.debug("Count = " + sortN.cnt);
		
		SearchResults searchResultsObj = new SearchResults();
		searchResultsObj.setFileName(fileName);
		searchResultsObj.setCount(sortN.cnt);

		try {
			File f = new File("FilesConvertedToTxt/" + fileName + ".txt");
			InputStream input = new FileInputStream(f);
			BufferedReader read = new BufferedReader(new InputStreamReader(input));
			String stringline;
			int row_number = 0;
			while( (stringline = read.readLine()) !=null){
				row_number++;
				if(row_number < sortN.line) {
					continue;
				}
				else if(row_number > sortN.line) {
					break;
				}
				else {
					LOGGER.debug(stringline);
					
					searchResultsObj.setStringLine(getSentence(stringline, keyword));
				}   		
			}
			read.close();
			input.close();
			searchResults.add(searchResultsObj);

		}catch (FileNotFoundException e) {  
			LOGGER.warn(e.getMessage());  
		} catch (IOException e) {  
			LOGGER.warn(e.getMessage());  
		}
	}

	public List<SearchResults> driver(String keyword) {
		WebSearchServiceImpl webS = new WebSearchServiceImpl();
		List<SearchResults> searchResults = new ArrayList<SearchResults>();
		SortNode[] cntArray = null;
		LOGGER.debug("Searching Keyword : "+keyword);
		// Read text files and put words to TST
		webS.token = new TSTStringTokenizerImpl(webS.tst);
		webS.token.readFile();

			int wordIdx = webS.tst.get(keyword);
		if (wordIdx != -1) {
			cntArray = webS.idxTable.getCntArray(wordIdx);
			quickSortImpl.quicksort(cntArray);
			/*for (int i = 0; i < cntArray.length; i++) {
				QuickSelectImpl.quickSelect(cntArray, i);
			}*/
		}

			if(cntArray != null) {
				// The sort of cnt is increasing order. We want to display it in decreasing order.
				for(int i = cntArray.length-1; i>=0; i--) {
					// cnt=0 means the file doesn't contain the key word.
					if(cntArray[i].cnt == 0) break;
					webS.displayFile( cntArray[i], searchResults, keyword);
				}
			}
			return searchResults;
	}
	
	public static String getSentence(String text, String word) {
	    final String lcword = word.toLowerCase();
	    return Constants.END_OF_SENTENCE.splitAsStream(text)
	            .filter(s -> s.toLowerCase().contains(" "+lcword+" "))
	            .findAny()
	            .orElse(null);
	}

}
