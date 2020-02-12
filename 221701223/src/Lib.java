import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


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
 *
 * @author ybn
 */
public class Lib {

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
}

/**
 * Record
 */
class Record {
    /**
     * Int infected
     */
    Integer intInfected = 0;
    /**
     * Int suspected
     */
    Integer intSuspected = 0;
    /**
     * Int cured
     */
    Integer intCured = 0;
    /**
     * Int dead
     */
    Integer intDead = 0;

    /**
     * Str infected
     */
    String strInfected = "";
    /**
     * Str suspected
     */
    String strSuspected = "";
    /**
     * Str cured
     */
    String strCured = "";
    /**
     * Str dead
     */
    String strDead = "";

    /**
     * Gets record string *
     *
     * @param record record
     * @return the record string
     */
    public static String getRecordString(Record record) {
        return "感染患者" + record.strInfected + record.intInfected + "人 疑似患者"
            + record.strSuspected + record.intSuspected + "人 治愈" + record.strCured
            + record.intCured + "人 死亡" + record.strDead + record.intDead;
    }

    /**
     * Update record *
     *
     * @param strAttribute str attribute
     * @param intAttribute int attribute
     * @param value        value
     */
    public void updateRecord(String strAttribute, Integer intAttribute, Integer value) {
        if ("".equals(strAttribute) || value < 0) {
            strAttribute += value.toString();
        } else {
            strAttribute += "+" + value.toString();
        }
        intAttribute += value;
    }

//    String getRecordString(){
//        return "感染患者"infected.toString()
//    }
}

/**
 * Record container
 */
class RecordContainer {

    /**
     * Container
     */
    HashMap<String, Record> container;

    /**
     * Gets record by province *
     *
     * @param province province
     */
    void getRecordByProvince(String[] province) {

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
    private static final String[] PROVINCE_LIST = {"全国", "安徽", "澳门", "北京", "重庆", "福建", "甘肃", "广东", "广西", "贵州",
        "哈尔滨", "海南", "河北", "河南", "湖北", "湖南", "吉林", "江苏", "江西", "内蒙古", "宁夏", "青海", "山东", "山西",
        "山西", "上海", "四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江"
    };
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

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

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
        //index<0表明命令行参数中不含-type命令，就在type数组写一个"null"然后返回
        if (index < 0) {
            return new ArrayList<>(1) {{
                add("所有");
            }};
        }
        //如果args中-type的下一个元素也是一条命令选项，则表明-type命令没有参数，报错
        if (COMMAND_LIST.contains(originalArguments[index + 1])) {
            System.err.println("-type:参数不能为空！");
            System.exit(-1);
        }

        HashMap<String, String> patientTypeMap = new HashMap<>(4) {{
            put("ip", "感染患者");
            put("sp", "疑似患者");
            put("cure", "治愈");
            put("dead", "死亡");
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
            if (Lib.getIndexFromStrings(PROVINCE_LIST, originalArguments[index + 1]) >= 0) {
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
    public void show() {
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
    public final static String LOG_FILTER = "(19|20)[0-9][0-9]-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01]).log.txt";
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

    public void readFile() {
        try {
            for (String fileName : fileList) {
                BufferedReader reader = Files.newBufferedReader(Paths.get(logPath + fileName));
                System.out.println(fileName + ":");
                String read = null;
                while ((read = reader.readLine()) != null) {
                    String[] line = read.split(" ");
                    if ("//".equals(line[0])) {
                        continue;
                    }
                    for (String o : line) {
                        System.out.print(o + " ");
                    }
                    System.out.println();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initFileList() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(logPath))) {
            fileList = new ArrayList<>() {{
                for (Path path : stream) {
                    if (path.getFileName().toString().matches(LOG_FILTER)) {
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
                    if (name.matches(LOG_FILTER) && name.compareTo(newestFileName) <= 0) {
                        add(path.getFileName().toString());
                    }
                }
            }};
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show() {
        for (String s : fileList) {
            System.out.println(s);
        }
    }
}
