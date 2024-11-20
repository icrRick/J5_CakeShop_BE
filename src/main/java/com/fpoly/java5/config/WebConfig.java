package com.fpoly.java5.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer{
@Override
    public void addResourceHandlers(@SuppressWarnings("null") ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
        .addResourceLocations("file:./images/");
        registry.addResourceHandler("/**")
        .addResourceLocations("classpath:/static/");
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
