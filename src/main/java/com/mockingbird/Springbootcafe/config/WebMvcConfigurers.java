package com.mockingbird.Springbootcafe.config;

import com.mockingbird.Springbootcafe.interceptor.LoginInterceptor;
import com.mockingbird.Springbootcafe.interceptor.OtherInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class WebMvcConfigurers implements WebMvcConfigurer {

    @Bean
    public OtherInterceptor getOtherIntercepter() {
        return new OtherInterceptor();
    }

    @Bean
    public LoginInterceptor getLoginIntercepter() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(getOtherIntercepter())
                .addPathPatterns("/**");
        registry.addInterceptor(getLoginIntercepter())
                .addPathPatterns("/**");
    }
}