package com.Erp.entity;

import com.Erp.constant.SectionCategory;
import com.Erp.dto.SectionAddDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.DecimalFormat;

@Entity
@Table(name = "sections")
@Getter
@Setter
public class Section extends DistributionEntity{
    // 공급처 테이블
    @Id
    @Column(unique = true)
    private String secCode; // 공급처 코드 (SE-20230516-01) : SE + 등록일 + 01(카운트)

    @Column(nullable = false)
    private String secName; // 공급처 이름(자재창고1, 제품창고1, 생산공장1)

    @Column(nullable = false, name = "secCategory")
    @Enumerated(EnumType.STRING)
    private SectionCategory secCategory; // 공급처 분류(자재창고, 제품창고)

    @Column(name = "secTotalCount")
    private int secTotalCount; // 상품(자재, 제품)별 현재 총 적재 수량

    @Column(nullable = false, name = "secMaxCount")
    private int secMaxCount; // 공급처별 최대 적재 수량

    @Column(name = "secAddress")
    private String secAddress; // 공급처 위치

    // Section 엔터티 필드값 저장
    public static Section createSection(SectionAddDto sectionAddDto, String strNowDate, int count){

        String code = null;
        DecimalFormat newcnt = new DecimalFormat("-00");
        code = strNowDate + newcnt.format(count+1);
        String secCode = "SE-" + code;

        Section section = new Section();

        section.setSecCode(secCode);
        section.setSecCategory(sectionAddDto.getSecCategory());
        section.setSecName(sectionAddDto.getSecName());
        section.setSecMaxCount(sectionAddDto.getSecMaxCount());
        section.setSecAddress(sectionAddDto.getSecAddress());
        section.setPageYandN("Y");

        return section;
    }

}
