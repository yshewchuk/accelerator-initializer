/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core;

import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;


public interface ProjectCreationService {
    
    /**
     * Method responsible for getting all resources created
     * It contains a list of FileCreator and it filters it based upon
     * user's request
     * @param projectCreation
     * @return converts the project dir into zip and then returns it as byte[]
     */
    byte[] create(ProjectCreation projectCreation);
}