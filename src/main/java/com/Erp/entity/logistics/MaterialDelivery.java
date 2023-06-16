package com.Erp.entity.logistics;

import com.Erp.constant.ProductionLine;
import com.Erp.constant.StackAreaCategory;
import com.Erp.dto.logistics.MaterialDeliveryAddDto;
import com.Erp.entity.logistics.Product;
import com.Erp.entity.logistics.Section;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "material_deliverys")
@Getter @Setter
public class MaterialDelivery extends DistributionEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long maDeliveryId; // 자재불출 id

    @Column(nullable = false)
    private LocalDateTime maDeliveryDate; // 불출일자

    @OneToOne
    @JoinColumn(name = "secCode")
    private Section section;                //창고 정보.

    @Column(nullable = false, name = "ma_productionLine")
    @Enumerated(EnumType.STRING)
    private ProductionLine productionLine; // 자재를 불출받을 공장라인

    @Column(nullable = false)
    private int maDeliveryCount;           // 불출수량

    @OneToOne
    @JoinColumn(name = "prCode")
    private Product product;                //상품 정보.

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StackAreaCategory stackAreaCategory; // 구역정보 불출받을 공장라인


    public static MaterialDelivery createMaDelivery(MaterialDeliveryAddDto maDeliveryAddDto, Section section, Inventory inventory,String memberName){
        MaterialDelivery materialDelivery = new MaterialDelivery();

        materialDelivery.setMaDeliveryDate(LocalDateTime.now());
        materialDelivery.setSection(section);
        materialDelivery.setStackAreaCategory(maDeliveryAddDto.getStackAreaCategory());
        materialDelivery.setProductionLine(maDeliveryAddDto.getProductionLine());
        materialDelivery.setMaDeliveryCount(maDeliveryAddDto.getMaDeliveryCount());
        materialDelivery.setProduct(inventory.getProduct());
        materialDelivery.setPageYandN("Y");
        materialDelivery.setCreateName(memberName);

        return materialDelivery;
    }
}
