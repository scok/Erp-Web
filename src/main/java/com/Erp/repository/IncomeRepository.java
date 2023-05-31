package com.Erp.repository;

import com.Erp.dto.IncomeChartData;
import com.Erp.dto.IncomeDto;
import com.Erp.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income,Long> {

    @Query(value = "SELECT NEW com.Erp.dto.IncomeDto(i," +
            " SUM(CASE WHEN t.transactionCategory = 'OUTS' THEN t.amount ELSE 0 END)," +
            " SUM(CASE WHEN m.department = '판매' THEN m.salary ELSE 0 END)," +
            " SUM(CASE WHEN m.department = '판매' THEN m.compensation ELSE 0 END)," +
            " SUM(CASE WHEN m.department = '관리' THEN m.salary ELSE 0 END)," +
            " SUM(m.salary))" +
            " FROM Income i" +
            " LEFT JOIN Transaction t ON i.num = t.income.num" +
            " LEFT JOIN Member m ON i.member.id = m.id" +
            " GROUP BY i.id" +
            " ORDER BY i.year DESC, i.quarter ASC")
    List<IncomeDto> findIncomesList();

    @Query(value = "SELECT NEW com.Erp.dto.IncomeDto(i," +
            " SUM(CASE WHEN t.transactionCategory = 'OUTS' THEN t.amount ELSE 0 END)," +
            " SUM(CASE WHEN m.department = '판매' THEN m.salary ELSE 0 END)," +
            " SUM(CASE WHEN m.department = '판매' THEN m.compensation ELSE 0 END)," +
            " SUM(CASE WHEN m.department = '관리' THEN m.salary ELSE 0 END)," +
            " SUM(m.salary))" +
            " FROM Income i" +
            " LEFT JOIN Transaction t ON i.num = t.income.num" +
            " LEFT JOIN Member m ON i.member.id = m.id" +
            " WHERE i.year = :year" +
            " GROUP BY i.id" +
            " ORDER BY i.year DESC, i.quarter ASC")
    List<IncomeDto> findSearchList(@Param("year") Short year);

    @Query(value = " select new com.Erp.dto.IncomeChartData(sum(i.sales_revenue), sum(i.operate_revenue)," +
            " sum(i.operate_expenses)," +
            " sum(i.netIncome), i.year)" +
            " from Income i" +
            " group by i.year" +
            " order by i.year asc")
    List<IncomeChartData> findChartDataList();

    @Query(" select i from Income i where i.year = :year and i.quarter = :quarter")
    Income findIncomeYearAndQuarter(@Param("year") Short year, @Param("quarter") Integer quarter);
}
