package reg;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegSix {
    private final String REGEX = "(\\S+) 治愈 (\\d+)人";

    public RegSix() {

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
            Integer population = Integer.parseInt(result[2].substring(0,result[2].length()-1));

            // 封装成map
            Integer[] num = {-population,0,population,0};
            map.put(province,num);
        map.put("全国",new Integer[]{-population,0,population,0});
        return map;
    }
}
