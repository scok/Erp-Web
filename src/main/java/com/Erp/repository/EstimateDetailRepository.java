package com.Erp.repository;

import com.Erp.entity.EstimateDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstimateDetailRepository extends JpaRepository<EstimateDetail, Long> {

}
