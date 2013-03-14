/*
 * File: TriColTabOutput.java
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
package org.osframework.contract.date.fincal.output.file.tab;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.osframework.contract.date.fincal.holiday.Holiday;
import org.osframework.contract.date.fincal.output.HolidayOutput;
import org.osframework.contract.date.fincal.output.file.AbstractDelimitedTextFileOutput;

/**
 * Stores <tt>Holiday</tt> data in TriCol tab-delimited format specified by <a
 * href="http://www.financialcalendar.com/">financialcalendar.com</a>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class TriColTabOutput extends AbstractDelimitedTextFileOutput<Holiday>
	implements HolidayOutput<OutputStream, IOException> {

	public TriColTabOutput(final File tabFile) throws IOException {
		super(tabFile, String.valueOf('\t'));
	}

	protected String objectToRow(Holiday holiday) {
		StringBuilder tsv = new StringBuilder()
		                        .append(holiday.getDate())
		                        .append(delimiter)
		                        .append(holiday.getFinancialCalendar().getId())
		                        .append(delimiter)
		                        .append(holiday.getHolidayDefinition().getName());
		return tsv.toString();
	}

}
