package com.echo.backend.entity.tenant;

import com.echo.backend.entity.BaseEntity;
import com.echo.backend.entity.tenant.enums.TenantSubscriptionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;


/**
 * This tables holds list of all features of tenants. During tenant subscription,the feature of package they select
 * will be copied here too.
 * */

@Entity
@Table(name = "tenant_features")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class TenantFeature extends BaseEntity {
    @ManyToOne
    private Tenant tenant;
    @ManyToOne
    private TenantSubscription subscription;
    @ManyToOne
    private Feature feature;
    private boolean enabled;
}
