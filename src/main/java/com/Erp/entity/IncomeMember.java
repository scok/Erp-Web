package com.Erp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "incomeMember")
@Getter @Setter
public class IncomeMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "income_id")
    private Income income;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
