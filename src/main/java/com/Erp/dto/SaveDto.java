package com.Erp.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class SaveDto { // 손익계산서, 재무상태표 수정 시 항목에 대한 DTO

    private Long num; // 수정 값
    private String name; // 수정할 칼럼 이름
    private Integer value;

    public SaveDto(Long num, String name, Integer value) {
        this.num = num;
        this.name = name;
        this.value = value;
    }
}
