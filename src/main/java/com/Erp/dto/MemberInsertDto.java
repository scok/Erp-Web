package com.Erp.dto;

import com.Erp.constant.MemberRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter@Setter@ToString
public class MemberInsertDto{

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;
    @NotEmpty(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotEmpty(message = "생년월일은 필수 입력 값입니다.")
    private String birth; //생년월일

    @Email(message = "이메일 형식으로 입력해 주세요.")
    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    private String email; //이메일

    @NotEmpty(message = "핸드폰 번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^010-?([0-9]{4})-?([0-9]{4})$",message = "휴대폰 번호를 재대로 입력해주세요.")
    private String phoneNumber; //핸드폰 번호

    @NotNull(message = "입사년월은 필수 입력 값입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotEmpty (message = "부서는 필수 입력 값입니다.")
    private String department;

    @NotEmpty (message = "직위는 필수 입력 값입니다.")
    private String position;

    @NotEmpty (message = "직급은 필수 입력 값입니다.")
    private String paystep;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

}
