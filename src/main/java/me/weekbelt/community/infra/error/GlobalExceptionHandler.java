package me.weekbelt.community.infra.error;

import com.fasterxml.jackson.core.JsonParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    public static ResponseEntity<ErrorResponse> getErrorResponseEntity(ErrorCode errorCode) {
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(RuntimeException e) {
        log.error("RuntimeException : ", e);
        return GlobalExceptionHandler.getErrorResponseEntity(ErrorCode.SERVICE_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(NullPointerException e) {
        log.error("NullPointerException : ", e);
        return GlobalExceptionHandler.getErrorResponseEntity(ErrorCode.SERVICE_ERROR);
    }

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<ErrorResponse> handleIOException(IOException e) {
        log.error("IOException", e);
        return GlobalExceptionHandler.getErrorResponseEntity(ErrorCode.SERVICE_ERROR);
    }

    @ExceptionHandler(JsonParseException.class)
    protected ResponseEntity<ErrorResponse> handleJsonParseException(JsonParseException e) {
        log.error("JsonParseException", e);
        return GlobalExceptionHandler.getErrorResponseEntity(ErrorCode.INVALID_INPUT_VALUE);
    }

    /**
     * MethodArgumentNotValidException is thrown when @Valid or @Validated has been failed <br />
     * Usually thrown by @RequestBody, @RequestPart annotation process
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Argument Validation Failed : {}", e.getBindingResult());
        return GlobalExceptionHandler.getErrorResponseEntity(ErrorCode.INVALID_INPUT_VALUE);
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.error("Controller Param Binding Error", e);
        return GlobalExceptionHandler.getErrorResponseEntity(ErrorCode.INVALID_INPUT_VALUE);
    }

}
