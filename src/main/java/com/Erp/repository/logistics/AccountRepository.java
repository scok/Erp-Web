package com.Erp.repository.logistics;

import com.Erp.entity.logistics.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,String>{


    //거래 구분의 구매이며 페이지 표시 여부가 Y인 데이터를 가져옵니다.
    @Query("select a from Account a where pageYandN = 'Y' and acCategory = '구매'")
    List<Account> findBuyAllY();

    //거래 구분의 판매이며 페이지 표시 여부가 Y인 데이터를 가져옵니다.
    @Query("select a from Account a where pageYandN = 'Y' and acCategory = '판매'")
    List<Account> findSellerAllY();

    //상품 등록에 사용할 거래처 전부를 가져옵니다.-
    @Query("select a from Account a where pageYandN = 'Y'")
    List<Account> findAllY();

    @Query("select count(a) FROM Account a where YEAR(a.regDate) = :year AND MONTH(a.regDate) = :month AND DAY(a.regDate) = :day")
    int getAccountCount(@Param("year") int year, @Param("month") int month, @Param("day") int day);
}
