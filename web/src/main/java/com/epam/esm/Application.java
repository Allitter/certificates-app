package com.epam.esm;

import com.epam.esm.config.ApplicationSecurityConfig;
import com.epam.esm.config.PasswordConfig;
import com.epam.esm.config.SpringConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(new Class[]{
                Application.class,
                SpringConfig.class,
                ApplicationSecurityConfig.class,
                PasswordConfig.class
        }, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }
}
