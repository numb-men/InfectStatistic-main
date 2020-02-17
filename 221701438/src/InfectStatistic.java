import java.io.*;
import java.io.File;
import java.text.Collator;
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
         * 处理：疑似患者确诊
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
                System.out.println("该省份不存在！");
            }
            else if(!isListName(list, m.group(2))) {
                statement pro =new statement(m.group(2),0,Integer.parseInt(m.group(3)),0,0);
                list.add(pro);
                for(int j = 0; j < list.size(); j++){
                    if(list.get(j).getprovinceName().equals(m.group(1))){
                        list.get(j).setSp(list.get(j).getSp() - Integer.parseInt(m.group(3)));//修改流出省的疑似患者人数
                    }
                }
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
                int allIp = 0, allSp = 0, allCure = 0, allDead = 0;
                for(int i = 0; i < list.size(); i++){
                    allIp += list.get(i).getIp();
                    allSp += list.get(i).getSp();
                    allCure += list.get(i).getCure();
                    allDead += list.get(i).getDead();
                }
                statement st = new statement("全国", allIp, allSp, allCure, allDead );
                list.add(st);
            }catch (Exception e){
                e.printStackTrace();
            }
            return list;
        }
    }
    /**
     * TODO
     * 日志中每一行statement的拆分,provinceName,ip,sp,cure,dead
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
        public static statement init(){//初始化province类
            return new statement(null,0,0,0,0);
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
     * 建立Hashmap，将命令行的参数与参数值分开
     * @author hmx1
     * @version 1.0.0
     */
    public static HashMap<String, String[]> commandLine(String[] args) {
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
     * 命令行解析后功能实现
     * @author hmx1
     * @version 1.0.0
     */
    public static void commandLineParsing(HashMap<String, String[]> commandLine,String[] args) throws ParseException, IOException {
        //完整的省份列表
        ArrayList<statement> list = new ArrayList<>();
        //筛选后的列表
        ArrayList<statement> list1 = new ArrayList<>();
        //用来存放-type后的语句段
        String temp = null;
        RegularMatch regularMatch = null;
        if(commandLine.isEmpty()){
            return;
        }
        if (commandLine.containsKey("-log") && commandLine.get("-log").length == 1){
            if (commandLine.containsKey("-date") && commandLine.get("-date").length == 1){
                String date = commandLine.get("-date")[0];
                if(!isValidDate(date)){
                    System.out.println("date参数值的日期格式不正确,正确格式：yyyy-MM-dd");
                    return;
                }
                String[] path = commandLine.get("-log");
                //读取文件夹下的文件
                String[] allContent = readFile(path[0],date);
                //对内容分析返回结果
                list = regularMatch.match(allContent);
            }
            else if(commandLine.containsKey("-log") && commandLine.get("-date").length > 1){
                System.out.println("date参数值只能存在一个！");
                return;
            }
            else{
                String[] path = commandLine.get("-log");
                Date currentTime = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String currentDate = simpleDateFormat.format(currentTime);
                String[] allContent = readFile(path[0],currentDate);//读取文件夹下的文件
                list = regularMatch.match(allContent);//对内容分析返回结果
            }
            list = sortProvince(list);
        }
        else {
            System.out.println("log参数格式错误！");
            return;
        }
        if (commandLine.containsKey("-province")){
            String[] provinces = commandLine.get("-province");
            for (String province : provinces) {
                int j;
                for (j = 0; j < list.size(); j++) {
                    if (province.equals(list.get(j).getprovinceName())) {//从list中找到和-province参数值相同的省份
                        list1.add(list.get(j));//把选定的省份加入筛选过后的列表里
                        break;
                    }
                    if (j == list.size() - 1) {//list中不存在该省份时新建
                        statement st = new statement(province, 0, 0, 0, 0);
                        list1.add(st);
                    }
                }
            }
        }
        else{
            list1 = list;
        }
        if (commandLine.containsKey("-out") && commandLine.get("-out").length == 1){
            String[] filePath = commandLine.get("-out");
            clearInfoForFile(filePath[0]);
            if (commandLine.containsKey("-type") && commandLine.get("-type").length >= 1){
                String[] type = commandLine.get("-type");
                for(int i = 0; i < type.length; i++){//判断非法参数值
                    if(!type[i].equals("ip") && !type[i].equals("sp") && !type[i].equals("cure") && !type[i].equals("dead")){
                        System.out.println("输入的参数值非法！");
                        return;
                    }
                    for(int j = i + 1; j < type.length; j++){
                        if(type[i].equals(type[j])){
                            System.out.println("输入的参数值不能相同！");
                            return;
                        }
                    }
                }
                for (InfectStatistic.statement province : list1) {
                    temp = province.getprovinceName();
                    for (String s : type) {
                        switch (s) {
                            case "ip":{
                                temp += province.printIp();
                                break;
                            }
                            case "sp": {
                                temp += province.printSp();
                                break;
                            }
                            case "cure":{
                                temp += province.printCure();
                                break;
                            }
                            case "dead": {
                                temp += province.printDead();
                                break;
                            }
                        }
                    }
                    System.out.println("有type:");
                    writeStringToFile(filePath[0], temp);//输出temp拼接成的语句
                }
            }
            else if(commandLine.containsKey("-type") && commandLine.get("-type").length < 1){
                System.out.println("type参数值必须大于等于一个！");
            }
            else{
                for (InfectStatistic.statement st : list1) {
                    System.out.println("没有type:\n");
                    writeStringToFile(filePath[0], st.printStatement());//没有-type则输出完整语句
                }
            }
            System.out.println("仅供测试使用:\n");
            writeStringToFile(filePath[0],"// 该文档并非真实数据，仅供测试使用");
            String args1 = "";
            for (String arg : args) {
                args1 += " " + arg;
            }
            System.out.println("写入文件：\n");
            writeStringToFile(filePath[0], "//"+args1);
            System.out.println("写入文件"+filePath[0]+"成功！");
        }
        else {
            System.out.println("out参数格式错误！");
            return;
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
    public static void writeStringToFile(String fileName, String str) {
        System.out.println(str);
        try {
            File file = new File(fileName);
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.println(str);
            ps.append("\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        FileWriter fwriter = null;
//        try {
//            // true表示不覆盖原来的内容，而是加到文件的后面。若要覆盖原来的内容，直接省略这个参数就好
//            fwriter = new FileWriter(fileName, true);
//            fwriter.write(str);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } finally {
//            try {
//                fwriter.flush();
//                fwriter.close();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
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
            String s;
            // 使用readLine方法，一次读一行，并忽略注释行
            while ((s = br.readLine()) != null && !s.startsWith("//")) {
                result.append(System.lineSeparator() + s);
            }
            br.close();
            allContent[i] = result.toString();
        }
        for (int i=0; i<allContent.length; i++){
            System.out.println(allContent[i]);
            System.out.println("读取成功");
        }
        return allContent;
    }
    /**
     * TODO
     * 读取文件夹下所有文件的文件名
     * @author hmx1
     * @version 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static List getFileName(String path, String date) throws ParseException {
        File file = new File(path);
        List listLocal = new ArrayList<>();
        if (file != null) {
            File[] f = file.listFiles();
            if (f != null) {
                for (File value : f) {
                    String str = String.valueOf(value);
                    String str1 = str.substring(str.length() - 18, str.length() - 8);
                    String str2 = str.substring(str.length() - 8);
                    if (isValidDate(str1) && str2.matches(".log.txt") && compareDate(str1, date)) {
                        listLocal.add(value);
                    }
                }
            }
        }
        return listLocal;
    }
    /**
     * TODO
     * 判断字符串是否为日期格式yyyy-mm-dd
     * @author hmx1
     * @version 1.0.0
     */
    public static boolean isValidDate(String str) {
        boolean flag = true;
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
    public static boolean compareDate(String time1, String time2) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd");
        Date date1 =  simpleDateFormat.parse(time1);
        Date date2 =  simpleDateFormat.parse(time2);
        return !date1.after(date2);
    }
    /**
     * TODO
     * 判断list中是否含有指定provinceName属性的province
     * @author hmx1
     * @version 1.0.0
     */
    public static boolean isListName(ArrayList<statement> list, String name){
        for(int i = 0; i < list.size(); i++){
            if (list.get(i).getprovinceName().equals(name)){
                return true;
            }
        }
        return false;
    }
    /**
     * TODO
     * 对给定省份列表进行排序
     * @author hmx1
     * @version 1.0.0
     */
    public static ArrayList<statement> sortProvince(ArrayList<statement> list){
        ArrayList<statement> newList = new ArrayList<>();
        Comparator<Object> cmp = Collator.getInstance(java.util.Locale.CHINA);
        String[] newArray = new String[list.size() - 1];
        for(int i = 0 ; i < list.size(); i++){
            if(!list.get(i).getprovinceName().equals("全国")){
                newArray[i] = list.get(i).getprovinceName()+" "+list.get(i).getIp()+" "+list.get(i).getSp()+" "+
                        list.get(i).getCure()+" "+list.get(i).getDead();
            }
            else{
                statement st = list.get(i);
                newList.add(st);
            }
        }
        Arrays.sort(newArray,cmp);
        for(int i = 0; i < newArray.length; i++){
            String[] tempArr = newArray[i].split(" ");
            statement st = new statement(tempArr[0],Integer.parseInt(tempArr[1]),Integer.parseInt(tempArr[2]),
                    Integer.parseInt(tempArr[3]),Integer.parseInt(tempArr[4]));
            newList.add(st);
        }
        return newList;
    }

    public static void main(String[] args) throws IOException, ParseException {
        HashMap<String, String[]> commandLine = commandLine(args);
        commandLineParsing(commandLine,args);
    }

}
