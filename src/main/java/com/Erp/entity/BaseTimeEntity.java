package com.Erp.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(value={AuditingEntityListener.class})
@MappedSuperclass
@Getter@Setter
public abstract class BaseTimeEntity {
    
    @CreatedDate //엔터티 생성시 자동으로 시간 기록
    @Column(updatable = false)  // Enetity 수정시 갱신 안함
    private LocalDateTime regDate; // 생성 시간

    @LastModifiedDate //엔터니 수정시 자동으로 시간 기록
    private LocalDateTime updateDate; // 수정 시간

}
