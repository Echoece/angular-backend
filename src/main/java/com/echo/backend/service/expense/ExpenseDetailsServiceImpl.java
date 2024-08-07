package com.echo.backend.service.expense;

import com.echo.backend.dto.expense.ExpenseFilter;
import com.echo.backend.entity.expense.ExpenseDetails;
import com.echo.backend.exception.customException.ApiSystemException;
import com.echo.backend.repository.expense.ExpenseDetailsRepository;
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
public class ExpenseDetailsServiceImpl implements ExpenseDetailsService {
    private final ExpenseDetailsRepository expenseDetailsRepository;

    @Override
    public ExpenseDetails save(ExpenseDetails payload) throws ApiSystemException {
        if (Objects.nonNull(payload.getId()))
            throw new ApiSystemException("cant have id");

        return expenseDetailsRepository.save(payload);
    }

    @Override
    public ExpenseDetails update(ExpenseDetails payload, Long id) throws ApiSystemException {
        try{
            payload.setId(id);
            return expenseDetailsRepository.save(payload);
        } catch (Exception e) {
            throw new ApiSystemException("system error");
        }
    }

    @Override
    public ExpenseDetails updatePartial(ExpenseDetails payload, Long id) throws ApiSystemException {
        try{
            ExpenseDetails savedEntity = findById(id);
            copyNonNullProperties(payload, savedEntity);
            return expenseDetailsRepository.save(savedEntity);
        } catch (Exception e) {
            throw new ApiSystemException("system error");
        }
    }

    @Override
    public void delete(Long id) throws ApiSystemException {
        ExpenseDetails savedEntity = findById(id);
        expenseDetailsRepository.delete(savedEntity);
    }

    @Override
    public Page<ExpenseDetails> findAll(Pageable pageable, ExpenseFilter filter) {
        return expenseDetailsRepository.findAll(pageable);
    }

    @Override
    public ExpenseDetails findById(Long id) throws ApiSystemException {
        return expenseDetailsRepository.findById(id)
                .orElseThrow(()-> new ApiSystemException("wrong id"));
    }
}
