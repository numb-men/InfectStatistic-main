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

    }
}

/**
 *
 * 文件处理类
 *
 */
class FileHandle {

}
