package com.example.bucket4jratelimitingsample.AppConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/*
* Finally, we add the custom interceptor to our WebMvcConfigurer
* and add it to the spring interceptor registry using a config class.
* We can add the API endpoint patterns for which our custom interceptor is to be used.
* If we want it for all the APIs in the application, we can simply say "/**".*/
@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Autowired
    private RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/**");
    }
}
