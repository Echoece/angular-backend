package com.echo.backend.service.expense;


import com.echo.backend.dto.expense.ExpenseFilter;
import com.echo.backend.entity.expense.Category;
import com.echo.backend.exception.customException.ApiSystemException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Category save(Category payload) throws ApiSystemException;
    Category update(Category payload, Long id) throws ApiSystemException;
    Category updatePartial(Category payload, Long id) throws ApiSystemException;
    void delete(Long id) throws ApiSystemException;
    Page<Category> findAll(Pageable pageable, ExpenseFilter filter);
    Category findById(Long id) throws ApiSystemException;

}
