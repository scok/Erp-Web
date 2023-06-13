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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imageName;       //이미지 이름(UUID)
    private String orImageName;     //원본 이미지 이름
    private String imageUrl;        //이미지 조회 경로


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    public void updateMemberImage(String orImageName, String imageName, String imageUrl, Member member) {
        this.orImageName = orImageName;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.member = member;
    }

}
