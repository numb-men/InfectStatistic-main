import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
     * DATE_REGEX
     */
    private final static String DATE_REGEX = "(19|20)[0-9][0-9]-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])";


    /**
     * Validate format of date string boolean
     *
     * @param dateString date string
     * @return the boolean
     */
    public static boolean validateFormatOfDateString(String dateString) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            format.setLenient(false);
//            format.parse(dateString);
//        } catch (ParseException parseException) {
//            return false;
//        }
//        return true;
        return dateString.matches(DATE_REGEX);
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
        return -10;
    }

    /**
     * Extract number from string int
     *
     * @param string string
     * @return the int
     */
    public static int extractNumberFromString(String string) {
        return Integer.parseInt(string.replaceAll(CHINESE_CHARACTER, ""));
    }
}

/**
 * Argument exception
 */
class ArgumentException extends Exception {
    /**
     * Argument exception
     *
     * @param message message
     */
    ArgumentException(String message) {
        super(message);
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

/**
 * Log format exception
 */
class LogFormatException extends Exception {
    /**
     * Log format exception
     */
    LogFormatException() {
        super();
    }
}

/**
 * Record
 * 单条记录类
 *
 * @author ybn
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

//    /**
//     * Print all
//     */
//    void printAll() {
//        System.out.printf("感染患者%d人 疑似患者%d人 治愈%d人 死亡%d人\n", infected, suspected, cured, dead);
//    }

//    /**
//     * Print with patient type filter *
//     * @deprecated
//     * @param filter filter
//     */
//    void printWithPatientTypeFilter(ArrayList<String> filter) {
//        for (String type : filter) {
//            System.out.print(" ");
//            switch (type) {
//                case Lib.INFECTED:
//                    System.out.print(getStringOfInfected());
//                    break;
//                case Lib.SUSPECTED:
//                    System.out.print(getStringOfSuspected());
//                    break;
//                case Lib.CURED:
//                    System.out.print(getStringOfCured());
//                    break;
//                case Lib.DEAD:
//                    System.out.print(getStringOfDead());
//                    break;
//                default:
//                    break;
//            }
//        }
//        System.out.println();
//    }

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
 * 记录容器类
 *
 * @author ybn
 */
class RecordContainer {

    /**
     * Map container
     */
    LinkedHashMap<String, Record> mapContainer;
    /**
     * Whole country
     * 一个独立的Record对象用来存储全国的记录
     */
    Record wholeCountry;

    /**
     * Patient types
     */
    ArrayList<String> patientTypes;
    /**
     * Province list
     */
    HashSet<String> provinceList;

    /**
     * Record container
     *
     * @param argumentContainer argument container
     */
    public RecordContainer(ArgumentContainer argumentContainer) {
        patientTypes = argumentContainer.patientTypes;
        provinceList = argumentContainer.provinceList;
    }

    /**
     * Init
     * 因为不能在构造方法中包含业务逻辑，故使用init方法来初始化容器
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
     * <省> 治愈/死亡 n人
     *
     * @param province    province
     * @param patientType patient type
     * @param number      number
     * @throws LogFormatException log format exception
     */
    private void updateRecord(String province, String patientType, int number) throws LogFormatException {
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
                throw new LogFormatException();
        }
    }

    /**
     * Update record *
     * <省> 新增 感染患者/疑似患者 n人
     * <省> 排除 疑似患者 n人
     *
     * @param province    province
     * @param operation   operation
     * @param patientType patient type
     * @param number      number
     * @throws LogFormatException log format exception
     */
    private void updateRecord(String province, String operation, String patientType, int number) throws LogFormatException {
        switch (patientType) {
            case Lib.INFECTED:
                if (Lib.INCREASED.equals(operation.trim())) {
                    mapContainer.get(province).updateInfected(number);
                    wholeCountry.updateInfected(number);
                } else {
                    throw new LogFormatException();
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
                        throw new LogFormatException();
                }
                break;
            default:
                throw new LogFormatException();
        }
    }

    /**
     * Update record *
     * <省1> 感染患者/疑似患者 流入 <省2> n人
     *
     * @param provinceOut province out
     * @param patientType patient type
     * @param number      number
     * @param provinceIn  province in
     * @throws LogFormatException log format exception
     */
    private void updateRecord(String provinceOut, String patientType, int number, String provinceIn) throws LogFormatException {
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
                throw new LogFormatException();
        }
    }

    /**
     * Parse single line *
     * 将一行log分割成字符串数组，根据数组元素的个数（3、4、5）分别调用不同的updateRecord方法
     *
     * @param line line
     * @throws LogFormatException log format exception
     */
    void parseSingleLine(String line) throws LogFormatException {
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
                throw new LogFormatException();
        }
    }

    /**
     * New output data set array list
     * 返回一个用于输出到文件的ArrayList数组，输出部分交给FileTools类，更好地保持了封装性。
     *
     * @return the array list
     */
    public ArrayList<String> newOutputDataSet() {
        mapContainer.put(Lib.PROVINCE_LIST[0], wholeCountry);

        return new ArrayList<>() {{
            //带-province参数的情况
            if (provinceList != null) {
                for (String province : Lib.PROVINCE_LIST) {
                    if (provinceList.contains(province)) {
                        add(province + mapContainer.get(province).getStringWithPatientTypeFilter(patientTypes));
                    }
                }
            } else {
                //不带-province参数的情况
                for (Map.Entry<String, Record> entry : mapContainer.entrySet()) {
                    if (!entry.getValue().empty()) {
                        add(entry.getKey() + entry.getValue().getStringWithPatientTypeFilter(patientTypes));
                    }
                }
            }
        }};
    }

    //    /**
//     * Print records
//     *
//     * @deprecated
//     */
//    public void printRecords() {
//        for (Map.Entry<String, Record> entry : mapContainer.entrySet()) {
//            if (entry.getValue().empty()) {
//                System.out.print(entry.getKey() + " ");
//                entry.getValue().printAll();
//            }
//        }
//    }

//    /**
//     * Print records filter by arguments *
//     *
//     * @param argumentContainer argument container
//     * @deprecated
//     */
//    public void printRecordsFilterByArguments(ArgumentContainer argumentContainer) {
//        for (String province : argumentContainer.provinceList) {
//            System.out.print(province);
//            System.out.println(mapContainer.get(province).getStringWithPatientTypeFilter(argumentContainer.patientTypes));
//        }
//    }
}

/**
 * Argument handler
 * 处理命令行参数的类
 *
 * @author ybn
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
     * 获得解析完成的参数容器
     *
     * @param originalArguments original arguments
     * @return the argument container
     * @throws ArgumentException argument exception
     */
    public static ArgumentContainer getArgumentContainer(String[] originalArguments) throws ArgumentException {
        //处理list命令的异常
        if (!"list".equals(originalArguments[0])) {
            throw new ArgumentException("缺少主命令\"list\"");
        }

        String date = getDate(originalArguments);
        String logPath;

        //处理-log命令的异常
        try {
            logPath = originalArguments[getIndexOfCommand(originalArguments, "-log") + 1];
            if (!Files.exists(Paths.get(logPath))) {
                throw new ArgumentException("-log:路径错误");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            //这是未输入-log命令的情况
            throw new ArgumentException("-log:命令必须包含");
        }

        Path outPath;
        //处理-out命令的异常
        try {
            outPath = Paths.get(originalArguments[getIndexOfCommand(originalArguments, "-out") + 1]);
            if (!outPath.isAbsolute()) {
                throw new ArgumentException("-out:路径错误");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            //这是未输入-out命令的情况
            throw new ArgumentException("-out:命令必须包含");
        }

        ArrayList<String> patientType = getPatientType(originalArguments);
        HashSet<String> provinceList = getProvinces(originalArguments);

        return new ArgumentContainer(logPath, outPath.toString(), date, patientType, provinceList);
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
     * 解析-date命令
     *
     * @param originalArguments original arguments
     * @return the date
     * @throws ArgumentException argument exception
     */
    private static String getDate(String[] originalArguments) throws ArgumentException {

        int index = getIndexOfCommand(originalArguments, "-date");

        if (index < 0) {
            return "null";
        }

        if (Lib.validateFormatOfDateString(originalArguments[index + 1])) {
            return originalArguments[index + 1];
        } else {
            throw new ArgumentException("-date:参数错误，无效的日期格式。");
        }
    }

    /**
     * Gets patient type *
     * 解析-type命令
     *
     * @param originalArguments original arguments
     * @return the patient type
     * @throws ArgumentException argument exception
     */
    private static ArrayList<String> getPatientType(String[] originalArguments) throws ArgumentException {

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
            throw new ArgumentException("-type:参数不能为空！");
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
            throw new ArgumentException("-province:参数不能为空！");
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
 *
 * @author ybn
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
    /**
     * Patient type registered
     */
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
 *
 * @author ybn
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
     * File list
     */
    ArrayList<String> fileList;

    /**
     * File tools
     *
     * @param arguments arguments
     * @throws ArgumentException argument exception
     */
    public FileTools(ArgumentContainer arguments) throws ArgumentException {

        this.logPath = arguments.logPath;
        this.outPath = arguments.outPath;

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
     * @throws ArgumentException argument exception
     */
    private void initFileListWithDateLimit(String newestFileName) throws ArgumentException {
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
                    throw new ArgumentException("-date:指定的日期不存在。");
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
                int lineNumber = 0;
                try {
                    BufferedReader reader = Files.newBufferedReader(Paths.get(logPath + fileName));
                    String read;
                    while ((read = reader.readLine()) != null) {
                        lineNumber++;
                        if (read.startsWith("//")) {
                            continue;
                        }
                        container.parseSingleLine(read);
                    }
                    reader.close();
                } catch (LogFormatException e) {
                    System.err.printf("检测到格式异常，文件：%s，行号：%d\n", fileName, lineNumber);
                }
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
            writer.write("// 该文档并非真实数据，仅供测试使用\n");
            writer.close();
            //System.out.println("输出文件保存于：" + outPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
