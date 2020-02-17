import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version 1.0
 */
class InfectStatistic {

    /** 保存args的值 */
    public static String[] paramenterStrings;
    /** index为参数名在哈希表中的位置，值为参数名在paramenterStrings中的下标，不存在参数名则为-1 */
    public static int[]  indexOfParamenterStrings = {-1, -1, -1, -1, -1, -1};
    /** log 日志文件目录 */
    public static String inputDir = "";
    /** 统计到哪一天 */
    public static String toDateString = "";
    /** 输出路径&文件名 */
    public static String outputFileNameString = "";
    /** type的参数值 */
    public static String[] paramentersOfType = new String[10];
    /** province的参数值 */
    public static String[] paramentersOfProvince = new String[25];
    /** 用来存储省份的哈希表 */
    public static Hashtable<String, Province> hashtable = new Hashtable<String, Province>(40);

    /** Province类 */
    public class Province {

        /** 省份名称 */
        String provinceName;
        /** 感染患者 */
        long ip;
        /** 疑似患者 */
        long sp;
        /** 治愈 */
        long cure;
        /** 死亡 */
        long dead;

        Province(String provinceName, int ip, int sp, int cure, int dead) {
            this.provinceName = provinceName;
            this.ip = ip;
            this.sp = sp;
            this.cure = cure;
            this.dead = dead;
        }
        public void increaseIp(int newIpNum) {
            ip += newIpNum;
        }
        public void decreaseIp(int ipNum) {
            ip -= ipNum;
        }
        public void increaseSp(int newSpNum) {
            sp += newSpNum;
        }
        public void decreaseSp(int spNum) {
            sp -= spNum;
        }
        public void increaseCure(int newCureNum) {
            cure += newCureNum;
        }
        public void increaseDead(int newDeadNum) {
            dead += newDeadNum;
        }
        public String getProvinceName() {
            return provinceName;
        }
        public long getIp() {
            return ip;
        }
        public long getSp() {
            return sp;
        }
        public long getCure() {
            return cure;
        }
        public long getDead() {
            return dead;
        }
        /** description：打印全部统计的数据结果 */
        public String getDefaultResult() {
            String resString = provinceName + ' ' + "感染患者" + ip + "人" + ' ' + "疑似患者" + sp + "人" + ' ' + "治愈" + cure
                    + "人" + ' ' + "死亡" + dead + "人";
            return resString;
        }
        /** description：按指定参数值要求给出结果 */
        public String getAppointResult(String[] paramentersOfType) {
            String resString = provinceName + ' ';
            for(int i=0; paramentersOfType[i] != null; i++) {
                switch (paramentersOfType[i]) {
                    case "ip":
                        resString += "感染患者" + ip + "人" + ' ';
                        break;
                    case "sp":
                        resString += "疑似患者" + sp + "人" + ' ';
                        break;
                    case "cure":
                        resString += "治愈" + cure + "人" + ' ';
                        break;
                    case "dead":
                        resString += "死亡" + dead + "人" + ' ';
                        break;
                    default:
                        break;
                }
            }
            return resString;
        }
    }

    /** description:关于操作单行字符串（从文本读入的一行数据）的一些方法 */
    static class OperateLineString {

        /** description：获取一个字符串前的数字 */
        public static int getNumber(String string) {
            for (int i=0,len=string.length(); i < len; i++) {
                if (Character.isDigit(string.charAt(i))) {
                    ;
                } else {
                    string = string.substring(0, i);
                    break;
                }
            }

            return Integer.parseInt(string);
        }
        /** description：得到要修改数据的省份名称modifyProvinceName */
        public static String[] getProvince(String[] strings) {
            int len = strings.length;
            String[] resStrings = new String[2];
            if (len == 3 || len == 4) {
                resStrings[0] = strings[0];
                resStrings[1] = "";
            } else if (len == 5) {
                resStrings[0] = strings[0];
                resStrings[1] = strings[3];
            }
            return resStrings;
        }
        /**  description：判断操作类型 */
        public static int getOperateType(String[] strings) {
            int len = strings.length;
            int res = 0;
            if (len == 3) {
                if (strings[1].equals("死亡")) {
                    res = 1;
                } else if (strings[1].equals("治愈")) {
                    res = 2;
                }
            } else if (len == 4) {
                if (strings[1].equals("新增")) {
                    if (strings[2].equals("感染患者")) {
                        res = 3;
                    } else if (strings[2].equals("疑似患者")) {
                        res = 4;
                    }
                } else if (strings[1].equals("排除")) {
                    res = 5;
                } else {
                    res = 6;
                }
            } else {
                if (strings[1].equals("感染患者")) {
                    res = 7;
                } else {
                    res = 8;
                }
            }
            return res;
        }
        /** description：简单判断该行是注释行，仅判断前两个字符"//"，如果是空行也跳过 */
        public static boolean isNotes(String lineString) {
            if (lineString.equals("") || lineString.charAt(0) == '/' && lineString.charAt(1) == '/') {
                return true;
            } else {
                return false;
            }
        }
    }

    /** description:获取输入文件的相关方法 */
    static class GetFile {

        /** description：取得所有log中最大的日期 */
        public static Date getMaxDate(String[] nameStrings) {
            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
            String maxDateString = "2020-01-01";
            Date maxDate = null;
            try {
                maxDate = dFormat.parse(maxDateString);
                Date tmpDate = new Date();  //性能优化点1
                for(int i=0, len=nameStrings.length; i<len; i++) {
                    tmpDate = dFormat.parse(nameStrings[i]);
                    if(tmpDate.getTime() >= maxDate.getTime()) {
                        maxDate = tmpDate;
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return maxDate;
        }
        /** description：获取文件夹下指定日期前的所有文件文件名 */
        public static void getBeforeDateFileName(String path, String date, ArrayList<String> fileName) {
            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
            File file = new File(path);
            String[] nameStrings = file.list(); //取得所有文件名称
            Date maxDate = getMaxDate(nameStrings); //得到所有文件名称的最大日期
            if (nameStrings != null) {
                try {
                    String dateOfFileNameString = "";
                    Date dateOfFileNameDate = new Date();
                    Date limitDate = dFormat.parse(date);   //指定日期--统计到哪一天
                    for (int i = 0, len=nameStrings.length; i < len; i++) {
                        dateOfFileNameString = nameStrings[i].substring(0, nameStrings[i].indexOf('.')); //取得文件名中的日期****-**-**
                        dateOfFileNameDate = dFormat.parse(dateOfFileNameString);  //将string日期转为date格式
                        limitDate = dFormat.parse(date);   //指定日期--统计到哪一天
                        if(limitDate.getTime() > maxDate.getTime()) {
                            System.out.println("日期超出范围");
                        }else {
                            if (dateOfFileNameDate.getTime() <= limitDate.getTime()) {
                                fileName.add(nameStrings[i]);
                            }
                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        }
        /** description：取得指定目录中最大的日期 */
        public static String getMaxDateInputDir(String inputDir) {
            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
            File file = new File(inputDir);
            String[] nameStrings = file.list();
            Date maxDate = getMaxDate(nameStrings);
            return (dFormat.format(maxDate));
        }

    }

    /** description:涉及到Province的一些方法 */
    static class ProviceStatistic {

        /** description：统计省份数据 */
        public static void StatisticProvince(String lineString, Hashtable<String, Province> hashtable) {
            InfectStatistic infectStatistic = new InfectStatistic();
            String[] afterSplitStrings = lineString.split(" ");
            int numAfterSplit = afterSplitStrings.length; // 切割后数量
            int number = OperateLineString.getNumber(afterSplitStrings[numAfterSplit - 1]); // 一行信息中涉及的人数
            String[] provinceNameStrings = OperateLineString.getProvince(afterSplitStrings);   //需要修改数据的省份名称
            int operateType = OperateLineString.getOperateType(afterSplitStrings);    // 获得操作类型

            if (provinceNameStrings[1].equals("")) { // 只有一个省
                if (!hashtable.containsKey(provinceNameStrings[0])) { // 哈希表中没有该省
                    Province province = infectStatistic.new Province(provinceNameStrings[0], 0, 0, 0, 0);
                    ProviceStatistic.execOperate(province, province, operateType, number);
                    hashtable.put(province.getProvinceName(), province);
                } else {
                    Province province = hashtable.get(provinceNameStrings[0]);
                    ProviceStatistic.execOperate(province, province, operateType, number);
                }
            } else if (!provinceNameStrings[1].equals("")) { // 有两个省
                Province province1 = null;
                Province province2 = null;
                if (hashtable.containsKey(provinceNameStrings[0])) {
                    province1 = hashtable.get(provinceNameStrings[0]);
                    if(hashtable.containsKey(provinceNameStrings[1])){
                        province2 = hashtable.get(provinceNameStrings[1]);
                    }else{
                        province2 = infectStatistic.new Province(provinceNameStrings[1], 0, 0, 0, 0);
                        hashtable.put(provinceNameStrings[1], province2);
                    }
                }else if (!hashtable.containsKey(provinceNameStrings[0])) {
                    province1 = infectStatistic.new Province(provinceNameStrings[0], 0, 0, 0, 0);
                    if(hashtable.containsKey(provinceNameStrings[1])){
                        province2 = hashtable.get(provinceNameStrings[1]);
                    }else{
                        province2 = infectStatistic.new Province(provinceNameStrings[1], 0, 0, 0, 0);
                        hashtable.put(provinceNameStrings[1], province2);
                    }
                    hashtable.put(provinceNameStrings[0], province1);
                }
                ProviceStatistic.execOperate(province1, province2, operateType, number);
            }

        }
        /**  description：统计全国的数据 */
        public static void StatisticNation(Hashtable<String, Province> hashtable) {
            InfectStatistic infectStatistic = new InfectStatistic();
            Province Nation = infectStatistic.new Province("全国", 0, 0, 0, 0);
            Set set = hashtable.keySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()) {
                Object keyObject = iterator.next();
                Nation.ip += hashtable.get(keyObject).getIp();
                Nation.sp += hashtable.get(keyObject).getSp();
                Nation.cure += hashtable.get(keyObject).getCure();
                Nation.dead += hashtable.get(keyObject).getDead();
            }
            hashtable.put("全国", Nation);
        }
        /** description：根据省份和操作类型ID执行相应的操作 */
        public static void execOperate(Province province1, Province province2, int operateType, int number) {
            switch (operateType) {
                case 1:
                    province1.increaseDead(number);
                    province1.decreaseIp(number);
                    break;
                case 2:
                    province1.increaseCure(number);
                    province1.decreaseIp(number);
                    break;
                case 3:
                    province1.increaseIp(number);
                    break;
                case 4:
                    province1.increaseSp(number);
                    break;
                case 5:
                    province1.decreaseSp(number);
                    break;
                case 6:
                    province1.decreaseSp(number);
                    province1.increaseIp(number);
                    break;
                case 7:
                    province1.decreaseIp(number);
                    province2.increaseIp(number);
                    break;
                case 8:
                    province1.decreaseSp(number);
                    province2.increaseSp(number);
                    break;
                default:
                    break;
            }
        }
    }

    public static void main(String[] args) {
        //...
    }
}
