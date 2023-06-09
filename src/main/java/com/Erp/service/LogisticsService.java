package com.Erp.service;

import com.Erp.dto.WarehousingInAndOutFormDto;
import com.Erp.entity.Inventory;
import com.Erp.entity.WarehousingInAndOut;
import com.Erp.repository.InventoryRepository;
import com.Erp.repository.WarehousingInAndOutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogisticsService {
    private final WarehousingInAndOutRepository warehousingInAndOutRepository;
    private final InventoryRepository inventoryRepository;

    //입고 출고 등록
    @Transactional
    public WarehousingInAndOut WarehousingSave(WarehousingInAndOut warehousingInAndOut) {
       return warehousingInAndOutRepository.save(warehousingInAndOut);
    }

    //입고 목록 불러오기
    public List<WarehousingInAndOutFormDto> getWarehousingInList() {
        List<WarehousingInAndOut> warehousingInAndOuts = warehousingInAndOutRepository.findByWarehousingInList();
        List<WarehousingInAndOutFormDto> warehousingInAndOutFormDtos = new ArrayList<WarehousingInAndOutFormDto>();

        for (WarehousingInAndOut warehousingInAndOut : warehousingInAndOuts){
            WarehousingInAndOutFormDto warehousingInAndOutFormDto = WarehousingInAndOutFormDto.of(warehousingInAndOut);
            warehousingInAndOutFormDtos.add(warehousingInAndOutFormDto);
        }
        return warehousingInAndOutFormDtos;
    }

    //출고 목록 불러오기
    public List<WarehousingInAndOutFormDto> getWarehousingOutList() {
        List<WarehousingInAndOut> warehousingInAndOuts = warehousingInAndOutRepository.findByWarehousingOutList();
        List<WarehousingInAndOutFormDto> warehousingInAndOutFormDtos = new ArrayList<WarehousingInAndOutFormDto>();

        for (WarehousingInAndOut warehousingInAndOut : warehousingInAndOuts){
            WarehousingInAndOutFormDto warehousingInAndOutFormDto = WarehousingInAndOutFormDto.of(warehousingInAndOut);
            warehousingInAndOutFormDtos.add(warehousingInAndOutFormDto);
        }
        return warehousingInAndOutFormDtos;
    }

    //재고 등록
    @Transactional
    public void InventorySave(Inventory inventory) {
        inventoryRepository.save(inventory);
    }

    //모든 재고 조화
    public List<Inventory> getInventoryAll() {
        return inventoryRepository.getInventoryAll();
    }
}

