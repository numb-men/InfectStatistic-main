package reg;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegEight {
    private final String REGEX = "(\\S+) 排除 疑似患者 (\\d+)人";

    public RegEight() {

    }

    // 返回一个省份对应信息的映射
    public Map<String, Integer[]> process(String line) {

        Map<String,Integer[]> map = new HashMap<>();
        String[] result = line.split(" ");
//            for (String s:result) {
//                System.out.println(s);
//            }
        // 获取省份
        String province = result[0];
        // 获取人数
        Integer population = Integer.parseInt(result[3].substring(0,result[3].length()-1));
        // 封装成map
        Integer[] num = {0,-population,0,0};
        map.put(province,num);
        map.put("全国",new Integer[]{0,-population,0,0});
        return map;
    }
}
