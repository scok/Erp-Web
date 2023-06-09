package com.Erp.repository;

import com.Erp.dto.FinancialChartData;
import com.Erp.dto.FinancialDto;
import com.Erp.entity.Financial;
import com.Erp.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancialRepository extends JpaRepository<Financial, Long> {

    @Query(" select f from Financial f where f.year = :year order by f.quarter asc")
    List<Financial> findSearchList(@Param("year") Short year);

//    @Query(value = " select new com.Erp.dto.IncomeChartData(i.sales_revenue, i.operate_revenue, i.operate_expenses, " +
//            "i.netIncome, i.year)" +
//            " from Income i" +
//            " order by i.year desc, i.quarter asc")
//    List<IncomeChartData> findChartDataList();

    @Query(" select f from Financial f where f.year = :year and f.quarter = :quarter")
    Financial findFinancialQuarter(@Param("year") Short year, @Param("quarter") Integer quarter);

//    @Query(value = " select new com.Erp.dto.FinancialChartData(sum(f.total_assets), sum((f.cash + f.cash_equivalents + f.raw_mt + f.product_mt + f.fixture_mt)), sum((f.real_estate + f.equipment + f.vehicles)), sum(f.paid_capital), sum(f.total_liabilities), sum(f.total_capital), f.year)" +
//            " from Financial f" +
//            " group by f.year" +
//            " order by f.year asc")
//    List<FinancialChartData> findChartDataList();
}
