package com.Erp.dto.logistics;

import com.Erp.constant.AccountCategory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@ToString
public class AccountFormDto {

    private String acCode;         //구매처 코드

    private String acName;         //구매처 명

    private String acRegDate;    //등록일자

    private String acCreateName;            //생성자

    @Enumerated(EnumType.STRING)
    private AccountCategory acCategory;
}
