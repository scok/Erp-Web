package com.Erp.controller.logistics;

import com.Erp.dto.UserDto;
import com.Erp.dto.logistics.SectionAddDto;
import com.Erp.dto.logistics.SectionFormDto;
import com.Erp.entity.Member;
import com.Erp.entity.logistics.Section;
import com.Erp.service.MemberService;
import com.Erp.service.logistics.SectionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping(value = "/section")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;
    private final MemberService memberService;

    @GetMapping(value = "/list")
    public String goSectionList(){
        return "/section/sectionForm";
    }

    @GetMapping(value = "/check")
    public @ResponseBody Object SelectAll(){
        List<SectionFormDto> sectionFormDtoList = sectionService.sectionListAll();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", sectionFormDtoList);
        Object data = map;

        return data;
    }
    // 저장하기
    @PostMapping(value = "/addSection")
    public @ResponseBody ResponseEntity addSection(@RequestBody @Valid SectionAddDto sectionAddDto, BindingResult error, Principal principal, HttpServletRequest request){

        boolean department = this.getSession(request);
        if(!department){
            return new ResponseEntity<String>("등록 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        StringBuilder errorMessages = new StringBuilder();

        if (error.hasErrors()) {
            for (FieldError errorBean : error.getFieldErrors()) {
                errorMessages.append(errorBean.getDefaultMessage() + "\n");
            }
            return new ResponseEntity<String>(errorMessages.toString(), HttpStatus.BAD_REQUEST);
        }

        if(sectionAddDto.getSecCode() == null || sectionAddDto.getSecCode().trim() == "") { // 신규등록
            LocalDateTime regDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String strNowDate = regDateTime.format(formatter);

            int year = Integer.parseInt(strNowDate.substring(0,4));
            int month = Integer.parseInt(strNowDate.substring(4,6));
            int day = Integer.parseInt(strNowDate.substring(6));

            int count = sectionService.getSecCodeCount(year, month, day);
            Member member = memberService.getMemberName(principal.getName());

            Section section = Section.createSection(sectionAddDto, strNowDate, count, member.getName());
            sectionService.saveSection(section);

        }else { // 기존 데이터 수정
            sectionService.updateSection(sectionAddDto);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 수정할 데이터 불러오기
    @PostMapping(value = "updateSection")
    public @ResponseBody ResponseEntity updateSection(@RequestBody String code,HttpServletRequest request){

        boolean department = this.getSession(request);
        if(!department){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        code = code.substring(1,code.length()-1);
        Section section = sectionService.findBySecCode(code);
        SectionAddDto sectionAddDto = SectionAddDto.of(section);
        sectionAddDto.setSecAddress("");

        return new ResponseEntity<>(sectionAddDto, HttpStatus.OK);
    }

    // 삭제하기
    @PostMapping(value = "/deleteSection")
    public @ResponseBody ResponseEntity deleteSection(@RequestBody List<String> code, HttpServletRequest request){

        boolean department = this.getSession(request);
        if(!department){
            return new ResponseEntity<String>("삭제 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        sectionService.deleteSection(code);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 불러오기
    @GetMapping(value = "/getSectionsMaterial")
    public @ResponseBody Map<String,SectionFormDto> getSectionsMaterial() {
        Map<String,SectionFormDto> sectionFormDtoList = sectionService.sectionMapMaterial();

        return sectionFormDtoList;
    }

    @GetMapping(value = "/getSectionsProduct")
    public @ResponseBody Map<String,SectionFormDto> getSectionsProduct() {
        Map<String,SectionFormDto> sectionFormDtoList = sectionService.sectionMapProduct();

        return sectionFormDtoList;
    }

    public boolean getSession(HttpServletRequest request){

        HttpSession session = request.getSession();
        UserDto user = (UserDto) session.getAttribute("User");

        boolean department = false;

        if(user.getDepartment().equals("물류팀")) {
            department = true;
            return department;
        }else {
            return department;
        }
    }

}