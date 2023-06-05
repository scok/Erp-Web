package com.Erp.repository;

import com.Erp.entity.Member;
import com.Erp.entity.MemberImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MemberImageRepository extends JpaRepository<MemberImage,String> {
    MemberImage findByMember(Member member);

    @Transactional
    @Modifying
    @Query("DELETE FROM MemberImage mi WHERE mi.member.id = :memberId")
    void deleteByMemberId(String memberId);


}