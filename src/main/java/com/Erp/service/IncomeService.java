package com.Erp.service;

import com.Erp.constant.TransactionCategory;
import com.Erp.dto.IncomeDto;
import com.Erp.dto.SaveDto;
import com.Erp.entity.Financial;
import com.Erp.entity.Income;
import com.Erp.entity.Member;
import com.Erp.entity.Transaction;
import com.Erp.repository.FinancialRepository;
import com.Erp.repository.IncomeRepository;
import com.Erp.repository.MemberRepository;
import com.Erp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final IncomeRepository incomeRepository;
    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;
    private final FinancialService financialService;
    private final FinancialRepository financialRepository;

    public Short addData(IncomeDto dto) {

        for (int i = 1; i < 5; i++) {
            Income income = new Income(0L, dto.getYear(), i);

            List<Transaction> transactions = transactionRepository.findTransactionList(Integer.valueOf(dto.getYear()), i);

            income.setTransactions(transactions);

            for (Transaction transaction : transactions){
                transaction.setIncome(income);
            }

            List<Member> memberList = memberRepository.findAll();

            for (Member member : memberList){
                List<Income> mIncomeList = member.getIncomes();
                mIncomeList.add(income);

                income.setMember(member);

                updateTotalData(income, transactions);

                incomeRepository.save(income);

                member.setIncomes(mIncomeList);
                memberRepository.save(member);
            }

            Financial financial = financialRepository.findFinancialQuarter(dto.getYear(), i);

            if (financial != null){
                financial.setIncomes(income);

                financialService.updateTotalData(financial, income);
                financialRepository.save(financial);
            }
        }

        return dto.getYear();
    }

    public Long updateData(SaveDto dto) throws Exception {

        Long id = dto.getNum();

        Income income = incomeRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        income.setDynamicField(dto.getName(), dto.getValue().longValue());

        List<Transaction> transactions = transactionRepository.findTransactionList(Integer.valueOf(income.getYear()), income.getQuarter());

        updateTotalData(income, transactions);

        incomeRepository.save(income);

        Financial financial = financialRepository.findFinancialQuarter(income.getYear(), income.getQuarter());

        if(financial != null){
            financial.setIncomes(income);

            financialService.updateTotalData(financial, income);

            financialRepository.save(financial);
        }

        return income.getNum();
    }

    public void updateTotalData(Income income, List<Transaction> transactions){

        income.setSales_revenue(getSalesRevenue(transactions));
        income.setTotal_revenue(getTotalRevenue(income, transactions));
        income.setTotal_expenses(getTotalExpenses(income));
        income.setOperate_expenses(getTotalSalMg(income));
        income.setOperate_revenue(getSalesRevenue(transactions));
        income.setOperate_income(getTotalOpIncome(income, transactions));
        income.setOther_expenses(getTotalOtExpenses(income));
        income.setTax_expenses(getTotalTaxExpenses(income));
        income.setNetIncome(getTotal(income, transactions));
    }

    // 매출액
    public Long getSalesRevenue(List<Transaction> transactions){

        Long result = 0L;

        for (Transaction transaction : transactions){
            if(transaction.getTransactionCategory() == TransactionCategory.OUTS){
                result += transaction.getAmount();
            }
        }

        return result;
    }

    // 기타 수익
    public Long getTotalOtIncome(Income income){

        Long result = 0L;

        result = income.getInterest() + income.getRental() + income.getInvestment() + income.getLicensing();

        return result;
    }

    // 총 수익
    public Long getTotalRevenue(Income income, List<Transaction> transactions){

        Long result = 0L;

        result = getSalesRevenue(transactions) + getTotalOtIncome(income);

        return result;
    }
    

    // 총 판매비와 관리비
    public Long getTotalSalMg(Income income){

        Long result = 0L;

        result = income.getSales_salary() + income.getCompensation() + income.getAdvertising() + income.getTravel_expenses() + income.getMp_salary() + income.getOffice_rent() + income.getAccService_costs() + income.getConsulting_costs() + income.getFixtures();

        return result;
    }

    public Long getTotalExpenses(Income income){

        Long result = 0L;

        result =  getTotalSalMg(income) + income.getSalary() + income.getEmployee_benefits() + income.getManage_expenses() + income.getRaw_mat_cost() + income.getComponents_cost();

        return result;
    }

    public Long getTotalOpIncome(Income income, List<Transaction> transactions){

        Long result = 0L;

        result = getSalesRevenue(transactions) - getTotalSalMg(income);

        return result;
    }

    public Long getTotalOtExpenses(Income income){

        Long result = 0L;

        result = income.getFinancial_expenses() + income.getDepreciation_expenses();

        return result;
    }

    public Long getTotalTaxExpenses(Income income){

        Long result = 0L;

        result = income.getCorporate_tax() + income.getIncome_tax();

        return result;
    }

    public Long getTotal(Income income, List<Transaction> transactions){

        Long result = 0L;

        result = ( getTotalOpIncome(income, transactions) + getTotalOtIncome(income) ) - ( getTotalOtExpenses(income) + getTotalTaxExpenses(income) );

        return result;
    }
}
