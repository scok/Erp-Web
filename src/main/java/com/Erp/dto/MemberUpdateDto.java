package com.Erp.dto;

import com.Erp.constant.MemberRole;
import com.Erp.constant.MemberStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Getter@Setter@ToString
public class MemberUpdateDto {

    private Long id; // 사번

    private String password; // 패스워드

    private String name; // 이름

    private String birth; // 생년월일

    private String email; // 이메일

    private String phone; // 핸드폰 번호

    private String address; // 주소

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date; // 입사 날짜

    private String department; // 부서

    private String position; // 직위

    private String hobong; // 호봉

    private String bank; // 계좌 번호

    @Enumerated(EnumType.STRING)
    private MemberStatus status; // 재직 현황 (재직중, 퇴사)

    @Enumerated(EnumType.STRING)
    private MemberRole role; // 구분 (관리자, 사용자)

    private String imageUrl ; // 이미지 저장 주소
}
