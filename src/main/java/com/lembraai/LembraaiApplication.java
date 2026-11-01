package com.lembraai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LembraaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LembraaiApplication.class, args);
	}
}