package com.echo.backend.service;


import com.echo.backend.dto.PatientFilter;
import com.echo.backend.entity.Patient;
import com.echo.backend.entity.auth.Users;
import com.echo.backend.exception.customException.ApiSystemException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientService {
    Patient save(Patient payload, Users user) throws ApiSystemException;
    Patient update(Patient payload, Long id) throws ApiSystemException;
    Patient updatePartial(Patient payload, Long id) throws ApiSystemException;
    void delete(Long id) throws ApiSystemException;
    Page<Patient> findAll(Pageable pageable, PatientFilter filter);
    Patient findById(Long id) throws ApiSystemException;

}
