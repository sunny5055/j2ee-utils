package com.googlecode.jutils.pp.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.googlecode.jutils.pp.action.exception.ActionException;
import com.googlecode.jutils.pp.service.TemplateProcessorService;
import com.googlecode.jutils.pp.settings.Settings;
import com.googlecode.jutils.pp.settings.SettingsBuilder;
import com.googlecode.jutils.pp.settings.exception.SettingsBuilderException;

public class CommandLineTool {
	protected static final Logger LOGGER = LoggerFactory.getLogger(CommandLineTool.class);
	private static final String FILE_CONFIG_EXTENSION = "tppc";

	public static void main(String[] args) throws IOException, SettingsBuilderException, ActionException {
		if (args.length < 1) {
			printHelp();
		} else {
			final Options options = createOptions();
			CommandLine cmd = null;
			try {
				final CommandLineParser parser = new GnuParser();
				cmd = parser.parse(options, args);

				if (cmd.hasOption("c")) {
					final String fileName = cmd.getOptionValue("c");
					if (!FilenameUtils.isExtension(fileName, FILE_CONFIG_EXTENSION)) {
						throw new IOException("Invalid file " + fileName + " must be a " + FILE_CONFIG_EXTENSION + " file");
					} else {
						final File configFile = new File(fileName);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Launch with " + fileName);
						}
						if (configFile.exists()) {
							final ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "spring/application-context.xml" });
							if (context != null) {
								final SettingsBuilder settingsBuilder = context.getBean(SettingsBuilder.class);
								final TemplateProcessorService templateProcessorService = context.getBean(TemplateProcessorService.class);
								if (settingsBuilder != null) {
									final Settings settings = settingsBuilder.createSettings(configFile);
									settingsBuilder.validate(settings);

									if (templateProcessorService != null) {
										templateProcessorService.process(settings);
									}
								}
							}
						} else {
							throw new FileNotFoundException(configFile.getAbsolutePath());
						}
					}
				}

			} catch (final ParseException e) {
				printHelp();
			}
		}
	}

	private static void printHelp() {
		final HelpFormatter formatter = new HelpFormatter();
		final Options options = createOptions();
		formatter.printHelp("tpp", options);
	}

	private static Options createOptions() {
		final Options options = new Options();
		options.addOption("h", "help", false, "prints the help content");
		options.addOption("c", "config", true, "configuration file (" + FILE_CONFIG_EXTENSION + ")");

		return options;
	}

}