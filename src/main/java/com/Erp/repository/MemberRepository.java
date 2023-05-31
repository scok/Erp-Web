package com.Erp.repository;

import com.Erp.dto.UserDto;
import com.Erp.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member,String>, QuerydslPredicateExecutor<Member> {
    //<받아올 데이터 객체(재너릭 기법),프라이머리 키 데이터 타입>
    Member findMemberById(String id);
    Member findMemberByEmail(String email);
    Member findMemberByPhoneNumber(String phoneNumber);

    @Query("select new com.Erp.dto.UserDto(u.id, u.name, u.birth, u.email, u.phoneNumber, u.date, u.department,u.role) from Member u where u.id = :id")
    UserDto findMemberInfo(@Param("id") String id);

    @Query("select new com.Erp.dto.UserDto(u.id, u.name, u.birth, u.email, u.phoneNumber, u.date, u.department,u.role) from Member u where u.id = :id")
    UserDto selectAll(@Param("id") String id);

}
