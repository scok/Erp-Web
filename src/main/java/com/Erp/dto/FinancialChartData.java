package com.Erp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FinancialChartData {

    private Long total_assets; // 총 자산
    private Long current_assets; // 유동 자산
    private Long tangible_assets; // 유형 자산
    private Long paid_capital; // 자기자본
    private Long total_liabilities; // 총 부채
    private Long total_capital; // 총 자본
    private Short year; // 연도

    public FinancialChartData(Long total_assets, Long current_assets, Long tangible_assets, Long paid_capital, Long total_liabilities, Long total_capital, Short year) {
        this.total_assets = total_assets;
        this.current_assets = current_assets;
        this.tangible_assets = tangible_assets;
        this.paid_capital = paid_capital;
        this.total_liabilities = total_liabilities;
        this.total_capital = total_capital;
        this.year = year;
    }
}
