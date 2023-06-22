package com.Erp.service;

import com.Erp.constant.ProductDivisionCategory;
import com.Erp.dto.FinancialDto;
import com.Erp.dto.SaveDto;
import com.Erp.entity.Financial;
import com.Erp.entity.Income;
import com.Erp.entity.logistics.Inventory;
import com.Erp.entity.logistics.Product;
import com.Erp.repository.FinancialRepository;
import com.Erp.repository.IncomeRepository;
import com.Erp.repository.logistics.InventoryRepository;
import com.Erp.repository.logistics.ProductRepository;
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
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    // 데이터 추가
    public Short addData(FinancialDto dto) {

        List<Financial> financials = financialRepository.findSearchList(dto.getYear());

        // 기존의 재무 상태표 없을 시
        if(financials.size() == 0){
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
        }else {
            return  -1;
        }
    }

    // 총합 계산 후 저장
    public void saveData(Financial financial){

        Income income = financial.getIncomes();
        List<Inventory> inventory = inventoryRepository.getInventoryYear(Integer.valueOf(financial.getYear()), financial.getQuarter());

        long raw_mat_inven = 0L;
        long product_inven = 0L;

        // 항목 수정 후 총합 다시 계산
        if(inventory.size() != 0){
            for (Inventory bean : inventory){

                String id = bean.getProduct().getPrCode();

                Product product = productRepository.getReferenceById(id);

                if(product.getPrDivCategory() == ProductDivisionCategory.자재){
                    raw_mat_inven += (bean.getInQuantity() * product.getPrPrice());
                } else if (product.getPrDivCategory() == ProductDivisionCategory.제품) {
                    product_inven += (bean.getInQuantity() * product.getPrPrice());
                }
            }
        }

        long total_assets = 0L;
        long total_liabilities = 0L;
        long paid_capital = 0L;
        long total_capital = 0L;
        long totalLiabilitiesCapital = 0L;

        total_assets = financial.getCash() + financial.getCash_equivalents() + raw_mat_inven + product_inven + income.getFixtures() + financial.getReal_estate() + financial.getEquipment() + financial.getVehicles() + financial.getEquity_invest() + financial.getReal_estate_invest() + financial.getCorporate_invest() + financial.getTrademarks() + financial.getLicenses() + financial.getNotes_receivable() + financial.getDeposits() + financial.getPension_assets();
        total_liabilities = financial.getBank_loans() + financial.getTrade_credit() + financial.getAdvance_payments() + financial.getTax_liabilities() + financial.getBonds() + financial.getLt_borrow_pay() + financial.getLt_deposits();
        paid_capital = total_assets - total_liabilities;
        total_capital = paid_capital + income.getNetIncome();
        totalLiabilitiesCapital = total_liabilities + total_capital;

        financial.setRaw_mt_inven(raw_mat_inven);
        financial.setProduct_inven(product_inven);
        financial.setTotal_assets(total_assets);
        financial.setTotal_liabilities(total_liabilities);
        financial.setPaid_capital(paid_capital);
        financial.setTotal_capital(total_capital);
        financial.setTotalLiabilitiesCapital(totalLiabilitiesCapital);

        financialRepository.save(financial);
    }

    // 데이터 수정
    public Long updateData(SaveDto dto) throws Exception{

        Long id = dto.getNum();
        Long value = Long.valueOf(dto.getValue());

        Financial financial = financialRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        financial.setDynamicField(dto.getName(), value);

        saveData(financial);

        return financial.getId();
    }

    // 화면 데이터 불러오기
    public List<FinancialDto> findFinancialsList() {

        List<FinancialDto> financialDtos = new ArrayList<>();

        List<Financial> financials = financialRepository.findAll();

        for (Financial financial : financials){

            saveData(financial);

            Income income = financial.getIncomes();

            Long fixture_mt = 0L;
            Long netIncome = 0L;

            if(income != null){
                fixture_mt = income.getFixtures();
                netIncome = income.getNetIncome();
            }

            FinancialDto financialDto = new FinancialDto(financial, fixture_mt, netIncome);

            financialDtos.add(financialDto);
        }

        return financialDtos;
    }

    // 화면 데이터(연도) 불러오기
    public List<FinancialDto> findFinancialsList(Short year) {

        List<FinancialDto> financialDtos = new ArrayList<>();

        List<Financial> financials = financialRepository.findSearchList(year);

        for (Financial financial : financials){

            saveData(financial);

            Income income = financial.getIncomes();

            Long fixture_mt = 0L;
            Long netIncome = 0L;

            if(income != null){
                fixture_mt = income.getFixtures();
                netIncome = income.getNetIncome();
            }

            FinancialDto financialDto = new FinancialDto(financial, fixture_mt, netIncome);

            financialDtos.add(financialDto);
        }

        return financialDtos;
    }
}
