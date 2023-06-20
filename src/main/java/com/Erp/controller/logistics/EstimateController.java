package com.Erp.controller.logistics;

import com.Erp.dto.UserDto;
import com.Erp.dto.logistics.*;
import com.Erp.entity.Member;
import com.Erp.entity.logistics.*;
import com.Erp.entity.logistics.Account;
import com.Erp.entity.logistics.Estimate;
import com.Erp.entity.logistics.EstimateDetail;
import com.Erp.entity.logistics.Product;
import com.Erp.service.MemberService;
import com.Erp.service.logistics.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class EstimateController {

    private final ProductService productService;
    private final EstimateService estimateService;
    private final OrderSheetService orderSheetService;
    private final AccountService AccountService;
    private final MemberService memberService;

    //페이지 접속시 구매처 정보 바인딩
    @GetMapping(value = "/buyEstimates/list")
    public String getBuyAccountList(Model model) {

        List<AccountFormDto> accountFormDtos = AccountService.buySelectAll();
        model.addAttribute("accountFormDtos", accountFormDtos);

        return "/estimate/buyEstimateForm";
    }

    // 데이터 베이스에 있는 구매 견적서 정보를 조회
    @GetMapping(value = "/buyEstimates/check")
    public @ResponseBody Object buyEstimatesSelectAll(Model model) {
        List<EstimateFormDto> estimateFormDtos = estimateService.estimateListBuy();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", estimateFormDtos);
        Object data = map;

        return data;
    }

    //판매처 정보 바인딩
    @GetMapping(value = "/sellerEstimates/list")
    public String getSellerAccountList(Model model) {

        List<AccountFormDto> accountFormDtos = AccountService.sellerSelectAll();
        model.addAttribute("accountFormDtos", accountFormDtos);

        return "/estimate/sellerEstimateForm";
    }

    // 데이터 베이스에 있는 판매 견적서 정보를 조회
    @GetMapping(value = "/sellerEstimates/check")
    public @ResponseBody Object SelectAll(Model model) {
        List<EstimateFormDto> estimateFormDtos = estimateService.estimateListSeller();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", estimateFormDtos);
        Object data = map;

        return data;
    }

    //견적서 페이지에 수정할 데이터 정보 modal에 데이터를 넣어주는 메소드
    @PostMapping(value = "/estimates/updateEstimate")
    public @ResponseBody ResponseEntity updateData(@RequestBody String code, HttpServletRequest request){

        boolean department = this.getSession(request);

        if(!department){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        //현재 문제점 Ajax로 넘겨 받은 데이터안에 ""가 붙어있어서 문제 발생.
        code = code.substring(1,code.length()-1);

        Estimate estimate = estimateService.findByCode(code);

        EstimateUpdateFormDto estimateUpdateFormsDto = EstimateUpdateFormDto.of(estimate); //필요한 데이터만 가져오는 작업.

        Map<String, Object> data= new HashMap<>();

        data.put("data", estimateUpdateFormsDto);

        return new ResponseEntity<>(data,HttpStatus.OK);
    }

    //Estimate 테이블 저장,수정 구역
    @PostMapping(value = "/estimates/addEstimate")
    public @ResponseBody ResponseEntity addEstimate(@RequestBody Map<String,Object> esData, Principal principal, HttpServletRequest request) {

        boolean department = this.getSession(request);

        if(!department){
            return new ResponseEntity<String>("작성 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        EstimateAddmDto estimateAddmDto = null;    //견적서 정보와
        List<EstimateDetailAddmDto> estimateDetailAddmDtos = new ArrayList<EstimateDetailAddmDto>(); //견적서 디테일 정보

        for (Map.Entry<String, Object> entry : esData.entrySet()) {
            Object value = entry.getValue();

            if(entry.getKey().equals("esInfo")) {//견적서의 정보가 담겨져 있는 데이터
                Map<String, String> innerMap = (Map<String, String>) value; // 맵으로 변환해주는 문장
                estimateAddmDto = EstimateAddmDto.EsMapMapper(innerMap);

            }else {
                Map<String, String> innerMap = (Map<String, String>) value; // 맵으로 변환해주는 문장
                EstimateDetailAddmDto estimateDetailAddmDto = EstimateDetailAddmDto.EsdMapMapper(innerMap);
                estimateDetailAddmDtos.add(estimateDetailAddmDto);
            }
        }
        String errorMessage = "";
        LocalDateTime regDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String strNowDate = regDateTime.format(formatter);

        int year = Integer.parseInt(strNowDate.substring(0, 4));
        int month = Integer.parseInt(strNowDate.substring(4, 6));
        int day = Integer.parseInt(strNowDate.substring(6));

        int count = estimateService.getEstimateCount(year, month, day);
        Member member = memberService.getMemberName(principal.getName());

        List<EstimateDetail> estimateDetailList = new ArrayList<EstimateDetail>();

        try {
            if(estimateAddmDto.getEsCode() == null) {//만약 코드에 유의미한 값이 들어 있다면 수정기능을 사용합니다.
                //여기는 등록 기능입니다.
                //견적서 디테일 먼저 저장합니다.
                for (EstimateDetailAddmDto estimateDetailAddmDto : estimateDetailAddmDtos){
                    Product product = productService.findByCode(estimateDetailAddmDto.getPrCode());
                    EstimateDetail estimateDetail = EstimateDetail.createCode(estimateDetailAddmDto,product);
                    estimateDetailList.add(estimateDetail);
                }
                Account account = AccountService.findByAcCode(estimateAddmDto.getAcCode());

                Estimate estimate = Estimate.createCode(estimateAddmDto, account, strNowDate, count,estimateDetailList,member.getName());

                estimateService.estimateSave(estimate);

                return new ResponseEntity<>(HttpStatus.OK);

            } else{    //여기는 수정기능입니다. 수정시 견적서 상태가 승인이면 주문서 데이터 테이블에 값을 넣어줍니다.
                List<EstimateDetail> estimateDetails = new ArrayList<EstimateDetail>();

                for (EstimateDetailAddmDto estimateDetailAddmDto : estimateDetailAddmDtos){

                    EstimateDetail estimateDetail = null;
                    Product product = productService.findByCode(estimateDetailAddmDto.getPrCode());

                    if(estimateDetailAddmDto.getEsdId() == null){   //견적서 디테일 수정시 새로운 상품을 추가할경우
                        estimateDetail = EstimateDetail.createCode(estimateDetailAddmDto,product);
                        estimateDetail = estimateService.estimateDetailSave(estimateDetail);
                    }else{
                        estimateDetail = estimateService.estimateDetailUpdate(estimateDetailAddmDto,product);
                    }
                    estimateDetails.add(estimateDetail);
                }
                Estimate estimate = estimateService.estimateUpdate(estimateAddmDto,estimateDetails);
                if(String.valueOf(estimateAddmDto.getDivisionStatus()).equals("승인")){ //견적서 상태가 승인일경우 주문서 테이블에 저장합니다.
                    orderSheetService.orderSheetSave(estimate,estimateDetails);
                }
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }catch (Exception e){
            errorMessage =e.getMessage();
            return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/estimates/deleteEstimate")
    public @ResponseBody ResponseEntity deleteEstimate(@RequestBody List<String> code, HttpServletRequest request){

        boolean department = this.getSession(request);

        if(!department){
            return new ResponseEntity<String>("삭제 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        estimateService.deleteEstimate(code);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 데이터 베이스에 있는 구매 주문서 상태별 정보를 조회
    @PostMapping(value = "/estimates/click")
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
        List<EstimateFormDto> estimateFormDtos = estimateService.estimateListFilter(acCategory,divCategory);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", estimateFormDtos);
        Object data = map;

        return data;
    }

    public boolean getSession(HttpServletRequest request){

        HttpSession session = request.getSession();
        UserDto user = (UserDto) session.getAttribute("User");

        boolean department = false;

        if(user.getDepartment().equals("구매팀") || user.getDepartment().equals("영업팀")) {
            department = true;
            return department;
        }else {
            return department;
        }
    }
}
