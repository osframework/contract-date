/*
 * File: FileOutputType.java
 * 
 * Copyright 2013 OSFramework Project.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.osframework.contract.date.spring.fincal.output.file;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Typesafe enumeration of recognized output file types.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public enum FileOutputType {

	CSV("CSV"), TAB("TAB","TSV");

	private final Set<String> extensions;

	private FileOutputType(final String... extensions) {
		this.extensions = new HashSet<String>(Arrays.asList(extensions));
	}

	public boolean matchesExtension(String extension) {
		return extensions.contains(extension);
	}

}