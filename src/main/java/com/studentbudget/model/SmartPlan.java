package com.studentbudget.model;

import java.math.BigDecimal;
import java.util.List;

public class SmartPlan {
    private final BigDecimal investmentAmount;
    private final BigDecimal travelAmount;
    private final BigDecimal clothesAmount;
    private final BigDecimal saveAmount;
    private final List<String> messages;
    public SmartPlan(BigDecimal investmentAmount, BigDecimal travelAmount, BigDecimal clothesAmount, BigDecimal saveAmount, List<String> messages) {
        this.investmentAmount = investmentAmount; this.travelAmount = travelAmount;
        this.clothesAmount = clothesAmount; this.saveAmount = saveAmount; this.messages = messages;
    }
    public BigDecimal getInvestmentAmount() { return investmentAmount; }
    public BigDecimal getTravelAmount() { return travelAmount; }
    public BigDecimal getClothesAmount() { return clothesAmount; }
    public BigDecimal getSaveAmount() { return saveAmount; }
    public List<String> getMessages() { return messages; }
}
