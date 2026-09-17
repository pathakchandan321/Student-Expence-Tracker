package com.studentbudget.repository;

import com.studentbudget.model.RecurringExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import com.studentbudget.model.AppUser;
import java.util.List;
import java.util.Optional;

public interface RecurringExpenseRepository extends JpaRepository<RecurringExpense, Long> {
    List<RecurringExpense> findByUser(AppUser user);
    Optional<RecurringExpense> findByIdAndUser(Long id, AppUser user);
}
