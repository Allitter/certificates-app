package com.epam.esm;

import com.epam.esm.config.SpringConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(new Class[]{Application.class, SpringConfig.class}, args);
    }
}
