package com.Erp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "member_image")
@Getter@Setter
public class MemberImage extends BaseEntity {

    @Id
    @Column(name = "member_image_id")
    @GeneratedValue(strategy = GenerationType.AUTO) // 자동으로 id를 만들어줌
    private Long id;

    private String imageName; // 이미지 이름(UUID)
    private String orImageName; // 원본 이미지 이름
    private String imageUrl; //이미지 저장 주소


    @OneToOne(fetch = FetchType.LAZY) // Member 일대일 매핑
    @JoinColumn(name = "memberId") // memberId(사번)과 조인하여 컬럼 생성
    private Member member;

    public void updateMemberImage(String orImageName, String imageName, String imageUrl, Member member) {
        this.orImageName = orImageName;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.member = member;
    }

}
