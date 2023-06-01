package com.Erp.service;

import com.Erp.dto.FinancialDto;
import com.Erp.dto.SaveDto;
import com.Erp.entity.Financial;
import com.Erp.entity.Income;
import com.Erp.repository.FinancialRepository;
import com.Erp.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class FinancialService {

    private final FinancialRepository financialRepository;
    private final IncomeRepository incomeRepository;

    public Short addData(FinancialDto dto) {

        for (int i = 1; i < 5; i++) {
            Financial financial = new Financial(0L, dto.getYear(), i);

            Income income = incomeRepository.findIncomeYearAndQuarter(dto.getYear(), i);

            financial.setIncomes(income);

            updateTotalData(financial, income);

            financialRepository.save(financial);
        }

        return dto.getYear();
    }

    public Long updateData(SaveDto dto) throws Exception{

        Long id = Long.valueOf(dto.getNum());
        Long value = Long.valueOf(dto.getValue());

        Financial financial = financialRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        financial.setDynamicField(dto.getName(), value);

        Income income = incomeRepository.findIncomeYearAndQuarter(financial.getYear(), financial.getQuarter());

        updateTotalData(financial, income);

        financialRepository.save(financial);

        return financial.getNum();
    }

    public void updateTotalData(Financial financial, Income income){

        financial.setFixture_mt(income.getFixtures());
        financial.setTotal_assets(getTotalAssets(financial, income));
        financial.setTotal_liabilities(getliabilities(financial));
        financial.setPaid_capital(getPaidCapital(financial, income));
        financial.setTotal_capital(getTotalCapital(financial, income));
        financial.setTotalLiabilitiesCapital(getTotalLiabilitiesCapital(financial, income));
    }

    public Long getCurrentAssets(Financial financial, Income income){

        Long result = 0L;

        result = financial.getCash() + financial.getCash_equivalents() + financial.getRaw_mt() + financial.getProduct_mt() + income.getFixtures();

        return result;
    }

    public Long getNonCurrentAssets(Financial financial){

        Long result = 0L;

        result = financial.getReal_estate() + financial.getEquipment() + financial.getVehicles() + financial.getEquity_invest() + financial.getReal_estate_invest() + financial.getCorporate_invest() + financial.getTrademarks() + financial.getLicenses() + financial.getNotes_receivable() + financial.getDeposits() + financial.getPension_assets();

        return result;
    }

    public Long getTotalAssets(Financial financial, Income income){

        Long result = 0L;

        result = getCurrentAssets(financial, income) + getNonCurrentAssets(financial);

        return result;
    }

    public Long getliabilities(Financial financial){

        Long result = 0L;

        result = financial.getBank_loans() + financial.getTrade_credit() + financial.getAdvance_payments() + financial.getTax_liabilities() + financial.getBonds() + financial.getLt_borrow_pay() + financial.getLt_deposits();

        return result;
    }

    public Long getPaidCapital(Financial financial, Income income){

        Long result = 0L;

        result = getTotalAssets(financial, income) - getliabilities(financial);

        return result;
    }

    public Long getTotalCapital(Financial financial, Income income){

        Long result = 0L;

        result = getPaidCapital(financial, income) + income.getNetIncome();

        return result;
    }

    public Long getTotalLiabilitiesCapital(Financial financial, Income income){

        Long result = 0L;

        result = getliabilities(financial) + getTotalCapital(financial, income);

        return result;
    }
}
