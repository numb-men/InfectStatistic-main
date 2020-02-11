/**
 * InfectStatistic
 *
 *
 * @author 欧振贵
 * @version 1.0
 * @since 2020.2.11
 */
import org.junit.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.System.exit;

class CommandErrorException extends Exception{

    public CommandErrorException(){}

    public CommandErrorException(String message) {
        super(message);
    }
}


public class InfectStatistic {
    static class ListCommand {
        private Map<String, List<String>> listMap = new HashMap<>();
        static List<String> command = new ArrayList<>();
        static {
            command.add("-log");
            command.add("-out");
            command.add("-date");
            command.add("-type");
            command.add("-province");
        }
    }

    ListCommand listCommand = new ListCommand();

    static List<String> commands = new ArrayList<>();

    static {
        commands.add("list");
    }

    public static String getTheLatestDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date());
    }

    public void commandAnalyze(String[] commands) throws CommandErrorException {
        if(!InfectStatistic.commands.contains(commands[0]))
        {
            throw new CommandErrorException();
        }
        if("list".equals(commands[0])) {
            String command = "";
            List<String> params = new ArrayList<>();
            int i;
            int count = 0;
            for (i = 1; i <= commands.length; i++) {
                if(i == commands.length)
                {
                    if(params.isEmpty())
                    {
                        throw new CommandErrorException();
                    }
                    listCommand.listMap.put(command,params);
                    break;
                }
                if(commands[i].charAt(0) == '-' && !ListCommand.command.contains(commands[i]))
                {
                    throw new CommandErrorException();
                }
                if(!commands[i].equals(command) && !"".equals(command) && ListCommand.command.contains(commands[i]))
                {
                    if(params.isEmpty())
                    {
                        throw new CommandErrorException();
                    }
                    listCommand.listMap.put(command,params);
                    params = new ArrayList<>();
                }
                if(ListCommand.command.contains(commands[i]))
                {
                    command = commands[i];
                }
                else
                {
                    params.add(commands[i]);
                    count++;
                }

            }
        }
    }
    public void fileRead() throws IOException {
        String input = listCommand.listMap.get("-log").get(0);
        String output = listCommand.listMap.get("-out").get(0);
        String tempString="";
        BufferedReader reader = new BufferedReader(new FileReader(input));
        //BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        if(listCommand.listMap.containsKey("-date"))
        {
            input += listCommand.listMap.get("-date") + ".log.txt";

        }
        else
        {
            input += getTheLatestDate() + ".log.txt";
        }
        if(!listCommand.listMap.containsKey("-type") && !listCommand.listMap.containsKey("-province")) {

            while ((tempString = reader.readLine()) != null) {
                System.out.println(stringProcessing(tempString));
                //writer.write();
                //writer.newLine();
            }
            //writer.flush();
            reader.close();
            //writer.close();
        }

    }

    public String stringProcessing(String ch)
    {
        return ch;
    }



    @Test
    public void unitTest()
    {
        List<String> list = new ArrayList<>();
        list.add("list -date 2020-01-22 -log D:\\java\\InfectStatistic-main\\221701227\\log -out D:/output.txt");
        //list.add("list -type sp cure -province 福建 -log D:/log/ -out D:/output.txt");
        //list.add("change -date 2020-01-22 -log D:/log/ -out D:/output.txt");
        //list.add("list -typesp cure -log D:/log/ -out D:/output.txt");
        //list.add("list -type sp cure -province -log D:/log/ -out D:/output.txt");
        for(String command:list) {
            String[] commands = command.split(" ");
            for (String ch:commands)
            {
                System.out.println(ch);
            }
            System.out.println("-------------------------------");
            try {
                commandAnalyze(commands);
                for (String key : listCommand.listMap.keySet()) {
                    System.out.print(key + ":");
                    for (String ch : listCommand.listMap.get(key)) {
                        System.out.print(ch + " ");
                    }
                    System.out.println("");
                }
                System.out.println("-------------------------------");
               // fileRead();
            }catch (CommandErrorException e){
                System.out.println("您输入的命令出现错误，请重新输入!");
                exit(-1);
            }
        }
    }



    public static void main(String[] args) {

    }
}
