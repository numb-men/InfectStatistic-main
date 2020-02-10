import java.io.*;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.*;
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
        private String name;
        private int ip;//感染
        private int sp;//疑似
        private int cure;//治愈
        private int dead;//死亡
        province(String name, int ip, int sp, int cure, int dead){
            this.name = name;
            this.ip = ip;
            this.sp = sp;
            this.cure = cure;
            this.dead = dead;
        }
        public String getName(){
            return name;
        }
        public int getIp(){
            return ip;
        }
        public int getSp(){
            return sp;
        }
        public int getCure(){
            return cure;
        }
        public int getDead() {
            return dead;
        }
        public void setName(String name) {
            this.name = name;
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
        public String printResult(){
            return name+" 感染患者"+ip+"人"+" 疑似患者"+sp+"人"+ " 治愈"+cure+"人"+" 死亡"+dead+"人";
        }
    }
    public static province init(){//初始化province类
        province pro = new province(null,0,0,0,0);
        return pro;
    }
    //建立HashMap，把命令参数和参数值分开
    private static HashMap<String, String[]> parseArgs(String[] args) {
        HashMap<String, String[]> tmp = new HashMap<String, String[]>();
        ArrayList<String> arrValue = new ArrayList<>();
        String key = null;
        //判断命令是否为list
        if(args.length == 0){
            System.out.println("必须含有命令list！");
            return tmp;
        }
        if (!args[0].equals("list")) {
            System.out.println("该命令不存在！");
            return tmp;
        }
        //命令参数和参数值放入HashMap中
        for (int i = 1; i<args.length; i++){
            String arg = args[i];
            if (arg.startsWith("-")){
                if (key != null){
                    String[] argsValue = new String[arrValue.size()];
                    arrValue.toArray(argsValue);
                    tmp.put(key,argsValue);
                    arrValue.clear();
                }
                key = arg;
            }
            else {
                arrValue.add(arg);
            }
        }
        if(key != null){
            String[] argsValue = new String[arrValue.size()];
            arrValue.toArray(argsValue);
            tmp.put(key,argsValue);
            arrValue.clear();
        }
       return tmp;
    }
    //命令对应的行为
    private static void func(HashMap<String, String[]> parseArgs){
        ArrayList<province> list = new ArrayList<>();
        if(parseArgs.isEmpty()){
            return;
        }
        if (parseArgs.containsKey("-log") && parseArgs.get("-log").length == 1){
            String[] path = parseArgs.get("-log");
            String[] allContent = readFile(path[0]);//读取文件夹下的文件
            list = match(allContent);//对内容分析返回结果
            sortProvince(list);
            for(int i = 0;i < list.size(); i++){
                System.out.println(list.get(i).printResult());
            }
        }
        else {
            System.out.println("log参数格式错误！");
            return;
        }
        if (parseArgs.containsKey("-out") && parseArgs.get("-out").length == 1){
            String[] filePath = parseArgs.get("-out");
            initFile(filePath[0]);
            for(int i = 0;i < list.size(); i++){
                writeStringToFile(filePath[0], list.get(i).printResult());//暂时输出的是全部省份信息
            }
            System.out.println("写入文件"+filePath[0]+"成功！");
        }
        else {
            System.out.println("out参数格式错误！");
            return;
        }
        if (parseArgs.containsKey("-date")){

        }
        if (parseArgs.containsKey("-type")){
            String[] type = parseArgs.get("-type");

        }
        if (parseArgs.containsKey("-province")){

        }
    }
    //将省份排序
    private static void sortProvince(ArrayList<province> list){
        //Comparator<String> comparator = Collator.getInstance(Locale.CHINA);
        Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
        /*for(int i = 0; i < list.size() - 1; i++){
            cmp.compare(list.get(i).getName(),list.get(i+1).getName());
        }*/
    }
    //清空文件内容
    private static void initFile(String filePath){
        FileWriter writer;
        try {
            writer = new FileWriter(filePath);
            writer.write("");//清空原文件内容
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //将字符串写入文件
    private static void writeStringToFile(String filePath, String str) {
        try {
            FileWriter fw = new FileWriter(filePath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(str);
            bw.write("\n");
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //判断list中是否含有指定name属性的province
    private static boolean isListName(ArrayList<province> list, String name){
        for(int i = 0; i < list.size(); i++){
            if (list.get(i).getName().equals(name)){
                return true;
            }
        }
        return false;
    }
    //对读取到的内容正则匹配
    private static ArrayList<province> match(String[] allContent){
        ArrayList<province> list = new ArrayList<>();
        try{
            for(int i = 0; i<allContent.length; i++){
                BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(allContent[i].getBytes())));
                String s;
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
                    while(m1.find()){//新增感染患者
                        if(!isListName(list, m1.group(1))){//若该省份不存在则新建
                            province pro = new province(m1.group(1),Integer.parseInt(m1.group(2)),0,0,0);
                            list.add(pro);
                        }
                        else{//存在该省份，则修改ip
                            for(int j = 0; j < list.size(); j++){
                                if(list.get(j).getName().equals(m1.group(1))){
                                    list.get(j).setIp(Integer.parseInt(m1.group(2)) + list.get(j).getIp());//修改该省份的感染患者人数
                                }
                            }
                        }
                    }
                    while(m2.find()){//新增疑似患者
                        if(!isListName(list, m2.group(1))){//若该省份不存在则新建
                            province pro =new province(m2.group(1),0,Integer.parseInt(m2.group(2)),0,0);
                            list.add(pro);
                        }
                        else{//存在该省份，则修改sp
                            for(int j = 0; j < list.size(); j++){
                                if(list.get(j).getName().equals(m2.group(1))){
                                    list.get(j).setSp(Integer.parseInt(m2.group(2)) + list.get(j).getSp());//修改该省份的疑似患者人数
                                }
                            }
                        }
                    }
                    while(m3.find()){//感染患者迁移
                        if(!isListName(list, m3.group(1))){//流出省不存在则输出错误
                            System.out.println("该省份不存在，无法流出！");
                        }
                        else if(!isListName(list, m3.group(2))) {//流入省不存在则新建
                            province pro =new province(m3.group(2),Integer.parseInt(m3.group(3)),0,0,0);
                            list.add(pro);
                        }
                        else{//流出流入省都存在，则修改两省的ip
                            for(int j = 0; j < list.size(); j++){
                                if(list.get(j).getName().equals(m3.group(1))){
                                    list.get(j).setIp(list.get(j).getIp() - Integer.parseInt(m3.group(3)));//修改流出省的感染患者人数
                                }
                                if(list.get(j).getName().equals(m3.group(2))){
                                    list.get(j).setIp(list.get(j).getIp() + Integer.parseInt(m3.group(3)));//修改流入省的感染患者人数
                                }
                            }
                        }
                    }
                    while(m4.find()){//疑似患者迁移
                        if(!isListName(list, m4.group(1))){//流出省不存在则输出错误
                            System.out.println("该省份不存在，无法流出！");
                        }
                        else if(!isListName(list, m4.group(2))) {//流入省不存在则新建
                            province pro =new province(m4.group(2),0,Integer.parseInt(m4.group(3)),0,0);
                            list.add(pro);
                        }
                        else{//流出流入省都存在，则修改两省的sp
                            for(int j = 0; j < list.size(); j++){
                                if(list.get(j).getName().equals(m4.group(1))){
                                    list.get(j).setSp(list.get(j).getSp() - Integer.parseInt(m4.group(3)));//修改流出省的疑似患者人数
                                }
                                if(list.get(j).getName().equals(m4.group(2))){
                                    list.get(j).setSp(list.get(j).getSp() + Integer.parseInt(m4.group(3)));//修改流入省的疑似患者人数
                                }
                            }
                        }
                    }
                    while(m5.find()){//死亡
                        if(!isListName(list, m5.group(1))){//若该省份不存在则输出错误
                            System.out.println("该省份无感染患者，无法死亡！");
                        }
                        else{//存在该省份，则修改ip,dead
                            for(int j = 0; j < list.size(); j++){
                                if(list.get(j).getName().equals(m5.group(1))){
                                    list.get(j).setIp(list.get(j).getIp() - Integer.parseInt(m5.group(2)));//修改该省份的感染患者人数
                                    list.get(j).setDead(Integer.parseInt(m5.group(2)) + list.get(j).getDead());//修改该省份的死亡人数
                                }
                            }
                        }
                    }
                    while(m6.find()){//治愈
                        if(!isListName(list, m6.group(1))){//若该省份不存在则输出错误
                            System.out.println("该省份无感染患者，无法治愈！");
                        }
                        else{//存在该省份，则修改ip,cure
                            for(int j = 0; j < list.size(); j++){
                                if(list.get(j).getName().equals(m6.group(1))){
                                    list.get(j).setIp(list.get(j).getIp() - Integer.parseInt(m6.group(2)));//修改该省份的感染患者人数
                                    list.get(j).setCure(Integer.parseInt(m6.group(2)) + list.get(j).getCure());//修改该省份的治愈人数
                                }
                            }
                        }
                    }
                    while(m7.find()){//疑似患者确诊感染
                        if(!isListName(list, m7.group(1))){//若该省份不存在则输出错误
                            System.out.println("该省份无疑似患者，无法确诊！");
                        }
                        else{//存在该省份，则修改ip,sp
                            for(int j = 0; j < list.size(); j++){
                                if(list.get(j).getName().equals(m7.group(1))){
                                    list.get(j).setIp(Integer.parseInt(m7.group(2)) + list.get(j).getIp());//修改该省份的感染患者人数
                                    list.get(j).setSp(list.get(j).getSp() - Integer.parseInt(m7.group(2)));//修改该省份的疑似患者人数
                                }
                            }
                        }
                    }
                    while(m8.find()){//排除疑似患者
                        if(!isListName(list, m8.group(1))){//若该省份不存在则输出错误
                            System.out.println("该省份无疑似患者，无法排除！");
                        }
                        else{//存在该省份，则修改sp
                            for(int j = 0; j < list.size(); j++){
                                if(list.get(j).getName().equals(m8.group(1))){
                                    list.get(j).setSp(list.get(j).getSp() - Integer.parseInt(m8.group(2)));//修改该省份的疑似患者人数
                                }
                            }
                        }
                    }
                }
            }
            int allIp = 0;
            int allSp = 0;
            int allCure = 0;
            int allDead = 0;
            for(int i = 0; i < list.size(); i++){
                allIp += list.get(i).getIp();
                allSp += list.get(i).getSp();
                allCure += list.get(i).getCure();
                allDead += list.get(i).getDead();
            }
            province country = new province("全国", allIp, allSp, allCure, allDead );
            list.add(country);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
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
        }
        return allContent;
    }
    //txt转String
    private static String txt2String(File file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));// 构造一个BufferedReader类来读取文件
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
        String cmdLine = "list -date -log C:/Users/ASUS/Documents/GitHub/InfectStatistic-main/221701429/log" +
                " -type ip sp -out G:/output.txt";
        args = cmdLine.split(" ");
        HashMap<String, String[]> parseArgs = parseArgs(args);
        func(parseArgs);
    }
}
