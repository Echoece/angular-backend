package com.echo.backend.service.expense;

import com.echo.backend.dto.expense.ExpenseFilter;
import com.echo.backend.entity.expense.Category;
import com.echo.backend.exception.customException.ApiSystemException;
import com.echo.backend.repository.expense.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.echo.backend.utility.Utility.copyNonNullProperties;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category save(Category payload) throws ApiSystemException {
        if (Objects.nonNull(payload.getId()))
            throw new ApiSystemException("cant have id");

        return categoryRepository.save(payload);
    }

    @Override
    public Category update(Category payload, Long id) throws ApiSystemException {
        try{
            payload.setId(id);
            return categoryRepository.save(payload);
        } catch (Exception e) {
            throw new ApiSystemException("system error");
        }
    }

    @Override
    public Category updatePartial(Category payload, Long id) throws ApiSystemException {
        try{
            Category savedEntity = findById(id);
            copyNonNullProperties(payload, savedEntity);
            return categoryRepository.save(savedEntity);
        } catch (Exception e) {
            throw new ApiSystemException("system error");
        }
    }

    @Override
    public void delete(Long id) throws ApiSystemException {
        Category savedEntity = findById(id);
        categoryRepository.delete(savedEntity);
    }

    @Override
    public Page<Category> findAll(Pageable pageable, ExpenseFilter filter) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Category findById(Long id) throws ApiSystemException {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new ApiSystemException("wrong id"));
    }
}
