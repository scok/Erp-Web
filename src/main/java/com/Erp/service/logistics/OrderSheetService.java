package com.Erp.service.logistics;

import com.Erp.constant.AccountCategory;
import com.Erp.constant.DivisionStatus;
import com.Erp.dto.logistics.OrderSheetAddmDto;
import com.Erp.dto.logistics.OrderSheetDetailAddmDto;
import com.Erp.dto.logistics.OrderSheetFormDto;
import com.Erp.entity.logistics.*;
import com.Erp.repository.logistics.OrderSheetRepository;
import com.Erp.repository.logistics.OrderSheetDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderSheetService {

    private final OrderSheetRepository orderSheetRepository;
    private final OrderSheetDetailRepository orderSheetDetailRepository;

    //구매 주문서 전부를 조회 합니다.
    public List<OrderSheetFormDto> orderSheetListBuy() {
        List<OrderSheet> buyOrderSheets = orderSheetRepository.findBuyOrderSheets();
        List<OrderSheetFormDto> orderSheetFormDtos = new ArrayList<OrderSheetFormDto>();
        for (OrderSheet orderSheet : buyOrderSheets){
            OrderSheetFormDto dto = OrderSheetFormDto.of(orderSheet);
            orderSheetFormDtos.add(dto);
        }
        return orderSheetFormDtos;
    }

    //판매 주문서 전부를 조회 합니다.
    public List<OrderSheetFormDto> orderSheetListSeller() {
        List<OrderSheet> buyOrderSheets = orderSheetRepository.findSellerOrderSheets();
        List<OrderSheetFormDto> orderSheetFormDtos = new ArrayList<OrderSheetFormDto>();
        for (OrderSheet orderSheet : buyOrderSheets){
            OrderSheetFormDto dto = OrderSheetFormDto.of(orderSheet);
            orderSheetFormDtos.add(dto);
        }
        return orderSheetFormDtos;
    }

    //주문서를 필터하여 조회합니다.
    public List<OrderSheetFormDto> orderSheetListFilter(String acCategory,String divCategory) {
        List<OrderSheet> buyOrderSheets = orderSheetRepository.findBuyOrderSheetFilter(AccountCategory.valueOf(acCategory),DivisionStatus.valueOf(divCategory));
        List<OrderSheetFormDto> orderSheetFormDtos = new ArrayList<OrderSheetFormDto>();
        for (OrderSheet orderSheet : buyOrderSheets){
            OrderSheetFormDto dto = OrderSheetFormDto.of(orderSheet);
            orderSheetFormDtos.add(dto);
        }
        return orderSheetFormDtos;
    }

    //주문서 1건에 대한 데이터를 찾습니다.
    public OrderSheet findByCode(String code) {
        return orderSheetRepository.findById(code).orElseThrow(EntityNotFoundException::new);
    }

    //주문서 디테일 1건을 저장합니다.
    public OrderSheetDetail orderSheetDetailSave(OrderSheetDetail orderSheetDetail) {
        return orderSheetDetailRepository.save(orderSheetDetail);
    }

    //주문서 디테일 1건을 수정합니다.
    public OrderSheetDetail orderSheetDetailUpdate(OrderSheetDetailAddmDto orderSheetDetailAddmDto, Product product) {

        OrderSheetDetail orderSheetDetail = orderSheetDetailRepository.getReferenceById(orderSheetDetailAddmDto.getOsdId());

        orderSheetDetail.setOsQuantity(orderSheetDetailAddmDto.getOsQuantity());
        orderSheetDetail.setOsStandard(orderSheetDetailAddmDto.getOsStandard());
        orderSheetDetail.setOsSupplyValue(orderSheetDetailAddmDto.getOsSupplyValue());
        orderSheetDetail.setOsTaxAmount(orderSheetDetailAddmDto.getOsTaxAmount());
        orderSheetDetail.setOsPrice(orderSheetDetailAddmDto.getOsPrice());
        orderSheetDetail.setProduct(product);

        return orderSheetDetail;
    }
    //주문서 1건을 수정합니다.
    @Transactional
    public void orderSheetUpdate(OrderSheetAddmDto orderSheetAddmDto, List<OrderSheetDetail> orderSheetDetails) {

        OrderSheet orderSheet = orderSheetRepository.findById(orderSheetAddmDto.getOsCode()).orElseThrow(EntityNotFoundException::new);

        orderSheet.setOsTotalPrice(orderSheetAddmDto.getOsTotalPrice());
        orderSheet.setAcCategory(orderSheetAddmDto.getAcCategory());
        if(orderSheetAddmDto.getDivisionStatus() == null){
            orderSheet.setDivisionStatus(DivisionStatus.주문대기);
        }else {
            orderSheet.setDivisionStatus(orderSheetAddmDto.getDivisionStatus());
        }
        orderSheet.setOsComment(orderSheetAddmDto.getOsComment());
        orderSheet.setOsReceiptDate(orderSheetAddmDto.getOsReceiptDate());
        orderSheet.setOrderSheetDetails(orderSheetDetails);
    }


    //견적서 상태가 승인 상태일 경우 주문서에 저장하는 로직.
    public void orderSheetSave(Estimate estimate, List<EstimateDetail> estimateDetails) {

        List<OrderSheetDetail> orderSheetDetails = new ArrayList<OrderSheetDetail>();   //주문서 Detail을 저장합니다.
        for(EstimateDetail estimateDetail : estimateDetails){

            OrderSheetDetail orderSheetDetail = new OrderSheetDetail();

            orderSheetDetail.setOsPrice(estimateDetail.getEsPrice());
            orderSheetDetail.setOsStandard(estimateDetail.getEsStandard());
            orderSheetDetail.setOsQuantity(estimateDetail.getEsQuantity());
            orderSheetDetail.setOsSupplyValue(estimateDetail.getEsSupplyValue());
            orderSheetDetail.setOsTaxAmount(estimateDetail.getEsTaxAmount());
            orderSheetDetail.setProduct(estimateDetail.getProduct());

            orderSheetDetailRepository.save(orderSheetDetail);
            orderSheetDetails.add(orderSheetDetail);
        }

        //주문서를 저장합니다.
        LocalDateTime regDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String strNowDate = regDateTime.format(formatter);

        int year = Integer.parseInt(strNowDate.substring(0, 4));
        int month = Integer.parseInt(strNowDate.substring(4, 6));
        int day = Integer.parseInt(strNowDate.substring(6));

        int count = orderSheetRepository.getOrderSheetsCount(year, month, day);

        String code = "";
        DecimalFormat newcnt = new DecimalFormat("00");

        if(String.valueOf(estimate.getAcCategory()).equals("구매")){   //자재일 경우
            code = "OSBU-" + strNowDate +"-"+ newcnt.format(count+1);
        }
        if(String.valueOf(estimate.getAcCategory()).equals("판매")){ //제품 경우
            code = "OSSE-" + strNowDate+"-"+ newcnt.format(count+1);
        }

        if (code == null || code.trim() == ""){
            throw new IllegalArgumentException("code가 비어있습니다.");
        }
        OrderSheet orderSheet = new OrderSheet();

        orderSheet.setPageYandN("Y");
        orderSheet.setOsCode(code);
        orderSheet.setOsTotalPrice(estimate.getEsTotalPrice());
        orderSheet.setAcCategory(estimate.getAcCategory());
        orderSheet.setDivisionStatus(DivisionStatus.주문대기);
        orderSheet.setEstimate(estimate);
        orderSheet.setOrderSheetDetails(orderSheetDetails);
        orderSheet.setAccount(estimate.getAccount());
        orderSheet.setCreateName(estimate.getCreateName());

        orderSheetRepository.save(orderSheet);
    }

    //주문서 상태가 입하 대기인 항목만 불러옵니다.
    public List<OrderSheetFormDto> orderSheetListBuyState() {
        List<OrderSheet> buyOrderSheets = orderSheetRepository.findBuyOrderSheetsState();
        List<OrderSheetFormDto> orderSheetFormDtos = new ArrayList<OrderSheetFormDto>();
        for (OrderSheet orderSheet : buyOrderSheets){
            OrderSheetFormDto dto = OrderSheetFormDto.of(orderSheet);
            orderSheetFormDtos.add(dto);
        }
        return orderSheetFormDtos;
    }

    //주문서 상태가 입하 대기인 항목만 불러옵니다.
    public List<OrderSheetFormDto> orderSheetListSellerState() {
        List<OrderSheet> buyOrderSheets = orderSheetRepository.findSellerOrderSheetsState();
        List<OrderSheetFormDto> orderSheetFormDtos = new ArrayList<OrderSheetFormDto>();
        for (OrderSheet orderSheet : buyOrderSheets){
            OrderSheetFormDto dto = OrderSheetFormDto.of(orderSheet);
            orderSheetFormDtos.add(dto);
        }
        return orderSheetFormDtos;
    }

    //주문서 삭제 공정
    public void deleteOrderSheet(List<String> code) {
        for (String osCode :  code){
            OrderSheet orderSheet = orderSheetRepository.findById(osCode).orElseThrow(EntityNotFoundException::new);
            orderSheet.setPageYandN("N");
        }
    }

    //주문서의 상태를 업데이트 해줍니다.
    @Transactional
    public OrderSheet OrderSheetStatusUpdate(String code, String status) {
        OrderSheet orderSheet = orderSheetRepository.findById(code).orElseThrow(EntityNotFoundException::new);
        orderSheet.setDivisionStatus(DivisionStatus.valueOf(status));
        return orderSheet;
    }

    public List<OrderSheet> orderSheetListAll() {

        return orderSheetRepository.findByAll();
    }

    public List<OrderSheet> orderSheetOsRecFilter(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return orderSheetRepository.orderSheetOsRecFilter(startDateTime,endDateTime);
    }
}
