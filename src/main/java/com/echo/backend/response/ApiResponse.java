package com.echo.backend.response;

import com.echo.backend.exception.dto.ErrorDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.Instant;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@ToString
public class ApiResponse<T>{
    @Builder.Default
    private Instant timestamp = Instant.now();
    private int status;
    private String message;
    private T data;     // actual data
    private ErrorDTO error;
    private Object metadata; // additional metadata
    private boolean isPaginated; // flag to determine if response is paginated

    // Inner class for PaginationMetadata
    @Getter
    @Setter
    @AllArgsConstructor
    public static class PaginationMetadata {
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
    }


    // Success static factory methods
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status(200)
                .message("Success")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .status(200)
                .message(message)
                .data(data)
                .build();
    }

    // Error static factory methods
    public static ApiResponse<Void> error(int status, String message) {

        return ApiResponse.<Void>builder()
                .status(status)
                .message(message)
                .build();
    }


    public static <T> ApiResponse<Page<T>> paginatedResponse(Page<T> data) {
        // Extract pagination information from the Page object
        PaginationMetadata paginationMetadata = new PaginationMetadata(
                data.getNumber(),      // current page number
                data.getSize(),        // page size
                data.getNumberOfElements(),  // total elements
                data.getTotalPages()   // total pages
        );

        // Return the ApiResponse with the paginated data and metadata
        return ApiResponse.<Page<T>>builder()
                .status(200)
                .message("Success")
                .data(data)
                .metadata(paginationMetadata)
                .isPaginated(true)
                .build();
    }



}
