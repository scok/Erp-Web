package com.Erp.entity;

import com.Erp.constant.AccountCategory;
import com.Erp.constant.DivisionStatus;
import com.Erp.dto.EstimateAddmDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.List;

@Entity
@Table(name = "Estimates")
@Getter@Setter
public class Estimate extends DistributionEntity{

    @Id
    @Column(unique = true)
    private String esCode;        //견적서 코드

    @Column(nullable = false)
    private Long esTotalPrice;    //총 금액

    @Enumerated(EnumType.STRING)
    private AccountCategory acCategory;     //거래 유형

    @Enumerated(EnumType.STRING)
    private DivisionStatus divisionStatus;    // 반려, 승인,승인 대기 등 상태 표시

    private String esComment;    //반려시 코맨트 작성 유도

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "acCode")
    private Account account; //거래처 매핑

    @JsonManagedReference //양방향 매핑시 재귀 호출을 방지하기 위한 어노테이션입니다. 부모클래스와 같이 사용해야합니다.
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "esCode")
    private List<EstimateDetail> estimateDetail; //견적서 디테일과 매핑

    public static Estimate createCode(EstimateAddmDto estimateAddmDto, Account account, String strNowDate, int count,
                                      List<EstimateDetail> estimateDetails,String memberName) {

        Estimate estimate = new Estimate();

        String code = "";
        DecimalFormat newcnt = new DecimalFormat("00");

        if(String.valueOf(estimateAddmDto.getAcCategory()).equals("구매")){   //제품일 경우
            code = "ESBU-" + strNowDate +"-"+ newcnt.format(count+1);
        }
        if(String.valueOf(estimateAddmDto.getAcCategory()).equals("판매")){ //자재일 경우
            code = "ESSE-" + strNowDate+"-"+ newcnt.format(count+1);
        }

        System.out.println(code);
        if (code == null || code.trim() == ""){
            throw new IllegalArgumentException("code가 비어있습니다.");
        }

        estimate.setEsCode(code);
        estimate.setEsTotalPrice(estimateAddmDto.getEsTotalPrice());
        estimate.setAcCategory(estimateAddmDto.getAcCategory());
        estimate.setDivisionStatus(DivisionStatus.승인대기);
        estimate.setPageYandN("Y");
        estimate.setAccount(account);
        estimate.setEstimateDetail(estimateDetails);
        estimate.setCreateName(memberName);

        return estimate;
    }
}
