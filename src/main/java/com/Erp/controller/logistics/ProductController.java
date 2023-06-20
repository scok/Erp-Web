package com.Erp.controller.logistics;

import com.Erp.dto.UserDto;
import com.Erp.dto.logistics.AccountFormDto;
import com.Erp.dto.logistics.ProductAddDto;
import com.Erp.dto.logistics.ProductFormDto;
import com.Erp.entity.logistics.Account;
import com.Erp.entity.Member;
import com.Erp.entity.logistics.Product;
import com.Erp.service.logistics.AccountService;
import com.Erp.service.MemberService;
import com.Erp.service.logistics.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/products")
public class ProductController {

    private final ProductService productService;
    private final AccountService accountService;
    private final MemberService memberService;

    //페이지 접속시 거래처의 정보 바인딩
    @GetMapping(value = "/list")
    public String goProductList(Model model){
        List<AccountFormDto> accountFormDtos = accountService.findSelectAll();
        model.addAttribute("accountFormDtos",accountFormDtos); // 거래처의 정보를 바인딩 합니다.
        return "/product/productForm";
    }

    // 데이터 베이스에 있는 상품 정보를 조회
    @GetMapping(value = "/check")
    public @ResponseBody Object SelectAll(Model model){
        List<ProductFormDto> productListDtoForm = productService.productListAll();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", productListDtoForm);
        Object data = map;

        return data;
    }

    //상품 페이지 modal에 데이터를 넣어주는 메소드
    @PostMapping(value = "/updateProduct")
    public @ResponseBody ResponseEntity updateData(@RequestBody String code, HttpServletRequest request){

        boolean department = this.getSession(request);
        if(!department){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }
        //현재 문제점 Ajax로 넘겨 받은 데이터안에 ""가 붙어있어서 문제 발생.
        code = code.substring(1,code.length()-1);
        Product product = productService.findByCode(code);
        ProductAddDto productAddDto = ProductAddDto.of(product); //필요한 데이터만 가져오는 작업.
        return new ResponseEntity<>(productAddDto,HttpStatus.OK);
    }

    @PostMapping(value = "/addProduct")
    public @ResponseBody ResponseEntity addProduct(@RequestBody @Valid ProductAddDto dto, BindingResult error, Principal principal, HttpServletRequest request){

        boolean department = this.getSession(request);
        if(!department){
            return new ResponseEntity<String>("등록 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        String errorMessage = "";

        StringBuilder erromessages = new StringBuilder();
        if (error.hasErrors()){
            for (FieldError errorBean : error.getFieldErrors()) {
                erromessages.append(errorBean.getField() + " : " + errorBean.getDefaultMessage() + "\n");
            }
            errorMessage = erromessages.toString();

            return new ResponseEntity<String>(errorMessage,HttpStatus.BAD_REQUEST);
        }

        Account account = accountService.findByAcCode(dto.getAcCode());
        Member member = memberService.getMemberName(principal.getName());

        try {
            System.out.println(dto.toString());
            if(dto.getPrCode() == null){//만약 코드에 유의미한 값이 들어 있다면 수정기능을 사용합니다.
                //여기는 등록 기능입니다.
                int count = 0;

                LocalDateTime regDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                String strNowDate = regDateTime.format(formatter);

                int year=Integer.parseInt(strNowDate.substring(0,4));
                int month=Integer.parseInt(strNowDate.substring(4,6));
                int day=Integer.parseInt(strNowDate.substring(6));

                count = productService.productListCount(year,month,day);

                Product product = Product.createCode(dto,account,count,strNowDate,member.getName());
                productService.save(product);
            }else{
                //여기는 수정기능입니다.
                productService.updateProduct(dto,account);
            }
        }catch (Exception e){
            errorMessage =e.getMessage();
            return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/deleteProduct")
    public @ResponseBody ResponseEntity deleteAccount(@RequestBody List<String> code, HttpServletRequest request){

        boolean department = this.getSession(request);
        if(!department){
            return new ResponseEntity<String>("삭제 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        productService.deleteAccount(code);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 견적서 등록 페이지 거래처 선택시 거래처와 동일한 상품 LIST를 가져옵니다.
    @PostMapping(value = "/productList")
    public @ResponseBody ResponseEntity AccountInfoPrList(@RequestBody String acCode){

        Account accounts = accountService.findByAcCode(acCode);

        List<Product> products = productService.findByAccout(acCode);

        List<ProductFormDto> productFormDtos = new ArrayList<ProductFormDto>();

        for (Product product: products) {
            ProductFormDto productFormDto = ProductFormDto.of(product);
            productFormDtos.add(productFormDto);
        }
        return new ResponseEntity<>(productFormDtos,HttpStatus.OK);
    }

    // 견적서 등록 페이지 거래처 선택시 상품 정보를 가져옵니다.
    @PostMapping(value = "/productInfo")
    public @ResponseBody ResponseEntity productInfo(@RequestBody String prCode){

        Product product = productService.findByCode(prCode);
        ProductFormDto productFormDto = ProductFormDto.of(product);

        return new ResponseEntity<>(productFormDto,HttpStatus.OK);
    }
    public boolean getSession(HttpServletRequest request){

        HttpSession session = request.getSession();
        UserDto user = (UserDto) session.getAttribute("User");

        boolean department = false;

        if(user.getDepartment().equals("구매팀") || user.getDepartment().equals("영업팀")) {
            department = true;
            return department;
        }else {
            return department;
        }
    }
}
