package com.Erp.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class MemberImageDto {

    private Long id;

    private String imageName;//

    private String orImageName; //원본 이미지 이름

    private String imageUrl; //이미지 저장 주소

}
