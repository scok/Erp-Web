package com.Erp.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class SaveDto {

    private Long num;
    private String name;
    private Integer value;

    public SaveDto(Long num, String name, Integer value) {
        this.num = num;
        this.name = name;
        this.value = value;
    }
}
