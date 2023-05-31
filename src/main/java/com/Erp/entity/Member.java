package com.Erp.entity;

import com.Erp.constant.MemberRole;
import com.Erp.dto.MemberInsertDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity //JPA로 관리한다는 설정.
@Table(name = "members")
@Getter@Setter
public class Member extends BaseEntity {

    @Id
    @Column(unique = true)//unique = true 해당 값은 유니크한 값이 들어간다. 중복 x
    private String id;  //사원번호

    @Column( nullable = false)
    private String password;    //password

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long salary;

    private Long compensation;

    @Column(nullable = false)
    private String birth; //생년월일

    @Column(nullable = false, unique = true)
    private String email; //이메일

    @Column(nullable = false, unique = true)
    private String phoneNumber; //핸드폰 번호
    
    @Column( nullable = false)
    private LocalDate date; //입사년월
    
    @Column( nullable = false)
    private String department; //부서

    @Column( nullable = false)
    private String position; //직위

    @Column(nullable = false)
    private String paystep; //직급


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;//사용자&관리자

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Income> incomes = new ArrayList<>();

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
        member.setPhoneNumber(memberInsertDto.getPhoneNumber());
        member.setDate(memberInsertDto.getDate());
        member.setDepartment(memberInsertDto.getDepartment());
        member.setPosition(memberInsertDto.getPosition());
        member.setPaystep(memberInsertDto.getPaystep());
        member.setRole(memberInsertDto.getRole());

        return member;
    }

}
