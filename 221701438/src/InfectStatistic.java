import java.io.*;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
/**
 * InfectStatistic
 *
 * @author hmx1
 * @version 1.0.0
 * @since 2020.2.10
 */
class InfectStatistic {
    /**
     * TODO
     * 建立Hashmap，将命令行的参数与参数值分开
     * @author hmx1
     * @version 1.0.0
     */
    public static HashMap<String, String[]> ParsingCommandLine(String[] args) {
        HashMap<String, String[]> arguments = new HashMap<String, String[]>();
        ArrayList<String> ParsingValue = new ArrayList<>();
        String str = null;
        //如果命令不是list，那么报错
        if ((args.length == 0) || (!args[0].equals("list"))) {
            System.out.println("请输入正确的命令list！");
            return arguments;
        }
        for (int i = 1; i<args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("-")){
                if (str != null){
                    String[] arr = new String[ParsingValue.size()];
                    ParsingValue.toArray(arr);
                    arguments.put(str,arr);
                    ParsingValue.clear();
                }
                str = arg;
            }
            else {
                ParsingValue.add(arg);
            }
        }
        if(str != null){
            String[] argsValue = new String[ParsingValue.size()];
            ParsingValue.toArray(argsValue);
            arguments.put(str,argsValue);
            ParsingValue.clear();
        }
        return arguments;
    }
    /**
     * TODO
     * 正則匹配工具类
     * @author hmx1
     * @version 1.0.0
     */
    static class RegularMatch {
        Pattern p1 = Pattern.compile("(.*) 新增 感染患者 (\\d*)人");
        Pattern p2 = Pattern.compile("(.*) 新增 疑似患者 (\\d*)人");
        Pattern p3 = Pattern.compile("(.*) 感染患者 流入 (.*) (\\d*)人");
        Pattern p4 = Pattern.compile("(.*) 疑似患者 流入 (.*) (\\d*)人");
        Pattern p5 = Pattern.compile("(.*) 死亡 (\\d*)人");
        Pattern p6 = Pattern.compile("(.*) 治愈 (\\d*)人");
        Pattern p7 = Pattern.compile("(.*) 疑似患者 确诊感染 (\\d*)人");
        Pattern p8 = Pattern.compile("(.*) 排除 疑似患者 (\\d*)人");
        /**
         * TODO
         * 处理：新增感染者
         * @author hmx1
         * @version 1.0.0
         */
        public static void addIp (Matcher m, ArrayList<statement> list) {
            if(!isListName(list, m.group(1))){
                statement st = new statement(m.group(1),Integer.parseInt(m.group(2)),0,0,0);
                list.add(st);
            }
            else{
                for(int j = 0; j < list.size(); j++){
                    if(list.get(j).getprovinceName().equals(m.group(1))){
                        list.get(j).setIp(Integer.parseInt(m.group(2)) + list.get(j).getIp());//修改该省份的感染患者人数
                    }
                }
            }
        }
        /**
         * TODO
         * 处理：新增疑似患者
         * @author hmx1
         * @version 1.0.0
         */
        public static void addSp (Matcher m, ArrayList<statement> list) {
            if(!isListName(list, m.group(1))){
                statement st =new statement(m.group(1),0,Integer.parseInt(m.group(2)),0,0);
                list.add(st);
            }
            else{
                for(int j = 0; j < list.size(); j++){
                    if(list.get(j).getprovinceName().equals(m.group(1))){
                        list.get(j).setSp(Integer.parseInt(m.group(2)) + list.get(j).getSp());//修改该省份的疑似患者人数
                    }
                }
            }
        }
        /**
         * TODO
         * 处理：死亡
         * @author hmx1
         * @version 1.0.0
         */
        public static void dead (Matcher m, ArrayList<statement> list) {
            if(!isListName(list, m.group(1))){
                System.out.println("该省份无感染患者，无法死亡！");
            }
            else{
                for(int j = 0; j < list.size(); j++){
                    if(list.get(j).getprovinceName().equals(m.group(1))){
                        list.get(j).setIp(list.get(j).getIp() - Integer.parseInt(m.group(2)));//修改该省份的感染患者人数
                        list.get(j).setDead(Integer.parseInt(m.group(2)) + list.get(j).getDead());//修改该省份的死亡人数
                    }
                }
            }
        }
        /**
         * TODO
         * 处理：治愈
         * @author hmx1
         * @version 1.0.0
         */
        public static void cure (Matcher m, ArrayList<statement> list) {
            if(!isListName(list, m.group(1))){
                System.out.println("该省份无感染患者，无法治愈！");
            }
            else{
                for(int j = 0; j < list.size(); j++){
                    if(list.get(j).getprovinceName().equals(m.group(1))){
                        list.get(j).setIp(list.get(j).getIp() - Integer.parseInt(m.group(2)));//修改该省份的感染患者人数
                        list.get(j).setCure(Integer.parseInt(m.group(2)) + list.get(j).getCure());//修改该省份的治愈人数
                    }
                }
            }
        }
        /**
         * TODO
         * 处理：感染患者迁移
         * @author hmx1
         * @version 1.0.0
         */
        public static void ipTransfer (Matcher m, ArrayList<statement> list) {
            if(!isListName(list, m.group(1))){
                System.out.println("该省份不存在，无法流出！");
            }
            else if(!isListName(list, m.group(2))) {
                statement st =new statement(m.group(2),Integer.parseInt(m.group(3)),0,0,0);
                list.add(st);
            }
            else{
                for(int j = 0; j < list.size(); j++){
                    if(list.get(j).getprovinceName().equals(m.group(1))){
                        list.get(j).setIp(list.get(j).getIp() - Integer.parseInt(m.group(3)));//修改流出省的感染患者人数
                    }
                    if(list.get(j).getprovinceName().equals(m.group(2))){
                        list.get(j).setIp(list.get(j).getIp() + Integer.parseInt(m.group(3)));//修改流入省的感染患者人数
                    }
                }
            }
        }
        /**
         * TODO
         * 处理：疑似患者确诊感染
         * @author hmx1
         * @version 1.0.0
         */
        public static void spToIp (Matcher m, ArrayList<statement> list) {
            if (!isListName(list, m.group(1))) {
                System.out.println("该省份无疑似患者，无法确诊！");
            } else {
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).getprovinceName().equals(m.group(1))) {
                        list.get(j).setIp(Integer.parseInt(m.group(2)) + list.get(j).getIp());//修改该省份的感染患者人数
                        list.get(j).setSp(list.get(j).getSp() - Integer.parseInt(m.group(2)));//修改该省份的疑似患者人数
                    }
                }
            }
        }
        /**
         * TODO
         * 处理：排除疑似患者
         * @author hmx1
         * @version 1.0.0
         */
        public static void deleteSp(Matcher m, ArrayList<statement> list) {
            if(!isListName(list, m.group(1))){
                System.out.println("该省份无疑似患者，无法排除！");
            }
            else{
                for(int j = 0; j < list.size(); j++){
                    if(list.get(j).getprovinceName().equals(m.group(1))){
                        //修改该省份的疑似患者人数
                        list.get(j).setSp(list.get(j).getSp() - Integer.parseInt(m.group(2)));
                    }
                }
            }
        }
        /**
         * TODO
         * 处理：疑似者迁移
         * @author hmx1
         * @version 1.0.0
         */
        public static void  spTransfer(Matcher m, ArrayList<statement> list) {
            if(!isListName(list, m.group(1))){
                System.out.println("该省份不存在，无法流出！");
            }
            else if(!isListName(list, m.group(2))) {
                statement st =new statement(m.group(2),0,Integer.parseInt(m.group(3)),0,0);
                list.add(st);
            }
            else{
                for(int j = 0; j < list.size(); j++){
                    if(list.get(j).getprovinceName().equals(m.group(1))){
                        list.get(j).setSp(list.get(j).getSp() - Integer.parseInt(m.group(3)));//修改流出省的疑似患者人数
                    }
                    if(list.get(j).getprovinceName().equals(m.group(2))){
                        list.get(j).setSp(list.get(j).getSp() + Integer.parseInt(m.group(3)));//修改流入省的疑似患者人数
                    }
                }
            }
        }
        /**
         * TODO
         * 对读取到的内容正则匹配
         * @author hmx1
         * @version 1.0.0
         */
        public static ArrayList<statement> match(String[] allContent){
            RegularMatch regularMatch = null;
            ArrayList<statement> list = new ArrayList<>();
            try{
                for(int i = 0; i<allContent.length; i++){
                    BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(allContent[i].getBytes())));
                    String s;
                    while((s = br.readLine()) != null){
                        Matcher m1 = regularMatch.p1.matcher(s);
                        Matcher m2 = regularMatch.p2.matcher(s);
                        Matcher m3 = regularMatch.p3.matcher(s);
                        Matcher m4 = regularMatch.p4.matcher(s);
                        Matcher m5 = regularMatch.p5.matcher(s);
                        Matcher m6 = regularMatch.p6.matcher(s);
                        Matcher m7 = regularMatch.p7.matcher(s);
                        Matcher m8 = regularMatch.p8.matcher(s);
                        //新增感染患者
                        while(m1.find()){
                            addIp(m1, list);
                        }
                        //新增疑似患者
                        while(m2.find()){
                            addSp(m2, list);
                        }
                        //感染患者迁移
                        while(m3.find()){
                            ipTransfer(m3, list);
                        }
                        //疑似患者迁移
                        while(m4.find()){
                            spTransfer(m4, list);
                        }
                        //死亡
                        while(m5.find()){
                            dead(m5, list);
                        }
                        //治愈
                        while(m6.find()){
                            cure(m6, list);
                        }
                        //疑似患者确诊感染
                        while(m7.find()){
                            spToIp(m7, list);
                        }
                        //排除疑似患者
                        while(m8.find()){
                            deleteSp(m8, list);
                        }
                    }
                }
                //计算全国情况
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
                statement country = new statement("全国", allIp, allSp, allCure, allDead );
                list.add(country);
            }catch (Exception e){
                e.printStackTrace();
            }
            return list;
        }
    }
    /**
     * TODO
     * 判断list中是否含有指定provinceName属性的province
     * @author hmx1
     * @version 1.0.0
     */
    private static boolean isListName(ArrayList<statement> list, String name){
        for(int i = 0; i < list.size(); i++){
            if (list.get(i).getprovinceName().equals(name)){
                return true;
            }
        }
        return false;
    }
    /**
     * TODO
     * 日志中每一行statement的拆分,infestorName,ip,sp,cure,dead
     * @author hmx1
     * @version 1.0.0
     */
    static class statement{
        private String provinceName;
        private int ip;//感染
        private int sp;//疑似
        private int cure;//治愈
        private int dead;//死亡
        statement(String provinceName, int ip, int sp, int cure, int dead){
            this.provinceName = provinceName;
            this.ip = ip;
            this.sp = sp;
            this.cure = cure;
            this.dead = dead;
        }
        public String getprovinceName(){
            return provinceName;
        }
        public void setprovinceName(String infestorName) {
            this.provinceName = infestorName;
        }
        public int getIp(){
            return ip;
        }
        public void setIp(int ip) {
            this.ip = ip;
        }
        public int getSp(){
            return sp;
        }
        public void setSp(int sp) {
            this.sp = sp;
        }
        public int getCure(){
            return cure;
        }
        public void setCure(int cure) {
            this.cure = cure;
        }
        public int getDead() {
            return dead;
        }
        public void setDead(int dead) {
            this.dead = dead;
        }
        public String printStatement(){
            return provinceName+" 感染患者" + ip + "人" + " 疑似患者" + sp + "人" + " 治愈" + cure + "人" + " 死亡" + dead + "人";
        }
        public String printIp() {
            return " 感染患者" + ip + "人";
        }
        public String printSp() {
            return " 疑似患者" + sp + "人";
        }
        public String printCure(){
            return " 治愈" + cure + "人";
        }
        public String printDead(){
            return " 死亡" + dead + "人";
        }
    }
    /**
     * TODO
     * 清空文件内容,func
     * @author hmx1
     */
    public static void clearInfoForFile(String fileName) {
        File file =new File(fileName);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * TODO
     * 将字符串写入文件,func
     * @author hmx1
     * @version 1.0.0
     */
    public static void WriteStringToFile(String fileName, String str) {
        try {
            File file = new File(fileName);
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.println(str);
            ps.append("\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * TODO
     * 读取目录下的所有日志
     * @author hmx1
     * @version 1.0.0
     */
    public static String[] readFile(String path,String date) throws ParseException, IOException {
        List allFilePath = getFileName(path,date);
        String[] allContent = new String[allFilePath.size()];
        for (int i = 0; i < allFilePath.size(); i++){
            File file = new File(String.valueOf(allFilePath.get(i)));
            StringBuilder result = new StringBuilder();
            // 构造一个BufferedReader类来读取文件
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            String s = null;
            // 使用readLine方法，一次读一行，并忽略注释行
            while ((s = br.readLine()) != null && !s.startsWith("//")) {
                result.append(System.lineSeparator() + s);
            }
            br.close();
            allContent[i] = result.toString();
        }
        return allContent;
    }
    /**
     * TODO
     * 读取文件夹下所有文件的文件名
     * @author hmx1
     * @version 1.0.0
     */
    public static List getFileName(String path, String date) throws ParseException {
        //检验文件名是否合理
        String str,str1,str2;
        List FileNameList = new ArrayList<>();
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i=0; i<files.length; i++) {
                // 如果还是文件夹 递归获取里面的文件 文件夹
                if (files[i].isDirectory()) {
                    //System.out.println("目录：" + files[i].getPath());
                    getFileName(files[i].getPath(), date);
                } else {
                    //System.out.println("文件：" + files[i].getPath());
                    str = String.valueOf(file);
                    str1 = str.substring(str.length() - 18, str.length() - 8);
                    str2 = str.substring(str.length() - 8);
                    if (isValidDate(str1) && str2.matches(".log.txt") && isBefore(str1, date)) {//判断文件名是否符合标准
                        FileNameList.add(file);
                    }
                }
            }
        }
        else {
            //System.out.println("文件：" + file.getPath());
            str = String.valueOf(file);
            str1 = str.substring(str.length() - 18, str.length() - 8);
            str2 = str.substring(str.length() - 8);
            if (isValidDate(str1) && str2.matches(".log.txt") && isBefore(str1, date)) {//判断文件名是否符合标准
                FileNameList.add(file);
            }
        }

        return FileNameList;
    }
    /**
     * TODO
     * 判断字符串是否为日期格式yyyy-mm-dd
     * @author hmx1
     * @version 1.0.0
     */
    private static boolean isValidDate(String str) {
        boolean flag = true;
        // 日期格式为yyyy-mm-dd；
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            format.setLenient(false);
            format.parse(str);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
    /**
     * TODO
     * 比较日期前后，time1日期比time2前则返回true
     * @author hmx1
     * @version 1.0.0
     */
    private static boolean isBefore(String time1, String time2) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd");
        Date date1 =  simpleDateFormat.parse(time1);
        Date date2 =  simpleDateFormat.parse(time2);
        return !date1.after(date2);
    }


    public static void main(String[] args) {
        System.out.println("helloworld");
    }
}
