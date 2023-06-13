package com.Erp.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class MemberPayInsertDto {

    private String id; //아이디
    private String name; // 이름
    private String birth; // 생년월일
    private String department; // 부서
    private String position; // 직위
    private String bank; // 계좌번호



    private Long salary; //기본급

    private Long bonus; // 상여금

    private Long nightPay; //야근수당

    private Long foodPay; //식비

    private Long carPay; //차량지원금

    private Long goInsurance; // 고용보험

    private Long gunInsurance; //건강보험

    private Long sanInsurance; //산재보험

    private Long kukInsurance; //국민연금

    private Long incomeTax; //소득세

    private Long localTax; //지방소득세

    private Long plusMoney; //지급 총액

    private Long minusMoney; //공제 총액

    private Long TotalMoney; //차감 지급액


}