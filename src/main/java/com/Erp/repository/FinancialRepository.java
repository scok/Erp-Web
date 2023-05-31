package com.Erp.repository;

import com.Erp.dto.FinancialDto;
import com.Erp.entity.Financial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancialRepository extends JpaRepository<Financial, Long> {

    @Query(value = " select new com.Erp.dto.FinancialDto(f, i.fixtures, i.netIncome)" +
            " from Financial f" +
            " LEFT JOIN Income i ON f.incomes.num = i.num" +
            " order by f.year desc, f.quarter asc")
    List<FinancialDto> findFinancialList();

    @Query(value = " select new com.Erp.dto.FinancialDto(f, i.fixtures, i.netIncome)" +
            " from Financial f" +
            " LEFT JOIN Income i ON f.incomes.num = i.num" +
            " where f.year = :year" +
            " order by f.year desc, f.quarter asc")
    List<FinancialDto> findSearchList(@Param("year") Short year);

//    @Query(value = " select new com.Erp.dto.IncomeChartData(i.sales_revenue, i.operate_revenue, i.operate_expenses, " +
//            "i.netIncome, i.year)" +
//            " from Income i" +
//            " order by i.year desc, i.quarter asc")
//    List<IncomeChartData> findChartDataList();

    @Query(" select f from Financial f where f.year = :year and f.quarter = :quarter")
    Financial findFinancialQuarter(@Param("year") Short year, @Param("quarter") Integer quarter);
}
