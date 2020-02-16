/**
 * InfectStatistic
 * TODO
 *
 * @author DDDDy
 * @version 1.0
 * @since 2020-02-16
 */


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
        //省份出现标记数组
        int provinceAppear [] = new int[32];
        for (int i=0; i < 32; i++) {
            //初始化
            provinceAppear[i] = 0;
        }
        
        //合法性检验待定
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
        System.out.println("命令解析完毕，测试输出如下：");
        //合法性检验待定
        System.out.println("读取路径：" + logPath);
        System.out.println("输出路径：" + outPath);
        if (dateCount > 0) {
            System.out.println("指定日期：" + dateInput);
        }
        if (typeCount > 0) {
            System.out.println("指定类型：");
            for (int i = 0; i < typeAppear.length; i++) {
                if (typeAppear[i] == 1) {
                    //此处先输出缩写
                    System.out.print(allType[i] + " ");
                }
            }
            System.out.println();
        }
        if (provinceCount > 0) {
            System.out.println("指定省份：");
            for (int i = 0; i < provinceAppear.length; i++) {
                if (provinceAppear[i] == 1) {
                    System.out.print(allProvince[i] + " ");
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
