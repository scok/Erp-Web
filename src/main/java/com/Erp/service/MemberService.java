package com.Erp.service;

import com.Erp.dto.MemberDetailDto;
import com.Erp.dto.MemberPayInsertDto;
import com.Erp.dto.MemberSearchDto;
import com.Erp.dto.MemberUpdateDto;
import com.Erp.entity.Member;
import com.Erp.entity.MemberImage;
import com.Erp.repository.MemberImageRepository;
import com.Erp.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // for 비즈니스 로직 담당자
@RequiredArgsConstructor // final 이나 NotNull이 붙어 있는 변수에 생성자를 자동으로 만들어 줍니다.
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final MemberImageRepository memberImageRepository;
    private final IncomeRepository incomeRepository;
    private final IncomeService incomeService;
    private final FinancialService financialService;

    // implements UserDetailsService
    // .usernameParameter("Id") 시큐리티 설정 class에 usernameParameter확인 필요.
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        Member member = memberRepository.findMemberById(id);
        if(member == null){ // 회원이 존재하지 않는 경우
            throw new UsernameNotFoundException(id);
        }

        return User.builder()
                .username(member.getId())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }


    // 중복 유효성 검사 후 멤버 저장 save
    public Member saveMember(Member member){

        validateDuplicateMember(member);
        return memberRepository.save(member);
    }


    // unique 값 유효성 검사를 위한 메소드
    private void validateDuplicateMember(Member member){

        Member findId = memberRepository.findMemberById(member.getId());
        Member findEmail = memberRepository.findMemberByEmail(member.getEmail());
        Member findPhone = memberRepository.findMemberByPhone(member.getPhone());

        if(findId != null ||findEmail != null || findPhone != null ) {
            throw new RuntimeException("이미 가입된 회원입니다.");
        }
    }


    // 페이징 처리, 검색엔진을 위한 메소드
    public Page<Member> getMemberListPage(MemberSearchDto dto, Pageable pageable){
        return memberRepository.getMemberListPage(dto, pageable);
    }


    // 사번으로 멤버 조회
    public Member getMemberById(Long id) {
        return memberRepository.findById(String.valueOf(id)).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 ID 입니다."));
    }


    // 멤버 수정을 위한 메소드 (이미지도 같이 수정)
    public MemberUpdateDto getMemberWithImage(Long id) {
        Member member = memberRepository.findById(String.valueOf(id)).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 ID 입니다."));

        MemberImage memberImage = memberImageRepository.findByMember(member);

        MemberUpdateDto dto = new MemberUpdateDto();
        dto.setId(Long.valueOf(member.getId()));
        dto.setName(member.getName());
        dto.setPosition(member.getPosition());
        dto.setRole(member.getRole());
        dto.setStatus(member.getStatus());
        dto.setPhone(member.getPhone());
        dto.setPassword(member.getPassword());
        dto.setHobong(member.getHobong());
        dto.setAddress(member.getAddress());
        dto.setBank(member.getBank());
        dto.setDepartment(member.getDepartment());
        dto.setDate(member.getDate());
        dto.setBirth(member.getBirth());
        dto.setEmail(member.getEmail());
        dto.setImageUrl(memberImage.getImageUrl());
        return dto;
    }


    // 멤버 상세를 위한 메소드 (이미지도 같이 조회)
    public MemberDetailDto getMemberWithImage2(Long id) {
        Member member = memberRepository.findById(String.valueOf(id)).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 ID 입니다."));

        MemberImage memberImage = memberImageRepository.findByMember(member);

        MemberDetailDto dto = new MemberDetailDto();
        dto.setId(Long.valueOf(member.getId()));
        dto.setName(member.getName());
        dto.setPosition(member.getPosition());
        dto.setRole(member.getRole());
        dto.setStatus(member.getStatus());
        dto.setPhone(member.getPhone());
        dto.setPassword(member.getPassword());
        dto.setHobong(member.getHobong());
        dto.setAddress(member.getAddress());
        dto.setBank(member.getBank());
        dto.setDepartment(member.getDepartment());
        dto.setDate(member.getDate());
        dto.setBirth(member.getBirth());
        dto.setEmail(member.getEmail());
        dto.setImageUrl(memberImage.getImageUrl());
        return dto;
    }


    // 멤버 수정 save
    public void updateMember(Member member) {
        memberRepository.save(member);
    }
}
