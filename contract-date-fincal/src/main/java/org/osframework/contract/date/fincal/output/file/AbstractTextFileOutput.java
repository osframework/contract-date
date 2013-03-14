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
package org.osframework.contract.date.fincal.output.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.osframework.contract.date.fincal.output.AbstractOutputStreamOutput;

/**
 * Abstract superclass of <code>Output</code> objects that store data to a
 * a text file.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class AbstractTextFileOutput<M> extends AbstractOutputStreamOutput<M> {

	public AbstractTextFileOutput(final File textFile) throws IOException {
		super(new FileOutputStream(textFile));
	}

}
