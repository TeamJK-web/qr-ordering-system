package com.restaurant.ordering.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name = "payments")
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @OneToOne(optional = false) @JoinColumn(name = "order_id", unique = true) private Order order;
    @ManyToOne(optional = false) @JoinColumn(name = "cashier_id") private Employee cashier;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Method method;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal amount;
    @Column(name = "reference_no", length = 100) private String referenceNo;
    @Column(nullable = false) private LocalDateTime paidAt = LocalDateTime.now();
    public enum Method { CASH, GCASH, CARD }
    protected Payment() {}
    public Payment(Order order, Employee cashier, Method method, BigDecimal amount, String referenceNo) { this.order=order; this.cashier=cashier; this.method=method; this.amount=amount; this.referenceNo=referenceNo; }
    public LocalDateTime getPaidAt(){return paidAt;} public BigDecimal getAmount(){return amount;}
}
