/**
 * Project Name:InfectStatistic-main2
 * File Name:LogLine.java
 * Package Name:
 * Date:下午3:39:57
 * Copyright (c) 2020, Doris All Rights Reserved.
 *
*/

/**
 * Description:日志格式 <br/>
 * Date: 下午3:39:57 <br/>
 * 
 * @author Doris
 * @version
 * @see
 */
/*
 * 1、<省> 新增 感染患者 n人 2、<省> 新增 疑似患者 n人 3、<省1> 感染患者 流入 <省2> n人 4、<省1> 疑似患者 流入 <省2>
 * n人 5、<省> 死亡 n人 6、<省> 治愈 n人 7、<省> 疑似患者 确诊感染 n人 8、<省> 排除 疑似患者 n人
 */
public class LogLine {
    String s1 = "([\\u4e00-\\u9fa5]+) 新增 感染患者 (\\d+)人";

    String s2 = "([\\u4e00-\\u9fa5]+) 新增 疑似患者 (\\d+)人";

    String s3 = "([\\u4e00-\\u9fa5]+) 感染患者 流入 ([\\u4e00-\\u9fa5]+) (\\d+)人";

    String s4 = "([\\u4e00-\\u9fa5]+) 疑似患者 流入 ([\\u4e00-\\u9fa5]+) (\\d+)人";

    String s5 = "([\\u4e00-\\u9fa5]+) 死亡 (\\d+)人";

    String s6 = "([\\u4e00-\\u9fa5]+) 治愈 (\\d+)人";

    String s7 = "([\\u4e00-\\u9fa5]+) 疑似患者 确诊感染 (\\d+)人";

    String s8 = "([\\u4e00-\\u9fa5]+) 排除 疑似患者 (\\d+)人";

    public String getS1() {
        return s1;
    }

    public String getS2() {
        return s2;
    }

    public String getS3() {
        return s3;
    }

    public String getS4() {
        return s4;
    }

    public String getS5() {
        return s5;
    }

    public String getS6() {
        return s6;
    }

    public String getS7() {
        return s7;
    }

    public String getS8() {
        return s8;
    }

}
