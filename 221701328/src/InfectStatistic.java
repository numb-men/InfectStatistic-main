import java.util.Map;
import java.util.TreeMap;

/**
 * InfectStatistic
 * TODO
 *
 * @author herokilito@outlook.com
 * @version 0.2
 * @since 2020.02
 */
class InfectStatistic {

    /*一些常量*/
    private final String INFECTION_PATIENT="感染患者";
    private final String SUSPECTED_PATIENT="疑似患者";
    private final String CURE="治愈";
    private final String DEAD="死亡";
    private final String INCREMENT="新增";
    private final String EXCLUDE="排除";
    private final String INFLOW="流入";

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
    private String[] typeParams;
    private String[] provinceParams;

    /*用于保存各省份疫情信息*/
    private Map<String, Map<String,Integer>> statistics;

    /*构造方法*/
    public InfectStatistic(){
        hasLog=false;
        hasOut=false;
        hasDate=false;
        hasType=false;
        hasProvince=false;
        statistics=new TreeMap<>();
        Map<String,Integer> data=new TreeMap<>(); //全国数据
        data.put(INFECTION_PATIENT,0);
        data.put(SUSPECTED_PATIENT,0);
        data.put(CURE,0);
        data.put(DEAD,0);
        statistics.put("全国",data);
    }

    public static void main(String[] args) {
        InfectStatistic infectStatistic=new InfectStatistic();
        infectStatistic.execute(args);
    }

    /**
     *解析参数，保存，设定默认值，并调用相应的方法
     * @param args 传递给main方法的参数
     */
    public void execute(String[] args) {

    }

    /**
     *执行-log命令参数
     * @param logPath -log参数后面的log文件路径
     */
    private void doLog(String logPath){

    }

    /**
     *执行-out命令参数
     * @param outPath -out参数后面的输出路径
     */
    private void doOut(String outPath){

    }

    /**
     *执行-date命令参数
     * @param date -date参数后面的具体日期
     */
    private void doDate(String date){

    }

    /**
     *执行-type命令参数
     * @param types -type命令参数后面的具体参数值数组
     */
    private void doType(String types){

    }

    /**
     *执行-province命令参数
     * @param provinces -province命令参数后面的具体参数值数组
     */
    private void doProvince(String provinces){

    }
}
