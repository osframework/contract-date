/*
 * File: AbstractTextFileOutput.java
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

import org.osframework.contract.date.fincal.data.AbstractOutput;

/**
 * Abstract superclass of <code>Output</code> objects that store data to an
 * <code>OutputStream</code> on a text file. Operations provided by this class
 * and subclasses must synchronize on the provided output stream object.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class AbstractTextFileOutput<M> extends AbstractOutput<M, OutputStream, IOException> {

	protected final BufferedWriter writer;
	protected final Object lock;

	public AbstractTextFileOutput(final OutputStream out) throws IOException {
		super();
		this.writer = new BufferedWriter(new OutputStreamWriter(out, Charset.forName("UTF-8")));
		this.lock = out;
	}

	public void close() throws IOException {
		synchronized (lock) {
			writer.close();
		}	
	}

}
