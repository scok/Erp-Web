package com.Erp.repository.logistics;

import com.Erp.constant.StackAreaCategory;
import com.Erp.dto.ProductionChartDto;
import com.Erp.entity.logistics.Inventory;
import com.Erp.entity.logistics.Production;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ProductionRepository extends JpaRepository<Production, Long> {

    @Query("select NEW com.Erp.dto.ProductionChartDto(p.productionLine,p.product.prCode,p.product.prName, sum(p.count))" +
            "from Production p " +
            "where p.regDate Between :startDateTime and :endDateTime "+
            "group by p.productionLine, p.product.prCode " +
            "order by p.productionLine")
    List<ProductionChartDto> productionsChartData(@Param("startDateTime")LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);
}
