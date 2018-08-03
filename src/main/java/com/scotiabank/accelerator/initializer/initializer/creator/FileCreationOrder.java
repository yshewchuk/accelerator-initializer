/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.initializer.creator;

public enum FileCreationOrder {
    
    SRC_FOLDER(0),
    JAVA_MAIN_FOLDER(SRC_FOLDER.order() + 50),
    JAVA_TEST_FOLDER(SRC_FOLDER.order() + 50),
    JAVA_INTEGRATION_TEST_FOLDER(SRC_FOLDER.order() + 50),
    JAVA_INTEGRATION_PACKAGE(JAVA_INTEGRATION_TEST_FOLDER.order() + 50),
    JAVA_PACKAGES(JAVA_MAIN_FOLDER.order()+50),
    APPLICATION_CLASS(JAVA_PACKAGES.order()+50),
    APPLICATION_YML(JAVA_MAIN_FOLDER.order() + 50),
    APPLICATION_TEST_CLASS(JAVA_PACKAGES.order()+50),
    PACKAGE_INFO_TEST(JAVA_PACKAGES.order()+50),
    ENVIRONMENTS_FOLDER(0),
    MANIFEST_FILES(ENVIRONMENTS_FOLDER.order()+50),
    TEST_FOLDER(0),
    INDEX_JS(SRC_FOLDER.order()+50),
    INDEX_SPEC_JS(TEST_FOLDER.order()+50),
    INTEGRATIONTEST_FOLDER(0),
    CONF_JS(INTEGRATIONTEST_FOLDER.order()+50),
    HEALTHCHECK_JS(INTEGRATIONTEST_FOLDER.order()+50),
    APP_JS(SRC_FOLDER.order()+50),
    APP_TEST_JS(TEST_FOLDER.order()+50),
    APP_SPEC_JS(INTEGRATIONTEST_FOLDER.order()+50);

    private final int order;
    
    private FileCreationOrder(int order) {
        this.order = order;
    }
    
    public int order() {
        return order;
    }
    
}