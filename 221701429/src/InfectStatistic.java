import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InfectStatistic
 * TODO
 *
 * @author 221701429_黄晓东
 * @version 1.0
 * @since xxx
 */
class InfectStatistic {
    static class province{
        /*enum name{
            北京,天津,上海,重庆,河北,山西,辽宁,吉林,黑龙江,江苏,浙江,安徽,福建,江西, 山东,河南,湖北,
            湖南,广东,海南,四川,贵州,云南,陕西,甘肃,青海,台湾,内蒙古, 广西,西藏,宁夏,新疆,香港,澳门
        }*/
        String name;
        int ip;//感染
        int sp;//疑似
        int cure;//治愈
        int dead;//死亡
        province(String name, int ip, int sp, int cure, int dead){
            this.name = name;
            this.ip = ip;
            this.sp = sp;
            this.cure = cure;
            this.dead = dead;
        }
    }
    //建立HashMap，把命令参数和参数值分开
    private static HashMap<String, String> parseArgs(String[] args) {
        HashMap<String, String> tmp = new HashMap<String, String>();
        String key = null;
        //判断命令是否为list
        if (!args[0].equals("list")) {
            System.out.println("该命令不存在！");
            return tmp;
        }
        //命令参数和参数值放入HashMap中
        for (int i = 1; i<args.length; i++){
            String arg = args[i];
            if (arg.startsWith("-")){
                if (key == null){
                    key = arg;
                }
                else{
                    tmp.put(key,null);
                    key = arg;
                }
            }
            else {
                tmp.put(key, arg);
                key = null;
            }
        }
       return tmp;
    }
    //命令对应的行为
    private static void func(HashMap<String, String> parseArgs){
        if (parseArgs.containsKey("-log")){
            String path = parseArgs.get("-log");
            String[] allContent = readFile(path);//读取文件夹下的文件
            String[] result = match(allContent);//对内容分析返回结果

        }
        else {
            System.out.println("必须附带log参数！");
            return;
        }
        if (parseArgs.containsKey("-out")){

        }
        else {
            System.out.println("必须附带out参数！");
            return;
        }
        if (parseArgs.containsKey("-date")){

        }
        if (parseArgs.containsKey("-type")){
            String type = parseArgs.get("-type");
            switch (type){
                case "ip":break;
                case "sp":break;
                case "cure":break;
                case "dead":break;
                default:
            }
        }
        if (parseArgs.containsKey("-province")){

        }
    }

    //对读取到的内容正则匹配
    private static String[] match(String[] allContent){
        String[] result = new String[allContent.length];
        try{
            for(int i = 0; i<allContent.length; i++){
                BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(allContent[i].getBytes())));
                String s;
                ArrayList<String> list = new ArrayList<String>();
                while((s = br.readLine()) != null){
                    Pattern p1 = Pattern.compile("(.*) 新增 感染患者 (\\d*)人");
                    Pattern p2 = Pattern.compile("(.*) 新增 疑似患者 (\\d*)人");
                    Pattern p3 = Pattern.compile("(.*) 感染患者 流入 (.*) (\\d*)人");
                    Pattern p4 = Pattern.compile("(.*) 疑似患者 流入 (.*) (\\d*)人");
                    Pattern p5 = Pattern.compile("(.*) 死亡 (\\d*)人");
                    Pattern p6 = Pattern.compile("(.*) 治愈 (\\d*)人");
                    Pattern p7 = Pattern.compile("(.*) 疑似患者 确诊感染 (\\d*)人");
                    Pattern p8 = Pattern.compile("(.*) 排除 疑似患者 (\\d*)人");
                    Matcher m1 = p1.matcher(s);
                    Matcher m2 = p2.matcher(s);
                    Matcher m3 = p3.matcher(s);
                    Matcher m4 = p4.matcher(s);
                    Matcher m5 = p5.matcher(s);
                    Matcher m6 = p6.matcher(s);
                    Matcher m7 = p7.matcher(s);
                    Matcher m8 = p8.matcher(s);
                    while(m1.find()){
                        System.out.println(m1.group(1)+m1.group(2));
                    }
                    while(m2.find()){
                        System.out.println(m2.group(1)+m2.group(2));
                    }
                    while(m3.find()){
                        System.out.println(m3.group(1)+m3.group(2)+m3.group(3));
                    }
                    while(m4.find()){
                        System.out.println(m4.group(1)+m4.group(2)+m4.group(3));
                    }
                    while(m5.find()){
                        System.out.println(m5.group(1)+m5.group(2));
                    }
                    while(m6.find()){
                        System.out.println(m6.group(1)+m6.group(2));
                    }
                    while(m7.find()){
                        System.out.println(m7.group(1)+m7.group(2));
                    }
                    while(m8.find()){
                        System.out.println(m8.group(1)+m8.group(2));
                    }
            }
                /*Pattern p = Pattern.compile(str1, Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(s);
                while(m.find()){
                    list.add(m.group());
                }*/
            }
            //System.out.println(list);

        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }
    //读取目录下的日志
    private static String[] readFile(String path){
        File all = new File(path);
        List allPath = getFile(all);
        String[] allContent = new String[allPath.size()];
        for (int i = 0; i < allPath.size(); i++){
            File file = new File(String.valueOf(allPath.get(i)));
            String content = txt2String(file);
            allContent[i] = content;
            //System.out.println(allContent[i]);
        }
        return allContent;
    }
    //txt转String
    private static String txt2String(File file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null && !s.startsWith("//")) {// 使用readLine方法，一次读一行
                result.append(System.lineSeparator() + s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
    // 读取文件夹下所有文件名
    private static List getFile(File file) {
        List listLocal = new ArrayList<>();
        if (file != null) {
            File[] f = file.listFiles();
            if (f != null) {
                for (int i = 0; i < f.length; i++) {
                    //getFile(f[i]);
                    String str = String.valueOf(f[i]);
                    String str1 = str.substring(str.length() - 18, str.length() - 8);
                    String str2 = str.substring(str.length() - 8);
                    if(isValidDate(str1) && str2.matches(".log.txt")){//判断文件名是否符合标准
                        listLocal.add(f[i]);
                    }
                }
            }
        }
        return listLocal;
    }
    //判断字符串是否为日期格式
    private static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            format.setLenient(false);
            format.parse(str);
        } catch (Exception e) {
            convertSuccess = false;
        }
        return convertSuccess;
    }
    public static void main(String[] args) {
        String cmdLine = "list -date -log C:/Users/ASUS/Documents/GitHub/InfectStatistic-main/221701429/log -out D:/output.txt";
        args = cmdLine.split(" ");
        HashMap<String, String> parseArgs = parseArgs(args);
        //System.out.println(parseArgs);

        func(parseArgs);
    }
}
