package com.Erp.entity;

import com.Erp.dto.MemberPayInsertDto;
import com.Erp.dto.MemberPayUpdateDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity //JPA로 관리한다는 설정.
@Table(name = "membersPay")
@Getter@Setter@ToString
public class MemberPay extends BaseEntity {

    @Id
    @Column(name = "pay_id")//unique = true 해당 값은 유니크한 값이 들어간다. 중복 x
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;  //사원번호

    @Column(nullable = false)
    private Long salary ; //기본급

    @Column(nullable = false)
    private Long bonus;    // 상여금

    @Column(nullable = false)
    private Long nightPay; //야근수당

    @Column(nullable = false)
    private Long foodPay; //식비

    @Column(nullable = false)
    private Long carPay; //차량지원금

    @Column(nullable = false)
    private Long goInsurance; // 고용보험

    @Column(nullable = false)
    private Long gunInsurance; //건강보험

    @Column(nullable = false)
    private Long sanInsurance; //산재보험

    @Column(nullable = false)
    private Long kukInsurance; //국민연금

    @Column(nullable = false)
    private Long incomeTax; //소득세

    @Column(nullable = false)
    private Long localTax; //지방소득세

    @Column(nullable = false)
    private Long plusMoney; //지급총액

    @Column(nullable = false)
    private Long minusMoney; //공제총액

    @Column(nullable = false)
    private Long TotalMoney; //차감지급액


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    // 급여 콤마 제거후 저장
    public static MemberPay insertPay(MemberPayInsertDto memberPayInsertDto, Member member) {
        MemberPay memberPay = new MemberPay();
        System.out.println("급여 콤마 제거후 저장 들어왔니?");
        memberPay.setMember(member);

        memberPay.setSalary(memberPayInsertDto.getSalary());
        memberPay.setBonus(memberPayInsertDto.getBonus());
        memberPay.setNightPay(memberPayInsertDto.getNightPay());
        memberPay.setFoodPay(memberPayInsertDto.getFoodPay());
        memberPay.setCarPay(memberPayInsertDto.getCarPay());
        memberPay.setGoInsurance(memberPayInsertDto.getGoInsurance());
        memberPay.setGunInsurance(memberPayInsertDto.getGunInsurance());
        memberPay.setSanInsurance(memberPayInsertDto.getSanInsurance());
        memberPay.setKukInsurance(memberPayInsertDto.getKukInsurance());
        memberPay.setIncomeTax(memberPayInsertDto.getIncomeTax());
        memberPay.setLocalTax(memberPayInsertDto.getLocalTax());
        memberPay.setPlusMoney(memberPayInsertDto.getPlusMoney());
        memberPay.setMinusMoney(memberPayInsertDto.getMinusMoney());
        memberPay.setTotalMoney(memberPayInsertDto.getTotalMoney());

        return memberPay;
    }


    // 급여 수정 콤마 제거후 저장
    public static MemberPay updatePay(MemberPayUpdateDto memberPayUpdateDto, Member member, String payId) {
        MemberPay memberPay = new MemberPay();
        System.out.println("급여 수정 콤마 제거후 저장 들어왔니?");
        memberPay.setMember(member);
        memberPay.setId(Long.valueOf(payId));
        memberPay.setSalary(memberPayUpdateDto.getSalary());
        memberPay.setBonus(memberPayUpdateDto.getBonus());
        memberPay.setNightPay(memberPayUpdateDto.getNightPay());
        memberPay.setFoodPay(memberPayUpdateDto.getFoodPay());
        memberPay.setCarPay(memberPayUpdateDto.getCarPay());
        memberPay.setGoInsurance(memberPayUpdateDto.getGoInsurance());
        memberPay.setGunInsurance(memberPayUpdateDto.getGunInsurance());
        memberPay.setSanInsurance(memberPayUpdateDto.getSanInsurance());
        memberPay.setKukInsurance(memberPayUpdateDto.getKukInsurance());
        memberPay.setIncomeTax(memberPayUpdateDto.getIncomeTax());
        memberPay.setLocalTax(memberPayUpdateDto.getLocalTax());
        memberPay.setPlusMoney(memberPayUpdateDto.getPlusMoney());
        memberPay.setMinusMoney(memberPayUpdateDto.getMinusMoney());
        memberPay.setTotalMoney(memberPayUpdateDto.getTotalMoney());
        System.out.println("member객체"+member);
        System.out.println("memberPayUpdateDto.getSalary()"+memberPayUpdateDto.getSalary());
        System.out.println("memberPayUpdateDto.getGoInsurance()"+memberPayUpdateDto.getGoInsurance());
        System.out.println("member객체"+member);

        return memberPay;
    }


}