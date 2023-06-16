package com.Erp.dto;

import com.Erp.constant.MemberStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberSearchDto {
    private MemberStatus memberStatus; // 재직 현황 (재직중, 퇴사)
    private String searchBy; // 검색 엔진
    private String searchQuery; // 검색 엔진
}
