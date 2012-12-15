/*
 * File: ManifestReader.java
 * 
 * Copyright 2012 OSFramework Project.
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
package org.osframework.contract.date.fincal;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.security.CodeSource;
import java.util.jar.Manifest;

/**
 * Reads entry attributes from this artifact's JAR manifest.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
class ManifestReader {

	private static final String MANIFEST_SUFFIX = "META-INF/MANIFEST.MF";

	private final Manifest manifest;

	/**
	 * Constructor - description here.
	 *
	 */
	ManifestReader() {
		CodeSource cs = getClass().getProtectionDomain().getCodeSource();
		if (null != cs) {
			try {
				URL url = resolve(cs.getLocation().toURI());
				this.manifest = new Manifest(url.openStream());
			} catch (Exception e) {
				throw new RuntimeException(("Unable to locate " + MANIFEST_SUFFIX), e);
			}
		} else {
			throw new RuntimeException("Null CodeSource");
		}
	}

	public String getImplementationTitle() {
		return manifest.getMainAttributes().getValue("Implementation-Title");
	}

	public String getImplementationVersion() {
		return manifest.getMainAttributes().getValue("Implementation-Version");
	}

	private URL resolve(URI uri) throws Exception {
		File file = new File(uri.getPath());
		if (file.isDirectory()) {
			return uri.resolve(MANIFEST_SUFFIX).toURL();
		} else {
			return new URL("jar:" + uri.toASCIIString() + "!/" + MANIFEST_SUFFIX);
		}
	}

}
