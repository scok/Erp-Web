package com.Erp.dto;

import com.Erp.constant.SectionCategory;
import com.Erp.entity.Section;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class SectionFormDto {
    private String secCode;
    private String secName;
    private SectionCategory secCategory;
    private int secTotalCount;
    private int secMaxCount;
    private String secAddress;
    private String inventoryLoadingRate;

    public static SectionFormDto of(Section section){
        SectionFormDto sectionFormDto = new SectionFormDto();

        sectionFormDto.setSecCode(section.getSecCode());
        sectionFormDto.setSecName(section.getSecName());
        sectionFormDto.setSecCategory(section.getSecCategory());
        sectionFormDto.setSecMaxCount(section.getSecMaxCount());
        sectionFormDto.setSecAddress(section.getSecAddress());

        if(section.getSecTotalCount() != 0){
            sectionFormDto.setSecTotalCount(section.getSecTotalCount());
            double inventoryLoadingRate =
                    (Double.valueOf(section.getSecTotalCount())/Double.valueOf(section.getSecMaxCount())) * 100;
            String formattedValue = String.format("%.1f", inventoryLoadingRate);
            sectionFormDto.setInventoryLoadingRate(formattedValue+"%");
        }else{
            sectionFormDto.setSecTotalCount(0);
            sectionFormDto.setInventoryLoadingRate("0.0%");
        }
        return sectionFormDto;
    }

}
