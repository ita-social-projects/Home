package com.softserveinc.ita.homeproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication (scanBasePackages = "com.softserveinc.ita.homeproject.config")
public class HomeApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
         //SpringApplication.run(HomeApplication.class, args);
         new HomeApplication().configure(new SpringApplicationBuilder(HomeApplication.class)).run(args);
    }
}
