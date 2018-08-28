/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.model;

import com.scotiabank.accelerator.initializer.model.ApplicationType;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.text.WordUtils;

import java.io.File;
import java.util.regex.Matcher;

@Builder
@Getter
public class ProjectCreation {

    private String rootDir;
    private String group;
    private String name;
    private ApplicationType type;

    public boolean isJavaBasedProject() {
        return isSpringBootApp() ||
                ApplicationType.JAVA_LIBRARY == type;
    }

    public boolean isSpringBootApp() {
        return ApplicationType.JAVA_SPRING_BOOT.equals(type) ||
                ApplicationType.JAVA_SPRING_BOOT_2.equals(type);
    }

    public boolean isNodeApp() {
        return ApplicationType.NODE.equals(type);
    }

    public String resolvePackageName() {
        return "com." + getGroup().toLowerCase();
    }

    public String getJavaPackageName() {
        return getName()
                .replaceAll("[\\.\\-]", "")
                .toLowerCase();
    }

    public String getJavaApplicationName() {
        return WordUtils.capitalizeFully(getName(), '.', ' ', '-')
                .replaceAll("[\\.\\-]", "");
    }

    public String packageToPath(String content) {
        return content.replaceAll("\\.", Matcher.quoteReplacement(File.separator));
    }

    public String discoverySpringBootVersion() {
        return "1.5.15.RELEASE";
    }
}