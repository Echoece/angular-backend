package com.echo.backend.service;

import com.echo.backend.dto.PatientFilter;
import com.echo.backend.entity.Patient;
import com.echo.backend.entity.auth.Users;
import com.echo.backend.exception.customException.ApiBadRequestException;
import com.echo.backend.exception.customException.ApiSystemException;
import com.echo.backend.repository.PatientRepository;
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
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;

    @Override
    public Patient save(Patient payload, Users user) throws ApiSystemException {
        if (Objects.nonNull(payload.getId()))
            throw new ApiSystemException("cant have id");

        payload.setDoctor(user);

        return patientRepository.save(payload);
    }

    @Override
    public Patient update(Patient payload, Long id) throws ApiSystemException {
        try{
            payload.setId(id);
            return patientRepository.save(payload);
        } catch (Exception e) {
            throw new ApiSystemException("system error");
        }
    }

    @Override
    public Patient updatePartial(Patient payload, Long id) throws ApiSystemException {
        try{
            Patient savedEntity = patientRepository.findById(id)
                            .orElseThrow(()-> new ApiBadRequestException("wrong id"));
            copyNonNullProperties(payload, savedEntity);
            return patientRepository.save(savedEntity);
        } catch (Exception e) {
            throw new ApiSystemException("system error");
        }
    }

    @Override
    public void delete(Long id) throws ApiSystemException {
        Patient savedEntity = findById(id);
        patientRepository.delete(savedEntity);
    }

    @Override
    public Page<Patient> findAll(Pageable pageable, PatientFilter filter) {
        return patientRepository.findAll(pageable);
    }

    @Override
    public Patient findById(Long id) throws ApiSystemException {
        return patientRepository.findById(id)
                .orElseThrow(()-> new ApiSystemException("wrong id"));
    }
}
