package com.Erp.dto.logistics;


import com.Erp.constant.AccountCategory;
import com.Erp.constant.DivisionStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Map;

@Getter @Setter@ToString
public class EstimateAddmDto{

    @JsonProperty("esCode")
    private String esCode;  //견적서 코드

    @JsonProperty("acCode")
    private String acCode;  //거래처 코드

    @JsonProperty("esTotalPrice")
    private Long esTotalPrice;    //총금액

    @JsonProperty("acCategory")
    @Enumerated(EnumType.STRING)
    private AccountCategory acCategory;     //거래 유형

    @JsonProperty("esComment")
    private String esComment;     //반려시 작성할 코멘트

    @JsonProperty("divisionStatus")
    @Enumerated(EnumType.STRING)
    private DivisionStatus divisionStatus;     //견적서 상태 유형

    @JsonProperty("esdId")
    private String esdId;     //견적서 디테일 id

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static EstimateAddmDto EsMapMapper(Map<String,String> data){
        //Ajax로 받아온 데이터를 자동으로 알맞은 인스턴스 변수에 넣어줍니다.
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //이 경우는 웹에서 받아온 JSON 데이터에는 값이 존재하지만 POJO클래스에서는 값이 존재하지 않을경우
        //예외를 발생하지 않는다 라는 설정입니다.
        return objectMapper.convertValue(data, EstimateAddmDto.class);
    }
}
