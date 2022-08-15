package com.softserveinc.ita.homeproject.homeoauthserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@PropertySource("classpath:application-home-oauth-server.properties")
@EntityScan(basePackages = "com.softserveinc.ita.homeproject.homedata")
@EnableJpaRepositories(basePackages = "com.softserveinc.ita.homeproject.homedata")
public class HomeOauthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeOauthServerApplication.class, args);
    }

}
