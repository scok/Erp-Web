package com.Erp.repository.logistics;

import com.Erp.entity.logistics.MaterialDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MaterialDeliveryRepository extends JpaRepository<MaterialDelivery, Long> {

    @Query("select m from MaterialDelivery m where pageYandN = 'Y' ")
    List<MaterialDelivery> findAllY();
}
