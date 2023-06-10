package com.Erp.repository.logistics;

import com.Erp.constant.StackAreaCategory;
import com.Erp.entity.logistics.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    //재고에 같은 상품이 있는지 검사합니다.
    @Query("select i from Inventory i where i.section.secCode = :secCode " +
            "and i.stackAreaCategory = :stackAreaCategory " +
            "and i.product.prCode = :prCode")
    Inventory getInventoryInFo(@Param("secCode") String secCode, @Param("stackAreaCategory") StackAreaCategory stackAreaCategory, @Param("prCode") String prCode);

    @Query("select i from Inventory i where i.pageYandN = 'Y' and i.inQuantity > 0 ")
    List<Inventory> getInventoryAll();

    @Query("select i from Inventory i where i.pageYandN = 'Y' And i.section.secCode = :secCode")
    List<Inventory> findInventorySecCode(@Param("secCode") String secCode);

    @Query("SELECT i FROM Inventory i WHERE i.product.prDivCategory = '자재' and i.inQuantity > 0 ")
    List<Inventory> getinventoryInFo();
}
