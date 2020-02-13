package reg;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegFour {
    private final String REGEX = "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";

    public RegFour() {

    }

    // 返回一个省份对应信息的映射

    public Map<String, Integer[]> process(String line) {

        Map<String,Integer[]> map = new HashMap<>();

        String[] result = line.split(" ");
//            for (String s:result) {
//                System.out.println(s);
//            }
        // 获取流出省份和流入省份
        String province1 = result[0];
        String province2 = result[3];
        // 获取流入的疑似患者
        Integer population = Integer.parseInt(result[4].substring(0,result[4].length()-1));

        // 封装成map
        Integer[] num = {0,-population,0,0};
        Integer[] num2 = {0,population,0,0};
        map.put(province1,num);
        map.put(province2,num2);

        return map;
    }
}
