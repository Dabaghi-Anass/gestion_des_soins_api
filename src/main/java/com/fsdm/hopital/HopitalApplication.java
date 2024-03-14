package com.fsdm.hopital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HopitalApplication {
	@GetMapping("/")
	public String home() {
		return "Welcome to Hopital Application";
	}

	public static void main(String[] args) {
		SpringApplication.run(HopitalApplication.class, args);
	}

}
