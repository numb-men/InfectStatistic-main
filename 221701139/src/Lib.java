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

}


