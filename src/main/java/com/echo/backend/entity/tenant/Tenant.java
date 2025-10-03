package com.echo.backend.entity.tenant;

import com.echo.backend.entity.BaseEntity;
import com.echo.backend.entity.tenant.enums.TenantStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;


/**
 * This tables holds list of all tenants and their status.
 * */

@Entity
@Table(name = "tenants")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Tenant extends BaseEntity {
    private String name;
    private String domain;
    private String email;
    @Enumerated(EnumType.STRING)
    private TenantStatus status;
}
