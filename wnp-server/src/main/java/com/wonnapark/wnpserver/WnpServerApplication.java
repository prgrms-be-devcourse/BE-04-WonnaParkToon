package com.wonnapark.wnpserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WnpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WnpServerApplication.class, args);
    }

}
