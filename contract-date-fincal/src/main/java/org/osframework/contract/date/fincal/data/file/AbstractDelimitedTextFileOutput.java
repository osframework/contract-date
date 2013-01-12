/*
 * File: AbstractDelimitedTextFileOutput.java
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
package org.osframework.contract.date.fincal.data.file;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.osframework.contract.date.fincal.config.xml.XmlConstants;
import org.osframework.contract.date.fincal.data.AbstractHolidayOutput;

/**
 * AbstractDelimitedTextFileOutput description here.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class AbstractDelimitedTextFileOutput extends AbstractHolidayOutput<OutputStream, IOException> {

	protected final BufferedWriter writer;
	protected final String delimiter;

	public AbstractDelimitedTextFileOutput(final OutputStream out, final String delimiter) throws IOException {
		super();
		this.writer = new BufferedWriter(new OutputStreamWriter(out, Charset.forName(XmlConstants.DEFAULT_ENCODING)));
		this.delimiter = delimiter;
	}

}