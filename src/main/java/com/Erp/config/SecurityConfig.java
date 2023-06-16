package com.Erp.config;

import com.Erp.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration//이 클래스를 설정 파일로 사용할게요.
@EnableWebSecurity//스프링 시큐리티를 사용할게요.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/members/login","/members/login/error").permitAll()    // 모든 사용자가 접속할 수 있는 영역.
                .mvcMatchers("/members/admin/**").hasRole("ADMIN") //권한이 부여된 사용자 외 접근 불가능하게 하는 설정.
                .anyRequest().authenticated()//그 외엔 모든 사용자가 인증 절차를 가져야 함을 설정
                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler()) // AccessDeniedHandler 설정
                .and()
                .formLogin().loginPage("/members/login")    //로그인 페이지 설정
                .defaultSuccessUrl("/",true)
                // 로그인 완료 후 가는 페이지 true == 무조건 설정한 페이지로 가게하는 매개변수
                .usernameParameter("id")//로그인 시 id를 파라미터로 받는다
                .failureUrl("/members/login/error")//로그인시 에러 처리될때 가는 페이지 설정
                .and()
                .logout()//로그아웃 구현
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))//로그 아웃 페이지
                .addLogoutHandler(new CustomLogoutHandler())
                .logoutSuccessUrl("/members/login"); //로그아웃후 가는 페이지
        http .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());
        //로그인 안한 사용자가 어떤 페이지를 접속하려할 때 처리해주는 함수.
    }

    //로그인 되어야만 권한 인증을하기때문에 그 외 css,js,img파일에 문제가 생긴다. 그것을 제외하기 위한 코드
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**","/js/**","/img/**");
    }

    @Autowired
    MemberService memberService;

//    AuthenticationManagerBuilder는 AuthenticationManager(인증 관리자) 객체를 생성하는 역할을 합니다. ;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
