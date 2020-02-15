import argument.ArgParser;
import command.Command;
import command.CommandReceiver;
import command.ListCommand;
import reg.AllInformation;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 这个是调用者
 */
public class InfectStatistic {
    public static void main(String[] args) throws IOException {
        CommandReceiver commandReceiver = new CommandReceiver();
        Command list = new ListCommand(args,commandReceiver);
        list.execute();

    }
}
