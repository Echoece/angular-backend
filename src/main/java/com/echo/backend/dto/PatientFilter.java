package com.echo.backend.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientFilter {
    private String patientNo;
    private String patientName;
    private String email;
    private String gender;
    private Long age;
    private Long doctorId;
    private LocalDate dob;
    private String referedBy;
}
