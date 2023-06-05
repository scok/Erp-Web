package com.Erp.dto;

import com.Erp.constant.AccountCategory;
import com.Erp.constant.ProductCategory;
import com.Erp.constant.ProductDivisionCategory;
import com.Erp.entity.Product;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.format.DateTimeFormatter;

@Getter @Setter
public class ProductFormDto {

    private String prCode;

    private String prName;

    private Long prPrice;

    private ProductCategory prCategory;

    @Enumerated(EnumType.STRING)
    private ProductDivisionCategory prDivCategory;

    private String acName;

    private String prRegDate;

    private String prCreateName;

    @Enumerated(EnumType.STRING)
    private AccountCategory acCategory;

    public static ProductFormDto of(Product product){
        ProductFormDto productFormDto = new ProductFormDto();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String newRegDate = product.getRegDate().format(formatter);

        productFormDto.setPrCode(product.getPrCode());
        productFormDto.setPrName(product.getPrName());
        productFormDto.setPrPrice(product.getPrPrice());
        productFormDto.setPrCategory(product.getPrCategory());
        productFormDto.setPrDivCategory(product.getPrDivCategory());
        productFormDto.setAcName(product.getAccount().getAcName());
        productFormDto.setPrRegDate(newRegDate);
        productFormDto.setPrCreateName(product.getCreateName());
        productFormDto.setAcCategory(product.getAccount().getAcCategory());

        return productFormDto;
    }
}
