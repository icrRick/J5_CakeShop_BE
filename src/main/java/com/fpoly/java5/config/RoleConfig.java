package com.fpoly.java5.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fpoly.java5.compoment.RoleInterceptor;

@Configuration
public class RoleConfig implements WebMvcConfigurer {
    @Autowired
    RoleInterceptor roleInterceptor;
    @Override
    public void addInterceptors(@SuppressWarnings("null") InterceptorRegistry registry) {

        registry.addInterceptor(roleInterceptor).excludePathPatterns("/login", "/home", "/about", "/contact",
            "/forgot-pass","/reset-pass",    "/products","/products/search","/products/filter", "/menu","/register/step1","/register/step2" ,"/productdetail","/images/**", "/css/**", "/js/**", "/vendors/**", "/fonts/**","/img/**");
    }
}