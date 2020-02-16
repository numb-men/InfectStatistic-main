import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * InfectStatistic
 * TODO
 *
 * @author 221701312
 * @version 1.5
 * @since 2020-02-11
 */
class InfectStatistic {
    public static void main(String[] args)
    {
        CommandIdentity cmdit=new CommandIdentity(args);
        cmdit.PrintCommand();
        cmdit.Identify();
    }
}
class FileManager{
    public String[] provinces={"安徽","北京","重庆","福建","甘肃","广东","广西","贵州",
            "海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古",
            "宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"};
    public int[] InfectedPatients= new int[31];
    public int[] SuspectedPatients=new int[31];
    public int[] CurePatients=new int[31];
    public int[] DeadPatients=new int[31];
    public boolean[] IsVisited = new boolean[31];
    public int AllIP=0,AllSP=0,AllCP=0,AllDP=0;
    private static String REGEX_CHINESE = "[\u4e00-\u9fa5]";// 中文正则
    ArrayList<String> fileNames = new ArrayList<>();
    ArrayList<String> fileContent = new ArrayList<>();

    public FileManager(){

    }
    public void ReadFile(String FilePath,String dateparam){
        File f = new File(FilePath);
        int stopPoint=0;//判断读取哪些文件的断点
        int flag=0;//判断日期是否合法
        String stopDate = dateparam + ".log.txt";
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
                if(fileNames.get(i).compareTo(stopDate) == 0){
                    stopPoint=i;
                    flag=1;
                }
            }
        }

        if(dateparam=="") {
            flag = 1;
            stopPoint=fileNames.size();
        }
        if(flag==0){
            System.out.println("输入的日期超出范围！");
            return;
        }

        try {
            for(int i=0;i<=stopPoint;i++) {
                String SingleFile = FilePath + "\\" + fileNames.get(i);
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(SingleFile),"UTF-8"));
                String line = "";
                while((line=br.readLine())!=null) {
                    if(!line.startsWith("/"))
                        fileContent.add(line);
                }
            }
        }catch (Exception e){

        }
        GetStatistic(fileContent);
    }

    public void WriteFile(String FileName,ArrayList<String> typeparam,ArrayList<String> provinceparam){
        File file = new File(FileName);
        FileWriter fileWriter;
        try{
            if(!file.exists())
                file.createNewFile();
            fileWriter=new FileWriter(file,true);

            /*String s=String.format("全国 感染患者%d人 疑似患者%d人 治愈%d人 死亡%d人\n",
                    AllIP,AllSP,AllCP,AllDP);
            fileWriter.write(s);*/

            for(int i=0;i<31;i++){
                if(IsVisited[i]){
                    String st=String.format("%s 感染患者%d人 疑似患者%d人 治愈%d人 死亡%d人\n",
                            provinces[i], InfectedPatients[i], SuspectedPatients[i],
                            CurePatients[i], DeadPatients[i]);
                    fileWriter.write(st);
                }
            }

            fileWriter.flush();
            fileWriter.close();
        }catch (Exception e){

        }
    }

    public void GetStatistic(ArrayList<String> fileContent){
        for(int i=0;i<fileContent.size();i++){
            String[] temp=fileContent.get(i).split(" ");
            DealStrings(temp);
        }
        System.out.printf("全国 感染患者%d人 疑似患者%d人 治愈%d人 死亡%d人\n",
                AllIP,AllSP,AllCP,AllDP);
        for(int i=0;i<31;i++){
            if(IsVisited[i]) {
                System.out.printf("%s 感染患者%d人 疑似患者%d人 治愈%d人 死亡%d人\n",
                        provinces[i], InfectedPatients[i], SuspectedPatients[i],
                        CurePatients[i], DeadPatients[i]);
            }
        }
    }

    public void DealStrings(String[] str){
        int index = 0;
        int increase;
        String tes=str[str.length-1];
        //除去中文字符
        Pattern pat = Pattern.compile(REGEX_CHINESE);
        Matcher mat = pat.matcher(tes);
        increase=Integer.parseInt(mat.replaceAll(""));
        for(int i=0;i<provinces.length;i++){
            if(provinces[i].equals(str[0])){
                index=i;
                break;
            }
        }
        if(str[1].equals("新增")){
            if(str[2].equals("感染患者")){
                InfectedPatients[index]+=increase;
                AllIP+=increase;
            }
            else if(str[2].equals("疑似患者")){
                SuspectedPatients[index]+=increase;
                AllSP+=increase;
            }
            IsVisited[index]=true;
            return;
        }
        else if(str[1].equals("感染患者")){
            InfectedPatients[index]-=increase;
            for(int i=0;i<provinces.length;i++){
                if(provinces[i].equals(str[3])){
                    index=i;
                    break;
                }
            }
            InfectedPatients[index]+=increase;
            IsVisited[index]=true;
            return;
        }
        else if(str[1].equals("疑似患者")){
            if(str[2].equals("流入")){
                SuspectedPatients[index]-=increase;
                for(int i=0;i<provinces.length;i++){
                    if(provinces[i].equals(str[3])){
                        index=i;
                        break;
                    }
                }
                SuspectedPatients[index]+=increase;
                IsVisited[index]=true;
                return;
            }
            else if(str[2].equals("确诊感染")){
                SuspectedPatients[index]-=increase;
                InfectedPatients[index]+=increase;
                AllSP-=increase;
                AllIP+=increase;
                IsVisited[index]=true;
                return;
            }
        }
        else if(str[1].equals("死亡")){
            InfectedPatients[index]-=increase;
            DeadPatients[index]+=increase;
            AllIP-=increase;
            AllDP+=increase;
            IsVisited[index]=true;
            return;
        }
        else if(str[1].equals("治愈")){
            InfectedPatients[index]-=increase;
            CurePatients[index]+=increase;
            AllIP-=increase;
            AllCP+=increase;
            IsVisited[index]=true;
            return;
        }
        else if(str[1].equals("排除")){
            SuspectedPatients[index]-=increase;
            AllSP-=increase;
            IsVisited[index]=true;
            return;
        }
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
            /*System.out.printf("-log的位置为：%d  参数为：%s\n",logpos,logparam);
            System.out.printf("-out的位置为：%d  参数为：%s\n",outpos,outparam);
            System.out.printf("-date的位置为：%d  参数为：%s\n",datepos,dateparam);
            System.out.printf("-type的位置为：%d  参数为：",typepos);
            for(String s:typeparam)
                System.out.print(s + " ");
            System.out.println();
            System.out.printf("-province的位置为：%d  参数为：",provincepos);
            for(String s:provinceparam)
                System.out.print(s + " ");
            System.out.println();*/
        }
        FileManager fm = new FileManager();
        fm.ReadFile(logparam,dateparam);
        fm.WriteFile(outparam,typeparam,provinceparam);
    }
}
