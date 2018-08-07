/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator;

public interface ProjectCreator<T> {
    
    void create(T request);

    default boolean skip(T t) {
        return false;
    }
}