package com.Erp.dto.logistics;

import com.Erp.constant.AccountCategory;
import com.Erp.entity.logistics.Account;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;

@Getter@Setter@ToString
public class AccountAddDto {

    //@JsonProperty("acCode") Ajax로 받아온 데이터를 자동으로 알맞은 인스턴스 변수에 넣어줍니다.
    @JsonProperty("acCode")
    private String acCode;         //거래처 명

    @JsonProperty("acName")
    @NotEmpty(message = "거래처 명은 필수 입력 값입니다.")
    private String acName;         //거래처 명

    @JsonProperty("acCeo")
    @NotEmpty(message = "거래처 대표자명은 필수 입력 값입니다.")
    private String acCeo;          //거래처 대표자명

    @JsonProperty("acBusiness")
    @NotEmpty(message = "거래처 업태는 필수 입력 값입니다.")
    private String acBusiness;     //거래처 업태

    @JsonProperty("acEvent")
    @NotEmpty(message = "거래처 종목은 필수 입력 값입니다.")
    private String acEvent;        //거래처 종목

    @JsonProperty("acRN")
    @NotEmpty(message = "거래처 사업자 등록 번호는 필수 입력 값입니다.")
    private String acRN;           //사업자등록번호(registration num)

    @JsonProperty("acAddress")
    @NotEmpty(message = "거래처 주소는 필수 입력 값입니다.")
    private String acAddress;      //거래처  주소

    @JsonProperty("acAddressDetail")
    @NotEmpty(message = "거래처 상세 주소는 필수 입력 값입니다.")
    private String acAddressDetail; //거래처  상세주소

    @JsonProperty("acNumber")
    @NotEmpty(message = "거래처 번호는 필수 입력 값입니다.")
    private String acNumber;       //거래처 번호

    @JsonProperty("acFax")
    @NotEmpty(message = "거래처 팩스번호는 필수 입력 값입니다.")
    private String acFax;          //거래처 팩스번호

    @JsonProperty("acHomepage")
    @NotEmpty(message = "거래처 홈페이지는 필수 입력 값입니다.")
    private String acHomepage;     //거래처 사이트

    @JsonProperty("acCategory")
    @Enumerated(EnumType.STRING)
    private AccountCategory acCategory;     //거래처 구분

    private static ModelMapper modelMapper = new ModelMapper();

    public static AccountAddDto of(Account account){
        return modelMapper.map(account, AccountAddDto.class);
    }
}
