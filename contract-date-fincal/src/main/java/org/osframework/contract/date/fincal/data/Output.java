/*
 * File: Output.java
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
package org.osframework.contract.date.fincal.data;

/**
 * Defines basic behavior of objects which store financial calendar domain
 * model objects to an output destination.
 *
 * @param <M> Domain model type
 * @param <T> Output destination type
 * @param <E> Base exception class thrown by operations on output
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public interface Output<M, T, E extends Exception> {

	public void store(M... m) throws E;

	public void close() throws E;

}
