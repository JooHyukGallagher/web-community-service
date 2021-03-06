package me.weekbelt.community.infra.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

@JsonComponent
public class ErrorSerializer extends JsonSerializer<Errors> {
    @Override
    public void serialize(Errors errors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        errors.getFieldErrors().forEach(fieldError -> {
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("field", fieldError.getField());
                jsonGenerator.writeStringField("objectName", fieldError.getObjectName());
                jsonGenerator.writeStringField("code", fieldError.getCode());
                jsonGenerator.writeStringField("defaultMessage", fieldError.getDefaultMessage());
                Object rejectedValue = fieldError.getRejectedValue();
                if (rejectedValue != null) {
                    jsonGenerator.writeStringField("rejectValue", rejectedValue.toString());
                }
                jsonGenerator.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        errors.getGlobalErrors().forEach(globalError -> {
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("objectName", globalError.getObjectName());
                jsonGenerator.writeStringField("code", globalError.getCode());
                jsonGenerator.writeStringField("defaultMessage", globalError.getDefaultMessage());
                jsonGenerator.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        jsonGenerator.writeEndArray();

    }
}
