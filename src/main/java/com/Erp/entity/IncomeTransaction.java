package com.Erp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "incomeTransaction")
@Getter @Setter
public class IncomeTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "income_id")
    private Income income;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;
}
