package com.Erp.dto;

import com.Erp.entity.Income;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class IncomeDto {

    private Long num ; // 손익번호
    private Integer quarter ; // 분기

    // 수익
    private Long sales_revenue; // 판매 수익
    private Long interest; // 이자 수익
    private Long rental; // 임대 수익
    private Long investment; // 투자 수익
    private Long licensing; // 라이선스 수익
    private Long total_revenue; // 총 수익

    // 비용
    private Long sales_salary; // 판매 인력 급여
    private Long compensation; // 판매 상여금
    private Long advertising; // 광고비
    private Long travel_expenses; // 출장비
    private Long mp_salary; // 관리 인력 급여
    private Long office_rent; // 임대료
    private Long accService_costs; // 회계 서비스 비용
    private Long consulting_costs; // 컨설팅 비용
    private Long fixtures; // 비품
    private Long salary; // 급여
    private Long employee_benefits; // 급여 부대비용
    private Long manage_expenses; // 인력 관리비용
    private Long raw_mat_cost; // 원자재 비용
    private Long components_cost; // 부품 비용
    private Long total_expenses; // 총 비용

    // 영업 이익
    private Long operate_revenue; // 영업 수익
    private Long operate_expenses; // 영업 비용
    private Long operate_income; // 영업 이익

    // 기타 비용
    private Long financial_expenses; // 금융 비용
    private Long depreciation_expenses; // 감가상각비
    private Long other_expenses; // 총 기타 비용

    // 세금 비용
    private Long corporate_tax; // 법인세
    private Long income_tax; // 소득세
    private Long tax_expenses; // 총 세금 비용

    private Long netIncome ; // 당기 순이익

    private Short year; // 작성 연도

    public IncomeDto() {
    }

    public IncomeDto(Income income, Long sumAmount, Long sales_salary, Long compensation, Long mp_salary, Long salary) {
        this.num = income.getNum();
        this.quarter = income.getQuarter();
        this.sales_revenue = sumAmount;
        this.interest = income.getInterest();
        this.rental = income.getRental();
        this.investment = income.getInvestment();
        this.licensing = income.getLicensing();
        this.total_revenue = income.getTotal_revenue();
        this.sales_salary = sales_salary;
        this.compensation = compensation;
        this.advertising = income.getAdvertising();
        this.travel_expenses = income.getTravel_expenses();
        this.mp_salary = mp_salary;
        this.office_rent = income.getOffice_rent();
        this.accService_costs = income.getAccService_costs();
        this.consulting_costs = income.getConsulting_costs();
        this.fixtures = income.getFixtures();
        this.salary = salary;
        this.employee_benefits = income.getEmployee_benefits();
        this.manage_expenses = income.getManage_expenses();
        this.raw_mat_cost = income.getRaw_mat_cost();
        this.components_cost = income.getComponents_cost();
        this.total_expenses = income.getTotal_expenses();
        this.operate_revenue = sumAmount;
        this.operate_expenses = income.getOperate_expenses();
        this.operate_income = income.getOperate_income();
        this.financial_expenses = income.getFinancial_expenses();
        this.depreciation_expenses = income.getDepreciation_expenses();
        this.other_expenses = income.getOther_expenses();
        this.corporate_tax = income.getCorporate_tax();
        this.income_tax = income.getIncome_tax();
        this.tax_expenses = income.getTax_expenses();
        this.netIncome = income.getNetIncome();
        this.year = income.getYear();
    }
}
