package com.Erp.dto.logistics;


import com.Erp.constant.SectionCategory;
import com.Erp.entity.logistics.Section;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

@Getter @Setter @ToString
public class SectionFormDto {
    private String secCode;
    private String secName;
    private SectionCategory secCategory;
    private int secTotalCount;
    private int secMaxCount;
    private String secAddress;
    private String inventoryLoadingRate;
    private String secCreateName;
    private String secRegDate;

    public static SectionFormDto of(Section section){
        SectionFormDto sectionFormDto = new SectionFormDto();

        sectionFormDto.setSecCode(section.getSecCode());
        sectionFormDto.setSecName(section.getSecName());
        sectionFormDto.setSecCategory(section.getSecCategory());
        sectionFormDto.setSecMaxCount(section.getSecMaxCount());
        sectionFormDto.setSecAddress(section.getSecAddress());
        sectionFormDto.setSecCreateName(section.getCreateName());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String newRegDate = section.getRegDate().format(formatter);
        sectionFormDto.setSecRegDate(newRegDate);

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
