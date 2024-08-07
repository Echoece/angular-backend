package com.echo.backend.dto.expense;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseFilter {
    private Long categoryId;
    private String categoryName;

    private LocalDate toDate;
    private LocalDate fromDate;

    private Long incomeId;
    private String incomeName;
    private String incomeAmount;
    private Boolean incomeReceived;

    private Long expenseId;
    private String expenseName;
    private String expenseAmount;
    private Boolean expensePaid;
    private LocalDate expenseDueDate;
    private LocalDate expensePaymentDate;

}
