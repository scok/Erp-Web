package com.Erp.entity;

import com.Erp.constant.ProductCategory;
import com.Erp.constant.ProductDivisionCategory;
import com.Erp.dto.ProductAddDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.DecimalFormat;

@Entity
@Table(name = "products")
@Getter @Setter
public class Product extends DistributionEntity{

    @Id
    @Column(unique = true, nullable = false)
    private String prCode; // 상품 코드(ex_FC2023042601) : FC(first class 의 약자)+등록일 + 01(카운트)

    @Column(nullable = false)
    private String prName; // 상품 이름

    @Column(nullable = false)
    private Long prPrice; // 단가

    private String prStandard; // 제품 규격

    @Column(nullable = false, name = "pr_div_category")
    @Enumerated(EnumType.STRING)
    private ProductDivisionCategory prDivCategory; // 등록한 데이터의 구분(제품 or 자재)

    @Column(nullable = false, name = "prCategory")
    @Enumerated(EnumType.STRING)
    private ProductCategory prCategory; // 상세 분류

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acCode")
    private Account account; //상품 또는 자재  id

    public static Product createCode(ProductAddDto dto,Account account,int count,String strNowDate,String memberName ){
        Product product = new Product();

        String code = "";
        DecimalFormat newcnt = new DecimalFormat("00");

        if(String.valueOf(dto.getPrDivCategory()).equals("제품")){   //제품일 경우
            code = "FC-" + strNowDate +"-"+ newcnt.format(count+1);
        }
        if(String.valueOf(dto.getPrDivCategory()).equals("자재")){ //자재일 경우
            code = "MA-" + strNowDate+"-"+ newcnt.format(count+1);
        }

        if (code == null || code.trim() == ""){
            throw new IllegalArgumentException("code가 비어있습니다.");
        }

        product.setPrCode(code);
        product.setPrName(dto.getPrName());
        product.setPrPrice(dto.getPrPrice());
        if(String.valueOf(dto.getPrDivCategory()).equals("제품")){
            product.setPrStandard(dto.getPrStandard());
        }
        product.setPrPrice(dto.getPrPrice());
        product.setPrDivCategory(dto.getPrDivCategory());
        product.setPrCategory(dto.getPrCategory());
        product.setAccount(account);
        product.setPageYandN("Y");
        product.setCreateName(memberName);

        return product;
    }
}
