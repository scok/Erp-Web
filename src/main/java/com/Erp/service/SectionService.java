package com.Erp.service;

import com.Erp.dto.SectionFormDto;
import com.Erp.entity.Section;
import com.Erp.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;

    // 창고 목록에 보여줄 데이터
    public List<SectionFormDto> sectionListAll(){
        List<Section> sectionList = sectionRepository.findSectionAllY();
        List<SectionFormDto> sectionFormDtoList = new ArrayList<SectionFormDto>();

        for (Section section : sectionList){
            System.out.println(section.toString());
            SectionFormDto sectionFormDto = SectionFormDto.of(section);
            sectionFormDtoList.add(sectionFormDto);
        }
        return sectionFormDtoList;
    }


    // secCode 생성 시 필요한 count를 구함
    public Integer getSecCodeCount(int year, int month, int day){
        System.out.println("secCode 생성을 위한 count를 구합니다.");

        Integer count = 0;
        count = sectionRepository.getSecCodeCount(year, month, day);
        System.out.println("count 확인 : " + count);

        return count;
    }

    public Section findBySecCode(String code){
        System.out.println("code 값 확인 : " + code);
        return sectionRepository.findById(code).orElseThrow(EntityNotFoundException::new);
    }


    // 저장하기
    @Transactional
    public Section saveSection(Section section){
        System.out.println("입력된 데이터를 데이터베이스에 저장합니다.");
        System.out.println(section.toString());

        return sectionRepository.save(section);
    }


    //자재 창고만 가져옵니다.
    public Map<String,SectionFormDto> sectionMapMaterial() {
        List<Section> sectionList = sectionRepository.findSectionMaterial();
        Map<String ,SectionFormDto> map = new HashMap<String ,SectionFormDto>();
        int i = 1;
        for (Section section : sectionList){
            SectionFormDto sectionFormDto = SectionFormDto.of(section);
            map.put("data"+ String.valueOf(i),sectionFormDto);
            i++;
        }
        return map;
    }

    //제품 창고만 가져옵니다.
    public Map<String,SectionFormDto> sectionMapProduct() {
        List<Section> sectionList = sectionRepository.findSectionProduct();
        Map<String ,SectionFormDto> map = new HashMap<String ,SectionFormDto>();

        for (Section section : sectionList){
            int i = 1;
            SectionFormDto sectionFormDto = SectionFormDto.of(section);
            map.put("data"+ String.valueOf(i),sectionFormDto);
        }

        return map;
    }

    //자재 창고만 가져옵니다.
    public List<SectionFormDto> sectionListProduct() {
        List<Section> sectionList = sectionRepository.findSectionProduct();
        List<SectionFormDto> sectionFormDtoList = new ArrayList<SectionFormDto>();
        for (Section section : sectionList){
            SectionFormDto sectionFormDto = SectionFormDto.of(section);
            sectionFormDtoList.add(sectionFormDto);
        }

        return sectionFormDtoList;
    }
}
