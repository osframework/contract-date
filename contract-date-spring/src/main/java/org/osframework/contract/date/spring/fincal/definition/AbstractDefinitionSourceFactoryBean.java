/*
 * File: AbstractDefinitionSourceFactoryBean.java
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
package org.osframework.contract.date.spring.fincal.definition;

import org.osframework.contract.date.fincal.definition.DefinitionSource;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.util.Assert;

/**
 * AbstractDefinitionSourceFactoryBean description here.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class AbstractDefinitionSourceFactoryBean<T extends DefinitionSource> extends AbstractFactoryBean<T> {

	protected final Class<T> definitionSourceType;

	private Object source;

	public AbstractDefinitionSourceFactoryBean(Class<T> definitionSourceType) {
		super();
		Assert.notNull(definitionSourceType, "Argument 'definitionSourceType' cannot be null");
		this.definitionSourceType = definitionSourceType;
	}

	public Class<?> getObjectType() {
		return definitionSourceType;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	protected Object getSource() {
		return source;
	}

}
