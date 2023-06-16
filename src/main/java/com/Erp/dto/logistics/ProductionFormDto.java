package com.Erp.dto.logistics;


import com.Erp.constant.ProductionLine;
import com.Erp.constant.StackAreaCategory;
import com.Erp.entity.logistics.Production;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Getter @Setter@ToString
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

    private String prName;  //제품명

    private String secName; //창고명

    @JsonProperty("SACategory")
    @NotNull(message = "구역을 선택해 주세요.")
    @Enumerated(EnumType.STRING)
    private StackAreaCategory SACategory; //구역 카테고리

    private String registrationDate; //등록일자

    @JsonProperty("secCode")
    @NotNull(message = "창고를 선택해 주세요.")
    @Enumerated(EnumType.STRING)
    private String secCode; //창고 코드

    @NotBlank(message = "제품을 선택해주세요.")
    @JsonProperty("prCode")
    private String prCode;  //상품 코드

    public static ProductionFormDto of(Production production) {
        ProductionFormDto productionFormDto = new ProductionFormDto();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String newRegDate = production.getRegistrationDate().format(formatter);

        productionFormDto.setProCode(production.getProCode());
        productionFormDto.setProductionLine(production.getProductionLine());
        productionFormDto.setCount(production.getCount());
        productionFormDto.setMeName(production.getCreateName());
        productionFormDto.setSACategory(production.getStackAreaCategory());
        productionFormDto.setSecName(production.getSection().getSecName());
        productionFormDto.setRegistrationDate(newRegDate);
        productionFormDto.setPrName(production.getProduct().getPrName());

        return productionFormDto;
    }
}
