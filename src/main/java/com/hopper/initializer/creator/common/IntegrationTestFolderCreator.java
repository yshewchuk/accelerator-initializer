/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.hopper.initializer.creator.common;

import com.hopper.initializer.creator.FileCreator;
import com.hopper.initializer.creator.annotation.Node;
import com.hopper.initializer.creator.annotation.React;
import com.hopper.initializer.model.ProjectCreation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

@Component
@Slf4j
@React
@Node
public class IntegrationTestFolderCreator implements FileCreator<ProjectCreation> {

    public static final String INTEGRATION_TEST_PATH = "acceptanceTest";

    @Override
    public void create(ProjectCreation request) {
        log.info("Creating integration test folder for project type {}", request.getType());
        Paths.get(request.getRootDir(), INTEGRATION_TEST_PATH)
                .toFile()
                .mkdir();
    }

}