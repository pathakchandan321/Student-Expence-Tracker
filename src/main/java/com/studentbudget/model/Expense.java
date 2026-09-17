package com.studentbudget.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expense")
public class Expense {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;
    @Column(nullable = false, length = 30)
    private String category;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "date", nullable = false)
    private LocalDate date;
    @Column(length = 250)
    private String note;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;
    public Long getId() { return id; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public AppUser getUser() { return user; }
    public void setUser(AppUser user) { this.user = user; }
}
