package com.umc.ttg.global.error.handler;

import com.umc.ttg.global.common.BaseResponseDto;
import com.umc.ttg.global.common.ResponseCode;
import com.umc.ttg.global.error.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class MasterExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
        return handleExceptionInternal(e, ResponseCode._UNAUTHORIZED, request);
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<Object> general(GeneralException e, WebRequest request) {
        return handleExceptionInternal(e, e.getErrorCode(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {
        e.printStackTrace(); // 클라이언트에게 불필요한 정보를 노출할 수 있으므로 삭제
        return handleExceptionInternalFalse(e, ResponseCode._INTERNAL_SERVER_ERROR, HttpHeaders.EMPTY, ResponseCode._INTERNAL_SERVER_ERROR.getHttpStatus(),request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> processValidationError(MethodArgumentNotValidException exception) {

        final List<BaseResponseDto.FieldError> fieldErrors = getFieldErrors(exception.getBindingResult());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildFieldErrors(ResponseCode._BAD_REQUEST, fieldErrors));

    }

    private List<BaseResponseDto.FieldError> getFieldErrors(BindingResult bindingResult) {
        final List<FieldError> errors = bindingResult.getFieldErrors();
        return errors.parallelStream()
                .map(error -> BaseResponseDto.FieldError.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage()).build())
                .collect(Collectors.toList());
    }

    private BaseResponseDto buildFieldErrors(ResponseCode responseCode, List<BaseResponseDto.FieldError> errors) {
        return BaseResponseDto.builder()
                .isSuccess(false)
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .result(errors)
                .build();
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, ResponseCode errorCode,
                                                           WebRequest request) {
        return handleExceptionInternal(e, errorCode, HttpHeaders.EMPTY, errorCode.getHttpStatus(),
                request);
    }


    private ResponseEntity<Object> handleExceptionInternal(Exception e, ResponseCode errorCode,
                                                           HttpHeaders headers, HttpStatus status, WebRequest request) {
        BaseResponseDto<Object> body = BaseResponseDto.onFailure(null, errorCode);
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(body);
    }

    private ResponseEntity<Object> handleExceptionInternalFalse(Exception e, ResponseCode errorCode,
                                                                HttpHeaders headers, HttpStatus status, WebRequest request) {
        BaseResponseDto<Object> body = BaseResponseDto.onFailure(null, errorCode);
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(body);
    }

}
