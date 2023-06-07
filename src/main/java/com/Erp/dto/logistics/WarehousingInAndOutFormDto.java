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
    private Long wioId;
    private String acName;
    private String secName;
    private StackAreaCategory stackAreaCategory;
    private String prName;
    private int osQuantity;
    private String inAndOutDate;
    private DivisionStatus divisionStatus;

    public static WarehousingInAndOutFormDto of(WarehousingInAndOut warehousingInAndOut){

        WarehousingInAndOutFormDto warehousingInAndOutFormDto = new WarehousingInAndOutFormDto();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String newDate = warehousingInAndOut.getInAndOutDate().format(formatter);

        warehousingInAndOutFormDto.setWioId(warehousingInAndOut.getWioId());
        if (warehousingInAndOut.getOrderSheetDetail() != null){
            warehousingInAndOutFormDto.setAcName(warehousingInAndOut.getOrderSheetDetail().getProduct().getAccount().getAcName());
            warehousingInAndOutFormDto.setPrName(warehousingInAndOut.getOrderSheetDetail().getProduct().getPrName());
            warehousingInAndOutFormDto.setOsQuantity(warehousingInAndOut.getOrderSheetDetail().getOsQuantity());
        }else{
            warehousingInAndOutFormDto.setAcName(warehousingInAndOut.getProduction().getProduct().getAccount().getAcName());
            warehousingInAndOutFormDto.setPrName(warehousingInAndOut.getProduction().getProduct().getPrName());
            warehousingInAndOutFormDto.setOsQuantity(warehousingInAndOut.getProduction().getCount());
        }
        warehousingInAndOutFormDto.setSecName(warehousingInAndOut.getSection().getSecName());
        warehousingInAndOutFormDto.setStackAreaCategory(warehousingInAndOut.getStackAreaCategory());
        warehousingInAndOutFormDto.setInAndOutDate(newDate);
        warehousingInAndOutFormDto.setDivisionStatus(warehousingInAndOut.getDivisionStatus());
        return warehousingInAndOutFormDto;
    }
}
