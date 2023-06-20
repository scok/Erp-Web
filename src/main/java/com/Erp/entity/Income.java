package com.Erp.entity;

import com.Erp.dto.IncomeDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToOne
    @JoinColumn(name = "financial_id")
    private Financial financial  ;

    @JsonIgnore
    @OneToMany(mappedBy = "income", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    // 데이터 항목에 따라 동작하는 데이터 수정 메소드
    public void setDynamicField(String fieldName, Object fieldValue) throws Exception{
        String setterName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        Method setter = getClass().getMethod(setterName, fieldValue.getClass());
        setter.invoke(this, fieldValue);
    }

    public Income() {
    }

    // 초기화
    public Income(Long initNum, Short year, Integer quarter) {
        this.quarter = quarter;
        this.sales_revenue = initNum;
        this.interest = initNum;
        this.rental = initNum;
        this.investment = initNum;
        this.licensing = initNum;
        this.total_revenue = initNum;
        this.salary = initNum;
        this.bonus = initNum;
        this.plusMoney = initNum;
        this.minusMoney = initNum;
        this.totalMoney = initNum;
        this.manage_expenses = initNum;
        this.advertising = initNum;
        this.office_rent = initNum;
        this.accService_costs = initNum;
        this.consulting_costs = initNum;
        this.fixtures = initNum;
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
        this.localTax = initNum;
        this.tax_expenses = initNum;
        this.netIncome = initNum;
        this.year = year;
    }

    public Income(IncomeDto incomeDto) {
        this.id = incomeDto.getId();
        this.quarter = incomeDto.getQuarter();
        this.sales_revenue = incomeDto.getSales_revenue();
        this.interest = incomeDto.getInterest();
        this.rental = incomeDto.getRental();
        this.investment = incomeDto.getInvestment();
        this.licensing = incomeDto.getLicensing();
        this.total_revenue = incomeDto.getTotal_revenue();
        this.salary = incomeDto.getSalary();
        this.bonus = incomeDto.getBonus();
        this.plusMoney = incomeDto.getPlusMoney();
        this.minusMoney = incomeDto.getMinusMoney();
        this.totalMoney = incomeDto.getTotalMoney();
        this.manage_expenses = incomeDto.getManage_expenses();
        this.advertising = incomeDto.getAdvertising();
        this.office_rent = incomeDto.getOffice_rent();
        this.accService_costs = incomeDto.getAccService_costs();
        this.consulting_costs = incomeDto.getConsulting_costs();
        this.fixtures = incomeDto.getFixtures();
        this.raw_mat_cost = incomeDto.getRaw_mat_cost();
        this.components_cost = incomeDto.getComponents_cost();
        this.total_expenses = incomeDto.getTotal_expenses();
        this.operate_revenue = incomeDto.getOperate_revenue();
        this.operate_expenses = incomeDto.getOperate_expenses();
        this.operate_income = incomeDto.getOperate_income();
        this.financial_expenses = incomeDto.getFinancial_expenses();
        this.depreciation_expenses = incomeDto.getDepreciation_expenses();
        this.other_expenses = incomeDto.getOther_expenses();
        this.corporate_tax = incomeDto.getCorporate_tax();
        this.income_tax = incomeDto.getIncome_tax();
        this.localTax = incomeDto.getLocalTax();
        this.tax_expenses = incomeDto.getTax_expenses();
        this.netIncome = incomeDto.getNetIncome();
        this.year = incomeDto.getYear();
    }

    @Override
    public String toString() {
        return "Income{" +
                "id=" + id +
                ", quarter=" + quarter +
                ", sales_revenue=" + sales_revenue +
                ", interest=" + interest +
                ", rental=" + rental +
                ", investment=" + investment +
                ", licensing=" + licensing +
                ", total_revenue=" + total_revenue +
                ", salary=" + salary +
                ", bonus=" + bonus +
                ", plusMoney=" + plusMoney +
                ", minusMoney=" + minusMoney +
                ", totalMoney=" + totalMoney +
                ", manage_expenses=" + manage_expenses +
                ", advertising=" + advertising +
                ", office_rent=" + office_rent +
                ", accService_costs=" + accService_costs +
                ", consulting_costs=" + consulting_costs +
                ", fixtures=" + fixtures +
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
                ", localTax=" + localTax +
                ", tax_expenses=" + tax_expenses +
                ", netIncome=" + netIncome +
                ", year=" + year +
                ", financial=" + financial +
                ", transactions=" + transactions +
                '}';
    }
}
