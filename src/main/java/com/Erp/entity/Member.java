package com.Erp.entity;

import com.Erp.constant.MemberRole;
import com.Erp.constant.MemberStatus;
import com.Erp.dto.MemberInsertDto;
import com.Erp.dto.MemberUpdateDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity //JPA로 관리한다는 설정.
@Table(name = "members")
@Getter@Setter@ToString
public class Member extends BaseEntity {

    @Id
    @Column(unique = true)// unique = true 해당 값은 유니크한 값이 들어간다. 중복 x
    private String id;  // 사번

    @Column(nullable = false)
    private String name; // 이름

    @Column(nullable = false)
    private String password; // 패스워드

    @Column(nullable = false)
    private String birth; // 생년월일

    @Column(nullable = false, unique = true)
    private String email; // 이메일

    @Column(nullable = false, unique = true)
    private String phone; // 핸드폰 번호

    @Column(nullable = false)
    private String address; // 주소
    
    @Column(nullable = false)
    private LocalDate date; // 입사 날짜
    
    @Column(nullable = false)
    private String department; // 부서

    @Column(nullable = false)
    private String position; // 직위

    @Column(nullable = false)
    private String hobong; // 호봉

    @Column(nullable = false)
    private String bank; // 계좌 번호

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberStatus status; // 재직 현황 (재직중, 퇴사)

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role; //구분 (관리자, 사용자)

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "member") // Member 일대일 매핑
    private MemberImage memberImage; // 이미지 저장 주소


    // 사번을 자동으로 만들어 주는 메소드 (현재날짜를 가져와서 + 1)
    public static Member createid(MemberInsertDto memberInsertDto, PasswordEncoder passwordEncoder, JPAQueryFactory queryFactory){
        //입사년월
        int year = 0;
        int month = 0;
        int day = 0;

        Member member = new Member();

        QMember qbean = QMember.member;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String strNowDate = memberInsertDto.getDate().format(formatter);

        year=Integer.parseInt(strNowDate.substring(0,4));
        month=Integer.parseInt(strNowDate.substring(4,6));
        day=Integer.parseInt(strNowDate.substring(6));

        Long count = queryFactory
                .select(qbean.count())
                .from(qbean)
                .where(qbean.date.year().eq(year))
                .where(qbean.date.month().eq(month))
                .where(qbean.date.dayOfMonth().eq(day))
                .fetchOne();

        String id = null;
        DecimalFormat newcnt = new DecimalFormat("00");

        id=strNowDate + newcnt.format(count+1);

        member.setId(id);
        //비밀번호 암호화
        String password=passwordEncoder.encode(memberInsertDto.getPassword());
        member.setPassword(password);
        member.setName(memberInsertDto.getName());
        member.setBirth(memberInsertDto.getBirth());
        member.setEmail(memberInsertDto.getEmail());
        member.setPhone(memberInsertDto.getPhone());
        member.setAddress(memberInsertDto.getAddress());
        member.setDate(memberInsertDto.getDate());
        member.setDepartment(memberInsertDto.getDepartment());
        member.setPosition(memberInsertDto.getPosition());
        member.setHobong(memberInsertDto.getHobong());
        member.setBank(memberInsertDto.getBank());
        member.setStatus(memberInsertDto.getStatus());
        member.setRole(memberInsertDto.getRole());

        System.out.println("멤버 : " + member);

        return member;
    }

    // 멤버정보 수정 메소드
    public static Member updateid(MemberUpdateDto memberUpdateDto, Member existingMember, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        // 기존의 ID 정보 사용
        member.setId(existingMember.getId());

        // 기존의 암호화된 비밀번호 유지
        member.setPassword(existingMember.getPassword());

        // 비밀번호가 수정되었다면, 새로운 비밀번호로 암호화하여 설정
        if (memberUpdateDto.getPassword() != null && !memberUpdateDto.getPassword().isEmpty()) {
            String newPassword = passwordEncoder.encode(memberUpdateDto.getPassword());
            member.setPassword(newPassword);
        }

        // 나머지 정보 업데이트
        member.setName(memberUpdateDto.getName());
        member.setBirth(memberUpdateDto.getBirth());
        member.setEmail(memberUpdateDto.getEmail());
        member.setPhone(memberUpdateDto.getPhone());
        member.setAddress(memberUpdateDto.getAddress());
        member.setDate(memberUpdateDto.getDate());
        member.setDepartment(memberUpdateDto.getDepartment());
        member.setPosition(memberUpdateDto.getPosition());
        member.setHobong(memberUpdateDto.getHobong());
        member.setBank(memberUpdateDto.getBank());
        member.setStatus(memberUpdateDto.getStatus());
        member.setRole(memberUpdateDto.getRole());

        return member;
    }
}
