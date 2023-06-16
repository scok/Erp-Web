package com.Erp.entity.logistics;

import com.Erp.constant.StackAreaCategory;
import com.Erp.entity.logistics.OrderSheetDetail;
import com.Erp.entity.logistics.Product;
import com.Erp.entity.logistics.Section;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "Inventory")
@Getter @Setter @ToString
public class Inventory extends DistributionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long inId; //재고 Id

    @Enumerated(EnumType.STRING)
    private StackAreaCategory stackAreaCategory;    //적재 구역(A구역, B구역, C구역...))

    private int inQuantity;                         //(재고 수량)
    private String inStandard;                      //규격

    @OneToOne
    @JoinColumn(name = "prCode")
    private Product product;                        //(상품 테이블과 양방향 매핑)

    @OneToOne
    @JoinColumn(name = "secCode")
    private Section section;                        //(창고 테이블(Section 엔터티와 양방향 매핑))

    public static Inventory create(OrderSheetDetail orderSheetDetail, Section section, StackAreaCategory stackAreaCategory){

        Inventory inventory = new Inventory();

        inventory.setStackAreaCategory(stackAreaCategory);
        inventory.setInQuantity(orderSheetDetail.getOsQuantity());
        inventory.setInStandard(orderSheetDetail.getOsStandard());
        inventory.setProduct(orderSheetDetail.getProduct());
        inventory.setSection(section);
        inventory.setPageYandN("Y");

        return inventory;
    }

}
