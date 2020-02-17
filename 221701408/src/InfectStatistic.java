/**
 * InfectStatistic
 * TODO
 *
 * @author 陈怡
 * @version 1.8
 * @since 2020.2.15
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 *主函数，用作测试及java命令行命令
 */
class InfectStatistic {
    public static void main(String[] args)
    {

    }
}

/**
 *
 *命令处理类
 */
class CmdHandle {
    public String[] command;
    public CmdHandle(String[] x){
        command = x;
    }
    /**
     * 获得各命令位置和参数
     */
    public void GetCmdOrderParam(){
        int logorder=0,outorder=0,dateorder=0,typeorder=0,provinceorder=0;
        ArrayList<String > typeparam=new ArrayList<>();
        ArrayList<String > provinceparam = new ArrayList<>();
        String logparam="",outparam="",dateparam="";
        if(!command[0].equals("list")) {
            System.out.println("命令格式错误！");
            return ;
        }
        else{
            /*log命令位置获取和参数*/
            for(int i=0;i<command.length;i++) {
                if (command[i].equals("-log")) {
                    logorder = i;
                    break;
                }
            }
            logparam=command[logorder+1];
            /*out命令位置获取和参数*/
            for(int i=0;i<command.length;i++) {
                if(command[i].equals("-out")) {
                    outorder = i;
                    break;
                }
            }
            outparam=command[outorder+1];
            /*date命令位置获取和参数*/
            for(int i=0;i<command.length;i++) {
                if (command[i].equals("-date")) {
                    dateorder = i;
                    break;
                }
            }
            dateparam=command[dateorder+1];
            /*type命令位置获取和参数*/
            for(int i=0;i<command.length;i++) {
                if (command[i].equals("-type")) {
                    typeorder = i;
                    break;
                }
            }
            for(int i=typeorder+1;i<command.length;i++){
                if(command[i].startsWith("-"))
                    break;
                else
                    typeparam.add(command[i]);
            }
            /*province命令位置获取和参数*/
            for(int i=0;i<command.length;i++) {
                if (command[i].equals("-province")) {
                    provinceorder = i;
                    break;
                }
            }
            for(int i=provinceorder+1;i<command.length;i++){
                if(command[i].startsWith("-"))
                    break;
                else
                    provinceparam.add(command[i]);
            }
        }
        //FileManager fm = new FileManager();
        //    fm.ReadFile(logparam,dateparam);
        //    fm.WriteFile(outparam,typeparam,provinceparam);

    }
}

/**
 *
 * 文件处理类
 *
 */
class FileHandle {
    public String[] provinces={"安徽","北京","重庆","福建","甘肃","广东","广西","贵州",
            "海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古",
            "宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"};
    public int[] InfectedPatients= new int[31];
    public int[] SuspectedPatients=new int[31];
    public int[] CurePatients=new int[31];
    public int[] DeadPatients=new int[31];
    public boolean[] IsVisited = new boolean[31];
    public int AllIP=0,AllSP=0,AllCP=0,AllDP=0;
    private static String REGEX_CHINESE = "[\u4e00-\u9fa5]";/* 中文正则*/
    ArrayList<String> FileNames = new ArrayList<>();//日志名字
    ArrayList<String> FileContent = new ArrayList<>();//日志内容
    public FileHandle(){

    }
    public int ReadFile(String FilePath,String dateparam){
        File f = new File(FilePath);
        int stopPoint=0;/*判断读取哪些文件的断点*/
        int flag=0;/*判断日期是否合法*/
        String stopDate = dateparam + ".log.txt";
        if(f.isDirectory()) {
            String[] temp = f.list();
            for(int i=0;i<temp.length;i++)
                FileNames.add(temp[i]);//FileName数组中包含所有日志文件名
        }
        /*找到断点*/
        if(dateparam.equals("")) {
            stopPoint = FileNames.size();
            flag=1;
        }
        else{
            for(int i=0;i<FileNames.size();i++){
                if(FileNames.get(i).compareTo(stopDate) == 0){
                    stopPoint=i;
                    flag=1;
                }
            }
        }

        if(flag==0){
            System.out.println("日期超出范围！");
            return 0;
        }
        try {
            for (int i = 0; i <= stopPoint; i++) {
                String SingleFile = FilePath + "\\" + FileNames.get(i);
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(SingleFile), "UTF-8"));
                String line = "";
                while ((line = br.readLine()) != null) {
                    if (!line.startsWith("//"))
                        FileContent.add(line);//指定日期前所有日志全输入filecontent
                }
            }
        } catch (Exception e) {

        }
        //GetStatistic(FileContent);
        return 1;
    }
    /*
     * 文本处理
     *
     */
    public void textProcessing(String string) {
        String pattern1 = "(\\S+) 新增 感染患者 (\\d+)人";
        String pattern2 = "(\\S+) 新增 疑似患者 (\\d+)人";
        String pattern3 = "(\\S+) 治愈 (\\d+)人";
        String pattern4 = "(\\S+) 死亡 (\\d+)人";
        String pattern5 = "(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
        String pattern6 = "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
        String pattern7 = "(\\S+) 疑似患者 确诊感染 (\\d+)人";
        String pattern8 = "(\\S+) 排除 疑似患者 (\\d+)人";
        boolean isMatch1 = Pattern.matches(pattern1, string);
        boolean isMatch2 = Pattern.matches(pattern2, string);
        boolean isMatch3 = Pattern.matches(pattern3, string);
        boolean isMatch4 = Pattern.matches(pattern4, string);
        boolean isMatch5 = Pattern.matches(pattern5, string);
        boolean isMatch6 = Pattern.matches(pattern6, string);
        boolean isMatch7 = Pattern.matches(pattern7, string);
        boolean isMatch8 = Pattern.matches(pattern8, string);

        if(isMatch1) //新增 感染患者处理
            //addIP(string);
        else if(isMatch2) //新增 疑似患者处理
            //addSP(string) ;
        else if(isMatch3) //新增 治愈患者处理
            //addCure(string);
        else if(isMatch4) //新增 死亡患者处理
            //addDead(string);
        else if(isMatch5) //感染患者 流入处理
            //flowIP(string);
        else if(isMatch6) //疑似患者 流入处理
            //flowSP(string);
        else if(isMatch7) //疑似患者 确诊感染处理
            //diagnosisSP(string);
        else if(isMatch8) //排除 疑似患者处理
            //removeSP(string);
    }

}
