import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * InfectStatistic
 * TODO
 * 1.InfectStatistic
 * 2.Container
 * 3.Record
 *
 * @author 岳逾先
 * @version 1.1.1
 * @since 1.8
 */
class InfectStatistic {

    private String logFilePath;
    private String outFilePath;
    private String date;
    private Vector<String> types;
    private Vector<String> provinces;
    private Map<String, File> logFilesMap;// logFileName -> File
    private Container container;
    private Record country;
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

    public Vector<String> getProvinces() {
        return provinces;
    }

    public void addProvince(String province) {
        this.provinces.add(province);
    }

    public int provinceNumber() { return this.provinces.size(); }

    public Map<String, File> getLogFilesMap() {
        return logFilesMap;
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
    public InfectStatistic() {
        this.types = null;
        this.provinces = null;
        this.logFilePath = "/home/yyx/JavaWorkspace/InfectStatistic-main/221701232/log";
        this.outFilePath = "/home/yyx/JavaWorkspace/InfectStatistic-main/221701232/result/";
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.date = sdf.format(now);
        country = new Record("全国");
        container = new Container();
        logFilesMap = new HashMap<>();
    }
    public static void main(String[] args) {

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
    // debug
    public void showMessage() {
        String provinceRecord = this.getProvinceName() + " 感染患者"
                + this.getIpNum() +"人 "
                + "疑似患者" + this.getSpNum() + "人 "
                + "治愈" + this.getCureNum() + "人 "
                + "死亡" + this.getDeadNum() + "人";
        provinceRecord += "\r\n";
        System.out.println(provinceRecord);
    }
    // Constructor
    public Record(String provinceName) {
        this.provinceName = provinceName;
        this.ipNum = 0;
        this.spNum = 0;
        this.deadNum = 0;
        this.cureNum = 0;
    }
}
