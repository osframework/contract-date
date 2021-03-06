/*
 * File: InfoCsvOutput.java
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
package org.osframework.contract.date.fincal.output.io.csv;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang.StringUtils;
import org.osframework.contract.date.fincal.definition.FinancialCalendar;
import org.osframework.contract.date.fincal.output.DefinitionOutput;
import org.osframework.contract.date.fincal.output.io.AbstractDelimitedTextOutput;


/**
 * Stores <tt>FinancialCalendar</tt> info in CSV format as specified by <a
 * href="http://www.financialcalendar.com/">financialcalendar.com</a>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class InfoCsvOutput extends AbstractDelimitedTextOutput<FinancialCalendar>
	implements DefinitionOutput<OutputStream, IOException> {

	public InfoCsvOutput(final File csvFile) throws IOException {
		super(csvFile, ",");
	}

	@Override
	protected void writeHeaderRow() throws IOException {
		writer.write(StringUtils.join(COLUMNS, delimiter));
		writer.newLine();
	}

	protected String objectToRow(FinancialCalendar calendar) {
		StringBuilder csv = new StringBuilder()
		                        .append(calendar.getId())
		                        .append(delimiter)
		                        .append(calendar.getDescription())
		                        .append(delimiter)
		                        .append("---")
		                        .append(delimiter)
		                        .append(calendar.getCurrency().getCurrencyCode())
		                        .append(delimiter)
		                        .append("Bank")
		                        .append(delimiter)
		                        .append(calendar.getCentralBank().getCountry())
		                        .append(delimiter)
		                        .append(DEFAULT_DEFINE1)
		                        .append(delimiter)
		                        .append("");
		return csv.toString();
	}

}
