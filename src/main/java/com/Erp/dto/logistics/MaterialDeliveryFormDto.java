package com.Erp.dto.logistics;


import com.Erp.constant.ProductionLine;
import com.Erp.constant.StackAreaCategory;
import com.Erp.entity.logistics.MaterialDelivery;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter @Setter@ToString
public class MaterialDeliveryFormDto {

    private Long maDeliveryId;

    private String maDeliveryDate; // 불출일자

    private String prName; // 불출할 상품(자재)

    private int maDeliveryCount; // 불출수량

    private String secName; // 자재를 불출할 창고

    @Enumerated(EnumType.STRING)
    private StackAreaCategory stackAreaCategory; // 불출할 구역

    @Enumerated(EnumType.STRING)
    private ProductionLine productionLine; // 자재를 불출받을 공장라인

    private String createName; // 등록인

    public static MaterialDeliveryFormDto of(MaterialDelivery maDelivery){
        MaterialDeliveryFormDto materialDeliveryFormDto = new MaterialDeliveryFormDto();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String newMaDate = maDelivery.getMaDeliveryDate().format(formatter);

        materialDeliveryFormDto.setMaDeliveryId(maDelivery.getMaDeliveryId());
        materialDeliveryFormDto.setMaDeliveryDate(newMaDate);
        materialDeliveryFormDto.setPrName(maDelivery.getProduct().getPrName());
        materialDeliveryFormDto.setMaDeliveryCount(maDelivery.getMaDeliveryCount());
        materialDeliveryFormDto.setSecName(maDelivery.getSection().getSecName());
        materialDeliveryFormDto.setStackAreaCategory(maDelivery.getStackAreaCategory());
        materialDeliveryFormDto.setProductionLine(maDelivery.getProductionLine());
        materialDeliveryFormDto.setCreateName(maDelivery.getCreateName());

        return materialDeliveryFormDto;
    }

}
