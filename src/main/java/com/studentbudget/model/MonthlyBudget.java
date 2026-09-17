package com.studentbudget.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "monthly_budget", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "month"}))
public class MonthlyBudget {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "month", nullable = false, length = 7)
    private String month;
    @Column(name = "budget", nullable = false, precision = 12, scale = 2)
    private BigDecimal budget;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }
    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }
    public AppUser getUser() { return user; }
    public void setUser(AppUser user) { this.user = user; }
}
