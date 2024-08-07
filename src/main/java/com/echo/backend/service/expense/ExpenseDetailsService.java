package com.echo.backend.service.expense;


import com.echo.backend.dto.expense.ExpenseFilter;
import com.echo.backend.entity.expense.ExpenseDetails;
import com.echo.backend.exception.customException.ApiSystemException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseDetailsService {
    ExpenseDetails save(ExpenseDetails payload) throws ApiSystemException;
    ExpenseDetails update(ExpenseDetails payload, Long id) throws ApiSystemException;
    ExpenseDetails updatePartial(ExpenseDetails payload, Long id) throws ApiSystemException;
    void delete(Long id) throws ApiSystemException;
    Page<ExpenseDetails> findAll(Pageable pageable, ExpenseFilter filter);
    ExpenseDetails findById(Long id) throws ApiSystemException;

}
