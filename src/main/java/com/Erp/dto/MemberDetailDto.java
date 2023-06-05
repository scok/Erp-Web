package com.Erp.dto;

import com.Erp.constant.MemberRole;
import com.Erp.constant.MemberStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter@Setter@ToString
public class MemberDetailDto {

    private Long id;
    private String password;
    private String name;
    private String birth; //생년월일
    private String email; //이메일
    private String phone; //핸드폰 번호
    private String address; // 주소
    private LocalDate date;
    private String department;
    private String position;
    private String hobong;
    private String bank;
    private MemberStatus status;
    private MemberRole role;
    private String imageUrl ;
}
