package com.Erp.controller;

import com.Erp.dto.UserDto;
import com.Erp.dto.logistics.MaterialDeliveryFormDto;
import com.Erp.dto.logistics.OrderSheetFormDto;
import com.Erp.dto.logistics.ProductFormDto;
import com.Erp.dto.logistics.ProductionFormDto;
import com.Erp.entity.logistics.MaterialDelivery;
import com.Erp.entity.logistics.OrderSheet;
import com.Erp.entity.logistics.OrderSheetDetail;
import com.Erp.entity.logistics.Production;
import com.Erp.repository.MemberRepository;
import com.Erp.service.logistics.MaterialDeliveryService;
import com.Erp.service.logistics.OrderSheetService;
import com.Erp.service.logistics.ProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    // 로그인이 완료 되면 무조건 홈 페이지로 오기때문에 홈 컨트롤러에서 dsl을 이용하여 로그인한 유저의 정보를 가져 옵니다.
    // 정보를 ssession 영역에 바인딩 후 로그아웃할때 session을 삭제 시켜버립니다.

    private final MemberRepository memberRepository;

    @GetMapping(value = "/")
    public String goHome(HttpSession session) {  //세션에 넣는 작업은 로그인 성공시 1번만 가능하게 새로운 설정파일을 만들어야함.
        String sessionId = "User";
        if (session.getAttribute(sessionId) == null ){//세션 영역에 데이터가 없을때만 세션에 바인딩 합니다.
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) { // 유저 정보가 null일때 대비
                Object principal = authentication.getPrincipal();
                if (principal instanceof User) {
                    User user = (User) principal;
                    String id = user.getUsername();//로그인한 유저의 id값을 가져옵니다.

                    UserDto result = memberRepository.findMemberInfo(id);
                    //유저 정보를 데이터 베이스와 연동하여 필요한 값만 가져옵니다

                    if (result != null) {
                        System.out.println("세션영역에 데이터를 바인딩 합니다.");
                        session.setAttribute(sessionId, result);
                    }
                }
            }
        }else {
            System.out.println("세션 영역에 이미 정보가 있습니다.");
        }
        return "redirect:http://localhost:3000/";
        //return "/";
    }
}
