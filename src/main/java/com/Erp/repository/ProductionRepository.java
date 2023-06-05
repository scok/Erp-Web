package com.Erp.repository;

import com.Erp.entity.Production;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductionRepository extends JpaRepository<Production, Long> {

}
