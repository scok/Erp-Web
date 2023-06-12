package com.Erp.repository.logistics;

import com.Erp.entity.logistics.OrderSheet;
import com.Erp.entity.logistics.OrderSheetDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderSheetDetailRepository extends JpaRepository<OrderSheetDetail, Long> {

}
