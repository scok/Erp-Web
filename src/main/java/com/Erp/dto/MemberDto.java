package com.Erp.dto;

import com.Erp.constant.MemberRole;
import com.Erp.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter@Setter
public class MemberDto extends BaseEntity {

    private String id;  //사원번호
    private String password;    //password
    private String name;
    private Long salary;
    private Long compensation;
    private String birth; //생년월일
    private String email; //이메일
    private String phoneNumber; //핸드폰 번호
    private LocalDate date; //입사년월
    private String department; //부서
    private String position; //직위
    private String paystep; //직급
    private MemberRole role;//사용자&관리자

    public MemberDto(String id, String password, String name, Long salary, Long compensation, String birth, String email, String phoneNumber, LocalDate date, String department, String position, String paystep, MemberRole role) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.salary = salary;
        this.compensation = compensation;
        this.birth = birth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.department = department;
        this.position = position;
        this.paystep = paystep;
        this.role = role;
    }
}
