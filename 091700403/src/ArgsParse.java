import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
* Project Name:InfectStatistic-main2
* File Name:ArgsParse.java
* Package Name:
* Date:2020年2月12日 上午11:32:59
* Copyright (c) 2020 Doris. All Rights Reserved.
*
*/

/**
 * Description: Date:2020年2月12日 上午11:32:59 <br/>
 * 
 * @author Doris
 * @version
 * @see
 */
public class ArgsParse {
    Option log;// 必填

    Option out;// 必填

    Option data;

    Option type;// 多个参数

    Option province;// 多个参数

    Options options;

    String slog;// 必填

    String sout;// 必填

    String sdata;

    String stype;// 多个参数

    String sprovince;// 多个参数

    CommandLine cli;

    CommandLineParser cliParser;

    HelpFormatter helpFormatter;

    public ArgsParse() {
        // public Options addOption(String opt,
        // String longOpt,
        // boolean hasArg,
        // String description)
        log = new Option("l", "log", true, "log");
        out = new Option("o", "out", true, "out");
        data = new Option("d", "data", true, "data");
        type = new Option("t", "type", true, "type");
        province = new Option("p", "province", true, "pronvince");
        options = new Options();

        cli = null;
        cliParser = new DefaultParser();
        helpFormatter = new HelpFormatter();

        log.setRequired(true);
        out.setRequired(true);
        data.setRequired(false);
        type.setRequired(false);
        province.setRequired(false);

        options.addOption(log);
        options.addOption(out);
        options.addOption(data);
        options.addOption(type);
        options.addOption(province);
    }

    public void parse(String[] args) {
        try {
            cli = cliParser.parse(options, args);
        } catch (ParseException e) {

            // Auto-generated catch block
            e.printStackTrace();

        }
        if (cli.hasOption("log")) {
            slog = cli.getOptionValue("log");
        }

        if (cli.hasOption("out")) {
            sout = cli.getOptionValue("out");
        }

        if (cli.hasOption("data")) {
            sdata = cli.getOptionValue("data");

        }
        if (cli.hasOption("type")) {
            stype = cli.getOptionValue("type");
        }
        if (cli.hasOption("province")) {
            sprovince = cli.getOptionValue("province");
        }

    }

}
