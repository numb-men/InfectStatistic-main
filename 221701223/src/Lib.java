import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    /**
     * PROVINCE_LIST
     */
    public static final String[] PROVINCE_LIST = {"全国", "安徽", "北京", "重庆", "福建", "甘肃", "广东", "广西", "贵州", "海南",
        "河北", "河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西",
        "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江"
    };

    /**
     * CHINESE_CHARACTER
     */
    public final static String CHINESE_CHARACTER = "[\u4e00-\u9fa5]";

    /**
     * INFECTED
     */
    public final static String INFECTED = "感染患者";
    /**
     * SUSPECTED
     */
    public final static String SUSPECTED = "疑似患者";
    /**
     * CURED
     */
    public final static String CURED = "治愈";
    /**
     * DEAD
     */
    public final static String DEAD = "死亡";
    /**
     * CONFIRMED
     */
    public final static String CONFIRMED = "确诊感染";
    /**
     * INCREASED
     */
    public final static String INCREASED = "新增";
    /**
     * REMOVED
     */
    public final static String REMOVED = "排除";

    /**
     * Validate format of date string boolean
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

    /**
     * Extract number from string int
     *
     * @param string string
     * @return the int
     */
    public static int extractNumberFromString(String string) {
        return Integer.parseInt(string.replaceAll(CHINESE_CHARACTER, "").trim());
    }
}

/**
 * Record
 */
class Record {
    /**
     * Infected
     */
    private int infected = 0;
    /**
     * Suspected
     */
    private int suspected = 0;
    /**
     * Cured
     */
    private int cured = 0;
    /**
     * Dead
     */
    private int dead = 0;

    /**
     * Gets string of infected *
     *
     * @return the string of infected
     */
    String getStringOfInfected() {
        return " " + Lib.INFECTED + infected + "人";
    }

    /**
     * Gets string of suspected *
     *
     * @return the string of suspected
     */
    String getStringOfSuspected() {
        return " " + Lib.SUSPECTED + suspected + "人";
    }

    /**
     * Gets string of cured *
     *
     * @return the string of cured
     */
    String getStringOfCured() {
        return " " + Lib.CURED + cured + "人";
    }

    /**
     * Gets string of dead *
     *
     * @return the string of dead
     */
    String getStringOfDead() {
        return " " + Lib.DEAD + dead + "人";
    }

    /**
     * Update infected *
     *
     * @param number number
     */
    void updateInfected(int number) {
        infected += number;
    }

    /**
     * Update suspected *
     *
     * @param number number
     */
    void updateSuspected(int number) {
        suspected += number;
    }

    /**
     * Update cured *
     *
     * @param number number
     */
    void updateCured(int number) {
        cured += number;
        //治愈数增加了，感染数就要同步减少
        updateInfected(-number);
    }

    /**
     * Update dead *
     *
     * @param number number
     */
    void updateDead(int number) {
        dead += number;

        //治愈数增加了，感染数就要同步减少
        updateInfected(-number);
    }

    boolean isEmpty() {
        return (infected == 0 && suspected == 0 && cured == 0 && dead == 0);
    }

    /**
     * Print all
     */
    void printAll() {
        System.out.printf("感染患者%d人 疑似患者%d人 治愈%d人 死亡%d人\n", infected, suspected, cured, dead);
    }

    /**
     * Print with patient type filter *
     *
     * @param filter filter
     */
    void printWithPatientTypeFilter(ArrayList<String> filter) {
        for (String type : filter) {
            System.out.print(" ");
            switch (type) {
                case Lib.INFECTED:
                    System.out.print(getStringOfInfected());
                    break;
                case Lib.SUSPECTED:
                    System.out.print(getStringOfSuspected());
                    break;
                case Lib.CURED:
                    System.out.print(getStringOfCured());
                    break;
                case Lib.DEAD:
                    System.out.print(getStringOfDead());
                    break;
                default:
                    break;
            }
        }
        System.out.println();
    }

    /**
     * Gets string with patient type filter *
     *
     * @param filter filter
     * @return the string with patient type filter
     */
    String getStringWithPatientTypeFilter(ArrayList<String> filter) {
        StringBuilder builder = new StringBuilder();
        for (String type : filter) {
            switch (type) {
                case Lib.INFECTED:
                    builder.append(getStringOfInfected());
                    break;
                case Lib.SUSPECTED:
                    builder.append(getStringOfSuspected());
                    break;
                case Lib.CURED:
                    builder.append(getStringOfCured());
                    break;
                case Lib.DEAD:
                    builder.append(getStringOfDead());
                    break;
                default:
                    break;
            }
        }
        return builder.toString();
    }
}

/**
 * Record container
 */
class RecordContainer {

    /**
     * Map container
     */
    LinkedHashMap<String, Record> mapContainer;
    /**
     * Whole country
     */
    Record wholeCountry;

    /**
     * Init
     */
    public void init() {
        mapContainer = new LinkedHashMap<>() {{
            for (String province : Lib.PROVINCE_LIST) {
                put(province, new Record());
            }
        }};
        wholeCountry = new Record();
    }

    /**
     * Update record *
     *
     * @param province    province
     * @param patientType patient type
     * @param number      number
     */
    private void updateRecord(String province, String patientType, int number) {
        switch (patientType) {
            case Lib.CURED:
                mapContainer.get(province).updateCured(number);
                wholeCountry.updateCured(number);
                break;
            case Lib.DEAD:
                mapContainer.get(province).updateDead(number);
                wholeCountry.updateDead(number);
                break;
            case Lib.CONFIRMED:
                mapContainer.get(province).updateInfected(number);
                mapContainer.get(province).updateSuspected(-number);
                wholeCountry.updateInfected(number);
                wholeCountry.updateSuspected(-number);
                break;
            default:
                System.err.println("3参数log错误，可能含有非法字符。");
                System.exit(-2);
                break;
        }

    }

    /**
     * Update record *
     *
     * @param province    province
     * @param operation   operation
     * @param patientType patient type
     * @param number      number
     */
    private void updateRecord(String province, String operation, String patientType, int number) {
        switch (patientType) {
            case Lib.INFECTED:
                if (Lib.INCREASED.equals(operation.trim())) {
                    mapContainer.get(province).updateInfected(number);
                    wholeCountry.updateInfected(number);
                } else {
                    System.err.println("更新感染患者出错。");
                    System.exit(-1);
                }
                break;
            case Lib.SUSPECTED:
                switch (operation.trim()) {
                    case Lib.INCREASED:
                        mapContainer.get(province).updateSuspected(number);
                        wholeCountry.updateSuspected(number);
                        break;
                    case Lib.REMOVED:
                        mapContainer.get(province).updateSuspected(-number);
                        wholeCountry.updateSuspected(-number);
                        break;
                    default:
                        System.err.println("更新疑似患者出错。");
                        System.exit(-1);
                        break;
                }
                break;
            default:
                System.err.println("四参数log出错");
                System.exit(-1);
                break;
        }
    }

    /**
     * Update record *
     *
     * @param provinceOut province out
     * @param patientType patient type
     * @param number      number
     * @param provinceIn  province in
     */
    private void updateRecord(String provinceOut, String patientType, int number, String provinceIn) {
        switch (patientType) {
            case Lib.INFECTED:
                mapContainer.get(provinceOut).updateInfected(-number);
                mapContainer.get(provinceIn).updateInfected(number);
                break;
            case Lib.SUSPECTED:
                mapContainer.get(provinceOut).updateSuspected(-number);
                mapContainer.get(provinceIn).updateSuspected(number);
                break;
            default:
                System.err.println("5参数log出错");
                break;
        }
    }

    /**
     * Parse single line *
     *
     * @param line line
     */
    void parseSingleLine(String line) {
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
        return container.get(province) != null;
    }

    public void printRecords() {
        for (Map.Entry<String, Record> entry : container.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                System.out.print(entry.getKey() + " ");
                entry.getValue().printAll();
            }
        }
    }

    /**
     * Print records filter by arguments *
     *
     * @param argumentContainer argument container
     */
    public void printRecordsFilterByArguments(ArgumentContainer argumentContainer) {
        for (String province : argumentContainer.provinceList) {
            System.out.print(province);
            container.get(province).printWithPatientTypeFilter(command.patientTypes);
        }
    }
}

/**
 * Argument handler
 */
class ArgumentHandler {

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
     * Gets argument container *
     *
     * @param originalArguments original arguments
     * @return the argument container
     */
    public static ArgumentContainer getArgumentContainer(String[] originalArguments) {
        String date = getDate(originalArguments);
        String logPath = originalArguments[getIndexOfCommand(originalArguments, "-log") + 1];
        //补上一个"/"防止后续读取文件时出错
        if (!logPath.endsWith("/")) {
            logPath += "/";
        }
        String outPah = originalArguments[getIndexOfCommand(originalArguments, "-out") + 1];
        ArrayList<String> patientType = getPatientType(originalArguments);
        ArrayList<String> provinceList = getProvinces(originalArguments);

        return new ArgumentContainer(logPath, outPah, date, patientType, provinceList);
    }

    /**
     * Gets index of command *
     *
     * @param originalArguments original arguments
     * @param command           command
     * @return the index of command
     */
    private static int getIndexOfCommand(String[] originalArguments, String command) {
        return Lib.getIndexFromStrings(originalArguments, command);
    }

    /**
     * Gets date *
     *
     * @param originalArguments original arguments
     * @return the date
     */
    private static String getDate(String[] originalArguments) {

        int index = getIndexOfCommand(originalArguments, "-date");

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

    /**
     * Gets patient type *
     *
     * @param originalArguments original arguments
     * @return the patient type
     */
    @NotNull
    private static ArrayList<String> getPatientType(String[] originalArguments) {

        int index = getIndexOfCommand(originalArguments, "-type");
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

    private static ArrayList<String> getProvinces(String[] originalArguments) {

        int index = getIndexOfCommand(originalArguments, "-province");
        //index<0表明命令行参数中不含-province命令，在province数组写默认的选项"全国"
        if (index < 0) {
            return null;
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
 * Argument container
 */
class ArgumentContainer {

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
     * Patient types
     */
    ArrayList<String> patientTypes;
    boolean patientTypeRegistered = false;
    /**
     * Province
     */
    ArrayList<String> provinceList;
    boolean provinceRegistered = false;

    /**
     * Command
     *
     * @param logPath      log path
     * @param outPath      out path
     * @param date         date
     * @param patientType  patient type
     * @param provinceList province list
     */
    ArgumentContainer(String logPath, String outPath, String date, ArrayList<String> patientType, ArrayList<String> provinceList) {
        this.date = date;
        this.logPath = logPath;
        this.outPath = outPath;
        this.patientTypes = patientType;
        this.provinceList = provinceList;
    }

    /**
     * Show
     */
    public void dump() {
        System.out.println(date);
        for (String s : patientTypes) {
            System.out.println(s);
        }
        for (String s : provinces) {
            System.out.println(s);
        }
    }
}

/**
 * File tools
 */
class FileTools {

    /**
     * FILE_NAME_FILTER
     */
    public final static String FILE_NAME_FILTER = "(19|20)[0-9][0-9]-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01]).log.txt";

    /**
     * Log path
     */
    String logPath;
    /**
     * Out path
     */
    String outPath;
    /**
     * Province list
     */
    HashSet<String> provinceList;
    /**
     * File list
     */
    ArrayList<String> fileList;

    /**
     * File tools
     *
     * @param arguments arguments
     */
    public FileTools(ArgumentContainer arguments) {

        this.logPath = arguments.logPath;
        this.outPath = arguments.outPath;
        this.provinceList = arguments.provinceList;

        if ("null".equals(arguments.date)) {
            initFileList();
        } else {
            initFileListWithDateLimit(arguments.date + ".log.txt");
        }
    }

    /**
     * Init file list
     */
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

    /**
     * Init file list with date limit *
     *
     * @param newestFileName newest file name
     */
    private void initFileListWithDateLimit(String newestFileName) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(logPath))) {
            fileList = new ArrayList<>() {{
                boolean dateOutOfBound = true;
                for (Path path : stream) {
                    String name = path.getFileName().toString();
                    if (name.matches(FILE_NAME_FILTER) && name.compareTo(newestFileName) <= 0) {
                        if (name.equals(newestFileName)) {
                            dateOutOfBound = false;
                        }
                        add(path.getFileName().toString());
                    }
                }
                if (dateOutOfBound) {
                    System.err.println("指定的日期不存在。");
                    System.exit(-1);
                }
            }};

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read file *
     *
     * @param container container
     */
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
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create output file *
     *
     * @param recordStrings record strings
     */
    public void createOutputFile(ArrayList<String> recordStrings) {
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(outPath), StandardCharsets.UTF_8);
            for (String line : recordStrings) {
                writer.write(line + "\n");
                writer.flush();
            }
            writer.close();
            System.out.println("输出文件保存于：" + outPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
