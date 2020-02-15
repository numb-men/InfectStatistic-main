import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
        new CmdArgs(args).action();
    }
}

/**
 * CmdArgs 处理命令
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

    void action() {
        switch (args[0]) {
            case "list": {
                new CmdList(args).action();
                break;
            }
            default: {
                break;
            }
        }
    }
    /**
     * 获取命令
     * 
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
                if (i + 1 < args.length) {
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
 * CmdList 处理list命令
 */
class CmdList extends CmdArgs {
    private String logPath = null; // 日志文件路径
    private String outPath = null; // 输出文件路径
    private File logDir = null; //日志文件的文件夹
    private String[] files = null;  //日志文件的文件名
    private ArrayList<String> logs = new ArrayList<>(); //用于储存日志文件的动态数组
    private ArrayList<Log.Region> regions = new ArrayList<>();  //用于储存地区类的动态数组
    private String date = null;
    private ArrayList<String> type = null;
    private ArrayList<String> province = null;

    /**
     * 传入list命令
     * 
     * @param args
     */
    CmdList(String[] args) {
        super(args);
        if (isLegal()) {
            this.logPath = argVal("-log");
            this.outPath = argVal("-out");
            if (getCmd().contains("-date")) {
                this.date = argVal("-date");
            }
            if (getCmd().contains("-type")) {
                this.type = argVals("-type");
            }
            if (getCmd().contains("-province")) {
                this.province = argVals("-province");
            }
        }
        logDir = new File(logPath);
        if (logDir.isDirectory()) {
            this.files = logDir.list();
            Arrays.sort(files);
            if (getCmd().contains("-date")) {
                Log.rmAfter(files, getDate() + ".log.txt");
            }
        }
    }

    void action() {
        try {
            readLog(this.logPath, this.files, this.logs);
            logToRegion(this.logs, this.regions);
            outLog(this.outPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 读取日志文件并储存在logs里，然后调用logToRegion将日志文件转换并保存
     * @param logPath
     * @throws IOException
     */
    static void readLog(String logPath, String[] files, ArrayList<String> logs) throws IOException {
        BufferedReader br = null;
        String line = null;
        for (String name : files) {
            if (name.equals("")) continue;
            FileInputStream fis = new FileInputStream(logPath + name);
            InputStreamReader isr = new InputStreamReader(fis, "utf-8");
            br = new BufferedReader(isr);
            while((line = br.readLine()) != null) {
                if(!Log.isIgnore(line)) {
                    logs.add(line);
                }
            }
            br.close();
        }
    }

    /**
     * 将日志文件转换为地区类并储存
     * @param logs
     */
    void logToRegion(ArrayList<String> logs, ArrayList<Log.Region> regions) {
        for (String log : logs) {
            String[] items = log.split("[\\s]+");
            int index = Log.indexOfRegion(regions, items[0]);
            int num = 0;
            for (String item : items) {
                if (item.endsWith("人")) {
                    num = Integer.valueOf(item.replace("人", ""));
                }
            }
            Log.Region region = regions.get(index);
            switch (items[1]) {
            case "新增": {
                if (items[2].equals("感染患者")) {
                    region.ip.add(num);
                } else if (items[2].equals("疑似患者")) {
                    region.sp.add(num);
                }
                break;
            }
            case "感染患者": {
                if (items[2].equals("流入")) {
                    region.ip.add(0 - num);
                    Log.Region another = regions.get(Log.indexOfRegion(regions, items[3]));
                    another.ip.add(num);
                }
                break;
            }
            case "疑似患者": {
                if (items[2].equals("确诊感染")) {
                    region.ip.add(num);
                    region.sp.add(0 - num);
                } else if (items[2].equals("流入")) {
                    region.sp.add(0 - num);
                    Log.Region another = regions.get(Log.indexOfRegion(regions, items[3]));
                    another.sp.add(num);
                }
                break;
            }
            case "治愈": {
                region.cure.add(num);
                region.ip.add(0 - num);
                break;
            }
            case "死亡": {
                region.dead.add(num);
                region.ip.add(0 - num);
                break;
            }
            case "排除": {
                region.sp.add(0 - num);
                break;
            }
            default: {
                break;
            }
            }
        }
    }

    /**
     * 向指定路径输出结果
     * @param outPath
     * @throws IOException
     */
    void outLog(String outPath) throws IOException {
        Log.Region.sort(regions);
        FileOutputStream fos = new FileOutputStream(outPath);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
        Log.Region all = Log.sumAll(regions);
        osw.write(regionToLine(all) + "\n");
        osw.flush();
        for (Log.Region region : regions) {
            if(isAssign(this.province, region)) {
                osw.write(regionToLine(region) + "\n");
                osw.flush();
            }
        }
        osw.close();
    }

    /**
     * 获取当前地区是否为指定输出的地区
     * @param province
     * @param region
     * @return
     */
    boolean isAssign(ArrayList<String> province, Log.Region region) {
        if (province == null) {
            return true;
        } else if (province.contains(region.name)) {
            return true;
        } else {
            return false;
        }
    }

    String regionToLine(Log.Region region) {
        String line = region.name;
        if (this.type != null) {
            if (this.type.contains("ip")) {
                line += " 感人患者" + Log.Region.regionItemToInt(region, region.ip) + "人";
            }
            if (this.type.contains("sp")) {
                line += " 疑似患者" + Log.Region.regionItemToInt(region, region.sp) + "人";
            }
            if (this.type.contains("cure")) {
                line += " 治愈" + Log.Region.regionItemToInt(region, region.cure) + "人";
            }
            if (this.type.contains("dead")) {
                line += " 死亡" + Log.Region.regionItemToInt(region, region.dead) + "人";
            }
        } else {
            line += " 感人患者" + Log.Region.regionItemToInt(region, region.ip) + "人";
            line += " 疑似患者" + Log.Region.regionItemToInt(region, region.sp) + "人";
            line += " 治愈" + Log.Region.regionItemToInt(region, region.cure) + "人";
            line += " 死亡" + Log.Region.regionItemToInt(region, region.dead) + "人";
        }
        return line;
    }

    /**
     * 获取日志文件的文件名
     * @return
     */
    String[] getFiles() {
        return this.files;
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
     * 获取日志文件输出路径
     * @return
     */
    String getOutPath() {
        return this.outPath;
    }

    /**
     * 获取命令中日期参数的值
     * 
     * @return
     */
    String getDate() {
        return this.date;
    }

    /**
     * 获取命令中类型参数的值
     * @return
     */
    ArrayList<String> getType() {
        return this.type;
    }

    /**
     * 获取命令中地区参数的值
     * @return
     */
    ArrayList<String> getProvince() {
        return this.province;
    }

    /**
     * 获取暂存的地区数据
     * @return
     */
    ArrayList<Log.Region> getRegions() {
        return this.regions;
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
 * 处理日志文件
 */
class Log {
    private String logPath = null;  //日志文件路径
    private File logDir = null; //日志文件的文件夹
    private String[] files = null;  //日志文件的文件名
    private static String ignore = "//.*"; //行忽略标志

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
            if (cmdList.getCmd().contains("-date")) {
                rmAfter(files, cmdList.getDate() + ".log.txt");
            }
        }
    }

    /**
     * 将指定日期之后的文件名置为""
     * @param date
     */
    static void rmAfter(String[] files, String date) {
        if(files.length > 0) {
            List<String> fileList = new ArrayList<String>(Arrays.asList(files));
            List<String> tmpList = new ArrayList<String>(Arrays.asList(files));
            for (String file : tmpList) {
                if (date.compareTo(file) < 0) {
                    int index = fileList.indexOf(file);
                    fileList.set(index, "");
                }
            }
            files = fileList.toArray(files);
        }
    }    

 

    /**
     * 判断一行日志是否为注释行
     * 
     * @param line
     * @return
     */
    static boolean isIgnore(String line) {
        boolean result = false;
        if (Pattern.matches(ignore, line)) {
            result = true;
        }
        return result;
    }

    /**
     * 获取全国地区总的数据
     * @param regions
     * @return
     */
    static Region sumAll(ArrayList<Region> regions) {
        Region all = new Log.Region("全国");
        for (Region region : regions) {
            for (int num : region.ip) {
                all.ip.add(num);
            }
            for (int num : region.sp) {
                all.sp.add(num);
            }
            for (int num : region.cure) {
                all.cure.add(num);
            }
            for (int num : region.dead) {
                all.dead.add(num);
            }
        }
        return all;
    }

    /**
     * 获取指定地区在动态数组中的下表，如果数组中没有，则向动态数组中加入该地区
     * 
     * @param name
     * @return
     */
    static int indexOfRegion(ArrayList<Region> regions, String name) {
        int index = -1;
        Region region = new Region(name);
        if (!regions.contains(region)) {
            region = new Region(name);
            regions.add(region);
        }
        index = regions.indexOf(region);
        return index;
    }

    /** 
     * Region 用于暂存日志文件内容的类对象
     */
    static class Region {
        protected String name; // 地区名称
        protected ArrayList<Integer> ip, sp, cure, dead; // 感染患者，疑似患者，治愈，死亡患者

        /**
         * 传入地区名称
         * 
         * @param name
         */
        Region(String name) {
            this.name = name;
            this.ip = new ArrayList<>();
            this.sp = new ArrayList<>();
            this.cure = new ArrayList<>();
            this.dead = new ArrayList<>();
        }

        /**
         * 重载equals函数，用于排序和判断
         * 
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
         * 对传入的地区的动态数组按名字进行降序排序
         * 
         * @param regions
         */
        public static void sort(ArrayList<Region> regions) {
            Collections.sort(regions, new Comparator<Region>() {
                @Override
                public int compare(Region r1, Region r2) {
                    return r1.name.compareTo(r2.name);
                }
            });
        }

        static int regionItemToInt(Region region, ArrayList<Integer> nums) {
            int result = 0;
            for (Integer num : nums) {
                result += num;
            }
            return result;
        }
    }
}