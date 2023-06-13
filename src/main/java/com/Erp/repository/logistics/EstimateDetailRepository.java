package com.Erp.repository.logistics;

import com.Erp.entity.logistics.EstimateDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstimateDetailRepository extends JpaRepository<EstimateDetail, Long> {

}
