package com.Erp.entity;

import com.Erp.constant.DivisionStatus;
import com.Erp.constant.StackAreaCategory;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.mapping.ToOne;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "WarehousingInAndOuts")
@Getter
@Setter
public class WarehousingInAndOut extends DistributionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long wioId ; //입고 또는 출고 Id

    @Enumerated(EnumType.STRING)
    private DivisionStatus divisionStatus ; // 입고 또는 출고 구분하는 역활

    @Column(nullable = false)
    private LocalDateTime inAndOutDate ; //입고 또는 출고 일자

    @OneToOne
    @JoinColumn(name = "osdId")
    private OrderSheetDetail orderSheetDetail ; //견적서에 매핑

    @OneToOne
    @JoinColumn(name = "secCode")
    private Section section ; //창고 매핑

    @Enumerated(EnumType.STRING)
    private StackAreaCategory stackAreaCategory ; //구역 정보

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "proCode")
    private Production production ;

    @OneToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transactions;

    public static WarehousingInAndOut of(OrderSheet orderSheet, OrderSheetDetail orderSheetDetail, Section section, String SACategory){

        WarehousingInAndOut warehousingInAndOut = new WarehousingInAndOut();

        warehousingInAndOut.setInAndOutDate(LocalDateTime.now());
        warehousingInAndOut.setDivisionStatus(orderSheet.getDivisionStatus());
        warehousingInAndOut.setOrderSheetDetail(orderSheetDetail);
        warehousingInAndOut.setPageYandN("Y");
        warehousingInAndOut.setSection(section);
        warehousingInAndOut.setStackAreaCategory(StackAreaCategory.valueOf(SACategory));

        return warehousingInAndOut;
    }

}
