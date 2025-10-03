package com.echo.backend.entity.tenant;

import com.echo.backend.entity.BaseEntity;
import com.echo.backend.entity.tenant.enums.TenantStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


/**
 * This tables holds list of all available default packages in the app
 * */

@Entity
@Table(name = "default_packages")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class DefaultPackage extends BaseEntity {
    private String name;
    private Double monthly_price;
    private Double yearly_price;
    private String description;
    private boolean isDefault;

    @Transient
    List<DefaultPackageFeature> featureList;
}
