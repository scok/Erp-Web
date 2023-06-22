package com.Erp.dto;

import com.Erp.entity.Income;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class IncomeDto {

    private Long id ; // 손익번호
    private Integer quarter ; // 분기

    // 수익
    private Long sales_revenue; // 판매 수익
    private Long interest; // 이자 수익
    private Long rental; // 임대 수익
    private Long investment; // 투자 수익
    private Long licensing; // 라이선스 수익
    private Long total_revenue; // 총 수익

    // 비용
    private Long salary; // 기본급
    private Long bonus; // 상여금
    private Long plusMoney; // 지급 총액
    private Long minusMoney; // 공제 총액
    private Long totalMoney; // 차감 지급액
    private Long manage_expenses; // 인력 관리비용
    private Long advertising; // 광고비
    private Long office_rent; // 임대료
    private Long accService_costs; // 회계 서비스 비용
    private Long consulting_costs; // 컨설팅 비용
    private Long fixtures; // 비품
    private Long raw_mat_cost; // 원자재 비용
    private Long components_cost; // 부품 비용
    private Long total_expenses; // 총 비용

    // 운영 이익
    private Long operate_revenue; // 운영 수익
    private Long operate_expenses; // 운용 비용
    private Long operate_income; // 운용 이익

    // 기타 비용
    private Long financial_expenses; // 금융 비용
    private Long depreciation_expenses; // 감가상각비
    private Long other_expenses; // 총 기타 비용

    // 세금 비용
    private Long corporate_tax; // 법인세
    private Long income_tax; // 소득세
    private Long localTax; // 지방소득세
    private Long tax_expenses; // 총 세금 비용

    private Long netIncome ; // 당기 순이익

    private Short year; // 작성 연도

    public IncomeDto() {
    }

    public IncomeDto(Income income, Long sales_revenue, Long salary, Long bonus, Long plusMoney, Long minusMoney, Long totalMoney, Long manage_expenses, Long income_tax, Long localTax, Long raw_mat_cost, Long components_cost) {
        this.id = income.getId();
        this.quarter = income.getQuarter();
        this.sales_revenue = sales_revenue;
        this.interest = income.getInterest();
        this.rental = income.getRental();
        this.investment = income.getInvestment();
        this.licensing = income.getLicensing();
        this.total_revenue = this.sales_revenue + this.interest + this.rental + this.investment + this.licensing;

        this.salary = salary;
        this.bonus = bonus;
        this.plusMoney = plusMoney;
        this.minusMoney = minusMoney;
        this.totalMoney = totalMoney;
        this.manage_expenses = manage_expenses;
        this.advertising = income.getAdvertising();
        this.office_rent = income.getOffice_rent();
        this.accService_costs = income.getAccService_costs();
        this.consulting_costs = income.getConsulting_costs();
        this.fixtures = income.getFixtures();
        this.raw_mat_cost = raw_mat_cost;
        this.components_cost = components_cost;
        this.total_expenses = this.totalMoney + this.manage_expenses + this.advertising + this.office_rent + this.accService_costs + this.consulting_costs + this.fixtures + this.raw_mat_cost + this.components_cost;

        this.operate_revenue = this.sales_revenue;
        this.operate_expenses = this.total_expenses - this.raw_mat_cost - this.components_cost;
        this.operate_income = this.operate_revenue - this.operate_expenses;

        this.financial_expenses = income.getFinancial_expenses();
        this.depreciation_expenses = income.getDepreciation_expenses();
        this.other_expenses = this.financial_expenses + this.depreciation_expenses;

        this.netIncome = this.total_revenue - this.total_expenses - this.other_expenses;

        this.corporate_tax = (long) (this.netIncome * 0.22);
        this.income_tax = income_tax;
        this.localTax = localTax;
        this.tax_expenses = this.corporate_tax + this.income_tax + this.localTax;

        this.year = income.getYear();
    }
}
