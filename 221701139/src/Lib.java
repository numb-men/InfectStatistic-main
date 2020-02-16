import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lib
 * TODO
 * @author cybersa
 * @version 1.0
 * @since 2020.2.10
 */
// 用来放自己的文件
public class Lib {
    public CommandReceiver commandReceiver;
    public Command command;

    public Lib(String[] args) {
        this.commandReceiver = new CommandReceiver();
        this.command = new ListCommand(args,this.commandReceiver);
    }
    // 执行
    public void execute() {
        this.command.execute();
    }
}

// 参数解析器类
class ArgParser {
    // 命令名称
    private String command;
    // 参数列表
    private Map<String, List<String>> arguments;
    /**
     * 从给定的命令行参数构造命令
     * @param args 命令行参数
     */
    public ArgParser(String[] args) {
        this.command = args[0];
        this.arguments = new HashMap<>();
        // 根据参数列表创建参数映射
        for (int i = 1;i<args.length;i++) {
            // 如果以-开头代表是参数名
            if (args[i].startsWith("-")) {
                String key = args[i];
                int j = i+1;
                List<String> vals = new ArrayList<>();

                while (!args[j].startsWith("-")) {
                    vals.add(args[j]);
                    j++;
                    if (j==args.length) {
                        break;
                    }
                }
                arguments.put(key,vals);
            }
        }
    }

    /**
     * 打印命令和参数列表
     * @param
     * @return
     */
    public void printArg() {
        System.out.println("命令为:"+command);
        for (String key:arguments.keySet()) {
            System.out.println(key+"======="+arguments.get(key));
        }
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }

    public void setArguments(Map<String, List<String>> arguments) {
        this.arguments = arguments;
    }

    public Map<String, List<String>> getArguments() {
        return arguments;
    }

    // 得到全部的键
    public Set<String> getKeys() {
        return arguments.keySet();
    }

    public List<String> getVals(String key) {
        return  arguments.get(key);
    }

    /**
     * 判断是否有某个参数
     * @param key 需要判断的键名
     * @return 是否有某个键
     */
    public boolean hasKey(String key) {
        return arguments.get(key)!=null;
    }
}

interface Command {
    void execute();
}

class CommandReceiver {

    // list命令
    public void list(String[] args) {
        // 封装参数
        ArgParser argParser = new ArgParser(args);
        // 根据参数调用不同的类
        String root = argParser.getVals("-log").get(0);
        String outFileName = argParser.getVals("-out").get(0);
        List<String> dates = argParser.getVals("-date");

        //执行完成,输出提示信息
        File path = new File(root);
        // 读取文件
        File[] files = path.listFiles();
        String filePath = null;

        if (dates!=null) {
            // 如果提供了要处理的文件的日期名
            // 直接按给定的文件名处理
            for (File file : files) {
                if (file.getName().contains(dates.get(0))) {
                    filePath = path + "/" + file.getName();
                    AllInformation allInformation = new AllInformation();
                    allInformation.processInfo(filePath);
                    allInformation.writeIntoLog(outFileName,argParser);
                    allInformation.printInfo();
                }
                break;
            }
        }
        else {
            // 如果没有给定日期就处理所有文件
//                File lastFile = files[files.length-1];
//                filePath = path+"/"+lastFile.getName();
            AllInformation prev = new AllInformation();
            for (File file : files) {
                filePath = path + "/" + file.getName();

                AllInformation allInformation = new AllInformation();
                // 获取上一个文件处理的信息,写入这个这次的文件中
                Map<String, Integer[]> info = prev.getInfo();
                allInformation.setting(info);

                // 之后处理这个文件的信息
                allInformation.processInfo(filePath);
                // 写入文件
                allInformation.writeIntoLog(outFileName, argParser);
                prev = allInformation;
            }

        }

    }
    // 下面还可以有其他命令

}

class ListCommand implements Command {
    private String[] args;  //参数数组
    private CommandReceiver receiver;

    /**
     * 构造函数
     * @param args 命令行参数
     * @param receiver  命令接收者
     */
    public ListCommand(String[] args, CommandReceiver receiver) {
        this.args = args;
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.list(args);
    }

    // 将需要输出的转换为下标
    public static List<Integer> judgeType(ArgParser argParser) {
        List<String> types = argParser.getVals("-type");
        List<Integer> newTypes = new ArrayList<>();

        // 如果没有给type参数就按顺序输出
        if (types==null) {
            newTypes.add(0);
            newTypes.add(1);
            newTypes.add(2);
            newTypes.add(3);
            return newTypes;
        }
        // 如果有给type参数就按type参数值的顺序输出
        for (String type:types) {
            if ("ip".equals(type)) {
                newTypes.add(0);
            }
            else if ("sp".equals(type)) {
                newTypes.add(1);
            }
            else if ("cure".equals(type)) {
                newTypes.add(2);
            }
            else if ("dead".equals(type)){
                newTypes.add(3);
            }
        }
        return newTypes;
    }
}

class NoCommand implements Command {
    @Override
    public void execute() {

    }
}

class RegOne {

    // <省> 新增 感染患者 n人
//    private final String REGEX = "(\\S+) 新增 感染患者 (\\d+)人";

    // 返回一个省份对应信息的映射
    public Map<String, Integer[]> process(String line) {
        Map<String,Integer[]> map = new HashMap<>();
        // 如果匹配代表该行由这个正则表达式处理
        String[] result = line.split(" ");
        // 获取省份
        String province = result[0];
        // 获取人数
        int population = Integer.parseInt(result[3].substring(0,result[3].length()-1));

        // 封装成map
        Integer[] num = {population,0,0,0};
        map.put(province,num);
        map.put("全国",new Integer[]{population,0,0,0});
        return map;
    }
}
class RegTwo {
//    private final String REGEX = "(\\S+) 新增 疑似患者 (\\d+)人";

    // 返回一个省份对应信息的映射
    public Map<String, Integer[]> process(String line) {
        Map<String,Integer[]> map = new HashMap<>();
        String[] result = line.split(" ");
        // 获取省份
        String province = result[0];
        // 获取人数
        int population = Integer.parseInt(result[3].substring(0,result[3].length()-1));

        // 封装成map
        Integer[] num = {0,population,0,0};
        map.put(province,num);
        map.put("全国",new Integer[]{0,population,0,0});
        return map;
    }
}

class RegThree {
//    private final String REGEX = "(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
    // 返回一个省份对应信息的映射

    public Map<String, Integer[]> process(String line) {

        Map<String,Integer[]> map = new HashMap<>();

        String[] result = line.split(" ");
        // 获取流出省份和流入省份
        String province1 = result[0];
        String province2 = result[3];
        // 获取人数
        int population = Integer.parseInt(result[4].substring(0,result[4].length()-1));

        // 封装成map
        Integer[] num = {-population,0,0,0};
        Integer[] num2 = {population,0,0,0};
        map.put(province1,num);
        map.put(province2,num2);
        return map;
    }
}

class RegFour {
//    private final String REGEX = "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
    // 返回一个省份对应信息的映射

    public Map<String, Integer[]> process(String line) {

        Map<String,Integer[]> map = new HashMap<>();

        String[] result = line.split(" ");
        // 获取流出省份和流入省份
        String province1 = result[0];
        String province2 = result[3];
        // 获取流入的疑似患者
        int population = Integer.parseInt(result[4].substring(0,result[4].length()-1));

        // 封装成map
        Integer[] num = {0,-population,0,0};
        Integer[] num2 = {0,population,0,0};
        map.put(province1,num);
        map.put(province2,num2);

        return map;
    }
}

class RegFive {
//    private final String REGEX = "(\\S+) 死亡 (\\d+)人";


    // 返回一个省份对应信息的映射
    public Map<String, Integer[]> process(String line) {
        Map<String,Integer[]> map = new HashMap<>();
        String[] result = line.split(" ");

        // 获取省份
        String province = result[0];
        // 获取人数
        int population = Integer.parseInt(result[2].substring(0,result[2].length()-1));

        // 封装成map
        Integer[] num = {-population,0,0,population};
        map.put(province,num);
        map.put("全国",new Integer[]{-population,0,0,population});
        return map;
    }
}

class RegSix {
//    private final String REGEX = "(\\S+) 治愈 (\\d+)人";

    // 返回一个省份对应信息的映射
    public Map<String, Integer[]> process(String line) {

        Map<String,Integer[]> map = new HashMap<>();

        String[] result = line.split(" ");
        // 获取省份
        String province = result[0];
        // 获取人数
        int population = Integer.parseInt(result[2].substring(0,result[2].length()-1));

        // 封装成map
        Integer[] num = {-population,0,population,0};
        map.put(province,num);
        map.put("全国",new Integer[]{-population,0,population,0});
        return map;
    }
}
class RegSeven {
//    private final String REGEX = "(\\S+) 疑似患者 确诊感染 (\\d+)人";
    // 返回一个省份对应信息的映射

    public Map<String, Integer[]> process(String line) {

        Map<String,Integer[]> map = new HashMap<>();
        String[] result = line.split(" ");
        // 获取省份
        String province = result[0];
        // 获取人数
        int population = Integer.parseInt(result[3].substring(0,result[3].length()-1));

        // 封装成map
        Integer[] num = {population,-population,0,0};
        map.put(province,num);
        map.put("全国",new Integer[]{population,-population,0,0});
        return map;
    }
}
class RegEight {
    private final String REGEX = "(\\S+) 排除 疑似患者 (\\d+)人";
    // 返回一个省份对应信息的映射
    public Map<String, Integer[]> process(String line) {

        Map<String,Integer[]> map = new HashMap<>();
        String[] result = line.split(" ");

        // 获取省份
        String province = result[0];
        // 获取人数
        int population = Integer.parseInt(result[3].substring(0,result[3].length()-1));
        // 封装成map
        Integer[] num = {0,-population,0,0};
        map.put(province,num);
        map.put("全国",new Integer[]{0,-population,0,0});
        return map;
    }
}

class AllInformation {
    private Map<String, Integer[]> info;
    private final String REGEX1 = "(\\S+) 新增 感染患者 (\\d+)人";
    private final String REGEX2 = "(\\S+) 新增 疑似患者 (\\d+)人";
    private final String REGEX3 = "(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
    private final String REGEX4 = "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
    private final String REGEX5 = "(\\S+) 死亡 (\\d+)人";
    private final String REGEX6 = "(\\S+) 治愈 (\\d+)人";
    private final String REGEX7 = "(\\S+) 疑似患者 确诊感染 (\\d+)人";
    private final String REGEX8 = "(\\S+) 排除 疑似患者 (\\d+)人";

    public AllInformation() {
        info = new LinkedHashMap<>();
        info.put("全国", new Integer[]{0, 0, 0, 0});
        info.put("安徽", new Integer[]{0, 0, 0, 0});
        info.put("北京", new Integer[]{0, 0, 0, 0});
        info.put("重庆", new Integer[]{0, 0, 0, 0});
        info.put("福建", new Integer[]{0, 0, 0, 0});
        info.put("甘肃", new Integer[]{0, 0, 0, 0});
        info.put("广东", new Integer[]{0, 0, 0, 0});
        info.put("广西", new Integer[]{0, 0, 0, 0});
        info.put("贵州", new Integer[]{0, 0, 0, 0});
        info.put("海南", new Integer[]{0, 0, 0, 0});
        info.put("河北", new Integer[]{0, 0, 0, 0});
        info.put("河南", new Integer[]{0, 0, 0, 0});
        info.put("黑龙", new Integer[]{0, 0, 0, 0});
        info.put("湖北", new Integer[]{0, 0, 0, 0});
        info.put("湖南", new Integer[]{0, 0, 0, 0});
        info.put("吉林", new Integer[]{0, 0, 0, 0});
        info.put("江苏", new Integer[]{0, 0, 0, 0});
        info.put("江西", new Integer[]{0, 0, 0, 0});
        info.put("辽宁", new Integer[]{0, 0, 0, 0});
        info.put("内蒙", new Integer[]{0, 0, 0, 0});
        info.put("宁夏", new Integer[]{0, 0, 0, 0});
        info.put("青海", new Integer[]{0, 0, 0, 0});
        info.put("山东", new Integer[]{0, 0, 0, 0});
        info.put("山西", new Integer[]{0, 0, 0, 0});
        info.put("陕西", new Integer[]{0, 0, 0, 0});
        info.put("上海", new Integer[]{0, 0, 0, 0});
        info.put("四川", new Integer[]{0, 0, 0, 0});
        info.put("天津", new Integer[]{0, 0, 0, 0});
        info.put("西藏", new Integer[]{0, 0, 0, 0});
        info.put("新疆", new Integer[]{0, 0, 0, 0});
        info.put("云南", new Integer[]{0, 0, 0, 0});
        info.put("浙江", new Integer[]{0, 0, 0, 0});

    }

    // 通过reg类返回的map信息设置要写入的文件信息
    public void setting(Map<String,Integer[]> lineInfo) {
        // 得到要写入的键
        Set<String> keys = lineInfo.keySet();
        for (String key:keys) {

            // 得到要写入的整数信息
            Integer[] newInfo = lineInfo.get(key);

            // 得到原始的整数信息
            Integer[] oriInfo = info.get(key);
            if ("福建".equals(key)) {
                System.out.println("修改完成前");
                System.out.println(key+" "+oriInfo[0]+" "+oriInfo[1]+" "+oriInfo[2]+" "+oriInfo[3]+" ");
            }


            for (int i = 0;i<newInfo.length;i++) {
                // 找到不为0的信息修改
                oriInfo[i] = oriInfo[i]+newInfo[i];
            }

            if ("福建".equals(key)) {
                System.out.println("修改完成hou");
                System.out.println(key + " " + oriInfo[0] + " " + oriInfo[1] + " " + oriInfo[2] + " " + oriInfo[3] + " ");
            }
            info.put(key,oriInfo);
        }
    }

    // 将读入的文件的每一行经过正则表达式处理
    public void processInfo(String filePath) {
        // filePath 为要处理的文件名
        try {
            // 准备输入流
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            Map<String,Integer[]> lineInfo;
            // 读取文件的每一行
            while((line = bufferedReader.readLine())!=null && !line.startsWith("//")) {
//                System.out.println(line);
                lineInfo = judgeReg(line);

                // 设置信息
                Set<String> keys = lineInfo.keySet();
//                for (String key:keys) {
//                    Integer[] num = lineInfo.get(key);
//                    System.out.println(key+"   "+num[0]+"  "+num[1]+"  "+num[2]+"  "+num[3]);
//                }
                this.setting(lineInfo);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 判断用哪个正则表达式
    public Map<String,Integer[]> judgeReg(String line) {
        Map<String,Integer[]> lineInfo = new HashMap<>();
        Pattern pattern1 = Pattern.compile(REGEX1);
        Pattern pattern2 = Pattern.compile(REGEX2);
        Pattern pattern3 = Pattern.compile(REGEX3);
        Pattern pattern4 = Pattern.compile(REGEX4);
        Pattern pattern5 = Pattern.compile(REGEX5);
        Pattern pattern6 = Pattern.compile(REGEX6);
        Pattern pattern7 = Pattern.compile(REGEX7);
        Pattern pattern8 = Pattern.compile(REGEX8);

        Matcher matcher1 = pattern1.matcher(line);
        Matcher matcher2 = pattern2.matcher(line);
        Matcher matcher3 = pattern3.matcher(line);
        Matcher matcher4 = pattern4.matcher(line);
        Matcher matcher5 = pattern5.matcher(line);
        Matcher matcher6 = pattern6.matcher(line);
        Matcher matcher7 = pattern7.matcher(line);
        Matcher matcher8 = pattern8.matcher(line);

        if(matcher1.find()) {
            lineInfo = new RegOne().process(line);
        }
        else if (matcher2.find()) {
            lineInfo = new RegTwo().process(line);

        }
        else if (matcher3.find()) {
            lineInfo = new RegThree().process(line);

        }
        else if (matcher4.find()) {
            lineInfo = new RegFour().process(line);

        }
        else if (matcher5.find()) {
            lineInfo = new RegFive().process(line);

        }
        else if (matcher6.find()) {
            lineInfo = new RegSix().process(line);

        }
        else if (matcher7.find()) {
            lineInfo = new RegSeven().process(line);


        }
        else if (matcher8.find()) {
            lineInfo = new RegEight().process(line);
        }
        return lineInfo;
    }

    // 打印allinformation里面的信息
    public void printInfo() {
        Set<String> keys = info.keySet();
        for (String key:keys) {
            Integer[] integers = info.get(key);
            System.out.println(String.format("%s %d %d %d %d",key,integers[0],
                    integers[1],integers[2],integers[3]));
        }
    }

    public Map<String,Integer[]> getInfo() {
        return this.info;
    }

    // 写入日志文件
    public void writeIntoLog(String logPath, ArgParser argParser) {
        // 经过上面处理已经获得了全部的要写入日志的信息
        // 需要输出的类型
        List<Integer> indexTypes = ListCommand.judgeType(argParser);
        List<String>  types = new ArrayList<>();
        types.add("感染患者");
        types.add("疑似患者");
        types.add("治愈");
        types.add("死亡");
        // 需要输出的省份名
        List<String> outProvinces = argParser.getVals("-province");
        try {
            FileOutputStream fos = new FileOutputStream(logPath);
            // 键为省份信息,第一个为全国,其他为按照拼音顺序
            Set<String> provinces = info.keySet();
            for (String province:provinces) {
                Integer[] populations = info.get(province);
                StringBuilder line = new StringBuilder(province);

                // 如果要输出的省份中不包含就跳过
                // 这里有错
                if (outProvinces!=null && !outProvinces.contains(province)) { continue; }

                if (populations[0]!=0 || populations[1]!=0 || populations[2]!=0 || populations[3]!=0) {
                    for (Integer index:indexTypes) {
                        line.append(" ").append(types.get(index)).append(populations[index]).append("人");
                    }
                    line.append("\n");
                    // line = String.format("%s 感染患者%d人 疑似患者%d人 治愈%d人 死亡%d人\n",
                    // province,populations[0],populations[1],populations[2],populations[3]);
                    // 写入信息
                    fos.write(line.toString().getBytes());
                }

            }
            String endLine = "// 该文档并非真实数据，仅供测试使用\n";
            fos.write(endLine.getBytes());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}


