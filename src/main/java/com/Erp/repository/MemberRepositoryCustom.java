package com.Erp.repository;

import com.Erp.dto.MemberSearchDto;
import com.Erp.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {
    Page<Member> getMemberListPage(MemberSearchDto searchDto, Pageable pageable);
}
