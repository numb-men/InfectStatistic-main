import java.io.*;
import java.util.regex.*;

public class InfectStatistic {

    /**
     * 各省份状态
     * 默认0：没有状况
     * 1：有状况
     */
    public static int[] province_status = new int[32];

    /**
     * 省份排序（包括“全国”）
     */
    public static String[] province_str = {"全国", "安徽", "北京", "重庆", "福建", "甘肃",
            "广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南",
            "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西",
            "陕西", "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江"};

    /**
     * 指定患者类型
     */
    public static int[] type_num = {1,2,3,4};

    /**
     * 类型列表
     */
    public static String[] type_str = {"感染患者", "疑似患者", "治愈", "死亡"};

    /**
     * 指定人数
     * 一级：省份
     * 二级：患者类型
     */
    public static int[][] people_num = new int[32][4];

    /**
     * 文本中存在的所有情况
     */
    public static String[] situation_str = {"(\\S+) 新增 感染患者 (\\d+)人", "(\\S+) 新增 疑似患者 (\\d+)人",
            "(\\S+) 感染患者 流入 (\\S+) (\\d+)人", "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人",
            "(\\S+) 死亡 (\\d+)人", "(\\S+) 治愈 (\\d+)人",
            "(\\S+) 疑似患者 确诊感染 (\\d+)人", "(\\S+) 排除 疑似患者 (\\d+)人"};

    /**
     * 读文件
     */
    public static void readFile() throws Throwable {
        try {
            Throwable var1 = null;
            Object var2 = null;

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("E:\\InfectStatistic-main\\081700316\\log\\2020-01-22.log.txt")));

                String line;
                try {
                    while((line = in.readLine()) != null) {
                        //System.out.println(line);
                        FileProcessing(line);
                    }
                } finally {
                    if (in != null) {
                        in.close();
                    }

                }
            } catch (Throwable var11) {
                if (var1 == null) {
                    var1 = var11;
                } else if (var1 != var11) {
                    var1.addSuppressed(var11);
                }

                throw var1;
            }
        } catch (Exception var12) {
            var12.printStackTrace();
        }

    }

    /**
     * 文本内容处理
     */
    public static void FileProcessing(String string) {
        int num = -1;
        for(int i=0; i<situation_str.length; i++){
            boolean isMatch = Pattern.matches(situation_str[i], string);
            if(isMatch)
                num = i;
        }
        switch(num) {
            case 0:
                addInfected(string);
                break;
            case 1:
                addSuspected(string);
                break;
            case 2:
                flowInfected(string);
                break;
            case 3:
                flowSuspected(string);
                break;
            case 4:
                addDead(string);
                break;
            case 5:
                addCure(string);
                break;
            case 6:
                diagnosisSuspected(string);
                break;
            case 7:
                removeSuspected(string);
                break;
            default:
                System.out.println("wrong!!!");
        }
    }

    /**
     * 新增 感染患者
     */
    public static void addInfected(String string) {
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+|\\d+");
        Matcher m = p.matcher(string);
        String[] arr = new String[5];
        int cnt=0;
        while (m.find()) {
                arr[cnt] = m.group();
                cnt++;
        }
        int n = Integer.parseInt(arr[3]);
        for(int i = 0; i < province_str.length; i++) {
            if(arr[0].equals(province_str[i])) {
                people_num[0][0] += n;
                people_num[i][0] += n;
                province_status[i] = 1;
                break;
            }
        }
    }

    /**
     * 新增 疑似患者
     */
    public static void addSuspected(String string) {
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+|\\d+");
        Matcher m = p.matcher(string);
        String[] arr = new String[5];
        int cnt=0;
        while (m.find()) {
            arr[cnt] = m.group();
            cnt++;
        }
        int n = Integer.parseInt(arr[3]);
        for(int i = 0; i < province_str.length; i++) {
            if(arr[0].equals(province_str[i])) {
                people_num[0][1] += n;
                people_num[i][1] += n;
                province_status[i] = 1;
                break;
            }
        }
    }

    /**
     * 感染患者 流入
     */
    public static void flowInfected(String string) {
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+|\\d+");
        Matcher m = p.matcher(string);
        String[] arr = new String[6];
        int cnt=0;
        while (m.find()) {
            arr[cnt] = m.group();
            cnt++;
        }
        int n = Integer.parseInt(arr[4]);
        for(int i = 0; i < province_str.length; i++) {
            if(arr[0].equals(province_str[i])) {
                people_num[i][0] -= n;
                province_status[i] = 1;
            }
            if(arr[3].equals(province_str[i])) {
                people_num[i][0] += n;
                province_status[i] = 1;
            }
        }
    }

    /**
     * 疑似患者 流入
     */
    public static void flowSuspected(String string) {
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+|\\d+");
        Matcher m = p.matcher(string);
        String[] arr = new String[6];
        int cnt=0;
        while (m.find()) {
            arr[cnt] = m.group();
            cnt++;
        }
        int n = Integer.parseInt(arr[4]);
        for(int i = 0; i < province_str.length; i++) {
            if(arr[0].equals(province_str[i])) {
                people_num[i][1] -= n;
                province_status[i] = 1;
            }
            if(arr[3].equals(province_str[i])) {
                people_num[i][1] += n;
                province_status[i] = 1;
            }
        }

    }

    /**
     * 死亡
     */
    public static void addDead(String string) {
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+|\\d+");
        Matcher m = p.matcher(string);
        String[] arr = new String[4];
        int cnt=0;
        while (m.find()) {
            arr[cnt] = m.group();
            cnt++;
        }
        int n = Integer.parseInt(arr[2]);
        for(int i = 0; i < province_str.length; i++) {
            if(arr[0].equals(province_str[i])) {
                people_num[0][3] += n;
                people_num[0][0] -= n;
                people_num[i][3] += n;
                people_num[i][0] -= n;
                province_status[i] = 1;
                break;
            }
        }
    }

    /**
     * 治愈
     */
    public static void addCure(String string) {
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+|\\d+");
        Matcher m = p.matcher(string);
        String[] arr = new String[4];
        int cnt=0;
        while (m.find()) {
            arr[cnt] = m.group();
            cnt++;
        }
        int n = Integer.parseInt(arr[2]);
        for(int i = 0; i < province_str.length; i++) {
            if(arr[0].equals(province_str[i])) {
                people_num[0][2] += n;
                people_num[0][0] -= n;
                people_num[i][2] += n;
                people_num[i][0] -= n;
                province_status[i] = 1;
                break;
            }
        }
    }

    /**
     * 疑似患者 确诊感染
     */
    public static void diagnosisSuspected(String string) {
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+|\\d+");
        Matcher m = p.matcher(string);
        String[] arr = new String[5];
        int cnt=0;
        while (m.find()) {
            arr[cnt] = m.group();
            cnt++;
        }
        int n = Integer.parseInt(arr[3]);
        for(int i = 0; i < province_str.length; i++) {
            if(arr[0].equals(province_str[i])) {
                people_num[0][1] -= n;
                people_num[0][0] += n;
                people_num[i][1] -= n;
                people_num[i][0] += n;
                province_status[i] = 1;
                break;
            }
        }
    }

    /**
     * 排除 疑似患者
     */
    public static void removeSuspected(String string) {
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+|\\d+");
        Matcher m = p.matcher(string);
        String[] arr = new String[5];
        int cnt=0;
        while (m.find()) {
            arr[cnt] = m.group();
            cnt++;
        }
        int n = Integer.parseInt(arr[3]);
        for(int i = 0; i < province_str.length; i++) {
            if(arr[0].equals(province_str[i])) {
                people_num[i][1] -= n;
                people_num[0][1] -= n;
                province_status[i] = 1;
                break;
            }
        }
    }

    /**
     * 写文件
     */
    public static void writeFile() throws Throwable {
        try {
            Throwable var0 = null;
            Object var1 = null;

            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("E:\\InfectStatistic-main\\081700316\\result\\ListOut.txt")));
                try {
                    province_status[0] = 1;
                    for(int i = 0; i < province_str.length; i++) {
                        if(province_status[i] == 1) {
                            bw.write(province_str[i] + " ");
                            for (int j = 0; j < type_num.length; j++) {
                                for (int k = 0; k < type_num.length; k++) {
                                    if (type_num[k] == j + 1) {
                                        bw.write(type_str[k] + people_num[i][k] + "人 ");
                                        break;
                                    }
                                }
                            }
                            bw.newLine();
                        }
                    }
                    bw.write("// 该文档并非真实数据，仅供测试使用");
                } finally {
                    if (bw != null) {
                        bw.close();
                    }

                }
            } catch (Throwable var10) {
                if (var0 == null) {
                    var0 = var10;
                } else if (var0 != var10) {
                    var0.addSuppressed(var10);
                }

                throw var0;
            }
        } catch (Exception var11) {
            var11.printStackTrace();
        }

    }


    public static void main(String[] args) throws Throwable {
        readFile();
        writeFile();
    }
}
