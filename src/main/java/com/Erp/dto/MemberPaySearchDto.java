package com.Erp.dto;

import com.Erp.constant.MemberStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberPaySearchDto {
    private String searchBy; // 검색 엔진
    private String searchQuery; // 검색 엔진
}
