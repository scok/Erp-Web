package com.Erp.entity.logistics;

import com.Erp.constant.AccountCategory;
import com.Erp.dto.logistics.AccountAddDto;
import com.Erp.entity.logistics.DistributionEntity;
import com.Erp.entity.logistics.OrderSheet;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.text.DecimalFormat;

//거래처 entity
@Entity
@Table(name = "Accounts")
@Getter@Setter
public class Account extends DistributionEntity {

    @Id
    @Column(unique = true)//unique = true 해당 값은 유니크한 값이 들어간다. 중복 x
    private String acCode;         //거래처 코드

    @Column(nullable = false)
    private String acName;         //거래처 명

    @Column(nullable = false)
    private String acCeo;          //거래처 대표자 명

    @Column(nullable = false)
    private String acBusiness;     //거래처 업태

    @Column(nullable = false)
    private String acEvent;        //거래처 종목

    @Column(name = "ac_rn" , nullable = false)
    private String acRN;           //사업자등록번호(registration num)

    @Column(nullable = false)
    private String acAddress;      //거래처 주소

    @Column(nullable = false)
    private String acNumber;       //거래처 번호

    @Column(nullable = false)
    private String acFax;          //거래처 팩스번호

    @Column(nullable = false)
    private String acHomePage;     //거래처 사이트

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountCategory acCategory; //거래 구분

    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "acCode")
    private OrderSheet orderSheet;

    private static ModelMapper modelMapper = new ModelMapper();

    public static Account of(AccountAddDto accountAddDto){
        return modelMapper.map(accountAddDto,Account.class);
    }

    public static Account setAccount(AccountAddDto accountAddDto, int count,String strNowDate,String memberName){

        Account account = new Account();

        String category = String.valueOf(accountAddDto.getAcCategory());

        String address = accountAddDto.getAcAddress() + accountAddDto.getAcAddressDetail();

        String code = null;
        DecimalFormat newcnt = new DecimalFormat("00");
        if(category.equals("구매")){
            code="BU-"+accountAddDto.getAcRN()+"-"+strNowDate+"-"+newcnt.format(count+1);
        }else if(category.equals("판매")){
            code="SE-"+accountAddDto.getAcRN()+"-"+strNowDate+"-"+newcnt.format(count+1);
        }
        account.setAcCode(code);
        account.setAcName(accountAddDto.getAcName());
        account.setAcCeo(accountAddDto.getAcCeo());
        account.setAcBusiness(accountAddDto.getAcBusiness());
        account.setAcEvent(accountAddDto.getAcEvent());
        account.setAcRN(accountAddDto.getAcRN());
        account.setAcAddress(address);
        account.setAcNumber(accountAddDto.getAcNumber());
        account.setAcFax(accountAddDto.getAcFax());
        account.setAcHomePage(accountAddDto.getAcHomepage());
        account.setAcCategory(accountAddDto.getAcCategory());
        account.setPageYandN("Y");
        account.setCreateName(memberName);

        return account;
    }
}
