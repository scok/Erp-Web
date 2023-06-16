package com.Erp.dto.logistics;

import com.Erp.constant.DivisionStatus;
import com.Erp.constant.StackAreaCategory;
import com.Erp.entity.logistics.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class WarehousingInAndOutDetailDto{

    private AccountAddDto accountAddDto;
    private ProductAddDto productAddDto;
    private OrderSheetDetailAddmDto orderSheetDetailAddmDto;
    private ProductionFormDto productionFormDto;
    private MaterialDeliveryFormDto materialDeliveryFormDto;

    //주문을 통한 입고, 출고 내역입니다.
    public static WarehousingInAndOutDetailDto order(WarehousingInAndOut warehousing){

        WarehousingInAndOutDetailDto warehousingInAndOut = new WarehousingInAndOutDetailDto();
        warehousingInAndOut.setAccountAddDto(AccountAddDto.of(warehousing.getOrderSheetDetail().getOrderSheet().getAccount()));
        warehousingInAndOut.setProductAddDto(ProductAddDto.of(warehousing.getOrderSheetDetail().getProduct()));
        warehousingInAndOut.setOrderSheetDetailAddmDto(OrderSheetDetailAddmDto.of(warehousing.getOrderSheetDetail()));

        return warehousingInAndOut;
    }

    //생산을 통한 입고 내역입니다.
    public static WarehousingInAndOutDetailDto production(WarehousingInAndOut warehousing) {
        WarehousingInAndOutDetailDto warehousingInAndOut = new WarehousingInAndOutDetailDto();

        warehousingInAndOut.setAccountAddDto(AccountAddDto.of(warehousing.getProduction().getProduct().getAccount()));
        warehousingInAndOut.setProductAddDto(ProductAddDto.of(warehousing.getProduction().getProduct()));
        warehousingInAndOut.setProductionFormDto(ProductionFormDto.of(warehousing.getProduction()));

        return warehousingInAndOut;
    }

    //자재 불출을 통한 출고 내역입니다.
    public static WarehousingInAndOutDetailDto Delivery(WarehousingInAndOut warehousing) {
        WarehousingInAndOutDetailDto warehousingInAndOut = new WarehousingInAndOutDetailDto();

        warehousingInAndOut.setAccountAddDto(AccountAddDto.of(warehousing.getMaterialDelivery().getProduct().getAccount()));
        warehousingInAndOut.setProductAddDto(ProductAddDto.of(warehousing.getMaterialDelivery().getProduct()));
        warehousingInAndOut.setMaterialDeliveryFormDto(MaterialDeliveryFormDto.of(warehousing.getMaterialDelivery()));

        return warehousingInAndOut;
    }
}
