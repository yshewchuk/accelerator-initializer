/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.controller.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.scotiabank.accelerator.initializer.model.ApplicationType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
public class ProjectProperties {

    @Length(max = 128)
    @Pattern(regexp = "^([a-z0-9]|[a-z0-9][a-z0-9\\-_.]+)$", message = "{accp.initializer.component.invalid.name}")
    private String name;

    @NotEmpty
    private String group;

    @NotNull
    private ApplicationType type;
}
