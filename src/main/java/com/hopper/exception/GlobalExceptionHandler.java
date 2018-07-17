/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.hopper.exception;

import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hopper.model.ExceptionResult;
import com.hopper.model.ValidationError;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResult resolveValidationErrors(MethodArgumentNotValidException exception) {
        log.error("Validation failed", exception);
        Locale locale = LocaleContextHolder.getLocale();
        ExceptionResult result = ExceptionResult.builder().status(HttpStatus.BAD_REQUEST).timeStamp(ZonedDateTime.now()).message("Validation error").build();
        BindingResult bindingResult = exception.getBindingResult();
        for (ObjectError error : bindingResult.getAllErrors()) {
            val validationErrorBuilder = ValidationError.builder();
            if (error instanceof FieldError) {
                validationErrorBuilder
                    .objectName(error.getObjectName())
                    .field(((FieldError) error).getField())
                    .message(messageSource.getMessage(error, locale))
                    .rejectedValue(((FieldError) error).getRejectedValue());
            } else {
                validationErrorBuilder
                    .objectName(error.getObjectName())
                    .message(messageSource.getMessage(error, locale));
            }
            result.addError(validationErrorBuilder.build());
        }
        return result;
    }

    @ExceptionHandler(InitializerException.class)
    public ResponseEntity<ExceptionResult> resolveGenericException(InitializerException exception) {
        log.error("An error occured", exception);
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(exception.getMessage(), exception.getArgs(), exception.getMessage(), locale);
        ExceptionResult result = ExceptionResult.builder().status(exception.getStatus()).timeStamp(ZonedDateTime.now()).message(message).build();
        return ResponseEntity.status(exception.getStatus()).body(result);
    }
    
    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ExceptionResult> resolveGenericException(TimeoutException exception) {
        log.error("An error occured", exception);
        String message = "Timeout has occured";
        ExceptionResult result = ExceptionResult.builder()
                                     .status(HttpStatus.SERVICE_UNAVAILABLE)
                                     .timeStamp(ZonedDateTime.now())
                                     .message(message)
                                     .build();
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(result);
    }
}