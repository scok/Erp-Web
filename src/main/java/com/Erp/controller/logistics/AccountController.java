package com.Erp.controller.logistics;

import com.Erp.dto.UserDto;
import com.Erp.dto.logistics.AccountAddDto;
import com.Erp.dto.logistics.AccountFormDto;
import com.Erp.entity.logistics.Account;
import com.Erp.entity.Member;
import com.Erp.service.logistics.AccountService;
import com.Erp.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AccountController {

    //구매처 서비스
    private final AccountService accountService;
    private final MemberService memberService;

    //구매 관리 거래처 페이지로 가는 메소드
    @GetMapping(value = "/buys/list")
    public String goBuyForm(){
        return "account/buyAccountForm";
    }

    //영업 관리 거래처 페이지로 가는 메소드
    @GetMapping(value = "/sellers/list")
    public String goSellerForm(){
        return "account/sellerAccountForm";
    }

    //구매 관리 거래처 페이지에 거래처 테이블에 등록된 데이터를 json 타입으로 넘겨주는 메소드
    @GetMapping(value = "/buys/check")
    public @ResponseBody Object buySelectAll(){

        List<AccountFormDto> accountFormDtos = accountService.buySelectAll();
        Map<String, Object> mp = new HashMap<String, Object>();
        mp.put("data", accountFormDtos);
        Object data = mp;

        return data;
    }

    //영업 관리 거래처 데이터 베이스에 등록된 데이터를 json 타입으로 넘겨주는 메소드
    @GetMapping(value = "/sellers/check")
    public @ResponseBody Object sellerSelectAll(){

        List<AccountFormDto> accountFormDtos = accountService.sellerSelectAll();
        Map<String, Object> mp = new HashMap<String, Object>();
        mp.put("data", accountFormDtos);
        Object data = mp;

        return data;
    }
    //구매관리 거래처 페이지에서 거래처를 등록 또는 수정 요청을 하면 호출되는 메소드
    @PostMapping(value = "/accounts/addAccount")
    public @ResponseBody ResponseEntity addBuyAccount(@RequestBody @Valid AccountAddDto data, BindingResult error, Principal principal, HttpServletRequest request){

        boolean department = this.getSession(request);

        if(!department){
            return new ResponseEntity<String>("작성 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        StringBuilder erromessages = new StringBuilder();
        if (error.hasErrors()) {
            for (FieldError errorBean : error.getFieldErrors()) {
                erromessages.append(errorBean.getDefaultMessage() + "\n");
            }
            return new ResponseEntity<String>(erromessages.toString(), HttpStatus.BAD_REQUEST);
        }
        Member member = memberService.getMemberName(principal.getName());

        if (data.getAcCode() == null || data.getAcCode().trim() == "") {//만약 코드에 유의미한 값이 들어 있다면 수정기능을 사용합니다.
            //여기는 등록 기능입니다.
            int count = 0;

            LocalDateTime regDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String strNowDate = regDateTime.format(formatter);

            int year = Integer.parseInt(strNowDate.substring(0, 4));
            int month = Integer.parseInt(strNowDate.substring(4, 6));
            int day = Integer.parseInt(strNowDate.substring(6));
            count = accountService.accountCount(year, month, day);

            Account account = Account.setAccount(data, count, strNowDate, member.getName());
            accountService.saveAccount(account);
        } else {
            //여기는 수정기능입니다.
            accountService.updateBuy(data, member.getName());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //구매, 영업 거래처 페이지에 데이터 1건을 보내줍니다.
    @PostMapping(value = "/accounts/updateAccount")
    public @ResponseBody ResponseEntity buyUpdateData(@RequestBody String code, HttpServletRequest request){

        boolean department = this.getSession(request);

        if(!department){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }
        //현재 문제점 Ajax로 넘겨 받은 데이터안에 ""가 붙어있어서 문제 발생.
        code = code.substring(1,code.length()-1);

        Account buy = accountService.findByAcCode(code);

        AccountAddDto accountAddDto = AccountAddDto.of(buy); //modelMapper 엔터티를 Dto로 전환
        accountAddDto.setAcAddress(""); //주소 초기화 작업

        return new ResponseEntity<>(accountAddDto, HttpStatus.OK);
    }

    //구매,판매 거래처를 삭제합니다.
    @PostMapping(value = "/accounts/deleteAccount")
    public @ResponseBody ResponseEntity deleteBuyAccount(@RequestBody List<String> code, HttpServletRequest request) {

        boolean department = this.getSession(request);
        if(!department){
            return new ResponseEntity<String>("삭제 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }
        accountService.deleteBuy(code);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    //상품에서 거래처를 선택하면 거래처의 거래 구분을 확인하여 상품 분류select 박스를 체크해주는 로직.
    @PostMapping(value = "/accounts/getAccountCategory")
    public @ResponseBody ResponseEntity getAccountCategory(@RequestBody String code) {
        code = code.substring(1,code.length()-1);
        String prDivCategory ="";
        Account account = accountService.findByAcCode(code);
        if(String.valueOf(account.getAcCategory()) == "구매") {
            prDivCategory = "자재";
        }else if(String.valueOf(account.getAcCategory()) == "판매"){
            prDivCategory = "제품";
        }
        return new ResponseEntity<String>(prDivCategory, HttpStatus.OK);
    }

    public boolean getSession(HttpServletRequest request){

        HttpSession session = request.getSession();
        UserDto user = (UserDto) session.getAttribute("User");

        boolean department = false;

        if(user.getDepartment().equals("구매팀") || user.getDepartment().equals("영업팀")) {
            department = true;
            return department;
        }else {
            return department;
        }
    }
}