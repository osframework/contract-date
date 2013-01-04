/*
 * File: AbstractConfiguration.java
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
package org.osframework.contract.date.fincal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Abstract convenience superclass of objects which provide the definitions used
 * to generate financial calendar data. This class provides a logging facility
 * for use by subclasses.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class AbstractConfiguration implements Configuration {

	protected final Logger logger;

	/**
	 * Default constructor.
	 */
	public AbstractConfiguration() {
		this.logger = LoggerFactory.getLogger(this.getClass());
	}

}
