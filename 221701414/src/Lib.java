
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





