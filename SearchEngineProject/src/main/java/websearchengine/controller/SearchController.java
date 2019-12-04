package websearchengine.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import websearchengine.models.SearchQuery;
import websearchengine.searchservices.HtmlToTextConverter;
import websearchengine.websearchservice.websearchserviceImpl.WebSearchServiceImpl;

@Controller
public class SearchController {

	private static final Logger log = LogManager.getLogger(SearchController.class);

	@Autowired
	HtmlToTextConverter htmlToTextConverter;

	@Autowired
	WebSearchServiceImpl webSearchService;

	@Value("${spring.application.name}")
	String appName;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String webSearchEngineStartUp(Model model) {
		if (log.isDebugEnabled())
			log.debug("Executing searchEngineStartUp controller...");
		htmlToTextConverter.readHtmlFiles();
		model.addAttribute("appName", appName);
		model.addAttribute("searchQuery", new SearchQuery());
		return "Main";
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String webSearchQuery(@ModelAttribute SearchQuery webSearchQuery, Model model) {
		if (log.isDebugEnabled())
			log.debug("Executing searchQuery controller...");
		model.addAttribute("searchQuery", webSearchQuery);
		model.addAttribute("searchResults", webSearchService.search(webSearchQuery.getMessage()));
		return "SearchResult";
	}
}
