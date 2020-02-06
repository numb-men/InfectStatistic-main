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
        boolean convertSuccess=true;
        // comfirm date is valid
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            format.parse(date);
        } catch (ParseException e) {
            convertSuccess=false;
        }
        if (!convertSuccess) {
            System.out.println("args's date is not invalid, date is been setted to" + this.date);
            return;
        }
        // comfirm now date >= date
        Date now = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        if (now.compareTo(sd.parse(date)) < 0) {// now >= date
            System.out.println("now's date < date, date is been setted to" + this.date);
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

    public void incSpNum(int num) { this.spNum += num; }

    public void decSpNum(int num) { this.spNum -= num; }

    public int getCureNum() {
        return cureNum;
    }

    public void incCureNum(int num) { this.cureNum += num; }

    public int getDeadNum() {
        return deadNum;
    }

    public void incDeadNum(int num) { this.deadNum += num; }
    // Constructor
    public Record(String provinceName) {
        this.provinceName = provinceName;
        this.ipNum = 0;
        this.spNum = 0;
        this.deadNum = 0;
        this.cureNum = 0;
    }
}
