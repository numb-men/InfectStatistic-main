import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project Name:InfectStatistic-main2
 * File Name:LogHandle.java
 * Package Name:
 * Date:下午4:20:21
 * Copyright (c) 2020, Doris All Rights Reserved.
 *
*/

/**
 * Description: <br/>
 * Date: 下午4:20:21 <br/>
 * 
 * @author Doris
 * @version
 * @see
 */
public class LogHandle {

    Pattern p;

    Matcher m;

    LogLine ll;

    HashMap<String, InflectInfo> hm = new HashMap<>();

    public LogHandle() {
        ll = new LogLine();

    }

    public StringBuilder ShowHm() {
        int sumIp = 0;
        int sumSp = 0;
        int sumCure = 0;
        int sumDead = 0;

        StringBuilder sb = new StringBuilder();
        ArrayList<String> list = new ArrayList<>(hm.keySet());
        Collections.sort(list, Collator.getInstance(Locale.CHINA));
        Iterator<String> iterator = list.iterator();
        // 迭代排序后的key的list
        while ((iterator.hasNext())) {
            String key = iterator.next();
            InflectInfo value = hm.get(key);
            sb.append(value.getArea() + " 感染患者" + value.getIp() + "人 疑似患者" + value.getSp() + "人 治愈" + value.getCure()
                    + "人 死亡" + value.getDead() + "人\n");
            sumIp += value.getIp();
            sumSp += value.getSp();
            sumCure += value.getCure();
            sumDead += value.getDead();

        }
        String str = "全国 感染患者" + sumIp + "人 疑似患者" + sumSp + "人 治愈" + sumCure + "人 死亡" + sumDead + "人\n";
        sb.insert(0, str);

        // for (Entry<String, InflectInfo> entry : hm.entrySet()) {
        //
        // InflectInfo value = entry.getValue();
        //
        // // System.out.println(entry.getKey() + " 感染患者" + value.getIp() + "人
        // // 疑似患者" + value.getSp() + "人 治愈"
        // // + value.getCure() + "人 死亡" + value.getDead() + "人\n");
        // sb.append(entry.getKey() + " 感染患者" + value.getIp() + "人 疑似患者" +
        // value.getSp() + "人 治愈" + value.getCure()
        // + "人 死亡" + value.getDead() + "人\n");
        //
        // }
        return sb;

    }

    public void WriteToFile(String filePath) {
        try {
            File file = new File(filePath);
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.println(ShowHm());// 往文件里写入字符串

            ps.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void calculate(String rows) {
        String s1 = ll.getS1();
        String s2 = ll.getS2();
        String s3 = ll.getS3();
        String s4 = ll.getS4();
        String s5 = ll.getS5();
        String s6 = ll.getS6();
        String s7 = ll.getS7();
        String s8 = ll.getS8();
        count1(s1, rows);
        count2(s2, rows);
        count3(s3, rows);
        count4(s4, rows);
        count5(s5, rows);
        count6(s6, rows);
        count7(s7, rows);
        count8(s8, rows);
    }

    // String s1 = "([\\u4e00-\\u9fa5]+) 新增 感染患者 (\\d+)人";
    public void count1(String str, String rows) {
        p = Pattern.compile(str);

        m = p.matcher(rows);

        while (m.find()) {

            String area = m.group(1);

            if (hm.containsKey(area)) {
                hm.get(area).setIp(hm.get(area).getIp() + Integer.valueOf(m.group(2)));
            } else {
                InflectInfo inflectInfo = new InflectInfo();
                inflectInfo.setArea(area);

                inflectInfo.setIp(Integer.valueOf(m.group(2)));
                hm.put(area, inflectInfo);
            }

        }

    }

    // String s2 = "([\\u4e00-\\u9fa5]+) 新增 疑似患者 (\\d+)人";
    public void count2(String str, String rows) {
        p = Pattern.compile(str);

        m = p.matcher(rows);

        while (m.find()) {

            String area = m.group(1);

            if (hm.containsKey(area)) {
                hm.get(area).setSp(hm.get(area).getSp() + Integer.valueOf(m.group(2)));
            } else {
                InflectInfo inflectInfo = new InflectInfo();
                inflectInfo.setArea(area);
                inflectInfo.setSp(Integer.valueOf(m.group(2)));
                hm.put(area, inflectInfo);
            }

        }

    }

    // String s3 = "([\\u4e00-\\u9fa5]+) 感染患者 流入 ([\\u4e00-\\u9fa5]+) (\\d+)人";
    public void count3(String str, String rows) {
        p = Pattern.compile(str);

        m = p.matcher(rows);

        while (m.find()) {
            String areaStart = m.group(1);

            if (hm.containsKey(areaStart)) {
                hm.get(areaStart).setIp(hm.get(areaStart).getIp() - Integer.valueOf(m.group(3)));
            } else {

                InflectInfo inflectInfoStart = new InflectInfo();

                inflectInfoStart.setArea(areaStart);
                inflectInfoStart.setIp(-Integer.valueOf(m.group(3)));
                hm.put(areaStart, inflectInfoStart);
            }

            String area = m.group(2);

            if (hm.containsKey(area)) {
                hm.get(area).setIp(hm.get(area).getIp() + Integer.valueOf(m.group(3)));
            } else {
                InflectInfo inflectInfo = new InflectInfo();
                inflectInfo.setArea(area);
                inflectInfo.setIp(Integer.valueOf(m.group(3)));
                hm.put(area, inflectInfo);
            }

        }

    }

    // String s4 = "([\\u4e00-\\u9fa5]+) 疑似患者 流入 ([\\u4e00-\\u9fa5]+) (\\d+)人";

    public void count4(String str, String rows) {
        p = Pattern.compile(str);

        m = p.matcher(rows);

        while (m.find()) {
            String areaStart = m.group(1);
            if (hm.containsKey(areaStart)) {
                hm.get(areaStart).setSp(hm.get(areaStart).getSp() - Integer.valueOf(m.group(3)));
            } else {

                InflectInfo inflectInfoStart = new InflectInfo();

                inflectInfoStart.setArea(areaStart);
                inflectInfoStart.setSp(-Integer.valueOf(m.group(3)));
                hm.put(areaStart, inflectInfoStart);
            }

            String area = m.group(2);

            if (hm.containsKey(area)) {
                hm.get(area).setSp(hm.get(area).getSp() + Integer.valueOf(m.group(3)));
            } else {
                InflectInfo inflectInfo = new InflectInfo();
                inflectInfo.setArea(area);
                inflectInfo.setSp(Integer.valueOf(m.group(3)));
                hm.put(area, inflectInfo);
            }

        }

    }

    // String s5 = "([\\u4e00-\\u9fa5]+) 死亡 (\\d+)人";
    public void count5(String str, String rows) {
        p = Pattern.compile(str);

        m = p.matcher(rows);

        while (m.find()) {

            String area = m.group(1);

            if (hm.containsKey(area)) {
                hm.get(area).setDead(hm.get(area).getDead() + Integer.valueOf(m.group(2)));
                hm.get(area).setIp(hm.get(area).getIp() - Integer.valueOf(m.group(2)));
            } else {
                InflectInfo inflectInfo = new InflectInfo();
                inflectInfo.setArea(area);
                inflectInfo.setDead(Integer.valueOf(m.group(2)));
                inflectInfo.setIp(-Integer.valueOf(m.group(2)));
                hm.put(area, inflectInfo);
            }

        }

    }

    // String s6 = "([\\u4e00-\\u9fa5]+) 治愈 (\\d+)人";

    public void count6(String str, String rows) {
        p = Pattern.compile(str);

        m = p.matcher(rows);

        while (m.find()) {

            String area = m.group(1);

            if (hm.containsKey(area)) {
                hm.get(area).setCure(hm.get(area).getCure() + Integer.valueOf(m.group(2)));
                hm.get(area).setIp(hm.get(area).getIp() - Integer.valueOf(m.group(2)));
            } else {
                InflectInfo inflectInfo = new InflectInfo();
                inflectInfo.setArea(area);
                inflectInfo.setCure(Integer.valueOf(m.group(2)));
                inflectInfo.setIp(-Integer.valueOf(m.group(2)));
                hm.put(area, inflectInfo);
            }

        }

    }

    // String s7 = "([\\u4e00-\\u9fa5]+) 疑似患者 确认感染 (\\d+)人";
    public void count7(String str, String rows) {
        p = Pattern.compile(str);

        m = p.matcher(rows);

        while (m.find()) {

            String area = m.group(1);

            if (hm.containsKey(area)) {
                hm.get(area).setIp(hm.get(area).getIp() + Integer.valueOf(m.group(2)));
                hm.get(area).setSp(hm.get(area).getSp() - Integer.valueOf(m.group(2)));
            } else {
                InflectInfo inflectInfo = new InflectInfo();
                inflectInfo.setArea(area);
                inflectInfo.setIp(Integer.valueOf(m.group(2)));
                inflectInfo.setSp(-Integer.valueOf(m.group(2)));
                hm.put(area, inflectInfo);
            }

        }

    }

    // String s8 = "([\\u4e00-\\u9fa5]+) 排除疑似患者 (\\d+)人"
    public void count8(String str, String rows) {
        p = Pattern.compile(str);

        m = p.matcher(rows);

        while (m.find()) {

            String area = m.group(1);

            if (hm.containsKey(area)) {

                hm.get(area).setSp(hm.get(area).getSp() - Integer.valueOf(m.group(2)));
            } else {
                InflectInfo inflectInfo = new InflectInfo();
                inflectInfo.setArea(area);

                inflectInfo.setSp(-Integer.valueOf(m.group(2)));
                hm.put(area, inflectInfo);
            }

        }

    }
}
