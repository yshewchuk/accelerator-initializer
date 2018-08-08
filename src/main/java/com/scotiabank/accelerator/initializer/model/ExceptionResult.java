/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.model;

import com.scotiabank.accelerator.initializer.serializer.RFC1123DateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by s6995185 on 2017-12-15.
 */
@Getter
@Builder
public class ExceptionResult {
    private HttpStatus status;
    @JsonSerialize(using = RFC1123DateTimeSerializer.class)
    private ZonedDateTime timeStamp;
    private String message;
    private List<ValidationError> errors;

    public void addError(ValidationError error) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(error);
    }
}