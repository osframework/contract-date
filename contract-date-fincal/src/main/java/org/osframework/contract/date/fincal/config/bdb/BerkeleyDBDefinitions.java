/*
 * File: BerkeleyDBDefinitions.java
 * 
 * Copyright 2012 OSFramework Project.
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
package org.osframework.contract.date.fincal.config.bdb;

import java.io.File;

import org.osframework.contract.date.fincal.config.Definitions;

import com.sleepycat.je.Environment;

/**
 * BerkeleyDBDefinitions description here.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 *
 */
public class BerkeleyDBDefinitions extends Definitions<Environment> {

	public static final String HOME_DIRECTORY_DEFAULT = System.getProperty("user.home") + "/.contract-date-fincal";

	public BerkeleyDBDefinitions() throws Exception {
		this(HOME_DIRECTORY_DEFAULT);
	}

	public BerkeleyDBDefinitions(String dbHomeDirectory) throws Exception {
		this(new File(dbHomeDirectory));
	}

	public BerkeleyDBDefinitions(File dbHomeDirectory) throws Exception {
		super();
		this.definitionSource = new Environment(dbHomeDirectory, null);
	}

	public BerkeleyDBDefinitions(Definitions<?> original) {
		super(original);
	}

	@Override
	public void load() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
