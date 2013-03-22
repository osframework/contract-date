/*
 * File: AbstractInfoOutputFactoryBean.java
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
package org.osframework.contract.date.spring.fincal.output;

import org.osframework.contract.date.fincal.output.InfoOutput;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * AbstractInfoOutputFactoryBean description here.
 *
 * @author <a href="mailto:david.joyce13@gmail.com">Dave Joyce</a>
 */
public abstract class AbstractInfoOutputFactoryBean<T, E extends Exception> extends AbstractFactoryBean<InfoOutput<T, E>> {

	protected T target;

	public AbstractInfoOutputFactoryBean() {
		super();
	}

	public void setTarget(T target) {
		this.target = target;
	}

}
