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
 * @version 2.0
 */
class InfectStatistic {

    /** 保存args中的参数值 */
    public static String[] paramenterStrings;
    /** index为参数名在哈希表中的位置 */
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
        /** description：默认的数据统计结果 */
        public String getDefaultResult() {
            String resString = provinceName + ' ' + "感染患者" + ip + "人" + ' ' + "疑似患者" + sp + "人" + ' ' + "治愈" + cure
                    + "人" + ' ' + "死亡" + dead + "人";
            return resString;
        }
        /** description：按参数值指定要求的数据统计结果 */
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

    /** description:读取文本中的单行数据 */
    static class OperateLineString {

        /** description：获取字符串中对应参数值 */
        public static int getValue(String string) {
            for (int i=0,len=string.length(); i < len; i++) {
                if (!Character.isDigit(string.charAt(i))) {
                    string = string.substring(0, i);
                    break;
                }
            }

            return Integer.parseInt(string);
        }
        /** description：获取需修改数据的省份名称 */
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
        /**  description：根据命令行中参数判断操作类型 */
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
    }

    /** description:获取输入文件 */
    static class GetFile {

        /** description：获取log目录中最大的日期 */
        public static Date getMaxDate(String[] nameStrings) {
            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
            String maxDateString = "2020-01-01";
            Date maxDate = null;
            try {
                maxDate = dFormat.parse(maxDateString);
                Date tmpDate = new Date();
                for(int i=0, len=nameStrings.length; i<len; i++) {
                    tmpDate = dFormat.parse(nameStrings[i]);
                    if(tmpDate.getTime() >= maxDate.getTime()) {
                        maxDate = tmpDate;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return maxDate;
        }
        /** description：获取文件夹下指定日期前的所有文件文件名 */
        public static void getBeforeDateFileName(String path, String date, ArrayList<String> fileName) {
            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
            File file = new File(path);
            String[] nameStrings = file.list(); //取得所有文件名称
            Date maxDate = getMaxDate(nameStrings);
            if (nameStrings != null) {
                try {
                    String dateOfFileNameString = "";
                    Date dateOfFileNameDate = new Date();
                    Date limitDate = dFormat.parse(date);
                    for (int i = 0, len=nameStrings.length; i < len; i++) {
                        dateOfFileNameString = nameStrings[i].substring(0, nameStrings[i].indexOf('.')); //取得文件名中的日期****-**-**
                        dateOfFileNameDate = dFormat.parse(dateOfFileNameString);
                        limitDate = dFormat.parse(date);
                        if(limitDate.getTime() > maxDate.getTime()) {
                            System.out.println("日期超出范围");
                        }else {
                            if (dateOfFileNameDate.getTime() <= limitDate.getTime()) {
                                fileName.add(nameStrings[i]);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        /** description：获取指定目录中最大的日期 */
        public static String getMaxDateInputDir(String inputDir) {
            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
            File file = new File(inputDir);
            String[] nameStrings = file.list();
            Date maxDate = getMaxDate(nameStrings);
            return (dFormat.format(maxDate));
        }

    }

    /** description:各省份数据统计 */
    static class ProviceStatistic {

        /** description：统计省份数据 */
        public static void StatisticProvince(String lineString, Hashtable<String, Province> hashtable) {
            InfectStatistic infectStatistic = new InfectStatistic();
            String[] afterSplitStrings = lineString.split(" ");
            int numAfterSplit = afterSplitStrings.length;
            int number = OperateLineString.getValue(afterSplitStrings[numAfterSplit - 1]);
            String[] provinceNameStrings = OperateLineString.getProvince(afterSplitStrings);
            int operateType = OperateLineString.getOperateType(afterSplitStrings);

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
        /** description：根据省份和操作类型统计相应数据 */
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

    /** description:输出文件 */
    static class OutPutFile {

        /** description:遍历哈希表，打印所有信息 */
        public static void writeInfoOfHashtale(Hashtable<String, Province> hashtable,OutputStreamWriter outputStreamWriter) {
            List<Map.Entry<String,Province>> list = OperateHashTable.sortByHead(hashtable);
            Province province = null;
            try {
                for (Map.Entry entry : list){
                    province = (Province) entry.getValue();

                    if(paramentersOfType[0].equals("null")) {   //没有指定输出类型
                        outputStreamWriter.write(province.getDefaultResult() + "\r\n");
                        outputStreamWriter.flush();
                    }else {
                        outputStreamWriter.write(province.getAppointResult(paramentersOfType) + "\r\n");
                        outputStreamWriter.flush();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /**  description：写入文件 */
        public static void writeFile(Hashtable<String, Province> hashtable, FileOutputStream fileOutputStream,
                                     String[] paramentersOfType, String[] paramentersOfProvince,String[] commandLineStrings) {
            String endLineString = "// 该文档并非真实数据，仅供测试使用";

            InfectStatistic infectStatistic = new InfectStatistic();
            try {

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream,"UTF8");

                if(paramentersOfProvince[0].equals("null")) {   //没有指定省份

                    writeInfoOfHashtale(hashtable, outputStreamWriter);

                    outputStreamWriter.write(endLineString + "\r\n" );
                    outputStreamWriter.flush();
                }else { //指定省份
                    Hashtable<String, Province> requestProvinceHashtable = new Hashtable<String, InfectStatistic.Province>();
                    Province province = null;
                    for(int i=0; paramentersOfProvince[i] != null; i++) {
                        if(!hashtable.containsKey(paramentersOfProvince[i])) {  //哈希表中不存在
                            province = infectStatistic.new Province(paramentersOfProvince[i], 0, 0, 0, 0);
                            requestProvinceHashtable.put(paramentersOfProvince[i], province);
                        }else { //哈希表中存在
                            province = hashtable.get(paramentersOfProvince[i]);
                            requestProvinceHashtable.put(paramentersOfProvince[i], province);
                        }
                    }

                    writeInfoOfHashtale(requestProvinceHashtable, outputStreamWriter);

                    outputStreamWriter.write(endLineString + "\r\n" );
                    outputStreamWriter.flush();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** description:哈希表操作 */
    static class OperateHashTable {

        /** description：HashMap根据value获取key */
        public static int getKey(HashMap<Integer, String> map, String value) {
            int res = -1;
            for(int getKey:map.keySet()) {
                if(map.get(getKey).equals(value)) {
                    res = getKey;
                }
            }
            return res;
        }
        /** description：按城市首字母排序，“全国”置顶 */
        public static List<Map.Entry<String,Province>> sortByHead(Hashtable<String, Province> hashtable) {
            Hashtable<String, String> alphabetOfProvince = new Hashtable<String, String>(35);
            alphabetOfProvince.put("全国", "AAAQG");
            alphabetOfProvince.put("北京", "BJ");
            alphabetOfProvince.put("天津", "TJ");
            alphabetOfProvince.put("上海", "SH");
            alphabetOfProvince.put("重庆", "CQ");
            alphabetOfProvince.put("河北", "HB");
            alphabetOfProvince.put("山西", "SXA");
            alphabetOfProvince.put("辽宁", "LN");
            alphabetOfProvince.put("吉林", "JL");
            alphabetOfProvince.put("黑龙江", "HLJ");
            alphabetOfProvince.put("江苏", "JS");
            alphabetOfProvince.put("浙江", "ZJ");
            alphabetOfProvince.put("安徽", "AH");
            alphabetOfProvince.put("福建", "FJ");
            alphabetOfProvince.put("江西", "JX");
            alphabetOfProvince.put("山东", "SD");
            alphabetOfProvince.put("河南", "HN");
            alphabetOfProvince.put("湖北", "HB");
            alphabetOfProvince.put("湖南", "HN");
            alphabetOfProvince.put("广东", "GD");
            alphabetOfProvince.put("海南", "HN");
            alphabetOfProvince.put("四川", "SC");
            alphabetOfProvince.put("贵州", "GZ");
            alphabetOfProvince.put("云南", "YN");
            alphabetOfProvince.put("陕西", "SXB");
            alphabetOfProvince.put("甘肃", "GS");
            alphabetOfProvince.put("青海", "QH");
            alphabetOfProvince.put("台湾", "TW");
            alphabetOfProvince.put("内蒙古", "NMG");
            alphabetOfProvince.put("广西", "GX");
            alphabetOfProvince.put("西藏", "XZ");
            alphabetOfProvince.put("宁夏", "NX");
            alphabetOfProvince.put("新疆", "XZ");
            alphabetOfProvince.put("香港", "XG");
            alphabetOfProvince.put("澳门", "AM");

            List<Map.Entry<String,Province>> list = new ArrayList<>(hashtable.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<String, Province>>() {
                @Override
                public int compare(Map.Entry<String, Province> o1, Map.Entry<String, Province> o2) {
                    return alphabetOfProvince.get(o1.getKey()).compareTo(alphabetOfProvince.get(o2.getKey()));
                }
            });
            return list;
        }
    }

    /** description:命令行相关操作 */
    static class StartCMD {

        /** description:解析命令行中参数名和参数值并初始化相应参数 */
        public static void separateCMDInit(String[] args) {
            HashMap<Integer, String> paramenterHashMap = new HashMap<Integer, String>(5);
            paramenterHashMap.put(1, "-log");
            paramenterHashMap.put(2, "-out");
            paramenterHashMap.put(3, "-date");
            paramenterHashMap.put(4, "-type");
            paramenterHashMap.put(5, "-province");

            paramenterStrings = new String[args.length - 1];
            for(int i=1,len=args.length; i<len; i++) {
                paramenterStrings[i-1] = args[i];
            }
            int key;
            for(int i=0,len=paramenterStrings.length; i<len; i++) {
                key = OperateHashTable.getKey(paramenterHashMap, paramenterStrings[i]);
                if( key != -1) {
                    indexOfParamenterStrings[key] = i;
                }
            }
            paramentersOfType[0] = "null";
            paramentersOfProvince[0] = "null";

            for(int i=1; i<=5; i++) {
                if(indexOfParamenterStrings[i] != -1) {
                    if(i == 1) {    // -log
                        inputDir = paramenterStrings[indexOfParamenterStrings[i] + 1];
                        toDateString = GetFile.getMaxDateInputDir(inputDir);
                    }else if(i == 2) {  //-out
                        outputFileNameString = paramenterStrings[indexOfParamenterStrings[i] + 1];
                    }else if(i == 3) {  //-date
                        toDateString = paramenterStrings[indexOfParamenterStrings[i] + 1];
                    }else if(i == 4) {  //-type
                        String[] paramenterValues = new String[20];
                        int cnt = 0;
                        for(int j = indexOfParamenterStrings[i]+1;
                            j<paramenterStrings.length && OperateHashTable.getKey(paramenterHashMap, paramenterStrings[j])==-1; j++) {
                            paramenterValues[cnt++] = paramenterStrings[j];
                            paramentersOfType = paramenterValues;
                        }
                    }else if(i == 5) {  //-province
                        String[] paramenterValues = new String[20];
                        int cnt = 0;
                        for(int j = indexOfParamenterStrings[i]+1;
                            j<paramenterStrings.length && OperateHashTable.getKey(paramenterHashMap, paramenterStrings[j])==-1; j++) {
                            paramenterValues[cnt++] = paramenterStrings[j];
                            paramentersOfProvince = paramenterValues;
                        }
                    }
                }
            }
        }
        /** description:执行统计并写入文件 */
        public static void execCMD(String[] args) {
            InfectStatistic infectStatistic = new InfectStatistic();
            ArrayList<String> listFileNameArrayList = new ArrayList<String>();
            GetFile.getBeforeDateFileName(inputDir, toDateString, listFileNameArrayList);

            try {
                File file = null;
                File outputFile = new File(outputFileNameString);
                String outputDirString = outputFileNameString.substring(0,outputFileNameString.lastIndexOf("/"));
                File outputDir = new File(outputDirString);
                FileOutputStream fileOutputStream = null;
                InputStreamReader reader = null;
                String filePathString = "";
                for (int cnt=0, len=listFileNameArrayList.size(); cnt < len; cnt++) {
                    filePathString = inputDir + "/" + listFileNameArrayList.get(cnt);
                    file = new File(filePathString);

                    if(!outputDir.exists()) {
                        outputDir.mkdir();
                    }
                    if (!outputFile.exists()) {
                        outputFile.createNewFile();
                    }
                    if (file.isFile() && file.exists()) {
                        reader = new InputStreamReader(new FileInputStream(file), "UTF8");
                        BufferedReader bufferedReader = new BufferedReader(reader);
                        fileOutputStream = new FileOutputStream(outputFileNameString);

                        String lineString = null;
                        while ((lineString = bufferedReader.readLine()) != null) {
                            if (lineString.charAt(0) == '/' && lineString.charAt(1) == '/') { //注释行
                                ;
                            } else {
                                ProviceStatistic.StatisticProvince(lineString, hashtable);
                            }
                        }
                    }else {
                        System.out.println("输入文件路径："+filePathString);
                        System.out.println("找不到输入文件");
                    }
                }

                ProviceStatistic.StatisticNation(hashtable);
                OutPutFile.writeFile(hashtable, fileOutputStream, paramentersOfType, paramentersOfProvince, args);
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        StartCMD.separateCMDInit(args);
        StartCMD.execCMD(args);
    }
}

