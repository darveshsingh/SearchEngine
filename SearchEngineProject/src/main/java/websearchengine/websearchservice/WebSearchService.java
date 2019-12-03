package websearchengine.websearchservice;

import java.util.List;

import websearchengine.models.SearchResults;
import websearchengine.models.SortNode;

public interface WebSearchService {
	public void displayFile(SortNode sortN, List<SearchResults> searchResults, String keyword);
	public List<SearchResults> search(String keyword);
}
