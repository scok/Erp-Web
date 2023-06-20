package com.Erp.controller.logistics;

import com.Erp.constant.DivisionStatus;
import com.Erp.dto.UserDto;
import com.Erp.dto.logistics.InventoryDeliveryFormDto;
import com.Erp.dto.logistics.MaterialDeliveryAddDto;
import com.Erp.dto.logistics.MaterialDeliveryFormDto;
import com.Erp.entity.Member;
import com.Erp.entity.logistics.Inventory;
import com.Erp.entity.logistics.MaterialDelivery;
import com.Erp.entity.logistics.Section;
import com.Erp.entity.logistics.WarehousingInAndOut;
import com.Erp.service.MemberService;
import com.Erp.service.logistics.InventorService;
import com.Erp.service.logistics.LogisticsService;
import com.Erp.service.logistics.MaterialDeliveryService;
import com.Erp.service.logistics.SectionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/materialDelivery")
public class MaterialDeliveryController {

    private final MemberService memberService;
    private final MaterialDeliveryService maDeliveryService;
    private final InventorService inventorService;
    private final SectionService sectionService;
    private final LogisticsService logisticsService;

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
    public @ResponseBody ResponseEntity addMaDelivery(@RequestBody MaterialDeliveryAddDto maDeliveryAddDto, BindingResult bindingResult, Principal principal, HttpServletRequest request) throws JsonProcessingException {
        try {
            getSession(request);
        } catch (Exception e) {
            String errorMessage = "생산팀만 게시글을 등록할 수 있습니다.";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(errorMessage);
        }


        Section section = sectionService.findBySecCode(maDeliveryAddDto.getSecCode());
        Inventory inventory = inventorService.findByInId(maDeliveryAddDto.getInId());
        Member member = memberService.getMemberName(principal.getName());

        if(inventory.getInQuantity() >= maDeliveryAddDto.getMaDeliveryCount()) {
            int resultCount = inventory.getInQuantity() - maDeliveryAddDto.getMaDeliveryCount();
            inventory.setInQuantity(resultCount);

            section.setSecTotalCount(section.getSecTotalCount() - maDeliveryAddDto.getMaDeliveryCount());

            MaterialDelivery maDelivery = MaterialDelivery.createMaDelivery(maDeliveryAddDto, section, inventory, member.getName());
            MaterialDelivery  materialDelivery = maDeliveryService.saveMaDelivery(maDelivery);

            WarehousingInAndOut warehousingInAndOut = new WarehousingInAndOut();

            warehousingInAndOut.setInAndOutDate(LocalDateTime.now());
            warehousingInAndOut.setDivisionStatus(DivisionStatus.출고);
            warehousingInAndOut.setPageYandN("Y");
            warehousingInAndOut.setSection(section);
            warehousingInAndOut.setStackAreaCategory(maDeliveryAddDto.getStackAreaCategory());
            warehousingInAndOut.setMaterialDelivery(materialDelivery);

            logisticsService.WarehousingSave(warehousingInAndOut);

            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>("재고를 확인해주세요.",HttpStatus.BAD_REQUEST);
        }
    }


    public void getSession(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        UserDto user = (UserDto) session.getAttribute("User");

        if (!user.getDepartment().equals("생산팀")) {
            throw new Exception("생산팀만 게시글을 등록할 수 있습니다.");
        }
    }





}