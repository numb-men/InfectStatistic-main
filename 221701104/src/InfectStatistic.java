import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InfectStatistic
 * TODO
 *
 * @author pcy
 * @version 1.0
 * @since xxx
 */
class InfectStatistic {
    /*定义全局变量文本匹配模式*/
    static Pattern ADD_EXACT_PATIENT = Pattern.compile("(\\S+) 新增 感染患者 (\\d+)人");
    static Pattern ADD_DOUBTED_PATIENT = Pattern.compile("(\\S+) 新增 疑似患者 (\\d+)人");
    static Pattern MOVE_EXACT_PATIENT = Pattern.compile("(\\S+) 感染患者 流入 (\\S+) (\\d+)人");
    static Pattern MOVE_DOUBTED_PATIENT = Pattern.compile("(\\S+) 疑似患者 流入 (\\S+) (\\d+)人");
    static Pattern CURED_PATIENT = Pattern.compile("(\\S+) 治愈 (\\d+)人");
    static Pattern DEAD_PATIENT = Pattern.compile("(\\S+) 死亡 (\\d+)人");
    static Pattern DOUBTED_TO_EXACT = Pattern.compile("(\\S+) 疑似患者 确诊感染 (\\d+)人");
    static Pattern DOUBTED_TO_NONE = Pattern.compile("(\\S+) 排除 疑似患者 (\\d+)人");
    static int PROVINCE_NUM = 35;
    static String[] PROVINCE = {"全国", "安徽", "澳门" ,"北京", "重庆", "福建","甘肃",
            "广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林",
            "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海",
            "四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江"};
    ProvinceList PROVINCE_LIST = new ProvinceList();

    /*
    解析命令行类
     */
    class CmdArgs {
        String[] args;

        CmdArgs(String[] args) {
            this.args = args;
        }

        /*
        返回取得的命令
         */
        public String getCmd() {
            return args[0];
        }

        /*
        判断是否有特定的参数
        @param 特定参数名如-log -province
        @return 返回特定参数的索引值
         */
        public int getParam(String param) {
            for(int i = 0; i < args.length; i++) {
                if(args[i].equals(param)) {
                    return i;
                }
            }
            return 0;
        }

        /*
        获取参数的单个参数值
        @param index：参数下标值
        @return 参数值
         */
        public String getVal(int index) {
            String tmp = "";
            try {
                tmp = args[index + 1];
            }
            catch (Exception e) {
                System.err.println("数组要求越界");
            }
            return tmp;
        }

        /*
        获取参数的所有参数值
        @param i:参数下标值
        @return 参数值列表
         */
        public List<String> getVals(int index) {
            List<String> paramVals = new ArrayList<String>();

            for(int i = index + 1; i < args.length; i++) {
                if(!args[i].startsWith("-")) {
                    paramVals.add(args[i]);
                } else {
                    break;
                }
            }
            return paramVals;
        }
    }


    /*
    命令抽象类
     */
    abstract class Command {
        public abstract void execute();
    }

    /*
    实际命令类
     */
    class ListCommand extends Command {
        CmdHandler cmdHandler;
        CmdArgs cmdArgs;

        ListCommand(CmdHandler cmdHandler, CmdArgs cmdArgs) {
            this.cmdHandler = cmdHandler;
            this.cmdArgs = cmdArgs;
        }

        @Override
        public void execute() {
            cmdHandler.listHandler(cmdArgs);
        }
    }

    /*
    命令处理类
     */
    class CmdHandler {
        String logPath, outPath, date;
        List<String> type, province;
        DealLog dealLog;

        CmdHandler(CmdArgs cmdArgs, DealLog dealLog)
        {
            this.dealLog = dealLog;
            //获取文件地址
            logPath = cmdArgs.getVal(cmdArgs.getParam("-log"));
            //获取输出文件地址
            outPath = cmdArgs.getVal(cmdArgs.getParam("-out"));
            //获取日期
            //指定日期
            if(cmdArgs.getParam("-date") > 0) {
                date = cmdArgs.getVal(cmdArgs.getParam("-date"));
            } else {
                //未指定日期
            }
            //获取所需类型
            if(cmdArgs.getParam("-type") > 0) {
                type = cmdArgs.getVals(cmdArgs.getParam("-type"));
            }
            //获取省份
            if(cmdArgs.getParam("-province") > 0) {
                type = cmdArgs.getVals((cmdArgs.getParam("-province")));
            }
        }

        public void listHandler(CmdArgs cmdArgs) {
            //读取文件
            FileHandler fh = new FileHandler(logPath, dealLog, PROVINCE_LIST);
            for(String logPath : fh.logPaths) {
                fh.readLog(logPath);
            }
            PROVINCE_LIST.showData();

        }
    }

    /*
    文件处理类
     */
    class FileHandler {
        File[] fileList;
        String path;
        //新建文件内日志处理类
        DealLog dealLog;
        List<String> logNames, logPaths;

        FileHandler (String path, DealLog dealLog, ProvinceList provinceList)
        {
            File f = new File(path);

            this.path = path;
            this.dealLog = dealLog;
            logNames = new ArrayList<String>();
            logPaths = new ArrayList<String>();
            fileList = f.listFiles();
            initLogName();
            initLogPath();
        }

        /*
        获取目录下的日志名
         */
        public void initLogName() {
            for (int i = 0;i < fileList.length; i++) {
                logNames.add(fileList[i].getName());
            }
        }

        /*
        获取日志地址
         */
        public void initLogPath() {
            for(String logName : logNames) {
                logPaths.add(path + logName);
            }
        }

        public void readLog(String logPath) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream
                        (new File(logPath)), "UTF-8"));
                String line;

                while((line = br.readLine()) != null && !line.startsWith("//")) {
                    dealLog.execute(line, PROVINCE_LIST);
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /*
    定义各个省的情况类
     */
    class ProvinceStat {
        public String name;
        private boolean showStat;
        private int infected;
        private int doubted;
        private int cured;
        private int dead;

        ProvinceStat(String name)
        {
            this.name = name;
            showStat = false;
            infected = 0;
            doubted = 0;
            cured = 0;
            dead = 0;
        }

        /*
        改变是否显示
         */
        public void canBeShown()
        {
            showStat = true;
        }

        /*
        用以获取受感染人数
         */
        public int getInfected()
        {
            return infected;
        }

        /*
        用以获得疑似感染人数
         */
        public int getDoublted()
        {
            return doubted;
        }

        /*
        用以获得治愈人数
         */
        public int getCured()
        {
            return cured;
        }

        /*
        用以获得死亡人数
         */
        public int getDead()
        {
            return dead;
        }

        /*
        用以增加感染人数
         */
        public void addInfected(int n)
        {
            infected += n;
        }

        /*
        用以减少感染人数
         */
        public void decreaseInfected(int n)
        {
            infected -= n;
        }

        /*
        用以增加疑似感染人数
         */
        public void addDoubted(int n)
        {
            doubted += n;
        }

        /*
        用以减少疑似感染人数
         */
        public void decreaseDoubted(int n)
        {
            doubted -=n;
        }

        /*
        用以增加治愈人数
         */
        public void addCured(int n)
        {
            cured += n;
        }

        /*
        用以增加死亡人数
         */
        public void addDead(int n)
        {
            dead += n;
        }
    }

    /*
    定义全国情况列表类
     */
    class ProvinceList{
        ProvinceStat[] provinceList = new ProvinceStat[PROVINCE_NUM];

        ProvinceList() {
            //初始化全国情况
            for(int i = 0; i < PROVINCE_NUM; i++) {
                provinceList[i] = new ProvinceStat(PROVINCE[i]);
            }
        }

        public void showData() {
            for(int i = 0; i < PROVINCE_NUM; i++) {
                if(provinceList[i].showStat) {
                    System.out.println(provinceList[i].name + "感染患者" + provinceList[i].getInfected() + "人 "
                            + "疑似患者" + provinceList[i].getDoublted() + "人 "
                            + "治愈" + provinceList[i].getCured() + "人 "
                            + "死亡" + provinceList[i].getDead() + "人");
                }
            }
        }
    }

    /*
    定义处理文档的抽象类
     */
    public abstract class LogHandler {
        protected LogHandler successor;

        public LogHandler getSuccessor() {
            return successor;
        }

        public void setSuccessor(LogHandler successor){
            this.successor = successor;
        }

        /*
        抽象处理类
         */
        public abstract void handleRequest(String line, ProvinceList provinceList);
    }

    /*
    处理感染病人类
     */
    public class AddExactHandler extends LogHandler {
        @Override
        public void handleRequest(String line, ProvinceList provinceList) {
            Matcher m1 = ADD_EXACT_PATIENT.matcher(line);
            if (m1.matches()) {
                dealAddExact(line);
            } else {
                getSuccessor().handleRequest(line, provinceList);
            }
        }
    }

    /*
    处理疑似病人类
     */
    public class AddDoubtedHandler extends LogHandler {
        @Override
        public void handleRequest(String line, ProvinceList provinceList) {
            Matcher m1 = ADD_DOUBTED_PATIENT.matcher(line);
            if (m1.matches()) {
                dealAddDoubted(line);
            } else {
                getSuccessor().handleRequest(line, provinceList);
            }
        }
    }

    /*
    处理感染患者流入类
     */
    public class MoveExactHandler extends LogHandler {
        @Override
        public void handleRequest(String line, ProvinceList provinceList) {
            Matcher m1 = MOVE_EXACT_PATIENT.matcher(line);
            if (m1.matches()) {
                dealMoveExact(line);
            } else {
                getSuccessor().handleRequest(line, provinceList);
            }
        }
    }

    /*
    处理疑似患者流入类
     */
    public class MoveDoubtedHandler extends LogHandler {
        @Override
        public void handleRequest(String line, ProvinceList provinceList) {
            Matcher m1 = MOVE_DOUBTED_PATIENT.matcher(line);
            if (m1.matches()) {
                dealMoveDoubted(line);
            } else {
                getSuccessor().handleRequest(line, provinceList);
            }
        }
    }

    /*
    处理治愈病人类
     */
    public class CureHandler extends LogHandler {
        @Override
        public void handleRequest(String line, ProvinceList provinceList) {
            Matcher m1 = CURED_PATIENT.matcher(line);
            if (m1.matches()) {
                dealCure(line);
            } else {
                getSuccessor().handleRequest(line, provinceList);
            }
        }
    }

    /*
    处理死亡病人类
     */
    public class DeadHandler extends LogHandler {
        @Override
        public void handleRequest(String line, ProvinceList provinceList) {
            Matcher m1 = DEAD_PATIENT.matcher(line);
            if (m1.matches()) {
                dealDead(line);
            } else {
                getSuccessor().handleRequest(line, provinceList);
            }
        }
    }

    /*
    处理疑似患者确诊类
     */
    public class Doubted2ExactHandler extends LogHandler {
        @Override
        public void handleRequest(String line, ProvinceList provinceList) {
            Matcher m1 = DOUBTED_TO_EXACT.matcher(line);
            if (m1.matches()) {
                dealDoubted2Exact(line);
            } else {
                getSuccessor().handleRequest(line, provinceList);
            }
        }
    }

    /*
    处理疑似患者排除类
     */
    public class Doubted2NoneHandler extends LogHandler {
        @Override
        public void handleRequest(String line, ProvinceList provinceList) {
            Matcher m1 = DOUBTED_TO_NONE.matcher(line);
            if (m1.matches()) {
                dealDoubted2None(line);
            } else {
                getSuccessor().handleRequest(line, provinceList);
            }
        }
    }

    /*
    处理文档类，将责任链包括其中。
     */
    public class DealLog {
        public void execute(String line, ProvinceList provinceList) {
            LogHandler addExactPatient = new AddExactHandler();
            LogHandler addDoubtedPatient = new AddDoubtedHandler();
            LogHandler moveExactPatient = new MoveExactHandler();
            LogHandler moveDoubtedPatient = new MoveDoubtedHandler();
            LogHandler curePatient = new CureHandler();
            LogHandler deadPatient = new DeadHandler();
            LogHandler doubted2Exact = new Doubted2ExactHandler();
            LogHandler doubted2None = new Doubted2NoneHandler();

            //串联责任链
            addExactPatient.setSuccessor(addDoubtedPatient);
            addDoubtedPatient.setSuccessor(moveExactPatient);
            moveExactPatient.setSuccessor(moveDoubtedPatient);
            moveDoubtedPatient.setSuccessor(curePatient);
            curePatient.setSuccessor(deadPatient);
            deadPatient.setSuccessor(doubted2Exact);
            doubted2Exact.setSuccessor(doubted2None);

            //自动调用责任链
            addExactPatient.handleRequest(line, provinceList);
        }
    }

    /*
    对符合增加感染人数的行作数据处理
    @param 日志行
     */
    public void dealAddExact(String line) {
        //将字符串以空格分割为多个字符串
        String[] strArray = line.split(" ");
        int n = Integer.parseInt(strArray[3].replace("人", ""));

        for(int i = 0; i < PROVINCE_NUM; i++)
        {
            if(strArray[0].equals(PROVINCE[i]))
            {
                //全国感染人数增加
                PROVINCE_LIST.provinceList[0].addInfected(n);
                PROVINCE_LIST.provinceList[0].canBeShown();
                //当地区感染人数增加
                PROVINCE_LIST.provinceList[i].addInfected(n);
                PROVINCE_LIST.provinceList[i].canBeShown();
                break;
            }
        }
    }

    /*
   对符合增加疑似感染人数的行作数据处理
   @param line:日志行
   @param pl:省份列表
    */
    public void dealAddDoubted(String line) {
        //将字符串以空格分割为多个字符串
        String[] strArray = line.split(" ");
        int n = Integer.parseInt(strArray[3].replace("人", ""));

        for(int i = 0; i < PROVINCE_NUM; i++)
        {
            if(strArray[0].equals(PROVINCE[i]))
            {
                //全国疑似感染人数增加
                PROVINCE_LIST.provinceList[0].addDoubted(n);
                PROVINCE_LIST.provinceList[0].canBeShown();
                //当地区疑似感染人数增加
                PROVINCE_LIST.provinceList[i].addDoubted(n);
                PROVINCE_LIST.provinceList[i].canBeShown();
                break;
            }
        }
    }

    /*
   对符合流动感染人数的行作数据处理
   @param 日志行
   @param pl:省份列表
    */
    public void dealMoveExact(String line) {
        //将字符串以空格分割为多个字符串
        String[] strArray = line.split(" ");
        int n = Integer.parseInt(strArray[4].replace("人", ""));

        for(int i = 0; i < PROVINCE_NUM; i++)
        {
            if(strArray[0].equals(PROVINCE[i]))
            {
                //流出地感染人数减少
                PROVINCE_LIST.provinceList[i].decreaseInfected(n);
                break;
            }
        }
        for(int i = 0; i < PROVINCE_NUM; i++)
        {
            if(strArray[3].equals(PROVINCE[i]))
            {
                //流入地感染人数增加
                PROVINCE_LIST.provinceList[i].addInfected(n);
                PROVINCE_LIST.provinceList[i].canBeShown();
                break;
            }
        }
    }

    /*
  对符合流动疑似感染人数的行作数据处理
  @param 日志行
  @param pl:省份列表
   */
    public void dealMoveDoubted(String line) {
        //将字符串以空格分割为多个字符串
        String[] strArray = line.split(" ");
        int n = Integer.parseInt(strArray[4].replace("人", ""));

        for(int i = 0; i < PROVINCE_NUM; i++)
        {
            if(strArray[0].equals(PROVINCE[i]))
            {
                //流出地疑似感染人数减少
                PROVINCE_LIST.provinceList[i].decreaseDoubted(n);
                break;
            }
        }
        for(int i = 0; i < PROVINCE_NUM; i++)
        {
            if(strArray[3].equals(PROVINCE[i]))
            {
                //流入地疑似感染人数增加
                PROVINCE_LIST.provinceList[i].addDoubted(n);
                PROVINCE_LIST.provinceList[i].canBeShown();
                break;
            }
        }
    }

    /*
  对符合治愈人数的行作数据处理
  @param line:日志行
  @param pl:省份列表
   */
    public void dealCure(String line) {
        //将字符串以空格分割为多个字符串
        String[] strArray = line.split(" ");
        int n = Integer.parseInt(strArray[2].replace("人", ""));

        for(int i = 0; i < PROVINCE_NUM; i++)
        {
            if(strArray[0].equals(PROVINCE[i]))
            {
                //全国治愈人数增加
                PROVINCE_LIST.provinceList[0].addCured(n);
                //全国感染人数减少
                PROVINCE_LIST.provinceList[0].decreaseInfected(n);
                //当地区治愈人数增加
                PROVINCE_LIST.provinceList[i].addCured(n);
                //当地区感染人数减少
                PROVINCE_LIST.provinceList[i].decreaseInfected(n);
                break;
            }
        }
    }

    /*
  对符合死亡人数的行作数据处理
  @param line:日志行
  @param pl:省份列表
   */
    public void dealDead(String line) {
        //将字符串以空格分割为多个字符串
        String[] strArray = line.split(" ");
        int n = Integer.parseInt(strArray[2].replace("人", ""));

        for(int i = 0; i < PROVINCE_NUM; i++)
        {
            if(strArray[0].equals(PROVINCE[i]))
            {
                //全国死亡人数增加
                PROVINCE_LIST.provinceList[0].addDead(n);
                //全国感染人数减少
                PROVINCE_LIST.provinceList[0].decreaseInfected(n);
                //当地区死亡人数增加
                PROVINCE_LIST.provinceList[i].addDead(n);
                //当地区感染人数减少
                PROVINCE_LIST.provinceList[i].decreaseInfected(n);
                break;
            }
        }
    }

    /*
  对符合确诊人数的行作数据处理
  @param line:日志行
  @param pl:省份列表
   */
    public void dealDoubted2Exact(String line) {
        //将字符串以空格分割为多个字符串
        String[] strArray = line.split(" ");
        int n = Integer.parseInt(strArray[3].replace("人", ""));

        for(int i = 0; i < PROVINCE_NUM; i++)
        {
            if(strArray[0].equals(PROVINCE[i]))
            {
                //全国感染人数增加
                PROVINCE_LIST.provinceList[0].addInfected(n);
                //全国疑似感染人数减少
                PROVINCE_LIST.provinceList[0].decreaseDoubted(n);
                //当地区感染人数增加
                PROVINCE_LIST.provinceList[i].addInfected(n);
                //当地区疑似感染人数减少
                PROVINCE_LIST.provinceList[i].decreaseDoubted(n);
                break;
            }
        }
    }

    /*
  对符合排除人数的行作数据处理
  @param line:日志行
  @param pl:省份列表
   */
    public void dealDoubted2None(String line) {
        //将字符串以空格分割为多个字符串
        String[] strArray = line.split(" ");
        int n = Integer.parseInt(strArray[3].replace("人", ""));

        for(int i = 0; i < PROVINCE_NUM; i++)
        {
            if(strArray[0].equals(PROVINCE[i]))
            {
                //全国疑似感染人数减少
                PROVINCE_LIST.provinceList[0].decreaseDoubted(n);
                //当地区疑似感染人数减少
                PROVINCE_LIST.provinceList[i].decreaseDoubted(n);
                break;
            }
        }
    }


    public static void main(String[] args) {
        InfectStatistic infectStatistic = new InfectStatistic();
        InfectStatistic.DealLog dealLog = infectStatistic.new DealLog();
        //InfectStatistic.ProvinceList provinceList = infectStatistic.new ProvinceList();
        InfectStatistic.CmdArgs cmdArgs = infectStatistic.new CmdArgs(args);

        InfectStatistic.CmdHandler cmdHandler = infectStatistic.new CmdHandler(cmdArgs, dealLog);

        InfectStatistic.ListCommand listCommand = infectStatistic.new ListCommand(cmdHandler, cmdArgs);


        if(cmdArgs.getCmd().equals("list")) {
            listCommand.execute();
        }

        //dealLog.execute(line, provinceList);
        //provinceList.showStat();


    }
}
