/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.listener;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.scotiabank.accelerator.initializer.core.event.InitializerCleanUpEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InitializerCleanUpListener {
    
    @EventListener(InitializerCleanUpEvent.class)
    public void handleOrderCreatedEvent(InitializerCleanUpEvent event) {
        Path parent = Paths.get(event.getRootdir()).getParent();
        log.info("Cleaning folder {}", parent);
        FileUtils.deleteQuietly(parent.toFile());
        
        
    }
}