package me.weekbelt.community.infra.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private String code;
    private String message;
    private String description;
    private String severity;
    private String href;

    public static ErrorResponse of(ErrorCode errorCode) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(errorCode.getCode());
        errorResponse.setMessage(errorCode.getMessage());
        errorResponse.setSeverity(errorCode.getSeverity());
        return errorResponse;
    }

    public static ErrorResponse of(ErrorCode errorCode, String description) {
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        errorResponse.setDescription(description);
        return errorResponse;
    }

//    public static ErrorResponse of(ErrorCode errorCode, String description, String href) {
//        ErrorResponse errorResponse = ErrorResponse.of(errorCode, description);
//        errorResponse.setHref(href);
//        return errorResponse;
//    }

}
