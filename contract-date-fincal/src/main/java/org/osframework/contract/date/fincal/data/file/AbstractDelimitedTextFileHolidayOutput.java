/*
 * File: AbstractDelimitedTextFileHolidayOutput.java
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

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.Validate;
import org.osframework.contract.date.fincal.config.xml.XmlConstants;
import org.osframework.contract.date.fincal.data.AbstractHolidayOutput;
import org.osframework.contract.date.fincal.model.Holiday;

/**
 * AbstractDelimitedTextFileHolidayOutput description here.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class AbstractDelimitedTextFileHolidayOutput extends AbstractHolidayOutput<OutputStream, IOException> {

	protected static final String LINE_SEPARATOR = SystemUtils.LINE_SEPARATOR;

	protected final BufferedWriter writer;
	protected final String delimiter;

	protected int recordCount;

	protected Object lock;

	public AbstractDelimitedTextFileHolidayOutput(final OutputStream out, final String delimiter) throws IOException {
		super();
		Validate.notNull(out, "OutputStream argument cannot be null");
		this.writer = new BufferedWriter(new OutputStreamWriter(out, Charset.forName(XmlConstants.DEFAULT_ENCODING)));
		this.delimiter = delimiter;
		this.recordCount = 0;
		this.lock = out;
	}

	@Override
	public void store(Holiday... holidays) throws IOException {
		try {
			synchronized (lock) {
				for (int i = 0; i < holidays.length; i++) {
					if (0 < recordCount++) {
						writer.newLine();
					}
					writer.write(holidayToRecord(holidays[i]));
				}
			}
		} catch (IOException ioe) {
			StringBuilder exMsg = new StringBuilder("Storage failed at line [")
                                      .append(recordCount)
                                      .append("] with error: ")
                                      .append(ioe.getMessage());
			try {
				this.close();
			} catch (IOException ioe2) {
				exMsg.append(LINE_SEPARATOR)
				.append("Failed to close output stream with error: ")
				.append(ioe2.getMessage());
			}
			throw new IOException(exMsg.toString(), ioe);
		}
	}

	@Override
	public void close() throws IOException {
		synchronized (lock) {
			writer.close();
		}	
	}

	protected abstract String holidayToRecord(Holiday holiday);

}
