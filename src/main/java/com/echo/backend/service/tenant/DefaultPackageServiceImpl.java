package com.echo.backend.service.tenant;


import com.echo.backend.dto.tenant.TenantFilterDto;
import com.echo.backend.entity.tenant.DefaultPackage;
import com.echo.backend.entity.tenant.DefaultPackageFeature;
import com.echo.backend.entity.tenant.enums.FeatureUpdateStatus;
import com.echo.backend.exception.customException.ApiSystemException;
import com.echo.backend.repository.tenant.DefaultPackageFeatureRepository;
import com.echo.backend.repository.tenant.DefaultPackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.echo.backend.utility.Utility.copyNonNullProperties;

@Service
@RequiredArgsConstructor
public class DefaultPackageServiceImpl implements DefaultPackageService {
    private final DefaultPackageRepository defaultPackageRepository;
    private final DefaultPackageFeatureRepository defaultPackageFeatureRepository;


    @Override
    @Transactional
    public DefaultPackage create(DefaultPackage payload) throws ApiSystemException {
        try {
            DefaultPackage savedDefaultPackage = defaultPackageRepository.save(payload);
            savedDefaultPackage.setFeatureList(
                    saveDefaultPackageFeatures(savedDefaultPackage.getFeatureList(), savedDefaultPackage)
            );

            return savedDefaultPackage;
        } catch (Exception e) {
            throw new ApiSystemException("failed to create feature: " + e.getMessage());
        }
    }

    private List<DefaultPackageFeature> saveDefaultPackageFeatures(List<DefaultPackageFeature> defaultPackageFeatures,
                                                                   DefaultPackage defaultPackage) {
        try {
            List<DefaultPackageFeature> savedDefaultPackageFeatures = new ArrayList<>();
            if (Objects.nonNull(defaultPackageFeatures) && !defaultPackageFeatures.isEmpty()) {
                defaultPackageFeatures
                        .forEach(
                                defaultPackageFeature -> defaultPackageFeature.setDefaultPackage(defaultPackage)
                        );

                savedDefaultPackageFeatures = defaultPackageFeatureRepository.saveAll(defaultPackageFeatures);
            }

            return savedDefaultPackageFeatures;
        } catch (Exception e){
            return null;
        }
    }

    @Override
    @Transactional
    public DefaultPackage update(DefaultPackage payload, Long id) throws ApiSystemException {
        try {
            getById(id);
            payload.setId(id);
            DefaultPackage updatedEntity = defaultPackageRepository.save(payload);
            updatedEntity.setFeatureList(updateDefaultPackageFeatures(payload));

            return updatedEntity;
        } catch (Exception e) {
            throw new ApiSystemException("failed to update feature: "+ e.getMessage());
        }
    }

    private List<DefaultPackageFeature>  updateDefaultPackageFeatures(DefaultPackage entity){
        try{
            List<DefaultPackageFeature> defaultPackageFeatures = entity.getFeatureList();
            DefaultPackage defaultPackage = getById(entity.getId());

            if (Objects.nonNull(defaultPackageFeatures) && !defaultPackageFeatures.isEmpty()) {
                Map<FeatureUpdateStatus, List<DefaultPackageFeature>> grouped = defaultPackageFeatures
                        .stream()
                        .filter(element -> element.getStatus() != null)
                        .collect(Collectors.groupingBy(DefaultPackageFeature::getStatus));

                for (Map.Entry<FeatureUpdateStatus, List<DefaultPackageFeature>> entry : grouped.entrySet()) {
                    FeatureUpdateStatus status = entry.getKey();
                    List<DefaultPackageFeature> featureList = entry.getValue();

                    if (status.equals(FeatureUpdateStatus.ADD)) {
                        featureList.forEach(element -> element.setDefaultPackage(defaultPackage));
                        defaultPackageFeatureRepository.saveAll(featureList);
                    }

                    if (status.equals(FeatureUpdateStatus.DELETE)) {
                        defaultPackageFeatureRepository.deleteAll(featureList);
                    }
                }
            }

            return defaultPackageFeatureRepository.findAllByDefaultPackage(entity);
        } catch (Exception e){
            return null;
        }
    }

    @Override
    public DefaultPackage partialUpdate(DefaultPackage payload, Long id) throws ApiSystemException {
        try{
            DefaultPackage savedEntity = getById(id);
            copyNonNullProperties(payload, savedEntity);

            DefaultPackage updatedEntity = defaultPackageRepository.save(savedEntity);
            updatedEntity.setFeatureList(updateDefaultPackageFeatures(updatedEntity));

            return updatedEntity;
        } catch (Exception e){
            throw new ApiSystemException("failed to update feature: "+e.getMessage());
        }
    }

    @Override
    public void delete(Long id) throws ApiSystemException {
        try {
            defaultPackageRepository.deleteById(id);
        } catch (Exception e) {
            throw new ApiSystemException("failed to delete feature: "+ e.getMessage());
        }
    }

    @Override
    public Page<DefaultPackage> getAll(Pageable pageable, TenantFilterDto filter) throws ApiSystemException {
        try{
            Page<DefaultPackage> defaultPackages = defaultPackageRepository.findAll(pageable);
            defaultPackages
                    .getContent()
                    .forEach(defaultPackage -> defaultPackage.setFeatureList(
                                                    defaultPackageFeatureRepository.findAllByDefaultPackage(defaultPackage)
                                            )
                    );

            return defaultPackages;
        } catch (Exception e){
            throw new ApiSystemException("failed to get feature Data: "+ e.getMessage());
        }
    }

    @Override
    public DefaultPackage getById(Long id) throws ApiSystemException {
        DefaultPackage defaultPackage = defaultPackageRepository.findById(id)
                .orElseThrow(()-> new ApiSystemException("feature not found with id: "+ id));

        defaultPackage.setFeatureList(defaultPackageFeatureRepository.findAllByDefaultPackageId(id));

        return defaultPackage;
    }
}
