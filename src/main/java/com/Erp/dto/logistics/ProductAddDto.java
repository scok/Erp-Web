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
    @NotEmpty(message = "거래처를 선택해 주세요.")
    private String acCode;

    @JsonProperty("prCode")
    private String prCode;

    @JsonProperty("prName")
    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String prName; // 제품 이름

    @JsonProperty("prPrice")
    @NotNull(message = "상품 단가는 필수 입력 값입니다.")
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
