package com.Erp.dto;

import com.Erp.constant.SectionCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class SectionAddDto {
    @JsonProperty("secName")
    private String secName;

    @JsonProperty("secCategory")
    private SectionCategory secCategory;

    @JsonProperty("secMaxCount")
    private int secMaxCount;

    @JsonProperty("secAddress")
    private String secAddress;
}
