package com.Erp.repository.logistics;

import com.Erp.entity.logistics.Production;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductionRepository extends JpaRepository<Production, Long> {

}
