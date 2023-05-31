package com.Erp.controller;

import com.Erp.constant.MemberRole;
import com.Erp.dto.MemberInsertDto;
import com.Erp.entity.Member;
import com.Erp.entity.MemberImage;
import com.Erp.service.MemberImageServies;
import com.Erp.service.MemberService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/members")
@RequiredArgsConstructor //final 전용 어노테이션
public class MemberController {

    //로그인 페이지로 가는 메소드
    @GetMapping(value = "/login")
    public String LoginForm(){
        return "/member/memberLoginForm";
    }

    //form 태그와 SecurityConfig.java 파일에 정의 되어 있습니다.
    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        System.out.println("문제가 발생했습니다.");
        model.addAttribute("loginErrorMsg","Id 또는 비밀 번호를 확인해주세요");
        return "/member/memberLoginForm";
    }

    //회원가입 페이지로 가는 메소드
    @GetMapping(value = "/admin/new")
    public String memberInsertForm(Model model){
        model.addAttribute("memberInsertDto",new MemberInsertDto());
        return "/member/memberInsertForm";
    }

    private final PasswordEncoder passwordEncoder;

    private final MemberService memberService;
    private final MemberImageServies memberImageServies;

    private final JPAQueryFactory jpaQueryFactory;

    //회원가입을 눌렀을때. @Valid가 유효성 검사후 bindingResult에 에러가 존재하면 값을 넘겨줍니다.
    @PostMapping(value = "/admin/new")
    public String newMember(@Valid MemberInsertDto memberInsertDto, BindingResult bindingResult, Model model, MemberImage memberImage,
                            @RequestParam("memberImage")MultipartFile uploadedFile){
        String goPage="/member/memberInsertForm";

        System.out.println("포스트 방식 요청 들어옴");
        int count = 0;
        String role = String.valueOf(memberInsertDto.getRole());
        System.out.println(role);

        for (MemberRole target : MemberRole.values()){
            if(String.valueOf(target).equals(role)){
                count+=1;   //MemberRole에 맞는 값이 존재하면 +1
            }
        }
        if(count == 0){
            model.addAttribute("errorMessage","구분을 입력해주세요.");
            return goPage;
        }

        if (bindingResult.hasErrors() || uploadedFile.isEmpty()) { //오류 발생시 동작.
            if (uploadedFile.isEmpty()){
                model.addAttribute("errorMessage","이미지 등록은 필수입니다.");
            }
            return goPage;
        }
        try {
            Member member = Member.createid(memberInsertDto,passwordEncoder,this.jpaQueryFactory);
            memberService.saveMember(member);
            memberImageServies.saveImage(memberImage,uploadedFile);
        }catch (Exception e){
            model.addAttribute("errorMessage",e.getMessage());
            return "/member/memberInsertForm";
        }
        return "redirect:/"; //홈페이지
    }
}
