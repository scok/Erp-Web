package com.Erp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Incomes")
@Getter@Setter
public class Income {

    @Id
    @Column(name = "income_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    // 거래 ( 판매 수익 )
    @OneToMany(mappedBy = "income", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>() ;

    // 사원 ( 급여, 상여금 )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Member member;

    public void setDynamicField(String fieldName, Object fieldValue) throws Exception{
        String setterName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        Method setter = getClass().getMethod(setterName, fieldValue.getClass());
        setter.invoke(this, fieldValue);
    }

    public Income() {
    }

    public Income(Long initNum, Short year, Integer quarter) {
        this.quarter = quarter;
        this.sales_revenue = initNum;
        this.interest = initNum;
        this.rental = initNum;
        this.investment = initNum;
        this.licensing = initNum;
        this.total_revenue = initNum;
        this.sales_salary = initNum;
        this.compensation = initNum;
        this.advertising = initNum;
        this.travel_expenses = initNum;
        this.mp_salary = initNum;
        this.office_rent = initNum;
        this.accService_costs = initNum;
        this.consulting_costs = initNum;
        this.fixtures = initNum;
        this.salary = initNum;
        this.employee_benefits = initNum;
        this.manage_expenses = initNum;
        this.raw_mat_cost = initNum;
        this.components_cost = initNum;
        this.total_expenses = initNum;
        this.operate_revenue = initNum;
        this.operate_expenses = initNum;
        this.operate_income = initNum;
        this.financial_expenses = initNum;
        this.depreciation_expenses = initNum;
        this.other_expenses = initNum;
        this.corporate_tax = initNum;
        this.income_tax = initNum;
        this.tax_expenses = initNum;
        this.netIncome = initNum;
        this.year = year;
    }

    @Override
    public String toString() {
        return "Income{" +
                "num=" + num +
                ", quarter=" + quarter +
                ", sales_revenue=" + sales_revenue +
                ", interest=" + interest +
                ", rental=" + rental +
                ", investment=" + investment +
                ", licensing=" + licensing +
                ", total_revenue=" + total_revenue +
                ", sales_salary=" + sales_salary +
                ", compensation=" + compensation +
                ", advertising=" + advertising +
                ", travel_expenses=" + travel_expenses +
                ", mp_salary=" + mp_salary +
                ", office_rent=" + office_rent +
                ", accService_costs=" + accService_costs +
                ", consulting_costs=" + consulting_costs +
                ", fixtures=" + fixtures +
                ", salary=" + salary +
                ", employee_benefits=" + employee_benefits +
                ", manage_expenses=" + manage_expenses +
                ", raw_mat_cost=" + raw_mat_cost +
                ", components_cost=" + components_cost +
                ", total_expenses=" + total_expenses +
                ", operate_revenue=" + operate_revenue +
                ", operate_expenses=" + operate_expenses +
                ", operate_income=" + operate_income +
                ", financial_expenses=" + financial_expenses +
                ", depreciation_expenses=" + depreciation_expenses +
                ", other_expenses=" + other_expenses +
                ", corporate_tax=" + corporate_tax +
                ", income_tax=" + income_tax +
                ", tax_expenses=" + tax_expenses +
                ", netIncome=" + netIncome +
                ", year=" + year +
                ", transactions=" + transactions +
                ", member=" + member +
                '}';
    }
}
