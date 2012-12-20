/*
 * File: Main.java
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
package org.osframework.contract.date.fincal;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.osframework.contract.date.ProjectInfoReader;
import org.osframework.contract.date.fincal.config.Definitions;
import org.osframework.contract.date.fincal.config.xml.XMLDefinitions;
import org.osframework.contract.date.fincal.expression.HolidayExpression;
import org.osframework.contract.date.fincal.expression.centralbank.CentralBankDecoratorLocator;
import org.osframework.contract.date.fincal.model.CentralBank;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.HolidayDefinition;

/**
 * JAR main class; provides command-line executable driver.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class Main {

	private static final String UNDERLINE = "--------------------------------------------------------------------------------";
	private static final String OPT_HELP = "help";
	private static final String OPT_VERSION = "version";
	private static final String OPT_DEFINITIONS = "definitions";
	private static final String OPT_YEARS = "years";
	private static final String OPT_OUTPUT = "output";
	private static final String DEFAULT_OUTPUT = System.getProperty("user.dir") + "/fincal.csv";

	private static Options OPTIONS = new Options();
	private static String jarName = null,
			              jarVersion = null;

	private static void initJarInfo() {
		ProjectInfoReader reader = InfoPropertiesReader.getInstance();
		jarName = reader.getImplementationTitle();
		jarVersion = reader.getImplementationVersion();
	}

	private static void initOptions() {
		// Help display options
		Option help = new Option(OPT_HELP, false, "Display this message and exit");
		Option version = new Option(OPT_VERSION, false, "Display version information");
		OPTIONS.addOption(help);
		OPTIONS.addOption(version);
		
		// Configuration options
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("path-to-defs");
		OptionBuilder.withDescription("Load financial calendar definitions from path");
		Option definitions = OptionBuilder.create(OPT_DEFINITIONS);
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("range-of-years");
		OptionBuilder.withValueSeparator('-');
		OptionBuilder.withDescription("Range of years to generate financial holidays");
		Option years = OptionBuilder.create(OPT_YEARS);
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("path-to-output");
		OptionBuilder.withDescription("Write generated holidays to path");
		Option out = OptionBuilder.create(OPT_OUTPUT);
		OPTIONS.addOption(definitions);
		OPTIONS.addOption(years);
		OPTIONS.addOption(out);
	}

	private static void printError(Throwable t) {
		System.out.println("Error: " + t.getMessage());
		System.out.println(UNDERLINE);
		showUsage();
	}

	private static void showHelp() {
		showUsage();
		System.out.println(UNDERLINE);
		HelpFormatter hf = new HelpFormatter();
		hf.printHelp("fincal", OPTIONS, false);
	}

	private static void showUsage() {
		HelpFormatter hf = new HelpFormatter();
		hf.printUsage(new PrintWriter(System.out), UNDERLINE.length(), "fincal", OPTIONS);
	}

	private static void showVersion() {
		StringBuilder vBuf = new StringBuilder(jarName).append(", version: ").append(jarVersion);
		System.out.println(vBuf.toString());
		System.out.println(UNDERLINE);
		System.out.println("");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		initJarInfo();
		initOptions();
		if (0 == args.length) {
			showUsage();
			System.exit(0);
		}
		CommandLineParser clp = new GnuParser();
		CommandLine cl = null;
		try {
			cl = clp.parse(OPTIONS, args);
		} catch (ParseException pe) {
			printError(pe);
			System.exit(1);
		}
		if (cl.hasOption(OPT_HELP)) {
			showHelp();
			System.exit(0);
		} else if (cl.hasOption(OPT_VERSION)) {
			showVersion();
			System.exit(0);
		}
		
		// Definitions setup
		Definitions<?> definitions = null;
		try {
			if (cl.hasOption(OPT_DEFINITIONS)) {
				String defPath = cl.getOptionValue(OPT_DEFINITIONS);
				File defFile = new File(defPath);
				String absDefPath = defFile.getAbsolutePath();
				definitions = new XMLDefinitions(absDefPath);
			} else {
				URL defaultDefURL = Main.class.getResource("/definitions.default.xml");
				definitions = new XMLDefinitions(defaultDefURL);
			}
			definitions.load();
		} catch (Exception e) {
			printError(e);
			System.exit(1);
		}
		
		// Years setup
		int minYear, maxYear, startYear, endYear;
		if (cl.hasOption(OPT_YEARS)) {
			String[] years = cl.getOptionValues(OPT_YEARS);
			switch (years.length) {
			case 0:
				Calendar c = Calendar.getInstance();
				minYear = c.get(Calendar.YEAR);
				maxYear = minYear;
				break;
			case 1:
				minYear = Integer.parseInt(years[0]);
				maxYear = minYear;
				break;
			default:
				minYear = Integer.parseInt(years[0]);
				maxYear = Integer.parseInt(years[1]);
				break;
			}
		} else {
			Calendar c = Calendar.getInstance();
			minYear = c.get(Calendar.YEAR);
			maxYear = minYear;
		}
		startYear = Math.min(minYear, maxYear);
		endYear = Math.max(minYear, maxYear);
		
		// Output setup
		String outputPath = (cl.hasOption(OPT_OUTPUT))
				             ? cl.getOptionValue(OPT_OUTPUT)
				             : DEFAULT_OUTPUT;
		File outputFile = new File(outputPath);
		
		List<FinancialCalendar> fcList = definitions.getFinancialCalendars();
		for (int y = startYear; y <= endYear; y++) {
			for (FinancialCalendar fc : fcList) {
				System.out.printf("Calculating %d holidays for '%s'%n", Integer.valueOf(y), fc.getId());
				CentralBank cb = fc.getCentralBank();
				for (HolidayDefinition hd : fc) {
					HolidayExpression he = CentralBankDecoratorLocator.decorate(hd.createHolidayExpression(), cb);
					Date d = he.evaluate(y);
				}
			}
		}
	}

}
