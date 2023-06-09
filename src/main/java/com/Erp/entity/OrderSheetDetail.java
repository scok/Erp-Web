package com.Erp.entity;

import com.Erp.dto.OrderSheetDetailAddmDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "OrderSheetDetails")
@Getter
@Setter
public class OrderSheetDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long osdId;        //주문서 디테일

    @Column(nullable = false)
    private int osQuantity;         //수량

    @Column(nullable = false)
    private String osStandard;    //규격

    @Column(nullable = false)
    private Long osSupplyValue;    //공급 가액

    @Column(nullable = false)
    private Long osTaxAmount;    //세액

    @Column(nullable = false)
    private Long osPrice;  //협상 과정중 단가가 변경될수 있으므로 추가합니다.

    @OneToOne
    @JoinColumn(name = "prCode")
    private Product product;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "osCode",updatable = false,insertable = false)   //읽기전용입니다.
    private OrderSheet orderSheet;

    public static OrderSheetDetail createCode(OrderSheetDetailAddmDto orderSheetDetailAddmDto, Product product) {

        OrderSheetDetail orderSheetDetail = new OrderSheetDetail();

        orderSheetDetail.setOsQuantity(orderSheetDetailAddmDto.getOsQuantity());
        orderSheetDetail.setOsStandard(orderSheetDetailAddmDto.getOsStandard());
        orderSheetDetail.setOsSupplyValue(orderSheetDetailAddmDto.getOsSupplyValue());
        orderSheetDetail.setOsTaxAmount(orderSheetDetailAddmDto.getOsTaxAmount());
        orderSheetDetail.setOsPrice(orderSheetDetailAddmDto.getOsPrice());
        orderSheetDetail.setProduct(product);

        return orderSheetDetail;

    }
}
