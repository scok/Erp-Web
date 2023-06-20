package com.Erp.controller.logistics;

import com.Erp.dto.UserDto;
import com.Erp.dto.logistics.*;
import com.Erp.entity.logistics.OrderSheet;
import com.Erp.entity.logistics.OrderSheetDetail;
import com.Erp.entity.logistics.Product;
import com.Erp.service.logistics.AccountService;
import com.Erp.service.logistics.OrderSheetService;
import com.Erp.service.logistics.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OrderSheetController {

    private final ProductService productService;
    private final OrderSheetService orderSheetService;
    private final AccountService accountService;

    //페이지 접속시 구매처 정보 바인딩
    @GetMapping(value = "/buyOrderSheets/list")
    public String getBuyAccountList(Model model) {

        List<AccountFormDto> accountFormDtos = accountService.buySelectAll();
        model.addAttribute("accountFormDtos", accountFormDtos);

        return "/orderSheet/buyOrderSheetForm";
    }

    // 데이터 베이스에 있는 구매 주문서 정보를 조회
    @GetMapping(value = "/buyOrderSheets/check")
    public @ResponseBody Object buyOrderSheetSelectAll(Model model) {
        List<OrderSheetFormDto> orderSheetFormDtos = orderSheetService.orderSheetListBuy();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", orderSheetFormDtos);
        Object data = map;

        return data;
    }

    //페이지 접속시 판매처 정보 바인딩
    @GetMapping(value = "/sellerOrderSheets/list")
    public String getSellerAccountList(Model model) {

        List<AccountFormDto> accountFormDtos = accountService.sellerSelectAll();
        model.addAttribute("accountFormDtos", accountFormDtos);

        return "/orderSheet/sellerOrderSheetForm";
    }

    // 데이터 베이스에 있는 판매 주문서 정보를 조회
    @GetMapping(value = "/sellerOrderSheets/check")
    public @ResponseBody Object sellerOrderSheetSelectAll(Model model) {
        List<OrderSheetFormDto> orderSheetFormDtos = orderSheetService.orderSheetListSeller();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", orderSheetFormDtos);
        Object data = map;

        return data;
    }

    // 데이터 베이스에 있는 구매 주문서 상태별 정보를 조회
    @PostMapping(value = "/OrderSheets/click")
    public @ResponseBody Object OrderSheetSelectFilter(@RequestBody Map<String,String> Filters) {
        String acCategory = "";
        String divCategory = "";

        for(String item : Filters.keySet()){
            if(item.equals("acCategory")){
                acCategory = Filters.get(item);
            }else {
                divCategory = Filters.get(item);
            }
        }
        List<OrderSheetFormDto> orderSheetFormDtos = orderSheetService.orderSheetListFilter(acCategory,divCategory);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", orderSheetFormDtos);
        Object data = map;

        return data;
    }


    //주문서 페이지에 수정할 데이터 정보 modal에 데이터를 넣어주는 메소드
    @PostMapping(value = "/orderSheets/updateOrderSheet")
    public @ResponseBody ResponseEntity updateData(@RequestBody String code, HttpServletRequest request){

        boolean department = this.getSession(request);

        if(!department){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        //현재 문제점 Ajax로 넘겨 받은 데이터안에 ""가 붙어있어서 문제 발생.
        code = code.substring(1,code.length()-1);

        OrderSheet orderSheet = orderSheetService.findByCode(code);

        OrderSheetUpdateFormDto orderSheetUpdateFormDto = OrderSheetUpdateFormDto.of(orderSheet); //필요한 데이터만 가져오는 작업.

        Map<String, Object> data= new HashMap<>();

        data.put("data", orderSheetUpdateFormDto);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    //orderSheet 테이블 저장,수정 구역
    @PostMapping(value = "/orderSheets/updateOrderSheets")
    public @ResponseBody ResponseEntity updateOrderSheets(@RequestBody Map<String,Object> osData, HttpServletRequest request) {

        boolean department = this.getSession(request);

        if(!department){
            return new ResponseEntity<String>("등록 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        String errorMessage = "";
        OrderSheetAddmDto orderSheetAddmDto = null;    //견적서 정보와
        List<OrderSheetDetailAddmDto> orderSheetDetailAddmDtos = new ArrayList<OrderSheetDetailAddmDto>(); //견적서 디테일 정보

        for (Map.Entry<String, Object> entry : osData.entrySet()) {
            Object value = entry.getValue();

            if(entry.getKey().equals("osInfo")) {//견적서의 정보가 담겨져 있는 데이터
                Map<String, String> innerMap = (Map<String, String>) value; // 맵으로 변환해주는 문장
                orderSheetAddmDto = OrderSheetAddmDto.OsMapMapper(innerMap);

            }else {
                Map<String, String> innerMap = (Map<String, String>) value; // 맵으로 변환해주는 문장
                OrderSheetDetailAddmDto orderSheetDetailAddmDto = OrderSheetDetailAddmDto.OsdMapMapper(innerMap);
                orderSheetDetailAddmDtos.add(orderSheetDetailAddmDto);
            }
        }
        if(String.valueOf(orderSheetAddmDto.getDivisionStatus()) == "입하대기"){
            LocalDateTime dateTime = LocalDateTime.now();

            if(orderSheetAddmDto.getOsReceiptDate() == null){
                errorMessage ="입고 예정일을 선택해 주세요.";
                return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
            }else if(dateTime.isAfter(orderSheetAddmDto.getOsReceiptDate())){   //현재일 보다 입고 예정일이 과거일수는 없습니다.
                errorMessage ="입고 예정일을 다시 선택해 주세요";
                return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
            }
        }

        List<OrderSheetDetail> orderSheetDetails = new ArrayList<OrderSheetDetail>();

        try {
            for (OrderSheetDetailAddmDto orderSheetDetailAddmDto : orderSheetDetailAddmDtos){

                OrderSheetDetail orderSheetDetail = null;
                Product product = productService.findByCode(orderSheetDetailAddmDto.getPrCode());

                if(orderSheetDetailAddmDto.getOsdId() == null){   //주문서 디테일 수정시 새로운 상품을 추가할경우
                    orderSheetDetail = OrderSheetDetail.createCode(orderSheetDetailAddmDto,product);
                    orderSheetDetail = orderSheetService.orderSheetDetailSave(orderSheetDetail);
                }else{
                    orderSheetDetail = orderSheetService.orderSheetDetailUpdate(orderSheetDetailAddmDto,product);
                }
                orderSheetDetails.add(orderSheetDetail);
            }
            orderSheetService.orderSheetUpdate(orderSheetAddmDto,orderSheetDetails);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            errorMessage =e.getMessage();
            return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
        }
    }

    //주문서 삭제 기능
    @PostMapping(value = "/orderSheets/deleteOrderSheets")
    public @ResponseBody ResponseEntity deleteEstimate(@RequestBody List<String> code, HttpServletRequest request){

        boolean department = this.getSession(request);

        if(!department){
            return new ResponseEntity<String>("삭제 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        orderSheetService.deleteOrderSheet(code);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public boolean getSession(HttpServletRequest request){

        HttpSession session = request.getSession();
        UserDto user = (UserDto) session.getAttribute("User");

        boolean department = false;

        if(user.getDepartment().equals("구매팀") || user.getDepartment().equals("영업팀") || user.getDepartment().equals("물류팀")) {
            department = true;
            return department;
        }else {
            return department;
        }
    }
}
