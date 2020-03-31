import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * InfectStatistic TODO
 *
 * @author Doris
 * @version v1.0
 * @since 2020.2.11
 */
class InfectStatistic {

    /**
     * Description: 参数处理 Date:2020年2月12日 上午11:32:59 <br/>
     * 
     * @author Doris
     * @version
     * @see
     */
    static class ArgsParse {
        Option log;// 必填

        Option out;// 必填

        Option date;

        Option type;// 多个参数

        Option province;// 多个参数

        Options options;

        String slog;// 必填

        String sout;// 必填

        String sdate;

        ArrayList<String> stype;// 多个参数

        ArrayList<String> sprovince;// 多个参数

        CommandLine cli;

        CommandLineParser cliParser;

        HelpFormatter helpFormatter;

        boolean hasType;

        boolean hasPronvince;

        public ArgsParse() {
            // public Options addOption(String opt,
            // String longOpt,
            // boolean hasArg,
            // String description)
            log = new Option("l", "log", true, "log");
            out = new Option("o", "out", true, "out");
            date = new Option("d", "date", true, "date");
            type = new Option("t", "type", true, "type");
            province = new Option("p", "province", true, "pronvince");
            options = new Options();

            cli = null;
            cliParser = new DefaultParser();
            helpFormatter = new HelpFormatter();

            log.setRequired(true);
            out.setRequired(true);
            date.setRequired(false);
            type.setRequired(false);
            province.setRequired(false);

            options.addOption(log);
            options.addOption(out);
            options.addOption(date);
            options.addOption(type);
            options.addOption(province);
        }

        public void parse(String[] args) {
            try {
                cli = cliParser.parse(options, args);
            } catch (org.apache.commons.cli.ParseException e) {

                // Auto-generated catch block
                e.printStackTrace();

            }
            if (cli.hasOption("log")) {
                slog = cli.getOptionValue("log");
            }

            if (cli.hasOption("out")) {
                sout = cli.getOptionValue("out");
            }

            if (cli.hasOption("date")) {
                sdate = cli.getOptionValue("date");

            }
            if (cli.hasOption("type")) {
                stype = typeAndPronvinceParse(args, "-type");
            }
            if (cli.hasOption("province")) {
                sprovince = typeAndPronvinceParse(args, "-province");
            }

        }

        public ArrayList<String> typeAndPronvinceParse(String[] args, String oneArg) {
            ArrayList<String> al = new ArrayList<>();
            for (int i = 0; i < args.length; i++) {
                if (oneArg.equals(args[i])) {
                    for (int j = i + 1; j < args.length; j++) {
                        if (args[j].startsWith("-")) {
                            return al;
                        } else {
                            al.add(args[j]);
                        }

                    }
                }
            }
            return al;

        }

    }

    /**
     * Description: 文件处理<br/>
     * Date: 上午9:43:46 <br/>
     * 
     * @author Doris
     * @version
     * @see
     */
    static class FileHandle {

        // 读取一个文件内容
        public StringBuilder fileRead(String fileName) {
            File file = new File(fileName);// 定义一个file对象，用来初始化FileReader
            FileReader reader = null;
            try {
                reader = new FileReader(file);
            } catch (FileNotFoundException e) {

                // Auto-generated catch block
                e.printStackTrace();

            } // 定义一个fileReader对象，用来初始化BufferedReader
            BufferedReader bReader = new BufferedReader(reader);// new一个BufferedReader对象，将文件内容读取到缓存
            StringBuilder sb = new StringBuilder();
            sb = new StringBuilder();// 定义一个字符串缓存，将字符串存放缓存中
            String s = "";
            try {
                while ((s = bReader.readLine()) != null) {// 逐行读取文件内容，不读取换行符和末尾的空格
                    sb.append(s + "\n");// 将读取的字符串添加换行符后累加存放在缓存中

                }
            } catch (IOException e) {

                // Auto-generated catch block
                e.printStackTrace();

            }
            try {
                bReader.close();
            } catch (IOException e) {

                // Auto-generated catch block
                e.printStackTrace();

            }

            return sb;
        }

        public ArrayList<String> getFiles(String path, String sdate) {
            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");

            File file = new File(path);
            String[] nameStrings = file.list(); // 取得path目录下所有文件名称
            ArrayList<String> fileName = new ArrayList<>();
            String FileDateStr = "";
            Date filedate = new Date();
            Date argDate = null;
            Date maxdate = null;
            String maxdateStr = nameStrings[0].substring(0, nameStrings[0].indexOf('.'));// 截取文件名中的日期

            try {
                maxdate = dFormat.parse(maxdateStr);
            } catch (ParseException e1) {

                // Auto-generated catch block
                e1.printStackTrace();

            }
            try {

                for (int i = 0; i < nameStrings.length; i++) {
                    FileDateStr = nameStrings[i].substring(0, nameStrings[i].indexOf('.'));// 截取文件名中的日期

                    filedate = dFormat.parse(FileDateStr);
                    if (filedate.getTime() >= maxdate.getTime()) {
                        maxdate = filedate;// 找出最大日期

                    }

                }
                if (sdate != null) {

                    argDate = dFormat.parse(sdate);// 将参数转为日期形式
                    if (argDate.getTime() > maxdate.getTime()) {
                        System.out.println("日期超出范围");
                        System.exit(0);
                    }
                } else {
                    argDate = maxdate;
                }

                for (int i = 0; i < nameStrings.length; i++) {
                    FileDateStr = nameStrings[i].substring(0, nameStrings[i].indexOf('.'));// 截取文件名中的日期

                    filedate = dFormat.parse(FileDateStr);

                    if (filedate.getTime() <= argDate.getTime()) {

                        fileName.add(nameStrings[i]);
                    }

                }
            } catch (ParseException e) {

                e.printStackTrace();

            }

            return fileName;
        }

        // 读取多个文件内容
        public StringBuilder filesRead(String path, String sdate) {
            ArrayList<String> fileName = getFiles(path, sdate);
            StringBuilder ab = new StringBuilder();
            Iterator<String> it = fileName.iterator();

            StringBuilder abtmp = new StringBuilder();
            while (it.hasNext()) {
                abtmp = fileRead(path + "\\" + it.next());
                ab.append(abtmp);
            }
            return ab;
        }

        public void WriteToFile(String filePath, HashMap<String, InflectInfo> hm, ArrayList<String> sprovince,
                ArrayList<String> stype) {
            try {
                File file = new File(filePath);
                PrintStream ps = new PrintStream(new FileOutputStream(file));
                ps.println(ShowHm(hm, sprovince, stype));// 往文件里写入字符串

                ps.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public StringBuilder ShowHm(HashMap<String, InflectInfo> hm, ArrayList<String> sprovince,
                ArrayList<String> stype) {

            StringBuilder sb = new StringBuilder();
            ArrayList<String> list = new ArrayList<>(hm.keySet());
            Collections.sort(list, Collator.getInstance(Locale.CHINA));
            Iterator<String> iterator = list.iterator();
            // 迭代排序后的key的list
            while ((iterator.hasNext())) {
                String key = iterator.next();

                if (sprovince != null) {
                    if (sprovince.contains(key)) {
                        InflectInfo value = hm.get(key);
                        sb.append(getInfo(value, stype));
                        // sb.append(value.getArea() + " 感染患者" + value.getIp() +
                        // "人
                        // 疑似患者" + value.getSp() + "人 治愈"
                        // + value.getCure() + "人 死亡" + value.getDead() +
                        // "人\n");

                    }

                } else {// 无-province参数时
                    InflectInfo value = hm.get(key);
                    // sb.append(value.getArea() + " 感染患者" + value.getIp() + "人
                    // 疑似患者" + value.getSp() + "人 治愈"
                    // + value.getCure() + "人 死亡" + value.getDead() + "人\n");
                    sb.append(getInfo(value, stype));
                    String str = getCountrywideInfo(hm, stype);
                    sb.insert(0, str);
                    return sb;
                }

            }

            if (sprovince != null && sprovince.contains("全国")) {
                String str = getCountrywideInfo(hm, stype);
                sb.insert(0, str);
            }
            return sb;

        }

        public String getCountrywideInfo(HashMap<String, InflectInfo> hm, ArrayList<String> stype) {
            int sumIp = 0;
            int sumSp = 0;
            int sumCure = 0;
            int sumDead = 0;
            StringBuilder sb = new StringBuilder();
            ArrayList<String> list = new ArrayList<>(hm.keySet());
            Iterator<String> iterator = list.iterator();
            while ((iterator.hasNext())) {
                String key = iterator.next();
                InflectInfo value = hm.get(key);

                sumIp += value.getIp();
                sumSp += value.getSp();
                sumCure += value.getCure();
                sumDead += value.getDead();
            }
            InflectInfo value = new InflectInfo("全国", sumIp, sumSp, sumCure, sumDead);
            sb = getInfo(value, stype);
            // String str = "全国 感染患者" + sumIp + "人 疑似患者" + sumSp + "人 治愈" +
            // sumCure
            // + "人 死亡" + sumDead + "人\n";
            return sb.toString();
        }

        public StringBuilder getInfo(InflectInfo value, ArrayList<String> stype) {
            String ip;
            String sp;
            String dead;
            String cure;
            StringBuilder sb = new StringBuilder();
            ip = "感染患者" + value.getIp() + "人 ";
            sp = "疑似患者" + value.getSp() + "人 ";

            cure = "治愈" + value.getCure() + "人 ";
            dead = "死亡" + value.getDead() + "人";
            sb.append(value.getArea() + " ");
            if (stype != null) {
                if (stype.contains("ip")) {
                    sb.append(ip);
                } else if (stype.contains("sp")) {
                    sb.append(sp);
                } else if (stype.contains("cure")) {
                    sb.append(cure);
                } else if (stype.contains("dead")) {
                    sb.append(dead);
                }
            } else {
                sb.append(ip + sp + cure + dead);
            }
            sb.append("\n");

            return sb;
        }

    }

    /**
     * Description:正则表达式 1、<省> 新增 感染患者 n人 2、<省> 新增 疑似患者 n人 3、<省1> 感染患者 流入 <省2>
     * n人 4、<省1> 疑似患者 流入 <省2> n人 5、<省> 死亡 n人 6、<省> 治愈 n人 7、<省> 疑似患者 确诊感染 n人
     * 8、<省> 排除 疑似患者 n人<br/>
     * 
     * Date: 下午3:39:57 <br/>
     * 
     * @author Doris
     * @version
     * @see
     */

    static class LogLine {
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

    /**
     * Description: 日志处理<br/>
     * Date: 下午4:20:21 <br/>
     * 
     * @author Doris
     * @version
     * @see
     */
    static class LogHandle {

        Pattern p;

        Matcher m;

        LogLine ll;

        HashMap<String, InflectInfo> hm = new HashMap<>();

        public LogHandle() {
            ll = new LogLine();

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

        // String s3 = "([\\u4e00-\\u9fa5]+) 感染患者 流入 ([\\u4e00-\\u9fa5]+)
        // (\\d+)人";
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

        // String s4 = "([\\u4e00-\\u9fa5]+) 疑似患者 流入 ([\\u4e00-\\u9fa5]+)
        // (\\d+)人";

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

    /**
     * Description:疫情实体类 <br/>
     * Date: 下午4:03:05 <br/>
     * 
     * @author Doris
     * @version
     * @see
     */
    static class InflectInfo {
        String area;

        int ip;

        int sp;

        int cure;

        int dead;

        public InflectInfo() {

        }

        public InflectInfo(String area, int ip, int sp, int cure, int dead) {

            this.area = area;
            this.ip = ip;
            this.sp = sp;
            this.cure = cure;
            this.dead = dead;
        }

        @Override
        public String toString() {
            return "InflectInfo [area=" + area + ", ip=" + ip + ", sp=" + sp + ", cure=" + cure + ", dead=" + dead
                    + "]";
        }

        public void setArea(String area) {
            this.area = area;
        }

        public void setIp(int ip) {
            this.ip = ip;
        }

        public void setSp(int sp) {
            this.sp = sp;
        }

        public void setCure(int cure) {
            this.cure = cure;
        }

        public void setDead(int dead) {
            this.dead = dead;
        }

        public String getArea() {
            return area;
        }

        public int getIp() {
            return ip;
        }

        public int getSp() {
            return sp;
        }

        public int getCure() {
            return cure;
        }

        public int getDead() {
            return dead;
        }

    }

    public static void main(String[] args) {
        ArgsParse ap = new ArgsParse();
        ap.parse(args);
        FileHandle fh = new FileHandle();

        StringBuilder sb = fh.filesRead(ap.slog, ap.sdate);
        LogHandle lh = new LogHandle();
        lh.calculate(sb.toString());
        fh.WriteToFile(ap.sout, lh.hm, ap.sprovince, ap.stype);
    }
}
