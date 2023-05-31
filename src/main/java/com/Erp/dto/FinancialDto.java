package com.Erp.dto;

import com.Erp.entity.Financial;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class FinancialDto {

    private Long num ; // 재무 번호
    private Integer quarter; //분기

    // 자산
    private Long cash; // 현금
    private Long cash_equivalents; // 현금성 예금
    private Long raw_mt; // 원자재 재고
    private Long product_mt; // 제품 재고
    private Long fixture_mt; // 비품 재고
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
    private Long netIncome; // 당기 순이익
    private Long total_capital; // 총 자본

    private Long totalLiabilitiesCapital; // 총 부채 및 자본

    // 연도
    private Short year;

    public FinancialDto() {
    }

    public FinancialDto(Financial financial, Long fixture_mt, Long netIncome) {
        this.num = financial.getNum();
        this.quarter = financial.getQuarter();
        this.cash = financial.getCash();
        this.cash_equivalents = financial.getCash_equivalents();
        this.raw_mt = financial.getRaw_mt();
        this.product_mt = financial.getProduct_mt();
        this.fixture_mt = fixture_mt;
        this.real_estate = financial.getReal_estate();
        this.equipment = financial.getEquipment();
        this.vehicles = financial.getVehicles();
        this.equity_invest = financial.getEquity_invest();
        this.real_estate_invest = financial.getReal_estate_invest();
        this.corporate_invest = financial.getCorporate_invest();
        this.trademarks = financial.getTrademarks();
        this.licenses = financial.getLicenses();
        this.notes_receivable = financial.getNotes_receivable();
        this.deposits = financial.getDeposits();
        this.pension_assets = financial.getPension_assets();
        this.total_assets = financial.getTotal_assets();
        this.bank_loans = financial.getBank_loans();
        this.trade_credit = financial.getTrade_credit();
        this.advance_payments = financial.getAdvance_payments();
        this.tax_liabilities = financial.getTax_liabilities();
        this.bonds = financial.getBonds();
        this.lt_borrow_pay = financial.getLt_borrow_pay();
        this.lt_deposits = financial.getLt_deposits();
        this.total_liabilities = financial.getTotal_liabilities();
        this.paid_capital = financial.getPaid_capital();
        this.netIncome = netIncome;
        this.total_capital = financial.getTotal_capital();
        this.totalLiabilitiesCapital = financial.getTotalLiabilitiesCapital();
        this.year = financial.getYear();
    }
}
