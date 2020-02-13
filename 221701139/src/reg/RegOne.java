package reg;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 找到匹配第一条正则表达式规则之后,提取对应省份的信息
 */
public class RegOne {

    // <省> 新增 感染患者 n人
    private final String REGEX = "(\\S+) 新增 感染患者 (\\d+)人";
    // 返回一个省份对应信息的映射
    public Map<String, Integer[]> process(String line) {
        Map<String,Integer[]> map = new HashMap<>();
        // 如果匹配代表该行由这个正则表达式处理
        String[] result = line.split(" ");
        // 获取省份
        String province = result[0];
        // 获取人数
        Integer population = Integer.parseInt(result[3].substring(0,result[3].length()-1));

        // 封装成map
        Integer[] num = {population,0,0,0};
        map.put(province,num);
        map.put("全国",new Integer[]{population,0,0,0});
        return map;
    }
}
