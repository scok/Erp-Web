package com.Erp.repository;

import com.Erp.entity.MemberPay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberPayRepository extends JpaRepository<MemberPay,String>, MemberPayRepositoryCustom {

    MemberPay findByMemberId(String id);

    List<MemberPay> findAllByMemberId(String id);

    MemberPay findMemberPayById(String id);
}
