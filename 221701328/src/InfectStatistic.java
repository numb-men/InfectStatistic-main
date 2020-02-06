import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * InfectStatistic
 * TODO
 *
 * @author herokilito@outlook.com
 * @version 0.5
 * @since 2020.02
 */
class InfectStatistic {

    /*一些常量*/
    private final String INFECTION_PATIENT = "感染患者";
    private final String SUSPECTED_PATIENT = "疑似患者";
    private final String DIAGNOSE = "确诊感染";
    private final String CURE = "治愈";
    private final String DEAD = "死亡";
    private final String INCREMENT = "新增";
    private final String EXCLUDE = "排除";
    private final String INFLOW = "流入";

    /*以下成员变量用于判断是否传入了相应的参数*/
    private boolean hasLog;
    private boolean hasOut;
    private boolean hasDate;
    private boolean hasType;
    private boolean hasProvince;

    /*以下成员变量用于保存具体参数的参数值*/
    private String logParam;
    private String outParam;
    private String dateParam;
    private ArrayList<String> typeParams;
    private ArrayList<String> provinceParams;

    /*用于保存各省份疫情信息*/
    private Map<String, ArrayList<Integer>> statistics;

    /*日志目录*/
    private File logDirectory;

    /*构造方法*/
    public InfectStatistic(){
        hasLog = false;
        hasOut = false;
        hasDate = false;
        hasType = false;
        hasProvince = false;
        typeParams = new ArrayList<>();
        provinceParams = new ArrayList<>();
        statistics = new LinkedHashMap<>();
        Lib.mapInit(statistics);
    }

    public static void main(String[] args) {
        InfectStatistic infectStatistic = new InfectStatistic();
        infectStatistic.execute(args);
    }

    /**
     *解析参数，保存，设定默认值，并调用相应的方法
     * @param args 传递给main方法的参数
     */
    public void execute(String[] args) {
        if(args.length == 0){
            Lib.help();    //显示提示信息
            System.exit(0);
        }
        /*分离参数*/
        switch (args[0]){
            case "list":
                int i = 1;
                while(i < args.length){
                    switch (args[i]){
                        case "-log":
                            hasLog = true;
                            if(++i >= args.length){
                                System.out.println("-log参数缺少参数值");
                                System.exit(1);
                            }
                            logParam = args[i++];
                            break;
                        case "-out":
                            hasOut = true;
                            if(++i >= args.length){
                                System.out.println("-out参数缺少参数值");
                                System.exit(1);
                            }
                            outParam = args[i++];
                            break;
                        case "-date":
                            hasDate = true;
                            if(++i >= args.length){
                                System.out.println("-date参数缺少参数值");
                                System.exit(1);
                            }
                            dateParam = args[i++];
                            break;
                        case  "-type":
                            hasType = true;
                            while(++i < args.length && !args[i].equals("-log") && !args[i].equals("-out") && !args[i].equals("-date")
                                    && !args[i].equals("-province")){
                                typeParams.add(args[i]);
                            }
                            break;
                        case  "-province":
                            hasProvince = true;
                            while(++i < args.length && !args[i].equals("-log") && !args[i].equals("-out") && !args[i].equals("-date")
                                    && !args[i].equals("-type")){
                                provinceParams.add(args[i]);
                            }
                            break;
                        default:
                            System.out.println("\"" + args[i] + "\"无法解析的参数");
                            System.exit(1);
                            break;
                    }
                }
                break;
            //case something:此处可扩展其他命令
            default:
                System.out.println(args[0] + " 无效的命令");
                System.exit(1);
        }
        /*执行相应的方法*/
        if(!hasLog){  //log必须有
            System.out.println("缺少-log参数");
            System.exit(1);
        }
        if(!hasOut){  //out必须有
            System.out.println("缺少-out参数");
            System.exit(1);
        }
        if(!hasDate){  //如果没有data参数
            dateParam=new SimpleDateFormat("yyyy-MM-dd").format(new Date()); //当前日期
        }
        doLog(logParam);    //读取日志路径
        doDate(dateParam);   //读取日志路径下相应日期的日志
        if(hasType){
            doType(typeParams);   //需要输出的信息类型
        }
        if(hasProvince){
            doProvince(provinceParams);   //需要输出的省份疫情信息
        }
        doOut(outParam);  //输出到指定的路径
    }

    /**
     *执行-log命令参数
     * @param logPath -log参数后面的log文件路径
     */
    private void doLog(String logPath){
        logDirectory = new File(logPath);  //读取路径
        if(!logDirectory.exists()){
            System.out.println("\"-log\" " + logDirectory + " 无法解析的路径");
            System.exit(1);
        }
    }

    /**
     *执行-out命令参数
     * @param outPath -out参数后面的输出路径
     */
    private void doOut(String outPath){
        File outFile = new File(outPath);
        FileWriter writer = null;    //字符输出流
        try {
            writer = new FileWriter(outFile);
            for(String province : statistics.keySet()){   //遍历统计数据
                ArrayList<Integer> data = statistics.get(province);
                writer.write(province + "    ");
                writer.write(INFECTION_PATIENT + data.get(0) + "人    ");
                writer.write(SUSPECTED_PATIENT + data.get(1) + "人    ");
                writer.write(CURE + data.get(2) + "人    ");
                writer.write(DEAD + data.get(3) + "人    ");
                writer.write("\n");
            }
            writer.flush();
        }catch (Exception e){
            System.out.println("\"out\" " + e.getMessage());   //出现错误提示并退出程序
            System.exit(1);
        }finally {
            try {
                assert writer != null;
                writer.close();   //关闭流
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     *执行-date命令参数
     * @param date -date参数后面的具体日期
     */
    private void doDate(String date){
        File[] logList = logDirectory.listFiles();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date paramDate;
        BufferedReader reader = null;
        try {
            paramDate = dateFormat.parse(date);
            List<Integer> nationalData = statistics.get("全国"); //全国数据
            for (File log : logList) {
                Date logDate = dateFormat.parse(log.getName().substring(0, log.getName().indexOf('.')));
                if(logDate.compareTo(paramDate) <= 0){  //判断日志文件的日期是否小于等于给定日期
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(log), StandardCharsets.UTF_8));
                    String dataRow;
                    while((dataRow = reader.readLine()) != null){
                        if(!dataRow.startsWith("//")) { //忽略注释行
                            String[] data = dataRow.split(" ");
                            List<Integer> provinceData = statistics.get(data[0]);
                            List<Integer> destProvince;
                            switch (data[1]) {
                                case INCREMENT:  //处理新增
                                    if (data[2].equals(INFECTION_PATIENT)) {  //新增感染
                                        increaseInf(nationalData, provinceData, Lib.parseData(data[3]));
                                    } else {                                  //新增疑似
                                        increaseSus(nationalData, provinceData, Lib.parseData(data[3]));
                                    }
                                    break;
                                case EXCLUDE:
                                    excludeSus(nationalData, provinceData, Lib.parseData(data[3]));
                                    break;
                                case CURE:
                                    cure(nationalData,provinceData,Lib.parseData(data[2]));
                                    break;
                                case DEAD:
                                    dead(nationalData,provinceData,Lib.parseData(data[2]));
                                    break;
                                case INFECTION_PATIENT:
                                    destProvince = statistics.get(data[3]);
                                    infInflow(provinceData,destProvince,Lib.parseData(data[4]));
                                    break;
                                case SUSPECTED_PATIENT:
                                    if(data[2].equals(INFLOW)){
                                        destProvince = statistics.get(data[3]);
                                        susInflow(provinceData,destProvince,Lib.parseData(data[4]));
                                    } else if(data[2].equals(DIAGNOSE)) {
                                        diagnose(nationalData,provinceData,Lib.parseData(data[3]));
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.exit(1);
        }finally {
            try{
                if (reader != null) {
                    reader.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     *执行-type命令参数
     * @param types -type命令参数后面的具体参数值数组
     */
    private void doType(ArrayList<String> types){
        System.out.print("types:");
        for(String s : types){
            System.out.print(s + " ");
        }
        System.out.println("");
    }

    /**
     *执行-province命令参数
     * @param provinces -province命令参数后面的具体参数值数组
     */
    private void doProvince(ArrayList<String> provinces){
        System.out.print("provinces:");
        for(String s : provinces){
            System.out.print(s + " ");
        }
        System.out.println("");
    }

    /**
     *新增确诊患者的计算
     * @param nationalData 全国疫情
     * @param provinceData 当前省份疫情
     * @param count 新增数量
     */
    private void increaseInf(List<Integer> nationalData,List<Integer> provinceData,int count){
        int provinceInf = provinceData.get(0);
        int nationalInf = nationalData.get(0);
        provinceInf += count;
        nationalInf += count;
        provinceData.set(0,provinceInf);
        nationalData.set(0,nationalInf);
    }

    /**
     *新增疑似患者的计算
     * @param nationalData 全国疫情
     * @param provinceData 当前省份疫情
     * @param count 新增人数
     */
    private void increaseSus(List<Integer> nationalData,List<Integer> provinceData,int count){
        int provinceSus = provinceData.get(1);
        int nationalSus = nationalData.get(1);
        provinceSus += count;
        nationalSus += count;
        provinceData.set(1,provinceSus);
        nationalData.set(1,nationalSus);
    }

    /**
     *排除疑似患者的计算
     * @param nationalData 全国疫情
     * @param provinceData 当前省份疫情
     * @param count 排除人数
     */
    private void excludeSus(List<Integer> nationalData,List<Integer> provinceData,int count){
        int provinceSus = provinceData.get(1);
        int nationalSus = nationalData.get(1);
        provinceSus -= count;
        nationalSus -= count;
        provinceData.set(1,provinceSus);
        nationalData.set(1,nationalSus);
    }

    /**
     *新增治愈病例的计算
     * @param nationalData 全国疫情
     * @param provinceData 当前省份疫情
     * @param count 治愈人数
     */
    private void cure(List<Integer> nationalData,List<Integer> provinceData,int count){
        int provinceCure = provinceData.get(2);
        int nationalCure = nationalData.get(2);
        int provinceInf = provinceData.get(0);
        int nationalInf = nationalData.get(0);
        provinceCure += count;
        nationalCure += count;
        provinceInf -= count;
        nationalInf -= count;
        provinceData.set(2,provinceCure);
        nationalData.set(2,nationalCure);
        provinceData.set(0,provinceInf);
        nationalData.set(0,nationalInf);
    }

    /**
     *新增死亡病例的计算
     * @param nationalData 全国疫情
     * @param provinceData 当前省份疫情
     * @param count 死亡人数
     */
    private void dead(List<Integer> nationalData,List<Integer> provinceData,int count){
        int provinceDead = provinceData.get(3);
        int nationalDead = nationalData.get(3);
        int provinceInf = provinceData.get(0);
        int nationalInf = nationalData.get(0);
        provinceDead += count;
        nationalDead += count;
        provinceInf -= count;
        nationalInf -= count;
        provinceData.set(3,provinceDead);
        nationalData.set(3,nationalDead);
        provinceData.set(0,provinceInf);
        nationalData.set(0,nationalInf);
    }

    /**
     * 流入感染患者的计算
     * @param sourceProvince 感染者从那个省（市、自治区）流出
     * @param destProvince 感染者流入哪个省（市、自治区）
     * @param count 人数
     */
    private void infInflow(List<Integer> sourceProvince,List<Integer> destProvince,int count){
        int sourceInf = sourceProvince.get(0);
        int destInf = destProvince.get(0);
        sourceInf -= count;
        destInf += count;
        sourceProvince.set(0,sourceInf);
        destProvince.set(0,destInf);
    }

    /**
     * 流入疑似患者的计算
     * @param sourceProvince 感染者从那个省（市、自治区）流出
     * @param destProvince 感染者流入哪个省（市、自治区）
     * @param count 人数
     */
    private void susInflow(List<Integer> sourceProvince,List<Integer> destProvince,int count){
        int sourceSus = sourceProvince.get(1);
        int destSus = destProvince.get(1);
        sourceSus -= count;
        destSus += count;
        sourceProvince.set(1,sourceSus);
        destProvince.set(1,destSus);
    }

    /**
     *确诊病例的计算
     * @param nationalData 全国疫情
     * @param provinceData 当前省份疫情
     * @param count 确诊人数
     */
    private void diagnose(List<Integer> nationalData,List<Integer> provinceData,int count){
        int provinceInf = provinceData.get(0);
        int provinceSus = provinceData.get(1);
        int nationalInf = nationalData.get(0);
        int nationalSus = nationalData.get(1);
        provinceInf += count;
        provinceSus -= count;
        nationalInf += count;
        nationalSus -= count;
        provinceData.set(0,provinceInf);
        provinceData.set(1,provinceSus);
        nationalData.set(0,nationalInf);
        nationalData.set(1,nationalSus);
    }
}
