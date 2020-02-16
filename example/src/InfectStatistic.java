import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * InfectStatistic
 * FINISHED
 *
 * @author 221701312 张庭博
 * @version 1.9
 * @since 2020-02-11
 */
class InfectStatistic {
    public static void main(String[] args)
    {
        CommandIdentity cmdit=new CommandIdentity(args);
        /*cmdit.PrintCommand();*/
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
    private static String REGEX_CHINESE = "[\u4e00-\u9fa5]";/* 中文正则*/
    ArrayList<String> FileNames = new ArrayList<>();
    ArrayList<String> FileContent = new ArrayList<>();

    public FileManager(){

    }
    public int ReadFile(String FilePath,String dateparam){
        File f = new File(FilePath);
        int stopPoint=0;/*判断读取哪些文件的断点*/
        int flag=0;/*判断日期是否合法*/
        String stopDate = dateparam + ".log.txt";
        if(f.isDirectory()) {
            String[] temp = f.list();
            for(int i=0;i<temp.length;i++)
                FileNames.add(temp[i]);
        }
        else {
            System.out.println("log文件目录错误！");
            return 0;
        }
        /*找到断点*/
        if(dateparam.equals(""))
            stopPoint=FileNames.size();
        else{
            for(int i=0;i<FileNames.size();i++){
                if(FileNames.get(i).compareTo(stopDate) == 0){
                    stopPoint=i;
                    flag=1;
                }
            }
        }

        if(dateparam=="") {
            flag = 1;
            stopPoint=FileNames.size();
        }
        if(flag==0){
            System.out.println("输入的日期超出范围！");
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
                        FileContent.add(line);
                }
            }
        } catch (Exception e) {

        }
        GetStatistic(FileContent);
        return 1;
    }

    public void WriteFile(String FileName,ArrayList<String> typeparam,ArrayList<String> provinceparam){
        File file = new File(FileName);
        FileWriter fileWriter = null;
        try {
            if (!file.exists())
                file.createNewFile();
            fileWriter = new FileWriter(file, true);

            if (provinceparam.size() == 0) {/*-province没有参数*/
                String s = GetStringByType(typeparam,"全国");
                fileWriter.write(s);
                for (int i = 0; i < 31; i++) {
                    if (IsVisited[i]) {
                        String st = GetStringByType(typeparam,provinces[i]);
                        fileWriter.write(st);
                    }
                }
            }
            if(provinceparam.size() > 0) {/*-province有参数*/
                for(int i=0;i<provinceparam.size();i++){
                    String s=GetStringByType(typeparam,provinceparam.get(i));
                    fileWriter.write(s);
                }
            }
            fileWriter.write("//该文档并非真实数据，仅供测试使用\n");
            fileWriter.flush();
            fileWriter.close();
        }catch (Exception e){

        }
        finally {
            try {
                fileWriter.close();
            }catch (Exception ee){

            }
        }
    }

    public void GetStatistic(ArrayList<String> FileContent){
        for(int i=0;i<FileContent.size();i++){
            String[] temp=FileContent.get(i).split(" ");
            DealStrings(temp);
        }
    }

    public String GetStringByType(ArrayList<String> type,String province){
        String result="";
        int index=0;
        for(int i=0;i<31;i++){
            if(province.equals(provinces[i]))
                index=i;
        }
        result+=province + " ";
        if(province.equals("全国")) {
            if(type.size()!=0) {
                for (int i = 0; i < type.size(); i++) {
                    if (type.get(i).equals("ip")) {
                        result += "感染患者" + AllIP + "人 ";
                    }
                    if (type.get(i).equals("sp")) {
                        result += "疑似患者" + AllSP + "人 ";
                    }
                    if (type.get(i).equals("cure")) {
                        result += "治愈" + AllCP + "人 ";
                    }
                    if (type.get(i).equals("dead")) {
                        result += "死亡" + AllDP + "人 ";
                    }
                }
            }
            else{
                result+=String.format("感染患者%d人 疑似患者%d人 治愈%d人 死亡%d人",
                        AllIP,AllSP,AllCP,AllDP);
            }
            return result + "\n";
        }
        if(type.size()==0){
            result +=String.format("感染患者%d人 疑似患者%d人 治愈%d人 死亡%d人",
                    InfectedPatients[index], SuspectedPatients[index],
                    CurePatients[index], DeadPatients[index]);
        }
        else {
            for (int i = 0; i < type.size(); i++) {
                if (type.get(i).equals("ip")) {
                    result += "感染患者" + InfectedPatients[index] + "人 ";
                }
                if (type.get(i).equals("sp")) {
                    result += "疑似患者" + SuspectedPatients[index] + "人 ";
                }
                if (type.get(i).equals("cure")) {
                    result += "治愈" + CurePatients[index] + "人 ";
                }
                if (type.get(i).equals("dead")) {
                    result += "死亡" + DeadPatients[index] + "人 ";
                }
            }
        }
        return result + "\n";
    }

    public void DealStrings(String[] str){
        int index = 0;
        int increase;
        String tes=str[str.length-1];
        /*除去中文字符*/
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
        for(int i=0;i< command.length;i++)
            System.out.print(command[i]);
        System.out.println();
    }
    public void Identify(){
        int logpos=0,outpos=0,datepos=0,typepos=0,provincepos=0;
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
                    logpos = i;
                    break;
                }
            }
            logparam=command[logpos+1];
            /*out命令位置获取和参数*/
            for(int i=0;i<command.length;i++) {
                if(command[i].equals("-out")) {
                    outpos = i;
                    break;
                }
            }
            outparam=command[outpos+1];
            /*date命令位置获取和参数*/
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
            /*type命令位置获取和参数*/
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
            /*province命令位置获取和参数*/
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
        }
        int result;
        FileManager fm = new FileManager();
        result = fm.ReadFile(logparam,dateparam);
        if(result==1)
            fm.WriteFile(outparam,typeparam,provinceparam);
    }
}
