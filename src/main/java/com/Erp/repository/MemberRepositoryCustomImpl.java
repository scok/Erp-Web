package com.Erp.repository;

import com.Erp.constant.MemberStatus;
import com.Erp.dto.MemberSearchDto;
import com.Erp.entity.Member;
import com.Erp.entity.QMember;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{
    private JPAQueryFactory queryFactory ;

    @Override
    public Page<Member> getMemberListPage(MemberSearchDto searchDto, Pageable pageable) {
        QueryResults<Member> results = this.queryFactory
                .selectFrom(QMember.member)
                .where(sellStatusCondition(searchDto.getMemberStatus()),
                        searchByCondition(searchDto.getSearchBy(), searchDto.getSearchQuery()))
                .orderBy(QMember.member.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Member> content = results.getResults();
        long total = results.getTotal() ;

        return new PageImpl<>(content, pageable, total);
    }

    public MemberRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchByCondition(String searchBy, String searchQuery) {
        if(StringUtils.equals("name", searchBy)){ // 회원 이름으로 검색
            return QMember.member.name.like("%" + searchQuery + "%" ) ;

        }else if(StringUtils.equals("id", searchBy)){ // 사번으로 검색
            return QMember.member.id.like("%" + searchQuery + "%" ) ;
        }

        return null ;
    }
    private BooleanExpression sellStatusCondition(MemberStatus memberStatus) {
        return memberStatus == null ? null : QMember.member.status.eq(memberStatus) ;
    }
}
