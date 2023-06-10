package com.Erp.service.logistics;

import com.Erp.dto.logistics.ProductionFormDto;
import com.Erp.entity.logistics.MaterialDelivery;
import com.Erp.entity.logistics.Production;
import com.Erp.repository.logistics.ProductionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductionService {
    private final ProductionRepository productionRepository;

    //실적 정보 전부를 가져옵니다.
    @Transactional
    public List<ProductionFormDto> productionListAll(){
        List<Production> productionList = productionRepository.findAll();
        List<ProductionFormDto> productionFormDtos = new ArrayList<ProductionFormDto>();

        for(Production production : productionList){
            ProductionFormDto dto = ProductionFormDto.of(production);
            productionFormDtos.add(dto);
        }

        return productionFormDtos;
    }

    @Transactional
    public Production save(Production production) {
        return productionRepository.save(production);
    }

    public Production findById(String proId) {
        return productionRepository.findById(Long.valueOf(proId)).orElseThrow(EntityNotFoundException::new);
    }
}
