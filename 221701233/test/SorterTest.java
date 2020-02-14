import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SorterTest {

    public static List<String> EXPECTED_REGION = new ArrayList<>(Arrays.asList(
            "全国 ", "安徽 ", "北京 ", "重庆 ", "福建 ", "甘肃 ", "广东 ", "广西 ", "贵州 ", "海南 ", "河北 ", "河南 ",
            "黑龙江 ", "湖北 ", "湖南 ", "吉林 ", "江苏 ", "江西 ", "辽宁 ", "内蒙古 ", "宁夏 ", "青海 ", "山东 ", "山西 ",
            "陕西 ", "上海 ", "四川 ", "天津 ", "西藏 ", "新疆 ", "云南 ", "浙江 "
    ));


    /**
     * 将有序的地区列表打乱，测试是否能重排序成功
     */
    @Test
    public void sortByRegion() {
        List<String> testData = new ArrayList<>(EXPECTED_REGION);

        Collections.shuffle(testData);

        Sorter.sortByRegion(testData);

        Assert.assertEquals(testData.size(), EXPECTED_REGION.size());

        for (int i = 0; i < EXPECTED_REGION.size(); i++) {
            assertEquals(EXPECTED_REGION.get(i), testData.get(i));
        }
    }
}
