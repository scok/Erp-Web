package com.Erp.dto.logistics;


import com.Erp.constant.SectionCategory;
import com.Erp.entity.logistics.Section;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter @ToString
public class SectionAddDto {

    @JsonProperty("secCode")
    private String secCode;

    @JsonProperty("secName")
    private String secName;

    @JsonProperty("secCategory")
    private SectionCategory secCategory;

    @JsonProperty("secMaxCount")
    private int secMaxCount;

    @JsonProperty("secAddress")
    @NotBlank(message = "주소는 필수입니다.")
    private String secAddress;

    @JsonProperty("secAddressDetail")
    @NotBlank(message = "상세 주소는 필수입니다.")
    private String secAddressDetail;

    private static ModelMapper modelMapper = new ModelMapper();

    public static SectionAddDto of(Section section){
        return modelMapper.map(section, SectionAddDto.class);
    }
}
