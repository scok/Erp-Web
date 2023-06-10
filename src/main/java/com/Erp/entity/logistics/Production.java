package com.Erp.entity.logistics;

import com.Erp.constant.ProductionLine;
import com.Erp.constant.StackAreaCategory;
import com.Erp.dto.logistics.ProductionFormDto;
import com.Erp.entity.Member;
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
    private Integer count;         //수량

    private LocalDateTime registrationDate; //등록일자

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "prCode")
    private Product product;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "secCode")
    private Section section;

    @Enumerated(EnumType.STRING)
    private StackAreaCategory stackAreaCategory;
    public static Production of(ProductionFormDto dto, Member member, Product product,Section section){
        Production production = new Production();

        production.setProductionLine(dto.getProductionLine());
        production.setCount(dto.getCount());
        production.setCreateName(member.getName());
        production.setRegistrationDate(LocalDateTime.now());
        production.setStackAreaCategory(dto.getSACategory());
        production.setProduct(product);
        production.setSection(section);
        production.setPageYandN("Y");

        return production;
    }
}
