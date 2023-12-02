package com.example.cooing.global.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@ToString
public class BaseResponseDto<T> {

    private final boolean success;
    private final int code;
    private final String message;

    //    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result;


    public static <T> BaseResponseDto<T> success() {
        return new BaseResponseDto<>(true, 200, null, null);
    }

    public static <T> BaseResponseDto<T> success(String message) {
        return new BaseResponseDto<>(true, 200, message, null);
    }

    public static <T> BaseResponseDto<T> success(String message, T result) {
        return new BaseResponseDto<>(true, 200, message, result);
    }

    public static <T> BaseResponseDto<T> fail(HttpStatus httpStatus, String message) {
        return new BaseResponseDto<>(false, httpStatus.value(), message, null);
    }

    public static <T> BaseResponseDto<T> fail(HttpStatus httpStatus, String message, T result) {
        return new BaseResponseDto<>(false, httpStatus.value(), message, result);
    }
}