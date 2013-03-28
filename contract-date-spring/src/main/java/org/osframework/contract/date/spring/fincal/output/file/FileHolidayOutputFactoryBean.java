/*
 * File: FileHolidayOutputFactoryBean.java
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

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.osframework.contract.date.fincal.output.HolidayOutput;
import org.osframework.contract.date.fincal.output.io.csv.TriColCsvOutput;
import org.osframework.contract.date.fincal.output.io.tab.TriColTabOutput;
import org.osframework.contract.date.spring.fincal.output.AbstractHolidayOutputFactoryBean;

/**
 * <tt>FactoryBean</tt> for creation of file-based <tt>HolidayOutput</tt>
 * objects. Instances of this factory bean class produce only <i>prototype</i>
 * -scoped beans, due to creation of an output stream on the target file.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class FileHolidayOutputFactoryBean extends AbstractHolidayOutputFactoryBean {

	private static final FileOutputType DEFAULT_TYPE = FileOutputType.CSV;

	private File target = null;
	private FileOutputType type = null;

	public FileHolidayOutputFactoryBean() {
		super();
	}

	/**
	 * Is the object managed by this factory a singleton? Always returns
	 * <code>false</code>.
	 */
	@Override
	public boolean isSingleton() {
		return false;
	}

	/**
	 * Set if a singleton should be created, or a new object on each request
	 * otherwise.
	 * 
	 * @param singleton whether a singleton should be created
	 * @throws IllegalArgumentException if argument is <code>true</code>
	 */
	@Override
	public void setSingleton(boolean singleton) {
		if (singleton) {
			throw new IllegalArgumentException("File-based HolidayOutput objects cannot be singletons");
		}
	}

	/**
	 * Set the file output target.
	 * 
	 * @param target file to which output is written
	 */
	public void setTarget(File target) {
		this.target = target;
	}

	/**
	 * Set the type of output to be written to the target file. If a valid type
	 * is not identified, output type is set to default of CSV.
	 * 
	 * @param outputType type of output to be written. Recognized values are: 'CSV','TAB','TSV'
	 * @throws IllegalArgumentException if argument is null or empty string
	 */
	public void setOutputType(String outputType) {
		if (StringUtils.isBlank(outputType)) {
			throw new IllegalArgumentException("Missing required 'outputType' property value");
		}
		String compare = outputType.trim().toUpperCase();
		for (FileOutputType fiot : FileOutputType.values()) {
			if (fiot.matchesExtension(compare)) {
				type = fiot;
				break;
			}
		}
		if (null == type) {
			logger.warn("Ignoring unrecognized output type: '" + outputType + "'");
			logger.warn("Using default output type: " + DEFAULT_TYPE);
			type = DEFAULT_TYPE;
		} else {
			logger.info("Using output type: " + type);
		}
	}

	protected HolidayOutput<?, ? extends Exception> createInstance() throws Exception {
		Validate.notNull(target, "Missing required 'target' property");
		if (null == type) {
			final String path = target.getAbsolutePath();
			final String ext = path.substring(path.lastIndexOf('.') + 1);
			setOutputType(ext);
		}
		HolidayOutput<?, ? extends Exception> output = null;
		switch (type) {
		case TAB:
			output = new TriColTabOutput(target);
			break;
		case CSV:
		default:
			output = new TriColCsvOutput(target);
			break;
		}
		return output;
	}

}
