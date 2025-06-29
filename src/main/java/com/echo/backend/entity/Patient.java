package com.echo.backend.entity;


import com.echo.backend.entity.auth.Users;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Patient extends BaseEntity{
    private String patientNo;

    private String patientName;

    private String email;

    private String phoneNo;

    private Integer age;

    private String gender;

    private String occupation;

    private String address;

    @ManyToOne
    private Users doctor;

    private LocalDate patientCreatedDate;

    private LocalDate dob;

    private String referredBy;
}
