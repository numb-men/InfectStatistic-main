import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Lib
 * TODO 解析log文件
 *
 * @author 叶博宁
 * @version 0.2
 * @since xxx
 */

/**
 * Lib
 * 工具类
 *
 * @author ybn
 */
public class Lib {

    public static final String[] PROVINCE_LIST = {"全国", "安徽", "北京", "重庆", "福建", "甘肃", "广东", "广西", "贵州", "海南",
        "河北", "河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西",
        "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江"
    };

    public final static String CHINESE_CHARACTER = "[\u4e00-\u9fa5]";

    public final static String INFECTED = "感染患者";
    public final static String SUSPECTED = "疑似患者";
    public final static String CURED = "治愈";
    public final static String DEAD = "死亡";
    public final static String CONFIRMED = "确诊感染";
    public final static String INCREASED = "新增";
    public final static String REMOVED = "排除";

    /**
     * Validate format of date string boolean
     * 用来验证log文件名中对日期是否符合格式要求
     *
     * @param dateString date string
     * @return the boolean
     */
    public static boolean validateFormatOfDateString(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            format.setLenient(false);
            format.parse(dateString);
        } catch (ParseException parseException) {
            return false;
        }
        return true;
    }

    /**
     * Gets index from strings *
     * 顺序查找目标字符串在字符串数组中的索引
     *
     * @param strings strings
     * @param target  target
     * @return the index from strings
     */
    public static int getIndexFromStrings(String[] strings, String target) {
        for (int i = 0; i < strings.length; i++) {
            if (target.equals(strings[i])) {
                return i;
            }
        }
        return -1;
    }

    public static int extractNumberFromString(String string) {
        return Integer.parseInt(string.replaceAll(CHINESE_CHARACTER, "").trim());
    }
}

/**
 * Record
 *
 * @author ybn
 */
class Record {
    int infected = 0;
    int suspected = 0;
    int cured = 0;
    int dead = 0;

    public String getStringOfInfected() {
        return Lib.INFECTED + infected + "人";
    }

    public String getStringOfSuspected() {
        return Lib.SUSPECTED + suspected + "人";
    }

    public String getStringOfCured() {
        return Lib.CURED + cured + "人";
    }

    public String getStringOfDead() {
        return Lib.DEAD + dead + "人";
    }

    public void updateInfected(int number) {
        infected += number;
    }

    public void updateSuspected(int number) {
        suspected += number;
    }

    public void updateCured(int number) {
        cured += number;
        //治愈数增加了，感染数就要同步减少
        updateInfected(-number);
    }

    public void updateDead(int number) {
        dead += number;

        //治愈数增加了，感染数就要同步减少
        updateInfected(-number);
    }

    public boolean isEmpty() {
        return (infected == 0 && suspected == 0 && cured == 0 && dead == 0);
    }

    public void printAll() {
        System.out.printf("感染患者%d人 疑似患者%d人 治愈%d人 死亡%d人\n", infected, suspected, cured, dead);
    }
}

/**
 * Record container
 */
class RecordContainer {

    /**
     * Container
     */
    LinkedHashMap<String, Record> container;

    public void init() {
        container = new LinkedHashMap<>() {{
            for (String province : Lib.PROVINCE_LIST) {
                put(province, new Record());
            }
        }};
    }

    public void init(Command command) {

    }

    private void updateRecord(String province, String patientType, int number) {
        for (Map.Entry<String, Record> entry : container.entrySet()) {
            if (province.trim().equals(entry.getKey())) {
                switch (patientType.trim()) {
                    case Lib.CURED:
                        entry.getValue().updateCured(number);
                        break;
                    case Lib.DEAD:
                        entry.getValue().updateDead(number);
                        break;
                    case Lib.CONFIRMED:
                        entry.getValue().updateInfected(number);
                        entry.getValue().updateSuspected(-number);
                        break;
                    default:
                        System.err.println("3参数log错误，可能含有非法字符。");
                        System.exit(-2);
                        break;
                }
                break;
            }
        }
    }

    private void updateRecord(String province, String operation, String patientType, int number) {
        for (Map.Entry<String, Record> entry : container.entrySet()) {
            if (province.trim().equals(entry.getKey())) {
                switch (patientType.trim()) {
                    case Lib.INFECTED:
                        if (Lib.INCREASED.equals(operation.trim())) {
                            entry.getValue().updateInfected(number);
                        } else {
                            System.err.println("更新感染患者出错。");
                            System.exit(-1);
                        }
                        break;
                    case Lib.SUSPECTED:
                        switch (operation.trim()) {
                            case Lib.INCREASED:
                                entry.getValue().updateSuspected(number);
                                break;
                            case Lib.REMOVED:
                                entry.getValue().updateSuspected(-number);
                                break;
                            default:
                                System.err.println("更新疑似患者出错。");
                                System.exit(-1);
                                break;
                        }
                        break;
                    default:
                        System.err.println("四参数log出错");
                        break;
                }
                break;
            }
        }
    }

    private void updateRecord(String provinceOut, String patientType, int number, String provinceIn) {
        for (Map.Entry<String, Record> entryOut : container.entrySet()) {
            for (Map.Entry<String, Record> entryIn : container.entrySet()) {
                if (provinceOut.trim().equals(entryOut.getKey()) && provinceIn.equals(entryIn.getKey())) {
                    switch (patientType) {
                        case Lib.INFECTED:
                            entryOut.getValue().updateInfected(-number);
                            entryIn.getValue().updateInfected(number);
                            break;
                        case Lib.SUSPECTED:
                            entryOut.getValue().updateSuspected(-number);
                            entryIn.getValue().updateSuspected(number);
                            break;
                        default:
                            System.err.println("5参数log出错");
                            break;
                    }
                }
                break;
            }
        }
    }

    public void parseSingleLine(String line) {
        //将一行log用空格分隔成字符串数组
        String[] log = line.split(" ");

        switch (log.length) {
            case 3:
                updateRecord(log[0], log[1], Lib.extractNumberFromString(log[2]));
                break;
            case 4:
                //确诊感染
                if (Lib.CONFIRMED.equals(log[2].trim())) {
                    updateRecord(log[0], log[2].trim(), Lib.extractNumberFromString(log[3]));
                } else {
                    updateRecord(log[0], log[1], log[2], Lib.extractNumberFromString(log[3]));
                }
                break;
            case 5:
                updateRecord(log[0], log[1], Lib.extractNumberFromString(log[4]), log[3]);
                break;
            default:
                System.err.println("日志格式可能有错。");
                break;
        }
    }

    private boolean exists(String province) {
        for (Map.Entry<String, Record> entry : container.entrySet()) {
            if (province.equals(entry.getKey())) {
                return true;
            }
        }
        return false;
    }

    public void printRecords() {
        for (Map.Entry<String, Record> entry : container.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                System.out.print(entry.getKey() + " ");
                entry.getValue().printAll();
            }
        }
    }
}

/**
 * Argument parser
 */
class ArgumentParser {

    /**
     * COMMAND_LIST
     */
    public static final HashSet<String> COMMAND_LIST = new HashSet<>() {{
        add("-date");
        add("-type");
        add("-province");
        add("-log");
        add("-out");
    }};

    /**
     * Original arguments
     */
    String[] originalArguments;

    /**
     * Argument parser
     *
     * @param args args
     */
    public ArgumentParser(String[] args) {
        this.originalArguments = args;
    }

    /**
     * Make command command
     *
     * @return the command
     */
    public Command makeCommand() {
        String date = getDate();
        String logPath = originalArguments[getIndexOfCommand("-log") + 1];
        String outPah = originalArguments[getIndexOfCommand("-out") + 1];
        ArrayList<String> patientType = getPatientType();
        ArrayList<String> provinceList = getProvinces();

        return new Command(logPath, outPah, date, patientType, provinceList);
    }

    public FileTools makeFileTools() {
        String date = getDate();
        String logPath = originalArguments[getIndexOfCommand("-log") + 1];
        String outPah = originalArguments[getIndexOfCommand("-out") + 1];

        return new FileTools(date, logPath, outPah);
    }

    private int getIndexOfCommand(String command) {
        return Lib.getIndexFromStrings(originalArguments, command);
    }

    private String getDate() {

        int index = getIndexOfCommand("-date");

        if (index < 0) {
            return "null";
        }

        if (Lib.validateFormatOfDateString(originalArguments[index + 1])) {
            return originalArguments[index + 1];
        } else {
            System.err.println("-date:参数错误，无效的日期格式。");
            return "null";
        }
    }

    @NotNull
    private ArrayList<String> getPatientType() {

        int index = getIndexOfCommand("-type");
        //index<0表明命令行参数中不含-type命令，将所有类型都写入
        if (index < 0) {
            return new ArrayList<>(4) {{
                add(Lib.INFECTED);
                add(Lib.SUSPECTED);
                add(Lib.CURED);
                add(Lib.DEAD);
            }};
        }
        //如果args中-type的下一个元素也是一条命令选项，则表明-type命令没有参数，报错
        if (COMMAND_LIST.contains(originalArguments[index + 1])) {
            System.err.println("-type:参数不能为空！");
            System.exit(-1);
        }

        HashMap<String, String> patientTypeMap = new HashMap<>(4) {{
            put("ip", Lib.INFECTED);
            put("sp", Lib.SUSPECTED);
            put("cure", Lib.CURED);
            put("dead", Lib.DEAD);
        }};
        ArrayList<String> patientTypeList = new ArrayList<>();

        while (true) {
            //传入的参数和可用参数列表进行比对，get方法不返回null则取出参数对应的中文字符串加入List
            if (patientTypeMap.get(originalArguments[index + 1]) != null) {
                patientTypeList.add(patientTypeMap.get(originalArguments[index + 1]));
                index++;
            } else {
                break;
            }
        }

        return patientTypeList;
    }

    @NotNull
    private ArrayList<String> getProvinces() {

        int index = getIndexOfCommand("-province");
        //index<0表明命令行参数中不含-province命令，在province数组写默认的选项"全国"
        if (index < 0) {
            return new ArrayList<>(1) {{
                add("全国");
            }};
        }
        //如果args中-type的下一个元素也是一条命令选项，则表明-type命令没有参数，报错
        if (COMMAND_LIST.contains(originalArguments[index + 1])) {
            System.err.println("-province:参数不能为空！");
            System.exit(-1);
        }

        ArrayList<String> provinceList = new ArrayList<>();

        while (true) {
            //传入的参数和可用参数列表进行比对，get方法不返回null则取出参数对应的中文字符串加入List
            if (Lib.getIndexFromStrings(Lib.PROVINCE_LIST, originalArguments[index + 1]) >= 0) {
                provinceList.add(originalArguments[index + 1]);
                index++;
            } else {
                break;
            }
        }

        return provinceList;
    }
}

/**
 * Arguments
 */
class Command {

    /**
     * Date
     */
    String date;
    /**
     * Log path
     */
    String logPath;
    /**
     * Out path
     */
    String outPath;
    /**
     * Type
     */
    ArrayList<String> type;
    /**
     * Province
     */
    ArrayList<String> province;

    /**
     * Command
     *
     * @param logPath      log path
     * @param outPath      out path
     * @param date         date
     * @param patientType  patient type
     * @param provinceList province list
     */
    Command(String logPath, String outPath, String date, ArrayList<String> patientType, ArrayList<String> provinceList) {
        this.date = date;
        this.logPath = logPath;
        this.outPath = outPath;
        this.type = patientType;
        this.province = provinceList;
    }

    /**
     * Gets file name filter *
     *
     * @return the file name filter
     */
    public String getFileNameFilter() {
        //只有在确定命令中有-date参数才能调用这个方法
        return date + ".log.txt";
    }

    /**
     * Show
     */
    public void dump() {
        System.out.println(date);
        for (String s : type) {
            System.out.println(s);
        }
        for (String s : province) {
            System.out.println(s);
        }
    }
}

/**
 * File tools
 */
class FileTools {

    /**
     * LOG_FILTER
     * 用正则表达式比转换为Date对象更快一点
     */
    public final static String FILE_NAME_FILTER = "(19|20)[0-9][0-9]-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01]).log.txt";

    /**
     * Date
     */
    String newestFileName = "";
    /**
     * Log path
     * log文件存放的目录
     */
    String logPath;
    /**
     * Out path
     * 统计结果输出的完整路径（包括文件名）
     */
    String outPath;
    ArrayList<String> fileList;

    public FileTools(String date, String logPath, String outPath) {

        this.logPath = logPath;
        //补上一个"/"防止后续读取文件时出错
        if (!this.logPath.endsWith("/")) {
            this.logPath += "/";
        }
        this.outPath = outPath;
        if ("null".equals(date)) {
            initFileList();
        } else {
            this.newestFileName = date + ".log.txt";
            initFileListWithDateLimit();
        }
    }

    private void initFileList() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(logPath))) {
            fileList = new ArrayList<>() {{
                for (Path path : stream) {
                    if (path.getFileName().toString().matches(FILE_NAME_FILTER)) {
                        add(path.getFileName().toString());
                    }
                }
            }};
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initFileListWithDateLimit() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(logPath))) {
            fileList = new ArrayList<>() {{
                for (Path path : stream) {
                    String name = path.getFileName().toString();
                    if (name.matches(FILE_NAME_FILTER) && name.compareTo(newestFileName) <= 0) {
                        add(path.getFileName().toString());
                    }
                }
            }};
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile(RecordContainer container) {
        try {
            //makeRecordContainer
            for (String fileName : fileList) {
                BufferedReader reader = Files.newBufferedReader(Paths.get(logPath + fileName));
                String read;
                while ((read = reader.readLine()) != null) {
                    if (read.startsWith("//")) {
                        continue;
                    }
                    container.parseSingleLine(read);
                }
            }
            container.printRecords();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
