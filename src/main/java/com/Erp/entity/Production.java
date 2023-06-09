package com.Erp.entity;

import com.Erp.constant.ProductionLine;
import com.Erp.dto.ProductionFormDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

//실적
@Entity
@Table(name = "Productions")
@Getter
@Setter
public class Production extends DistributionEntity {

    @Id
    @Column
    @GeneratedValue
    private Long proCode;

    @Enumerated(EnumType.STRING)
    private ProductionLine productionLine;  //조립라인

    @Column(nullable = false)
    private String meName;         //등록자 명

    @Column(nullable = false)
    private Integer count;         //수량

    private LocalDateTime registrationDate; //등록일자

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "prCode")
    private Product product;
    public static Production of(ProductionFormDto dto, Member member, Product product){
        Production production = new Production();

        production.setProductionLine(dto.getProductionLine());
        production.setCount(dto.getCount());
        production.setMeName(member.getName());
        production.setRegistrationDate(LocalDateTime.now());
        production.setProduct(product);
        production.setPageYandN("Y");

        return production;
    }
}
