package com.Erp.repository;

import com.Erp.dto.MemberPaySearchDto;
import com.Erp.dto.MemberSearchDto;
import com.Erp.entity.MemberPay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberPayRepositoryCustom {

    // 사번에 해당하는 급여 전부 리스트로 가져오기
    List<MemberPay> getMemberPayList(String id);

    Page<MemberPay> getMemberPayListPage(MemberPaySearchDto searchDto, Pageable pageable);
}
