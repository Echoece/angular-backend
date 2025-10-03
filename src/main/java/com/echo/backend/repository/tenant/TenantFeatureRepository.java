package com.echo.backend.repository.tenant;

import com.echo.backend.entity.tenant.TenantFeature;
import com.echo.backend.entity.tenant.TenantSubscription;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenantFeatureRepository extends JpaRepository<TenantFeature, Long> {
    @EntityGraph(value = "TenantFeature.feature", type = EntityGraph.EntityGraphType.FETCH)
    List<TenantFeature> findAllBySubscription(TenantSubscription subscription);
}
