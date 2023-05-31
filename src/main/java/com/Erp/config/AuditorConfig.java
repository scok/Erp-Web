package com.Erp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditorConfig {
    //데이터 수정 및 생성 감시자
    @Bean
    public AuditorAware<String> auditorProvider(){
        return new AuditorAwareImpl();
    }



}
