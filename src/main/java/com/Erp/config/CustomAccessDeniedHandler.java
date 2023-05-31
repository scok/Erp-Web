package com.Erp.config;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//로그인 한 사용자가 권한이 없는데 권한이 필요한(관리자) 페이지를 접속하려고 할 때 에러 페이지를 보내줍니다.
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 에러 페이지 경로 설정
    }
}
