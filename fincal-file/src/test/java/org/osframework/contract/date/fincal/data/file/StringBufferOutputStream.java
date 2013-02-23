/*
 * File: StringBufferOutputStream.java
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

import org.apache.commons.lang.Validate;

/**
 * OutputStream which collects all output in a managed
 * <code>StringBuffer</code>. This class is for use in testing only.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class StringBufferOutputStream extends OutputStream {

	private static final String EXCEPTION_MESSAGE = "StringBufferOutputStream has already been closed";

	private final StringBuffer buffer;
	private volatile boolean closed;

	/**
	 * Construct an output stream on a new, empty <code>StringBuffer</code>.
	 */
	public StringBufferOutputStream() {
		this(new StringBuffer());
	}

	/**
	 * Construct an output stream on the specified <code>StringBuffer</code>.
	 * 
	 * @param buffer StringBuffer to which output is written by this stream
	 */
	public StringBufferOutputStream(final StringBuffer buffer) {
		Validate.notNull(buffer, "StringBuffer argument cannot be null");
		this.buffer = buffer;
		this.closed = false;
	}

	public void write(int b) throws IOException {
		if (closed) {
			throw new IOException(EXCEPTION_MESSAGE);
		}
		buffer.append((char)b);
	}

	@Override
	public void write(byte[] b) throws IOException {
		if (closed) {
			throw new IOException(EXCEPTION_MESSAGE);
		}
		write(b, 0, b.length);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		if (closed) {
			throw new IOException(EXCEPTION_MESSAGE);
		}
		for (int i = off; i < (off + len); i++) {
			write((int)b[i]);
		}
	}

	@Override
	public void flush() throws IOException {
		if (closed) {
			throw new IOException(EXCEPTION_MESSAGE);
		}
		super.flush();
	}

	@Override
	public void close() throws IOException {
		this.closed = true;
	}

	@Override
	public String toString() {
		return buffer.toString();
	}

	@Override
	public int hashCode() {
		synchronized (buffer) {
			return buffer.hashCode();
		}
	}

}
