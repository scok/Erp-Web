package com.Erp.repository;

import com.Erp.entity.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EstimateRepository extends JpaRepository<Estimate, String> {

    @Query(" select e from Estimate e where acCategory ='구매' and pageYandN = 'Y'")
    List<Estimate> findBuyEstimate();

    @Query("SELECT COUNT(e) FROM Estimate e WHERE YEAR(e.regDate) = :year AND MONTH(e.regDate) = :month AND DAY(e.regDate) = :day")
    int getEstimateCount(@Param("year") int year, @Param("month") int month, @Param("day") int day);

    @Query(" select e from Estimate e where acCategory ='판매' and pageYandN = 'Y'")
    List<Estimate> findSellerEstimate();
}
