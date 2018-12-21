/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.listener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

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
        try (Stream<Path> stream = Files.walk(parent)){
            log.info("Cleaning folder {}", parent);

            stream.sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .peek(System.out::println)
                .forEach(File::delete);
        } catch (IOException e) {
            log.error("Cleaning folder {} failed", parent);
        }
    }
}