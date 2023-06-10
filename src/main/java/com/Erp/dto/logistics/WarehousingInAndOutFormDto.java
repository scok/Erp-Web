package com.Erp.dto.logistics;


import com.Erp.constant.DivisionStatus;
import com.Erp.constant.StackAreaCategory;
import com.Erp.entity.logistics.WarehousingInAndOut;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter @Setter @ToString
public class WarehousingInAndOutFormDto {
    private Long wioId; //입고, 출고 코드
    private String acName;  //거래처 명
    private String secName; //창고 명
    private StackAreaCategory stackAreaCategory;    //구역 명
    private String prName;  //상품 명
    private int osQuantity; //수량
    private String inAndOutDate;    //입고, 출고 일자
    private DivisionStatus divisionStatus;  //상태

    public static WarehousingInAndOutFormDto of(WarehousingInAndOut warehousingInAndOut){

        WarehousingInAndOutFormDto warehousingInAndOutFormDto = new WarehousingInAndOutFormDto();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String newDate = warehousingInAndOut.getInAndOutDate().format(formatter);

        warehousingInAndOutFormDto.setWioId(warehousingInAndOut.getWioId());
        if (warehousingInAndOut.getOrderSheetDetail() != null){
            warehousingInAndOutFormDto.setAcName(warehousingInAndOut.getOrderSheetDetail().getProduct().getAccount().getAcName());
            warehousingInAndOutFormDto.setPrName(warehousingInAndOut.getOrderSheetDetail().getProduct().getPrName());
            warehousingInAndOutFormDto.setOsQuantity(warehousingInAndOut.getOrderSheetDetail().getOsQuantity());
        }else if(warehousingInAndOut.getProduction() != null){
            warehousingInAndOutFormDto.setAcName(warehousingInAndOut.getProduction().getProduct().getAccount().getAcName());
            warehousingInAndOutFormDto.setPrName(warehousingInAndOut.getProduction().getProduct().getPrName());
            warehousingInAndOutFormDto.setOsQuantity(warehousingInAndOut.getProduction().getCount());
        }else if(warehousingInAndOut.getMaterialDelivery() != null){
            warehousingInAndOutFormDto.setAcName(warehousingInAndOut.getMaterialDelivery().getProduct().getAccount().getAcName());
            warehousingInAndOutFormDto.setPrName(warehousingInAndOut.getMaterialDelivery().getProduct().getPrName());
            warehousingInAndOutFormDto.setOsQuantity(warehousingInAndOut.getMaterialDelivery().getMaDeliveryCount());
        }
        warehousingInAndOutFormDto.setSecName(warehousingInAndOut.getSection().getSecName());
        warehousingInAndOutFormDto.setStackAreaCategory(warehousingInAndOut.getStackAreaCategory());
        warehousingInAndOutFormDto.setInAndOutDate(newDate);
        warehousingInAndOutFormDto.setDivisionStatus(warehousingInAndOut.getDivisionStatus());
        return warehousingInAndOutFormDto;
    }
}
