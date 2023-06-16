package com.Erp.service;

import com.Erp.entity.Member;
import com.Erp.entity.MemberImage;
import com.Erp.repository.MemberImageRepository;
import com.Erp.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
public class MemberImageService {

    @Value("${MemberImageLocation}")
    private String memberImageLocation;

    private final MemberRepository memberRepository;
    private final MemberImageRepository memberImageRepository;
    private final FileService fileService;


    //상품 이미지 정보 저장
    public void saveImage(MemberImage memberImage, MultipartFile uploadedFile, Member member) throws Exception {

        String oriImageName = uploadedFile.getOriginalFilename();
        String imageName = null;
        String imageUrl = null;

        System.out.println("MemberImageLocation : " + memberImageLocation);

        //파일 업로드
        if(!StringUtils.isEmpty(oriImageName)){//이름이 있으면 업로드 하자.
            imageName=fileService.uploadFile(memberImageLocation,oriImageName,uploadedFile.getBytes());
            imageUrl="/images/member/" + imageName;
            System.out.println("imageUrl : " +imageUrl);
        }

        memberImage.updateMemberImage(oriImageName,imageName,imageUrl, member);
        memberImageRepository.save(memberImage);
    }

    //상품 이미지 정보 수정
    public void updateImage(MemberImage memberImage, MultipartFile uploadedFile, Member member, String id) throws Exception {

        memberImageRepository.deleteByMemberId(id);

        String oriImageName = uploadedFile.getOriginalFilename();
        String imageName = null;
        String imageUrl = null;

        System.out.println("MemberImageLocation : " + memberImageLocation);

        //파일 업로드
        if(!StringUtils.isEmpty(oriImageName)){//이름이 있으면 업로드 하자.
            imageName=fileService.uploadFile(memberImageLocation,oriImageName,uploadedFile.getBytes());
            imageUrl="/images/member/" + imageName;
            System.out.println("imageUrl : " +imageUrl);
        }

        memberImage.updateMemberImage(oriImageName,imageName,imageUrl, member);
        memberImageRepository.save(memberImage);
    }
}