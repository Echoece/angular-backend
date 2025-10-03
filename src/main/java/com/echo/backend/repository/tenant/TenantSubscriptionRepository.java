package com.echo.backend.repository.tenant;

import com.echo.backend.entity.tenant.TenantSubscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenantSubscriptionRepository extends JpaRepository<TenantSubscription, Long> {
    Page<TenantSubscription> findAllByTenantId(Pageable pageable, Long tenantId);
}
