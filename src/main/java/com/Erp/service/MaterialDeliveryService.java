package com.Erp.service;

import com.Erp.dto.InventoryDeliveryFormDto;
import com.Erp.dto.MaterialDeliveryFormDto;
import com.Erp.entity.Inventory;
import com.Erp.entity.MaterialDelivery;
import com.Erp.repository.InventoryRepository;
import com.Erp.repository.MaterialDeliveryRepository;
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

        List<Inventory> inventoryList = inventoryRepository.getinventoryInFo();
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

    // 삭제하기
    @Transactional
    public void deleteMaDelivery(List<Long> id){
        for(Long item : id){
            if(item != null){
                MaterialDelivery materialDelivery = materialDeliveryRepository.findById(item).orElseThrow(EntityNotFoundException::new);
                materialDelivery.setPageYandN("N");
            }
        }
    }
}
