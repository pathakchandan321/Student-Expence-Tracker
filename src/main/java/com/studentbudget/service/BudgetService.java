package com.studentbudget.service;

import com.studentbudget.model.Expense;
import com.studentbudget.model.MonthlyBudget;
import com.studentbudget.model.RecurringExpense;
import com.studentbudget.model.SmartPlan;
import com.studentbudget.repository.ExpenseRepository;
import com.studentbudget.repository.MonthlyBudgetRepository;
import com.studentbudget.repository.RecurringExpenseRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.ArrayList;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

@Service
public class BudgetService {
    private final MonthlyBudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;
    private final RecurringExpenseRepository recurringRepository;
    private final AppUserService userService;
    public BudgetService(MonthlyBudgetRepository budgetRepository, ExpenseRepository expenseRepository, RecurringExpenseRepository recurringRepository, AppUserService userService) {
        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
        this.recurringRepository = recurringRepository;
        this.userService = userService;
    }
    public void saveBudget(MonthlyBudget budget) {
        budgetRepository.findByMonthAndUser(budget.getMonth(), userService.getLoggedInUser()).ifPresent(old -> budget.setId(old.getId()));
        budget.setUser(userService.getLoggedInUser());
        budgetRepository.save(budget);
    }
    public MonthlyBudget getBudget(String month) { return budgetRepository.findByMonthAndUser(month, userService.getLoggedInUser()).orElse(null); }
    public List<Expense> getExpenses(String month) {
        YearMonth value = YearMonth.parse(month);
        return expenseRepository.findByUserAndDateBetweenOrderByDateDesc(userService.getLoggedInUser(), value.atDay(1), value.atEndOfMonth());
    }
    public BigDecimal getTotalSpent(String month) {
        return getExpenseTotal(month).add(getRecurringTotal());
    }
    public BigDecimal getExpenseTotal(String month) { return getExpenses(month).stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add); }
    public BigDecimal getRecurringTotal() { return recurringRepository.findByUser(userService.getLoggedInUser()).stream().map(RecurringExpense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add); }
    public void saveExpense(Expense expense) { expense.setUser(userService.getLoggedInUser()); expenseRepository.save(expense); }
    public void deleteExpense(Long id) { expenseRepository.findByIdAndUser(id, userService.getLoggedInUser()).ifPresent(expenseRepository::delete); }
    public String getCurrentMonth() { return YearMonth.now().toString(); }
    public long getDaysLeft(String month) {
        LocalDate today = LocalDate.now(); LocalDate end = YearMonth.parse(month).atEndOfMonth();
        return today.isAfter(end) ? 0 : ChronoUnit.DAYS.between(today, end) + 1;
    }
    public List<RecurringExpense> getRecurringExpenses() { return recurringRepository.findByUser(userService.getLoggedInUser()); }
    public void saveRecurringExpense(RecurringExpense expense) { expense.setUser(userService.getLoggedInUser()); recurringRepository.save(expense); }
    public void deleteRecurringExpense(Long id) { recurringRepository.findByIdAndUser(id, userService.getLoggedInUser()).ifPresent(recurringRepository::delete); }
    public SmartPlan getSmartPlan(BigDecimal remaining) {
        if (remaining.signum() <= 0) return new SmartPlan(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, List.of("First reduce spending or add budget. Investment suggestions start after the balance is positive."));
        BigDecimal investment = percentage(remaining, 5); BigDecimal travel = isInactive("Travel", 60) ? percentage(remaining, 15) : BigDecimal.ZERO;
        BigDecimal clothes = isInactive("Clothes", 90) ? percentage(remaining, 10) : BigDecimal.ZERO;
        List<String> messages = getPlanMessages(travel, clothes);
        return new SmartPlan(investment, travel, clothes, remaining.subtract(investment).subtract(travel).subtract(clothes), messages);
    }
    private boolean isInactive(String category, int days) { return expenseRepository.findFirstByUserAndCategoryOrderByDateDesc(userService.getLoggedInUser(), category).map(e -> e.getDate().isBefore(LocalDate.now().minusDays(days))).orElse(true); }
    private BigDecimal percentage(BigDecimal amount, int percent) { return amount.multiply(BigDecimal.valueOf(percent)).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP); }
    private List<String> getPlanMessages(BigDecimal travel, BigDecimal clothes) {
        List<String> messages = new ArrayList<>();
        if (travel.signum() > 0) messages.add("Travel has not appeared in your recent expense history. You may keep a small travel or outing budget.");
        if (clothes.signum() > 0) messages.add("Clothes have not appeared recently. If needed, reserve a limited amount instead of spending suddenly.");
        if (messages.isEmpty()) messages.add("Your recent spending is active in travel and clothes, so this plan keeps the balance mostly for savings and emergencies.");
        return messages;
    }
}
