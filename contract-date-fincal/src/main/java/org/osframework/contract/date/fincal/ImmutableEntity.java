/*
 * File: ImmutableEntity.java
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
package org.osframework.contract.date.fincal;

/**
 * Defines behavior of an entity which can provide an immutable representation
 * of itself.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public interface ImmutableEntity<T> {

	/**
	 * @return immutable representation of this object
	 */
	T toImmutable();

	/**
	 * Determine if this object is an immutable representation.
	 * 
	 * @return <code>true</code> if this object is immutable, <code>false</code>
	 *         otherwise
	 */
	boolean isImmutable();

	static final String DEFAULT_EXCEPTION_MSG = "This object is immutable";

}
