package com.Erp.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class MemberImageDto {

    private Long id; // 이미지 아이디
    private String imageName;// 이미지 이름
    private String orImageName; // 원본 이미지 이름
    private String imageUrl; // 이미지 저장 주소
    private String member_id; // 멤버 사번
}
