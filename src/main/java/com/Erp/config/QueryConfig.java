package com.Erp.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class QueryConfig {
    //엔터티 매니저 설정 파일

    @PersistenceContext
    EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaqueryFactory(){
        return new JPAQueryFactory(entityManager);
    }
}
