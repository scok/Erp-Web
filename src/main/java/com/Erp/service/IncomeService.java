package com.Erp.service;

import com.Erp.constant.ProductDivisionCategory;
import com.Erp.constant.TransactionCategory;
import com.Erp.dto.FinancialDto;
import com.Erp.dto.IncomeDto;
import com.Erp.dto.SaveDto;
import com.Erp.entity.*;
import com.Erp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final IncomeRepository incomeRepository;
    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;
    private final FinancialRepository financialRepository;
    private final MemberPayRepository memberPayRepository;

    public Short addData(IncomeDto dto) {

        List<Income> incomes = incomeRepository.findSearchList(dto.getYear());

        if(incomes.size() == 0){
            for (int i = 1; i < 5; i++) {
                Income income = new Income(0L, dto.getYear(), i);

                Integer year = Integer.valueOf(dto.getYear());
                int startMonth = (i - 1) * 3 + 1;
                Integer endMonth  = startMonth + 2;

                List<Member> members = memberRepository.findMemberYear(year, startMonth, endMonth);

                saveData(income, members);

                Financial financial = financialRepository.findFinancialQuarter(dto.getYear(), i);

                if(financial != null){
                    income.setFinancial(financial);
                    incomeRepository.save(income);
                    financial.setIncomes(income);

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
            }

            return dto.getYear();
        }else{
            return -1;
        }
    }

    public void saveData(Income income, List<Member> members) {

        List<Transaction> transactions = transactionRepository.findTransactionList(Integer.valueOf(income.getYear()), income.getQuarter());

        Long sales_revenue = 0L;
        Long salary = 0L;
        Long bonus = 0L;
        Long plusMoney = 0L;
        Long minusMoney = 0L;
        Long totalMoney = 0L;
        long manage_expenses = 0L;
        Long income_tax = 0L;
        Long localTax = 0L;
        Long raw_mat_cost = 0L;
        Long components_cost = 0L;

        for (Transaction transaction : transactions){
            if(transaction.getTransactionCategory() == TransactionCategory.OUTS){
                sales_revenue += transaction.getAmount();
            }else if(transaction.getTransactionCategory() == TransactionCategory.INS){
                Product product = transaction.getWarehousingInAndOut().getOrderSheetDetail().getProduct();

                if (product != null){
                    if(product.getPrDivCategory() == ProductDivisionCategory.자재){
                        raw_mat_cost += transaction.getAmount();
                    } else if (product.getPrDivCategory() == ProductDivisionCategory.제품) {
                        components_cost += transaction.getAmount();
                    }
                }
            }
        }

        for (Member member : members){
            MemberPay memberPay = memberPayRepository.findByMemberId(member.getId());

            if(memberPay != null){
                salary += memberPay.getSalary();
                bonus += memberPay.getBonus();
                plusMoney += memberPay.getPlusMoney();
                minusMoney += memberPay.getMinusMoney();
                totalMoney += memberPay.getTotalMoney();
                manage_expenses += (memberPay.getNightPay() + memberPay.getFoodPay() + memberPay.getCarPay() + memberPay.getKukInsurance() + memberPay.getGoInsurance() + memberPay.getSanInsurance() + memberPay.getGunInsurance());
                income_tax += memberPay.getIncomeTax();
                localTax += memberPay.getLocalTax();
            }
        }

        income.setSales_revenue(sales_revenue);
        income.setSalary(salary);
        income.setBonus(bonus);
        income.setPlusMoney(plusMoney);
        income.setMinusMoney(minusMoney);
        income.setTotalMoney(totalMoney);
        income.setManage_expenses(manage_expenses);
        income.setRaw_mat_cost(raw_mat_cost);
        income.setComponents_cost(components_cost);
        income.setIncome_tax(income_tax);
        income.setLocalTax(localTax);

        long total_revenue = 0L;
        long total_expenses = 0L;
        long operate_revenue = 0L;
        long operate_expenses = 0L;
        long operate_income = 0L;
        long other_expenses = 0L;
        long netIncome = 0L;
        long corporate_tax = 0L;
        long tax_expenses = 0L;

        total_revenue = income.getSales_revenue() + income.getInterest() + income.getRental() + income.getInvestment() + income.getLicensing();
        total_expenses = income.getSalary() + income.getBonus() + income.getPlusMoney() + income.getMinusMoney() + income.getTotalMoney() + income.getManage_expenses() + income.getAdvertising() + income.getOffice_rent() + income.getAccService_costs() + income.getConsulting_costs() + income.getFixtures() + income.getRaw_mat_cost() + income.getComponents_cost();
        operate_revenue = total_revenue;
        operate_expenses = total_expenses - income.getRaw_mat_cost() - income.getComponents_cost();
        operate_income = operate_revenue - operate_expenses;
        other_expenses = income.getFinancial_expenses() + income.getDepreciation_expenses();
        netIncome = total_revenue - total_expenses - other_expenses;
        corporate_tax = (long)(netIncome * 0.22);
        tax_expenses = corporate_tax + income.getIncome_tax() + income.getLocalTax();

        income.setTotal_revenue(total_revenue);
        income.setTotal_expenses(total_expenses);
        income.setOperate_revenue(operate_revenue);
        income.setOperate_expenses(operate_expenses);
        income.setOperate_income(operate_income);
        income.setOther_expenses(other_expenses);
        income.setNetIncome(netIncome);
        income.setCorporate_tax(corporate_tax);
        income.setTax_expenses(tax_expenses);

        incomeRepository.save(income);
    }

    public void saveData2(Income income) {

        List<Transaction> transactions = income.getTransactions();

        Long sales_revenue = 0L;
        Long salary = 0L;
        Long bonus = 0L;
        Long plusMoney = 0L;
        Long minusMoney = 0L;
        Long totalMoney = 0L;
        long manage_expenses = 0L;
        Long income_tax = 0L;
        Long localTax = 0L;
        Long raw_mat_cost = 0L;
        Long components_cost = 0L;

        for (Transaction transaction : transactions){
            if(transaction.getTransactionCategory() == TransactionCategory.OUTS){
                sales_revenue += transaction.getAmount();
            }else if(transaction.getTransactionCategory() == TransactionCategory.INS){
                Product product = transaction.getWarehousingInAndOut().getOrderSheetDetail().getProduct();

                if (product != null){
                    if(product.getPrDivCategory() == ProductDivisionCategory.자재){
                        raw_mat_cost += transaction.getAmount();
                    } else if (product.getPrDivCategory() == ProductDivisionCategory.제품) {
                        components_cost += transaction.getAmount();
                    }
                }
            }
        }

        Integer year = Integer.valueOf(income.getYear());
        int startMonth = (income.getQuarter() - 1) * 3 + 1;
        Integer endMonth  = startMonth + 2;

        List<Member> members = memberRepository.findMemberYear(year, startMonth, endMonth);

        for (Member member : members){
            MemberPay memberPay = memberPayRepository.findByMemberId(member.getId());

            if(memberPay != null){
                salary += memberPay.getSalary();
                bonus += memberPay.getBonus();
                plusMoney += memberPay.getPlusMoney();
                minusMoney += memberPay.getMinusMoney();
                totalMoney += memberPay.getTotalMoney();
                manage_expenses += (memberPay.getNightPay() + memberPay.getFoodPay() + memberPay.getCarPay() + memberPay.getKukInsurance() + memberPay.getGoInsurance() + memberPay.getSanInsurance() + memberPay.getGunInsurance());
                income_tax += memberPay.getIncomeTax();
                localTax += memberPay.getLocalTax();
            }
        }

        income.setSales_revenue(sales_revenue);
        income.setSalary(salary);
        income.setBonus(bonus);
        income.setPlusMoney(plusMoney);
        income.setMinusMoney(minusMoney);
        income.setTotalMoney(totalMoney);
        income.setManage_expenses(manage_expenses);
        income.setRaw_mat_cost(raw_mat_cost);
        income.setComponents_cost(components_cost);
        income.setIncome_tax(income_tax);
        income.setLocalTax(localTax);

        long total_revenue = 0L;
        long total_expenses = 0L;
        long operate_revenue = 0L;
        long operate_expenses = 0L;
        long operate_income = 0L;
        long other_expenses = 0L;
        long netIncome = 0L;
        long corporate_tax = 0L;
        long tax_expenses = 0L;

        total_revenue = income.getSales_revenue() + income.getInterest() + income.getRental() + income.getInvestment() + income.getLicensing();
        total_expenses = income.getSalary() + income.getBonus() + income.getPlusMoney() + income.getMinusMoney() + income.getTotalMoney() + income.getManage_expenses() + income.getAdvertising() + income.getOffice_rent() + income.getAccService_costs() + income.getConsulting_costs() + income.getFixtures() + income.getRaw_mat_cost() + income.getComponents_cost();
        operate_revenue = total_revenue;
        operate_expenses = total_expenses - income.getRaw_mat_cost() - income.getComponents_cost();
        operate_income = operate_revenue - operate_expenses;
        other_expenses = income.getFinancial_expenses() + income.getDepreciation_expenses();
        netIncome = total_revenue - total_expenses - other_expenses;
        corporate_tax = (long)(netIncome * 0.22);
        tax_expenses = corporate_tax + income.getIncome_tax() + income.getLocalTax();

        income.setTotal_revenue(total_revenue);
        income.setTotal_expenses(total_expenses);
        income.setOperate_revenue(operate_revenue);
        income.setOperate_expenses(operate_expenses);
        income.setOperate_income(operate_income);
        income.setOther_expenses(other_expenses);
        income.setNetIncome(netIncome);
        income.setCorporate_tax(corporate_tax);
        income.setTax_expenses(tax_expenses);

        incomeRepository.save(income);
    }

    public List<IncomeDto> findIncomesList(){

        List<IncomeDto> incomeDtos = new ArrayList<>();

        List<Income> incomes = incomeRepository.findAll();

        for (Income income : incomes){

            Long sales_revenue = income.getSales_revenue();
            Long salary = income.getSalary();
            Long bonus = income.getBonus();
            Long plusMoney = income.getPlusMoney();
            Long minusMoney = income.getMinusMoney();
            Long totalMoney = income.getTotalMoney();
            long manage_expenses = income.getManage_expenses();
            Long income_tax = income.getIncome_tax();
            Long localTax = income.getLocalTax();
            Long raw_mat_cost = income.getRaw_mat_cost();
            Long components_cost = income.getComponents_cost();

            IncomeDto incomeDto = new IncomeDto(income, sales_revenue, salary, bonus, plusMoney, minusMoney, totalMoney, manage_expenses, income_tax, localTax, raw_mat_cost, components_cost);

            incomeDtos.add(incomeDto);
        }

        return incomeDtos;
    }

    public List<IncomeDto> findIncomesList(Short year){

        List<IncomeDto> incomeDtos = new ArrayList<>();

        List<Income> incomes = incomeRepository.findSearchList(year);

        for (Income income : incomes){

            Long sales_revenue = income.getSales_revenue();
            Long salary = income.getSalary();
            Long bonus = income.getBonus();
            Long plusMoney = income.getPlusMoney();
            Long minusMoney = income.getMinusMoney();
            Long totalMoney = income.getTotalMoney();
            long manage_expenses = income.getManage_expenses();
            Long income_tax = income.getIncome_tax();
            Long localTax = income.getLocalTax();
            Long raw_mat_cost = income.getRaw_mat_cost();
            Long components_cost = income.getComponents_cost();

            IncomeDto incomeDto = new IncomeDto(income, sales_revenue, salary, bonus, plusMoney, minusMoney, totalMoney, manage_expenses, income_tax, localTax, raw_mat_cost, components_cost);

            incomeDtos.add(incomeDto);
        }

        return incomeDtos;
    }


    public Long updateData(SaveDto dto) throws Exception {

        Long id = dto.getNum();

        Income income = incomeRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        income.setDynamicField(dto.getName(), dto.getValue().longValue());

        Integer year = Integer.valueOf(income.getYear());
        int startMonth = (income.getQuarter() - 1) * 3 + 1;
        Integer endMonth  = startMonth + 2;

        List<Member> members = memberRepository.findMemberYear(year, startMonth, endMonth);

        saveData(income, members);

        incomeRepository.save(income);

        return income.getId();
    }
}
