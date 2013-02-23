/*
 * File: ConfigurationObserverChain.java
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Chain of <code>ConfigurationObserver</code> objects. Instances of this class
 * delegate event response to registered member observers.
 * <p>Chain objects maintain a synchronized collection of
 * <code>WeakReference</code>s to registered member observers, allowing for
 * timely garbage collection by the JVM.</p>
 * <p><strong>NOTE:</strong> Configuration implementations which use an instance
 * of <code>ConfigurationObserverChain</code> should always declare a field of
 * this type as final and instantiate it within their constructor. Additionally,
 * references to the internal chain should <i>not</i> be published to clients.
 * </p>
 * 
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class ConfigurationObserverChain implements ConfigurationObserver {

	private final Map<ConfigurationObserver, Object> observerList = Collections.synchronizedMap(new WeakHashMap<ConfigurationObserver, Object>());
	private final Object present = new Object();

	public void addObserver(ConfigurationObserver observer) {
		observerList.put(observer, present);
	}

	public void configurationCreated(Configuration cfg) {
		Set<ConfigurationObserver> observers = new HashSet<ConfigurationObserver>(observerList.keySet());
		synchronized (observerList) {
			for (ConfigurationObserver observer : observers) {
				observer.configurationCreated(cfg);
			}
		}
	}

	public void configurationChanged(Configuration cfg) {
		Set<ConfigurationObserver> observers = new HashSet<ConfigurationObserver>(observerList.keySet());
		synchronized (observerList) {
			for (ConfigurationObserver observer : observers) {
				observer.configurationChanged(cfg);
			}
		}
	}

}
