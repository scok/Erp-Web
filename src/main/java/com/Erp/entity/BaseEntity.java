package com.Erp.entity;

import com.Erp.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@EntityListeners(value={AuditingEntityListener.class})
@MappedSuperclass
@Getter@Setter
public abstract class BaseEntity extends BaseTimeEntity {

    @CreatedBy //엔터티 생성자 사용자의 id를 기록할게요.
    private String createBy; // 생성자

    @LastModifiedBy //엔터티 수정시 수정자의 id를 기록할게요.
    private String modifieBy; //수정자
    


}
