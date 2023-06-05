package com.Erp.service;

import com.Erp.dto.MemberPayDetailDto;
import com.Erp.dto.MemberPayInsertDto;
import com.Erp.entity.Member;
import com.Erp.entity.MemberPay;
import com.Erp.repository.MemberPayRepository;
import com.Erp.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service//for 비즈니스 로직 담당자
@RequiredArgsConstructor//final 이나 NotNull이 붙어 있는 변수에 생성자를 자동으로 만들어 줍니다.
public class MemberPayService {

    private final MemberPayRepository memberPayRepository;
    private final MemberRepository memberRepository;

    public MemberPay saveMemberPay(MemberPay memberPay){

        return memberPayRepository.save(memberPay);
    }

//    private void validateDuplicateMemberPay(MemberPay memberPay, String id){
//        MemberPay findId = memberPayRepository.findByMemberId(id);
//
//        if(findId != null ) {
//            throw new IllegalStateException("이미 급여가 등록된 회원입니다.");
//        }
//    }

    public MemberPayInsertDto getMemberPayMemberInfo(Long id) {
        Member member = memberRepository.findById(String.valueOf(id)).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 ID 입니다."));

        MemberPayInsertDto dto = new MemberPayInsertDto();
        dto.setId(member.getId());
        dto.setName(member.getName());
        dto.setPosition(member.getPosition());
        dto.setDepartment(member.getDepartment());
        dto.setBirth(member.getBirth());
        dto.setBank(member.getBank());
        dto.setSalary(dto.getSalary());

        return dto;
    }

    public MemberPayDetailDto getMemberPayInfo(Long id) {
        MemberPay memberpay = memberPayRepository.findMemberPayById(String.valueOf(id));

        MemberPayDetailDto dto = new MemberPayDetailDto();
        dto.setPayid(memberpay.getId());
        dto.setFoodPay(memberpay.getFoodPay());
        dto.setCarPay(memberpay.getCarPay());
        dto.setNightPay(memberpay.getNightPay());
        dto.setBonus(memberpay.getBonus());
        dto.setGoInsurance(memberpay.getGoInsurance());
        dto.setGunInsurance(memberpay.getGunInsurance());
        dto.setIncomeTax(memberpay.getIncomeTax());
        dto.setLocalTax(memberpay.getLocalTax());
        dto.setKukInsurance(memberpay.getKukInsurance());
        dto.setMinusMoney(memberpay.getMinusMoney());
        dto.setPlusMoney(memberpay.getPlusMoney());
        dto.setSanInsurance(memberpay.getSanInsurance());
        dto.setSalary(memberpay.getSalary());
        dto.setTotalMoney(memberpay.getTotalMoney());

        return dto;
    }


    public List<MemberPay> getMemberPayList(Long id){
        return memberPayRepository.getMemberPayList(String.valueOf(id));
    }
}