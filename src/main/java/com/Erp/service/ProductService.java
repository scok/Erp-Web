package com.Erp.service;

import com.Erp.dto.ProductAddDto;
import com.Erp.dto.ProductFormDto;
import com.Erp.entity.Account;
import com.Erp.entity.Product;
import com.Erp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    //상품 정보 전부를 가져옵니다.
    public List<ProductFormDto> productListAll(){
        List<Product> productList = productRepository.findAll();
        List<ProductFormDto> productFormDto = new ArrayList<ProductFormDto>();


        for(Product product : productList){
            ProductFormDto dto = ProductFormDto.of(product);
            productFormDto.add(dto);
        }

        return productFormDto;
    }

    //제품 정보 전부를 가져옵니다.
    public List<ProductFormDto> provideList(){
        List<Product> productList = productRepository.findByProvideAll();
        List<ProductFormDto> productFormDto = new ArrayList<ProductFormDto>();

        for(Product product : productList){
            ProductFormDto dto = ProductFormDto.of(product);
            productFormDto.add(dto);
        }

        return productFormDto;
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public Product findByCode(String code) {
        return productRepository.findById(code).orElseThrow(EntityNotFoundException::new);
    }

    //이렇게 하는 이유는 SPA 업데이트 문을 실행할때 SAVE 메소드를 이용할경우 데이터 베이스에 두번 조회를 하기 때문에
    //자원 낭비를 줄이고자  @Transactional를 이용하여 변경된 필드를 감지하여 DB에 커밋을 해줍니다.
    @Transactional
    public void updateProduct(ProductAddDto dto, Account account) {

        Product target = productRepository.findById(dto.getPrCode()).orElseThrow(EntityNotFoundException::new);
        target.setAccount(account);
        target.setPrName(dto.getPrName());
        target.setPrPrice(dto.getPrPrice());
        target.setPrStandard(dto.getPrStandard());
        target.setPrDivCategory(dto.getPrDivCategory());
        target.setPrCategory(dto.getPrCategory());
    }

    //상품 삭제 기능
    @Transactional
    public void deleteAccount(List<String> code) {
        for(String item : code) {
            if (item != null) {
                Product product = productRepository.findById(item).orElseThrow(EntityNotFoundException::new);
                product.setPageYandN("N");
            }
        }
    }

    //같은 거래처 정보를 담고 있는 제품 모두를 가져옵니다.
    public List<Product> findByAccout(String acCode) {

        return productRepository.findProductByAccount(acCode);
    }

    public int productListCount(int year, int month, int day) {
        return productRepository.getProductCount(year,month,day);
    }
}
