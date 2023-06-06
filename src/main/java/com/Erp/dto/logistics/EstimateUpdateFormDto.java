package com.Erp.dto.logistics;


import com.Erp.constant.AccountCategory;
import com.Erp.constant.DivisionStatus;
import com.Erp.entity.logistics.Estimate;
import com.Erp.entity.logistics.EstimateDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter@Setter@ToString
public class EstimateUpdateFormDto {

    private String esCode;        //견적서 코드

    private String acCode;        //거래처 코드

    private Long esTotalPrice;    //총 금액

    private AccountCategory acCategory;     //거래 유형

    private DivisionStatus divisionStatus;    // 반려, 승인,승인 대기 등 상태 표시

    private String esComment;    //반려시 코맨트 작성 유도

    private List<EstimateDetail> estimateDetail; //견적서 디테일과 매핑

    public static EstimateUpdateFormDto of(Estimate estimate) {
        EstimateUpdateFormDto estimateUpdateFormDto = new EstimateUpdateFormDto();

        estimateUpdateFormDto.setEsCode(estimate.getEsCode());
        estimateUpdateFormDto.setAcCode(estimate.getAccount().getAcCode());
        estimateUpdateFormDto.setEsTotalPrice(estimate.getEsTotalPrice());
        estimateUpdateFormDto.setAcCategory(estimate.getAcCategory());
        estimateUpdateFormDto.setEsComment(estimate.getEsComment());
        estimateUpdateFormDto.setDivisionStatus(estimate.getDivisionStatus());
        estimateUpdateFormDto.setEstimateDetail(estimate.getEstimateDetail());

        return estimateUpdateFormDto;
    }
}
