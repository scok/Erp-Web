package com.Erp.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//로그아웃시 세션에 있는 데이터를 삭제할 클래스
public class CustomLogoutHandler implements LogoutHandler{

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        HttpSession session = request.getSession();
        session.removeAttribute("user");

        String check = (String)session.getAttribute("user");
        if(check == null || check.isEmpty() ){
            System.out.println("세션영역에는 데이터가 없습니다.");
        }
    }
}
