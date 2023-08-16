package com.example.elderlinker;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class elderlinkConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}