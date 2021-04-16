package ar.edu.itba.sds_2021_q1_g02.parsers;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

import java.util.Properties;

import static ar.edu.itba.sds_2021_q1_g02.parsers.CommandUtils.JAVA_OPT;

public final class CommandParser {
    public static final String FILE_PATH_ARG_NAME = "input";
    public static final String MAX_ITERATIONS_ARG_NAME = "iterations";

    private static final int DEFAULT_MAX_ITERATIONS = 10;

    private static CommandParser instance;

    private boolean parsed;
    private String inputPath;
    private int maxIterations;

    private CommandParser() {
        this.parsed = false;
    }

    public void parse(String[] args) throws ParseException {
        Properties properties = parseArgs(args);

        this.inputPath = properties.getProperty(FILE_PATH_ARG_NAME);

        if (properties.containsKey(MAX_ITERATIONS_ARG_NAME))
            this.maxIterations = Integer.parseInt(properties.getProperty(MAX_ITERATIONS_ARG_NAME));
        else
            this.maxIterations = DEFAULT_MAX_ITERATIONS;

        this.parsed = true;
    }

    public String getInputPath() throws IllegalStateException {
        this.assertParsed();
        return this.inputPath;
    }

    public int getMaxIterations() {
        return this.maxIterations;
    }


    private void assertParsed() throws IllegalStateException {
        if (!this.parsed)
            throw new IllegalStateException();
    }

    public static CommandParser getInstance() {
        if (instance == null)
            instance = new CommandParser();
        return instance;
    }

    private static Properties parseArgs(String[] args) throws ParseException {

        Option inputFilepathOption = new Option(JAVA_OPT, "specifies the input file's file path");
        inputFilepathOption.setArgName(FILE_PATH_ARG_NAME);
        inputFilepathOption.setRequired(true);

        Option maxIterationsOption = new Option(JAVA_OPT, "specifies the max iterations to be performed. Defaults to 10");
        maxIterationsOption.setArgName(MAX_ITERATIONS_ARG_NAME);
        maxIterationsOption.setRequired(false);

        return CommandUtils.parseCommandLine(
                args,
                inputFilepathOption
        );
    }
}
