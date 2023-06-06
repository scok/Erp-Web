package com.Erp.entity.logistics;

import com.Erp.dto.logistics.EstimateDetailAddmDto;
import com.Erp.entity.logistics.Estimate;
import com.Erp.entity.logistics.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "EstimateDetails")
@Getter@Setter
public class EstimateDetail{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long esdId;        //견적서 디테일

    @Column(nullable = false)
    private int esQuantity;         //수량

    @Column(nullable = false)
    private String esStandard;    //규격

    @Column(nullable = false)
    private Long esSupplyValue;    //공급 가액

    @Column(nullable = false)
    private Long esTaxAmount;    //세액

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.DETACH)
    @JoinColumn(name = "prCode")
    private Product product; //견적서 디테일과 매핑

    @Column(nullable = false)
    private Long esPrice; //만약 매핑된 상품의 단가가 변경될시 디테일 테이블에 바로 적용이 되어 문제를 방지할 단가 컬럼을 추가합니다.

    @JsonBackReference  //양방향 매핑시 재귀 호출을 방지하기 위한 어노테이션입니다. 자식클래스와 세트로 사용해야하니 참고
    @ManyToOne
    @JoinColumn(name = "esCode",insertable = false,updatable = false)
    private Estimate estimate;

    public static EstimateDetail createCode(EstimateDetailAddmDto estimateDetailAddmDto, Product product){

        EstimateDetail estimateDetail = new EstimateDetail();

        estimateDetail.setEsQuantity(estimateDetailAddmDto.getEsQuantity());
        estimateDetail.setEsStandard(estimateDetailAddmDto.getEsStandard());
        estimateDetail.setEsSupplyValue(estimateDetailAddmDto.getEsSupplyValue());
        estimateDetail.setEsTaxAmount(estimateDetailAddmDto.getEsTaxAmount());
        estimateDetail.setEsPrice(product.getPrPrice());
        estimateDetail.setProduct(product);

        return estimateDetail;
    }
}
