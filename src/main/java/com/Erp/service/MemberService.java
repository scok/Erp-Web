package com.Erp.service;

import com.Erp.entity.Member;
import com.Erp.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service//for 비즈니스 로직 담당자
@RequiredArgsConstructor//final 이나 NotNull이 붙어 있는 변수에 생성자를 자동으로 만들어 줍니다.
public class MemberService implements UserDetailsService {


    //implements UserDetailsService
    //.usernameParameter("Id") 시큐리티 설정 class에 usernameParameter확인 필요.
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        Member member = memberRepository.findMemberById(id);
        if(member == null){//회원이 존재하지 않는 경우
            throw new UsernameNotFoundException(id);
        }

        return User.builder()
                .username(member.getId())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    
    private final MemberRepository memberRepository;

    public Member saveMember(Member member){

        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){

        Member findId = memberRepository.findMemberById(member.getId());
        Member findEmail = memberRepository.findMemberByEmail(member.getEmail());
        Member findPhoneNumber = memberRepository.findMemberByPhoneNumber(member.getPhoneNumber());

        if(findId != null ||findEmail != null || findPhoneNumber != null ) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
}
