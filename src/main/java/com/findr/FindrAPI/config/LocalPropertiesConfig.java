package com.findr.FindrAPI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;


//this class is loaded at startup by spring
//this loads the local.properties file so i dont have to store urls and whatnot in github
@Configuration
public class LocalPropertiesConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new FileSystemResource("local.properties"));
        return configurer;
    }
}
