package com.echo.backend.service.tenant;


import com.echo.backend.dto.tenant.TenantFilterDto;
import com.echo.backend.entity.tenant.DefaultPackageFeature;
import com.echo.backend.entity.tenant.TenantFeature;
import com.echo.backend.entity.tenant.TenantSubscription;
import com.echo.backend.entity.tenant.enums.FeatureUpdateStatus;
import com.echo.backend.exception.customException.ApiSystemException;
import com.echo.backend.repository.tenant.DefaultPackageFeatureRepository;
import com.echo.backend.repository.tenant.TenantFeatureRepository;
import com.echo.backend.repository.tenant.TenantSubscriptionRepository;
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
public class TenantSubscriptionServiceImpl implements TenantSubscriptionService {
    private final TenantSubscriptionRepository tenantSubscriptionRepository;
    private final TenantFeatureRepository tenantFeatureRepository;
    private final DefaultPackageFeatureRepository defaultPackageFeatureRepository;


    @Override
    @Transactional
    public TenantSubscription create(TenantSubscription payload) throws ApiSystemException {
        try {
            TenantSubscription tenantSubscription = tenantSubscriptionRepository.save(payload);
            tenantSubscription.setFeatureList(
                    saveTenantSubscriptionFeature(tenantSubscription.getFeatureList(), tenantSubscription)
            );

            return tenantSubscription;
        } catch (Exception e) {
            throw new ApiSystemException("failed to create feature: " + e.getMessage());
        }
    }

    private List<TenantFeature> saveTenantSubscriptionFeature(List<TenantFeature> tenantFeatureList,
                                                              TenantSubscription subscription) {
        try {
            List<TenantFeature> tenantFeatures = new ArrayList<>();

            // add custom feature list
            if (Objects.nonNull(tenantFeatureList) && !tenantFeatureList.isEmpty()) {
                tenantFeatureList.forEach(element -> element.setSubscription(subscription));
                tenantFeatures.addAll(tenantFeatureList);
            }

            // add selected package features
            if (Objects.nonNull(subscription.getDefaultPackage())){
                List<DefaultPackageFeature> defaultPackageFeatures = defaultPackageFeatureRepository
                        .findAllByDefaultPackage(subscription.getDefaultPackage());

                for (DefaultPackageFeature element : defaultPackageFeatures) {
                    TenantFeature t = new TenantFeature();
                    t.setTenant(subscription.getTenant());
                    t.setFeature(element.getFeature());
                    t.setSubscription(subscription);
                    t.setEnabled(true);

                    tenantFeatures.add(t);
                }
            }

            return tenantFeatureRepository.saveAll(tenantFeatures);
        } catch (Exception e){
            return null;
        }
    }

    @Override
    @Transactional
    public TenantSubscription update(TenantSubscription payload, Long id) throws ApiSystemException {
        try {
            getById(id);
            payload.setId(id);
            TenantSubscription updatedEntity = tenantSubscriptionRepository.save(payload);
            updatedEntity.setFeatureList(updateDefaultPackageFeatures(payload));

            return updatedEntity;
        } catch (Exception e) {
            throw new ApiSystemException("failed to update feature: "+ e.getMessage());
        }
    }

    private List<TenantFeature>  updateDefaultPackageFeatures(TenantSubscription entity){
        try{
            List<TenantFeature> defaultPackageFeatures = entity.getFeatureList();
            TenantSubscription subscription = getById(entity.getId());

            if (Objects.nonNull(defaultPackageFeatures) && !defaultPackageFeatures.isEmpty()) {
                Map<FeatureUpdateStatus, List<TenantFeature>> grouped = defaultPackageFeatures
                        .stream()
                        .filter(element -> element.getStatus() != null)
                        .collect(Collectors.groupingBy(TenantFeature::getStatus));

                for (Map.Entry<FeatureUpdateStatus, List<TenantFeature>> entry : grouped.entrySet()) {
                    FeatureUpdateStatus status = entry.getKey();
                    List<TenantFeature> featureList = entry.getValue();

                    if (status.equals(FeatureUpdateStatus.ADD)) {
                        featureList.forEach(element -> element.setSubscription(subscription));
                        tenantFeatureRepository.saveAll(featureList);
                    }

                    if (status.equals(FeatureUpdateStatus.DELETE)) {
                        tenantFeatureRepository.deleteAll(featureList);
                    }
                }
            }

            return tenantFeatureRepository.findAllBySubscription(entity);
        } catch (Exception e){
            return null;
        }
    }

    @Override
    public TenantSubscription partialUpdate(TenantSubscription payload, Long id) throws ApiSystemException {
        try{
            TenantSubscription savedEntity = getById(id);
            copyNonNullProperties(payload, savedEntity);

            TenantSubscription updatedEntity = tenantSubscriptionRepository.save(savedEntity);
            updatedEntity.setFeatureList(updateDefaultPackageFeatures(updatedEntity));

            return updatedEntity;
        } catch (Exception e){
            throw new ApiSystemException("failed to update feature: "+e.getMessage());
        }
    }

    @Override
    public void delete(Long id) throws ApiSystemException {
        try {
            tenantSubscriptionRepository.deleteById(id);
        } catch (Exception e) {
            throw new ApiSystemException("failed to delete feature: "+ e.getMessage());
        }
    }

    @Override
    public Page<TenantSubscription> getAll(Pageable pageable, TenantFilterDto filter) throws ApiSystemException {
        try{
            Page<TenantSubscription> defaultPackages = tenantSubscriptionRepository.findAll(pageable);
            defaultPackages
                    .getContent()
                    .forEach(defaultPackage -> defaultPackage.setFeatureList(
                                                    tenantFeatureRepository.findAllBySubscription(defaultPackage)
                                            )
                    );

            return defaultPackages;
        } catch (Exception e){
            throw new ApiSystemException("failed to get feature Data: "+ e.getMessage());
        }
    }

    @Override
    public TenantSubscription getById(Long id) throws ApiSystemException {
        TenantSubscription defaultPackage = tenantSubscriptionRepository.findById(id)
                .orElseThrow(()-> new ApiSystemException("feature not found with id: "+ id));

        defaultPackage.setFeatureList(tenantFeatureRepository.findAllBySubscription(defaultPackage));
        return defaultPackage;
    }

    @Override
    public Page<TenantSubscription> findAllByTenantId(Long tenantId, Pageable pageable) throws ApiSystemException {
        return tenantSubscriptionRepository.findAllByTenantId(pageable, tenantId);
    }
}
