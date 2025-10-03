package com.echo.backend.service.tenant;


import com.echo.backend.dto.tenant.TenantFilterDto;
import com.echo.backend.entity.tenant.Tenant;
import com.echo.backend.exception.customException.ApiNotFoundException;
import com.echo.backend.exception.customException.ApiSystemException;
import com.echo.backend.repository.tenant.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.echo.backend.utility.Utility.copyNonNullProperties;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {
    private final TenantRepository tenantRepository;


    @Override
    public Tenant create(Tenant payload) throws ApiSystemException {
        try {
            return tenantRepository.save(payload);
        } catch (Exception e) {
            throw new ApiSystemException("failed to create tenant: " + e.getMessage());
        }
    }

    @Override
    public Tenant update(Tenant payload, Long id) throws ApiSystemException {
        try {
            getById(id);
            payload.setId(id);
            return tenantRepository.save(payload);
        } catch (Exception e) {
            throw new ApiSystemException("failed to update tenant: "+ e.getMessage());
        }
    }

    @Override
    public Tenant partialUpdate(Tenant payload, Long id) throws ApiSystemException {
        try{
            Tenant savedEntity = getById(id);
            copyNonNullProperties(payload, savedEntity);
            return tenantRepository.save(savedEntity);
        } catch (Exception e){
            throw new ApiSystemException("failed to update tenant: "+e.getMessage());
        }
    }

    @Override
    public void delete(Long id) throws ApiSystemException {
        try {
            tenantRepository.deleteById(id);
        } catch (Exception e) {
            throw new ApiSystemException("failed to delete tenant: "+ e.getMessage());
        }
    }

    @Override
    public Page<Tenant> getAll(Pageable pageable, TenantFilterDto filter) throws ApiSystemException {
        try{
            return tenantRepository.findAll(pageable);
        } catch (Exception e){
            throw new ApiSystemException("failed to get tenant Data: "+ e.getMessage());
        }
    }

    @Override
    public Tenant getById(Long id) throws ApiSystemException {
        return tenantRepository.findById(id)
                .orElseThrow(()-> new ApiSystemException("Tenant not found with id: "+ id));
    }
}
