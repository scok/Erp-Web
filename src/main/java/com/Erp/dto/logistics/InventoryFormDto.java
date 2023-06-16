package com.Erp.dto.logistics;

import com.Erp.constant.SectionCategory;
import com.Erp.constant.StackAreaCategory;
import com.Erp.entity.logistics.Inventory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter@ToString
public class InventoryFormDto {

    private Long inId;  //재고 코드

    private String prCode;  //상품 코드

    private String prName;     //품목 명

    private String osStandard; //규격

    private int arTotalCount;    //총수량

    private String acName;    //거래처 명

    private String secName;  //창고명

    private SectionCategory secCategory; //창고 카테고리

    private StackAreaCategory stackAreaCategory;     //구역 명

    public static InventoryFormDto of(Inventory inventory){
        InventoryFormDto inventoryFormDto = new InventoryFormDto();

        inventoryFormDto.setInId(inventory.getInId());
        inventoryFormDto.setPrCode(inventory.getProduct().getPrCode());
        inventoryFormDto.setSecName(inventory.getSection().getSecName());
        inventoryFormDto.setStackAreaCategory(inventory.getStackAreaCategory());
        inventoryFormDto.setPrName(inventory.getProduct().getPrName());
        inventoryFormDto.setOsStandard(inventory.getInStandard());
        inventoryFormDto.setArTotalCount(inventory.getInQuantity());
        inventoryFormDto.setAcName(inventory.getProduct().getAccount().getAcName());
        inventoryFormDto.setSecCategory(inventory.getSection().getSecCategory());

        return inventoryFormDto;
    }
}
