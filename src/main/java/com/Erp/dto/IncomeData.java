package com.Erp.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class IncomeData {

    private int draw;
    private int recordsTotal;
    private int recordsFiltered;

    private List<IncomeDto> data;

    public List<IncomeDto> getData(){

        if(CollectionUtils.isEmpty(data)){

            data = new ArrayList<IncomeDto>();
        }

        return data;
    }
}
