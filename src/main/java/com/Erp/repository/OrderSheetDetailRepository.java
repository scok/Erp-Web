package com.Erp.repository;

import com.Erp.entity.OrderSheetDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderSheetDetailRepository extends JpaRepository<OrderSheetDetail, Long> {

}
