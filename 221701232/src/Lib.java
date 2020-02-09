import java.io.*;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

/**
 * Lib
 * TODO
 * CommonUtil
 * @author 岳逾先
 * @version 1.1.1
 * @since 1.8
 */
public class Lib {
}

class CommonUtil {

    // new BufferReader br
    public static BufferedReader newBufferReader(String path) throws FileNotFoundException {
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        return br;
    }
    // new BufferWriter bw
    public static BufferedWriter newBufferWriter(String path) throws IOException {
        File file = new File(path);
        if (file.isDirectory()) {
            file = new File(path + "output.txt");
        }
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fileWriter);
        return bw;
    }
    // d1 > d2 return true
    public static boolean compareDate(Date d1, Date d2) {
        return d1.compareTo(d2) > 0;
    }

    public static boolean compareDate(String d1, String d2) throws ParseException {
        Date date1 = stringToDate(d1);
        Date date2 = stringToDate(d2);
        return compareDate(date1, date2);
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
                str2 += str1.charAt(i);
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

    // sort map by key
    public static <A,B> Map<A, B> sortMapByKey(Map<A, B> map, Comparator<A> comparator) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<A, B> sortMap = new TreeMap<A, B>(comparator);
        sortMap.putAll(map);
        return sortMap;
    }

    // get files from path and only get file which date < date
    public static Map<String, File> getFiles(String path, String date, String regex) throws ParseException {
        File file = new File(path);
        File[] fileList = file.listFiles();
        Map<String, File> fileMap = new HashMap<>();
        if (fileList != null) {
            for (int i = 0; i < fileList.length; ++i) {
                if (fileList[i].isFile()) {
                    String[] temp = fileList[i].toString().split("/");
                    // "2020-[0-1][0-9]-[0-3][0-9].log.txt"
                    if (!temp[temp.length - 1].matches(regex)) {
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
        // sorted for file by date
        fileMap = fileMap.entrySet().stream()
                .sorted(comparingByValue())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e2, LinkedHashMap::new));
        return fileMap;
    }
}

class MapKeyComparator implements Comparator<String>{
    @Override
    public int compare(String str1, String str2) {
        Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
        return cmp.compare(str1,str2);
    }
}

