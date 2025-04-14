package com.echo.backend.utility.converter;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@Converter
@RequiredArgsConstructor
public class ListJsonConverter implements AttributeConverter<List<?>, String> {
    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(List<?> list) {
        try {
            if (list == null) {
                return null;
            }
            return objectMapper.writeValueAsString(list);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting List to JSON", e);
        }
    }

    @Override
    public List<?> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) {
                return null;
            }
            return objectMapper.readValue(dbData, new TypeReference<List<Object>>() {}); // Convert JSON string to List
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting JSON to List", e);
        }    }
}
