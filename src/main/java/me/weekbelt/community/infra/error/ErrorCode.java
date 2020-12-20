package me.weekbelt.community.infra.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Common
    SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Unspecified error", "ERROR");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
    private final String severity;
}
