/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scotiabank.accelerator.initializer.core;

import java.io.Reader;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ConcurrentReferenceHashMap;

import com.samskivert.mustache.Mustache.Compiler;
import com.samskivert.mustache.Template;

/**
 * @author Dave Syer
 */
@Component
public class MustacheRenderer {

	private static final Logger log = LoggerFactory.getLogger(MustacheRenderer.class);

	private boolean cache = true;

	private final Compiler mustache;
	private final ConcurrentMap<String, Template> templateCaches =
			new ConcurrentReferenceHashMap<>();

	public MustacheRenderer(Compiler mustache) {
		this.mustache = mustache;
	}

	public String process(String name, Map<String, ?> model) {
		try {
			Template template = getTemplate(name);
			return template.execute(model);
		}
		catch (Exception e) {
			log.error("Cannot render: " + name, e);
			throw new IllegalStateException("Cannot render template", e);
		}
	}

	public Template getTemplate(String name) {
		if (cache) {
			return this.templateCaches.computeIfAbsent(name, this::loadTemplate);
		}
		return loadTemplate(name);
	}

	protected Template loadTemplate(String name) {
		try {
			Reader template;
			template = mustache.loader.getTemplate(name);
			return mustache.compile(template);
		}
		catch (Exception e) {
			throw new IllegalStateException("Cannot load template " + name, e);
		}
	}
}