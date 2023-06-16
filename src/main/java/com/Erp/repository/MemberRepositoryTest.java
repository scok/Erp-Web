package com.Erp.repository;

import com.Erp.constant.MemberRole;
import com.Erp.constant.MemberStatus;
import com.Erp.entity.Member;
import com.Erp.entity.MemberPay;
import com.Erp.entity.QMember;
import com.Erp.service.MemberPayService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@SpringBootTest
public class MemberRepositoryTest{

    //사원의 ERP 정보를 저장하는 공간.
    //사원의 ID는 입사년월 + 번호 ex 01

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberImageRepository memberImageRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MemberPayRepository memberPayRepository;

    @Test
    @DisplayName("사원 저장 테스트")
    public void createMemberTest(@Autowired JPAQueryFactory jpaqueryFactory){

        //입사년월
        int year = 2023;    //나중에 웹 페이지에서 입사년월을 입력하면 변경될 인스턴스
        int month = 4;      //나중에 웹 페이지에서 입사년월을 입력하면 변경될 인스턴스
        int day = 12;       //나중에 웹 페이지에서 입사년월을 입력하면 변경될 인스턴스

        QMember qbean = QMember.member;

        Long count = jpaqueryFactory
                .select(qbean.count())
                .from(qbean)
                .where(qbean.date.year().eq(year))
                .where(qbean.date.month().eq(month))
                .where(qbean.date.dayOfMonth().eq(day))
                .fetchOne();

        Date nowDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        //원하는 데이터 포맷 지정
        String strNowDate = simpleDateFormat.format(nowDate);
        //지정한 포맷으로 변환

        String newday = null;
        DecimalFormat newcnt = new DecimalFormat("00");

        Member bean = new Member();

        newday=strNowDate + newcnt.format(count+1);

        bean.setId(newday);
        String password=passwordEncoder.encode(newday);
        bean.setPassword(password);
        bean.setName("관리자");
        bean.setBirth("19991212");
        bean.setEmail("admin@naver.com");
        bean.setPhone("01012345678");
        bean.setAddress("마포구 서교동");
        bean.setDate(LocalDate.now());
        bean.setDepartment("EA팀");
        bean.setPosition("사원");
        bean.setHobong("2호봉");
        bean.setBank("217802-04-462020");
        bean.setStatus(MemberStatus.WORKING);
        bean.setRole(MemberRole.ADMIN);

        Member findId = memberRepository.findMemberById(bean.getId());
        Member findEmail = memberRepository.findMemberByEmail(bean.getEmail());
        Member findPhone = memberRepository.findMemberByPhone(bean.getPhone());

        if(findId == null && findEmail == null && findPhone == null ) {
            Member savedItem = memberRepository.save(bean);
            System.out.println(count+1);
            System.out.println(savedItem.toString());
        }
    }

    @Test
    @DisplayName("사원 저장 테스트2")
    public void createMemberTest2(@Autowired JPAQueryFactory jpaqueryFactory){

        //입사년월
        int year = 2023;    //나중에 웹 페이지에서 입사년월을 입력하면 변경될 인스턴스
        int month = 4;      //나중에 웹 페이지에서 입사년월을 입력하면 변경될 인스턴스
        int day = 12;       //나중에 웹 페이지에서 입사년월을 입력하면 변경될 인스턴스

        QMember qbean = QMember.member;

        Long count = jpaqueryFactory
                .select(qbean.count())
                .from(qbean)
                .where(qbean.date.year().eq(year))
                .where(qbean.date.month().eq(month))
                .where(qbean.date.dayOfMonth().eq(day))
                .fetchOne();

        Date nowDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        //원하는 데이터 포맷 지정
        String strNowDate = simpleDateFormat.format(nowDate);
        //지정한 포맷으로 변환

        String newday = null;
        DecimalFormat newcnt = new DecimalFormat("00");

        for (int i = 0; i< 15; i++){
            Member bean = new Member();

            newday=strNowDate + newcnt.format(count+ 1 + i) ;

            bean.setId(newday);
            String password=passwordEncoder.encode(newday);
            bean.setPassword(password);
            bean.setName("사원" + i);
            bean.setBirth("19991212");
            bean.setEmail("member" + i + "@naver.com");
            bean.setPhone("0101234567" + i);
            bean.setAddress("마포구 서교동");
            bean.setDate(LocalDate.now());
            bean.setDepartment("EA팀");
            bean.setPosition("사원");
            bean.setHobong("2호봉");
            bean.setBank("217802-04-462020");
            bean.setStatus(MemberStatus.WORKING);
            bean.setRole(MemberRole.USER);

            Member findId = memberRepository.findMemberById(bean.getId());
            Member findEmail = memberRepository.findMemberByEmail(bean.getEmail());
            Member findPhone = memberRepository.findMemberByPhone(bean.getPhone());

            if(findId == null && findEmail == null && findPhone == null ) {
                Member savedItem = memberRepository.save(bean);
                System.out.println(count+1);
                System.out.println(savedItem.toString());
            }
        }
    }

    @Autowired
    private MemberPayService memberPayService;

    @Test
    @DisplayName("사원 급여 저장 테스트")
    public void createMemberPayTest(@Autowired JPAQueryFactory jpaqueryFactory){

        //입사년월
        int year = 2023;    //나중에 웹 페이지에서 입사년월을 입력하면 변경될 인스턴스
        int month = 4;      //나중에 웹 페이지에서 입사년월을 입력하면 변경될 인스턴스
        int day = 12;       //나중에 웹 페이지에서 입사년월을 입력하면 변경될 인스턴스

        QMember qbean = QMember.member;

        Long count = jpaqueryFactory
                .select(qbean.count())
                .from(qbean)
                .where(qbean.date.year().eq(year))
                .where(qbean.date.month().eq(month))
                .where(qbean.date.dayOfMonth().eq(day))
                .fetchOne();

        Date nowDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        //원하는 데이터 포맷 지정
        String strNowDate = simpleDateFormat.format(nowDate);

        String newday = null;
        DecimalFormat newcnt = new DecimalFormat("00");

        for (int i = 0; i< 15; i++){

            newday=strNowDate + newcnt.format(count+ 1 + i) ;

            MemberPay memberPay = new MemberPay();

            Member member = memberRepository.findMemberById(newday);

            memberPay.setId(Long.valueOf(i));
            memberPay.setMember(member);
            memberPay.setSalary((long) (3000000 + ((i+1) * 100000)));
            memberPay.setBonus((long) (3000000 + ((i+1) * 100000))/ 100);
            memberPay.setNightPay((long) ((long) (3000000 + ((i+1) * 100000)) * 0.03));
            memberPay.setFoodPay(150000L);
            memberPay.setCarPay(500000L);
            memberPay.setGoInsurance(50000L);
            memberPay.setGunInsurance(50000L);
            memberPay.setSanInsurance(50000L);
            memberPay.setKukInsurance(50000L);
            memberPay.setIncomeTax((long) (memberPay.getSalary() * 0.22));
            memberPay.setLocalTax((long) (memberPay.getSalary() * 0.08));

            memberPay.setPlusMoney(memberPay.getSalary() + memberPay.getBonus() + memberPay.getNightPay() + memberPay.getFoodPay() + memberPay.getCarPay());
            memberPay.setMinusMoney(memberPay.getGoInsurance() + memberPay.getGunInsurance() + memberPay.getSanInsurance() + memberPay.getKukInsurance() + memberPay.getIncomeTax() + memberPay.getLocalTax());

            memberPay.setTotalMoney(memberPay.getPlusMoney() - memberPay.getMinusMoney());

            memberPayService.saveMemberPay(memberPay);

            memberPayService.updateMemberData(memberPay);
        }
    }
}
