package com.uwindsor.acc.searchengine.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.uwindsor.acc.searchengine.conversionservice.HtmlToTextConverter;
import com.uwindsor.acc.searchengine.models.SearchQuery;
import com.uwindsor.acc.searchengine.searchserviceImpl.WebSearchServiceImpl;

@Controller
public class SearchController {

	private static final Logger LOGGER = LogManager.getLogger(SearchController.class);

	@Autowired
	HtmlToTextConverter htmlToTextConverter;

	@Autowired
	WebSearchServiceImpl webSearchService;

	@Value("${spring.application.name}")
	String appName;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String searchEngineStartUp(Model model) {
		LOGGER.debug("Executing searchEngineStartUp controller...");

		htmlToTextConverter.readHtmlFiles();

		model.addAttribute("appName", appName);
		model.addAttribute("searchQuery", new SearchQuery());
		return "form";
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String searchQuery(@ModelAttribute SearchQuery searchQuery, Model model) {
		LOGGER.debug("Executing searchQuery controller...");
		model.addAttribute("searchQuery", searchQuery);
		model.addAttribute("searchResults", webSearchService.driver(searchQuery.getMessage()));
		//webSearchService.driver(searchQuery.getMessage());
		return "result";
	}

}
