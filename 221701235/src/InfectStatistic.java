/**
 * InfectStatistic
 * TODO
 *
 * @author DDDDy
 * @version 1.0
 * @since 2020-02-16
 */
import java.io.File;

public class InfectStatistic {
    public static void main(String[] args) {
        //记录参数出现的次数
        int logCount = 0;
        int outCount = 0;
        int dateCount = 0;
        int typeCount = 0;
        int provinceCount = 0;
        //记录读写路径
        String logPath = "";
        String outPath = "";
        //记录输入的日期
        String dateInput = "";
        //所有类型
        String  allType [] = {"ip","sp","cure","dead"};
        //类型出现次数是否大于0，是就输出相关，若无大于0就全部输出。
        int typeAppear [] = {0,0,0,0};
        //可能出现的所有省(包括全国) 31+1
        String allProvince [] = {"全国","安徽","北京","重庆","福建","甘肃","广东","广西",
            "贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁",
            "内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏","新疆",
            "云南","浙江"};
        //指定省份出现标记数组，大小是全国1+31个可能出现的省份
        int provinceAppear [] = new int[32];
        for (int i=0; i < 32; i++) {
            //初始化
            provinceAppear[i] = 0;
        }
        //结合可能出现的所有省 和 所有感染情况类型 可以建立一个二维数组来统计人数
        int totalTable [][] = new int[32][4];
        for (int i = 0; i < totalTable.length; i++) {
            for (int j = 0; j < totalTable[i].length; j++) {
                //初始化
                totalTable[i][j] = 0;
            }
        }
        //处理过程中省份出现标记数组，大小也是32
        int provinceReade [] = new int[32];
        for (int i=0; i < 32; i++) {
            //初始化
            provinceReade[i] = 0;
        }
        
        //合法性检验待定，处理参数
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-log")) {
                logCount++;
                int j = i+1;
                logPath = args[j];
            }
            if (args[i].equals("-out")) {
                outCount++;
                int j = i+1;
                outPath = args[j];
            }
            if (args[i].equals("-date")) {
                dateCount++;
                int j = i+1;
                dateInput = args[j];
            }
            if (args[i].equals("-type")) {
                typeCount++;
                int j = i+1;
                //从"-type"下一处开始往后寻找参数
                while (true) {
                    if (j == args.length || args[j].charAt(0) == '-') {
                        break;
                    }
                    if (args[j].equals("ip")) {
                        typeAppear[0] = 1;
                    }
                    if (args[j].equals("sp")) {
                        typeAppear[1] = 1;
                    }
                    if (args[j].equals("cure")) {
                        typeAppear[2] = 1;
                    }
                    if (args[j].equals("dead")) {
                        typeAppear[3] = 1;
                    }
                    j++;
                }
            }
            if (args[i].equals("-province")) {
                provinceCount++;
                int j = i+1;
                //从"-province"下一处开始往后寻找参数,寻找参数在省份列表中的位置
                while (true) {
                    if (j == args.length || args[j].charAt(0) == '-') {
                        break;
                    }
                    int index = getIndex(allProvince,args[j]);
                    //未找到会返回-1
                    if (index >= 0) {
                        provinceAppear[index] = 1;
                    }
                    j++;
                }
            }
        }
        //"-log" 和 "-out"都有附带的情况下才能继续进行
        if ((logCount == 1) && (outCount == 1)) {
            //获取文件名列表
            File file = new File(logPath);
            if (file.isDirectory()) {
                String names [] = file.list();
                if (dateCount == 0) {
                    //未指定日期,获取最后一个文件名，即最新日志文件
                    dateInput = names[names.length-1];
                } else {
                    dateInput = dateInput + ".log.txt";
                }
                for (String name : names) {
                    int res = dateInput.compareTo(name);
                    if (res > 0) {
                        //当前name的日期比指定日期（未指定则认为指定日志最后一个日期即最新）早
                        //读取 String filePath = logPath + "/" + name 文件
                        //处理，统计
                    } else if (res == 0) {
                        //当前name处理就可以退出了
                        //读取 String filePath = logPath + "/" + name 文件
                        //处理，统计
                        break;
                    } else {
                        //指定日期比name的日期晚
                        break;
                    }
                }
                //处理统计完毕后进行写入
                //先判断provinceCount 是否为 0，为0即未指定省份，需要列出日志中出现的所有省份以及全国
                //指定省份则输出指定的省份
                if (provinceCount == 0) {
                    //遍历provinceReade数组，找到值为1的下标
                    //向outPath写入对应的totalTable的值
                    //输出过程中同样对类型type进行分析
                    
                } else {
                    //遍历provinceAppear数组，找到值为1的下标
                    //向outPath写入对应的totalTable的值
                    //输出过程中同样对类型type进行分析
                }
            }
        }
      
    }
    public static int getIndex(String str [],String value) {
        for (int i = 0; i < str.length; i++) {
            if (str[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }
}
