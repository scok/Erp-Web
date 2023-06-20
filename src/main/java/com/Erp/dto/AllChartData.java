package com.Erp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AllChartData {

    private Long total_revenue; // 총 수익
    private Long netIncome; // 순이익
    private Long operate_expenses; // 운용 비용
    private Long mNum; // 직원 수
    private Long operate_income; // 운용 이익
    private Long sales_revenue; // 판매 수익
    private Long total_assets; // 총 자산
    private Short year; // 연도

    public AllChartData(Long total_revenue, Long netIncome, Long operate_expenses, Long mNum, Long operate_income, Long sales_revenue, Long total_assets, Short year) {
        this.total_revenue = total_revenue;
        this.netIncome = netIncome;
        this.operate_expenses = operate_expenses;
        this.mNum = mNum;
        this.operate_income = operate_income;
        this.sales_revenue = sales_revenue;
        this.total_assets = total_assets;
        this.year = year;
    }
}
