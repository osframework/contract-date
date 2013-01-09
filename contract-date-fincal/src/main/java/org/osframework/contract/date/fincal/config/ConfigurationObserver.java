/*
 * File: ConfigurationObserver.java
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
package org.osframework.contract.date.fincal.config;

/**
 * Provides for reaction to <code>Configuration</code> events.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public interface ConfigurationObserver {

	/**
	 * Callback to indicate that the given <code>Configuration</code> object
	 * has been created and is now ready for use.
	 *
	 * @param cfg the configuration initialized
	 */
	public void configurationCreated(Configuration cfg);

	/**
	 * Callback to indicate that the given <code>Configuration</code> object's
	 * state has changed.
	 *
	 * @param cfg the configuration changed
	 */
	public void configurationChanged(Configuration cfg);

}
