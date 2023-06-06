package com.Erp.dto.logistics;


import com.Erp.constant.AccountCategory;
import com.Erp.constant.DivisionStatus;
import com.Erp.entity.logistics.DistributionEntity;
import com.Erp.entity.logistics.OrderSheet;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.format.DateTimeFormatter;

@Getter @Setter
public class OrderSheetFormDto extends DistributionEntity {

    private String osCode;  //주문서 코드

    @Enumerated(EnumType.STRING)
    private AccountCategory acCategory;     //거래 유형

    private String prName;      //품명

    private Long osTotalPrice;    //총금액

    private String acName;      //공급처 명

    private String acCeo;      //공급처 대표자 명

    private String acHomePage;      //거래처 홈페이지

    private String osCreateName;       //작성자

    private String osRegDate;  //등록일자


    @Enumerated(EnumType.STRING)
    private DivisionStatus divisionStatus;

    public static OrderSheetFormDto of(OrderSheet orderSheet){

        OrderSheetFormDto orderSheetFormDto = new OrderSheetFormDto();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String newRegDate = orderSheet.getRegDate().format(formatter);

        orderSheetFormDto.setOsCode(orderSheet.getOsCode());
        orderSheetFormDto.setAcCategory(orderSheet.getAcCategory());

        String prName = "";
        if (orderSheet.getOrderSheetDetails().size() > 1){   //상품이 여러개 일경우
            prName = orderSheet.getEstimate().getEstimateDetail().get(0).getProduct().getPrName() +
                    " 외"+orderSheet.getOrderSheetDetails().size()+"개";
        }else{
            prName = orderSheet.getEstimate().getEstimateDetail().get(0).getProduct().getPrName();
        }
        orderSheetFormDto.setPrName(prName);
        orderSheetFormDto.setOsTotalPrice(orderSheet.getOsTotalPrice());
        orderSheetFormDto.setAcName(orderSheet.getEstimate().getAccount().getAcName());
        orderSheetFormDto.setAcCeo(orderSheet.getEstimate().getAccount().getAcCeo());
        orderSheetFormDto.setAcHomePage(orderSheet.getEstimate().getAccount().getAcHomePage());
        orderSheetFormDto.setDivisionStatus(orderSheet.getDivisionStatus());
        orderSheetFormDto.setOsCreateName(orderSheet.getCreateName());
        orderSheetFormDto.setOsRegDate(newRegDate);

        return orderSheetFormDto;
    }
}
