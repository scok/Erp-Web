package com.Erp.dto;

import com.Erp.constant.StackAreaCategory;
import com.Erp.entity.Inventory;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InventoryDeliveryFormDto {

    private Long inId;
    private String prName;

    private StackAreaCategory stackAreaCategory;

    public static InventoryDeliveryFormDto of(Inventory inventory){
        InventoryDeliveryFormDto inventoryDeliveryFormDto = new InventoryDeliveryFormDto();

        inventoryDeliveryFormDto.setInId(inventory.getInId());
        inventoryDeliveryFormDto.setPrName(inventory.getProduct().getPrName());

        return inventoryDeliveryFormDto;
    }
}
