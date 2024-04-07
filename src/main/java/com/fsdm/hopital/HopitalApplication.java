package com.fsdm.hopital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.*;

@SpringBootApplication
public class HopitalApplication {
	public static void main(String[] args) {
		Logger.getLogger("javax.mail").setLevel(Level.OFF);
		SpringApplication.run(HopitalApplication.class, args);
	}
}
