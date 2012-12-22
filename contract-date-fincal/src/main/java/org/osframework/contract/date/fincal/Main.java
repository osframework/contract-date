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
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.IntRange;
import org.apache.commons.lang.math.Range;
import org.osframework.contract.date.ProjectInfoReader;
import org.osframework.contract.date.fincal.config.ConfigurationException;
import org.osframework.contract.date.fincal.config.Definitions;
import org.osframework.contract.date.fincal.config.xml.XMLDefinitions;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.Holiday;

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
	private static final String DEFAULT_DEFINITIONS = "/definitions.default.xml";
	private static final String DEFAULT_OUTPUT = System.getProperty("user.dir") + "/fincal.csv";

	private final Options options;
	private final String jarName, jarVersion;

	private Definitions<?> definitions;
	private Range yearRange;
	private File outputFile;

	private Main() {
		ProjectInfoReader reader = InfoPropertiesReader.getInstance();
		jarName = reader.getImplementationTitle();
		jarVersion = reader.getImplementationVersion();
		this.options = new Options();
		initOptions();
	}

	private void initOptions() {
		// Help display options
		Option help = new Option(OPT_HELP, false, "Display this message and exit");
		Option version = new Option(OPT_VERSION, false, "Display version information");
		options.addOption(help);
		options.addOption(version);
		
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
		options.addOption(definitions);
		options.addOption(years);
		options.addOption(out);
	}

	private void setDefinitions(final Definitions<?> definitions) {
		this.definitions = definitions;
	}

	private void setYearRange(final int startYear, final int endYear) {
		this.yearRange = new IntRange(startYear, endYear);
	}

	private void setOutputFile(final File outputFile) {
		this.outputFile = outputFile;
	}

	private void generateHolidays() {
		List<FinancialCalendar> fcList = definitions.getFinancialCalendars();
		ExecutorService service = Executors.newFixedThreadPool(fcList.size());
		CountDownLatch startSignal = new CountDownLatch(1),
				        doneSignal = new CountDownLatch(fcList.size());
		ConcurrentLinkedQueue<Holiday> queue = new ConcurrentLinkedQueue<Holiday>();
		for (FinancialCalendar fc : fcList) {
			service.execute(new FinancialCalendarHolidayProducer(fc, yearRange, queue, startSignal, doneSignal));
		}
		System.out.println("Starting holiday generation...");
		startSignal.countDown();
		try {
			doneSignal.await();
			System.out.println("Holidays generated.");
			System.out.println("Writing holidays to: " + outputFile.getAbsolutePath());
		} catch (InterruptedException ie) {
			// TODO ???
		}
	}

	private void showHelp() {
		showUsage();
		System.out.println(UNDERLINE);
		HelpFormatter hf = new HelpFormatter();
		hf.printHelp("fincal", options, false);
	}

	private void showUsage() {
		HelpFormatter hf = new HelpFormatter();
		hf.printUsage(new PrintWriter(System.out), UNDERLINE.length(), "fincal", options);
	}

	private void showVersion() {
		StringBuilder vBuf = new StringBuilder(jarName).append(", version: ").append(jarVersion);
		System.out.println(vBuf.toString());
		System.out.println(UNDERLINE);
		System.out.println("");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final Main main = new Main();
		if (0 == args.length) {
			main.showUsage();
		} else {
			final CommandLineParser parser = new GnuParser();
			final CommandLine cmdLine;
			try {
				cmdLine = parser.parse(main.options, args);
			} catch (ParseException pe) {
				throw new ConfigurationException("Invalid arguments", pe);
			}
			if (cmdLine.hasOption(OPT_HELP)) {
				main.showHelp();
			} else if (cmdLine.hasOption(OPT_VERSION)) {
				main.showVersion();
			} else {
				// Definitions setup
				final String defPath = StringUtils.defaultIfBlank(cmdLine.getOptionValue(OPT_DEFINITIONS), DEFAULT_DEFINITIONS);
				final Definitions<?> definitions;
				try {
					definitions  = (DEFAULT_DEFINITIONS.equals(defPath))
		                            ? new XMLDefinitions(Main.class.getResource(defPath))
                                    : new XMLDefinitions(new File(defPath));
		            definitions.load();
		            main.setDefinitions(definitions);
				} catch (Exception e) {
					throw new ConfigurationException("Failed to load definitions from: " + defPath, e);
				}
				
				// Years setup
				int minYear, maxYear;
				if (cmdLine.hasOption(OPT_YEARS)) {
					String[] years = cmdLine.getOptionValues(OPT_YEARS);
					
					switch (years.length) {
					case 0:
						Calendar cal = Calendar.getInstance();
						minYear = cal.get(Calendar.YEAR);
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
					Calendar cal = Calendar.getInstance();
					minYear = cal.get(Calendar.YEAR);
					maxYear = minYear;
				}
				main.setYearRange(minYear, maxYear);
				
				// Output file setup
				final String outputFilePath = (cmdLine.hasOption(OPT_OUTPUT))
						                       ? cmdLine.getOptionValue(OPT_OUTPUT)
						                       : DEFAULT_OUTPUT;
				main.setOutputFile(new File(outputFilePath));
				
				// Generate holidays
				main.generateHolidays();
			}
		}
		
	}

}
