package com.Erp.service;

import com.Erp.dto.FinancialDto;
import com.Erp.dto.IncomeDto;
import com.Erp.dto.SaveDto;
import com.Erp.entity.Financial;
import com.Erp.entity.Income;
import com.Erp.repository.FinancialRepository;
import com.Erp.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialService {

    private final FinancialRepository financialRepository;
    private final IncomeRepository incomeRepository;

    public Short addData(FinancialDto dto) {

        for (int i = 1; i < 5; i++) {
            Financial financial = new Financial(0L, dto.getYear(), i);

            financialRepository.save(financial);

            Income income = incomeRepository.findIncomeYearAndQuarter(dto.getYear(), i);

            if(income != null){
                financial.setIncomes(income);
                saveData(financial);
                income.setFinancial(financial);
                incomeRepository.save(income);
            }
        }

        return dto.getYear();
    }

    public void saveData(Financial financial){

        Income income = financial.getIncomes();

        long total_assets = 0L;
        long total_liabilities = 0L;
        long paid_capital = 0L;
        long total_capital = 0L;
        long totalLiabilitiesCapital = 0L;

        total_assets = financial.getCash() + financial.getCash_equivalents() + income.getRaw_mat_cost() + income.getComponents_cost() + income.getFixtures() + financial.getReal_estate() + financial.getEquipment() + financial.getVehicles() + financial.getEquity_invest() + financial.getReal_estate_invest() + financial.getCorporate_invest() + financial.getTrademarks() + financial.getLicenses() + financial.getNotes_receivable() + financial.getDeposits() + financial.getPension_assets();
        total_liabilities = financial.getBank_loans() + financial.getTrade_credit() + financial.getAdvance_payments() + financial.getTax_liabilities() + financial.getBonds() + financial.getLt_borrow_pay() + financial.getLt_deposits();
        paid_capital = total_assets - total_liabilities;
        total_capital = paid_capital + income.getNetIncome();
        totalLiabilitiesCapital = total_liabilities + total_capital;

        financial.setTotal_assets(total_assets);
        financial.setTotal_liabilities(total_liabilities);
        financial.setPaid_capital(paid_capital);
        financial.setTotal_capital(total_capital);
        financial.setTotalLiabilitiesCapital(totalLiabilitiesCapital);

        financialRepository.save(financial);
    }

    public Long updateData(SaveDto dto) throws Exception{

        Long id = dto.getNum();
        Long value = Long.valueOf(dto.getValue());

        Financial financial = financialRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        financial.setDynamicField(dto.getName(), value);

        saveData(financial);

        financialRepository.save(financial);

        return financial.getId();
    }

    public List<FinancialDto> findFinancialsList() {

        List<FinancialDto> financialDtos = new ArrayList<>();

        List<Financial> financials = financialRepository.findAll();

        for (Financial financial : financials){

            Income income = financial.getIncomes();

            Long raw_mt = 0L;
            Long product_mt = 0L;
            Long fixture_mt = 0L;
            Long netIncome = 0L;

            if(income != null){
                raw_mt = income.getRaw_mat_cost();
                product_mt = income.getComponents_cost();
                fixture_mt = income.getFixtures();
                netIncome = income.getNetIncome();
            }

            FinancialDto financialDto = new FinancialDto(financial, raw_mt, product_mt, fixture_mt, netIncome);

            financialDtos.add(financialDto);
        }

        return financialDtos;
    }

    public List<FinancialDto> findFinancialsList(Short year) {

        List<FinancialDto> financialDtos = new ArrayList<>();

        List<Financial> financials = financialRepository.findSearchList(year);

        for (Financial financial : financials){

            Income income = financial.getIncomes();

            Long raw_mt = 0L;
            Long product_mt = 0L;
            Long fixture_mt = 0L;
            Long netIncome = 0L;

            if(income != null){
                raw_mt = income.getRaw_mat_cost();
                product_mt = income.getComponents_cost();
                fixture_mt = income.getFixtures();
                netIncome = income.getNetIncome();
            }

            FinancialDto financialDto = new FinancialDto(financial, raw_mt, product_mt, fixture_mt, netIncome);

            financialDtos.add(financialDto);
        }

        return financialDtos;
    }
}
