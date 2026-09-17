package com.studentbudget.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "recurring_expense")
public class RecurringExpense {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 30)
    private String category;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;
    public Long getId() { return id; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public AppUser getUser() { return user; }
    public void setUser(AppUser user) { this.user = user; }
}
