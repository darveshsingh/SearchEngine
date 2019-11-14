package com.uwindsor.acc.searchengine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SearchEngineApplication {

	private static final Logger LOGGER = LogManager.getLogger(SearchEngineApplication.class);
	public static void main(String[] args) {
		LOGGER.debug("Starting up the Search Engine...");
		SpringApplication.run(SearchEngineApplication.class, args);
	}
}
