package com.Erp.service;

import com.Erp.dto.AccountAddDto;
import com.Erp.dto.AccountFormDto;
import com.Erp.entity.Account;
import com.Erp.repository.AccountRepository;
import com.Erp.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;

    //상품 등록에 사용할 구매 + 영업의 거래처를 가져옵니다.
    public List<AccountFormDto> findSelectAll() {
        List<Account> accounts = accountRepository.findAllY();
        List<AccountFormDto> accountFormDtos = new ArrayList<AccountFormDto>();

        for(Account account : accounts){

            AccountFormDto accountFormDto = new AccountFormDto();

            accountFormDto.setAcCode(account.getAcCode());
            accountFormDto.setAcName(account.getAcName());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String createDate = account.getRegDate().format(formatter);

            accountFormDto.setAcRegDate(createDate);
            accountFormDto.setAcCreateName(account.getCreateName());
            accountFormDto.setAcCategory(account.getAcCategory());

            accountFormDtos.add(accountFormDto);

        }
        return accountFormDtos;
    }

    //구매 관리의 거래처 리스트 호출
    public List<AccountFormDto> buySelectAll(){
        List<Account> accoutList = accountRepository.findBuyAllY();
        List<AccountFormDto> accountFormDtos = new ArrayList<AccountFormDto>();

        for (Account account : accoutList){
            AccountFormDto accountFormDto = new AccountFormDto();

            accountFormDto.setAcCode(account.getAcCode());
            accountFormDto.setAcName(account.getAcName());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String createDate = account.getRegDate().format(formatter);

            accountFormDto.setAcRegDate(createDate);
            accountFormDto.setAcCreateName(account.getCreateName());
            accountFormDto.setAcCategory(account.getAcCategory());

            accountFormDtos.add(accountFormDto);
        }
        return accountFormDtos;
    }

    //영업 관리의 거래처 리스트를 가져옵니다.
    public List<AccountFormDto> sellerSelectAll() {

        List<Account> accoutList = accountRepository.findSellerAllY();
        List<AccountFormDto> accountFormDtos = new ArrayList<AccountFormDto>();

        for (Account account : accoutList){
            AccountFormDto accountFormDto = new AccountFormDto();

            accountFormDto.setAcCode(account.getAcCode());
            accountFormDto.setAcName(account.getAcName());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String createDate = account.getRegDate().format(formatter);

            accountFormDto.setAcRegDate(createDate);
            accountFormDto.setAcCreateName(account.getCreateName());
            accountFormDto.setAcCategory(account.getAcCategory());

            accountFormDtos.add(accountFormDto);
        }
        return accountFormDtos;
    }

    //데이터 베이스에 값을 저장합니다.
    public Account saveAccount(Account account){
        return accountRepository.save(account);
    }

    public Account findByAcCode(String code) {
        return accountRepository.findById(code).orElseThrow(EntityNotFoundException::new);
    }

    public int accountCount(int year, int month, int day) {
        return accountRepository.getAccountCount(year,month,day);
    }

    //이렇게 하는 이유는 SPA 업데이트 문을 실행할때 SAVE 메소드를 이용할경우 데이터 베이스에 두번 조회를 하기 때문에
    //자원 낭비를 줄이고자  @Transactional를 이용하여 변경된 필드를 감지하여 DB에 커밋을 해줍니다.
    //데이터 베이스에 수정 작업을 합니다.
    @Transactional
    public Account updateBuy(AccountAddDto accountAddDto,String memberName){
        Account target = accountRepository.findById(accountAddDto.getAcCode()).orElseThrow(EntityNotFoundException::new);

        target.setAcName(accountAddDto.getAcName());
        target.setAcCeo(accountAddDto.getAcCeo());
        target.setAcBusiness(accountAddDto.getAcBusiness());
        target.setAcEvent(accountAddDto.getAcEvent());
        target.setAcRN(accountAddDto.getAcRN());
        target.setAcAddress(accountAddDto.getAcAddress());
        target.setAcNumber(accountAddDto.getAcRN());
        target.setAcFax(accountAddDto.getAcFax());
        target.setAcHomePage(accountAddDto.getAcHomepage());
        target.setAcCategory(accountAddDto.getAcCategory());
        target.setCreateName(memberName);

        return target;
    }

    @Transactional
    public void deleteBuy(List<String> code) {

        for(String item : code){

            if(item != null) {
                Account account = accountRepository.findById(item).orElseThrow(EntityNotFoundException::new);
                account.setPageYandN("N");
            }
        }
    }
}
