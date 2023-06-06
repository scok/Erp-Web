package com.Erp.dto.logistics;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter @Setter@ToString
public class EstimateDetailAddmDto{

    @JsonProperty("esdId")
    private Long esdId;        //견적서 코드

    @JsonProperty("esQuantity")
    private int esQuantity;         //수량

    @JsonProperty("esStandard")
    private String esStandard;    //규격

    @JsonProperty("esSupplyValue")
    private Long esSupplyValue;    //공급 가액

    @JsonProperty("esTaxAmount")
    private Long esTaxAmount;    //세액

    @JsonProperty("prCode")
    private String prCode; //상품 과 견적서 디테일과 매핑

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static EstimateDetailAddmDto EsdMapMapper(Map<String,String> data){
        //Ajax로 받아온 데이터를 자동으로 알맞은 인스턴스 변수에 넣어줍니다.
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //이 경우는 웹에서 받아온 JSON 데이터에는 값이 존재하지만 POJO클래스에서는 값이 존재하지 않을경우
        //예외를 발생하지 않는다 라는 설정입니다.
        return objectMapper.convertValue(data, EstimateDetailAddmDto.class);
    }
}
