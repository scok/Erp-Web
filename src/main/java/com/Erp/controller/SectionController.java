package com.Erp.controller;

import com.Erp.dto.SectionAddDto;
import com.Erp.dto.SectionFormDto;
import com.Erp.entity.Section;
import com.Erp.service.SectionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/section")
@RequiredArgsConstructor
public class SectionController {

    @GetMapping(value = "/list")
    public String goSectionList(){
        return "/section/sectionForm";
    }


    @Autowired
    private final SectionService sectionService;

    @GetMapping(value = "/check")
    public @ResponseBody Object SelectAll(){
        List<SectionFormDto> sectionFormDtoList = sectionService.sectionListAll();
        System.out.println(sectionFormDtoList.toString());

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", sectionFormDtoList);
        Object data = map;

        return data;
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


    // 저장하기
    @PostMapping(value = "/addSection")
    public @ResponseBody ResponseEntity addSection(@RequestBody SectionAddDto sectionAddDto) throws JsonProcessingException {
        System.out.println(sectionAddDto.toString());

        LocalDateTime regDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String strNowDate = regDateTime.format(formatter);

        int year = 0;
        int month = 0;
        int day = 0;

        year = Integer.parseInt(strNowDate.substring(0,4));
        month = Integer.parseInt(strNowDate.substring(4,6));
        day = Integer.parseInt(strNowDate.substring(6));
        System.out.println("연월일 확인 : " + year + month + day);

        int count = 0;
        count = sectionService.getSecCodeCount(year, month, day);
        System.out.println("count 확인 : " + count);

        Section section = Section.createSection(sectionAddDto, strNowDate, count);
        sectionService.saveSection(section);

        return new ResponseEntity<>( HttpStatus.OK);
    }


}
