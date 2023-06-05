package com.Erp.dto;

import com.Erp.constant.ProductionLine;
import com.Erp.constant.StackAreaCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;

@Getter @Setter@ToString
public class MaterialDeliveryAddDto {

    @JsonProperty("secCode")
    private String secCode; // 창고코드

    @JsonProperty("inId")
    private Long inId; // 재고 정보

    @JsonProperty("stackAreaCategory")
    private StackAreaCategory stackAreaCategory; // 구역정보

    @JsonProperty("productionLine")
    private ProductionLine productionLine; // 자재를 불출받을 공장라인

    @JsonProperty("maDeliveryCount")
    @Min(value = 0,message = "0이상 입력해주세요.")
    private int maDeliveryCount; // 불출수량
}
