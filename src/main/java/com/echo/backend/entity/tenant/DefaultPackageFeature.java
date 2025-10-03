package com.echo.backend.entity.tenant;

import com.echo.backend.entity.BaseEntity;
import com.echo.backend.entity.tenant.enums.FeatureUpdateStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * This tables holds list of all available features for a default package
 * */

@Entity
@Table(name = "default_package_features")
@Setter
@Getter
@NoArgsConstructor
@ToString
@NamedEntityGraph(
        name = "DefaultPackageFeature.feature",
        attributeNodes = @NamedAttributeNode("feature")
)
public class DefaultPackageFeature extends BaseEntity {
    @ManyToOne
    private Feature feature;
    @ManyToOne
    @JsonBackReference
    private DefaultPackage defaultPackage;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private FeatureUpdateStatus status;
}
