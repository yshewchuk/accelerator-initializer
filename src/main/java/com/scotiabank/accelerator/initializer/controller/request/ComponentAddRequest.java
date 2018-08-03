/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.controller.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.scotiabank.accelerator.initializer.model.ApplicationType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ComponentAddRequest {

    @Length(max = 128)
    @Pattern(regexp = "^([a-z0-9]|[a-z0-9][a-z0-9\\-_.]+)$", message = "{accp.initializer.component.invalid.name}")
    private String name;
    
    @NotEmpty
    private String projectKey;
    
    @NotNull
    private ApplicationType type;

    @JsonIgnore
    private String dummy;

}