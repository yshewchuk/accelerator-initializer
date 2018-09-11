/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.impl;

import com.scotiabank.accelerator.initializer.core.creator.ProjectCreator;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import com.scotiabank.accelerator.initializer.engine.TemplateProcessor;
import com.scotiabank.accelerator.initializer.model.ApplicationType;
import org.springframework.stereotype.Component;

@Component
class SpringBoot2ProjectCreatorImpl implements ProjectCreator<ProjectCreation> {

    private final TemplateProcessor templateProcessor;

    public SpringBoot2ProjectCreatorImpl(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @Override
    public boolean skip(ProjectCreation projectCreation) {
        return ApplicationType.JAVA_SPRING_BOOT_2 != projectCreation.getType();
    }

    @Override
    public void create(ProjectCreation request) {
        templateProcessor.createApplication(request);
    }
}