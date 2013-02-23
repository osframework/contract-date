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

import java.io.IOException;
import java.io.OutputStream;

/**
 * Abstract superclass of <code>Output</code> that store data to a text file as
 * flat, delimited records (rows).
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class AbstractDelimitedTextFileOutput<M> extends AbstractTextFileOutput<M> {

	protected final String delimiter;
	protected int recordCount;

	public AbstractDelimitedTextFileOutput(final OutputStream out, final String delimiter) throws IOException {
		super(out);
		this.delimiter = delimiter;
		this.recordCount = 0;
	}

	public void store(M... m) throws IOException {
		try {
			synchronized (lock) {
				if (0 == recordCount) {
					writeHeaderRow();
				}
				for (int i = 0; i < m.length; i++) {
					if (0 < recordCount++) {
						writer.newLine();
					}
					writer.write(objectToRow(m[i]));
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
				exMsg.append("; Failed to close output stream with error: ")
				     .append(ioe2.getMessage());
			}
			throw new IOException(exMsg.toString(), ioe);
		}
	}

	@Override
	public void close() throws IOException {
		synchronized (lock) {
			super.close();
			logger.debug("Wrote {} records to file", Integer.valueOf(recordCount));
		}
	}

	protected void writeHeaderRow() throws IOException {}

	protected abstract String objectToRow(M modelObj);

}
