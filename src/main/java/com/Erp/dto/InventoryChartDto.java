package com.Erp.dto;

import com.Erp.constant.ProductionLine;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@ToString
public class InventoryChartDto {

    private String secCode;

    private String prCode;

    private String secName;

    private String prName;

    private Long inQuantity;

    public InventoryChartDto(String secCode, String prCode, String secName, String prName , Long inQuantity) {
        this.secCode = secCode;
        this.prCode = prCode;
        this.secName = secName;
        this.prName = prName;
        this.inQuantity = inQuantity;
    }
}
