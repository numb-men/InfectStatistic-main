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
                + "福建 治愈 1人\\n" + "福建 疑似患者 确诊感染 1人\\n" + "山东 疑似患者 确诊感染 1人\\n" + "福建 排除 疑似患者 5人" + "云南 排除 疑似患者 5人";
        lh.calculate(rows);
        lh.ShowHm();

    }

    @Test
    public void testCount1() {
        LogHandle lh = new LogHandle();
        StringBuilder sb = new StringBuilder();
        String rows = "福建 新增 感染患者 5人\\n" + "湖北 新增 感染患者 10人\\n" + "湖北 新增 疑似患者 10人\\n" + "广东 新增 疑似患者 3人\\n"
                + "福建 新增 感染患者 5人\\n" + "湖北 感染患者 流入 福建 10人\\n" + "湖北 疑似患者 流入 福建 1人\\n" + "福建 死亡 5人\\n" + "浙江 死亡 5人\\n"
                + "福建 治愈 1人\\n" + "福建 疑似患者 确诊感染 1人\\n" + "山东 疑似患者 确诊感染 1人\\n" + "福建 排除 疑似患者 5人" + "云南 排除 疑似患者 5人";
        lh.count1("([\\u4e00-\\u9fa5]+) 新增 感染患者 (\\d+)人", rows);

        sb = lh.ShowHm();
        System.out.println(sb);
    }

    @Test
    public void testCount2() {
        LogHandle lh = new LogHandle();
        StringBuilder sb = new StringBuilder();
        String rows = "福建 新增 感染患者 5人\\n" + "湖北 新增 感染患者 10人\\n" + "湖北 新增 疑似患者 10人\\n" + "广东 新增 疑似患者 3人\\n"
                + "福建 新增 感染患者 5人\\n" + "湖北 感染患者 流入 福建 10人\\n" + "湖北 疑似患者 流入 福建 1人\\n" + "福建 死亡 5人\\n" + "浙江 死亡 5人\\n"
                + "福建 治愈 1人\\n" + "福建 疑似患者 确诊感染 1人\\n" + "山东 疑似患者 确诊感染 1人\\n" + "福建 排除 疑似患者 5人" + "云南 排除 疑似患者 5人";
        lh.count2("([\\u4e00-\\u9fa5]+) 新增 疑似患者 (\\d+)人", rows);

        sb = lh.ShowHm();
        System.out.println(sb);
    }

    @Test
    public void testCount3() {
        LogHandle lh = new LogHandle();
        StringBuilder sb = new StringBuilder();
        String rows = "福建 新增 感染患者 5人\\n" + "湖北 新增 感染患者 10人\\n" + "湖北 新增 疑似患者 10人\\n" + "广东 新增 疑似患者 3人\\n"
                + "福建 新增 感染患者 5人\\n" + "湖北 感染患者 流入 福建 10人\\n" + "湖北 疑似患者 流入 福建 1人\\n" + "福建 死亡 5人\\n" + "浙江 死亡 5人\\n"
                + "福建 治愈 1人\\n" + "福建 疑似患者 确诊感染 1人\\n" + "山东 疑似患者 确诊感染 1人\\n" + "福建 排除 疑似患者 5人" + "云南 排除 疑似患者 5人";
        lh.count3("([\\u4e00-\\u9fa5]+) 感染患者 流入 ([\\u4e00-\\u9fa5]+) (\\d+)人", rows);

        sb = lh.ShowHm();
        System.out.println(sb);
    }

    @Test
    public void testCount4() {
        LogHandle lh = new LogHandle();
        StringBuilder sb = new StringBuilder();
        String rows = "福建 新增 感染患者 5人\\n" + "湖北 新增 感染患者 10人\\n" + "湖北 新增 疑似患者 10人\\n" + "广东 新增 疑似患者 3人\\n"
                + "福建 新增 感染患者 5人\\n" + "湖北 感染患者 流入 福建 10人\\n" + "湖北 疑似患者 流入 福建 1人\\n" + "福建 死亡 5人\\n" + "浙江 死亡 5人\\n"
                + "福建 治愈 1人\\n" + "福建 疑似患者 确诊感染 1人\\n" + "山东 疑似患者 确诊感染 1人\\n" + "福建 排除 疑似患者 5人" + "云南 排除 疑似患者 5人";
        lh.count4("([\\u4e00-\\u9fa5]+) 疑似患者 流入 ([\\u4e00-\\u9fa5]+) (\\d+)人", rows);

        sb = lh.ShowHm();
        System.out.println(sb);
    }

    @Test
    public void testCount5() {
        LogHandle lh = new LogHandle();
        StringBuilder sb = new StringBuilder();
        String rows = "福建 新增 感染患者 5人\\n" + "湖北 新增 感染患者 10人\\n" + "湖北 新增 疑似患者 10人\\n" + "广东 新增 疑似患者 3人\\n"
                + "福建 新增 感染患者 5人\\n" + "湖北 感染患者 流入 福建 10人\\n" + "湖北 疑似患者 流入 福建 1人\\n" + "福建 死亡 5人\\n" + "浙江 死亡 5人\\n"
                + "福建 治愈 1人\\n" + "福建 疑似患者 确诊感染 1人\\n" + "山东 疑似患者 确诊感染 1人\\n" + "福建 排除 疑似患者 5人" + "云南 排除 疑似患者 5人";
        lh.count5("([\\u4e00-\\u9fa5]+) 死亡 (\\d+)人", rows);

        sb = lh.ShowHm();
        System.out.println(sb);
    }

    @Test
    public void testCount6() {
        LogHandle lh = new LogHandle();
        StringBuilder sb = new StringBuilder();
        String rows = "福建 新增 感染患者 5人\\n" + "湖北 新增 感染患者 10人\\n" + "湖北 新增 疑似患者 10人\\n" + "广东 新增 疑似患者 3人\\n"
                + "福建 新增 感染患者 5人\\n" + "湖北 感染患者 流入 福建 10人\\n" + "湖北 疑似患者 流入 福建 1人\\n" + "福建 死亡 5人\\n" + "浙江 死亡 5人\\n"
                + "福建 治愈 1人\\n" + "福建 疑似患者 确诊感染 1人\\n" + "山东 疑似患者 确诊感染 1人\\n" + "福建 排除 疑似患者 5人" + "云南 排除 疑似患者 5人";
        lh.count6("([\\u4e00-\\u9fa5]+) 治愈 (\\d+)人", rows);

        sb = lh.ShowHm();
        System.out.println(sb);
    }

    @Test
    public void testCount7() {
        LogHandle lh = new LogHandle();
        StringBuilder sb = new StringBuilder();
        String rows = "福建 新增 感染患者 5人\\n" + "湖北 新增 感染患者 10人\\n" + "湖北 新增 疑似患者 10人\\n" + "广东 新增 疑似患者 3人\\n"
                + "福建 新增 感染患者 5人\\n" + "湖北 感染患者 流入 福建 10人\\n" + "湖北 疑似患者 流入 福建 1人\\n" + "福建 死亡 5人\\n" + "浙江 死亡 5人\\n"
                + "福建 治愈 1人\\n" + "福建 疑似患者 确诊感染 1人\\n" + "山东 疑似患者 确诊感染 1人\\n" + "福建 排除 疑似患者 5人" + "云南 排除 疑似患者 5人";
        lh.count7("([\\u4e00-\\u9fa5]+) 疑似患者 确诊感染 (\\d+)人", rows);

        sb = lh.ShowHm();
        System.out.println(sb);
    }

    @Test
    public void testCount8() {
        LogHandle lh = new LogHandle();
        StringBuilder sb = new StringBuilder();
        String rows = "福建 新增 感染患者 5人\\n" + "湖北 新增 感染患者 10人\\n" + "湖北 新增 疑似患者 10人\\n" + "广东 新增 疑似患者 3人\\n"
                + "福建 新增 感染患者 5人\\n" + "湖北 感染患者 流入 福建 10人\\n" + "湖北 疑似患者 流入 福建 1人\\n" + "福建 死亡 5人\\n" + "浙江 死亡 5人\\n"
                + "福建 治愈 1人\\n" + "福建 疑似患者 确诊感染 1人\\n" + "山东 疑似患者 确诊感染 1人\\n" + "福建 排除 疑似患者 5人" + "云南 排除 疑似患者 5人";
        lh.count8("([\\u4e00-\\u9fa5]+) 排除 疑似患者 (\\d+)人", rows);

        sb = lh.ShowHm();
        System.out.println(sb);
    }

}
