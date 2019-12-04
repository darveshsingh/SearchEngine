package websearchengine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebSearchEngineApplication {

	private static final Logger log = LogManager.getLogger(WebSearchEngineApplication.class);

	public static void main(String[] args) {
		if (log.isDebugEnabled()) {
			log.debug("Start: Search Web Spring Boot Application");
		}
		SpringApplication.run(WebSearchEngineApplication.class, args);
	}
}
