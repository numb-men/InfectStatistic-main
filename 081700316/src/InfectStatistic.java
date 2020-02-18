import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InfectStatistic {

    /**
     * 各省份状态
     * 默认0：没有状况
     * 1：有状况
     */
    private static int[] province_status = new int[32];

    /**
     * 省份排序（包括“全国”）
     */
    private static String[] province_str = {"全国", "安徽", "北京", "重庆", "福建", "甘肃",
            "广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南",
            "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西",
            "陕西", "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江"};

    /**
     * 类型输出状态
     * 0：不需要输出
     * 默认为1：需要输出
     */
    private static int[] type_num = new int[]{1, 1, 1, 1};

    /**
     * 类型列表
     */
    private static String[] type_str = {"感染患者", "疑似患者", "治愈", "死亡"};

    /**
     * 指定人数
     * 一级：省份
     * 二级：患者类型
     */
    private static int[][] people_num = new int[32][4];

    /**
     * 文本中存在的所有情况
     */
    private static String[] situation_str = {"(\\S+) 新增 感染患者 (\\d+)人", "(\\S+) 新增 疑似患者 (\\d+)人",
            "(\\S+) 感染患者 流入 (\\S+) (\\d+)人", "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人",
            "(\\S+) 死亡 (\\d+)人", "(\\S+) 治愈 (\\d+)人",
            "(\\S+) 疑似患者 确诊感染 (\\d+)人", "(\\S+) 排除 疑似患者 (\\d+)人"};

    /**
     * 文本输入输出路径
     */
    private static String in_path;
    private static String out_path;

    /**
     * date用于记录输出的文本日期
     */
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static LocalDate today = LocalDate.now();
    private static String out_date = formatter.format(today);

    /**
     * 文件输入输出
     */
    static class ioFile {
        /**
         * 获取文件
         */
        void getFile() throws Throwable {
            File file = new File(in_path);
            File[] file_list = file.listFiles();
            String fileName;
            assert file_list != null;
            for (File value : file_list) {
                fileName = value.getName();
                if (fileName.compareTo(out_date) <= 0) {
                    readFile(in_path + fileName);
                }
            }
        }

        /**
         * 读文件
         */
        void readFile(String filePath) throws Throwable {
            try {
                Throwable var1;
                try {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), StandardCharsets.UTF_8))) {
                        String line;
                        while ((line = in.readLine()) != null) {
                            LogLine.FileProcessing(line);
                        }
                    }
                } catch (Throwable var11) {
                    var1 = var11;
                    throw var1;
                }
            } catch (Exception var12) {
                var12.printStackTrace();
            }

        }

        /**
         * 写文件
         */
        void writeFile() throws Throwable {
            try {
                Throwable var0;
                try {
                    try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(out_path)), StandardCharsets.UTF_8))) {
                        province_status[0] = 1;
                        for (int i = 0; i < province_str.length; i++) {
                            if (province_status[i] == 1) {
                                bw.write(province_str[i] + " ");
                                for (int j = 0; j < type_num.length; j++) {
                                    if (type_num[j] == 1) {
                                        bw.write(type_str[j] + people_num[i][j] + "人 ");
                                    }
                                }
                                bw.newLine();
                            }
                        }
                        bw.write("// 该文档并非真实数据，仅供测试使用");
                    }
                } catch (Throwable var10) {
                    var0 = var10;
                    throw var0;
                }
            } catch (Exception var11) {
                var11.printStackTrace();
            }

        }
    }


    /**
     * 不同类型日志行处理
     */
    static class LogLine {
        /**
         * 文本内容处理
         */
        private static void FileProcessing(String string) {
            int num = -1;
            for (int i = 0; i < situation_str.length; i++) {
                boolean isMatch = Pattern.matches(situation_str[i], string);
                if (isMatch)
                    num = i;
            }
            switch (num) {
                case 0:
                    addInfected(string);
                    break;
                case 1:
                    addSuspected(string);
                    break;
                case 2:
                    flowInfected(string);
                    break;
                case 3:
                    flowSuspected(string);
                    break;
                case 4:
                    addDead(string);
                    break;
                case 5:
                    addCure(string);
                    break;
                case 6:
                    diagnosisSuspected(string);
                    break;
                case 7:
                    removeSuspected(string);
                    break;
                default:
                    System.out.println("日志内容错误！");
            }
        }

        /**
         * 提取行里人数
         */
        static String[] getNum(String string){
            Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+|\\d+");
            Matcher m = p.matcher(string);
            String[] arr = new String[6];
            int cnt = 0;
            while (m.find()) {
                arr[cnt] = m.group();
                cnt++;
            }
            return arr;
        }

        /**
         * 增加数量
         */
        static void addNum(String str, int change_num, int status) {
            for (int i = 0; i < province_str.length; i++) {
                if (str.equals(province_str[i])) {
                    people_num[0][status] += change_num;
                    people_num[i][status] += change_num;
                    province_status[i] = 1;
                    break;
                }
            }
        }

        /**
         * 减少数量
         */
        static void diagnosisNum(String str, int change_num, int status) {
            for (int i = 0; i < province_str.length; i++) {
                if (str.equals(province_str[i])) {
                    people_num[0][status] -= change_num;
                    people_num[i][status] -= change_num;
                    province_status[i] = 1;
                    break;
                }
            }
        }

        /**
         * 新增 感染患者
         */
        static void addInfected(String string) {
            String[] arr = getNum(string);
            int change_num = Integer.parseInt(arr[arr.length-3]);
            addNum(arr[0], change_num, 0);
        }


        /**
         * 新增 疑似患者
         */
        static void addSuspected(String string) {
            String[] arr = getNum(string);
            int change_num = Integer.parseInt(arr[arr.length-3]);
            addNum(arr[0], change_num, 1);
        }

        /**
         * 感染患者 流入
         */
        static void flowInfected(String string) {
            String[] arr = getNum(string);
            int change_num = Integer.parseInt(arr[arr.length-2]);
            addNum(arr[3], change_num, 0);
            diagnosisNum(arr[0], change_num, 0);
        }

        /**
         * 疑似患者 流入
         */
        static void flowSuspected(String string) {
            String[] arr = getNum(string);
            int change_num = Integer.parseInt(arr[arr.length-2]);
            addNum(arr[3], change_num, 1);
            diagnosisNum(arr[0], change_num, 1);
        }

        /**
         * 死亡
         */
        static void addDead(String string) {
            String[] arr = getNum(string);
            int change_num = Integer.parseInt(arr[arr.length-4]);
            addNum(arr[0], change_num, 3);
            diagnosisNum(arr[0], change_num, 0);
        }

        /**
         * 治愈
         */
        static void addCure(String string) {
            String[] arr = getNum(string);
            int change_num = Integer.parseInt(arr[arr.length-4]);
            addNum(arr[0], change_num, 2);
            diagnosisNum(arr[0], change_num, 0);
        }

        /**
         * 疑似患者 确诊感染
         */
        static void diagnosisSuspected(String string) {
            String[] arr = getNum(string);
            int change_num = Integer.parseInt(arr[arr.length-3]);
            addNum(arr[0], change_num, 0);
            diagnosisNum(arr[0], change_num, 1);
        }

        /**
         * 排除 疑似患者
         */
        static void removeSuspected(String string) {
            String[] arr = getNum(string);
            int change_num = Integer.parseInt(arr[arr.length-3]);
            diagnosisNum(arr[0], change_num, 1);
        }
    }

    /**
     * 解析命令行参数
     */
    static class CmdArgs{
        String[] args;

        CmdArgs(String[] args_str){
            args = args_str;
        }

        boolean extractCmd() {
            if(!args[0].equals("list")) {
                System.out.println("错误命令：开头非list");
                return false;
            }
            for(int i = 1; i < args.length; i++) {
                int m;
                switch (args[i]) {
                    case "-log":
                        m = getInPath(i+1);
                        if (m == -1) {
                            System.out.println("错误命令：输入路径错误");
                            return false;
                        }
                        i = m;
                        break;
                    case "-out":
                        m = getOutPath(i+1);
                        if (m == -1) {
                            System.out.println("错误命令：输出路径错误");
                            return false;

                        }
                        i = m;
                        break;
                    case "-date":
                        m = getDate(i+1);
                        if (m == -1) {
                            System.out.println("错误命令：日期错误");
                            return false;
                        }
                        i = m;
                        break;
                    case "-type":
                        m = getType(i+1);
                        if (m == -1) {
                            System.out.println("错误命令：类型错误");
                            return false;
                        }
                        i = m;
                        break;
//                    case "-province":
//                        m = getProvince(i+1);
//                        if (m == -1) {
//                            System.out.println("错误命令：省份错误");
//                            return false;
//                        }
//                        i = m;
//                        break;
                    default:
                        System.out.println("错误命令！");
                        return false;
                }
            }
            return true;
        }

        /**
         * 获取输入位置
         */
        int getInPath(int i) {
            if(args[i].matches("^[A-z]:\\\\(.+?\\\\)*$"))
                in_path = args[i];
            else
                return -1;
            return i;
        }

        /**
         * 获取输出位置
         */
        int getOutPath(int i) {
            if(args[i].matches("^[A-z]:\\\\(\\S+)+(\\.txt)$"))
                out_path = args[i];
            else
                return -1;
            return i;
        }

        /**
         * 获取日期
         */
        int getDate(int i) {
            if(isValidDate(args[i])) {
                if(out_date.compareTo(args[i]) >= 0)
                    out_date = args[i] + ".log.txt";
                else
                    return -1;
            } else
                return -1;
            return i;
        }

        /**
         * 判断日期格式是否正确
         */
        boolean isValidDate(String dateStr) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                LocalDate date = LocalDate.parse(dateStr, formatter);
                if(!date.isLeapYear())
                    return false;
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        /**
         * 获取类型
         */
        int getType(int i) {
            int m = i;
            for(int j = 0; j < 4; j++)
                type_num[j] = 0;
            label:
            while(i<args.length) {
                switch (args[i]) {
                    case "ip":
                        type_num[0] = 1;
                        i++;
                        break;
                    case "sp":
                        type_num[1] = 1;
                        i++;
                        break;
                    case "cure":
                        type_num[2] = 1;
                        i++;
                        break;
                    case "dead":
                        type_num[3] = 1;
                        i++;
                        break;
                    default:
                        break label;
                }
            }
            if(m == i)
                return -1;
            return (i - 1);
        }

//        /**
//         * 获取省份
//         */
//        int getProvince(int i) {
//            //int m = i;
//                //while (i < args.length) {
//                    for (int j = 0; j < province_str.length; j++) {
//                        if (args[i].equals(province_str[j])) {
//                            province_status[j] = 0;
//                            //i++;
//                            break;
//                        }
//                    }
//                //}
//            //if (m == i)
//            //    return -1;
//            return i;
//        }
    }


    public static void main(String[] args) throws Throwable {
        if (args.length == 0) {
            System.out.println("未输入参数!");
            return;
        }
        InfectStatistic infect = new InfectStatistic();
        InfectStatistic.CmdArgs cmd_args = new CmdArgs(args);
        boolean b = cmd_args.extractCmd();
        if(!b) {
            return;
        }
        InfectStatistic.ioFile file_stream = new ioFile();
        file_stream.getFile();
        file_stream.writeFile();
    }
}
