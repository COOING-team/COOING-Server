package com.example.cooing.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException { // runtimeException 상속
    private CustomErrorCode customErrorCode;
    private String detaliMessage;

    public CustomException(CustomErrorCode customErrorCode) {
        super(customErrorCode.getStatusMessage()); // runtimeException
        this.customErrorCode = customErrorCode;
        this.detaliMessage = customErrorCode.getStatusMessage();
    }
}
