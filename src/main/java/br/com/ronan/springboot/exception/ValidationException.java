package br.com.ronan.springboot.exception;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ValidationException extends DetailsException{
    private final String fields;
    private final String fieldsMessage;
}
