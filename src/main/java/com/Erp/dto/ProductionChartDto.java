package com.Erp.dto;

import com.Erp.constant.ProductionLine;
import com.Erp.constant.StackAreaCategory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@ToString
public class ProductionChartDto {

    private Long totalCount;

    private String prName;

    @Enumerated(EnumType.STRING)
    private ProductionLine productionLine;

    public ProductionChartDto(ProductionLine productionLine,String prName ,Long totalCount) {
        this.productionLine = productionLine;
        this.prName = prName;
        this.totalCount = totalCount;
    }
}
