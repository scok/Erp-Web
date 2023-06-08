package com.Erp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TotalChartData {

    private Long total_revenue;
    private Long netIncome;
    private Long operate_expenses;
    private Long member_num;
    private Long total_income;
    private Long turnOver;
    private Short year;

    public TotalChartData(Long total_revenue, Long netIncome, Long operate_expenses, Long member_num, Long total_income, Long turnOver, Short year) {
        this.total_revenue = total_revenue;
        this.netIncome = netIncome;
        this.operate_expenses = operate_expenses;
        this.member_num = member_num;
        this.total_income = total_income;
        this.turnOver = turnOver;
        this.year = year;
    }
}
