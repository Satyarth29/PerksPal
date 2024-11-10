package com.infy.retail.perkspal.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Column(name = "CUSTOMER")
    private Customer customer;
    @Column(name = "PRICE($)")
    private Double price;
    @Column(name = "DATE")
    private LocalDate date;

}
