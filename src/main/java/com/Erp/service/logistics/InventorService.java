package com.Erp.service.logistics;

import com.Erp.constant.StackAreaCategory;
import com.Erp.dto.logistics.InventoryFormDto;
import com.Erp.entity.logistics.Inventory;
import com.Erp.repository.logistics.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventorService {

    private final InventoryRepository inventoryRepository;

    //창고명,구역,상품 코드가 같은 재고를 가져옵니다.
    @Transactional
    public Inventory inventorService(String escCode, StackAreaCategory stackAreaCategory, String prCode){
        Inventory inventory = new Inventory();
        inventory =  inventoryRepository.getInventoryInFo(escCode,stackAreaCategory,prCode);
        return inventory;
    }

    //수량을 업데이트 합니다.
    @Transactional
    public void updateInQuantity(Inventory inventory, int inQuantity) {
        int totalCount = inventory.getInQuantity() + inQuantity;
        inventory.setInQuantity(totalCount);
    }

    //설정한 창고의 재고만 가져옵니다.
    public List<Inventory> findInventorySecCode(String secCode) {
        System.out.println(secCode);
        if(secCode.equals("all")){
            return inventoryRepository.getInventoryAll();
        }else {
            return inventoryRepository.findInventorySecCode(secCode);
        }
    }

    //재고 id를 찾습니다.
    public Inventory findByInId(Long id){
        return inventoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<InventoryFormDto> getMaterialInventory() {
        List<Inventory> inventories = inventoryRepository.getMaterialInventory();
        List<InventoryFormDto> inventoryFormDtos = new ArrayList<InventoryFormDto>();

        for (Inventory inventory : inventories){
            InventoryFormDto inventoryFormDto = InventoryFormDto.of(inventory);
            inventoryFormDtos.add(inventoryFormDto);
        }
        return inventoryFormDtos;
    }
    public List<InventoryFormDto> getProductInventory() {
        List<Inventory> inventories = inventoryRepository.getProductInventory();
        List<InventoryFormDto> inventoryFormDtos = new ArrayList<InventoryFormDto>();

        for (Inventory inventory : inventories){
            InventoryFormDto inventoryFormDto = InventoryFormDto.of(inventory);
            inventoryFormDtos.add(inventoryFormDto);
        }
        return inventoryFormDtos;
    }
}
