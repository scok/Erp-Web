package com.Erp.repository;

import com.Erp.dto.AllChartData;
import com.Erp.dto.IncomeChartData;
import com.Erp.dto.IncomeDto;
import com.Erp.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income,Long> {

    @Query(" select i from Income i where i.year = :year order by i.quarter asc")
    List<Income> findSearchList(@Param("year") Short year);

    @Query(value = " select new com.Erp.dto.IncomeChartData(sum(i.sales_revenue), sum(i.operate_revenue)," +
            " sum(i.operate_expenses)," +
            " sum(i.netIncome), i.year)" +
            " from Income i" +
            " group by i.year" +
            " order by i.year asc")
    List<IncomeChartData> findChartDataList();

    @Query(value = " select new com.Erp.dto.AllChartData(sum(i.total_revenue), sum(i.netIncome)," +
            " sum(i.operate_expenses), count(distinct(m.id)), sum(i.operate_income), sum(i.sales_revenue)," +
            " sum(f.total_assets), i.year)" +
            " from Income i, Member m" +
            " JOIN Financial f ON i.financial.id = f.id" +
            " group by i.year" +
            " order by i.year asc")
    List<AllChartData> findChartDataList2();

    @Query(" select i from Income i where i.year = :year and i.quarter = :quarter")
    Income findIncomeYearAndQuarter(@Param("year") Short year, @Param("quarter") Integer quarter);
}
