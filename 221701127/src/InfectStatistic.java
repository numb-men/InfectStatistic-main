import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * InfectStatistic
 *
 * @author XTG-L
 * @version 0.2
 * @since 2020-2-8
 */
class InfectStatistic {
    public static void main(String[] args) {
        CmdList cmd = new CmdList(args);
        Log log = new Log(cmd);
        ArrayList<String> as = log.getLogs();
        for (String string : as) {
            System.out.println(string);
        }
    }
}

/**
 * CmdArgs
 * 
 * 处理命令
 */
class CmdArgs {
    protected String[] args = null;

    /**
     * 传入命令行参数数组构造
     * 
     * @param args
     */
    CmdArgs(String[] args) {
        this.args = args;
    }

    /**
     * 获取命令
     * @return
     */
    String[] getArgs() {
        return this.args;
    }

    /**
     * 获取命令
     * 
     * @return
     */
    String getCmd() {
        String cmd = new String(args[0]);
        for (int i = 1; i < args.length; i++) {
            cmd += " " + args[i];
        }
        return cmd;
    }

    /**
     * 获取某个命令行参数的值，多个值只会返回第一个
     * 
     * @param key
     * @return
     */
    String argVal(String key) {
        return argVals(key).get(0);
    }

    /**
     * 获取某个命令行参数的值，返回列表
     * 
     * @param key
     * @return
     */
    ArrayList<String> argVals(String key) {
        if (!hasValue(key)) {
            System.out.println(key + " no value");
            return null;
        }
        ArrayList<String> values = new ArrayList<>();
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(key)) {
                for (int j = i + 1; j < args.length; j++) {
                    if (args[j].charAt(0) == '-') {
                        break;
                    }
                    values.add(args[j]);
                }
            }
        }
        return values;
    }

    /**
     * 判断该命令是否有对应的参数
     * 
     * @param key
     * @return
     */
    boolean hasValue(String key) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(key)) {
                if(i + 1 < args.length) {
                    if (args[i + 1].charAt(0) != '-') {
                        return true;
                    } else {
                        break;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断输入的命令是否符合规范
     * 
     * @return
     */
    boolean isLegalCmd() {
        boolean result = false;
        String cmd = getCmd(); // 要判断的命令
        String[] patterns = { "list.*-log.*-out.*" }; // 命令集 包括命令及其必须的参数
        int i; // 命令在指令集中的位置
        // 获取命令在命令集中的位置 用于判断是什么命令
        for (i = 0; i < patterns.length; i++) {
            if (Pattern.matches(patterns[i], cmd)) {
                break;
            }
        }
        switch (i) {
            case 0: {
               if (new CmdList(args).isLegal()) {
                   result = true;
                }
                break;
          }
            default: {
                System.out.println("Wrong cmd");
                break;
            }
        }
        return result;
    }
}

/**
 * CmdList
 * 
 * 处理list命令
 */
class CmdList extends CmdArgs {
    private String logPath = null;
    private String outPath = null;

    /**
     * 传入list命令
     * 
     * @param cmd
     */
    CmdList(String[] args) {
        super(args);
        if(isLegal()) {
            logPath = argVal("-log");
            outPath = argVal("-out");
        }
    }

    /**
     * 获取日志文件的路径
     * 
     * @return
     */
    String getLogPath() {
        return this.logPath;
    }

    /**
     * 获取输出文件的路径
     * 
     * @return
     */
    String getOutPath() {
        return this.outPath;
    }

    /**
     * 判断list命令是否符合规范
     * 
     * @return
     */
    boolean isLegal() {
        boolean result = false;
        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
            case "-log":
            case "-out":
            case "-type":
            case "-province": {
                if (args[++i].charAt(0) == '-') {
                    System.out.println(args[i - 1] + " 选项参数有误或没有参数");
                    return result;
                }
                break;
            }
            default: {
                break;
            }
            }
        }
        result = true;
        return result;
    }
}

/**
 * Log
 * 
 * 处理日志文件
 */
class Log {
    private String logPath = null;
    private File logDir = null;
    private String[] filesName = null;
    private ArrayList<String> logs = new ArrayList<>();
    private String ignore = "//.*";

    /**
     * 传入命令类并初始化日志文件的路径和文件列表
     * 
     * @param cmd
     */
    Log(CmdList cmdList) {
        logPath = cmdList.getLogPath();
        logDir = new File(logPath);
        if (logDir.isDirectory()) {
            this.filesName = logDir.list();
            try {
                readLog(logPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取日志文件的文件名
     * 
     * @return
     */
    String[] getFilesName() {
        return this.filesName;
    }

    void readLog(String logPath) throws IOException {
        BufferedReader br = null;
        String line = null;
        for (String name : filesName) {
            FileInputStream fis = new FileInputStream(logPath + name);
            InputStreamReader isr = new InputStreamReader(fis, "utf-8");
            br = new BufferedReader(isr);
            while((line = br.readLine()) != null) {
                if(delIgnore(line) != null) {
                    logs.add(line);
                }
            }
            br.close();
        }
    }

    ArrayList<String> getLogs() {
        return this.logs;
    }

    String delIgnore(String line) {
        String result = line;
        if(Pattern.matches(ignore, line)) {
            result = null;
        }
        return result;
    }

    class Region {
        private String name;
        private int ip, sp, cure, dead; //感染患者， 疑似患者， 治愈， 死亡患者

        /**
         * 获取地区名称
         * @return
         */
        String getName() {
            return this.name;
        }

        /**
         * 获取地区感染患者人数
         * @return
         */
        int getIp() {
            return this.ip;
        }

        /**
         * 获取地区疑似患者人数
         * @return
         */
        int getSp() {
            return this.sp;
        }

        /**
         * 获取地区治愈人数
         * @return
         */
        int getCure() {
            return this.cure;
        }

        /**
         * 获取地区死亡患者人数
         * @return
         */
        int getDead() {
            return this.dead;
        }
    }
}