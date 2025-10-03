package com.echo.backend.entity.tenant;

import com.echo.backend.entity.BaseEntity;
import com.echo.backend.entity.tenant.enums.FeatureUpdateStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.repository.EntityGraph;


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
@NamedEntityGraph(
        name = "TenantFeature.feature",
        attributeNodes = @NamedAttributeNode("feature")
)
public class TenantFeature extends BaseEntity {
    @ManyToOne
    private Tenant tenant;
    @ManyToOne
    @JsonBackReference
    private TenantSubscription subscription;
    @ManyToOne
    private Feature feature;
    private boolean enabled;

    @Transient
    FeatureUpdateStatus status;
}
