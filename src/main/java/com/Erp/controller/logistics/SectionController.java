package com.Erp.controller.logistics;

import com.Erp.constant.StackAreaCategory;
import com.Erp.dto.UserDto;
import com.Erp.dto.logistics.SectionAddDto;
import com.Erp.dto.logistics.SectionFormDto;
import com.Erp.entity.Member;
import com.Erp.entity.logistics.*;
import com.Erp.service.MemberService;
import com.Erp.service.logistics.InventorService;
import com.Erp.service.logistics.LogisticsService;
import com.Erp.service.logistics.OrderSheetService;
import com.Erp.service.logistics.SectionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/section")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;
    private final MemberService memberService;
    private final OrderSheetService orderSheetService;
    private final InventorService inventorService;

    @GetMapping(value = "/list")
    public String goSectionList(){
        return "/section/sectionForm";
    }

    @GetMapping(value = "/check")
    public @ResponseBody Object SelectAll(){
        List<SectionFormDto> sectionFormDtoList = sectionService.sectionListAll();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", sectionFormDtoList);
        Object data = map;

        return data;
    }
    // 저장하기
    @PostMapping(value = "/addSection")
    public @ResponseBody ResponseEntity addSection(@RequestBody @Valid SectionAddDto sectionAddDto, BindingResult error, Principal principal, HttpServletRequest request){

        boolean department = this.getSession(request);
        if(!department){
            return new ResponseEntity<String>("등록 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        StringBuilder errorMessages = new StringBuilder();

        if (error.hasErrors()) {
            for (FieldError errorBean : error.getFieldErrors()) {
                errorMessages.append(errorBean.getDefaultMessage() + "\n");
            }
            return new ResponseEntity<String>(errorMessages.toString(), HttpStatus.BAD_REQUEST);
        }

        if(sectionAddDto.getSecCode() == null || sectionAddDto.getSecCode().trim() == "") { // 신규등록
            LocalDateTime regDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String strNowDate = regDateTime.format(formatter);

            int year = Integer.parseInt(strNowDate.substring(0,4));
            int month = Integer.parseInt(strNowDate.substring(4,6));
            int day = Integer.parseInt(strNowDate.substring(6));

            int count = sectionService.getSecCodeCount(year, month, day);
            Member member = memberService.getMemberName(principal.getName());

            Section section = Section.createSection(sectionAddDto, strNowDate, count, member.getName());
            sectionService.saveSection(section);

        }else { // 기존 데이터 수정
            sectionService.updateSection(sectionAddDto);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 수정할 데이터 불러오기
    @PostMapping(value = "updateSection")
    public @ResponseBody ResponseEntity updateSection(@RequestBody String code,HttpServletRequest request){

        boolean department = this.getSession(request);
        if(!department){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        code = code.substring(1,code.length()-1);
        Section section = sectionService.findBySecCode(code);
        SectionAddDto sectionAddDto = SectionAddDto.of(section);
        sectionAddDto.setSecAddress("");

        return new ResponseEntity<>(sectionAddDto, HttpStatus.OK);
    }

    // 삭제하기
    @PostMapping(value = "/deleteSection")
    public @ResponseBody ResponseEntity deleteSection(@RequestBody List<String> code, HttpServletRequest request){

        boolean department = this.getSession(request);
        if(!department){
            return new ResponseEntity<String>("삭제 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        sectionService.deleteSection(code);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 불러오기
    @GetMapping(value = "/getSectionsMaterial")
    public @ResponseBody Map<String,SectionFormDto> getSectionsMaterial() {
        Map<String,SectionFormDto> sectionFormDtoList = sectionService.sectionMapMaterial();

        return sectionFormDtoList;
    }

    @PostMapping(value = "/getSectionsProduct")
    public @ResponseBody ResponseEntity getSectionsProduct(@RequestBody String code) {
        // 주문서 정보를 가져와 주문서 디테일에 있는 상품들이 창고에 재고로 존재하는지 확인합니다.
        code = code.substring(1,code.length()-1);
        OrderSheet orderSheet = orderSheetService.findByCode(code);
        List<OrderSheetDetail> orderSheetDetails = orderSheet.getOrderSheetDetails();

        List<String> sectionCodes = new ArrayList<String>();
        List<String> targetSecCode = new ArrayList<String>();
        List<Inventory> targetInventory = new ArrayList<Inventory>();
        int index = 1;

        for (OrderSheetDetail orderSheetDetail : orderSheetDetails){
            //재고에 동일한 상품의 재고가 있는지 확인합니다.
            List<Inventory> inventories = inventorService.findByProductAndOsQuantity(orderSheetDetail.getProduct().getPrCode(),
                    orderSheetDetail.getOsQuantity());
            if(inventories == null) {
                return new ResponseEntity<>("재고가 있는 제품이 없습니다.", HttpStatus.BAD_REQUEST);
            }else if (index ==1){   //첫 번째는 무조건 sectionCodes담습니다.
                for(Inventory inventory: inventories){
                    targetSecCode.add(inventory.getSection().getSecCode());
                    targetInventory.add(inventory);
                }
                sectionCodes = targetSecCode.stream().distinct().collect(Collectors.toList());//중복 제거하는 중
                index ++;
            }else{
                int count = 1;
                for (String secCode : sectionCodes){
                    for (Inventory inventory: inventories){
                        if(secCode.equals(inventory.getSection().getSecCode())){
                            //창고 아이디와 구역 정보가 동일한게 있으면 클리어 하고 창고 코드를 넣습니다.
                            sectionCodes.clear();
                            sectionCodes.add(secCode);
                            targetInventory.clear();
                            targetInventory.add(inventory);
                            count++;
                            break;
                        }
                    }
                    if(index == 2){
                        break;
                    }
                }
                if (count == 1){
                    return new ResponseEntity<>("동일한 창고에 같은 구역에 재고가 있는 제품이 없습니다.", HttpStatus.BAD_REQUEST);
                }
            }
        }
        System.out.println(sectionCodes.toString());
        Map<String,Object> map = new HashMap<String,Object>();
        if(sectionCodes.size() >= 1){
            List<SectionFormDto> sectionFormDtos = new ArrayList<>();
            for (String secCode: sectionCodes){
                SectionFormDto sectionFormDto = sectionService.sectionProduct(secCode);
                sectionFormDtos.add(sectionFormDto);
            }
            map.put("secData",sectionFormDtos);
            map.put("inData",targetInventory);
            return new ResponseEntity<>(map,HttpStatus.OK);
        }else if(sectionCodes.size() == 1){
            SectionFormDto sectionFormDto = sectionService.sectionProduct(sectionCodes.toString());
            map.put("secData",sectionFormDto);
            map.put("inData",targetInventory);

            return new ResponseEntity<>(map,HttpStatus.OK);
        }else {
            return new ResponseEntity<>("프로그램에 문제가 발생하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public boolean getSession(HttpServletRequest request){

        HttpSession session = request.getSession();
        UserDto user = (UserDto) session.getAttribute("User");

        boolean department = false;

        if(user.getDepartment().equals("물류팀")) {
            department = true;
            return department;
        }else {
            return department;
        }
    }

}
