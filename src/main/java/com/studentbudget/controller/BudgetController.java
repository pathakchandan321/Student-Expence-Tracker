package com.studentbudget.controller;

import com.studentbudget.model.Expense;
import com.studentbudget.model.MonthlyBudget;
import com.studentbudget.model.RecurringExpense;
import com.studentbudget.service.BudgetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;

@Controller
public class BudgetController {
    private final BudgetService budgetService;
    private final String[] categories = {"Rent", "Food", "Travel", "Clothes", "Recharge", "Study", "Entertainment", "Others"};
    public BudgetController(BudgetService budgetService) { this.budgetService = budgetService; }

    @GetMapping("/")
    public String dashboard(@RequestParam(required = false) String month, Model model) {
        month = month == null ? budgetService.getCurrentMonth() : month;
        MonthlyBudget budget = budgetService.getBudget(month);
        BigDecimal total = budgetService.getTotalSpent(month);
        BigDecimal amount = budget == null ? BigDecimal.ZERO : budget.getBudget();
        model.addAttribute("month", month); model.addAttribute("budget", amount);
        model.addAttribute("spent", total); model.addAttribute("remaining", amount.subtract(total));
        model.addAttribute("recurringSpent", budgetService.getRecurringTotal());
        model.addAttribute("smartPlan", budgetService.getSmartPlan(amount.subtract(total)));
        long daysLeft = budgetService.getDaysLeft(month);
        BigDecimal dailyLimit = daysLeft == 0 ? BigDecimal.ZERO : amount.subtract(total).max(BigDecimal.ZERO).divide(BigDecimal.valueOf(daysLeft), 2, RoundingMode.HALF_UP);
        model.addAttribute("daysLeft", daysLeft); model.addAttribute("dailyLimit", dailyLimit);
        return "dashboard";
    }

    @GetMapping("/budget")
    public String budgetForm(@RequestParam(required = false) String month, Model model) {
        month = month == null ? budgetService.getCurrentMonth() : month;
        MonthlyBudget budget = budgetService.getBudget(month);
        if (budget == null) { budget = new MonthlyBudget(); budget.setMonth(month); }
        model.addAttribute("budget", budget); return "budget-form";
    }

    @PostMapping("/budget")
    public String saveBudget(@ModelAttribute MonthlyBudget budget) {
        budgetService.saveBudget(budget); return "redirect:/?month=" + budget.getMonth();
    }

    @GetMapping("/expenses/add")
    public String expenseForm(Model model) {
        Expense expense = new Expense(); expense.setDate(LocalDate.now());
        model.addAttribute("expense", expense); model.addAttribute("categories", categories);
        return "expense-form";
    }

    @PostMapping("/expenses")
    public String saveExpense(@ModelAttribute Expense expense) {
        budgetService.saveExpense(expense); return "redirect:/expenses?month=" + YearMonth.from(expense.getDate());
    }

    @GetMapping("/expenses")
    public String expenses(@RequestParam(required = false) String month, Model model) {
        month = month == null ? budgetService.getCurrentMonth() : month;
        model.addAttribute("month", month); model.addAttribute("expenses", budgetService.getExpenses(month));
        return "expense-history";
    }

    @PostMapping("/expenses/{id}/delete")
    public String deleteExpense(@PathVariable Long id, @RequestParam String month) {
        budgetService.deleteExpense(id); return "redirect:/expenses?month=" + month;
    }

    @GetMapping("/recurring")
    public String recurringForm(Model model) {
        model.addAttribute("recurring", new RecurringExpense());
        model.addAttribute("recurringExpenses", budgetService.getRecurringExpenses());
        model.addAttribute("categories", categories); return "recurring-expenses";
    }

    @PostMapping("/recurring")
    public String saveRecurring(@ModelAttribute RecurringExpense recurring) {
        budgetService.saveRecurringExpense(recurring); return "redirect:/recurring";
    }

    @PostMapping("/recurring/{id}/delete")
    public String deleteRecurring(@PathVariable Long id) {
        budgetService.deleteRecurringExpense(id); return "redirect:/recurring";
    }
}
