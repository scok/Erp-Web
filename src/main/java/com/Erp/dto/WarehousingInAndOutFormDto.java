package com.Erp.dto;

import com.Erp.constant.DivisionStatus;
import com.Erp.constant.StackAreaCategory;
import com.Erp.entity.WarehousingInAndOut;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter @ToString
public class WarehousingInAndOutFormDto {
    private Long wioId;
    private String acName;
    private String secName;
    private StackAreaCategory stackAreaCategory;
    private String prName;
    private int osQuantity;
    private LocalDateTime inAndOutDate;
    private DivisionStatus divisionStatus;

    public static WarehousingInAndOutFormDto of(WarehousingInAndOut warehousingInAndOut){

        WarehousingInAndOutFormDto warehousingInAndOutFormDto = new WarehousingInAndOutFormDto();

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
        warehousingInAndOutFormDto.setInAndOutDate(warehousingInAndOut.getInAndOutDate());
        warehousingInAndOutFormDto.setDivisionStatus(warehousingInAndOut.getDivisionStatus());
        return warehousingInAndOutFormDto;
    }
}
