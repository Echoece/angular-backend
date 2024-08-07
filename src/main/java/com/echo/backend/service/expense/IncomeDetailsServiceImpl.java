package com.echo.backend.service.expense;

import com.echo.backend.dto.expense.ExpenseFilter;
import com.echo.backend.entity.expense.IncomeDetails;
import com.echo.backend.exception.customException.ApiSystemException;
import com.echo.backend.repository.expense.IncomeDetailsRepository;
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
public class IncomeDetailsServiceImpl implements IncomeDetailsService {
    private final IncomeDetailsRepository incomeDetailsRepository;

    @Override
    public IncomeDetails save(IncomeDetails payload) throws ApiSystemException {
        if (Objects.nonNull(payload.getId()))
            throw new ApiSystemException("cant have id");

        return incomeDetailsRepository.save(payload);
    }

    @Override
    public IncomeDetails update(IncomeDetails payload, Long id) throws ApiSystemException {
        try{
            payload.setId(id);
            return incomeDetailsRepository.save(payload);
        } catch (Exception e) {
            throw new ApiSystemException("system error");
        }
    }

    @Override
    public IncomeDetails updatePartial(IncomeDetails payload, Long id) throws ApiSystemException {
        try{
            IncomeDetails savedEntity = findById(id);
            copyNonNullProperties(payload, savedEntity);
            return incomeDetailsRepository.save(savedEntity);
        } catch (Exception e) {
            throw new ApiSystemException("system error");
        }
    }

    @Override
    public void delete(Long id) throws ApiSystemException {
        IncomeDetails savedEntity = findById(id);
        incomeDetailsRepository.delete(savedEntity);
    }

    @Override
    public Page<IncomeDetails> findAll(Pageable pageable, ExpenseFilter filter) {
        return incomeDetailsRepository.findAll(pageable);
    }

    @Override
    public IncomeDetails findById(Long id) throws ApiSystemException {
        return incomeDetailsRepository.findById(id)
                .orElseThrow(()-> new ApiSystemException("wrong id"));
    }
}
