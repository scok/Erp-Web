package com.Erp.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(value={AuditingEntityListener.class})
@MappedSuperclass
@Getter
@Setter
public class DistributionEntity {

    @CreatedBy //엔터티 생성자 사용자의 id를 기록할게요.
    private String createBy; // 생성자

    @LastModifiedBy //엔터티 수정시 수정자의 id를 기록할게요.
    private String modifieBy; //수정자

    @CreatedDate //엔터티 생성시 자동으로 시간 기록
    @Column(updatable = false)  // Enetity 수정시 나는 갱신 안 할래요
    private LocalDateTime regDate;

    @LastModifiedDate //엔터니 수정시 자동으로 시간 기록
    private LocalDateTime updateDate;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'Y'")//페이지 표시 여부 기본값 : Y 사용자가 삭제 요청시 페이지에서만 제외 처리 됩니다.
    private String pageYandN;

    private String createName;  //작성자 이름을 표기합니다.

}
