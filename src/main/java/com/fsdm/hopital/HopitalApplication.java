package com.fsdm.hopital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class HopitalApplication {
	public static void main(String[] args) {
		SpringApplication.run(HopitalApplication.class, args);
	}
}
