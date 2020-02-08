import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

/**
 * Lib
 * TODO
 * 1.HandleFileUtil
 *      a.getFiles
 *      b.readLogFile
 *      c.writeOutFile
 * 2.HandleArgsUtil
 *
 * @author 岳逾先
 * @version 1.1.1
 * @since 1.8
 */
public class Lib {
}

class HandleFileUtil {

    public static void readLogFile(Map<String, File> fileMap, InfectStatistic infectStatistic) throws IOException, ParseException {
        Container container = infectStatistic.getContainer();
        int index = 0;
        for (Map.Entry<String, File> entry : fileMap.entrySet()) {
            index++;
            System.out.println("Handle file : " + entry.getKey());
            FileReader fr = new FileReader(entry.getValue());
            BufferedReader br = new BufferedReader(fr);
            String line  = null;
            while( (line = br.readLine()) != null){
                String data [] = line.split(" ");
                if (data[0].substring(0,2).equals("//")) {
                    continue;
                }
                // get province record
                Record record = container.getRecord(data[0]);
                if (record == null) {
                    record = new Record();
                    record.setProvinceName(data[0]);
                    container.addRecord(record);
                }
                // get number
                int num = CommonUtil.parserStringToInt(data[data.length - 1]);
                // handle data
                if (data.length == 3) {
                    if (data[1].equals("死亡")) {
                        infectStatistic.addDeadNum(record, num);
                    } else if (data[1].equals("治愈")) {
                        infectStatistic.addCureNum(record, num);
                    }
                } else if (data.length == 4) {
                    if (data[1].equals("新增")) {
                        if (data[2].equals("感染患者")) {
                            infectStatistic.addIpNum(record, num);
                        } else if (data[2].equals("疑似患者")) {
                            infectStatistic.addSpNum(record, num);
                        }
                    } else if (data[1].equals("疑似患者")) {
                        infectStatistic.spTurnToIp(record, num);
                    } else if (data[1].equals("排除")) {
                        infectStatistic.excludeSp(record, num);
                    }
                } else if (data.length == 5) {
                    Record infectedRecord = container.getRecord(data[3]);
                    if (infectedRecord == null) {
                        infectedRecord = new Record();
                        infectedRecord.setProvinceName(data[3]);
                        container.addRecord(infectedRecord);
                    }
                    infectStatistic.peopleMove(record, infectedRecord, data[1], num);
                }
                //debug
                for (int i = 0; i < data.length; ++i) {
                    System.out.println("data[" + i + "]" + "=" +data[i]);
                }
                CommonUtil.showRecordMessage(record);
                CommonUtil.showRecordMessage(record);
            }
        }
    }

    public static void writeOutFile(CmdArgs cmdArgs, Record country, Container container) {
        String outFilePath = cmdArgs.getOutFilePath();
        try {
            File file = new File(outFilePath);
            if (file.isDirectory()) {
                file = new File(outFilePath + "output.txt");
            }
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fileWriter);
            if (cmdArgs.getProvinces() != null &&
                    cmdArgs.getProvinces().contains("全国")) {
                CommonUtil.writeRecordToFile(bw, country, cmdArgs.getTypes());
            }
            CommonUtil.writeContainerToFile(bw, container,
                    cmdArgs.getTypes(), cmdArgs.getProvinces());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, File> getFiles(String path, String date) throws ParseException {
        File file = new File(path);
        File[] fileList = file.listFiles();
        Map<String, File> fileMap = new HashMap<>();
        if (fileList != null) {
            for (int i = 0; i < fileList.length; ++i) {
                if (fileList[i].isFile()) {
                    String[] temp = fileList[i].toString().split("/");
                    if (!temp[temp.length - 1].matches("2020-[0-1][0-9]-[0-3][0-9].log.txt")) {
                        System.out.println("invalid file : " + temp[temp.length - 1] +" skip");
                        continue;
                    }
                    // support args -date
                    if (date != null && CommonUtil.compareDate(temp[temp.length - 1], date)) {
                        continue;
                    }
                    fileMap.put(fileList[i].toString(), fileList[i]);
                }
            }
        }
        // sorted for file name
        fileMap = fileMap.entrySet().stream()
                .sorted(comparingByValue())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e2, LinkedHashMap::new));
        // debug
        if (fileMap != null) {
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                System.out.println(entry.getKey());
            }
        }
        return fileMap;
    }
}
class HandleArgsUtil {

    public static void handleArgs(String[] args, CmdArgs cmdArgs) throws ParseException {
        assert(cmdArgs != null);
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equals("-log")) {
                cmdArgs.setLogFilePath(args[++i]);
            } else if (args[i].equals("-out")) {
                cmdArgs.setOutFilePath(args[++i]);
            } else if (args[i].equals("-date")) {
                cmdArgs.setDate(args[++i]);
            } else if (args[i].equals("-type")) {
                while (i < args.length) {
                    if ((i + 1) >= args.length || args[i + 1].charAt(0) == '-') {
                        break;
                    } else {
                        cmdArgs.addType(args[++i]);
                    }
                }
            } else if (args[i].equals("-province")) {
                while (i < args.length) {
                    if ((i + 1) >= args.length || args[i + 1].charAt(0) == '-') {
                        break;
                    } else {
                        cmdArgs.addProvince(args[++i]);
                    }
                }
            }
        }
    }

    public static void showArgs(CmdArgs cmdArgs) {
        System.out.println("日志目录位置:" + cmdArgs.getLogFilePath());
        System.out.println("输出文件位置:" + cmdArgs.getOutFilePath());
        System.out.println("统计的最新截至日期:" + cmdArgs.getDate());
        if (cmdArgs.getTypes() != null && cmdArgs.typeNumber() != 0) {
            System.out.println("选择统计的人员类型:");
            for (String tem : cmdArgs.getTypes()) {
                System.out.print(tem + " ");
            }
        }
        if (cmdArgs.getProvinces() != null && cmdArgs.provinceNumber() != 0) {
            System.out.println("选择统计的省份:");
            for (String tem : cmdArgs.getProvinces()) {
                System.out.print(tem + " ");
            }
        }
    }
}

class CommonUtil {

    // d1 > d2 return true
    public static boolean compareDate(Date d1, Date d2) {
        return d1.compareTo(d2) > 0;
    }

    public static boolean compareDate(String d1, String d2) throws ParseException {
        Date date1 = stringToDate(d1);
        Date date2 = stringToDate(d2);
        return compareDate(date1, date2);
    }

    // r1 equals to r2 return true
    public static boolean compareRecord(Record r1, Record r2) {
        if (r1 == r2) {
            return true;
        } else if (r1 == null || r2 == null) {
            return false;
        } else if (!(r1.getProvinceName().equals(r2.getProvinceName()))) {
            return false;
        } else if (!(r1.getDeadNum() == r2.getDeadNum())) {
            return false;
        } else if (!(r1.getCureNum() == r2.getCureNum())) {
            return false;
        } else if (!(r1.getSpNum() == r2.getSpNum())) {
            return false;
        } else if (!(r1.getIpNum() == r2.getIpNum())) {
            return false;
        }
        return true;
    }

    public static boolean compareContainer(Container c1, Container c2) {
        Map<String, Record> recordMap1 = c1.getRecordMap();
        Map<String, Record> recordMap2 = c2.getRecordMap();
        if (recordMap1.size() != recordMap2.size()) {
            return false;
        }
        for (Map.Entry<String, Record> entry : recordMap1.entrySet()) {
            Record record1 = entry.getValue();
            Record record2 = c2.getRecord(record1.getProvinceName());
            if (!compareRecord(record1, record2)) {
                return false;
            }
        }
        return true;
    }

    // tranform "yyyy-MM-dd" to Date
    public static Date stringToDate(String dateStr) throws ParseException {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            System.out.println("args's date is not invalid");
            return null;
        }
        // comfirm now date >= date
        Date now = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        if (now.compareTo(sd.parse(dateStr)) < 0) {// now >= date
            System.out.println("now's date < date, " + dateStr + "is invalid" );
            return null;
        }
        return date;
    }

    // write container's message to file
    public static void writeContainerToFile(BufferedWriter bw, Container container,
                                            Vector<String> types, Vector<String> provinces) throws IOException {
        Map<String, Record> recordMap = container.getRecordMap();
        for (Map.Entry<String, Record> entry : recordMap.entrySet()) {
            Record record = entry.getValue();
            if (provinces != null) {
                String province = entry.getKey();
                if (!provinces.contains(province)) {
                    continue;
                }
            }
            writeRecordToFile(bw, record, types);
        }
    }
    // write record's message to file
    // [ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]
    public static void writeRecordToFile(BufferedWriter bw, Record record, Vector<String> types) throws IOException {
        String provinceRecord = "";
        if (types == null || types.size() == 0) {
            provinceRecord = record.getProvinceName() + " 感染患者"
                    + record.getIpNum() +"人 "
                    + "疑似患者" + record.getSpNum() + "人 "
                    + "治愈" + record.getCureNum() + "人 "
                    + "死亡" + record.getDeadNum() + "人";
        } else {
            provinceRecord = record.getProvinceName();
            for (String str : types) {
                if (str.equals("ip")) {
                    provinceRecord += " 感染患者" + record.getIpNum() + "人 ";
                } else if (str.equals("sp")) {
                    provinceRecord += " 疑似患者" + record.getSpNum() + "人 ";
                } else if (str.equals("cure")) {
                    provinceRecord += " 治愈" + record.getCureNum() + "人 ";
                } else if (str.equals("dead")) {
                    provinceRecord += " 死亡" + record.getDeadNum() + "人 ";
                }
            }
        }
        provinceRecord += "\r\n";
        bw.write(provinceRecord);
    }
    // show record's message
    public static void showRecordMessage(Record record) {
        String provinceRecord = record.getProvinceName() + " 感染患者"
                + record.getIpNum() +"人 "
                + "疑似患者" + record.getSpNum() + "人 "
                + "治愈" + record.getCureNum() + "人 "
                + "死亡" + record.getDeadNum() + "人";
        provinceRecord += "\r\n";
        System.out.println(provinceRecord);
    }
    // read out put file
    public static Container readOutFile(String outFilePath) throws IOException {
        Container container = new Container();
        File file = new File(outFilePath);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line = null;
        while((line = br.readLine()) != null) {
            Record record = new Record();
            if (readOneLineOfOutFile(record, line)) {
                container.addRecord(record);
            } else {
                break;
            }
        }
        return container;
    }
    // read one line in output file
    public static boolean readOneLineOfOutFile(Record record, String line) {
        String data [] = line.split(" ");
        if (data[0].equals("//")) {
            return false;
        }
        record.setProvinceName(data[0]);
        for (int i = 1; i < data.length; ++i) {
            String type = parserStringGetType(data[i]);
            if (type.equals("感染患者")) {
                record.setIpNum(CommonUtil.parserStringToInt(data[i]));
            } else if (type.equals("疑似患者")) {
                record.setSpNum(CommonUtil.parserStringToInt(data[i]));
            } else if (type.equals("治愈")) {
                record.setCureNum(CommonUtil.parserStringToInt(data[i]));
            } else if (type.equals("死亡")) {
                record.setDeadNum(CommonUtil.parserStringToInt(data[i]));
            }
        }
        return true;
    }
    // parser a string to get type
    // type ： 感染患者(ip) 疑似患者(sp) 治愈(cure) 死亡(dead)
    // example ： 感染患者5人 -> type is 感染患者
    public static String parserStringGetType(String string) {
        String str1 = string.trim();
        String str2 = "";
        for (int i = 0; i < str1.length(); ++i) {
            if(str1.charAt(i) >= '0' && str1.charAt(i) <= '9'){
                break;
            } else {
                str2 += str1;
            }
        }
        return str2;
    }
    // parser number from string
    public static int parserStringToInt(String string) {
        String str1 = string.trim();
        String str2 = "";
        for (int i = 0; i < str1.length(); ++i) {
            if(str1.charAt(i) >= '0' && str1.charAt(i) <= '9'){
                str2 += str1.charAt(i);
            } else {
                continue;
            }
        }
        if (str2.equals("")) {
            return 0;
        }
        int number = Integer.parseInt(str2);
        return number;
    }

}

