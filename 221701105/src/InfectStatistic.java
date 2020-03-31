/**
 * InfectStatistic
 * TODO
 *
 * @author 邵研
 * @version 1.0
 * @since xxx
 */

import sun.reflect.generics.tree.Tree;

import java.io.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class InfectStatistic {
    public static void main(String[] args) throws ParseException, IOException {
        ArgParse arg = new ArgParse(args);
        if (arg.command.equals("list"))
            new List(arg);
    }
}

class List {
    String[] provList = {"安徽", "北京", "重庆", "福建", "甘肃", "广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江"};
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    int[][] provStat;
    int[] all;
    TreeSet<String> logFiles;

    List(ArgParse arg) throws ParseException, IOException {

        for (int i = 0; i < provList.length; i++) {
            map.put(provList[i], i);
        }
        String temp;
        logFiles = getFile(arg.logPath);
        temp = logFiles.last().toString();
        temp = temp.substring(temp.lastIndexOf("\\") + 1, temp.lastIndexOf("\\") + 11);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (arg.endDate == null) {
            arg.endDate = temp;
        } else if (sdf.parse(arg.endDate).compareTo(sdf.parse(temp)) > 0) {
            System.out.println("日期错误");
            System.exit(1);
        }

        Date endDate = sdf.parse(arg.endDate);
        String strDate, strPath;
        provStat = new int[31][4];
        all = new int[4];
        Iterator i = logFiles.iterator();
        while (i.hasNext()) {
            strPath = i.next().toString();
            strDate = strPath.substring(strPath.lastIndexOf("\\") + 1, strPath.lastIndexOf("\\") + 11);
            if (sdf.parse(strDate).compareTo(endDate) <= 0) {
                applyLog(strPath);
            } else
                break;
        }
        write(arg.outPath, arg.arr, arg.aprovs);
    }

    //得到log文件列表，传入目录，返回文件路径集合
    TreeSet<String> getFile(String n) {
        TreeSet<String> files = new TreeSet<String>();
        File logPath = new File(n);
        File[] tepList = logPath.listFiles();
        Pattern pattern = Pattern.compile("(\\d){4}-(\\d){2}-(\\d){2}\\.log\\.txt");
        for (int i = 0; i < tepList.length; i++) {
            if (tepList[i].isFile() && pattern.matcher(tepList[i].getName()).matches())
                files.add(tepList[i].toString());
        }
        return files;
    }

    //应用一个log文件，传入log文件路径，直接修改数组provStat
    void applyLog(String path) {
        File file = null;
        BufferedReader br = null;
        StringBuffer buffer = null;
        String data = "";
        try {
            file = new File(path);
            buffer = new StringBuffer();
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
            br = new BufferedReader(isr);
            int s;
            while ((s = br.read()) != -1) {
                buffer.append((char) s);
            }
            data = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String prov1, prov2;
        int num = 0;

        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]{2,3} 新增 疑似患者 \\d+[人]");
        Matcher matcher = pattern.matcher(data);
        while (matcher.find()) {
            String[] arr = matcher.group().split("\\s+");
            prov1 = arr[0];
            num = Integer.parseInt(arr[3].substring(0, arr[3].length() - 1));
            provStat[map.get(prov1)][1] += num;
        }

        pattern = Pattern.compile("[\\u4e00-\\u9fa5]{2,3} 新增 感染患者 \\d+[人]");
        matcher = pattern.matcher(data);
        while (matcher.find()) {
            String[] arr = matcher.group().split("\\s+");
            prov1 = arr[0];
            num = Integer.parseInt(arr[3].substring(0, arr[3].length() - 1));
            provStat[map.get(prov1)][0] += num;
        }

        pattern = Pattern.compile("[\\u4e00-\\u9fa5]{2,3} 感染患者 流入 [\\u4e00-\\u9fa5]{2,3} \\d+[人]");
        matcher = pattern.matcher(data);
        while (matcher.find()) {
            String[] arr = matcher.group().split("\\s+");
            prov1 = arr[0];
            prov2 = arr[3];
            num = Integer.parseInt(arr[4].substring(0, arr[4].length() - 1));
            provStat[map.get(prov1)][0] -= num;
            provStat[map.get(prov2)][0] += num;
        }

        pattern = Pattern.compile("[\\u4e00-\\u9fa5]{2,3} 疑似患者 流入 [\\u4e00-\\u9fa5]{2,3} \\d+[人]");
        matcher = pattern.matcher(data);
        while (matcher.find()) {
            String[] arr = matcher.group().split("\\s+");
            prov1 = arr[0];
            prov2 = arr[3];
            num = Integer.parseInt(arr[4].substring(0, arr[4].length() - 1));
            provStat[map.get(prov1)][1] -= num;
            provStat[map.get(prov2)][1] += num;
        }

        pattern = Pattern.compile("[\\u4e00-\\u9fa5]{2,3} 死亡 \\d+[人]");
        matcher = pattern.matcher(data);
        while (matcher.find()) {
            String[] arr = matcher.group().split("\\s+");
            prov1 = arr[0];
            num = Integer.parseInt(arr[2].substring(0, arr[2].length() - 1));
            provStat[map.get(prov1)][3] += num;
            provStat[map.get(prov1)][0] -= num;
        }

        pattern = Pattern.compile("[\\u4e00-\\u9fa5]{2,3} 治愈 \\d+[人]");
        matcher = pattern.matcher(data);
        while (matcher.find()) {
            String[] arr = matcher.group().split("\\s+");
            prov1 = arr[0];
            num = Integer.parseInt(arr[2].substring(0, arr[2].length() - 1));
            provStat[map.get(prov1)][2] += num;
            provStat[map.get(prov1)][0] -= num;
        }

        pattern = Pattern.compile("[\\u4e00-\\u9fa5]{2,3} 疑似患者 确诊感染 \\d+[人]");
        matcher = pattern.matcher(data);
        while (matcher.find()) {
            String[] arr = matcher.group().split("\\s+");
            prov1 = arr[0];
            num = Integer.parseInt(arr[3].substring(0, arr[3].length() - 1));
            provStat[map.get(prov1)][1] -= num;
            provStat[map.get(prov1)][0] += num;
        }

        pattern = Pattern.compile("[\\u4e00-\\u9fa5]{2,3} 排除 疑似患者 \\d+[人]");
        matcher = pattern.matcher(data);
        while (matcher.find()) {
            String[] arr = matcher.group().split("\\s+");
            prov1 = arr[0];
            num = Integer.parseInt(arr[3].substring(0, arr[3].length() - 1));
            provStat[map.get(prov1)][1] -= num;
        }
    }

    //写文件，传入输出文件路径，人员类型、省份，无返回
    void write(String outPath, String[] arr, String[] provs) throws IOException {
        String str = "";
        for (int i = 0; i < 31; i++) {
            all[0] += provStat[i][0];
            all[1] += provStat[i][1];
            all[2] += provStat[i][2];
            all[3] += provStat[i][3];
        }


        File file = new File(outPath);
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        if (provs.length == 0 || Arrays.asList(provs).contains("全国")) {
            str = "全国";
            if (arr.length == 0 || Arrays.asList(arr).contains("ip")) str = str + " 感染" + all[0] + "人";
            if (arr.length == 0 || Arrays.asList(arr).contains("sp")) str = str + " 疑似" + all[1] + "人";
            if (arr.length == 0 || Arrays.asList(arr).contains("cure")) str = str + " 治愈" + all[2] + "人";
            if (arr.length == 0 || Arrays.asList(arr).contains("dead")) str = str + " 死亡" + all[3] + "人";
            out.write(str + "\n");
        }
        for (int i = 0; i < 31; i++) {
            if (provs.length == 0 || Arrays.asList(provs).contains(provList[i])) {
                str = provList[i];
                if (arr.length == 0 || Arrays.asList(arr).contains("ip")) str = str + "  感染" + provStat[i][0] + "人";
                if (arr.length == 0 || Arrays.asList(arr).contains("sp")) str = str + "  疑似" + provStat[i][1] + "人";
                if (arr.length == 0 || Arrays.asList(arr).contains("cure")) str = str + "  治愈" + provStat[i][2] + "人";
                if (arr.length == 0 || Arrays.asList(arr).contains("dead")) str = str + "  死亡" + provStat[i][3] + "人";
                out.write(str + "\n");
            }
        }
        out.write("// 该文档并非真实数据，仅供测试使用");
        out.flush();
        out.close();
    }
}

class ArgParse {
    public String command = null;
    public String logPath = null, outPath = null, endDate = null;
    public HashSet types = new HashSet();
    public HashSet provinces = new HashSet();
    public String[] arr;
    public String[] aprovs;

    ArgParse(String[] args) {
        if (args.length == 0) {
            System.out.println("至少要输入一个命令");
            System.exit(0);
        }
        command = args[0];
        HashSet types = new HashSet();
        HashSet provinces = new HashSet();

        if (command.equals("list")) {
            for (int i = 1; i < args.length; i++) {
                switch (args[i]) {
                    case "-log":
                        logPath = args[++i];
                        break;
                    case "-out":
                        outPath = args[++i];
                        break;
                    case "-date":
                        endDate = args[++i];
                        break;
                    case "-type":
                        while (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                            types.add(args[++i]);
                        }
                        break;
                    case "-province":
                        while (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                            provinces.add(args[++i]);
                        }
                        break;
                }
            }
        }
        arr = (String[]) types.toArray(new String[types.size()]);
        aprovs = (String[]) provinces.toArray(new String[types.size()]);
    }
}
