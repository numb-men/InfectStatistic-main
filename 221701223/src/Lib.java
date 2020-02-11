import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


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

    public static final HashSet<String> COMMAND_LIST = new HashSet<String>() {{
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

    public Command makeCommand() {
        String date = getDate();
        String logPath = originalArguments[getIndexOfCommand("-log") + 1];
        String outPah = originalArguments[getIndexOfCommand("-out") + 1];
        ArrayList<String> patientType = getPatientType();
        ArrayList<String> provinceList = getProvinces();

        return new Command(logPath, outPah, date, patientType, provinceList);
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

    private ArrayList<String> getPatientType() {

        int index = getIndexOfCommand("-type");
        //index<0表明命令行参数中不含-type命令，就在type数组写一个"null"然后返回
        if (index < 0) {
            return new ArrayList<String>(1) {{
                add("所有");
            }};
        }
        //如果args中-type的下一个元素也是一条命令选项，则表明-type命令没有参数，报错
        if (COMMAND_LIST.contains(originalArguments[index + 1])) {
            System.err.println("-type:参数不能为空！");
            System.exit(-1);
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
            return new ArrayList<String>(1) {{
                add("全国");
            }};
        }
        //如果args中-type的下一个元素也是一条命令选项，则表明-type命令没有参数，报错
        if (COMMAND_LIST.contains(originalArguments[index + 1])) {
            System.err.println("-province:参数不能为空！");
            System.exit(-1);
        }

        ArrayList<String> provinceList = new ArrayList<String>();

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
    String log;
    String out;
    ArrayList<String> type;
    ArrayList<String> province;

    Command(String logPath, String outPath, String date, ArrayList<String> patientType, ArrayList<String> provinceList) {
        this.date = date;
        this.log = logPath;
        this.out = outPath;
        this.type = patientType;
        this.province = provinceList;
    }

    public void show() {
        System.out.println(date);
        System.out.println(log);
        System.out.println(out);
        for (String s : type) {
            System.out.println(s);
        }
        for (String s : province) {
            System.out.println(s);
        }
    }
}
