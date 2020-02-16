import java.io.*;
import java.util.ArrayList;
/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
    public static void main(String[] args)
    {
        CommandIdentity cmdit=new CommandIdentity(args);
        //cmdit.PrintCommand();
        cmdit.Identify();
    }
}
class FileManager{
    public String[] provinces={"安徽","北京","重庆","福建","甘肃","广东","广西","贵州",
            "海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古",
            "宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"};

    public FileManager(){

    }
    public void ReadFile(String FilePath,String dateparam){
        File f = new File(FilePath);
        int stopPoint=0;
        String stopDate = dateparam + ".log.txt";
        ArrayList<String> fileNames = new ArrayList<>();
        ArrayList<String> fileContent = new ArrayList<>();
        if(f.isDirectory()) {
            String[] temp = f.list();
            for(int i=0;i<temp.length;i++)
                fileNames.add(temp[i]);
        }
        else
            System.out.println("log文件目录错误！");
        //找到断点
        if(dateparam.equals(""))
            stopPoint=fileNames.size();
        else{
            for(int i=0;i<fileNames.size();i++){
                if(fileNames.get(i).compareTo(stopDate) <= 0){
                    stopPoint=i;
                }
                /*System.out.printf("此时%s与%s的比较结果为：%d\n",fileNames.get(i),stopDate
                        ,fileNames.get(i).compareTo(stopDate));*/
            }
        }
        try {
            for(int i=0;i<=stopPoint;i++) {
                String SingleFile = FilePath + "\\" + fileNames.get(i);
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(SingleFile),"UTF-8"));
                String line = "";
                while((line=br.readLine())!=null)
                    fileContent.add(line);
            }
        }catch (Exception e){

        }
    }
    public void GetStatistic(){

    }
}
class CommandIdentity{
    public String[] command;
    public CommandIdentity(String[] x){
        command = x;
    }
    public void PrintCommand(){
        /*for(String s:command){
            System.out.print(s);
        }*/
        for(int i=0;i< command.length;i++)
            System.out.print(command[i]);
        System.out.println();
    }
    public void Identify(){
        int logpos=0,outpos=0,datepos=0,typepos=0,provincepos=0;
        ArrayList<String > typeparam=new ArrayList<String>(50);
        ArrayList<String > provinceparam = new ArrayList<String>(50);
        String logparam="",outparam="",dateparam="";
        if(!command[0].equals("list"))
            System.out.println("命令格式错误！");
        else{
            //log命令位置获取和参数
            for(int i=0;i<command.length;i++) {
                if (command[i].equals("-log")) {
                    logpos = i;
                    break;
                }
            }
            logparam=command[logpos+1];
            //out命令位置获取和参数
            for(int i=0;i<command.length;i++) {
                if(command[i].equals("-out")) {
                    outpos = i;
                    break;
                }
            }
            outparam=command[outpos+1];
            //date命令位置获取和参数
            for(int i=0;i<command.length;i++) {
                if (command[i].equals("-date")) {
                    datepos = i;
                    break;
                }
            }
            if(command[datepos+1].startsWith("-"))
                dateparam="";
            else
                dateparam=command[datepos+1];
            //type命令位置获取和参数
            for(int i=0;i<command.length;i++) {
                if (command[i].equals("-type")) {
                    typepos = i;
                    break;
                }
            }
            for(int i=typepos+1;i<command.length;i++){
                if(command[i].startsWith("-"))
                    break;
                else
                    typeparam.add(command[i]);
            }
            //province命令位置获取和参数
            for(int i=0;i<command.length;i++) {
                if (command[i].equals("-province")) {
                    provincepos = i;
                    break;
                }
            }
            for(int i=provincepos+1;i<command.length;i++){
                if(command[i].startsWith("-"))
                    break;
                else
                    provinceparam.add(command[i]);
            }
            System.out.printf("-log的位置为：%d  参数为：%s\n",logpos,logparam);
            System.out.printf("-out的位置为：%d  参数为：%s\n",outpos,outparam);
            System.out.printf("-date的位置为：%d  参数为：%s\n",datepos,dateparam);
            System.out.printf("-type的位置为：%d  参数为：",typepos);
            for(String s:typeparam)
                System.out.print(s + " ");
            System.out.println();
            System.out.printf("-province的位置为：%d  参数为：",provincepos);
            for(String s:provinceparam)
                System.out.print(s + " ");
            System.out.println();
        }
        FileManager fm = new FileManager();
        fm.ReadFile(logparam,dateparam);
    }
}
