package com.Erp.dto.logistics;


import com.Erp.constant.ProductionLine;
import com.Erp.constant.StackAreaCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

@Getter @Setter
public class ProductionFormDto {

    private Long proCode;   //생산실적 ID

    @JsonProperty("productionLine")
    @NotNull(message = "조립라인을 선택해 주세요.")
    @Enumerated(EnumType.STRING)
    private ProductionLine productionLine;  //조립라인

    private String meName;         //등록자 명

    @NotNull(message = "수량을 입력해주세요.")
    @Min(value = 0, message = "음수는 입력할 수 없습니다.")
    @JsonProperty("count")
    private Integer count;         //수량

    @NotBlank(message = "제품을 선택해주세요.")
    @JsonProperty("prCode")
    private String prCode;  //상품 코드

    private String prName;  //제품명

    private LocalDateTime registrationDate; //등록일자

    @JsonProperty("secCode")
    @NotNull(message = "창고를 선택해 주세요.")
    @Enumerated(EnumType.STRING)
    private String secCode; //창고 코드

    @JsonProperty("SACategory")
    @NotNull(message = "구역을 선택해 주세요.")
    @Enumerated(EnumType.STRING)
    private StackAreaCategory SACategory; //구역 카테고리

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static OrderSheetAddmDto OsMapMapper(Map<String,String> data){
        //Ajax로 받아온 데이터를 자동으로 알맞은 인스턴스 변수에 넣어줍니다.
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //이 경우는 웹에서 받아온 JSON 데이터에는 값이 존재하지만 POJO클래스에서는 값이 존재하지 않을경우
        //예외를 발생하지 않는다 라는 설정입니다.
        objectMapper.registerModule(new JavaTimeModule());
        //Json으로 받은 데이터를 Local Date Time 데이터 타입에 매핑 시켜주는 코드
        return objectMapper.convertValue(data, OrderSheetAddmDto.class);
    }
}
