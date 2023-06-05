package com.Erp.controller;

import com.Erp.dto.InventoryDeliveryFormDto;
import com.Erp.dto.MaterialDeliveryAddDto;
import com.Erp.dto.MaterialDeliveryFormDto;
import com.Erp.entity.Inventory;
import com.Erp.entity.MaterialDelivery;
import com.Erp.entity.Section;
import com.Erp.service.InventorService;
import com.Erp.service.MaterialDeliveryService;
import com.Erp.service.SectionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/materialDelivery")
public class MaterialDeliveryController {

    private final MaterialDeliveryService maDeliveryService;
    private final InventorService inventorService;
    private final SectionService sectionService;
    
    // 리스트 불러오기
    @GetMapping(value = "/list")
    public String goMaterialDeliveryList(Model model){

        List<InventoryDeliveryFormDto> inventoryInFos = maDeliveryService.getinventoryInFo();
        model.addAttribute("inventoryInFos", inventoryInFos);

        return "/materialDelivery/materialDeliveryForm";
    }

    @GetMapping(value = "/check")
    public @ResponseBody Object SelectAll(Model model){
        List<MaterialDeliveryFormDto> maDeliveryFormDtoList = maDeliveryService.materialDeliveryAll();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", maDeliveryFormDtoList);
        Object data = map;

        return data;
    }

    // 등록하기
    @PostMapping(value = "/addMaterialDelivery")
    @Transactional
    public @ResponseBody ResponseEntity addMaDelivery(@RequestBody @Valid MaterialDeliveryAddDto maDeliveryAddDto, BindingResult bindingResult) throws JsonProcessingException {

        Section section = sectionService.findBySecCode(maDeliveryAddDto.getSecCode());
        Inventory inventory = inventorService.findByInId(maDeliveryAddDto.getInId());

        int resultCount = inventory.getInQuantity() - maDeliveryAddDto.getMaDeliveryCount();
        inventory.setInQuantity(resultCount);

        section.setSecTotalCount(section.getSecTotalCount() - maDeliveryAddDto.getMaDeliveryCount());

        MaterialDelivery maDelivery = MaterialDelivery.createMaDelivery(maDeliveryAddDto,section,inventory);
        maDeliveryService.saveMaDelivery(maDelivery);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
