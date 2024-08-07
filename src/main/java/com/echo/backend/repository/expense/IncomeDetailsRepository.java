package com.echo.backend.repository.expense;

import com.echo.backend.entity.expense.IncomeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeDetailsRepository extends JpaRepository<IncomeDetails, Long> {
}
