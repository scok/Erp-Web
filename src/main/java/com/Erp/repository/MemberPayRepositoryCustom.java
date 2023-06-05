package com.Erp.repository;

import com.Erp.entity.MemberPay;

import java.util.List;

public interface MemberPayRepositoryCustom {
    List<MemberPay> getMemberPayList(String id);
}
