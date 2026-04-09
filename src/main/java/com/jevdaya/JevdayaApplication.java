package com.jevdaya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan("com.jevdaya.Entity")
public class JevdayaApplication {

	public static void main(String[] args) {
		SpringApplication.run(JevdayaApplication.class, args);
	}

}
