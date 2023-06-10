package com.Erp.service.logistics;

import com.Erp.dto.logistics.WarehousingInAndOutFormDto;
import com.Erp.entity.logistics.Inventory;
import com.Erp.entity.logistics.WarehousingInAndOut;
import com.Erp.repository.logistics.InventoryRepository;
import com.Erp.repository.logistics.WarehousingInAndOutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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

    //모든 재고 조회
    public List<Inventory> getInventoryAll() {
        return inventoryRepository.getInventoryAll();
    }

    //특정 입고 출고 아이디 찾기
    public WarehousingInAndOut widFindById(String widId) {
        return warehousingInAndOutRepository.findById(Long.valueOf(widId)).orElseThrow(EntityNotFoundException::new);
    }

    //특정 재고 아이디 찾기
    public Inventory inFindById(String inId) {
        return inventoryRepository.findById(Long.valueOf(inId)).orElseThrow(EntityNotFoundException::new);
    }
}

