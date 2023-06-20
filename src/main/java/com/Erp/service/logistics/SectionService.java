package com.Erp.service.logistics;

import com.Erp.dto.logistics.SectionAddDto;
import com.Erp.dto.logistics.SectionFormDto;
import com.Erp.entity.logistics.Section;
import com.Erp.repository.logistics.SectionRepository;
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
            SectionFormDto sectionFormDto = SectionFormDto.of(section);
            sectionFormDtoList.add(sectionFormDto);
        }
        return sectionFormDtoList;
    }

    // secCode 생성 시 필요한 count를 구함
    public int getSecCodeCount(int year, int month, int day){
        return sectionRepository.getSecCodeCount(year, month, day);
    }

    public Section findBySecCode(String code){
        return sectionRepository.findById(code).orElseThrow(EntityNotFoundException::new);
    }

    // 저장하기
    @Transactional
    public Section saveSection(Section section){
        return sectionRepository.save(section);
    }

    // 삭제하기
    @Transactional
    public void deleteSection(List<String> code){
        for(String item : code){
            if(item != null){
                Section section = sectionRepository.findById(item).orElseThrow(EntityNotFoundException::new);
                section.setPageYandN("N");
            }
        }
    }
    // 수정하기
    @Transactional
    public void updateSection(SectionAddDto sectionAddDto){
        Section updateSec = sectionRepository.findById(sectionAddDto.getSecCode()).orElseThrow(EntityNotFoundException::new);
        updateSec.setSecCategory(sectionAddDto.getSecCategory());
        updateSec.setSecName(sectionAddDto.getSecName());
        updateSec.setSecMaxCount(sectionAddDto.getSecMaxCount());
        updateSec.setSecAddress(sectionAddDto.getSecAddress()+" "+sectionAddDto.getSecAddressDetail());
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

    //특정 제품 창고를 가져옵니다.
    public SectionFormDto sectionProduct(String secCode) {
        Section section = sectionRepository.findById(secCode).orElseThrow(EntityNotFoundException::new);
        SectionFormDto sectionFormDto = SectionFormDto.of(section);

        return sectionFormDto;
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
