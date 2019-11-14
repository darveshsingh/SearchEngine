package com.uwindsor.acc.searchengine.searchservice;

import java.util.List;

import com.uwindsor.acc.searchengine.models.SearchResults;
import com.uwindsor.acc.searchengine.models.SortNode;

public interface WebSearchService {
	public void displayFile(SortNode sortN, List<SearchResults> searchResults, String keyword);
	public List<SearchResults> driver(String keyword);
}
