/*
 * File: GeneratorCmdLine.java
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
package org.osframework.contract.date.fincal.cmd;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.osframework.contract.date.fincal.config.Definitions;
import org.osframework.contract.date.fincal.config.xml.XMLDefinitions;
import org.osframework.contract.date.fincal.model.FinancialCalendar;

/**
 * Main class for command-line generation of financial calendar data.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public final class GeneratorCmdLine {

	private static final String COMMAND = "fincal";
	private static final String DEFAULT_DEFINITIONS_XML = "/definitions.default.xml";
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private static Options options;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		initOptions();
		CommandLineParser parser = new GnuParser();
		try {
			CommandLine cli = parser.parse(options, args);
			if (cli.hasOption("help")) {
				help();
				System.exit(0);
			} else if (cli.hasOption("version")) {
				version();
				System.exit(0);
			} else if (cli.hasOption("list")) {
				list();
				System.exit(0);
			}
		} catch (ParseException pe) {
			System.err.println("Illegal syntax: " + pe.getMessage());
			help();
			System.exit(1);
		}
	}

	private static void initOptions() {
		options = new Options();
		
		// Help message options
		Option help = new Option("help", "print this message");
		Option list = new Option("list", "list available calendar definitions");
		Option version = new Option("version", "print the version information and exit");
		options.addOption(help);
		options.addOption(list);
		options.addOption(version);
		
		
	}

	private static void help() {
		HelpFormatter hf = new HelpFormatter();
		hf.printHelp(COMMAND, options, true);
	}

	private static void list() {
		Definitions<?> defaults = null;
		try {
			defaults = new XMLDefinitions(GeneratorCmdLine.class.getResource(DEFAULT_DEFINITIONS_XML));
			defaults.load();
			if (defaults.isEmpty()) {
				System.out.println("No available calendar definitions");
			} else {
				System.out.println("Available calendar definitions:");
				System.out.println("-------------------------------");
				List<FinancialCalendar> list = defaults.getFinancialCalendars();
				StringBuilder fcBuf = new StringBuilder();
				for (FinancialCalendar fc : list) {
					fcBuf.setLength(0);
					fcBuf.append("* ")
					     .append(fc.getId())
					     .append(" - ")
					     .append(fc.getDescription())
					     .append(LINE_SEPARATOR)
					     .append("  [Central Bank: ")
					     .append(fc.getCentralBank().getName()).append("]");
					System.out.println(fcBuf.toString());
				}
			}
		} catch (Exception e) {
			System.err.println("Aborted. Error: " + e.getMessage());
		}
	}

	private static void version() {
		// TODO Read name, version from MANIFEST.MF
		String name = "Contract Date Calculator - Financial Calendar Generator";
		String version = "x.x.x";
		StringBuilder verBuf = new StringBuilder(name).append(", version ").append(version);
		System.out.println(verBuf.toString());
	}

}
