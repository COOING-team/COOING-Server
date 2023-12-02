package com.example.cooing.global.base;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

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

  public static <T> BaseResponseDto<T> fail(int httpStatus, String message) {
    return new BaseResponseDto<>(false, httpStatus, message, null);
  }

  public static <T> BaseResponseDto<T> fail(int httpStatus, String message, T result) {
    return new BaseResponseDto<>(false, httpStatus, message, result);
  }
}