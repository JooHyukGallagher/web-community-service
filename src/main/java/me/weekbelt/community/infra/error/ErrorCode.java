package me.weekbelt.community.infra.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Common
    SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Unspecified error", "ERROR"),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "400", "Invalid Parameter", "ERROR"),
    NOT_IMPLEMENTED(HttpStatus.NOT_IMPLEMENTED, "501", "Not Implemented yet", "ERROR"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "404", "Missing Resource", "ERROR");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
    private final String severity;
}
