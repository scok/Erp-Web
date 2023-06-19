package com.Erp.dto.logistics;


import com.Erp.constant.ProductCategory;
import com.Erp.constant.ProductDivisionCategory;
import com.Erp.entity.logistics.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter@ToString
public class ProductAddDto {

    @JsonProperty("acCode")
    private String acCode;

    @JsonProperty("prCode")
    private String prCode;

    @JsonProperty("prName")
    private String prName; // 제품 이름

    @JsonProperty("prPrice")
    private Long prPrice; // 단가

    private String prStandard;  //제품 규격

    @JsonProperty("prCategory")
    private ProductCategory prCategory; // 제품 분류

    @JsonProperty("prDivCategory")
    private ProductDivisionCategory prDivCategory; // 등록한 데이터의 구분(제품 or 자재)


    private static ModelMapper modelMapper =  new ModelMapper();
    static {
        TypeMap<Product, ProductAddDto> typeMap = modelMapper.createTypeMap(Product.class, ProductAddDto.class);
        typeMap.addMappings(mapper -> mapper.map(src -> src.getAccount().getAcCode(), ProductAddDto::setAcCode));
        //매핑 지정
    }
    public static ProductAddDto of(Product product){
        return modelMapper.map(product, ProductAddDto.class);
    }
}
