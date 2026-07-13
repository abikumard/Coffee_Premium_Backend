package com.abikumar.coffee_reservation.dto;

/**
 * Uniform envelope returned by every endpoint in the API.
 */
public class ApiResponseDto<T> {

    private boolean success;
    private String message;
    private T data;

    public ApiResponseDto() {
    }

    public ApiResponseDto(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponseDto<T> success(String message, T data) {
        return new ApiResponseDto<>(true, message, data);
    }

    public static <T> ApiResponseDto<T> success(String message) {
        return new ApiResponseDto<>(true, message, null);
    }

    public static <T> ApiResponseDto<T> failure(String message) {
        return new ApiResponseDto<>(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
