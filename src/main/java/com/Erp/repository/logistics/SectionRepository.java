package com.Erp.repository.logistics;

import com.Erp.entity.logistics.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, String> {

    @Query("select a from Section a where pageYandN = 'Y' ")
    List<Section> findSectionAllY();

    @Query("SELECT COUNT(s) FROM Section s WHERE YEAR(s.regDate) = :year AND MONTH(s.regDate) = :month AND DAY(s.regDate) = :day")
    int getSecCodeCount(@Param("year") int year, @Param("month") int month, @Param("day") int day);

    @Query("select s from Section s where s.secCategory = '자재창고' and s.pageYandN = 'Y' ")
    List<Section> findSectionMaterial();

    @Query("select s from Section s where s.secCategory = '제품창고' and s.pageYandN = 'Y' ")
    List<Section> findSectionProduct();
}
