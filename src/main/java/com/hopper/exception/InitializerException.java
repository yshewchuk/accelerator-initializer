/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.hopper.exception;

import org.springframework.http.HttpStatus;

import com.google.common.base.Throwables;

import lombok.Getter;

/**
 * Created by s6995185 on 2017-12-18.
 */
@Getter
public class InitializerException extends RuntimeException {
    private static final long serialVersionUID = -5109086597065648350L;
    private final Object[] args;
    private final HttpStatus status;

    public InitializerException(String message, Object... args) {
        super(message);
        this.args = args;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public InitializerException(String message, HttpStatus status, Object... args) {
        super(message);
        this.args = args;
        this.status = status;
    }

    public InitializerException(String message, Throwable cause, Object... args) {
        super(message, cause);
        this.args = args;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public InitializerException(String message, HttpStatus status, Throwable cause, Object... args) {
        super(message, cause);
        this.args = args;
        this.status = status;
    }

    @Override
    public String toString() {
        return Throwables.getStackTraceAsString(super.getCause());
    }
}