package com.Erp.service.logistics;

import com.Erp.dto.logistics.ProductionFormDto;
import com.Erp.entity.logistics.Production;
import com.Erp.repository.logistics.ProductionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            ProductionFormDto dto = new ProductionFormDto();

            dto.setProCode(production.getProCode());
            dto.setProductionLine(production.getProductionLine());
            dto.setCount(production.getCount());
            dto.setMeName(production.getMeName());
            dto.setRegistrationDate(production.getRegistrationDate());
            dto.setPrName(production.getProduct().getPrName());

            productionFormDtos.add(dto);
        }

        return productionFormDtos;
    }

    @Transactional
    public Production save(Production production) {
        return productionRepository.save(production);
    }
}
