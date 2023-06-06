package com.Erp.entity.logistics;

import com.Erp.constant.AccountCategory;
import com.Erp.constant.DivisionStatus;
import com.Erp.entity.logistics.OrderSheetDetail;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "OrderSheets")
@Getter @Setter
public class OrderSheet extends DistributionEntity{

    @Id
    @Column(unique = true)
    private String osCode;        //주문서 코드

    @Column(nullable = false)
    private Long osTotalPrice;    //총 금액

    @Enumerated(EnumType.STRING)
    private AccountCategory acCategory;     //거래 유형

    @Enumerated(EnumType.STRING)
    private DivisionStatus divisionStatus;    // 반려, 승인,승인 대기 등 상태 표시

    private LocalDateTime osReceiptDate;    //입고 예정 일자.

    private String osComment; //단가나 정보 변경시 코맨트를 작성합니다.

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "esCode")
    private Estimate estimate; //견적서 매핑


    //일 대 다의 경우 단반향 매핑시 설정 방법이 다름.
    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "osCode")
    private List<OrderSheetDetail> orderSheetDetails; //주문서 디테일과 매핑


    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "acCode")
    private Account account;

}
