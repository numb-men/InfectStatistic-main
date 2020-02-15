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
                    arguments.put(key,arr);
                    ParsingValue.clear();
                }
                str = arg;
            }
            else {
                arrValue.add(arg);
            }
        }
        if(str != null){
            String[] argsValue = new String[ParsingValue.size()];
            ParsingValue.toArray(argsValue);
            arguments.put(key,argsValue);
            ParsingValue.clear();
        }
        return arguments;
    }
    /**
     * TODO
     * 日志中每一行statement的拆分,infestorName,ip,sp,cure,dead
     * @author hmx1
     * @version 1.0.0
     */
    static class statement{
        private String infestorName;
        private int ip;//感染
        private int sp;//疑似
        private int cure;//治愈
        private int dead;//死亡
        statement(String infestorName, int ip, int sp, int cure, int dead){
            this.infestorName = infestorName;
            this.ip = ip;
            this.sp = sp;
            this.cure = cure;
            this.dead = dead;
        }
        public String getInfestorName(){
            return infestorName;
        }
        public void setInfestorName(String infestorName) {
            this.infestorName = infestorName;
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
            return infestorName+" 感染患者" + ip + "人" + " 疑似患者" + sp + "人" + " 治愈" + cure + "人" + " 死亡" + dead + "人";
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
     * 清空文件内容
     * @author hmx1
     * @version 1.0.0
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
     * 将字符串写入文件
     * @author hmx1
     * @version 1.0.0
     * @since 2020.2.11
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
     * @since 2020.2.11
     */
    public static String[] readFile(String path,String date) throws ParseException {
        List allFilePath = getFileName(path,date);
        String[] allContent = new String[allPath.size()];
        for (int i = 0; i < allPath.size(); i++){
            File file = new File(String.valueOf(allPath.get(i)));
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
     * @since 2020.2.11
     */
    public static List getFileName(String path, String date) throws ParseException {
        List listFileName = new ArrayList<>();
        File file = new File(path);
        if (file.isDirectory()) {
            // 获取路径下的所有文件
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                // 如果还是文件夹 递归获取里面的文件与文件夹
                if (files[i].isDirectory()) {
                    //System.out.println("目录：" + files[i].getPath());
                    getFiles(files[i].getPath());
                } else {
                    listFileName.add(files[i].getPath());
                    //System.out.println("文件：" + files[i].getPath());
                }
            }
        } else {
            listFileName.add(file.getPath());
            //System.out.println("文件：" + file.getPath());
        }
        return listFileName;
    }
    /**
     * TODO
     * 判断字符串是否为日期格式yyyy-mm-dd
     * @author hmx1
     * @version 1.0.0
     * @since 2020.2.10
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
        return convertSuccess;
    }















    public static void main(String[] args) {
        System.out.println("helloworld");
    }
}
