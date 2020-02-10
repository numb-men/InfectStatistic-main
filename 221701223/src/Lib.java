import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


/**
 * Lib
 * TODO 解析命令行参数
 *
 * @author 叶博宁
 * @version 0.1
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

    private static final String[] PROVINCE_LIST = {"安徽", "澳门", "北京", "重庆", "福建", "甘肃", "广东", "广西", "贵州",
        "哈尔滨", "海南", "河北", "河南", "湖北", "湖南", "吉林", "江苏", "江西", "内蒙古", "宁夏", "青海", "山东", "山西",
        "山西", "上海", "四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江"
    };
    private static final String[] COMMAND_LIST = {"-date", "-type", "-province", "-log", "-out"};
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

    public Command makeCommand() {
        String date = getDate();
        String logPath = getArgumentByCommand("-log");
        String outPah = getArgumentByCommand("-out");
        ArrayList<String> patientType = getPatientType();

        return new Command(logPath, outPah, date, patientType);
    }

    private int getIndexOfCommand(String command) {
        return Arrays.binarySearch(originalArguments, command);
    }

    private String getArgumentByCommand(String arg) {

        int index = getIndexOfCommand(arg);
        return originalArguments[index + 1];
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
            System.err.println("-list参数错误，无效的日期格式。");
            return "null";
        }
    }

    private ArrayList<String> getPatientType() {

        int index = getIndexOfCommand("-type");
        if (index < 0) {
            return new ArrayList<String>(1) {{
                add("null");
            }};
        }
        HashMap<String, String> patientTypeMap = new HashMap<String, String>(4) {{
            put("ip", "感染患者");
            put("sp", "疑似患者");
            put("cure", "治愈");
            put("dead", "死亡");
        }};
        ArrayList<String> patientTypeList = new ArrayList<String>();

        while (true) {
            //传入的参数和可用参数列表进行比对，get方法不返回null则取出参数对应的中文字符串加入List
            if (patientTypeMap.get(originalArguments[index + 1]) != null) {
                patientTypeList.add(patientTypeMap.get(originalArguments[index + 1]));
                index++;
            } else if (Arrays.binarySearch(COMMAND_LIST, originalArguments[index]) < 0) {
                System.err.println("-type:参数" + originalArguments[index + 1] + "无效！");
                System.exit(-1);
                break;
            } else {
                System.err.println("-type:参数不能为空！");
                System.exit(-1);
                break;
            }
        }

        return patientTypeList;
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
    String log;
    String out;
    ArrayList<String> type;
    ArrayList<String> province;

    Command(String logPath, String outPath, String date, ArrayList<String> patientType) {
        this.date = date;
        this.log = logPath;
        this.out = outPath;
        this.type = patientType;
    }

    public void show() {
        System.out.println(date);
        System.out.println(log);
        System.out.println(out);
        for (String s : type) {
            System.out.println(s);
        }
    }
}
