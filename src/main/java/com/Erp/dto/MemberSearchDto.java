package com.Erp.dto;

import com.Erp.constant.MemberStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberSearchDto {
    private MemberStatus memberStatus;
    private String searchBy;
    private String searchQuery;
}
