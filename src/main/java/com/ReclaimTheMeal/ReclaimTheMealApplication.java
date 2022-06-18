package com.ReclaimTheMeal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages= {"com.ReclaimTheMeal.UserRepository"})
public class ReclaimTheMealApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReclaimTheMealApplication.class, args);
	}

}
