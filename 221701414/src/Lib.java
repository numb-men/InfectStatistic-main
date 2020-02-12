
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lib
 *
 * @author 221701414 黎家泽
 * @version 1.0
 * @since 2020年2月6日15:28:23
 */
public class Lib {

    private String[] args;

    public Lib(String[] args) {
        this.args = args;
    }

    /**
     * 执行命令
     *
     * @throws FileNotFoundException
     */
    public void execute() throws FileNotFoundException {
        if (this.args == null) {
            return;
        }
        CmdArgs cmdArgs = new CmdArgs(this.args);
        //比较是否为 list 命令
        if (!cmdArgs.getCmd().equals(MyList.PARAMS[0])) {
            throw new UnsupportedOperationException("命令输入错误");
        }
        /*
         * list命令的参数，按照：list -log -date -province -type -out的顺序执行，
         * 如果一个非必要参数缺省：-date -type -province,
         * 仍然按照顺序执行缺省参数的默认值
         * */
        MyList list = new MyList(cmdArgs);
        ControlCommand controlCommand = new ControlCommand();
        //list -log
        controlCommand.setCommands(0, new ListLogCommand(list));
        //list -date
        controlCommand.setCommands(1, new ListDateCommand(list));
        //list -province
        controlCommand.setCommands(2, new ListProvinceCommand(list));
        //list -type
        controlCommand.setCommands(3, new ListTypeCommand(list));
        //list -out 输出在最后执行
        controlCommand.setCommands(4, new ListOutCommand(list));
        //执行全部命令
        controlCommand.sureExecuteAll();
    }
}

/**
 * 解析命令行参数
 *
 * @author 221701414 黎家泽
 * @version 1.0
 * @since 2020年2月8日16:20:58
 */
class CmdArgs {
    /**
     * 用于存放命令行参数的键值对，比如：
     * key="-log" value="D:\log\"
     */
    private HashMap<String, String[]> argMap = new HashMap<>();

    /**
     * 输入的命令数组
     */
    private String[] args;

    /**
     * 传入命令数组构造
     *
     * @param args
     */
    public CmdArgs(String[] args) {
        this.args = args;
        init();
    }

    /**
     * 传入命令字符串构造
     */
    public CmdArgs(String argsStr) {
        this(argsStr.split(" "));
    }

    /**
     * 初始化对象
     */
    private void init() {
        parseArgs(this.args);
    }

    /**
     * 命令行参数数组解析
     *
     * @param args 命令行传来的参数数组
     * @return 命令行参数数组解析后的键值对
     */
    private HashMap<String, String[]> parseArgs(String[] args) {
        ArrayList<String> argList = new ArrayList<>();
        String argKey = null;
        String[] valueMap = null;

        //处理命令
        valueMap = new String[1];
        argList.add(args[0]);
        argList.toArray(valueMap);
        this.argMap.put(args[0], valueMap);
        argList.clear();

        //处理参数值
        for (int i = 1; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("-")) {
                if (argKey != null) {
                    valueMap = new String[argList.size()];
                    argList.toArray(valueMap);
                    this.argMap.put(argKey, valueMap);
                    argList.clear();
                }
                argKey = arg;
            } else {
                argList.add(arg);
            }
        }
        if (argKey != null) {
            valueMap = new String[argList.size()];
            argList.toArray(valueMap);
            this.argMap.put(argKey, valueMap);
            argList.clear();
        }
        return this.argMap;
    }

    /**
     * 获取命令
     *
     * @return
     */
    public String getCmd() {
        return this.args != null && this.args.length >= 0 ? this.args[0] : null;
    }

    /**
     * 获取某个命令行参数的值，多个值只会返回第一个
     *
     * @param key
     * @return
     */
    public String getArgVal(String key) {
        if (this.argMap.containsKey(key)) {
            return this.argMap.get(key) != null && this.argMap.get(key).length >= 0 ?
                    this.argMap.get(key)[0] : null;
        }
        return null;
    }

    /**
     * 获取某个命令行参数的值，返回字符串数组
     *
     * @param key
     * @return
     */
    public String[] getArgVals(String key) {
        if (this.argMap.containsKey(key)) {
            return this.argMap.get(key) != null && this.argMap.get(key).length >= 0 ?
                    this.argMap.get(key) : null;
        }
        return null;
    }

    /**
     * 判断该命令是否有对应的参数
     *
     * @param key
     * @return
     */
    public boolean has(String key) {
        return this.argMap.containsKey(key);
    }

    //---------------------------getter/setter-----------------↓-------------------

    public HashMap<String, String[]> getArgMap() {
        return argMap;
    }

    public void setArgMap(HashMap<String, String[]> argMap) {
        this.argMap = argMap;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    //---------------------------getter/setter----------------↑--------------------
}

//------------------------------命令模式实现list命令--------------↓-----------------------

/**
 * list命令
 */
class MyList {
    /**
     * list命令的参数
     */
    public final static String[] PARAMS = {"list", "-log", "-date", "-province", "-type", "-out"};

    /**
     * list命令的参数 -type ，对应的所有值
     */
    public final static String[] TYPE_VALUE = {"ip", "sp", "cure", "dead"};

    /**
     * list 命令的参数 -province，对应的所有值
     */
    public final static String[] PROCINCE_VALUE = {"全国", "安徽", "北京", "重庆", "福建", "甘肃", "广东", "广西", "贵州",
            "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海",
            "山东", "山西", "陕西", "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江"};

    /**
     * 输出文件末尾需要添加的说明信息
     */
    private final static String REMIND = "// 该文档并非真实数据，仅供测试使用";

    /**
     * 日志的目录
     * -log 参数
     */
    private String dir;

    /**
     * 需要处理的log文件地址
     * -date 参数处理后，得到的需要处理的log文件地址
     */
    private ArrayList<String> logPath;

    /**
     * list命令的参数 -province，对应的值
     * -province 参数
     */
    private String[] provinceValue;

    /**
     * 需要输出的类型和顺序
     * -type 参数
     */
    private String[] outType;

    /**
     * 输出的路径
     * -out 参数
     */
    private String outPath;

    /**
     * 输入的命令
     */
    private CmdArgs cmdArgs;

    /**
     * list命令操作的日志含有的省份，以及 -provice 参数含有的省份，包括 “全国”
     * 通过键值对存放，比如：
     * key="全国" value=ProvinceStatus对象
     */
    public LinkedHashMap<String, ProvinceStatus> linkedHashMap;


    public MyList(CmdArgs cmdArgs) {
        this.cmdArgs = cmdArgs;
        this.linkedHashMap = new LinkedHashMap<>();
        //默认顺序，感染 疑似 治愈 死亡
        this.outType = TYPE_VALUE;
    }

    /**
     * 实现 list -log
     */
    public void log() {
        if (this.cmdArgs == null) {
            return;
        }
        String logValue = this.cmdArgs.getArgVal(PARAMS[1]);
        if (logValue != null) {
            dir = logValue;
        }
    }

    /**
     * 实现 list -date
     *
     * @throws FileNotFoundException
     */
    public void date() throws FileNotFoundException {
        if (this.cmdArgs == null || this.dir == null) {
            return;
        }
        getLogPath();
    }

    /**
     * 实现 list -province
     */
    public void province() {
        if (this.cmdArgs == null) {
            return;
        }
        //注意：如果 -pvovince 缺省，这里的值为空
        this.provinceValue = this.cmdArgs.getArgVals(PARAMS[3]);
        if (this.provinceValue != null) {
            for (int i = 0; i < this.provinceValue.length; i++) {
                this.linkedHashMap.put(this.provinceValue[i], new ProvinceStatus(this.provinceValue[i]));
            }
        }
    }

    /**
     * 实现 list -type
     */
    public void type() {
        if (this.cmdArgs == null || this.cmdArgs.getArgVal(PARAMS[4]) == null) {
            return;
        }
        outType = this.cmdArgs.getArgVals(PARAMS[4]);
    }

    /**
     * 实现 list -out
     */
    public void out() {
        if (this.cmdArgs == null || this.dir == null) {
            return;
        }
        this.outPath = this.cmdArgs.getArgVal(PARAMS[5]);
        if (this.outPath == null) {
            return;
        }

        //读入日志信息
        readLog();

        //将 “全国” 的信息添加进 this.linkedHashMap
        getProvinceStatusAll();

        //删除不需要输出的省份
        deleteNotOutputProvicne();

        //获取需要输出的信息
        String outStr = getOutStr();

        //将输出信息写入文件
        FileOperate.writeTxt(outPath, outStr);
    }

    /**
     * 形成责任链
     */
    private static AbstractLogLineType getChainOfLogLine() {
        NewInfectorLogLine newInfectorLogLine = new NewInfectorLogLine(AbstractLogLineType.LOGLINE_TYPE[0]);
        NewSuspectLogLine newSuspectLogLine = new NewSuspectLogLine(AbstractLogLineType.LOGLINE_TYPE[1]);
        InInfectorLogLine inInfectorLogLine = new InInfectorLogLine(AbstractLogLineType.LOGLINE_TYPE[2]);
        InSuspectLogLine inSuspectLogLine = new InSuspectLogLine(AbstractLogLineType.LOGLINE_TYPE[3]);
        DeathLogLine deathLogLine = new DeathLogLine(AbstractLogLineType.LOGLINE_TYPE[4]);
        CureLogLine cureLogLine = new CureLogLine(AbstractLogLineType.LOGLINE_TYPE[5]);
        DefiniteLogLine definiteLogLine = new DefiniteLogLine(AbstractLogLineType.LOGLINE_TYPE[6]);
        ExcludeLogLine excludeLogLine = new ExcludeLogLine(AbstractLogLineType.LOGLINE_TYPE[7]);
        MismatchingLogLine mismatchingLogLine = new MismatchingLogLine(AbstractLogLineType.LOGLINE_TYPE[8]);

        newInfectorLogLine.setNextLogType(newSuspectLogLine);
        newSuspectLogLine.setNextLogType(inInfectorLogLine);
        inInfectorLogLine.setNextLogType(inSuspectLogLine);
        inSuspectLogLine.setNextLogType(deathLogLine);
        deathLogLine.setNextLogType(cureLogLine);
        cureLogLine.setNextLogType(definiteLogLine);
        definiteLogLine.setNextLogType(excludeLogLine);
        excludeLogLine.setNextLogType(mismatchingLogLine);

        return newInfectorLogLine;
    }

    /**
     * 获取需要读入的log日志路径
     *
     * @throws FileNotFoundException
     */
    private void getLogPath() throws FileNotFoundException {
        logPath = FileOperate.getAllFileName(dir);
        //-date 参数日期
        String dateValue = this.cmdArgs.getArgVal(PARAMS[2]);
        if (dateValue == null) {
            //使用logPath
            return;
        }
        //当前日期
        LocalDate today = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayStr = today.format(fmt);
        //日期比较
        if (!LocalDate.parse(dateValue).isAfter(LocalDate.parse(todayStr))) {
            for (int i = 0; i < this.logPath.size(); i++) {
                String[] dateStrNum = this.logPath.get(i).split("\\.");
                if (dateStrNum != null && dateStrNum.length > 0) {
                    String dateStr = dateStrNum[0];
                    if (LocalDate.parse(dateStr).isAfter(LocalDate.parse(dateValue))) {
                        this.logPath.remove(i);
                        i--;
                    }
                }
            }
            return;
        }
        throw new UnsupportedOperationException("日期输入错误");
    }

    /**
     * 读入日志信息
     */
    private void readLog() {
        AbstractLogLineType abstractLogLineType = getChainOfLogLine();
        for (int i = 0; i < this.logPath.size(); i++) {
            String content = FileOperate.readFile(this.dir + this.logPath.get(i));
            if (content != null) {
                String[] logLine = content.split("#");
                for (int j = 0; j < logLine.length; j++) {
                    //处理日志行
                    abstractLogLineType.matchLogLine(logLine[j], this.linkedHashMap);
                }
            }
        }
    }


    /**
     * 获取 “全国” 的数据
     */
    private void getProvinceStatusAll() {
        ProvinceStatus provinceStatusAll = new ProvinceStatus(PROCINCE_VALUE[0]);
        for (String provinceStatusKey : this.linkedHashMap.keySet()) {
            ProvinceStatus provinceStatus = this.linkedHashMap.get(provinceStatusKey);

            int infect = provinceStatus.getInfect();
            int suspect = provinceStatus.getSuspect();
            int cure = provinceStatus.getCure();
            int death = provinceStatus.getDeath();
            provinceStatusAll.setInfect(provinceStatusAll.getInfect() + infect);
            provinceStatusAll.setSuspect(provinceStatusAll.getSuspect() + suspect);
            provinceStatusAll.setCure(provinceStatusAll.getCure() + cure);
            provinceStatusAll.setDeath(provinceStatusAll.getDeath() + death);
        }
        this.linkedHashMap.put(PROCINCE_VALUE[0], provinceStatusAll);
    }

    /**
     * 删除不需要输出的省份
     */
    private void deleteNotOutputProvicne() {
        if (this.provinceValue != null) {
            boolean flag;
            ArrayList<String> tempList = new ArrayList<>();
            for (String provinceStatusKey : this.linkedHashMap.keySet()) {
                ProvinceStatus provinceStatus = this.linkedHashMap.get(provinceStatusKey);
                flag = false;
                for (int i = 0; i < this.provinceValue.length; i++) {
                    if (provinceStatus.getName().equals(this.provinceValue[i])) {
                        flag = true;
                    }
                }
                if (!flag) {
                    tempList.add(provinceStatusKey);
                }
            }
            for (int i = 0; i < tempList.size(); i++) {
                this.linkedHashMap.remove(tempList.get(i));
            }
        }
    }

    /**
     * 获取需要输出的信息
     */
    private String getOutStr() {
        String outStr = "";
        for (int i = 0; i < PROCINCE_VALUE.length; i++) {
            if (this.linkedHashMap.containsKey(PROCINCE_VALUE[i])) {
                outStr += PROCINCE_VALUE[i];
                for (int k = 0; k < this.outType.length; k++) {
                    outStr += this.linkedHashMap.get(PROCINCE_VALUE[i]).getOutTypeStr(this.outType[k]);
                }
                outStr += "\n";
            }
        }
        outStr += REMIND;
        return outStr;
    }
}

/**
 * 命令的接口
 */
interface Command {
    void execute() throws FileNotFoundException;
}

/**
 * list -log
 */
class ListLogCommand implements Command {
    private MyList list;

    public ListLogCommand(MyList list) {
        this.list = list;
    }

    @Override
    public void execute() {
        list.log();
    }
}

/**
 * list -date
 */
class ListDateCommand implements Command {
    private MyList list;

    public ListDateCommand(MyList list) {
        this.list = list;
    }

    @Override
    public void execute() throws FileNotFoundException {
        list.date();
    }
}

/**
 * list -province
 */
class ListProvinceCommand implements Command {
    private MyList list;

    public ListProvinceCommand(MyList list) {
        this.list = list;
    }

    @Override
    public void execute() {
        list.province();
    }
}

/**
 * list -type
 */
class ListTypeCommand implements Command {

    private MyList list;

    public ListTypeCommand(MyList list) {
        this.list = list;
    }

    @Override
    public void execute() {
        list.type();
    }
}

/**
 * list -out
 */
class ListOutCommand implements Command {
    private MyList list;

    public ListOutCommand(MyList list) {
        this.list = list;
    }

    @Override
    public void execute() {
        list.out();
    }
}

/**
 * 命令控制
 */
class ControlCommand {
    /**
     * list 总共有5种参数,分别是：-log -date -type -province -out
     */
    private static final int CONTROL_SIZE = 5;

    private Command[] commands;

    public ControlCommand() {
        this.commands = new Command[CONTROL_SIZE];
    }

    /**
     * 添加一条待执行命令
     */
    public void setCommands(int index, Command command) {
        this.commands[index] = command;
    }

    /**
     * 一条命令确认开始执行
     */
    public void sureExecute(int index) throws FileNotFoundException {
        commands[index].execute();
    }

    /**
     * 全部命令确认开始执行
     */
    public void sureExecuteAll() throws FileNotFoundException {
        for (int i = 0; i < commands.length; i++) {
            commands[i].execute();
        }
    }
}

//------------------------------命令模式实现list命令------------------↑-------------------

//----------------------------责任链模式匹配8种不同的日志行-------------↓------------------

/**
 * 日志行
 */
abstract class AbstractLogLineType {

    public final static String[] LOGLINE_TYPE = {
            "(\\S+) 新增 感染患者 (\\d+)人",
            "(\\S+) 新增 疑似患者 (\\d+)人",
            "(\\S+) 感染患者 流入 (\\S+) (\\d+)人",
            "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人",
            "(\\S+) 死亡 (\\d+)人",
            "(\\S+) 治愈 (\\d+)人",
            "(\\S+) 疑似患者 确诊感染 (\\d+)人",
            "(\\S+) 排除 疑似患者 (\\d+)人",
            "([\\s\\S]*)"
    };

    /**
     * 日志行对应的正则表达式
     */
    protected String logLineTypeRegExp;

    /**
     * 责任链中的下一个元素
     */
    protected AbstractLogLineType nextLogLineType;

    public void setNextLogType(AbstractLogLineType nextLogLineType) {
        this.nextLogLineType = nextLogLineType;
    }

    public void matchLogLine(String logLineStr, LinkedHashMap<String, ProvinceStatus> linkedHashMap) {
        if (logLineStr.matches(logLineTypeRegExp)) {
            executeLogLine(logLineStr, linkedHashMap);
            return;
        }
        if (nextLogLineType != null) {
            nextLogLineType.matchLogLine(logLineStr, linkedHashMap);
        }
    }

    protected abstract void executeLogLine(String logLineStr, LinkedHashMap<String, ProvinceStatus> linkedHashMap);

}

/**
 * <省> 新增 感染患者 n 人
 */
class NewInfectorLogLine extends AbstractLogLineType {
    public NewInfectorLogLine(String logLineTypeRegExp) {
        this.logLineTypeRegExp = logLineTypeRegExp;
    }

    @Override
    protected void executeLogLine(String logLineStr, LinkedHashMap<String, ProvinceStatus> linkedHashMap) {
//        System.out.println(logLineStr);
        Pattern pattern = Pattern.compile(AbstractLogLineType.LOGLINE_TYPE[0]);
        Matcher matcher = pattern.matcher(logLineStr);
        if (matcher.find()) {
            String province = matcher.group(1);
            int num = Integer.parseInt(matcher.group(2));
//            System.out.println(province);
//            System.out.println(num);
            if (!linkedHashMap.containsKey(province)) {
                linkedHashMap.put(province, new ProvinceStatus(province));
            }
            ProvinceStatus provinceStatus = linkedHashMap.get(province);
            provinceStatus.setInfect(provinceStatus.getInfect() + num);
            linkedHashMap.put(province, provinceStatus);
        }
    }
}

/**
 * <省> 新增 疑似患者 n 人
 */
class NewSuspectLogLine extends AbstractLogLineType {
    public NewSuspectLogLine(String logLineTypeRegExp) {
        this.logLineTypeRegExp = logLineTypeRegExp;
    }

    @Override
    protected void executeLogLine(String logLineStr, LinkedHashMap<String, ProvinceStatus> linkedHashMap) {
//        System.out.println(logLineStr);
        Pattern pattern = Pattern.compile(AbstractLogLineType.LOGLINE_TYPE[1]);
        Matcher matcher = pattern.matcher(logLineStr);
        if (matcher.find()) {
            String province = matcher.group(1);
            int num = Integer.parseInt(matcher.group(2));
//            System.out.println(province);
//            System.out.println(num);
            if (!linkedHashMap.containsKey(province)) {
                linkedHashMap.put(province, new ProvinceStatus(province));
            }
            ProvinceStatus provinceStatus = linkedHashMap.get(province);
            provinceStatus.setSuspect(provinceStatus.getSuspect() + num);
            linkedHashMap.put(province, provinceStatus);
        }
    }
}

/**
 * <省1> 感染患者 流入 <省2> n人
 */
class InInfectorLogLine extends AbstractLogLineType {
    public InInfectorLogLine(String logLineTypeRegExp) {
        this.logLineTypeRegExp = logLineTypeRegExp;
    }

    @Override
    protected void executeLogLine(String logLineStr, LinkedHashMap<String, ProvinceStatus> linkedHashMap) {
//        System.out.println(logLineStr);
        Pattern pattern = Pattern.compile(AbstractLogLineType.LOGLINE_TYPE[2]);
        Matcher matcher = pattern.matcher(logLineStr);
        if (matcher.find()) {
            String provinceA = matcher.group(1);
            String provinceB = matcher.group(2);
            int num = Integer.parseInt(matcher.group(3));
//            System.out.println(provinceA);
//            System.out.println(provinceB);
//            System.out.println(num);
            if (linkedHashMap.containsKey(provinceA)) {
                if (linkedHashMap.containsKey(provinceB)) {
                    //
                } else {
                    linkedHashMap.put(provinceB, new ProvinceStatus(provinceB));
                }
            } else {
                if (linkedHashMap.containsKey(provinceB)) {
                    linkedHashMap.put(provinceA, new ProvinceStatus(provinceA));
                } else {
                    linkedHashMap.put(provinceB, new ProvinceStatus(provinceB));
                    linkedHashMap.put(provinceA, new ProvinceStatus(provinceA));
                }
            }
            ProvinceStatus provinceStatusA = linkedHashMap.get(provinceA);
            ProvinceStatus provinceStatusB = linkedHashMap.get(provinceB);
            provinceStatusA.setInfect(provinceStatusA.getInfect() - num);
            provinceStatusB.setInfect(provinceStatusB.getInfect() + num);
            linkedHashMap.put(provinceA, provinceStatusA);
            linkedHashMap.put(provinceB, provinceStatusB);
        }
    }
}

/**
 * <省1> 疑似患者 流入 <省2> n人
 */
class InSuspectLogLine extends AbstractLogLineType {
    public InSuspectLogLine(String logLineTypeRegExp) {
        this.logLineTypeRegExp = logLineTypeRegExp;
    }

    @Override
    protected void executeLogLine(String logLineStr, LinkedHashMap<String, ProvinceStatus> linkedHashMap) {
//        System.out.println(logLineStr);
        Pattern pattern = Pattern.compile(AbstractLogLineType.LOGLINE_TYPE[3]);
        Matcher matcher = pattern.matcher(logLineStr);
        if (matcher.find()) {
            String provinceA = matcher.group(1);
            String provinceB = matcher.group(2);
            int num = Integer.parseInt(matcher.group(3));
//            System.out.println(provinceA);
//            System.out.println(provinceB);
//            System.out.println(num);
            if (linkedHashMap.containsKey(provinceA)) {
                if (linkedHashMap.containsKey(provinceB)) {
                    //
                } else {
                    linkedHashMap.put(provinceB, new ProvinceStatus(provinceB));
                }
            } else {
                if (linkedHashMap.containsKey(provinceB)) {
                    linkedHashMap.put(provinceA, new ProvinceStatus(provinceA));
                } else {
                    linkedHashMap.put(provinceB, new ProvinceStatus(provinceB));
                    linkedHashMap.put(provinceA, new ProvinceStatus(provinceA));
                }
            }
            ProvinceStatus provinceStatusA = linkedHashMap.get(provinceA);
            ProvinceStatus provinceStatusB = linkedHashMap.get(provinceB);
            provinceStatusA.setSuspect(provinceStatusA.getSuspect() - num);
            provinceStatusB.setSuspect(provinceStatusB.getSuspect() + num);
            linkedHashMap.put(provinceA, provinceStatusA);
            linkedHashMap.put(provinceB, provinceStatusB);
        }
    }
}

/**
 * <省> 死亡 n人
 */
class DeathLogLine extends AbstractLogLineType {
    public DeathLogLine(String logLineTypeRegExp) {
        this.logLineTypeRegExp = logLineTypeRegExp;
    }

    @Override
    protected void executeLogLine(String logLineStr, LinkedHashMap<String, ProvinceStatus> linkedHashMap) {
//        System.out.println(logLineStr);
        Pattern pattern = Pattern.compile(AbstractLogLineType.LOGLINE_TYPE[4]);
        Matcher matcher = pattern.matcher(logLineStr);
        if (matcher.find()) {
            String province = matcher.group(1);
            int num = Integer.parseInt(matcher.group(2));
//            System.out.println(province);
//            System.out.println(num);
            if (!linkedHashMap.containsKey(province)) {
                linkedHashMap.put(province, new ProvinceStatus(province));
            }
            ProvinceStatus provinceStatus = linkedHashMap.get(province);
            provinceStatus.setDeath(provinceStatus.getDeath() + num);
            provinceStatus.setInfect(provinceStatus.getInfect() - num);
            linkedHashMap.put(province, provinceStatus);
        }
    }
}

/**
 * <省> 治愈 n人
 */
class CureLogLine extends AbstractLogLineType {
    public CureLogLine(String logLineTypeRegExp) {
        this.logLineTypeRegExp = logLineTypeRegExp;
    }

    @Override
    protected void executeLogLine(String logLineStr, LinkedHashMap<String, ProvinceStatus> linkedHashMap) {
//        System.out.println(logLineStr);
        Pattern pattern = Pattern.compile(AbstractLogLineType.LOGLINE_TYPE[5]);
        Matcher matcher = pattern.matcher(logLineStr);
        if (matcher.find()) {
            String province = matcher.group(1);
            int num = Integer.parseInt(matcher.group(2));
//            System.out.println(province);
//            System.out.println(num);
            if (!linkedHashMap.containsKey(province)) {
                linkedHashMap.put(province, new ProvinceStatus(province));
            }
            ProvinceStatus provinceStatus = linkedHashMap.get(province);
            provinceStatus.setCure(provinceStatus.getCure() + num);
            provinceStatus.setInfect(provinceStatus.getInfect() - num);
            linkedHashMap.put(province, provinceStatus);
        }
    }
}

/**
 * <省> 疑似患者 确诊感染 n人
 */
class DefiniteLogLine extends AbstractLogLineType {
    public DefiniteLogLine(String logLineTypeRegExp) {
        this.logLineTypeRegExp = logLineTypeRegExp;
    }

    @Override
    protected void executeLogLine(String logLineStr, LinkedHashMap<String, ProvinceStatus> linkedHashMap) {
//        System.out.println(logLineStr);
        Pattern pattern = Pattern.compile(AbstractLogLineType.LOGLINE_TYPE[6]);
        Matcher matcher = pattern.matcher(logLineStr);
        if (matcher.find()) {
            String province = matcher.group(1);
            int num = Integer.parseInt(matcher.group(2));
//            System.out.println(province);
//            System.out.println(num);
            if (!linkedHashMap.containsKey(province)) {
                linkedHashMap.put(province, new ProvinceStatus(province));
            }
            ProvinceStatus provinceStatus = linkedHashMap.get(province);
            provinceStatus.setInfect(provinceStatus.getInfect() + num);
            provinceStatus.setSuspect(provinceStatus.getSuspect() - num);
            linkedHashMap.put(province, provinceStatus);
        }
    }
}

/**
 * <省> 排除 疑似患者 n人
 */
class ExcludeLogLine extends AbstractLogLineType {
    public ExcludeLogLine(String logLineTypeRegExp) {
        this.logLineTypeRegExp = logLineTypeRegExp;
    }

    @Override
    protected void executeLogLine(String logLineStr, LinkedHashMap<String, ProvinceStatus> linkedHashMap) {
//        System.out.println(logLineStr);
        Pattern pattern = Pattern.compile(AbstractLogLineType.LOGLINE_TYPE[7]);
        Matcher matcher = pattern.matcher(logLineStr);
        if (matcher.find()) {
            String province = matcher.group(1);
            int num = Integer.parseInt(matcher.group(2));
//            System.out.println(province);
//            System.out.println(num);
            if (!linkedHashMap.containsKey(province)) {
                linkedHashMap.put(province, new ProvinceStatus(province));
            }
            ProvinceStatus provinceStatus = linkedHashMap.get(province);
            provinceStatus.setSuspect(provinceStatus.getSuspect() - num);
            linkedHashMap.put(province, provinceStatus);
        }
    }
}

/**
 * 不匹配的情况
 */
class MismatchingLogLine extends AbstractLogLineType {
    public MismatchingLogLine(String logLineTypeRegExp) {
        this.logLineTypeRegExp = logLineTypeRegExp;
    }

    @Override
    protected void executeLogLine(String logLineStr, LinkedHashMap<String, ProvinceStatus> linkedHashMap) {
//        System.out.println("不匹配:" + logLineStr);
    }
}

//----------------------------责任链模式匹配8种不同的日志行----------------↑---------------

/**
 * 省/全国类
 */
class ProvinceStatus {
    /**
     * 省份名称，包括“全国”
     */
    private String name;

    /**
     * 感染患者数量
     */
    private int infect;

    /**
     * 疑似患者数量
     */
    private int suspect;

    /**
     * 治愈患者数量
     */
    private int cure;

    /**
     * 死亡患者数量
     */
    private int death;

    public ProvinceStatus(String name) {
        this.name = name;
        // 默认等于 0
        this.infect = 0;
        this.suspect = 0;
        this.cure = 0;
        this.death = 0;
    }

    /**
     * @param typeValue 命令行参数 -type 指定的一种输出类型
     * @return 返回整理好的字符串
     */
    public String getOutTypeStr(String typeValue) {
        switch (typeValue) {
            case "ip":
                return " " + "感染患者" + this.infect + "人";
            case "sp":
                return " " + "疑似患者" + this.suspect + "人";
            case "cure":
                return " " + "治愈" + this.cure + "人";
            case "dead":
                return " " + "死亡" + this.death + "人";
            default:
                throw new IllegalStateException("Unexpected value: " + typeValue);
        }
    }

    //-----------------------------------setter/getter----------------------↓-------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInfect() {
        return infect;
    }

    public void setInfect(int infect) {
        this.infect = infect;
    }

    public int getSuspect() {
        return suspect;
    }

    public void setSuspect(int suspect) {
        this.suspect = suspect;
    }

    public int getCure() {
        return cure;
    }

    public void setCure(int cure) {
        this.cure = cure;
    }

    public int getDeath() {
        return death;
    }

    public void setDeath(int death) {
        this.death = death;
    }

    //-----------------------------------setter/getter----------------------↑-------------------------------------


    @Override
    public String toString() {
        return "ProvinceStatus{" +
                "name='" + name + '\'' +
                ", infect=" + infect +
                ", suspect=" + suspect +
                ", cure=" + cure +
                ", death=" + death +
                '}';
    }
}

/**
 * FileOperate
 * <p>
 * 实现对文件的操作
 *
 * @author 221701414 黎家泽
 * @version 1.0
 * @since 2020年2月8日10:41:51
 */
class FileOperate {
    /**
     * 根据提供的文件地址，读取文件内容
     *
     * @param path 文件地址
     * @return 每行内容用 # 分隔的String
     */
    public static String readFile(String path) {
        File file = new File(path);
        if (file != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    //每一行的数据通过 # 分隔
                    sb.append(line + "#");
                }
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 进行写文件
     *
     * @param path    文件地址
     * @param content 需要写入的文件内容
     * @return void
     */
    public static void writeTxt(String path, String content) {
        FileOutputStream fileOutputStream = null;
        File file = new File(path);
        try {
            if (file.exists()) {
                //判断文件是否存在，如果不存在就新建一个txt
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定目录的所有文件
     *
     * @param dir 文件目录
     * @return 文件目录下面的所有文件名
     * @throws FileNotFoundException
     */
    public static ArrayList<String> getAllFileName(String dir) throws FileNotFoundException {
        ArrayList<String> fileList = new ArrayList<String>();
        File file = new File(dir);
        if (file != null) {
            File[] tempList = file.listFiles();
            for (int i = 0; i < tempList.length; i++) {
                if (tempList[i].isFile()) {
                    fileList.add(tempList[i].getName());
                }
            }
            return fileList;
        }
        throw new FileNotFoundException("文件路径错误");
    }
}

