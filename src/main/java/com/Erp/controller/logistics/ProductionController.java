package com.Erp.controller.logistics;

import com.Erp.constant.DivisionStatus;
import com.Erp.dto.UserDto;
import com.Erp.dto.logistics.ProductFormDto;
import com.Erp.dto.logistics.ProductionFormDto;
import com.Erp.dto.logistics.SectionFormDto;
import com.Erp.entity.Member;
import com.Erp.entity.logistics.*;
import com.Erp.service.MemberService;
import com.Erp.service.logistics.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/productions")
public class ProductionController {

    private final ProductService productService;
    private final MemberService memberService;
    private final SectionService sectionService;
    private final LogisticsService logisticsService;
    private final InventorService inventorService;

    private final ProductionService productionService;

    //페이지 접속
    @GetMapping(value = "/list")
    public String goProductionList(Model model){
        List<ProductFormDto> productFormDtos = productService.provideList();
        List<SectionFormDto> sectionFormDtos = sectionService.sectionListProduct();
        model.addAttribute("productFormDtos",productFormDtos);
        model.addAttribute("sectionFormDtos",sectionFormDtos);

        return "/production/productionFrom";
    }

    // 데이터 베이스에 있는 상품 정보를 조회
    @GetMapping(value = "/check")
    public @ResponseBody Object SelectAll(Model model){
        List<ProductionFormDto> productionFormDtos = productionService.productionListAll();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", productionFormDtos);
        Object data = map;

        return data;
    }

    @PostMapping(value = "/addProduction")
    @Transactional
    public @ResponseBody ResponseEntity addProduction(@RequestBody @Valid ProductionFormDto dto, BindingResult error, Principal principal, HttpServletRequest request) {

        boolean department = this.getSession(request);
        
        if(!department){
            return new ResponseEntity<String>("등록 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        String errorMessage = "";

        System.out.println(dto.toString());

        StringBuilder erromessages = new StringBuilder();
        if (error.hasErrors()) {
            for (FieldError errorBean : error.getFieldErrors()) {
                erromessages.append(errorBean.getField() + " : " + errorBean.getDefaultMessage() + "\n");
            }
            errorMessage = erromessages.toString();

            return new ResponseEntity<String>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        //여기는 등록 기능입니다.

        Member member = memberService.getMemberName(principal.getName());
        Product product = productService.findByCode(dto.getPrCode());
        Section section = sectionService.findBySecCode(dto.getSecCode());

        int secTotalCount = section.getSecTotalCount();

        if((secTotalCount + dto.getCount())<section.getSecMaxCount()){//창고에 맥스 수량보다 많이 넣으면 오류를 보내줍니다.

            Production production = Production.of(dto,member,product,section);

            production = productionService.save(production);

            section.setSecTotalCount(secTotalCount + dto.getCount());

            WarehousingInAndOut warehousingInAndOut = new WarehousingInAndOut();

            warehousingInAndOut.setInAndOutDate(LocalDateTime.now());
            warehousingInAndOut.setDivisionStatus(DivisionStatus.입고);
            warehousingInAndOut.setPageYandN("Y");
            warehousingInAndOut.setSection(section);
            warehousingInAndOut.setStackAreaCategory(dto.getSACategory());
            warehousingInAndOut.setProduction(production);

            warehousingInAndOut = logisticsService.WarehousingSave(warehousingInAndOut);

            if (String.valueOf(warehousingInAndOut.getDivisionStatus()).equals("입고")) {
                Inventory inventory = inventorService.inventorService(section.getSecCode(), dto.getSACategory(), dto.getPrCode());

                if (inventory != null) {
                    int inQuantity = inventory.getInQuantity() - dto.getCount();

                    inventorService.updateInQuantity(inventory,inQuantity);
                } else {
                    //객체가 null 값이면 저장공간을 만들수 있도록 객체 생성을 해줘야 합니다.
                    inventory = new Inventory();
                    inventory.setStackAreaCategory(dto.getSACategory());
                    inventory.setInQuantity(dto.getCount());
                    inventory.setInStandard(product.getPrStandard());
                    inventory.setProduct(product);
                    inventory.setSection(section);
                    inventory.setPageYandN("Y");

                    logisticsService.InventorySave(inventory);
                }
            }
            sectionService.saveSection(section);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>("창고에 여유 공간이 없습니다.",HttpStatus.BAD_REQUEST);
        }
    }
    public boolean getSession(HttpServletRequest request){

        HttpSession session = request.getSession();
        UserDto user = (UserDto) session.getAttribute("User");

        boolean department = false;

        if(user.getDepartment().equals("생산팀")) {
            department = true;
            return department;
        }else {
            return department;
        }
    }
}
