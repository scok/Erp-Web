package com.Erp.repository;

import com.Erp.dto.MemberPaySearchDto;
import com.Erp.entity.Member;
import com.Erp.entity.MemberPay;
import com.Erp.entity.QMember;
import com.Erp.entity.QMemberPay;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class MemberPayRepositoryCustomImpl implements MemberPayRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Autowired
    public MemberPayRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    // memberDetail 페이지에 있는 급여리스트 메소드 (사번에 해당하는 급여 전부 리스트로 가져오기)
    @Override
    public List<MemberPay> getMemberPayList(String id) {

        QueryResults<MemberPay> results = this.queryFactory
                .selectFrom(QMemberPay.memberPay)
                .where(QMemberPay.memberPay.member.id.eq(id))
                .orderBy(QMemberPay.memberPay.regDate.desc()) // 급여 날짜로 정렬
                .fetchResults();

        List<MemberPay> content = results.getResults();

        return content;
    }


    // 급여리스트 페이징처리, 검색엔진
    @Override
    public Page<MemberPay> getMemberPayListPage(MemberPaySearchDto searchDto, Pageable pageable) {
        QueryResults<MemberPay> results = this.queryFactory
                .selectFrom(QMemberPay.memberPay)
                .where(searchByCondition(searchDto.getSearchBy(),
                        searchDto.getSearchQuery())) // 사번과 이름별 조회
                .orderBy(QMemberPay.memberPay.id.desc()) // 급여 아이디로 정렬
                .offset(pageable.getOffset()) //
                .limit(pageable.getPageSize()) // 최대 페이지 설정
                .fetchResults();

        List<MemberPay> content = results.getResults();
        long total = results.getTotal() ;

        return new PageImpl<>(content, pageable, total);
    }


    // 검색 엔진을 위한 메소드 (사번과 이름별 조회)
    private BooleanExpression searchByCondition(String searchBy, String searchQuery) {
        if(StringUtils.equals("pay_id", searchBy)){ // 급여 아이디로 검색
            return QMemberPay.memberPay.id.like("%" + searchQuery + "%" ) ;

        }else if(StringUtils.equals("name", searchBy)){ // 이름으로 검색
            return QMemberPay.memberPay.member.name.like("%" + searchQuery + "%" ) ;
        }

        return null ;
    }
}
