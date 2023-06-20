package com.Erp.repository;

import com.Erp.dto.AllChartData;
import com.Erp.dto.IncomeChartData;
import com.Erp.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income,Long> {

    // 특정 연도 데이터 찾기
    @Query(" select i from Income i where i.year = :year order by i.quarter asc")
    List<Income> findSearchList(@Param("year") Short year);

    // 특정 연도, 분기 데이터 찾기
    @Query(" select i from Income i where i.year = :year and i.quarter = :quarter")
    Income findIncomeYearAndQuarter(@Param("year") Short year, @Param("quarter") Integer quarter);

    // 손익계산서 차트 데이터 불러오기
    @Query(value = " select new com.Erp.dto.IncomeChartData(sum(i.sales_revenue), sum(i.operate_revenue)," +
            " sum(i.operate_expenses)," +
            " sum(i.netIncome), i.year)" +
            " from Income i" +
            " group by i.year" +
            " order by i.year asc")
    List<IncomeChartData> findChartDataList();

    // 한눈에 보기 차트 데이터 불러오기
    @Query(value = " select new com.Erp.dto.AllChartData(sum(i.total_revenue), sum(i.netIncome)," +
            " sum(i.operate_expenses), 0L, sum(i.operate_income), sum(i.sales_revenue)," +
            " sum(f.total_assets), i.year)" +
            " from Income i" +
            " JOIN Financial f ON i.financial.id = f.id" +
            " GROUP BY i.year" +
            " ORDER BY i.year asc")
    List<AllChartData> findChartDataList2();
}
