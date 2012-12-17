package org.osframework.contract.date.fincal;

import java.io.PrintWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.osframework.contract.date.ProjectInfoReader;

public class Main {

	private static final String UNDERLINE = "----------------------------------------";

	private static Options options = null;
	private static String projectName = null, projectVersion = null;

	private static void initOptions() {
		options = new Options();
		
		// Information display options
		Option help = new Option("help", false, "Display this message and exit");
		Option version = new Option("version", false, "Display current version info");
		options.addOption(help);
		options.addOption(version);
	}

	private static void initProjectInfo() {
		ProjectInfoReader reader = InfoPropertiesReader.getInstance();
		projectName = reader.getImplementationTitle();
		projectVersion = reader.getImplementationVersion();
	}

	private static void help() {
		HelpFormatter hf = new HelpFormatter();
		hf.printHelp(new PrintWriter(System.out), UNDERLINE.length(), "fincal", "", options, 0, 4, "");
	}

	private static void version() {
		StringBuilder vBuf = new StringBuilder(projectName)
		                         .append(", version: ").append(projectVersion);
		System.out.println(vBuf.toString());
		System.out.println(UNDERLINE);
	}

	private static void usage() {
		HelpFormatter hf = new HelpFormatter();
		hf.printUsage(new PrintWriter(System.out), UNDERLINE.length(), "fincal", options);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		initOptions();
		initProjectInfo();
		if (0 == args.length) {
			usage();
			System.exit(0);
		}
		
		CommandLine cl = null;
		try {
			CommandLineParser clp = new GnuParser();
			cl = clp.parse(options, args);
			
		} catch (ParseException pe) {
			System.err.println(pe.getClass().getName() + ": " + pe.getMessage());
			usage();
			System.exit(1);
		}
	}

}
