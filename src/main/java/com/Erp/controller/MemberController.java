package com.Erp.controller;

import com.Erp.dto.*;
import com.Erp.entity.Member;
import com.Erp.entity.MemberImage;
import com.Erp.entity.MemberPay;
import com.Erp.service.MemberImageService;
import com.Erp.service.MemberPayService;
import com.Erp.service.MemberService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/members")
@RequiredArgsConstructor //final 전용 어노테이션
public class MemberController {

    private final PasswordEncoder passwordEncoder;
    private final MemberPayService memberPayService;
    private final MemberService memberService;
    private final MemberImageService memberImageService;
    private final JPAQueryFactory jpaQueryFactory;

    //로그인 페이지로 가는 메소드
    @GetMapping(value = "/login")
    public String LoginForm(){
        return "/member/memberLoginForm";
    }


    //form 태그와 SecurityConfig.java 파일에 정의 되어 있습니다.
    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        System.out.println("문제가 발생했습니다.");
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }


    //멤버 등록 페이지로 가는 메소드
    @GetMapping(value = "/admin/new")
    public String memberInsertForm(Model model){
        model.addAttribute("memberInsertDto",new MemberInsertDto());
        return "/member/memberInsertForm";
    }


    //멤버 리스트 페이지로 가는 메소드
    @GetMapping(value = {"/list",  "/list/{page}"})
    public String memberList(MemberSearchDto dto, @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,3) ;
        Page<Member> members = memberService.getMemberListPage(dto, pageable) ;

        model.addAttribute("members", members);
        model.addAttribute("searchDto", dto);
        model.addAttribute("maxPage", 5);

        return "/member/memberList";
    }


    //멤버 상세 페이지로 가는 메소드
    // (멤버 상세 페이지에 "멤버수정, 급여리스트, 급여등록, 급여상세, 급여수정 전부 다 매핑)
    // (modal 사용으로 인해 memberDetail.html 에 모든 코드가 다 있음)
    @GetMapping(value = "/admin/detail/{id}")
    public String memberDetail(@PathVariable Long id, Model model){
        MemberDetailDto memberDetailDto = memberService.getMemberWithImage2(id);
        MemberUpdateDto memberUpdateDto = memberService.getMemberWithImage(id);
        MemberPayInsertDto memberPayInsertDto = memberPayService.getMemberPayMemberInfo(id);
        List<MemberPay> memberPayList = memberPayService.getMemberPayList(id);
        MemberPayUpdateDto memberPayUpdateDto = memberPayService.getMemberPayMemberInfo2(id);

        model.addAttribute("memberPayUpdateDto", memberPayUpdateDto);
        model.addAttribute("memberPayInsertDto", memberPayInsertDto);
        model.addAttribute("memberDetailDto", memberDetailDto);
        model.addAttribute("memberUpdateDto", memberUpdateDto);
        model.addAttribute("memberPayList", memberPayList);

        return "/member/memberDetail";
    }

    //급여 리스트 페이지로 가는 메소드
    @GetMapping(value = {"/admin/payList",  "/admin/payList/{page}"})
    public String memberPayList(MemberPaySearchDto dto, @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,3) ;
        Page<MemberPay> memberPays = memberPayService.getMemberPayListPage(dto, pageable) ;
        MemberPayUpdateDto memberPayUpdateDto = new MemberPayUpdateDto();

        model.addAttribute("memberPayUpdateDto", memberPayUpdateDto);
        model.addAttribute("memberPays", memberPays);
        model.addAttribute("searchDto", dto);
        model.addAttribute("maxPage", 10);

        return "/member/memberPayList";
    }


    // 멤버 급여 등록 모달에서 저장을 눌렀을 때
    @PostMapping(value = "/admin/payInsertNew/{id}")
    public String memberPayInsertFormNew(@PathVariable String id, MemberPayInsertDto memberPayInsertDto, Model model){
        System.out.println("급여등록 포스트 방식 요청 들어옴");
        Member member = memberService.getMemberById(Long.valueOf(id));

        try {
            MemberPay memberPay = MemberPay.insertPay(memberPayInsertDto, member);
            memberPayService.saveMemberPay(memberPay);

        }catch (Exception e){
            model.addAttribute("errorMessage",e.getMessage());
            return "/member/memberDetail";
        }
        return "redirect:/members/admin/detail/" + id;
    }


    // 마이 페이지로 가는 메소드
    @GetMapping(value = "/mypage/{id}")
    public String memberMyPage(@PathVariable Long id, Model model, Principal principal) {
        String loggedInUserId = principal.getName(); // 로그인된 사용자의 ID를 가져옴

        if (id.equals(Long.parseLong(loggedInUserId))) {
            MemberDetailDto memberDetailDto = memberService.getMemberWithImage2(id);

            MemberUpdateDto memberUpdateDto = memberService.getMemberWithImage(id);
            MemberPayInsertDto memberPayInsertDto = memberPayService.getMemberPayMemberInfo(id);
            List<MemberPay> memberPayList = memberPayService.getMemberPayList(id);
            MemberPayUpdateDto memberPayUpdateDto = memberPayService.getMemberPayMemberInfo2(id);

            model.addAttribute("memberPayUpdateDto", memberPayUpdateDto);
            model.addAttribute("memberPayInsertDto", memberPayInsertDto);
            model.addAttribute("memberUpdateDto", memberUpdateDto);
            model.addAttribute("memberPayList", memberPayList);

            model.addAttribute("memberDetailDto", memberDetailDto);
            return "/member/memberMyPage";
        } else {
            return "redirect:/"; //홈페이지
        }
    }


    // 멤버 등록을 눌렀을때. @Valid가 유효성 검사후 bindingResult에 에러가 존재하면 값을 넘겨줍니다.
    @PostMapping(value = "/admin/new")
    public ModelAndView newMember(MemberInsertDto memberInsertDto, Model model, MemberImage memberImage, @RequestParam("memberImage")MultipartFile uploadedFile){

        System.out.println("포스트 방식 요청 들어옴");

        try {
            Member member = Member.createid(memberInsertDto, passwordEncoder, this.jpaQueryFactory);
            memberService.saveMember(member);
            memberImageService.saveImage(memberImage, uploadedFile, member);
        } catch (Exception e) {
            String errorMessage = e.getMessage();

            // sweetAlert2를 사용하여 에러 메시지를 표시하는 JavaScript 코드를 추가합니다.
            StringBuilder scriptBuilder = new StringBuilder();
            scriptBuilder.append("Swal.fire({");
            scriptBuilder.append("  icon: 'error',");
            scriptBuilder.append("  title: 'Error!',");
            scriptBuilder.append("  text: '" + errorMessage + "',");
            scriptBuilder.append("})");

            // sweetAlert2 스크립트를 모델에 추가합니다.
            ModelAndView modelAndView = new ModelAndView("/member/memberInsertForm");
            modelAndView.addObject("sweetAlertScript", scriptBuilder.toString());
            return modelAndView;
        }

        return new ModelAndView("redirect:/"); // 홈페이지
    }


    // 멤버 수정을 눌렀을때. @Valid가 유효성 검사후 bindingResult에 에러가 존재하면 값을 넘겨줍니다.
    @PostMapping(value = "/admin/detail/{id}")
    public String updateMember(@PathVariable String id, MemberUpdateDto memberUpdateDto, Model model, MemberImage memberImage, @RequestParam("memberImage")MultipartFile uploadedFile){

        System.out.println("포스트 방식 요청 들어옴");

        try {
            Member existingMember = memberService.getMemberById(Long.valueOf(id));

            Member member = Member.updateid(memberUpdateDto, existingMember, passwordEncoder);
            memberService.updateMember(member);
            memberImageService.updateImage(memberImage,uploadedFile, member, id);
        }catch (Exception e){
            model.addAttribute("errorMessage",e.getMessage());
            return "/member/memberDetail";
        }
        return "redirect:/members/admin/detail/" + id;
    }


    // 급여 수정 모달 에서 저장을 눌렀을 때
    @PostMapping(value = "/admin/payUpdateNew/{id}/{payId}")
    public String memberPayUpdateFormNew(@PathVariable String id, @PathVariable String payId, MemberPayUpdateDto memberPayUpdateDto, Model model){
        System.out.println("급여 수정 포스트 방식 요청 들어옴");
        Member member = memberService.getMemberById(Long.valueOf(id));

        try {
            MemberPay memberPay = MemberPay.updatePay(memberPayUpdateDto, member, payId);
            memberPayService.updateMemberPay(memberPay);


        }catch (Exception e){
            model.addAttribute("errorMessage",e.getMessage());
            return "/member/memberDetail";
        }
        return "redirect:/members/admin/detail/" + id;
    }

    @PostMapping(value = "/admin/payUpdateNew2/{id}/{payId}")
    public String memberPayUpdateFormNew2(@PathVariable String id, @PathVariable String payId, MemberPayUpdateDto memberPayUpdateDto, Model model){
        System.out.println("급여 수정 포스트 방식 요청 들어옴");
        Member member = memberService.getMemberById(Long.valueOf(id));

        try {
            MemberPay memberPay = MemberPay.updatePay(memberPayUpdateDto, member, payId);
            memberPayService.updateMemberPay(memberPay);


        }catch (Exception e){
            model.addAttribute("errorMessage",e.getMessage());
            return "redirect:/members/admin/payList";
        }
        return "redirect:/members/admin/payList";
    }
}