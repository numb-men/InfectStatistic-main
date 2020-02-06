import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * InfectStatistic
 * TODO
 *程序执行过程中需要的一些静态方法
 *
 * @author herokilito@outlook.com
 * @version 0.4
 * @since 2020.02
 */
public class Lib {
    /**
     * 初始化统计数据
     * @param statistics 统计数据
     */
    public static void mapInit(Map<String,ArrayList<Integer>> statistics){
        ArrayList<Integer> originData = new ArrayList<>();
        originData.add(0); //感染人数
        originData.add(0); //疑似人数
        originData.add(0); //治愈人数
        originData.add(0); //死亡人数
        statistics.put("全国",new ArrayList<>(originData));
        statistics.put("安徽",new ArrayList<>(originData));
        statistics.put("澳门",new ArrayList<>(originData));
        statistics.put("北京",new ArrayList<>(originData));
        statistics.put("重庆",new ArrayList<>(originData));
        statistics.put("福建",new ArrayList<>(originData));
        statistics.put("甘肃",new ArrayList<>(originData));
        statistics.put("广东",new ArrayList<>(originData));
        statistics.put("广西",new ArrayList<>(originData));
        statistics.put("贵州",new ArrayList<>(originData));
        statistics.put("河北",new ArrayList<>(originData));
        statistics.put("海南",new ArrayList<>(originData));
        statistics.put("河南",new ArrayList<>(originData));
        statistics.put("黑龙江",new ArrayList<>(originData));
        statistics.put("湖北",new ArrayList<>(originData));
        statistics.put("湖南",new ArrayList<>(originData));
        statistics.put("吉林",new ArrayList<>(originData));
        statistics.put("江苏",new ArrayList<>(originData));
        statistics.put("江西",new ArrayList<>(originData));
        statistics.put("辽宁",new ArrayList<>(originData));
        statistics.put("内蒙古",new ArrayList<>(originData));
        statistics.put("宁夏",new ArrayList<>(originData));
        statistics.put("青海",new ArrayList<>(originData));
        statistics.put("山东",new ArrayList<>(originData));
        statistics.put("山西",new ArrayList<>(originData));
        statistics.put("陕西",new ArrayList<>(originData));
        statistics.put("上海",new ArrayList<>(originData));
        statistics.put("四川",new ArrayList<>(originData));
        statistics.put("台湾",new ArrayList<>(originData));
        statistics.put("天津",new ArrayList<>(originData));
        statistics.put("香港",new ArrayList<>(originData));
        statistics.put("西藏",new ArrayList<>(originData));
        statistics.put("新疆",new ArrayList<>(originData));
        statistics.put("云南",new ArrayList<>(originData));
        statistics.put("浙江",new ArrayList<>(originData));
    }

    /**
     *帮助文档，当执行 “java 程序名” 或者 ”java 程序名 list“ 时显示的内容
     */
    public static void help(){   //help message in here
        System.out.println("");
    }

    /**
     * 解析日志文档里的数据，比如“10人”解析成整型数字”10“
     * @param dataNumber 需要解析的数据
     * @return 解析结果
     */
    public static int parseData(String dataNumber){
        try{
            return Integer.parseInt(dataNumber.substring(0,dataNumber.length() - 1));
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.exit(1);
            return 0;
        }
    }
}
