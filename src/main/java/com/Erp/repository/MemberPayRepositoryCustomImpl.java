package com.Erp.repository;

import com.Erp.entity.MemberPay;
import com.Erp.entity.QMemberPay;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class MemberPayRepositoryCustomImpl implements MemberPayRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Autowired
    public MemberPayRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<MemberPay> getMemberPayList(String id) {

        QueryResults<MemberPay> results = this.queryFactory
                .selectFrom(QMemberPay.memberPay)
                .where(QMemberPay.memberPay.member.id.eq(id))
                .fetchResults();

        List<MemberPay> content = results.getResults();

        return content;
    }
}
