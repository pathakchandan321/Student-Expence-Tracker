package com.studentbudget.repository;

import com.studentbudget.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.studentbudget.model.AppUser;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserAndDateBetweenOrderByDateDesc(AppUser user, LocalDate start, LocalDate end);
    Optional<Expense> findFirstByUserAndCategoryOrderByDateDesc(AppUser user, String category);
    Optional<Expense> findByIdAndUser(Long id, AppUser user);
}
