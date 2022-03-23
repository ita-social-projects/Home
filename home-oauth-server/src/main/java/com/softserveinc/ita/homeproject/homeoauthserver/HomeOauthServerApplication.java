package com.softserveinc.ita.homeproject.homeoauthserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class HomeOauthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeOauthServerApplication.class, args);
	}

}
