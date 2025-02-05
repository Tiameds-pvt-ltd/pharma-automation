package com.pharma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.pharma")	// Scan all packages in the com.pharma package
public class PharmaApplication {
	public static void main(String[] args) {
		SpringApplication.run(PharmaApplication.class, args);
	}
}
