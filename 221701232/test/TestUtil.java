import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Vector;
import org.junit.jupiter.api.Test;

public class TestUtil {
    /** CmdArgs **/
    @Test
    void testHandleArgs() throws ParseException {
        InfectStatistic infectStatistic = new InfectStatistic();
        CmdArgs cmdArgs = infectStatistic.getCmdArgs();
        cmdArgs.handleArgs(TestArgs.args3, infectStatistic);
        assert(cmdArgs.getLogFilePath().equals("/home/yyx/JavaWorkspace/InfectStatistic-main/221701232/log"));
        assert(cmdArgs.getOutFilePath().equals("/home/yyx/JavaWorkspace/InfectStatistic-main/221701232/result/output3.txt"));
        assert(cmdArgs.getDate().equals("2020-01-23"));
        Vector<String> types = cmdArgs.getTypes();
        assert(types.get(0).equals("cure"));
        assert(types.get(1).equals("dead"));
        assert(types.get(2).equals("ip"));
        Vector<String> province = cmdArgs.getProvinces();
        assert(province.get(0).equals("全国"));
        assert(province.get(1).equals("浙江"));
        assert(province.get(2).equals("福建"));
    }
    @Test
    void testHandleArgsWithShow() throws ParseException {
        InfectStatistic infectStatistic = new InfectStatistic();
        CmdArgs cmdArgs = infectStatistic.getCmdArgs();
        cmdArgs.handleArgs(TestArgs.args5, infectStatistic);
    }
    @Test
    void testHandleArgsWithNoList() throws ParseException {
        InfectStatistic infectStatistic = new InfectStatistic();
        CmdArgs cmdArgs = infectStatistic.getCmdArgs();
        assert(!cmdArgs.handleArgs(TestArgs.args6, infectStatistic));
    }
    @Test
    void testHandleArgsWithNoLog() throws ParseException {
        InfectStatistic infectStatistic = new InfectStatistic();
        CmdArgs cmdArgs = infectStatistic.getCmdArgs();
        assert(!cmdArgs.handleArgs(TestArgs.args7, infectStatistic));
    }
    @Test
    void testHandleArgsWithInvalidDate() throws ParseException {
        InfectStatistic infectStatistic = new InfectStatistic();
        CmdArgs cmdArgs = infectStatistic.getCmdArgs();
        assert(!cmdArgs.handleArgs(TestArgs.args8, infectStatistic));
    }
    @Test
    void testHandleArgsWithInvalidLog() throws ParseException {
        InfectStatistic infectStatistic = new InfectStatistic();
        CmdArgs cmdArgs = infectStatistic.getCmdArgs();
        assert(!cmdArgs.handleArgs(TestArgs.args9, infectStatistic));
    }
    @Test
    void testHandleArgsWithInvalidOut() throws ParseException {
        InfectStatistic infectStatistic = new InfectStatistic();
        CmdArgs cmdArgs = infectStatistic.getCmdArgs();
        assert(!cmdArgs.handleArgs(TestArgs.args10, infectStatistic));
    }
    /** CommonUtil **/
    @Test
    void testGetFiles() throws ParseException {
        InfectStatistic infectStatistic = new InfectStatistic();
        CmdArgs cmdArgs = infectStatistic.getCmdArgs();
        cmdArgs.handleArgs(TestArgs.args1, infectStatistic);
        infectStatistic.setLogFilesMap(CommonUtil.getFiles(cmdArgs.getLogFilePath(),cmdArgs.getDate(),
                Lib.logFileRegex));
        Map<String, File> fileMap = infectStatistic.getLogFilesMap();
        String preDate = "2000-02-02";
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        Date pre = sd.parse(preDate);
        Date now = new Date();
        for (Map.Entry<String, File> entry : fileMap.entrySet()) {
            String[] strs = entry.getKey().split("/");
            String[] tempStrs = strs[strs.length - 1].split("\\.");
            Date temp = sd.parse(tempStrs[0]);
            // Ensure files are read in chronological order
            assert(pre.compareTo(temp) < 0);// pre < temp
            assert(now.compareTo(temp) >= 0);// now >= temp
            pre = temp;
        }
    }

    @Test
    void testCompareDate() throws ParseException {
        String dateStr1 = "2020-02-08";// dateStr cannot exceed current time
        String dateStr2 = "2020-02-06";// dateStr cannot exceed current time
        // if dateStr1 > dateStr2 return int > 0
        assert(CommonUtil.compareDate(dateStr1, dateStr2) > 0);
    }

    @Test
    void testStringToDate() throws ParseException {
        // string example : yyyy-MM-dd
        String dateStr1 = "2020-23-05";// invalid str return null
        Date date = CommonUtil.stringToDate(dateStr1);
        assert(date == null);
        // dateStr cannot exceed current time
        String dateStr2 = "2088-02-05";// invalid str return null
        date = CommonUtil.stringToDate(dateStr2);
        assert(date == null);
    }

    @Test
    void testParserStringGetType() {
        // str example : type + number + 人
        // parserStringGetType is subString type
        String str = "感染患者25人";
        String type = CommonUtil.parserStringGetType(str);
        assert(type.equals("感染患者"));
    }

    @Test
    void testParserStringToInt() {
        String str = "感染患者25人";
        int number = CommonUtil.parserStringToInt(str);
        assert(number == 25);
    }
    /** Container **/
    @Test
    void sortByProvince() {
        Container container = new Container();
        String[] province = {"安徽", "北京","重庆","福建","甘肃","广东","广西","贵州",
                "海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏",
                "江西","辽宁","内蒙古","宁夏","青海","山东","山西","陕西",
                "上海","四川","天津","西藏","新疆","云南","浙江"};
        for (String pro : province) {
            Record record = new Record();
            record.setProvinceName(pro);
            container.addRecord(record);
        }
        container.sortByProvince();
        Map<String, Record> stringRecordMap = container.getRecordMap();
        int index = 0;
        for (Map.Entry<String, Record> entry : stringRecordMap.entrySet()) {
            assert(entry.getValue().getProvinceName().equals(province[index]));
            index++;
        }
    }
    /** Record **/
    private static String testPath = "/home/yyx/JavaWorkspace/InfectStatistic-main/221701232/result/test.txt";
    @Test
    void testCompareTo() {
        Record r1 = new Record();
        r1.setAll("福建", 10, 20, 0, 10);
        Record r2 = new Record();
        r2.setAll("福建", 10, 20, 0, 10);
        assert(r1.compareTo(r2));
    }

    @Test
    void testWriteRecordToFile() throws IOException {
        Record r1 = new Record();
        r1.setAll("福建", 10, 20, 0, 10);
        BufferedWriter bw = null;
        BufferedReader br = null;
        Record r2 = new Record();
        try {
            // write one record to file
            bw = CommonUtil.newBufferWriter(testPath);
            r1.writeRecordToFile(bw, null);
            // read result from file
            br = CommonUtil.newBufferReader(testPath);
            String line = br.readLine();
            CommonUtil.readOneLineOfOutFile(r2, line);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bw.close();
            br.close();
        }
        r1.showRecordMessage();
        r2.showRecordMessage();
        assert(r1.compareTo(r2));
    }
    /** InfectStatistic **/
    String outFilePath = "/home/yyx/JavaWorkspace/InfectStatistic-main/221701232/result/";
    @Test
    void main() throws IOException, ParseException {
        Vector<String[]> args = new Vector<>(4);
        args.add(TestArgs.args1);
        args.add(TestArgs.args2);
        args.add(TestArgs.args3);
        args.add(TestArgs.args4);
        for (int i = 0; i < 4; ++i) {
            InfectStatistic.main(args.get(i));
            Container container1 = CommonUtil.readOutFile(
                    outFilePath + "output" + (i + 1) + ".txt");
            Container container2 = CommonUtil.readOutFile(outFilePath + "ListOut" + (i + 1) + ".txt");
            assert(container1.compareTo(container2));
        }
    }
}

class TestArgs {
    public static String log = "/home/yyx/JavaWorkspace/InfectStatistic-main/221701232/log";
    public static String out = "/home/yyx/JavaWorkspace/InfectStatistic-main/221701232/result/";
    /**
     * command line :
     * list -log D:\log\ -out D:\ListOut1.txt -date 2020-01-22
     */
    public static String[] args1 = new String[] {"list",
            "-log", log,
            "-out", out + "output1.txt",
            "-date", "2020-01-22"};
    /**
     * command line :
     * list -log D:\log\ -out D:\ListOut2.txt -date 2020-01-22 -province 福建 河北
     */
    public static String[] args2 = new String[] {"list",
            "-log", log,
            "-out", out + "output2.txt",
            "-date", "2020-01-22",
            "-province", "福建", "河北"};
    /**
     * command line :
     * list -log D:\log\ -out D:\ListOut3.txt -date 2020-01-23 -type cure dead ip -province 全国 浙江 福建
     */
    public  static String[] args3 = new String[] {"list",
            "-log", log,
            "-out", out + "output3.txt",
            "-date", "2020-01-23",
            "-type", "cure", "dead", "ip",
            "-province", "全国", "浙江" ,"福建"};
    public  static String[] args4 = new String[] {"list",
            "-log", log,
            "-out", out + "output4.txt",
            "-date", "2020-01-23",
            "-type", "cure", "dead", "ip",
            "-province", "全国", "浙江" ,"福建","重庆"};
    /**
     * command line with show
     */
    public  static String[] args5 = new String[] {"list",
            "-log", log,
            "-out", out + "output4.txt",
            "-date", "2020-01-23",
            "-type", "cure", "dead", "ip",
            "-province", "全国", "浙江" ,"福建","重庆",
            "-show"};
    /**
     * invalid command line : no list
     */
    public  static String[] args6 = new String[] {
            "-log", log,
            "-out", out + "output4.txt",
            "-date", "2020-01-23",
            "-type", "cure", "dead", "ip",
            "-province", "全国", "浙江" ,"福建","重庆",
            "-show"};
    /**
     * invalid command line : no -out or -log
     */
    public  static String[] args7 = new String[] {"list",
            "-out", out + "output4.txt",
            "-date", "2020-01-23",
            "-type", "cure", "dead", "ip",
            "-province", "全国", "浙江" ,"福建","重庆",
            "-show"};
    /**
     * invalid command line : invalid date
     */
    public  static String[] args8 = new String[] {"list",
            "-log", log,
            "-out", out + "output4.txt",
            "-date", "2028-01-23",
            "-type", "cure", "dead", "ip",
            "-province", "全国", "浙江" ,"福建","重庆",
            "-show"};
    /**
     * invalid command line : invalid log
     */
    public  static String[] args9 = new String[] {"list",
            "-log", log + "2020-01-23.log.txt",
            "-out", out + "output4.txt",
            "-date", "2020-01-23",
            "-type", "cure", "dead", "ip",
            "-province", "全国", "浙江" ,"福建","重庆",
            "-show"};
    /**
     * invalid command line : invalid out
     */
    public  static String[] args10 = new String[] {"list",
            "-log", log,
            "-out", out,
            "-date", "2020-01-23",
            "-type", "cure", "dead", "ip",
            "-province", "全国", "浙江" ,"福建","重庆",
            "-show"};
}