package com.echo.backend.repository.expense;

import com.echo.backend.entity.expense.ExpenseDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseDetailsRepository extends JpaRepository<ExpenseDetails, Long> {
}
