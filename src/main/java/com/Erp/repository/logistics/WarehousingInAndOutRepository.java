package com.Erp.repository.logistics;

import com.Erp.entity.logistics.WarehousingInAndOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface WarehousingInAndOutRepository extends JpaRepository<WarehousingInAndOut, Long> {

    @Query("select w from WarehousingInAndOut w where w.divisionStatus = '입고' and w.pageYandN = 'Y' ")
    List<WarehousingInAndOut> findByWarehousingInList();

    @Query("select w from WarehousingInAndOut w where w.divisionStatus = '출고' and w.pageYandN = 'Y' ")
    List<WarehousingInAndOut> findByWarehousingOutList();

    @Query("select w from WarehousingInAndOut w where w.divisionStatus = '입고' and " +
            "w.production.product.prCode = :prCode and w.production.count >= :osQuantity and " +
            "w.pageYandN = 'Y' ")
    List<WarehousingInAndOut> finByPrCode(@Param("prCode") String prCode, @Param("osQuantity") Integer osQuantity);
}
