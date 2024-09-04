package com.umc.ttg.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class BaseResponseDto<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public static <T> BaseResponseDto<T> onSuccess(T data, ResponseCode code) {
        return new BaseResponseDto<>(true, code.getCode(), code.getMessage(), data);
    }

    public static <T> BaseResponseDto<T> onFailure(T errors, ResponseCode code) {
        return new BaseResponseDto<>(false, code.getCode(), code.getMessage(), errors);
    }

    @Builder
    public BaseResponseDto(Boolean isSuccess, String code, String message, T result) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.result = result;
    }

    @Getter
    public static class FieldError {
        private String field;
        private String message;

        @Builder
        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }
}
