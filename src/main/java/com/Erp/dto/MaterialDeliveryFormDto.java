package com.Erp.dto;

import com.Erp.constant.ProductionLine;
import com.Erp.constant.StackAreaCategory;
import com.Erp.entity.MaterialDelivery;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter @Setter@ToString
public class MaterialDeliveryFormDto {

    private Long maDeliveryId; // 자재불출 id

    private LocalDateTime maDeliveryDate; // 불출일자

    private String prName; // 불출할 상품(자재)

    private Long maDeliveryCount; // 불출수량

    private String secName; // 자재를 불출할 창고

    @Enumerated(EnumType.STRING)
    private StackAreaCategory stackAreaCategory; // 불출할 구역

    @Enumerated(EnumType.STRING)
    private ProductionLine productionLine; // 자재를 불출받을 공장라인

    private String createBy; // 등록인



    private static ModelMapper modelMapper = new ModelMapper();
    static {
        TypeMap<MaterialDelivery, MaterialDeliveryFormDto> typeMap = modelMapper.createTypeMap(MaterialDelivery.class, MaterialDeliveryFormDto.class);
        typeMap.addMappings(mapper -> {
            mapper.map(src -> src.getProduct().getPrName(), MaterialDeliveryFormDto::setPrName);
            mapper.map(src -> src.getSection().getSecName(), MaterialDeliveryFormDto::setSecName);
            mapper.map(src -> src.getStackAreaCategory(), MaterialDeliveryFormDto::setStackAreaCategory);
        });
    }
    public static MaterialDeliveryFormDto of(MaterialDelivery maDelivery){
        return modelMapper.map(maDelivery, MaterialDeliveryFormDto.class);
    }

}
