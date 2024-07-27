package com.echo.backend.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Faruque on 5/30/2017.
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetailDTO {

    private String fieldName;

    private String value;

    private String message;



}
