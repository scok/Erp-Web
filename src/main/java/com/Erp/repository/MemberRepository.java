package com.Erp.repository;

import com.Erp.dto.UserDto;
import com.Erp.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, String>, QuerydslPredicateExecutor<Member>, MemberRepositoryCustom {
    //<받아올 데이터 객체(재너릭 기법),프라이머리 키 데이터 타입>
    // MemberRepositoryCustom 상속


    // id, email, phone 은 unique (중복 유효성검사를 위함)
    Member findMemberById(String id);

    Member findMemberByEmail(String email);

    Member findMemberByPhone(String phone);

    @Query("SELECT m FROM Member m WHERE YEAR(m.date) = :year AND FLOOR((MONTH(m.date) - 1)/ 3) + 1 <= :quarter")
    List<Member> findMemberYear(@Param("year") Integer year, @Param("quarter") Integer quarter);

    // 세션영역에 바인딩하기 위해 로그인한 사람의 정보를 가져오기 위한 쿼리메소드
    @Query("SELECT new com.Erp.dto.UserDto(u.id, u.name, u.birth, u.email, u.phone, u.date, u.department, u.role, m.imageUrl) FROM Member u JOIN u.memberImage m WHERE u.id = :id")
    UserDto findMemberInfo(@Param("id") String id);

    @Query("select new com.Erp.dto.UserDto(u.id, u.name, u.birth, u.email, u.phone, u.date, u.department,u.role, m.imageUrl) from Member u LEFT JOIN u.memberImage m where u.id = :id")
    UserDto selectAll(@Param("id") String id);

    @Query("SELECT COUNT(DISTINCT(m.id)) FROM Member m")
    Long countDistinctById();
}
