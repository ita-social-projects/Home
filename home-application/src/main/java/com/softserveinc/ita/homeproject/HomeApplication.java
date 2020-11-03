package com.softserveinc.ita.homeproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication (scanBasePackages = "com.softserveinc.ita.homeproject")
public class HomeApplication  {
    public static void main(String[] args) {
         SpringApplication.run(HomeApplication.class, args);
    }
}
