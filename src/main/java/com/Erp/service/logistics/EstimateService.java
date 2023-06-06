package com.Erp.service.logistics;

import com.Erp.constant.AccountCategory;
import com.Erp.constant.DivisionStatus;
import com.Erp.dto.logistics.EstimateAddmDto;
import com.Erp.dto.logistics.EstimateDetailAddmDto;
import com.Erp.dto.logistics.EstimateFormDto;
import com.Erp.dto.logistics.OrderSheetFormDto;
import com.Erp.entity.logistics.Estimate;
import com.Erp.entity.logistics.EstimateDetail;
import com.Erp.entity.logistics.OrderSheet;
import com.Erp.entity.logistics.Product;
import com.Erp.repository.logistics.EstimateDetailRepository;
import com.Erp.repository.logistics.EstimateRepository;
import com.Erp.service.logistics.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EstimateService {

    private final EstimateRepository estimateRepository;
    private final EstimateDetailRepository estimateDetailRepository;
    private final ProductService productService;
    private final EntityManager entityManager;

    //구매처 전부를 조회 합니다.
    public List<EstimateFormDto> estimateListBuy() {
        List<Estimate> estimates = estimateRepository.findBuyEstimate();
        List<EstimateFormDto> estimateFormDtos = new ArrayList<EstimateFormDto>();
        for (Estimate estimate : estimates){
            EstimateFormDto dto = EstimateFormDto.of(estimate);
            estimateFormDtos.add(dto);
        }
        return estimateFormDtos;
    }
    //판매처 전부를 조회 합니다.
    public List<EstimateFormDto> estimateListSeller() {
        List<Estimate> estimates = estimateRepository.findSellerEstimate();
        List<EstimateFormDto> estimateFormDtos = new ArrayList<EstimateFormDto>();
        for (Estimate estimate : estimates){
            EstimateFormDto dto = EstimateFormDto.of(estimate);
            estimateFormDtos.add(dto);
        }
        return estimateFormDtos;
    }

    //견적서를 생성할 때 견적서 코드에 사용할 카운터를 얻습니다.
    public int getEstimateCount(int year,int month,int day) {
        return estimateRepository.getEstimateCount(year,month,day);
    }

    //견적서 디테일 데이터를 가져옵니다.
    public EstimateDetail findById(Long id) {
        return estimateDetailRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    //견적서 저장
    public void estimateSave(Estimate estimate) {
        estimateRepository.save(estimate);
    }

    //견적서 디테일 저장
    public List<EstimateDetail> listSave(List<EstimateDetail> estimateDetailList) {
        List<EstimateDetail> esdList = new ArrayList<EstimateDetail>();
        try {
            for (EstimateDetail estimateDetail: estimateDetailList) {
                EstimateDetail savedEstimateDetail = estimateDetailRepository.save(estimateDetail);
                esdList.add(savedEstimateDetail);
            }
            entityManager.flush();
            entityManager.clear();
            return esdList;
        } catch (Exception e) {
            throw new RuntimeException("견적서 디테일 저장 실패", e);
        }
    }
    //견적서 디테일 1건 저장
    public EstimateDetail estimateDetailSave(EstimateDetail estimateDetail) {
        return estimateDetailRepository.save(estimateDetail);
    }

    //견적서 찾기
    public Estimate findByCode(String code) {
        return estimateRepository.findById(code).orElseThrow(EntityNotFoundException::new);
    }

    //견적서 업데이트 기능
    public Estimate estimateUpdate(EstimateAddmDto estimateAddmDto,List<EstimateDetail> estimateDetailList) {
        Estimate estimate = estimateRepository.findById(estimateAddmDto.getEsCode()).orElseThrow(EntityNotFoundException::new);

        estimate.setEsCode(estimateAddmDto.getEsCode());
        estimate.setEsTotalPrice(estimateAddmDto.getEsTotalPrice());
        estimate.setAcCategory(estimateAddmDto.getAcCategory());
        if(estimateAddmDto.getDivisionStatus() == null){
            estimate.setDivisionStatus(DivisionStatus.승인대기);
        }else {
            estimate.setDivisionStatus(estimateAddmDto.getDivisionStatus());
        }
        estimate.setEsComment(estimateAddmDto.getEsComment());
        estimate.setEstimateDetail(estimateDetailList);

        return estimate;
    }

    //견적서 디테일 업데이트 기능
    public EstimateDetail estimateDetailUpdate(EstimateDetailAddmDto estimateDetailAddmDto, Product product) {

        EstimateDetail estimateDetail = estimateDetailRepository.getReferenceById(estimateDetailAddmDto.getEsdId());

        estimateDetail.setEsQuantity(estimateDetailAddmDto.getEsQuantity());
        estimateDetail.setEsStandard(estimateDetailAddmDto.getEsStandard());
        estimateDetail.setEsSupplyValue(estimateDetailAddmDto.getEsSupplyValue());
        estimateDetail.setEsTaxAmount(estimateDetailAddmDto.getEsTaxAmount());
        estimateDetail.setProduct(product);

        return estimateDetail;

    }

    public void deleteEstimate(List<String> code) {
        for (String estimateCode : code){
           Estimate estimate =  estimateRepository.findById(estimateCode).orElseThrow(EntityNotFoundException::new);
           estimate.setPageYandN("N");
        }
    }

    //견적서를 필터링하여 조회합니다.
    public List<EstimateFormDto> estimateListFilter(String acCategory, String divCategory) {

        List<Estimate> buyOrderSheets = estimateRepository.findBuyEstimateFilter(AccountCategory.valueOf(acCategory),DivisionStatus.valueOf(divCategory));
        List<EstimateFormDto> estimateFormDtos = new ArrayList<EstimateFormDto>();
        for (Estimate estimate : buyOrderSheets){
            EstimateFormDto dto = EstimateFormDto.of(estimate);
            estimateFormDtos.add(dto);
        }
        return estimateFormDtos;
    }
}
