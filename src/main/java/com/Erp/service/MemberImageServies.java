package com.Erp.service;

import com.Erp.entity.MemberImage;
import com.Erp.repository.MemberImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
public class MemberImageServies {

    @Value("${MemberImageLocation}")
    private String memberImageLocation;

    private final MemberImageRepository memberImageRepository;
    private final FileService fileService;

    //상품 이미지 정보 저장
    public void saveImage(MemberImage memberImage, MultipartFile uploadedFile) throws Exception {

        String oriImageName = uploadedFile.getOriginalFilename();
        String imageName = null;
        String imageUrl = null;

        System.out.println("MemberImageLocation : " + memberImageLocation);

        //파일 업로드
        if(!StringUtils.isEmpty(oriImageName)){//이름이 있으면 업로드 하자.
            imageName=fileService.uploadFile(memberImageLocation,oriImageName,uploadedFile.getBytes());
            imageUrl="/images/product/" + imageName;
            System.out.println("imageUrl : " +imageUrl);
        }
        memberImage.updateMemberImage(oriImageName,imageName,imageUrl);
        memberImageRepository.save(memberImage);
    }
}
