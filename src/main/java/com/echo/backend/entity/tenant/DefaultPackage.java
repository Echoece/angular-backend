package com.echo.backend.entity.tenant;

import com.echo.backend.entity.BaseEntity;
import com.echo.backend.entity.tenant.enums.TenantStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


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
    private String monthly_price;
    private String yearly_price;
    private String description;
    private boolean isDefault;
}
