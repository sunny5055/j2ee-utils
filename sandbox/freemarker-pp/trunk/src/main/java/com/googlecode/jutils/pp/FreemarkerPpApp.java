package com.googlecode.jutils.pp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.googlecode.jutils.pp.action.exception.ActionException;
import com.googlecode.jutils.pp.service.TemplateProcessorService;
import com.googlecode.jutils.pp.settings.Settings;
import com.googlecode.jutils.pp.settings.SettingsBuilder;
import com.googlecode.jutils.pp.settings.exception.SettingsBuilderException;

public class FreemarkerPpApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(FreemarkerPpApp.class);
	private static ClassPathXmlApplicationContext applicationContext;
	private static final String FILE_CONFIG_EXTENSION = "tppc";

	public static void main(String[] args) {
		int exitCode = 0;

		try {
			final CommandLine commandLine = parseCommandLine(args);

			if (commandLine.getOptions().length == 0) {
				printHelp();
				exitCode = 1;
			} else if (commandLine.hasOption("h")) {
				printHelp();
			} else {
				applicationContext = new ClassPathXmlApplicationContext("/META-INF/spring/application-context.xml");

				if (commandLine.hasOption("c")) {
					final String fileName = commandLine.getOptionValue("c");

					if (!FilenameUtils.isExtension(fileName, FILE_CONFIG_EXTENSION)) {
						throw new IOException("Invalid file " + fileName + " must be a " + FILE_CONFIG_EXTENSION
								+ " file");
					} else {
						final File configFile = new File(fileName);
						if (configFile.exists()) {
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("Launch with " + fileName);
							}

							final SettingsBuilder settingsBuilder = applicationContext.getBean(SettingsBuilder.class);
							final TemplateProcessorService templateProcessorService = applicationContext
									.getBean(TemplateProcessorService.class);
							if (settingsBuilder != null) {
								final Settings settings = settingsBuilder.createSettings(configFile);
								settingsBuilder.validate(settings);

								if (templateProcessorService != null) {
									templateProcessorService.process(settings);
								}
							}
						} else {
							throw new FileNotFoundException(configFile.getAbsolutePath());
						}
					}
				}
			}
		} catch (final ParseException pe) {
			LOGGER.error("Arguments non valides : " + pe.getMessage());
			exitCode = 1;
		} catch (final IOException e) {
			LOGGER.error(e.getMessage(), e);
			exitCode = 1;
		} catch (final SettingsBuilderException e) {
			LOGGER.error(e.getMessage(), e);
			exitCode = 1;
		} catch (final ActionException e) {
			LOGGER.error(e.getMessage(), e);
			exitCode = 1;
		} finally {
			if (applicationContext != null) {
				applicationContext.close();
			}
		}

		System.exit(exitCode);
	}

	private static void printHelp() {
		final Options options = defineOptions();

		final HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("freemarker-pp [options]", options);
	}

	private static CommandLine parseCommandLine(String... args) throws ParseException {
		final Options options = defineOptions();
		final CommandLineParser parser = new BasicParser();
		return parser.parse(options, args);
	}

	@SuppressWarnings("static-access")
	private static Options defineOptions() {
		final Options options = new Options();

		final Option help = OptionBuilder.withLongOpt("help").withDescription("Prints the help content").create('h');
		final Option configFile = OptionBuilder.withLongOpt("config").withArgName("FILE").hasArg()
				.withDescription("configuration file (" + FILE_CONFIG_EXTENSION + ")").create('c');

		options.addOption(help);
		options.addOption(configFile);

		return options;
	}
}