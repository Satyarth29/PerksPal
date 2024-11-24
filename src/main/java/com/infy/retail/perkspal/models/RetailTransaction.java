package com.infy.retail.perkspal.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Entity
@Table(name = "TRANSACTION")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RetailTransaction {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;
    @Column(name = "PRICE($)")
    private Double price;
    @Column(name = "DATE")
    private LocalDate date;

}
