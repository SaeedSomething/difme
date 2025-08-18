package com.example.difme.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.difme.annotation.ApiResponseWrapper;
import com.example.difme.dto.MyApiResponse;

import io.swagger.v3.oas.models.responses.ApiResponse;

@ControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Check if the controller or method has the @ApiResponseWrapper annotation
        boolean hasAnnotation = returnType.getContainingClass().isAnnotationPresent(ApiResponseWrapper.class) ||
                returnType.hasMethodAnnotation(ApiResponseWrapper.class);

        // Don't wrap if already wrapped or if it's an internal Spring endpoint
        String packageName = returnType.getContainingClass().getPackageName();
        boolean isInternalEndpoint = packageName.startsWith("org.springframework") ||
                packageName.startsWith("springfox") ||
                packageName.startsWith("org.springdoc");

        // Don't wrap if response is already ApiResponse
        boolean isAlreadyWrapped = MyApiResponse.class.isAssignableFrom(returnType.getParameterType());

        return hasAnnotation && !isInternalEndpoint && !isAlreadyWrapped;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request, ServerHttpResponse response) {

        // Skip wrapping for Swagger/OpenAPI endpoints
        String path = request.getURI().getPath();
        if (path.contains("/swagger-ui") || path.contains("/api-docs") || path.contains("/docs")) {
            return body;
        }

        // Check if wrapping is disabled for this method
        ApiResponseWrapper methodAnnotation = returnType.getMethodAnnotation(ApiResponseWrapper.class);
        if (methodAnnotation != null && !methodAnnotation.enabled()) {
            return body;
        }

        // Create success response
        MyApiResponse<Object> apiResponse = MyApiResponse.success(body);
        apiResponse.setPath(path);

        return apiResponse;
    }
}