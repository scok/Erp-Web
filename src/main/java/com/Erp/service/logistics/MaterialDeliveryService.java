package com.Erp.service.logistics;

import com.Erp.dto.logistics.InventoryDeliveryFormDto;
import com.Erp.dto.logistics.MaterialDeliveryFormDto;
import com.Erp.entity.logistics.Inventory;
import com.Erp.entity.logistics.MaterialDelivery;
import com.Erp.repository.logistics.InventoryRepository;
import com.Erp.repository.logistics.MaterialDeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MaterialDeliveryService {
    private final MaterialDeliveryRepository materialDeliveryRepository;
    private final InventoryRepository inventoryRepository;

    //재고 테이블에 자재 항목만 조회합니다.
    public List<InventoryDeliveryFormDto> getinventoryInFo() {

        List<Inventory> inventoryList = inventoryRepository.getMaterialInventory();
        List<InventoryDeliveryFormDto> inventoryFormDtoList = new ArrayList<InventoryDeliveryFormDto>();

        for(Inventory inventory : inventoryList){
            InventoryDeliveryFormDto inventoryFormDto = InventoryDeliveryFormDto.of(inventory);
            inventoryFormDtoList.add(inventoryFormDto);
        }
        return inventoryFormDtoList;
    }

    // 자재 불출 목록에 보여줄 데이터
    public List<MaterialDeliveryFormDto> materialDeliveryAll(){
        List<MaterialDelivery> maDeliveryList = materialDeliveryRepository.findAllY();
        List<MaterialDeliveryFormDto> maDeliveryFormDtoList = new ArrayList<MaterialDeliveryFormDto>();

        for (MaterialDelivery maDelivery : maDeliveryList){
            MaterialDeliveryFormDto maDeliveryFormDto = MaterialDeliveryFormDto.of(maDelivery);
            maDeliveryFormDtoList.add(maDeliveryFormDto);
        }
        return maDeliveryFormDtoList;
    }

    // 등록하기
    @Transactional
    public MaterialDelivery saveMaDelivery(MaterialDelivery maDelivery){
        return materialDeliveryRepository.save(maDelivery);
    }

    public MaterialDelivery findById(String maCode) {
        return materialDeliveryRepository.findById(Long.valueOf(maCode)).orElseThrow(EntityNotFoundException::new);
    }
}
