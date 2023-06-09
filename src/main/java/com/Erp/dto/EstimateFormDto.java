package com.Erp.dto;

import com.Erp.constant.AccountCategory;
import com.Erp.constant.DivisionStatus;
import com.Erp.entity.DistributionEntity;
import com.Erp.entity.Estimate;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.format.DateTimeFormatter;

@Getter @Setter
public class EstimateFormDto extends DistributionEntity {

    private String esCode;  //견적서 코드

    @Enumerated(EnumType.STRING)
    private AccountCategory acCategory;     //거래 유형

    private String prName;      //품명

    private Long esTotalPrice;    //총금액

    private String acName;      //공급처 명

    private String acCeo;      //공급처 대표자 명

    private String acHomePage;      //거래처 홈페이지

    private String esCreateName;    //작성자 명

    private String esRegDate;       //작성일자

    @Enumerated(EnumType.STRING)
    private DivisionStatus divisionStatus;

    public static EstimateFormDto of(Estimate estimate){

        EstimateFormDto estimateFormDto = new EstimateFormDto();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        estimateFormDto.setEsCode(estimate.getEsCode());
        estimateFormDto.setAcCategory(estimate.getAcCategory());

        String prName = "";
        if (estimate.getEstimateDetail().size() > 1){   //상품이 여러개 일경우
            prName = estimate.getEstimateDetail().get(0).getProduct().getPrName() +
                    " 외"+estimate.getEstimateDetail().size()+"개";
        }else{
            prName = estimate.getEstimateDetail().get(0).getProduct().getPrName();
        }
        estimateFormDto.setPrName(prName);
        estimateFormDto.setEsTotalPrice(estimate.getEsTotalPrice());
        estimateFormDto.setAcName(estimate.getAccount().getAcName());
        estimateFormDto.setAcCeo(estimate.getAccount().getAcCeo());
        estimateFormDto.setAcHomePage(estimate.getAccount().getAcHomePage());
        estimateFormDto.setDivisionStatus(estimate.getDivisionStatus());
        estimateFormDto.setEsCreateName(estimate.getCreateName());
        estimateFormDto.setEsRegDate(formatter.format(estimate.getRegDate()));

        return estimateFormDto;
    }
}
