import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lib
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
// 用来放自己的文件
public class Lib {
    public class ArgParser {
        // 命令名称
        private String command;
        // 参数列表
        private Map<String, List<String>> arguments;
        /**
         * 从给定的命令行参数构造命令
         * @param args
         */
        public ArgParser(String[] args) {
            this.command = args[0];
            this.arguments = new HashMap<>();
            // 根据参数列表创建参数映射
            for (int i = 1;i<args.length;i++) {
                // 如果以-开头代表是参数名
                if (args[i].startsWith("-")) {
                    String key = args[i];
                    int j = i+1;
                    List<String> vals = new ArrayList<>();

                    while (!args[j].startsWith("-")) {
                        vals.add(args[j]);
                        System.out.println(j);
                        j++;
                        if (j==args.length) {
                            break;
                        }
                    }
                    arguments.put(key,vals);
                }
            }
        }
        /**
         * 打印命令和参数列表
         *
         * @param
         * @return
         */
        public void printArg() {
            System.out.println("命令为:"+command);
            for (String key:arguments.keySet()) {
                System.out.println(key+"======="+arguments.get(key));
            }
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        public Map<String, List<String>> getArguments() {
            return arguments;
        }

        public void setArguments(Map<String, List<String>> arguments) {
            this.arguments = arguments;
        }
        // 得到全部的键
        public Set<String> getKeys() {
            return arguments.keySet();
        }

        public List<String> getVals(String key) {
            return  arguments.get(key);
        }

        /**
         * 判断是否有某个参数
         * @param key 需要判断的键名
         * @return
         */
        public boolean hasKey(String key) {
            return arguments.get(key)!=null;
        }
    }

    public interface Command {
        void execute();
    }

    public class CommandReceiver {

        // list命令
        public void list(String[] args) {
            // 封装参数
            argument.ArgParser argParser = new argument.ArgParser(args);
            argParser.printArg();
            // 根据参数调用不同的类
            String root = argParser.getVals("-log").get(0);
            String outFileName = argParser.getVals("-out").get(0);
            List<String> dates = argParser.getVals("-date");

            //执行完成,输出提示信息
            File path = new File(root);
            // 读取文件
            File[] files = path.listFiles();
            String filePath = null;

            if (dates!=null) {
                // 如果提供了要处理的文件的日期名
                for (File file:files) {
                    if (file.getName().contains(dates.get(0))) {
                        filePath = path + "/" + file.getName();
                    }
                    break;
                }
            }
            else {
                // 如果没有给定日期就使用文件的最后一个
                File lastFile = files[files.length-1];
                filePath = path+"/"+lastFile.getName();
            }

            AllInformation allInformation = new AllInformation();
            allInformation.processInfo(filePath);
            allInformation.writeIntoLog(outFileName,argParser);

            allInformation.printInfo();
//        System.out.println("list命令执行完成");
        }
        // 下面还可以有其他命令

    }

    public class ListCommand implements Command {
        private String[] args;  //参数数组
        private CommandReceiver receiver;

        /**
         * 构造函数
         * @param args
         * @param receiver
         */
        public ListCommand(String[] args, CommandReceiver receiver) {
            this.args = args;
            this.receiver = receiver;
        }

        @Override
        public void execute() {
            receiver.list(args);
        }

        /**
         * 判断有没有给定参数日期
         */
        public boolean judgeDate(ArgParser argParer) {
            return argParer.hasKey("-date");
        }

        // 将需要输出的转换为下标
        public List<Integer> judgeType(ArgParser argParser) {
            List<String> types = argParser.getVals("-type");
            List<Integer> newTypes = new ArrayList<>();

            // 如果没有给type参数就按顺序输出
            if (types==null) {
                newTypes.add(0);
                newTypes.add(1);
                newTypes.add(2);
                newTypes.add(3);
                return newTypes;
            }
            // 如果有给type参数就按type参数值的顺序输出
            for (String type:types) {
                if (type.equals("ip")) {
                    newTypes.add(0);
                }
                else if (type.equals("sp")) {
                    newTypes.add(1);
                }
                else if (type.equals("cure")) {
                    newTypes.add(2);
                }
                else if (type.equals("dead")){
                    newTypes.add(3);
                }
            }
            return newTypes;
        }
    }

    public class NoCommand implements Command {
        @Override
        public void execute() {

        }
    }

    public class RegOne {

        // <省> 新增 感染患者 n人
        private final String REGEX = "(\\S+) 新增 感染患者 (\\d+)人";

        // 返回一个省份对应信息的映射
        public Map<String, Integer[]> process(String line) {
            Map<String,Integer[]> map = new HashMap<>();
            // 如果匹配代表该行由这个正则表达式处理
            String[] result = line.split(" ");
            // 获取省份
            String province = result[0];
            // 获取人数
            Integer population = Integer.parseInt(result[3].substring(0,result[3].length()-1));

            // 封装成map
            Integer[] num = {population,0,0,0};
            map.put(province,num);
            map.put("全国",new Integer[]{population,0,0,0});
            return map;
        }
    }
    public class RegTwo {
        private final String REGEX = "(\\S+) 新增 疑似患者 (\\d+)人";

        // 返回一个省份对应信息的映射
        public Map<String, Integer[]> process(String line) {
            Map<String,Integer[]> map = new HashMap<>();
            String[] result = line.split(" ");
            // 获取省份
            String province = result[0];
            // 获取人数
            Integer population = Integer.parseInt(result[3].substring(0,result[3].length()-1));

            // 封装成map
            Integer[] num = {0,population,0,0};
            map.put(province,num);
            map.put("全国",new Integer[]{0,population,0,0});
            return map;
        }
    }
}


