package com.echo.backend.service.tenant;


import com.echo.backend.dto.tenant.TenantFilterDto;
import com.echo.backend.entity.tenant.Feature;
import com.echo.backend.exception.customException.ApiSystemException;
import com.echo.backend.repository.tenant.FeatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.echo.backend.utility.Utility.copyNonNullProperties;

@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements FeatureService {
    private final FeatureRepository featureRepository;


    @Override
    public Feature create(Feature payload) throws ApiSystemException {
        try {
            return featureRepository.save(payload);
        } catch (Exception e) {
            throw new ApiSystemException("failed to create feature: " + e.getMessage());
        }
    }

    @Override
    public Feature update(Feature payload, Long id) throws ApiSystemException {
        try {
            getById(id);
            payload.setId(id);
            return featureRepository.save(payload);
        } catch (Exception e) {
            throw new ApiSystemException("failed to update feature: "+ e.getMessage());
        }
    }

    @Override
    public Feature partialUpdate(Feature payload, Long id) throws ApiSystemException {
        try{
            Feature savedEntity = getById(id);
            copyNonNullProperties(payload, savedEntity);
            return featureRepository.save(savedEntity);
        } catch (Exception e){
            throw new ApiSystemException("failed to update feature: "+e.getMessage());
        }
    }

    @Override
    public void delete(Long id) throws ApiSystemException {
        try {
            featureRepository.deleteById(id);
        } catch (Exception e) {
            throw new ApiSystemException("failed to delete feature: "+ e.getMessage());
        }
    }

    @Override
    public Page<Feature> getAll(Pageable pageable, TenantFilterDto filter) throws ApiSystemException {
        try{
            return featureRepository.findAll(pageable);
        } catch (Exception e){
            throw new ApiSystemException("failed to get feature Data: "+ e.getMessage());
        }
    }

    @Override
    public Feature getById(Long id) throws ApiSystemException {
        return featureRepository.findById(id)
                .orElseThrow(()-> new ApiSystemException("feature not found with id: "+ id));
    }
}
