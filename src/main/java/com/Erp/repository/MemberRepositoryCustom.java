package com.Erp.repository;

import com.Erp.dto.MemberSearchDto;
import com.Erp.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {

    // 검색엔진과 페이징 처리를 위한 메소드
    Page<Member> getMemberListPage(MemberSearchDto searchDto, Pageable pageable);
}
