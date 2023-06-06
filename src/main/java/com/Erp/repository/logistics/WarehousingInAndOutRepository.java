package com.Erp.repository.logistics;

import com.Erp.entity.logistics.WarehousingInAndOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WarehousingInAndOutRepository extends JpaRepository<WarehousingInAndOut, Long> {

    @Query("select w from WarehousingInAndOut w where w.divisionStatus = '입고' and w.pageYandN = 'Y' ")
    List<WarehousingInAndOut> findByWarehousingInList();

    @Query("select w from WarehousingInAndOut w where w.divisionStatus = '출고' and w.pageYandN = 'Y' ")
    List<WarehousingInAndOut> findByWarehousingOutList();
    
}
