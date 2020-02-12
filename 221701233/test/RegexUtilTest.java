import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class RegexUtilTest {

    @Test
    public void RegexTest() {
        String regex = "(\\S+) 新增 感染患者 (\\d+)人";
        System.out.println(Pattern.matches(regex, "福建省 新增 感染患者 15人"));
    }
}
