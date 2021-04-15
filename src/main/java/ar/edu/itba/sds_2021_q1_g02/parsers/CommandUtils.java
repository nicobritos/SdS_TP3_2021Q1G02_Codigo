package ar.edu.itba.sds_2021_q1_g02.parsers;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Arrays;
import java.util.Properties;

public abstract class CommandUtils {
    public static final String JAVA_OPT = "D";

    public static Properties parseCommandLine(String[] args, Option... optionArray) throws ParseException {
        Options options = new Options();

        // Because of a bug in Commons-CLI non required options must
        // be inserted before required ones, otherwise they will be treated
        // as required arguments.
        Arrays.sort(optionArray, (o1, o2) -> {
            if (!o1.isRequired() && !o2.isRequired()) return 0;
            if (!o1.isRequired()) return -1;
            return 1;
        });

        for (Option option : optionArray) {
            option.setArgs(2);
            options.addOption(option);
        }

        return new DefaultParser().parse(options, args, true).getOptionProperties(JAVA_OPT);
    }
}
