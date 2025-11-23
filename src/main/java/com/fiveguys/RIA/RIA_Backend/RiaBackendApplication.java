package com.fiveguys.RIA.RIA_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RiaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RiaBackendApplication.class, args);
	}

}
