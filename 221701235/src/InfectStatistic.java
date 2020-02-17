/**
 * InfectStatistic
 * TODO
 *
 * @author DDDDy
 * @version 1.0
 * @since 2020-02-16
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

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
                String nameOfFirst = names[0];
                String nameOfLast = names[names.length-1];
                if (dateCount == 0) {
                    //未指定日期,获取最后一个文件名，即最新日志文件
                    dateInput = nameOfLast;
                } else {
                    dateInput = dateInput + ".log.txt";
                }
                //首先与最早日志日期比较
                int firstRes = dateInput.compareTo(nameOfFirst);
                if (firstRes < 0) {
                    System.out.print("指定日期"+dateInput+"未出现疫情");
                    System.exit(0);
                }
                //其次与最晚日志日期比较
                int lastRes = dateInput.compareTo(nameOfLast);
                if (lastRes > 0) {
                    System.out.print("日期超出范围");
                    System.exit(0);
                }
                for (String name : names) {
                    int res = dateInput.compareTo(name);
                    if (res < 0) {
                        //指定日期比日志文件的日期早
                        break;
                    } else {
                        //当前日志文件的日期比指定日期（未指定则认为指定日志最后一个日期即最新）早或同一天
                        //读取 String filePath = logPath + "/" + name 文件
                        String filePath = logPath + "/" +name;
                        //处理，统计
                        try {
                            FileInputStream fileInputStream = new FileInputStream(filePath);
                            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream,"UTF-8");
                            BufferedReader buffReader = new BufferedReader(inputStreamReader);
                            String line = null;
                            
                            //开始逐行读取文件
                            while ((line = buffReader.readLine()) != null) {
                                /*
                                 * 对各个类型：
                                 * 类型1，注释，continue操作
                                 * 类型2，某地新增感染患者n人，读取该行文本，用split方法将其存入字符串数组
                                 *        直接取省份字符串，用getIndex方法获取省份位置 k，然后置provinceReade数组
                                 *        相应位置为1，获取人数后，令totalTable[k][0]和totalTable[0][0](全国)的值为相应人数
                                 * 类型3-9与类型2大同小异，获取省份、人数，进行相关计算。
                                 * */
                                String textType1 = "//.*";
                                String textType2 = "(\\S+) 新增 感染患者 (\\d+)人";
                                String textType3 = "(\\S+) 新增 疑似患者 (\\d+)人";
                                String textType4 = "(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
                                String textType5 = "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
                                String textType6 = "(\\S+) 死亡 (\\d+)人";
                                String textType7 = "(\\S+) 治愈 (\\d+)人";
                                String textType8 = "(\\S+) 疑似患者 确诊感染 (\\d+)人";
                                String textType9 = "(\\S+) 排除 疑似患者 (\\d+)人";
                                
                                //匹配
                                boolean isMatch1 = Pattern.matches(textType1, line);
                                boolean isMatch2 = Pattern.matches(textType2, line);
                                boolean isMatch3 = Pattern.matches(textType3, line);
                                boolean isMatch4 = Pattern.matches(textType4, line);
                                boolean isMatch5 = Pattern.matches(textType5, line);
                                boolean isMatch6 = Pattern.matches(textType6, line);
                                boolean isMatch7 = Pattern.matches(textType7, line);
                                boolean isMatch8 = Pattern.matches(textType8, line);
                                boolean isMatch9 = Pattern.matches(textType9, line);
                                
                                //开始处理
                                String lineSplit[] = line.split(" ");
                                if (isMatch1) {
                                    //是注释类型
                                    continue;
                                }
                                
                                if (isMatch2) {
                                    //<省> 新增 感染患者 n人,ip
                                    String s = getProvinceOne(lineSplit);
                                    int k = getIndex(allProvince,s);
                                    //表示该省在日志文件中出现过
                                    provinceReade[k] = 1;
                                    int numOfChange = getNumOfPeople(lineSplit);
                                    
                                    //记录
                                    totalTable[k][0] += numOfChange;
                                    totalTable[0][0] += numOfChange;
                                    
                                }
                                
                                if (isMatch3) {
                                    //<省> 新增 疑似患者 n人,sp
                                    String s = getProvinceOne(lineSplit);
                                    int k = getIndex(allProvince,s);
                                    provinceReade[k] = 1;
                                    int numOfChange = getNumOfPeople(lineSplit);
                                    
                                    //记录
                                    totalTable[k][1] += numOfChange;
                                    totalTable[0][1] += numOfChange;
                                    
                                }
                                
                                if (isMatch4) {
                                    //<省1> 感染患者 流入 <省2> n人,ip
                                    String s1 = getProvinceOne(lineSplit);
                                    String s2 = getProvinceTwo(lineSplit);
                                    int k1 = getIndex(allProvince,s1);
                                    int k2 = getIndex(allProvince,s2);
                                    provinceReade[k1] = 1;
                                    provinceReade[k2] = 1;
                                    int numOfChange = getNumOfPeople(lineSplit);
                                    
                                    //记录,省1减少,省2增加,全国不变
                                    totalTable[k1][0] -= numOfChange;
                                    totalTable[k2][0] += numOfChange;
                                }
                                
                                if (isMatch5) {
                                    //<省1> 疑似患者 流入 <省2> n人,sp
                                    String s1 = getProvinceOne(lineSplit);
                                    String s2 = getProvinceTwo(lineSplit);
                                    int k1 = getIndex(allProvince,s1);
                                    int k2 = getIndex(allProvince,s2);
                                    provinceReade[k1] = 1;
                                    provinceReade[k2] = 1;
                                    int numOfChange = getNumOfPeople(lineSplit);
                                    
                                    //记录,省1减少,省2增加,全国不变
                                    totalTable[k1][1] -= numOfChange;
                                    totalTable[k2][1] += numOfChange;
                                }
                                
                                if (isMatch6) {
                                    //<省> 死亡 n人,dead
                                    String s = getProvinceOne(lineSplit);
                                    int k = getIndex(allProvince,s);
                                    int numOfChange = getNumOfPeople(lineSplit);
                                    
                                    //记录,感染人数减少,死亡人数增加
                                    totalTable[k][3] += numOfChange;
                                    totalTable[k][0] -= numOfChange;
                                    totalTable[0][3] += numOfChange;
                                    totalTable[0][0] -= numOfChange;
                                }
                                
                                if (isMatch7) {
                                    //<省> 治愈 n人,cure
                                    String s = getProvinceOne(lineSplit);
                                    int k = getIndex(allProvince,s);
                                    int numOfChange = getNumOfPeople(lineSplit);
                                    
                                    //记录,感染人数减少,治愈人数增加
                                    totalTable[k][2] += numOfChange;
                                    totalTable[k][0] -= numOfChange;
                                    totalTable[0][2] += numOfChange;
                                    totalTable[0][0] -= numOfChange;
                                    
                                }
                                
                                if (isMatch8) {
                                    //<省> 疑似患者 确诊感染 n人
                                    String s = getProvinceOne(lineSplit);
                                    int k = getIndex(allProvince,s);
                                    int numOfChange = getNumOfPeople(lineSplit);
                                    
                                    //记录,疑似人数减少,确诊人数增加,sp-,ip+
                                    totalTable[k][1] -= numOfChange;
                                    totalTable[k][0] += numOfChange;
                                    totalTable[0][1] -= numOfChange;
                                    totalTable[0][0] += numOfChange;
                                }
                                
                                if (isMatch9) {
                                    //<省> 排除 疑似患者 n人
                                    String s = getProvinceOne(lineSplit);
                                    int k = getIndex(allProvince,s);
                                    int numOfChange = getNumOfPeople(lineSplit);
                                    
                                    //记录,疑似人数减少
                                    totalTable[k][1] -= numOfChange;
                                    totalTable[0][1] -= numOfChange;
                                }
                            }
                        } catch (Exception e) {
                            System.exit(0);
                        }
                        
                    }
                }
                for (int i = 0; i < provinceReade.length; i++) {
                    if (provinceReade[i] == 1) {
                        provinceReade[0] = 1;
                        break;
                    }
                }
                //测试处理结果，简易输出
                for (int i = 0; i < totalTable.length; i++) {
                    if (provinceReade[i] == 1) {
                        System.out.print(allProvince[i] + " : ");
                        System.out.print("ip:" + totalTable[i][0] + "人     ");
                        System.out.print("sp:" + totalTable[i][1] + "人     ");
                        System.out.print("cure:" + totalTable[i][2] + "人     ");
                        System.out.print("dead:" + totalTable[i][3] + "人     ");
                        System.out.println();
                    }
                }
                System.out.println("// 该文档并非真实数据，仅供测试使用");
                
                
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
    
    //获取字符串在字符串数组中的位置
    public static int getIndex(String str [],String value) {
        for (int i = 0; i < str.length; i++) {
            if (str[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }
    
    //获取人数
    public static int getNumOfPeople(String lineSplit []) {
        //n人
        String last = lineSplit[lineSplit.length-1];
        String num ="";
        for (int i = 0; i < last.length(); i++) {
            if (last.charAt(i) != '人') {
                num += last.charAt(i);
            }
        }
        
        return Integer.parseInt(num);
    }
    
    //获取省份1
    public static String getProvinceOne(String lineSplit []) {
        return lineSplit[0];
    }
    
    //获取省份2
    public static String getProvinceTwo(String lineSplit []) {
        return lineSplit[3];
    }
    
    
}
