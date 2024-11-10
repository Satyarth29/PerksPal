package com.infy.retail.perkspal.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "REWARD")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Reward {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "POINTS")
    private Integer points;
    @Column(name = "DATE")
    private LocalDate date;
    @Column(name = "CUSTOMER")
    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;
}
