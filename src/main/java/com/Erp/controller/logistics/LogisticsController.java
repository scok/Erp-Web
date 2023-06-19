package com.Erp.controller.logistics;

import com.Erp.dto.UserDto;
import com.Erp.dto.logistics.*;
import com.Erp.dto.logistics.AccountFormDto;
import com.Erp.entity.logistics.*;
import com.Erp.service.logistics.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value ="/logistics")
@RequiredArgsConstructor
public class LogisticsController {

    private final OrderSheetService orderSheetService;
    private final AccountService accountService;
    private final LogisticsService logisticsService;
    private final SectionService sectionService;
    private final InventorService inventorService;

    //입하 관리 페이지 접속시 구매처 정보 바인딩
    @GetMapping(value = "/buyOrderSheet/list")
    public String getBuyAccountListInstruct(Model model) {

        List<AccountFormDto> accountFormDtos = accountService.buySelectAll();
        model.addAttribute("accountFormDtos", accountFormDtos);

        return "arrival/arrivalInstructForm";
    }

    // 데이터 베이스에 있는 구매 견적서가 입하대기인 정보를 조회
    @GetMapping(value = "/buyOrderSheet/check")
    public @ResponseBody Object buyOrderSheetInstructAll(Model model) {
        List<OrderSheetFormDto> orderSheetFormDtos = orderSheetService.orderSheetListBuyState();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", orderSheetFormDtos);
        Object data = map;

        return data;
    }

    //출하 관리 페이지 접속시 구매처 정보 바인딩
    @GetMapping(value = "/sellerOrderSheet/list")
    public String getSellerAccountListInstruct(Model model) {

        List<AccountFormDto> accountFormDtos = accountService.sellerSelectAll();
        model.addAttribute("accountFormDtos", accountFormDtos);

        return "arrival/shippingInstructionsForm";
    }

    // 데이터 베이스에 있는 판매 주문서가 출하대기인 정보를 조회
    @GetMapping(value = "/sellerOrderSheet/check")
    public @ResponseBody Object sellerOrderSheetInstructAll(Model model) {
        List<OrderSheetFormDto> orderSheetFormDtos = orderSheetService.orderSheetListSellerState();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", orderSheetFormDtos);
        Object data = map;

        return data;
    }

    //입고,출고 등록 로직
    @Transactional
    @PostMapping(value = "/updateOrderSheets")
    public @ResponseBody ResponseEntity OrderSheetInstructUpdate(@RequestBody Map<String,String> data, HttpServletRequest request) {
        boolean department = this.getSession(request);

        if(!department){
            return new ResponseEntity<String>("등록 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        String message="";
        //주문서 코드와 상태를 보내줍니다.
        OrderSheet orderSheet =  orderSheetService.OrderSheetStatusUpdate(data.get("osCode"),data.get("divisionStatus"));
        Section section =sectionService.findBySecCode(data.get("secCode"));
        int secTotalCount = 0;
        //창고의 코드와 구역의 정보를 보내줍니다.
        
        for(OrderSheetDetail orderSheetDetail : orderSheet.getOrderSheetDetails()){
            if(data.get("divisionStatus").equals("입고")){
                secTotalCount += orderSheetDetail.getOsQuantity();
                if(section.getSecMaxCount() > secTotalCount){ //창고의 맥스 수량보다 많이 입고 시킬시 감지
                    WarehousingInAndOut warehousingInAndOut = WarehousingInAndOut.of(orderSheet,orderSheetDetail,section,data.get("SACategory"));
                    warehousingInAndOut = logisticsService.WarehousingSave(warehousingInAndOut);

                    Inventory inventory = inventorService.inventorService(section.getSecCode(),warehousingInAndOut.getStackAreaCategory(),orderSheetDetail.getProduct().getPrCode());

                    if(inventory == null){  //재고를 테이블을 새로 만드는 로직.
                        inventory = null;
                        Inventory createinventory = Inventory.create(orderSheetDetail,section,warehousingInAndOut.getStackAreaCategory());
                        logisticsService.InventorySave(createinventory);
                    }else{  //재고테이블 수정하는 로직
                        int inQuantity = inventory.getInQuantity() + orderSheetDetail.getOsQuantity();
                        inventorService.updateInQuantity(inventory,inQuantity);
                    }

                    section.setSecTotalCount(secTotalCount);
                    sectionService.saveSection(section);
                }else{
                    message ="창고에 여유 공간이 없습니다.";
                    throw new RuntimeException(message);
                }
            }else if(data.get("divisionStatus").equals("출고")){
                secTotalCount = section.getSecTotalCount() -orderSheetDetail.getOsQuantity();
                if(orderSheetDetail.getOsQuantity() < secTotalCount){ //창고의 맥스 수량보다 많이 입고 시킬시 감지
                    WarehousingInAndOut warehousingInAndOut = WarehousingInAndOut.of(orderSheet,orderSheetDetail,section,data.get("SACategory"));
                    warehousingInAndOut = logisticsService.WarehousingSave(warehousingInAndOut);

                    Inventory inventory = inventorService.inventorService(section.getSecCode(),warehousingInAndOut.getStackAreaCategory(),orderSheetDetail.getProduct().getPrCode());

                    int inQuantity = inventory.getInQuantity() - orderSheetDetail.getOsQuantity();
                    inventorService.updateInQuantity(inventory,inQuantity);
                }else{
                    message ="창고에 재고가 없습니다.";
                    throw new RuntimeException(message);
                }
            }

        }

        section.setSecTotalCount(secTotalCount);
        sectionService.saveSection(section);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //입고 관리 페이지 접속
    @GetMapping(value = "/inWarehousing/list")
    public String goWarehousingInList() {
        return "warehousing/warehousingInForm";
    }

    // 데이터 베이스에 있는 입고 테이블의 정보 조회
    @GetMapping(value = "/inWarehousing/check")
    public @ResponseBody Object getWarehousingInList() {
        List<WarehousingInAndOutFormDto> warehousingInAndOutFormDtos = logisticsService.getWarehousingInList();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", warehousingInAndOutFormDtos);
        Object data = map;
        return data;
    }

    // 데이터 베이스에 있는 입고 테이블의 정보 조회
    @PostMapping(value = "/warehousingInAndOut/Detail")
    public @ResponseBody ResponseEntity getWarehousingInAndOutDetail(@RequestBody String widId, HttpServletRequest request) {

        boolean department = this.getSession(request);

        if(!department){
            return new ResponseEntity<String>("조회 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        widId = widId.substring(1,widId.length()-1);
        WarehousingInAndOut warehousing = logisticsService.widFindById(widId);
        WarehousingInAndOutDetailDto warehousingInAndOutDetailDto = new WarehousingInAndOutDetailDto();
        if (warehousing.getOrderSheetDetail() != null){
            warehousingInAndOutDetailDto = WarehousingInAndOutDetailDto.order(warehousing);
        }else if(warehousing.getProduction() != null){
            warehousingInAndOutDetailDto = WarehousingInAndOutDetailDto.production(warehousing);
        }else if(warehousing.getMaterialDelivery() != null){
            warehousingInAndOutDetailDto = WarehousingInAndOutDetailDto.Delivery(warehousing);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", warehousingInAndOutDetailDto);
        Object data = map;
        return new ResponseEntity<>(data,HttpStatus.OK);
    }


    //출고 관리 페이지 접속
    @GetMapping(value = "/outWarehousing/list")
    public String goWarehousingOutList() {
        return "warehousing/warehousingOutForm";
    }

    // 데이터 베이스에 있는 출고 테이블의 정보 조회
    @GetMapping(value = "/outWarehousing/check")
    public @ResponseBody Object getWarehousingOutList() {

        List<WarehousingInAndOutFormDto> warehousingInAndOutFormDtos = logisticsService.getWarehousingOutList();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", warehousingInAndOutFormDtos);
        Object data = map;
        return data;
    }

    //재고 관리 페이지 접속
    @GetMapping(value = "/inventory/list")
    public String goInventoryList() {
        return "inventory/inventoryFrom";
    }

    //전체 재고 조회
    @GetMapping(value = "/inventory/check")
    public @ResponseBody Object getInventoryAll(Model model) {

        List<Inventory> inventories = logisticsService.getInventoryAll();
        List<InventoryFormDto> inventoryFormDtos = new ArrayList<InventoryFormDto>();

        for (Inventory inventory: inventories){
            InventoryFormDto inventoryFormDto = InventoryFormDto.of(inventory);
            inventoryFormDtos.add(inventoryFormDto);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", inventoryFormDtos);
        Object data = map;

        return data;
    }

    // 데이터 베이스에 재고 테이블 일부만 보기
    @PostMapping(value = "/inventory/sectionInfo")
    public @ResponseBody Object getSectionInfo(@RequestBody String inCode) {
        Map<String, Object> map = new HashMap<String,Object>();

        inCode=inCode.substring(1,inCode.length()-1);
        Inventory inventory = inventorService.findByInId(Long.valueOf(inCode));

        map.put("secCode",inventory.getSection().getSecCode());
        map.put("secName",inventory.getSection().getSecName());
        map.put("SACategory",inventory.getStackAreaCategory());

        return map;
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

