package com.Erp.repository.logistics;

import com.Erp.entity.logistics.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query(" select p from Product p where pageYandN = 'Y'")
    List<Product> findAll();

    @Query(" select p from Product p where ac_Code = :acCode and pageYandN = 'Y'")
    List<Product> findProductByAccount(@Param("acCode") String acCode);

    @Query("SELECT COUNT(p) FROM Product p WHERE YEAR(p.regDate) = :year AND MONTH(p.regDate) = :month AND DAY(p.regDate) = :day")
    int getProductCount(@Param("year") int year, @Param("month") int month, @Param("day") int day);

    @Query(" select p from Product p where pageYandN = 'Y' and p.prDivCategory = '제품'")
    List<Product> findByProvideAll();
}
