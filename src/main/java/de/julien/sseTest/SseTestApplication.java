package de.julien.sseTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class SseTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SseTestApplication.class, args);
	}

}
