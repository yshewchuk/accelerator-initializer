/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.hopper.config;

import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Mustache.Compiler;
import com.samskivert.mustache.Mustache.TemplateLoader;

@Configuration
public class MustacheConfig {
    
    @Bean
    TemplateLoader mustacheTemplateLoader() {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        String prefix = "classpath:/templates/";
        Charset charset = Charset.forName("UTF-8");
        return name -> new InputStreamReader(
                resourceLoader.getResource(prefix + name).getInputStream(), charset);
    }
    
    @Bean
    Compiler mustacheCompiler() {
        return Mustache.compiler().withLoader(mustacheTemplateLoader());
    }

    
    
}