package com.echo.backend.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by Rafiq on 5/30/2017.
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {

    private String name;

    private Integer statusCode;

    private String message;

    private String stackTrace;

    private List<ErrorDetailDTO> details;

    private String timestamp;
    
    private String traceId;

}
