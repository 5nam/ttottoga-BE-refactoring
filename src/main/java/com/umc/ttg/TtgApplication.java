package com.umc.ttg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TtgApplication {

    public static void main(String[] args) {
        SpringApplication.run(TtgApplication.class, args);
    }

}
