package com.google.code.jee.utils.image;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.collection.CollectionUtil;

public class ImageConsole {
    private final static String INPUT_FILE = "inputFile";
    private final static String LOGO = "logo";
    private final static String OUTPUT_NAME = "outputName";
    private final static String INFOS_TEXT = "infosText";
    private final static String XML_FILE = "xmlFile";
    private final static String CAMERA_ID = "cameraId";

    private final static String DEFAULT_LOGO_PATH = "src/test/resources/logo_direst_resized.jpg";

    public static void main(String[] args) throws IOException, ParseException {
        // create the parser
        CommandLineParser parser = new PosixParser();

        // create the Options
        Options options = new Options();
        OptionBuilder.withArgName("String");
        OptionBuilder.hasArg(true);
        OptionBuilder.withDescription("Image file in which the informational banner will be added.");
        OptionBuilder.isRequired(true);
        Option inputFileOpt = OptionBuilder.create(INPUT_FILE);

        OptionBuilder.withArgName("String");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("Image file that contains the logo");
        Option logoOpt = OptionBuilder.create(LOGO);

        OptionBuilder.withArgName("String");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("Name of the image file with the added banner");
        Option outputNameOpt = OptionBuilder.create(OUTPUT_NAME);

        OptionBuilder.withArgName("String");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("Text that will be used in the informational banner");
        Option infosTextOpt = OptionBuilder.create(INFOS_TEXT);

        OptionBuilder.withArgName("String");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("name of the camera's configuration file");
        Option xmlFileOpt = OptionBuilder.create(XML_FILE);

        OptionBuilder.withArgName("String");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("id of the camera");
        Option cameraIdOpt = OptionBuilder.create(CAMERA_ID);

        options.addOption(inputFileOpt);
        options.addOption(logoOpt);
        options.addOption(outputNameOpt);
        options.addOption(infosTextOpt);
        options.addOption(xmlFileOpt);
        options.addOption(cameraIdOpt);

        CommandLine line = null;
        try {
            line = parser.parse(options, args);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("ImageConsole", options);
        }

        String inputFile = "";
        String logo = "";
        String outputName = "";
        String infosText = "";
        String xmlFile = "";
        String cameraId = "";

        if (line != null) {
            if (line.hasOption(INPUT_FILE)) {
                inputFile = (String) line.getOptionValue(INPUT_FILE);
                if (line.hasOption(LOGO)) {
                    logo = (String) line.getOptionValue(LOGO);
                    if (line.hasOption(OUTPUT_NAME)) {
                        outputName = (String) line.getOptionValue(OUTPUT_NAME);
                        if (line.hasOption(INFOS_TEXT)) {
                            infosText = (String) line.getOptionValue(INFOS_TEXT);
                        } else if (line.hasOption(XML_FILE) && line.hasOption(CAMERA_ID)) {
                            xmlFile = (String) line.getOptionValue(XML_FILE);
                            cameraId = (String) line.getOptionValue(CAMERA_ID);
                        }
                    } else if (line.hasOption(INFOS_TEXT)) {
                        cameraId = (String) line.getOptionValue(INFOS_TEXT);
                    }
                } else if (line.hasOption(OUTPUT_NAME)) {
                    outputName = (String) line.getOptionValue(OUTPUT_NAME);
                } else if (line.hasOption(INFOS_TEXT)) {
                    outputName = (String) line.getOptionValue(OUTPUT_NAME);
                } else if (line.hasOption(XML_FILE) && line.hasOption(CAMERA_ID)) {
                    xmlFile = (String) line.getOptionValue(XML_FILE);
                    cameraId = (String) line.getOptionValue(CAMERA_ID);
                }
            }
            processImageExport(inputFile, logo, outputName, infosText,
                    xmlFile, cameraId);
        }

    }

    public static void stringToList(List<String> infos, String infoString) {
        String[] strings = infoString.split("\r\n");
        CollectionUtil.addAll(infos, strings);
    }

    public static void processImageExport(String inputFile, String logo, String outputName, String infosText,
            String xmlFile, String cameraId) throws IOException {
        List<String> infos = new ArrayList<String>();

        if(StringUtil.isEmpty(outputName)) {
            outputName = inputFile;
        }
        if (!StringUtil.isEmpty(infosText)) {
            stringToList(infos, infosText);
        }
        BufferedImage bufferedImage = ImageUtil.addInfosBanner(inputFile, logo, infos);

        BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(outputName));
        ImageIO.write(bufferedImage, "jpg", bo);
    }
}
