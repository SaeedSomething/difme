package com.example.difme.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard API response wrapper")
public class MyApiResponse<T> {

    @Schema(description = "Indicates if the request was successful", example = "true")
    private boolean success;

    @Schema(description = "Response message", example = "Operation completed successfully")
    private String message;

    @Schema(description = "Response data")
    private T data;

    @Schema(description = "Error details (only present when success = false)")
    private ErrorDetails error;

    @Schema(description = "Response timestamp", example = "2025-08-02T10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Request path", example = "/api/users/1")
    private String path;

    // Success response factory methods
    public static <T> MyApiResponse<T> success(T data) {
        return MyApiResponse.<T>builder()
                .success(true)
                .message("Operation completed successfully")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> MyApiResponse<T> success(T data, String message) {
        return MyApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> MyApiResponse<T> success(String message) {
        return MyApiResponse.<T>builder()
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Error response factory methods
    public static <T> MyApiResponse<T> error(String message, ErrorDetails error) {
        return MyApiResponse.<T>builder()
                .success(false)
                .message(message)
                .error(error)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> MyApiResponse<T> error(String message, String errorCode, int statusCode) {
        return MyApiResponse.<T>builder()
                .success(false)
                .message(message)
                .error(ErrorDetails.builder()
                        .code(errorCode)
                        .statusCode(statusCode)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Error details")
    public static class ErrorDetails {
        @Schema(description = "Error code", example = "USER_NOT_FOUND")
        private String code;

        @Schema(description = "HTTP status code", example = "404")
        private int statusCode;

        @Schema(description = "Detailed error information")
        private Object details;
    }
}