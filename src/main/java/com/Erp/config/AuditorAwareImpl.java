package com.Erp.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    //데이터 수정 생성의 감시자의 구현체
    @Override
    public Optional<String> getCurrentAuditor() {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId="";
        if(authentication != null){
            userId = authentication.getName();//.usernameParameter("id")을 반환
        }

        return Optional.of(userId);
    }
}
