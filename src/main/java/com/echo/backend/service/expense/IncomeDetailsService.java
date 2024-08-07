package com.echo.backend.service.expense;


import com.echo.backend.dto.expense.ExpenseFilter;
import com.echo.backend.entity.expense.IncomeDetails;
import com.echo.backend.exception.customException.ApiSystemException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IncomeDetailsService {
    IncomeDetails save(IncomeDetails payload) throws ApiSystemException;
    IncomeDetails update(IncomeDetails payload, Long id) throws ApiSystemException;
    IncomeDetails updatePartial(IncomeDetails payload, Long id) throws ApiSystemException;
    void delete(Long id) throws ApiSystemException;
    Page<IncomeDetails> findAll(Pageable pageable, ExpenseFilter filter);
    IncomeDetails findById(Long id) throws ApiSystemException;

}
