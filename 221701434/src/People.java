import java.util.HashMap;
import java.util.Map;

public class People {
    public int[] province=new int[35];
    public int[] province_deadpeople=new int[35];
    public int[] province_curepeople=new int[35];
    public int[] province_doubtpeople=new int[35];
    public int[] province_infectpeople=new int[35];
    kind[] kinds=new kind[4];
    province[] provinces=new province[35];
    String[] province2={
            "全国", "安徽", "澳门" ,"北京",
            "重庆", "福建","甘肃","广东",
            "广西", "贵州", "海南", "河北",
            "河南", "黑龙江", "湖北", "湖南",
            "吉林","江苏", "江西", "辽宁",
            "内蒙古", "宁夏", "青海", "山东",
            "山西", "陕西", "上海","四川",
            "台湾", "天津", "西藏", "香港",
            "新疆", "云南", "浙江"
    };
    String[] kindss={
            "感染患者","疑似患者","治愈","死亡"
    };
    Map<String,Integer> map1=new HashMap<String, Integer>();//创建哈希表
    //Map<Integer,String> map2=new HashMap<Integer, String>();
    People() {
        for (int i = 0; i < 35; i++) {
            province[i] = 0;
            province_curepeople[i] = 0;
            province_deadpeople[i] = 0;
            province_doubtpeople[i] = 0;
            province_infectpeople[i] = 0;
            provinces[i]=new province();
            provinces[i].num=i;
            provinces[i].name=province2[i];
        }
        for(int i=0;i<4;i++){
            kinds[i]=new kind();
            kinds[i].num=i;
            kinds[i].name=kindss[i];
        }
        for(int i=0;i<35;i++){//将省份以及对应数字连接起来
            map1.put(province2[i],i);// K V
           // map2.put(province[i],province2[i]);
        }

    }
}
