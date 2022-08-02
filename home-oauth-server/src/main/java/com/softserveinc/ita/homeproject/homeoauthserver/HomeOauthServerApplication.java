package com.softserveinc.ita.homeproject.homeoauthserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {"com.softserveinc.ita.homeproject.homedata.*"},
    exclude = {SecurityAutoConfiguration.class})
@PropertySource({"classpath:application-home-oauth-server.properties"})


public class HomeOauthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeOauthServerApplication.class, args);
    }

}
