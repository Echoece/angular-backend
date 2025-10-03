package com.echo.backend.entity.tenant;

import com.echo.backend.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;



/**
 * This tables holds list of all available features for a default package
 * */

@Entity
@Table(name = "default_package_features")
public class DefaultPackageFeature extends BaseEntity {
    @ManyToOne
    private Feature feature;
    @ManyToOne
    private DefaultPackage defaultPackage;
}
