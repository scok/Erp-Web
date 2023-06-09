package com.Erp.repository;

import com.Erp.constant.DivisionStatus;
import com.Erp.entity.OrderSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderSheetRepository extends JpaRepository<OrderSheet, String> {

    @Query(" select o from OrderSheet o where acCategory ='구매' and pageYandN = 'Y' and divisionStatus = '주문대기'")
    List<OrderSheet> findBuyOrderSheets();

    @Query("SELECT COUNT(o) FROM OrderSheet o WHERE YEAR(o.regDate) = :year AND MONTH(o.regDate) = :month AND DAY(o.regDate) = :day")
    int getOrderSheetsCount(@Param("year") int year, @Param("month") int month, @Param("day") int day);

    @Query(" select o from OrderSheet o where acCategory ='판매' and pageYandN = 'Y' and divisionStatus = '주문대기'")
    List<OrderSheet> findSellerOrderSheets();

    @Query(" select o from OrderSheet o where acCategory ='구매' and pageYandN = 'Y' and divisionStatus = '입하대기'")
    List<OrderSheet> findBuyOrderSheetsState();

    @Query(" select o from OrderSheet o where acCategory ='판매' and pageYandN = 'Y' and divisionStatus = '출하대기'")
    List<OrderSheet> findSellerOrderSheetsState();

    @Query(" select o from OrderSheet o where o.pageYandN = 'Y' and o.divisionStatus = :filter")
    List<OrderSheet> findBuyOrderSheetFilter(@Param("filter") DivisionStatus filter);
}
