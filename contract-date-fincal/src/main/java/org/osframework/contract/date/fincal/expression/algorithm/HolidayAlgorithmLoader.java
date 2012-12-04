/*
 * File: HolidayAlgorithmLoader.java
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
package org.osframework.contract.date.fincal.expression.algorithm;

import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Runtime service loader for <code>HolidayAlgorithm</code> implementations.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayAlgorithmLoader {

	private HolidayAlgorithmLoader() {}

	/**
	 * Load the <code>HolidayAlgorithm</code> object for the given algorithm
	 * name.
	 *  
	 * @param algorithmName name of algorithm to be loaded
	 * @return loaded algorithm object
	 * @throws NoSuchAlgorithmException if no object supporting the named
	 *         algorithm exists in classpath
	 */
	public static HolidayAlgorithm load(String algorithmName) throws NoSuchAlgorithmException {
		ServiceLoader<HolidayAlgorithm> sl = ServiceLoader.load(HolidayAlgorithm.class);
		Iterator<HolidayAlgorithm> it = sl.iterator();
		while (it.hasNext()) {
			HolidayAlgorithm ha = it.next();
			if (ha.name().equals(algorithmName)) {
				return ha;
			}
		}
		throw new NoSuchAlgorithmException("No such holiday algorithm: " + algorithmName);
	}

}
