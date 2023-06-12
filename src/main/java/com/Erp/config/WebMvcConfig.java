package com.Erp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${uploadPath}") //application.properties에 설정된 변수 값을 가져옵니다.
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                // "/images"으로 시작하는 url 요청시 uploadPath에 설정된 폴더를 기준으로 읽어 옵니다.
                .addResourceLocations(uploadPath) ; // 로컬 컴퓨터에 저장된 파일을 읽어 들일 root 경로를 지정합니다.
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE");
    }
}
