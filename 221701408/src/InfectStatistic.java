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
                    if (!line.startsWith("/"))
                        FileContent.add(line);//指定日期前所有日志全输入filecontent
                }
            }
        } catch (Exception e) {

        }
        //GetStatistic(FileContent);
        return 1;
    }

}
