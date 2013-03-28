/*
 * File: AbstractOutputStreamOutput.java
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
package org.osframework.contract.date.fincal.output.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.osframework.contract.date.fincal.output.AbstractOutput;

/**
 * AbstractOutputStreamOutput description here.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class AbstractOutputStreamOutput<M> extends AbstractOutput<M, OutputStream, IOException> {

	protected final BufferedWriter writer;

	public AbstractOutputStreamOutput(final OutputStream out) {
		super();
		this.writer = new BufferedWriter(new OutputStreamWriter(out, Charset.forName("UTF-8")));
	}

	public void close() throws IOException {
		synchronized (writer) {
			writer.close();
		}
	}

}
