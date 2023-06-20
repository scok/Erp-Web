package com.Erp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IncomeChartData {

    private Long sales_revenue; // 매출액
    private Long operate_revenue; // 영업 수익
    private Long operate_expenses; // 영업 비용
    private Long netIncome ; // 당기 순이익
    private Short year; // 연도

    public IncomeChartData(Long sales_revenue, Long operate_revenue, Long operate_expenses, Long netIncome, Short year) {
        this.sales_revenue = sales_revenue;
        this.operate_revenue = operate_revenue;
        this.operate_expenses = operate_expenses;
        this.netIncome = netIncome;
        this.year = year;
    }
}
