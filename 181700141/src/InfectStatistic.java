import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * InfectStatistic TODO
 *
 * @author 181700141_呼叫哆啦A梦
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
    public static void main(String[] args) {
        int length = args.length;
        if (length == 0)
            return;
        else if (args[0].equals("list")) {
            ListCommand command = new ListCommand();
            try {
                command.dealParameter(args);
                command.carryOutActions();
            } catch (IllegalException e) {
                System.out.println(e);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }

        } else {
            System.out.println("不存在" + args[0] + "指令");
        }

    }
}

//当参数或参数值非法时将抛出该异常类
class IllegalException extends Exception {
    // 记录错误原因
    private String message;

    public IllegalException(String tMessage) {
        message = tMessage;
    }

    public String toString() {
        return message;
    }
}

/**
 * 
 * @author 181700141_呼叫哆啦A梦
 * 
 *         处理list命令
 * 
 *         -log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径 。
 * 
 *         -out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径 。
 * 
 *         -date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期之前的所有log文件。
 * 
 *         -type 可选择[ip： infection patients 感染患者，sp： suspected patients
 *         疑似患者，cure：治愈 ，dead：死亡患者]， 使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp
 *         cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况， 不指定该项默认会列出所有情况。
 * 
 *         -province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江。
 */

class ListCommand {
    // 存储各省感染患者人数
    private LinkedHashMap<String, Integer> ipMap = new LinkedHashMap<>();
    // 存储各省疑似患者人数
    private LinkedHashMap<String, Integer> spMap = new LinkedHashMap<>();
    // 存储各省治愈患者人数
    private LinkedHashMap<String, Integer> cureMap = new LinkedHashMap<>();
    // 存储各省死亡人数
    private LinkedHashMap<String, Integer> deadMap = new LinkedHashMap<>();
    // 存储全国情况
    private LinkedHashMap<String, Integer> countryMap = new LinkedHashMap<>();

    // 记录日志目录路径
    private String inDirectory = null;
    // 记录输出目录路径
    private String outDirectory = null;
    // 记录日期：date的参数值
    private String date = null;
    // 记录type的值，如果集合为空，默认列出全部情况
    private HashSet<String> type = new HashSet<String>();
    // 记录要列出的省
    private HashSet<String> province = new HashSet<String>();

    private boolean dateIsExist = false;
    private boolean typeIsExist = false;
    private boolean provinceIsExist = false;

    public ListCommand() {

        String[] provinces = { "安徽", "北京", "重庆", "福建", "甘肃", "广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南",
                "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江" };
        for (int i = 0; i < provinces.length; i++) {
            ipMap.put(provinces[i], 0);
            spMap.put(provinces[i], 0);
            cureMap.put(provinces[i], 0);
            deadMap.put(provinces[i], 0);
        }
        countryMap.put("感染患者", 0);
        countryMap.put("疑似患者", 0);
        countryMap.put("治愈", 0);
        countryMap.put("死亡", 0);
    }

    /**
     * 处理list命令的各参数，对各个参数初始化其处理类。
     * 
     * @param 用户输入的命令，含list
     * @throws 如果初步解析list命令如：list命令未提供该参数的执行方法 同一参数出现多次，必须存在的参数不存在时将抛出IllegalException
     */
    public void dealParameter(String[] args) throws IllegalException {
        int l = args.length;
        // 存储参数值
        String[] parameterValues;
        for (int i = 1; i < l; i++) {
            switch (args[i]) {
            case "-log":
                if (inDirectory != null)
                    throw new IllegalException("错误，重复出现 -log参数");
                if (i == l - 1 || args[i + 1].charAt(0) == '-')
                    throw new IllegalException("错误，未提供-log参数值");
                // 给日志目录路径赋值
                inDirectory = args[++i];
                break;
            case "-out":
                if (outDirectory != null)
                    throw new IllegalException("错误，重复出现-out参数");
                if (i == l - 1 || args[i + 1].charAt(0) == '-')
                    throw new IllegalException("错误，未提供-out参数值");
                // 给输出目录路径赋值
                outDirectory = args[++i];
                break;
            case "-date":
                if (dateIsExist)
                    throw new IllegalException("错误，重复出现-date参数");
                if (i < l - 1 && args[i + 1].charAt(0) != '-')
                    date = args[++i];
                if (date != null && !date.matches("\\d{4}-\\d{2}-\\d{2}"))
                    throw new IllegalException("错误，-date参数值非法，须符合XXXX-XX-XX格式（X为0-9）");
                dateIsExist = true;
                break;
            case "-type":
                if (typeIsExist)
                    throw new IllegalException("错误，重复出现-type参数");
                for (int j = i + 1; j < l; j++) {
                    if (args[j].charAt(0) == '-')
                        break;
                    if (type.contains(args[j]))
                        throw new IllegalException("错误，参数-type出现重复参数值");
                    i = j;
                    type.add(args[j]);
                }
                typeIsExist = true;
                break;
            case "-province":
                if (provinceIsExist)
                    throw new IllegalException("错误，重复出现-province参数");
                for (int j = i + 1; j < l; j++) {
                    if (args[j].charAt(0) == '-')
                        break;
                    if (province.contains(args[j]))
                        throw new IllegalException("错误，参数-province出现重复参数值");
                    i = j;
                    province.add(args[j]);
                }
                provinceIsExist = true;
                break;
            default:
                if (args[i].charAt(0) == '-')
                    throw new IllegalException("错误，list命令不支持" + args[i] + "参数");
                throw new IllegalException("错误，参数值" + args[i] + "非法");
            }// end of switch
        } // end of for
        if (outDirectory == null || inDirectory == null)
            throw new IllegalException("错误，参数-log及-out要求必须存在");
    }

    // 执行各参数所要求的操作
    public void carryOutActions() throws Exception {

        /*
         * System.out.println("log:" + inDirectory + "\nout:" + outDirectory + "\ndate:"
         * + date + "\ntype:"); for (String s : type) System.out.print("  " + s);
         * System.out.println("\nprovince:\n"); for (String s : province)
         * System.out.print("  " + s);
         */

        File file = new File(inDirectory);
        if (!file.exists() || !file.isDirectory())
            throw new IllegalException("错误，日志目录" + inDirectory + "不存在");
        handleFiles(file);

    }

    // 读取目录下的日志文件
    public void handleFiles(File file) throws Exception {
        String[] logFiles = file.list();
        Arrays.sort(logFiles);
        int l = logFiles.length;
        if (date != null && date.compareTo(logFiles[l - 1]) > 0)
            throw new IllegalException("错误，日期非法，超出日志最晚时间");
        if (date == null)
            date = logFiles[l - 1];
        for (int i = 0; i < l; i++) {
            if (logFiles[i].matches("\\S+\\.log\\.txt")) {
                handleFile(inDirectory + "/" + logFiles[i]);
            }
        }

    }

    public void handleFile(String route) throws Exception {

        // 定义正则表达式
        String s1 = "\\s*\\S+ 新增 感染患者 \\d+人\\s*";
        String s2 = "\\s*\\S+ 新增 疑似患者 \\d+人\\s*";
        String s3 = "\\s*\\S+ 感染患者 流入 \\S+ \\d+人\\s*";
        String s4 = "\\s*\\S+ 疑似患者 流入 \\S+ \\d+人\\s*";
        String s5 = "\\s*\\S+ 死亡 \\d+人\\s*";
        String s6 = "\\s*\\S+ 治愈 \\d+人\\s*";
        String s7 = "\\s*\\S+ 疑似患者 确诊感染 \\d+人\\s*";
        String s8 = "\\s*\\S+ 排除 疑似患者 \\d+人\\s*";

        FileInputStream fstream = new FileInputStream(new File(route));
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;
        while ((strLine = br.readLine()) != null) {
            if (strLine.matches(s1)) {

                System.out.println(strLine);

            } else if (strLine.matches(s1)) {

                System.out.println(strLine);

            } else if (strLine.matches(s2)) {

                System.out.println(strLine);

            } else if (strLine.matches(s3)) {

                System.out.println(strLine);

            } else if (strLine.matches(s4)) {

                System.out.println(strLine);

            } else if (strLine.matches(s5)) {

                System.out.println(strLine);

            } else if (strLine.matches(s6)) {

                System.out.println(strLine);

            } else if (strLine.matches(s7)) {

                System.out.println(strLine);

            } else if (strLine.matches(s8)) {

                System.out.println(strLine);

            }
        }
    }

}
