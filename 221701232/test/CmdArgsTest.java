import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.Vector;

class CmdArgsTest {

    // command line :
    // list -log D:\log\ -out D:\ListOut1.txt -date 2020-01-22
    public static String[] args1 = new String[] {"list",
            "-log", "/home/yyx/JavaWorkspace/InfectStatistic-main/221701232/log",
            "-out", "/home/yyx/JavaWorkspace/InfectStatistic-main/221701232/result/output.txt",
            "-date", "2022-01-22"};
    // command line :
    // list -log D:\log\ -out D:\ListOut2.txt -date 2020-01-22 -province 福建 河北
    public static String[] args2 = new String[] {"list",
            "-log", "/home/yyx/JavaWorkspace/InfectStatistic-main/221701232/log",
            "-out", "/home/yyx/JavaWorkspace/InfectStatistic-main/221701232/result/output.txt",
            "-date", "2022-01-22",
            "-province", "福建", "河北"};
    // command line :
    // list -log D:\log\ -out D:\ListOut3.txt -date 2020-01-23 -type cure dead ip -province 全国 浙江 福建
    public  static String[] args3 = new String[] {"list",
            "-log", "/home/yyx/JavaWorkspace/InfectStatistic-main/221701232/log",
            "-out", "/home/yyx/JavaWorkspace/InfectStatistic-main/221701232/result/output.txt", "-date", "2020-01-23",
            "-type", "cure", "dead", "ip",
            "-province", "全国", "浙江" ,"福建"};
    private CmdArgs cmdArgs;
    private InfectStatistic infectStatistic;
    @BeforeEach
    void init() throws ParseException {
        infectStatistic = new InfectStatistic();
        cmdArgs = infectStatistic.getCmdArgs();
    }
    @Test
    void handleArgs() throws ParseException {
        cmdArgs.handleArgs(args3, infectStatistic);
        assert(cmdArgs.getLogFilePath().equals("/home/yyx/JavaWorkspace/InfectStatistic-main/221701232/log"));
        assert(cmdArgs.getOutFilePath().equals("/home/yyx/JavaWorkspace/InfectStatistic-main/221701232/result/output.txt"));
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
}