package com.softserveinc.ita.homeproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringBootApplication
public class HomeGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(HomeGatewayApplication.class);
    }
}
