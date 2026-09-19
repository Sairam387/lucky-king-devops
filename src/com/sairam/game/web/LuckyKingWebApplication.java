package com.sairam.game.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LuckyKingWebApplication {

    public static void main(String[] args) {

        SpringApplication application =
                new SpringApplication(LuckyKingWebApplication.class);

        application.setWebApplicationType(WebApplicationType.SERVLET);

        application.run(args);
    }
}
