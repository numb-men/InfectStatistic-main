import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
            // support args -date
            if (CommonUtil.compareDate(entry.getKey(), infectStatistic.getDate())) {
                break;
            }
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
                    record = new Record(data[0]);
                    container.addRecord(record);
                }
                // get number
                String str1 = data[data.length - 1].trim();
                String str2 = "";
                for (int i = 0; i < str1.length(); ++i) {
                    if(str1.charAt(i) >= '0' && str1.charAt(i) <= '9'){
                        str2 += str1.charAt(i);
                    } else {
                        break;
                    }
                }
                int num = Integer.parseInt(str2);
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
                        infectedRecord = new Record(data[3]);
                        container.addRecord(infectedRecord);
                    }
                    infectStatistic.peopleMove(record, infectedRecord, data[1], num);
                }
                //debug
                for (int i = 0; i < data.length; ++i) {
                    System.out.println("data[" + i + "]" + "=" +data[i]);
                }
                record.showMessage();
                infectStatistic.getCountry().showMessage();
            }
        }
    }

    public static Map<String,File> getFiles(String path) {
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

    public static void handleArgs(String[] args, InfectStatistic infectStatistic) throws ParseException {
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equals("-log")) {
                infectStatistic.setLogFilePath(args[++i]);
            } else if (args[i].equals("-out")) {
                infectStatistic.setOutFilePath(args[++i]);
            } else if (args[i].equals("-date")) {
                infectStatistic.setDate(args[++i]);
            } else if (args[i].equals("-type")) {
                while (i < args.length) {
                    if ((i + 1) >= args.length || args[i + 1].charAt(0) == '-') {
                        break;
                    } else {
                        infectStatistic.addType(args[++i]);
                    }
                }
            } else if (args[i].equals("-province")) {
                while (i < args.length) {
                    if ((i + 1) >= args.length || args[i + 1].charAt(0) == '-') {
                        break;
                    } else {
                        infectStatistic.addProvince(args[++i]);
                    }
                }
            }
        }
    }

    public static void showArgs(InfectStatistic infectStatistic) {
        System.out.println("日志目录位置:" + infectStatistic.getLogFilePath());
        System.out.println("输出文件位置:" + infectStatistic.getOutFilePath());
        System.out.println("统计的最新截至日期:" + infectStatistic.getDate());
        if (infectStatistic.getTypes() != null && infectStatistic.typeNumber() != 0) {
            System.out.println("选择统计的人员类型:");
            for (String tem : infectStatistic.getTypes()) {
                System.out.print(tem + " ");
            }
        }
        if (infectStatistic.getProvinces() != null && infectStatistic.provinceNumber() != 0) {
            System.out.println("选择统计的省份:");
            for (String tem : infectStatistic.getProvinces()) {
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
        if (!(r1.getProvinceName().equals(r2.getProvinceName()))) {
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
            System.out.println("now's date < date");
            return null;
        }
        return date;
    }

}

