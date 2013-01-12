/*
 * File: TriColCsvOutput.java
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
package org.osframework.contract.date.fincal.data.file.csv;

import java.io.IOException;
import java.io.OutputStream;

import org.osframework.contract.date.fincal.data.file.AbstractDelimitedTextFileOutput;
import org.osframework.contract.date.fincal.model.Holiday;

/**
 * Stores <tt>Holiday</tt> data in TriCol CSV format specified by <a
 * href="http://www.financialcalendar.com/">financialcalendar.com</a>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class TriColCsvOutput extends AbstractDelimitedTextFileOutput {

	private static final char NEWLINE = '\n';

	private int count;

	public TriColCsvOutput(final OutputStream csvOut) throws IOException {
		super(csvOut, ",");
		this.count = 0;
	}

	@Override
	public void store(Holiday... holidays) throws IOException {
		try {
			for (int i = 0; i < holidays.length; i++) {
				String hRow = holidayToCsvRecord(holidays[i], (0 < count));
				writer.write(hRow);
				count++;
			}
		} catch (IOException ioe) {
			StringBuilder exMsg = new StringBuilder("CSV storage failed at line [")
			                          .append(count)
			                          .append("] with error: ")
			                          .append(ioe.getMessage());
			try {
				this.close();
			} catch (IOException ioe2) {
				exMsg.append(NEWLINE)
				     .append("Failed to close output stream with error: ")
				     .append(ioe2.getMessage());
			}
			throw new IOException(exMsg.toString(), ioe);
		}
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}

	private String holidayToCsvRecord(Holiday h, boolean prependNewline) {
		StringBuilder csv = new StringBuilder()
		                        .append(prependNewline ? NEWLINE : "")
		                        .append(h.getDate())
		                        .append(delimiter)
		                        .append(h.getFinancialCalendar().getId())
		                        .append(delimiter)
		                        .append(h.getHolidayDefinition().getName());
		return csv.toString();
	}

}
