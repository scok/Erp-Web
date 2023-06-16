package com.Erp.service;

import com.Erp.dto.*;
import com.Erp.entity.Member;
import com.Erp.entity.MemberPay;
import com.Erp.repository.MemberPayRepository;
import com.Erp.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service//for 비즈니스 로직 담당자
@RequiredArgsConstructor//final 이나 NotNull이 붙어 있는 변수에 생성자를 자동으로 만들어 줍니다.
public class MemberPayService {

    private final MemberPayRepository memberPayRepository;
    private final MemberRepository memberRepository;
    private final IncomeRepository incomeRepository;
    private final IncomeService incomeService;
    private final FinancialService financialService;

    // 급여 저장 save
    public MemberPay saveMemberPay(MemberPay memberPay){
        return memberPayRepository.save(memberPay);
    }


    // 급여 수정 save
    public MemberPay updateMemberPay(MemberPay memberPay){

        return memberPayRepository.save(memberPay);
    }

    public Page<MemberPay> getMemberPayListPage(MemberPaySearchDto dto, Pageable pageable){
        return memberPayRepository.getMemberPayListPage(dto, pageable);
    }


    // 급여 중복 유효성검사 후 급여등록dto에 set하기
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


    // 급여 중복 유효성검사 후 급여수정dto에 set하기
    public MemberPayUpdateDto getMemberPayMemberInfo2(Long id) {
        Member member = memberRepository.findById(String.valueOf(id)).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 ID 입니다."));

        MemberPayUpdateDto dto = new MemberPayUpdateDto();
        dto.setId(member.getId());
        dto.setName(member.getName());
        dto.setPosition(member.getPosition());
        dto.setDepartment(member.getDepartment());
        dto.setBirth(member.getBirth());
        dto.setBank(member.getBank());
        dto.setSalary(dto.getSalary());

        return dto;
    }


    // 급여 리스트를 위한 메소드
    public List<MemberPay> getMemberPayList(Long id){
        return memberPayRepository.getMemberPayList(String.valueOf(id));
    }

    public void updateMemberData(MemberPay memberPay){

        Member member = memberPay.getMember();

        if (member != null){
            short year = (short) member.getDate().getYear();
            int quarter = (int) Math.ceil(((double) (member.getDate().getMonthValue() - 1) / 3) + 1);

            Income income = incomeRepository.findIncomeYearAndQuarter(year, quarter);

            if(income != null){

                List<Member> members = memberRepository.findMemberYear((int) year, income.getQuarter());

                if (members.stream().noneMatch(m -> m.getId().equals(member.getId()))) {
                    members.add(member);
                }

                incomeService.saveData(income, members);

                Financial financial = income.getFinancial();

                if(financial != null){
                    financialService.saveData(financial);
                }
            }
        }
    }
}