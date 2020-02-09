import java.io.*;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

/**
 * InfectStatistic
 * TODO
 * 1.InfectStatistic
 * 2.Container
 * 3.Record
 * 4.CmdArgs
 * @author 岳逾先
 * @version 1.1.1
 * @since 1.8
 */
class InfectStatistic {

    public static String logFileRegex = "2020-[0-1][0-9]-[0-3][0-9].log.txt";
    private CmdArgs cmdArgs;
    private Map<String, File> logFilesMap;// logFileName -> File
    private Container container;
    private Record country;
    // Set and Get Method
    public CmdArgs getCmdArgs() {
        return cmdArgs;
    }

    public void setCmdArgs(CmdArgs cmdArgs) {
        this.cmdArgs = cmdArgs;
    }

    public Map<String, File> getLogFilesMap() {
        return logFilesMap;
    }

    public void setLogFilesMap(Map<String, File> logFilesMap) {
        this.logFilesMap = logFilesMap;
    }

    public Container getContainer() {
        return container;
    }

    public Record getCountry() {
        return country;
    }
    // logical function
    public void addDeadNum(Record record, int num) {
        record.incDeadNum(num);
        record.decIpNum(num);
        country.incDeadNum(num);
        country.decIpNum(num);
    }

    public void addCureNum(Record record, int num) {
        record.incCureNum(num);
        record.decIpNum(num);
        country.incCureNum(num);
        country.decIpNum(num);
    }

    public void addIpNum(Record record, int num) {
        record.incIpNum(num);
        country.incIpNum(num);
    }

    public void addSpNum(Record record, int num) {
        record.incSpNum(num);
        country.incSpNum(num);
    }

    public void spTurnToIp(Record record, int num) {
        record.incIpNum(num);
        record.decSpNum(num);
        country.incIpNum(num);
        country.decSpNum(num);
    }

    public void excludeSp(Record record, int num) {
        record.decSpNum(num);
        country.decSpNum(num);
    }

    public void peopleMove(Record fromRecord, Record infectedRecord, String type, int num) {
        if (type.equals("感染患者")) {
            fromRecord.decIpNum(num);
            infectedRecord.incIpNum(num);
        } else if (type.equals("疑似患者")) {
            fromRecord.decSpNum(num);
            infectedRecord.incSpNum(num);
        }
    }
    // Constructor
    public InfectStatistic() throws ParseException {
        cmdArgs = new CmdArgs();
        country = new Record();
        country.setProvinceName("全国");
        container = new Container();
        logFilesMap = new HashMap<>();
    }
    // deal one line of log file
    public boolean dealOneLineOfLogFile(String line) {
        String data [] = line.split(" ");
        if (data[0].substring(0,2).equals("//")) {
            return false;
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
                this.addDeadNum(record, num);
            } else if (data[1].equals("治愈")) {
                this.addCureNum(record, num);
            }
        } else if (data.length == 4) {
            if (data[1].equals("新增")) {
                if (data[2].equals("感染患者")) {
                    this.addIpNum(record, num);
                } else if (data[2].equals("疑似患者")) {
                    this.addSpNum(record, num);
                }
            } else if (data[1].equals("疑似患者")) {
                this.spTurnToIp(record, num);
            } else if (data[1].equals("排除")) {
                this.excludeSp(record, num);
            }
        } else if (data.length == 5) {
            Record infectedRecord = container.getRecord(data[3]);
            if (infectedRecord == null) {
                infectedRecord = new Record();
                infectedRecord.setProvinceName(data[3]);
                container.addRecord(infectedRecord);
            }
            this.peopleMove(record, infectedRecord, data[1], num);
        }
        return true;
    }
    // read log file
    public void readLogFile() throws IOException {
        int index = 0;
        for (Map.Entry<String, File> entry : logFilesMap.entrySet()) {
            index++;
            System.out.println("Handle file : " + entry.getKey());
            BufferedReader br = null;
            try {
                br = CommonUtil.newBufferReader(entry.getKey());
                String line  = null;
                while( (line = br.readLine()) != null) {
                    if (!dealOneLineOfLogFile(line)) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                br.close();
            }
        }
    }
    // write out file
    public void writeOutFile() throws IOException {
        String outFilePath = cmdArgs.getOutFilePath();
        BufferedWriter bw = null;
        try {
            bw = CommonUtil.newBufferWriter(outFilePath);
            if (!cmdArgs.hasProvince() || cmdArgs.getProvinces().contains("全国")) {
                country.writeRecordToFile(bw, cmdArgs.getTypes());
            }
            container.writeContainerToFile(bw, cmdArgs.getTypes(), cmdArgs.getProvinces());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bw.close();
        }
    }

    public static void main(String[] args) throws ParseException, IOException {
        InfectStatistic infectStatistic = new InfectStatistic();
        CmdArgs cmdArgs =  infectStatistic.getCmdArgs();
        if (!cmdArgs.handleArgs(args, infectStatistic)) {
            System.out.println("处理命令行参数失败");
            return;
        }
        infectStatistic.setLogFilesMap(CommonUtil.getFiles(cmdArgs.getLogFilePath(),cmdArgs.getDate(),
                InfectStatistic.logFileRegex));
        infectStatistic.readLogFile();
        infectStatistic.writeOutFile();
    }
}

class Container {

    Map<String, Record> recordMap;// provinceName -> Record
    public Container() {
        recordMap = new HashMap<>();
    }

    public Map<String, Record> getRecordMap() {
        return recordMap;
    }

    public Record getRecord(String province) {
        return recordMap.get(province);
    }

    public void addRecord(Record record) {
        recordMap.put(record.getProvinceName(), record);
    }

    public boolean compareTo(Container c) {
        Map<String, Record> rdMap = c.getRecordMap();
        if (recordMap.size() != rdMap.size()) {
            return false;
        }
        for (Map.Entry<String, Record> entry : recordMap.entrySet()) {
            Record record1 = entry.getValue();
            Record record2 = c.getRecord(record1.getProvinceName());
            if (!record1.compareTo(record2)) {
                return false;
            }
        }
        return true;
    }

    public void sortByProvince() {
        MapKeyComparator mapKeyComparator = new MapKeyComparator();
        recordMap = CommonUtil.sortMapByKey(recordMap, mapKeyComparator);
    }

    // write container's message to file
    public void writeContainerToFile(BufferedWriter bw,
                                     Vector<String> types, Vector<String> provinces) throws IOException {
        // sort by province's pingying
        sortByProvince();
        for (Map.Entry<String, Record> entry : recordMap.entrySet()) {
            Record record = entry.getValue();
            if (provinces != null && provinces.size() > 0) {
                String province = entry.getKey();
                if (!provinces.contains(province)) {
                    continue;
                }
            }
            record.writeRecordToFile(bw, types);
        }
    }
}

class Record {

    private String provinceName;
    private int ipNum;// infection patients number
    private int spNum;// suspected patients number
    private int cureNum;// cure people number
    private int deadNum;//  dead people number
    // Set and Get Method
    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getIpNum() {
        return ipNum;
    }

    public void setIpNum(int ipNum) {
        this.ipNum = ipNum;
    }

    public void incIpNum(int num) { this.ipNum += num; }

    public void decIpNum(int num) { this.ipNum -= num; }

    public int getSpNum() {
        return spNum;
    }

    public void setSpNum(int spNum) {
        this.spNum = spNum;
    }

    public void incSpNum(int num) { this.spNum += num; }

    public void decSpNum(int num) { this.spNum -= num; }

    public int getCureNum() {
        return cureNum;
    }

    public void setCureNum(int cureNum) {
        this.cureNum = cureNum;
    }

    public void incCureNum(int num) { this.cureNum += num; }

    public int getDeadNum() {
        return deadNum;
    }

    public void setDeadNum(int deadNum) {
        this.deadNum = deadNum;
    }

    public void incDeadNum(int num) { this.deadNum += num; }

    public void setAll(String provinceName, int sp, int ip, int dead, int cure) {
        this.setProvinceName("福建");
        this.setSpNum(sp);
        this.setIpNum(ip);
        this.setDeadNum(dead);
        this.setCureNum(cure);
    }
    // r1 equals to r2 return true
    public boolean compareTo(Record r2) {
        if (r2 == null) {
            return false;
        } else if (this == r2) {
            return true;
        } else if (!(this.getProvinceName().equals(r2.getProvinceName()))) {
            return false;
        } else if (!(this.getDeadNum() == r2.getDeadNum())) {
            return false;
        } else if (!(this.getCureNum() == r2.getCureNum())) {
            return false;
        } else if (!(this.getSpNum() == r2.getSpNum())) {
            return false;
        } else if (!(this.getIpNum() == r2.getIpNum())) {
            return false;
        }
        return true;
    }
    // write record's message to file
    // [ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]
    public void writeRecordToFile(BufferedWriter bw, Vector<String> types) throws IOException {
        String provinceRecord = "";
        if (types == null || types.size() == 0) {
            provinceRecord = this.getProvinceName() + " 感染患者"
                    + this.getIpNum() +"人 "
                    + "疑似患者" + this.getSpNum() + "人 "
                    + "治愈" + this.getCureNum() + "人 "
                    + "死亡" + this.getDeadNum() + "人";
        } else {
            provinceRecord = this.getProvinceName();
            for (String str : types) {
                if (str.equals("ip")) {
                    provinceRecord += " 感染患者" + this.getIpNum() + "人 ";
                } else if (str.equals("sp")) {
                    provinceRecord += " 疑似患者" + this.getSpNum() + "人 ";
                } else if (str.equals("cure")) {
                    provinceRecord += " 治愈" + this.getCureNum() + "人 ";
                } else if (str.equals("dead")) {
                    provinceRecord += " 死亡" + this.getDeadNum() + "人 ";
                }
            }
        }
        provinceRecord += "\r\n";
        bw.write(provinceRecord);
        bw.flush();
    }
    // show record's message
    public void showRecordMessage() {
        String provinceRecord = this.getProvinceName() + " 感染患者"
                + this.getIpNum() +"人 "
                + "疑似患者" + this.getSpNum() + "人 "
                + "治愈" + this.getCureNum() + "人 "
                + "死亡" + this.getDeadNum() + "人";
        provinceRecord += "\r\n";
        System.out.println(provinceRecord);
    }
    // Constructor
    public Record() {
        this.provinceName = "";
        this.ipNum = 0;
        this.spNum = 0;
        this.deadNum = 0;
        this.cureNum = 0;
    }
}

class CmdArgs {
    private String logFilePath;
    private String outFilePath;
    private String date;
    private Vector<String> types;
    private Vector<String> provinces;
    // Constructor
    public CmdArgs() {
        this.types = new Vector<String>();
        this.provinces = new Vector<String>();
        this.logFilePath = "/home/yyx/JavaWorkspace/InfectStatistic-main/221701232/log";
        this.outFilePath = "/home/yyx/JavaWorkspace/InfectStatistic-main/221701232/result/";
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.date = sdf.format(now);
    }
    // Set and Get Method
    public String getLogFilePath() {
        return logFilePath;
    }

    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    public String getOutFilePath() {
        return outFilePath;
    }

    public void setOutFilePath(String outFilePath) {
        this.outFilePath = outFilePath;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) throws ParseException {
        if (CommonUtil.stringToDate(date) == null) {
            // invalid date
            return;
        }
        this.date = date;
    }

    public Vector<String> getTypes() {
        return types;
    }

    public int typeNumber() { return this.types.size(); }

    public void addType(String type) {
        this.types.add(type);
    }

    public void addType(String[] types) {
        for (String type : types) {
            this.types.add(type);
        }
    }

    public Vector<String> getProvinces() {
        return provinces;
    }

    public void addProvince(String province) {
        this.provinces.add(province);
    }

    public void addProvince(String[] provinces) {
        for (String province : provinces) {
            addProvince(province);
        }
    }

    public boolean hasProvince() { return provinces.size() > 0; }

    public int provinceNumber() { return this.provinces.size(); }

    public boolean handleArgs(String[] args, InfectStatistic infectStatistic) throws ParseException {
        if (args == null) {
            return false;
        }
        int i = 0;
        while (i < args.length) {
            if (args[i].equals("list")) {
                i++;
                break;
            }
            i++;
        }
        if (i >= args.length) {
            showRule();
            return false;
        }
        for (; i < args.length; ++i) {
            if (args[i].equals("-log")) {
                File file = new File(args[++i]);
                if (file.isDirectory()) {
                    this.setLogFilePath(args[i]);
                } else {
                    System.out.println("你必须指定日志文件夹的全路径");
                    return false;
                }
            } else if (args[i].equals("-out")) {
                File file = new File(args[++i]);
                if (file.isFile()) {
                    this.setOutFilePath(args[i]);
                } else {
                    System.out.println("你必须指定输出文件的全路径");
                    return false;
                }
            } else if (args[i].equals("-date")) {
                this.setDate(args[++i]);
            } else if (args[i].equals("-type")) {
                while (i < args.length) {
                    if ((i + 1) >= args.length || args[i + 1].charAt(0) == '-') {
                        break;
                    } else {
                        this.addType(args[++i]);
                    }
                }
            } else if (args[i].equals("-province")) {
                while (i < args.length) {
                    if ((i + 1) >= args.length || args[i + 1].charAt(0) == '-') {
                        break;
                    } else {
                        this.addProvince(args[++i]);
                        if (!args[i].equals("全国")) {
                            Record record = new Record();
                            record.setProvinceName(args[i]);
                            infectStatistic.getContainer().addRecord(record);
                        }
                    }
                }
            } else {
                System.out.println("Unknow args " + args[i]);
            }
        }
        if (this.logFilePath.length() == 0 || this.outFilePath.length() == 0) {
            showRule();
            return false;
        }
        return true;
    }

    public void showRule() {
        System.out.println("command line example : java InfectStatistic list");
        System.out.println("                    -log (logFilePath, Must specify");
        System.out.println("                    -out (logFilePath, Must specify)");
        System.out.println("                    -date (logFilePath)");
        System.out.println("                    -type (logFilePath)");
        System.out.println("                    -province (logFilePath)");
    }

    public void showArgs() {
        System.out.println("日志目录位置:" + this.getLogFilePath());
        System.out.println("输出文件位置:" + this.getOutFilePath());
        System.out.println("统计的最新截至日期:" + this.getDate());
        if (this.getTypes() != null && this.typeNumber() != 0) {
            System.out.println("选择统计的人员类型:");
            for (String tem : this.getTypes()) {
                System.out.print(tem + " ");
            }
        }
        if (this.getProvinces() != null && this.provinceNumber() != 0) {
            System.out.println("选择统计的省份:");
            for (String tem : this.getProvinces()) {
                System.out.print(tem + " ");
            }
        }
    }
}