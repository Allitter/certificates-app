package com.epam.esm.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EntityScan(basePackages = {"com.epam.esm.model", "com.epam.esm.audit"})
public class SpringConfig implements WebMvcConfigurer {
    private static final String EXCEPTION_MESSAGE_BUNDLE = "exception.message";
    private static final String DEFAULT_ENCODING = "UTF-8";

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(EXCEPTION_MESSAGE_BUNDLE);
        messageSource.setDefaultEncoding(DEFAULT_ENCODING);
        return messageSource;
    }
}
