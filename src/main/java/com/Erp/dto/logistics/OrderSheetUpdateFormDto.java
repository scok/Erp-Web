package com.Erp.dto.logistics;


import com.Erp.constant.AccountCategory;
import com.Erp.constant.DivisionStatus;
import com.Erp.entity.logistics.OrderSheet;
import com.Erp.entity.logistics.OrderSheetDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;


@Getter@Setter@ToString
public class OrderSheetUpdateFormDto {

    private String osCode;        //주문서 코드

    private String acCode;        //거래처 코드

    private Long osTotalPrice;    //총 금액

    private AccountCategory acCategory;     //거래 유형

    private DivisionStatus divisionStatus;    // 반려, 승인,승인 대기 등 상태 표시

    private LocalDateTime osReceiptDate;    //입고 예정 일자

    private String osComment;    //특이사항

    private List<OrderSheetDetail> orderSheetDetails; //견적서 디테일과 매핑

    public static OrderSheetUpdateFormDto of(OrderSheet orderSheet) {
        OrderSheetUpdateFormDto orderSheetUpdateFormDto = new OrderSheetUpdateFormDto();

        orderSheetUpdateFormDto.setOsCode(orderSheet.getOsCode());
        orderSheetUpdateFormDto.setAcCode(orderSheet.getEstimate().getAccount().getAcCode());
        orderSheetUpdateFormDto.setOsTotalPrice(orderSheet.getOsTotalPrice());
        orderSheetUpdateFormDto.setAcCategory(orderSheet.getAcCategory());
        orderSheetUpdateFormDto.setDivisionStatus(orderSheet.getDivisionStatus());
        orderSheetUpdateFormDto.setOsReceiptDate(orderSheet.getOsReceiptDate());
        orderSheetUpdateFormDto.setOsComment(orderSheet.getOsComment());
        orderSheetUpdateFormDto.setOrderSheetDetails(orderSheet.getOrderSheetDetails());

        return orderSheetUpdateFormDto;
    }
}
