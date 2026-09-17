package com.studentbudget.repository;

import com.studentbudget.model.MonthlyBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.studentbudget.model.AppUser;

public interface MonthlyBudgetRepository extends JpaRepository<MonthlyBudget, Long> {
    Optional<MonthlyBudget> findByMonthAndUser(String month, AppUser user);
}
