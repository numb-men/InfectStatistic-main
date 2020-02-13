package reg;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *  这个类用来发出匹配正则表达式请求,并将得到的信息放到一个map中
 *  将上述得到的map写入到本地文件
 */
public class AllInformation {
    private Map<String,Integer[]> info;
    String REGEX1 = "(\\S+) 新增 感染患者 (\\d+)人";
    String REGEX2 = "(\\S+) 新增 疑似患者 (\\d+)人";
    String REGEX3 = "(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
    String REGEX4 = "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
    String REGEX5 = "(\\S+) 死亡 (\\d+)人";
    String REGEX6 = "(\\S+) 治愈 (\\d+)人";
    String REGEX7 = "(\\S+) 疑似患者 确诊感染 (\\d+)人";
    String REGEX8 = "(\\S+) 排除 疑似患者 (\\d+)人";

    public AllInformation() {
        // 原来由于所有的键的引用都为同一个,所有修改一个会变为修改全部
//        Integer[] num = {0,0,0,0};
        info = new LinkedHashMap<>();
        info.put("全国",new Integer[]{0,0,0,0});
        info.put("安徽",new Integer[]{0,0,0,0});
        info.put("北京",new Integer[]{0,0,0,0});
        info.put("重庆",new Integer[]{0,0,0,0});
        info.put("福建",new Integer[]{0,0,0,0});
        info.put("甘肃",new Integer[]{0,0,0,0});
        info.put("广东",new Integer[]{0,0,0,0});
        info.put("广西",new Integer[]{0,0,0,0});
        info.put("贵州",new Integer[]{0,0,0,0});
        info.put("海南",new Integer[]{0,0,0,0});
        info.put("河北",new Integer[]{0,0,0,0});
        info.put("河南",new Integer[]{0,0,0,0});
        info.put("黑龙",new Integer[]{0,0,0,0});
        info.put("湖北",new Integer[]{0,0,0,0});
        info.put("湖南",new Integer[]{0,0,0,0});
        info.put("吉林",new Integer[]{0,0,0,0});
        info.put("江苏",new Integer[]{0,0,0,0});
        info.put("江西",new Integer[]{0,0,0,0});
        info.put("辽宁",new Integer[]{0,0,0,0});
        info.put("内蒙",new Integer[]{0,0,0,0});
        info.put("宁夏",new Integer[]{0,0,0,0});
        info.put("青海",new Integer[]{0,0,0,0});
        info.put("山东",new Integer[]{0,0,0,0});
        info.put("山西",new Integer[]{0,0,0,0});
        info.put("陕西",new Integer[]{0,0,0,0});
        info.put("上海",new Integer[]{0,0,0,0});
        info.put("四川",new Integer[]{0,0,0,0});
        info.put("天津",new Integer[]{0,0,0,0});
        info.put("西藏",new Integer[]{0,0,0,0});
        info.put("新疆",new Integer[]{0,0,0,0});
        info.put("云南",new Integer[]{0,0,0,0});
        info.put("浙江",new Integer[]{0,0,0,0});

    }

    // 通过reg类返回的map信息设置要写入的文件信息
    public void setting(Map<String,Integer[]> lineInfo) {
        // 得到要写入的键
        Set<String> keys = lineInfo.keySet();
        for (String key:keys) {

            // 得到要写入的整数信息
            Integer[] newInfo = lineInfo.get(key);

            // 得到原始的整数信息
            Integer[] oriInfo = info.get(key);
            System.out.println("修改完成前");
            System.out.println(key+" "+oriInfo[0]+" "+oriInfo[1]+" "+oriInfo[2]+" "+oriInfo[3]+" ");

            for (int i = 0;i<newInfo.length;i++) {
                // 找到不为0的信息修改
                oriInfo[i] = oriInfo[i]+newInfo[i];
            }
            System.out.println("修改完成hou");
            System.out.println(key+" "+oriInfo[0]+" "+oriInfo[1]+" "+oriInfo[2]+" "+oriInfo[3]+" ");
            info.put(key,oriInfo);
        }
    }

    // 将读入的文件的每一行经过正则表达式处理
    public void processInfo(String filePath) {
        // filePath 为要处理的文件名
//        RegOne regOne = new RegOne();
//        RegTwo regTwo = new RegTwo();
//        RegThree regThree = new RegThree();
//        RegFour regFour = new RegFour();
//        RegFive regFive = new RegFive();
//        RegSix regSix = new RegSix();
//        RegSeven regSeven = new RegSeven();
//        RegEight regEight = new RegEight();
//
//        regOne.setRegApprover(regTwo);
//        regTwo.setRegApprover(regThree);
//        regThree.setRegApprover(regFour);
//        regFour.setRegApprover(regFive);
//        regFive.setRegApprover(regSix);
//        regSix.setRegApprover(regSeven);
//        regSeven.setRegApprover(regEight);
//        regEight.setRegApprover(regOne);
//        RegOneTwo regOneTwo = new RegOneTwo();
//        RegThreeFour regThreeFour = new RegThreeFour();
//        RegFiveSix regFiveSix = new RegFiveSix();
//        RegSevenEight regSevenEight = new RegSevenEight();
//        regOneTwo.setRegApprover(regThreeFour);
//        regThreeFour.setRegApprover(regFiveSix);
//        regFiveSix.setRegApprover(regSevenEight);
//        regSevenEight.setRegApprover(regOneTwo);
        try {
            // 准备输入流
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            Map<String,Integer[]> lineInfo;
            // 读取文件的每一行
            while((line = bufferedReader.readLine())!=null && !line.startsWith("//")) {
                System.out.println(line);
                lineInfo = judgeReg(line);

                // 设置信息
                Set<String> keys = lineInfo.keySet();
                for (String key:keys) {
                    Integer[] num = lineInfo.get(key);
                    System.out.println(key+"---"+num[0]+"--"+num[1]+"--"+num[2]+"--"+num[3]);
                }
                this.setting(lineInfo);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    // 写入日志文件
    public void writeIntoLog(String logPath) {
        // 经过上面处理已经获得了全部的要写入日志的信息
        try {
            FileOutputStream fos = new FileOutputStream(logPath);
            // 键为省份信息,第一个为全国,其他为按照拼音顺序
            Set<String> provinces = info.keySet();
            for (String province:provinces) {
                Integer[] populations = info.get(province);
                String line;
                if (populations[0]!=0 || populations[1]!=0 || populations[2]!=0 || populations[3]!=0) {
                    line = String.format("%s 感染患者%d人 疑似患者%d人 治愈%d人 死亡%d人\n",
                            province,populations[0],populations[1],populations[2],populations[3]);
                    // 写入信息
                    fos.write(line.getBytes());
                }

            }
            String endLine = "// 该文档并非真实数据，仅供测试使用\n";
            fos.write(endLine.getBytes());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 判断用哪个正则表达式
    public Map<String,Integer[]> judgeReg(String line) {
        int regNo = 0;
        Map<String,Integer[]> lineInfo = new HashMap<>();
        Pattern pattern1 = Pattern.compile(REGEX1);
        Pattern pattern2 = Pattern.compile(REGEX2);
        Pattern pattern3 = Pattern.compile(REGEX3);
        Pattern pattern4 = Pattern.compile(REGEX4);
        Pattern pattern5 = Pattern.compile(REGEX5);
        Pattern pattern6 = Pattern.compile(REGEX6);
        Pattern pattern7 = Pattern.compile(REGEX7);
        Pattern pattern8 = Pattern.compile(REGEX8);

        Matcher matcher1 = pattern1.matcher(line);
        Matcher matcher2 = pattern2.matcher(line);
        Matcher matcher3 = pattern3.matcher(line);
        Matcher matcher4 = pattern4.matcher(line);
        Matcher matcher5 = pattern5.matcher(line);
        Matcher matcher6 = pattern6.matcher(line);
        Matcher matcher7 = pattern7.matcher(line);
        Matcher matcher8 = pattern8.matcher(line);

        if(matcher1.find()) {
            System.out.println("1");
            lineInfo = new RegOne().process(line);
        }
        else if (matcher2.find()) {
            lineInfo = new RegTwo().process(line);
            System.out.println("2");
        }
        else if (matcher3.find()) {
            lineInfo = new RegThree().process(line);
            System.out.println("3");
        }
        else if (matcher4.find()) {
            lineInfo = new RegFour().process(line);
            System.out.println("4");
        }
        else if (matcher5.find()) {
            lineInfo = new RegFive().process(line);
            System.out.println("5");
        }
        else if (matcher6.find()) {
            lineInfo = new RegSix().process(line);
            System.out.println("6");
        }
        else if (matcher7.find()) {
            System.out.println("===============================");
            lineInfo = new RegSeven().process(line);
            System.out.println("7");

        }
        else if (matcher8.find()) {
            lineInfo = new RegEight().process(line);
            System.out.println("8");
        }
        return lineInfo;
    }

    public void printInfo() {
        Set<String> keys = info.keySet();
        for (String key:keys) {
            Integer[] integers = info.get(key);
            System.out.println(String.format("%s %d %d %d %d",key,integers[0],
                    integers[1],integers[2],integers[3]));
        }
    }
}
