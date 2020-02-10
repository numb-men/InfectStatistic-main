
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Lib
 * TODO
 *
 * @author 221701414 黎家泽
 * @version xxx
 * @since 2020年2月6日15:28:23
 */
public class Lib {

    private String[] args;

    public Lib(String[] args) {
        this.args = args;
    }

    /**
     * 执行命令
     */
    public void execute() throws FileNotFoundException {
        if (this.args == null) {
            return;
        }
        CmdArgs cmdArgs = new CmdArgs(this.args);
        //比较是否为 list 命令
        if (!cmdArgs.getCmd().equals(MyList.params[0])) {
            throw new UnsupportedOperationException("命令输入错误");
        }
        MyList list = new MyList(cmdArgs);
        ControlCommand controlCommand = new ControlCommand();
        //list -log
        if (cmdArgs.has(MyList.params[1])) {
            controlCommand.setCommands(0, new ListLogCommand(list));
        }
        //list -date
        if (cmdArgs.has(MyList.params[2])) {
            controlCommand.setCommands(1, new ListDateCommand(list));
        }
        //list -province
        if (cmdArgs.has(MyList.params[3])) {
            controlCommand.setCommands(2, new ListProvinceCommand(list));
        }
        //list -type
        if (cmdArgs.has(MyList.params[4])) {
            controlCommand.setCommands(3, new ListTypeCommand(list));
        }
        //list -out 输出在最后执行
        if (cmdArgs.has(MyList.params[5])) {
            controlCommand.setCommands(4, new ListOutCommand(list));
        }
        //执行全部命令
        controlCommand.sureExecuteAll();
    }
}

/**
 * 解析命令行参数
 *
 * @author 221701414 黎家泽
 * @version xxx
 * @since 2020年2月8日16:20:58
 */
class CmdArgs {

    /**
     * 用于存放命令行参数的键值对
     */
    private HashMap<String, String[]> argMap = new HashMap<>();

    /**
     * 命令数组
     */
    private String[] args;

    /**
     * 传入命令行参数数组构造
     *
     * @param args
     */
    public CmdArgs(String[] args) {
        this.args = args;
        init();
    }

    /**
     * 传入命令行参数字符串构造
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
     * 遍历文件的命令，调用闭包
     *
     * @param fileName
     */
    public static void eachLine(String fileName) {
        //...
    }

    /**
     * 获取命令
     *
     * @return
     */
    public String getCmd() {
        return this.args != null && this.args.length >= 0 ? this.args[0].toString() : null;
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
                    this.argMap.get(key)[0].toString() : null;
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

    //---------------------------getter/setter------------------------------------

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

    //---------------------------getter/setter------------------------------------
}

//------------------------------命令模式实现list命令--------------↓-----------------------

/**
 * list命令
 */
class MyList {
    /**
     * list命令的参数
     */
    public final static String[] params = {"list", "-log", "-date", "-province", "-type", "-out"};

    /**
     * 输入的命令
     */
    private CmdArgs cmdArgs;

    public MyList(CmdArgs cmdArgs) {
        this.cmdArgs = cmdArgs;
    }

    /**
     *
     */
    public void log() throws FileNotFoundException {
        if (cmdArgs == null) {
            return;
        }
        String logValue = cmdArgs.getArgVal(params[1]);
        if (logValue != null) {
            String content = FileOperate.readFile(logValue);
            if (content != null) {
                String[] logLine = content.split("#");

            }
        }
        throw new FileNotFoundException("日志文件不存在");
    }

    public void out() {
        //
    }

    public void date() {
        //
    }

    public void type() {

    }

    public void province() {
        //
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
    public void execute() throws FileNotFoundException {
        list.log();
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
 * list -date
 */
class ListDateCommand implements Command {
    private MyList list;

    public ListDateCommand(MyList list) {
        this.list = list;
    }

    @Override
    public void execute() {
        list.date();
    }
}

/**
 * list -type
 */
class ListTypeCommand implements Command {
    /**
     * list命令的参数 -type ，对应的值
     */
    private final static String[] typeValue = {"ip", "sp", "cure", "dead"};

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
 * list -province
 */
class ListProvinceCommand implements Command {
    /**
     * list命令的参数 -province，对应的值
     */
    private final static String[] provinceValue = {"安徽", "澳门", "北京", "重庆", "福建", "甘肃", "广东", "广西", "贵州",
            "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海",
            "山东", "山西", "陕西", "上海", "四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江"};

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
 * 命令控制
 */
class ControlCommand {
    private static final int CONTROL_SIZE = 5;

    private Command[] commands;

    public ControlCommand() {
        commands = new Command[CONTROL_SIZE];
    }

    /**
     * 添加一条待执行命令
     */
    public void setCommands(int index, Command command) {
        commands[index] = command;
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
        for (int i = commands.length; i >= 0; i--) {
            commands[i].execute();
        }
    }
}

//------------------------------命令模式实现list命令------------------↑-------------------

/**
 * FileOperate
 * <p>
 * 实现对文件的操作
 *
 * @author 221701414 黎家泽
 * @version xxx
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
        if (file.isFile() && file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuffer sb = new StringBuffer();
                String line = null;
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
}


