import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * InfectStatistic
 * @author XTG-L
 * @version 0.2
 * @since 2020-2-8
 */
class InfectStatistic {
    public static void main(String[] args) {
        System.out.println("done");
        CmdList cmd = new CmdList(args);
        Log log = new Log(cmd);
        String[] filesName = log.getFiles();
        for (String file : filesName) {
            System.out.println(file);
        }
        log.logToRegion(log.getLogs());
        ArrayList<Log.Region> regions = log.getRegions();
        for (Log.Region region : regions) {
            System.out.println(
                region.getName()+" "+
                region.getIp()+" "+
                region.getSp()+" "+
                region.getCure()+" "+
                region.getDead()
            );
        }
    }
}

/**
 * CmdArgs
 * 处理命令
 */
class CmdArgs {
    protected String[] args = null;

    /**
     * 传入命令行参数数组构造
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
     * @param key
     * @return
     */
    String argVal(String key) {
        return argVals(key).get(0);
    }

    /**
     * 获取某个命令行参数的值，返回列表
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
 * 处理list命令
 */
class CmdList extends CmdArgs {
    private String logPath = null;  //日志文件路径
    private String outPath = null;  //输出文件路径
    private String date = null;
    private ArrayList<String> type = null;
    private ArrayList<String> province = null;

    /**
     * 传入list命令
     * 
     * @param cmd
     */
    CmdList(String[] args) {
        super(args);
        if(isLegal()) {
            this.logPath = argVal("-log");
            this.outPath = argVal("-out");
            if(getCmd().contains("-date")) {
                this.date = argVal("-date");
            }
            if(getCmd().contains("-type")) {
                this.type = argVals("-type");
            }
            if(getCmd().contains("-province")) {
                this.province = argVals("-province");
            }
        }
    }

    /**
     * 获取日志文件的路径
     * @return
     */
    String getLogPath() {
        return this.logPath;
    }

    /**
     * 获取输出文件的路径
     * @return
     */
    String getOutPath() {
        return this.outPath;
    }

    String getDate() {
        return this.date;
    }

    ArrayList<String> getType() {
        return this.type;
    }

    ArrayList<String> getProvince() {
        return this.province;
    }

    /**
     * 判断list命令是否符合规范
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
 * 处理日志文件
 */
class Log {
    private String logPath = null;  //日志文件路径
    private File logDir = null; //日志文件的文件夹
    private String[] files = null;  //日志文件的文件名
    private ArrayList<String> logs = new ArrayList<>(); //用于储存日志文件的动态数组
    private String ignore = "//.*"; //行忽略标志
    private ArrayList<Region> regions = new ArrayList<>();  //用于储存地区类的动态数组

    /**
     * 传入命令类并初始化日志文件的路径和文件列表
     * @param cmd
     */
    Log(CmdList cmdList) {
        logPath = cmdList.getLogPath();
        logDir = new File(logPath);
        if (logDir.isDirectory()) {
            this.files = logDir.list();
            Arrays.sort(files);
            if(cmdList.getCmd().contains("-date")) {
                delAfter(cmdList.argVal("-date"));
            }
            try {
                readLog(logPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    String[] delAfter(String date) {
        String[] result = null;
        if(files.length > 0) {
            List<String> fileList = new ArrayList<String>(Arrays.asList(files));
            for (String file : fileList) {
                if(date.compareTo(file) < 0) {
                    fileList.remove(file);
                }
            }
            fileList.toArray(result);
        }
        return result;
    }

    /**
     * 获取日志文件的文件名
     * @return
     */
    String[] getFiles() {
        return this.files;
    }

    /**
     * 获取日志文件数据
     * @return
     */
    ArrayList<String> getLogs() {
        return this.logs;
    }

    /**
     * 获取地区数据
     * @return
     */
    ArrayList<Region> getRegions() {
        return this.regions;
    }

    /**
     * 读取日志文件并储存在logs里，然后调用logToRegion将日志文件转换并保存
     * @param logPath
     * @throws IOException
     */
    void readLog(String logPath) throws IOException {
        BufferedReader br = null;
        String line = null;
        for (String name : files) {
            FileInputStream fis = new FileInputStream(logPath + name);
            InputStreamReader isr = new InputStreamReader(fis, "utf-8");
            br = new BufferedReader(isr);
            // br = new BufferedReader(new FileReader(logPath + name));
            while((line = br.readLine()) != null) {
                if(!isIgnore(line)) {
                    logs.add(line);
                }
            }
            br.close();
        }
        logToRegion(logs);
    }

    /**
     * 判断一行日志是否为注释行
     * @param line
     * @return
     */
    boolean isIgnore(String line) {
        boolean result = false;
        if(Pattern.matches(ignore, line)) {
            result = true;
        }
        return result;
    }

    /**
     * 将日志文件转换为地区类并储存
     * @param logs
     */
    void logToRegion(ArrayList<String> logs) {
        for (String log : logs) {
            String[] items = log.split("[\\s]+");
            int index = indexOfRegion(items[0]);
            Region region = regions.get(index);
            switch (items[1]) {
                case "新增": {
                    if(items[2].equals("感染患者")) {
                        region.ip += Integer.valueOf(items[3].replace("人", ""));
                    } else if(items[2].equals("疑似患者")) {
                        region.sp += Integer.valueOf(items[3].replace("人", ""));
                    }
                    break;
                }
                case "疑似患者": {
                    if(items[2].equals("确认感染")) {
                        region.ip += Integer.valueOf(items[3].replace("人", ""));
                    } else if(items[2].equals("流入")) {
                        region.sp -= Integer.valueOf(items[4].replace("人", ""));
                        Region another = regions.get(indexOfRegion(items[3]));
                        another.sp += Integer.valueOf(items[4].replace("人", ""));
                    }
                    break;
                }
                case "感染患者": {
                    region.ip -= Integer.valueOf(items[4].replace("人", ""));
                    Region another = regions.get(indexOfRegion(items[3]));
                    another.ip += Integer.valueOf(items[4].replace("人", ""));
                    break;
                }
                case "治愈": {
                    region.setCure(region.getCure() + Integer.valueOf(items[2].replace("人", "")));
                    break;
                }
                case "死亡": {
                    region.setDead(region.getDead() + Integer.valueOf(items[2].replace("人", "")));
                    break;
                }
                case "排除": {
                    region.setSp(region.getSp() - Integer.valueOf(items[3].replace("人", "")));
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    /**
     * 获取指定地区在动态数组中的下表，如果数组中没有，则向动态数组中加入该地区
     * @param name
     * @return
     */
    int indexOfRegion(String name) {
        int index = -1;
        Region region = new Region(name);
        if(!regions.contains(region)) {
            region = new Region(name);
            regions.add(region);
        }
        index = regions.indexOf(region);
        return index;
    }

    /**
     * Region
     * 用于暂存日志文件内容的类对象
     */
    class Region {
        protected String name;   //地区名称
        protected int ip, sp, cure, dead; //感染患者，疑似患者，治愈，死亡患者

        /**
         * 传入地区名称
         * @param name
         */
        Region(String name) {
            this.name = name;
            this.ip = 0;
            this.sp = 0;
            this.cure = 0;
            this.dead = 0;
        }

        /**
         * 重载equals函数，用于排序和判断
         * @param obj
         * @return
         */
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Region) {
                Region region = (Region) obj;
                return this.name.equals(region.name);
            }
            return super.equals(obj);
        }

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
         * 设置地区感染患者人数
         * @param ip
         */
        void setIp(int ip) {
            this.ip = ip;
        }

        /**
         * 获取地区疑似患者人数
         * @return
         */
        int getSp() {
            return this.sp;
        }

        /**
         * 设置地区疑似患者人数
         * @param sp
         */
        void setSp(int sp) {
            this.sp = sp;
        }

        /**
         * 获取地区治愈人数
         * @return
         */
        int getCure() {
            return this.cure;
        }

        /**
         * 设置地区治愈人数
         * @param cure
         */
        void setCure(int cure) {
            this.cure = cure;
        }

        /**
         * 获取地区死亡患者人数
         * @return
         */
        int getDead() {
            return this.dead;
        }

        /**
         * 设置地区死亡人数
         * @param dead
         */
        void setDead(int dead) {
            this.dead = dead;
        }
    }
}