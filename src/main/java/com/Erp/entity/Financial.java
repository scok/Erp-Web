package com.Erp.entity;

import com.Erp.dto.FinancialDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.lang.reflect.Method;

@Entity
@Table(name = "financials")
@Getter @Setter
public class Financial {
    @Id
    @Column(name = "financial_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ; // 재무 번호
    private Integer quarter; //분기

    // 자산
    private Long cash; // 현금
    private Long cash_equivalents; // 현금성 예금
    private Long raw_mt_inven; // 원자재 재고
    private Long product_inven; // 제품 재고
    private Long real_estate; // 부동산
    private Long equipment; // 장비
    private Long vehicles; // 차량
    private Long equity_invest; // 주식 투자
    private Long real_estate_invest; // 부동산 투자
    private Long corporate_invest; // 기업 투자
    private Long trademarks; // 상표권
    private Long licenses; // 라이선스
    private Long notes_receivable; // 미수금
    private Long deposits; // 예치금
    private Long pension_assets; // 연금 자산
    private Long total_assets; // 총 자산

    // 부채
    private Long bank_loans; // 은행 대출
    private Long trade_credit; // 단기 대금 차입
    private Long advance_payments; // 선수금
    private Long tax_liabilities; // 세금 부채
    private Long bonds; // 사채
    private Long lt_borrow_pay; // 장기차입금 부채
    private Long lt_deposits; // 장기 예치금
    private Long total_liabilities; // 총 부채

    // 자본
    private Long paid_capital; // 자본금
    private Long total_capital; // 총 자본

    private Long totalLiabilitiesCapital; // 총 부채 및 자본

    // 연도
    private Short year;

    // 손익 계산서
    // 원자재, 제품, 비품
    @OneToOne
    @JoinColumn(name = "income_id")
    private Income incomes  ;

    public void setDynamicField(String fieldName, Object fieldValue) throws Exception{
        String setterName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        Method setter = getClass().getMethod(setterName, fieldValue.getClass());
        setter.invoke(this, fieldValue);
    }

    public Financial() {
    }

    public Financial(Long number, Short year, Integer quarter) {
        this.quarter = quarter;
        this.cash = number;
        this.cash_equivalents = number;
        this.raw_mt_inven = number;
        this.product_inven = number;
        this.real_estate = number;
        this.equipment = number;
        this.vehicles = number;
        this.equity_invest = number;
        this.real_estate_invest = number;
        this.corporate_invest = number;
        this.trademarks = number;
        this.licenses = number;
        this.notes_receivable = number;
        this.deposits = number;
        this.pension_assets = number;
        this.total_assets = number;
        this.bank_loans = number;
        this.trade_credit = number;
        this.advance_payments = number;
        this.tax_liabilities = number;
        this.bonds = number;
        this.lt_borrow_pay = number;
        this.lt_deposits = number;
        this.total_liabilities = number;
        this.paid_capital = number;
        this.total_capital = number;
        this.totalLiabilitiesCapital = number;
        this.year = year;
    }

    public Financial(FinancialDto financialDto) {
        this.id = financialDto.getId();
        this.quarter = financialDto.getQuarter();
        this.cash = financialDto.getCash();
        this.cash_equivalents = financialDto.getCash_equivalents();
        this.raw_mt_inven = financialDto.getRaw_mt();
        this.product_inven = financialDto.getProduct_mt();
        this.real_estate = financialDto.getReal_estate();
        this.equipment = financialDto.getEquipment();
        this.vehicles = financialDto.getVehicles();
        this.equity_invest = financialDto.getEquity_invest();
        this.real_estate_invest = financialDto.getReal_estate_invest();
        this.corporate_invest = financialDto.getCorporate_invest();
        this.trademarks = financialDto.getTrademarks();
        this.licenses = financialDto.getLicenses();
        this.notes_receivable = financialDto.getNotes_receivable();
        this.deposits = financialDto.getDeposits();
        this.pension_assets = financialDto.getPension_assets();
        this.total_assets = financialDto.getTotal_assets();
        this.bank_loans = financialDto.getBank_loans();
        this.trade_credit = financialDto.getTrade_credit();
        this.advance_payments = financialDto.getAdvance_payments();
        this.tax_liabilities = financialDto.getTax_liabilities();
        this.bonds = financialDto.getBonds();
        this.lt_borrow_pay = financialDto.getLt_borrow_pay();
        this.lt_deposits = financialDto.getLt_deposits();
        this.total_liabilities = financialDto.getTotal_liabilities();
        this.paid_capital = financialDto.getPaid_capital();
        this.total_capital = financialDto.getTotal_capital();
        this.totalLiabilitiesCapital = financialDto.getTotalLiabilitiesCapital();
        this.year = financialDto.getYear();
    }

    @Override
    public String toString() {
        return "Financial{" +
                "id=" + id +
                ", quarter=" + quarter +
                ", cash=" + cash +
                ", cash_equivalents=" + cash_equivalents +
                ", raw_mt_inven=" + raw_mt_inven +
                ", product_inven=" + product_inven +
                ", real_estate=" + real_estate +
                ", equipment=" + equipment +
                ", vehicles=" + vehicles +
                ", equity_invest=" + equity_invest +
                ", real_estate_invest=" + real_estate_invest +
                ", corporate_invest=" + corporate_invest +
                ", trademarks=" + trademarks +
                ", licenses=" + licenses +
                ", notes_receivable=" + notes_receivable +
                ", deposits=" + deposits +
                ", pension_assets=" + pension_assets +
                ", total_assets=" + total_assets +
                ", bank_loans=" + bank_loans +
                ", trade_credit=" + trade_credit +
                ", advance_payments=" + advance_payments +
                ", tax_liabilities=" + tax_liabilities +
                ", bonds=" + bonds +
                ", lt_borrow_pay=" + lt_borrow_pay +
                ", lt_deposits=" + lt_deposits +
                ", total_liabilities=" + total_liabilities +
                ", paid_capital=" + paid_capital +
                ", total_capital=" + total_capital +
                ", totalLiabilitiesCapital=" + totalLiabilitiesCapital +
                ", year=" + year +
                ", incomes=" + incomes +
                '}';
    }
}
