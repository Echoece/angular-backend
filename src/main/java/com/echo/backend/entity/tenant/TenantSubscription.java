package com.echo.backend.entity.tenant;

import com.echo.backend.entity.BaseEntity;
import com.echo.backend.entity.tenant.enums.TenantStatus;
import com.echo.backend.entity.tenant.enums.TenantSubscriptionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;


/**
 * This tables holds the tenant subscriptions information.
 * */

@Entity
@Table(name = "tenant_subscriptions")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class TenantSubscription extends BaseEntity {
    @ManyToOne
    private Tenant tenant;
    @ManyToOne
    private DefaultPackage defaultPackage;
    private LocalDate startDate;
    private LocalDate endDate;
    private TenantSubscriptionStatus status;
    private Boolean autoRenew;
}
