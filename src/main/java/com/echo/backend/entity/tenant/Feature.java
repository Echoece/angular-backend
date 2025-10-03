package com.echo.backend.entity.tenant;

import com.echo.backend.entity.BaseEntity;
import com.echo.backend.entity.tenant.enums.TenantStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This tables holds list of all available feature in the app
 * */


@Entity
@Table(name = "features")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Feature extends BaseEntity {
    private String name;
    private String description;
    private String code;
}
