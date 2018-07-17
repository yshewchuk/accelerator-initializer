/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.hopper.model;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by s6995185 on 2017-12-15.
 */
@Getter
@Builder
public class ValidationError {
    private String objectName;
    private String field;
    private Object rejectedValue;
    private String message;
}