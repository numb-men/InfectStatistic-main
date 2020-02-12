import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class RegexUtilTest {

    @Test
    public void RegexTest() {
        String regex = "(\\S+) 新增 感染患者 (\\d+)人";
        System.out.println(Pattern.matches(regex, "福建省 新增 感染患者 15人"));
    }

    @Test
    public void Test() {
        String logLine = "福建 新增 感染患者 3人";
        String regex = "(\\S+) 新增 (\\S+) (\\d+)人";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(logLine);

        if (m.find()) {
            System.out.println("Found value: " + m.group(0));
            System.out.println("Found value: " + m.group(1));
            System.out.println("Found value: " + m.group(2));
            System.out.println("Found value: " + m.group(3));
        }
    }

    @Test
    public void Test1() {
        String logLine = "湖北 感染患者 流入 福建 2人";
        String regex = "(\\S+) (\\S+) 流入 (\\S+) (\\d+)人";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(logLine);

        if (m.find()) {
            System.out.println("Found value: " + m.group(0));
            System.out.println("Found value: " + m.group(1));
            System.out.println("Found value: " + m.group(2));
            System.out.println("Found value: " + m.group(3));
            System.out.println("Found value: " + m.group(4));
        }
    }

    @Test
    public void Test2() {
        new Add(null).doAction("福建 新增 感染患者 3人");
        Region region = StatisticResult.get("福建");
        assertEquals(3, region.getInfected());
    }
}
