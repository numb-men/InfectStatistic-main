import org.junit.Test;

/**
 * Project Name:InfectStatistic-main2
 * File Name:LogHandleTest.java
 * Package Name:
 * Date:下午9:34:33
 * Copyright (c) 2020, Doris All Rights Reserved.
 *
*/

/**
 * Description: <br/>
 * Date: 下午9:34:33 <br/>
 * 
 * @author Doris
 * @version
 * @see
 */
public class LogHandleTest {
    @Test
    public void testCalculate() {
        LogHandle lh = new LogHandle();
        String rows = "福建 新增 感染患者 5人\\n" + "湖北 新增 感染患者 10人\\n" + "湖北 新增 疑似患者 10人\\n" + "广东 新增 疑似患者 3人\\n"
                + "福建 新增 感染患者 5人\\n" + "湖北 感染患者 流入 福建 10人\\n" + "湖北 疑似患者 流入 福建 1人\\n" + "福建 死亡 5人\\n" + "浙江 死亡 5人\\n"
                + "福建 治愈 1人\\n" + "福建 疑似患者 确认感染 1人\\n" + "山东 疑似患者 确认感染 1人\\n" + "福建 排除疑似患者 5人" + "云南 排除疑似患者 5人";
        lh.calculate(rows);
        lh.ShowHm();

    }
}
