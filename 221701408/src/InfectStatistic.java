/**
 * InfectStatistic
 * TODO
 *
 * @author 陈怡
 * @version 1.8
 * @since 2020.2.15
 */

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import java.util.regex.*;

/**
 *
 *主函数，用作测试及java命令行命令
 */
class InfectStatistic {
    public static void main(String[] args)
    {
        CmdHandle cmd=new CmdHandle(args);
        cmd.GetCmdParam();

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
     * 获得各命令参数
     */
    public void GetCmdParam(){
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
                    dateparam=command[dateorder+1];
                    break;
                }
            }

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
        FileHandle f = new FileHandle();
        f.ReadFile(logparam,dateparam);
        f.WriteFile(outparam,typeparam,provinceparam);

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
    public boolean[] IsProvince = new boolean[31];
    public int AllIP=0,AllSP=0,AllCURE=0,AllDEAD=0;
    ArrayList<String> FileNames = new ArrayList<>();//日志名字
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
            System.out.println("日期超出范围");
            return 0;
        }
        try {
            for (int i = 0; i <= stopPoint; i++) {
                String Filename = FilePath + "\\" + FileNames.get(i);
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(Filename), "UTF-8"));
                String line = "";
                while ((line = br.readLine()) != null) {
                    if (!line.startsWith("//"))
                        DataProcessing(line);
                }
            }
        } catch (Exception e) {

        }

        return 1;
    }
    /*
     * 数据处理
     *
     */
    public void DataProcessing(String line) {

        String pattern1 = "(\\S+) 新增 感染患者 (\\d+)人";
        String pattern2 = "(\\S+) 新增 疑似患者 (\\d+)人";
        String pattern3 = "(\\S+) 治愈 (\\d+)人";
        String pattern4 = "(\\S+) 死亡 (\\d+)人";
        String pattern5 = "(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
        String pattern6 = "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
        String pattern7 = "(\\S+) 疑似患者 确诊感染 (\\d+)人";
        String pattern8 = "(\\S+) 排除 疑似患者 (\\d+)人";
        boolean isMatch1 = Pattern.matches(pattern1, line);
        boolean isMatch2 = Pattern.matches(pattern2, line);
        boolean isMatch3 = Pattern.matches(pattern3, line);
        boolean isMatch4 = Pattern.matches(pattern4, line);
        boolean isMatch5 = Pattern.matches(pattern5, line);
        boolean isMatch6 = Pattern.matches(pattern6, line);
        boolean isMatch7 = Pattern.matches(pattern7, line);
        boolean isMatch8 = Pattern.matches(pattern8, line);

        if(isMatch1) //新增 感染患者处理
            DealIP(line);
        else if(isMatch2) //新增 疑似患者处理
            DealSP(line) ;
        else if(isMatch3) //新增 治愈患者处理
            DealCure(line);
        else if(isMatch4) //新增 死亡患者处理
            DealDead(line);
        else if(isMatch5) //感染患者 流入处理
            FlowIP(line);
        else if(isMatch6) //疑似患者 流入处理
            FlowSP(line);
        else if(isMatch7) //疑似患者 确诊感染处理
            TurnSPtoIP(line);
        else if(isMatch8) //排除 疑似患者处理
            RemoveSP(line);
    }
        public void DealIP(String line){
            String[] str = line.split(" "); //将字符串以空格分割为多个字符串
            int n = Integer.valueOf(str[3].replace("人", ""));//将人前的数字从字符串类型转化为int类型
            for(int i = 0; i < provinces.length; i++){
                if(str[0].equals(provinces[i])){
                    InfectedPatients[i]+=n;//该省份感染患者人数增加
                    AllIP+=n;//全国感染患者人数增加
                    IsProvince[i] = true;
                    break;
                }
            }
        }
        public void DealSP(String line){
        String[] str = line.split(" "); //将字符串以空格分割为多个字符串
        int n = Integer.valueOf(str[3].replace("人", ""));//将人前的数字从字符串类型转化为int类型
        for(int i = 0; i < provinces.length; i++){
            if(str[0].equals(provinces[i])){
                SuspectedPatients[i]+=n;//该省份感染患者人数增加
                AllSP+=n;//全国感染患者人数增加
                IsProvince[i] = true;
                break;
            }
        }
        }
        public void DealCure(String line){
            String[] str = line.split(" "); //将字符串以空格分割为多个字符串
            int n = Integer.valueOf(str[2].replace("人", ""));//将人前的数字从字符串类型转化为int类型
            for(int i = 0; i < provinces.length; i++){
                if(str[0].equals(provinces[i])){
                    InfectedPatients[i]-=n;//该省份感染患者人数减少
                    CurePatients[i]+=n;//该省份治愈患者人数增加
                    AllIP-=n;//全国感染患者人数减少
                    AllCURE+=n;
                    IsProvince[i] = true;
                    break;
                }
            }
        }
        public void DealDead(String line){
        String[] str = line.split(" "); //将字符串以空格分割为多个字符串
        int n = Integer.valueOf(str[2].replace("人", ""));//将人前的数字从字符串类型转化为int类型
        for(int i = 0; i < provinces.length; i++){
            if(str[0].equals(provinces[i])){
                InfectedPatients[i]-=n;//该省份感染患者人数减少
                DeadPatients[i]+=n;//该省份治愈患者人数增加
                AllIP-=n;//全国感染患者人数减少
                AllDEAD+=n;
                IsProvince[i] = true;
                break;
            }
        }
        }
        public void FlowIP(String line){
            String[] str = line.split(" "); //将字符串以空格分割为多个字符串
            int flag2=0;
            int n = Integer.valueOf(str[4].replace("人", ""));//将人前的数字从字符串类型转化为int类型
            for(int i = 0; i < provinces.length; i++){
                if(str[0].equals(provinces[i])){
                    InfectedPatients[i]-=n;//该省份感染患者人数减少
                    IsProvince[i] = true;
                    flag2+=1;
                    //break;
                }
                if(str[3].equals(provinces[i])){
                    InfectedPatients[i]+=n;//该省份感染患者人数增加
                    IsProvince[i] = true;
                    flag2+=1;
                    //break;
                }
                if(flag2==2) break;
            }
        }
        public void FlowSP(String line){
        String[] str = line.split(" "); //将字符串以空格分割为多个字符串
            int flag3=0;
            int n = Integer.valueOf(str[4].replace("人", ""));//将人前的数字从字符串类型转化为int类型
            for(int i = 0; i < provinces.length; i++){
            if(str[0].equals(provinces[i])){
                SuspectedPatients[i]-=n;//该省份疑似患者人数减少
                IsProvince[i] = true;
                flag3+=1;
                //break;
            }
            if(str[3].equals(provinces[i])){
                SuspectedPatients[i]+=n;//该省份疑似患者人数增加
                IsProvince[i] = true;
                //break;
                flag3+=1;
            }
                if(flag3==2) break;
        }
        }
        public void TurnSPtoIP(String line){
            String[] str = line.split(" "); //将字符串以空格分割为多个字符串
            int n = Integer.valueOf(str[3].replace("人", ""));//将人前的数字从字符串类型转化为int类型
            for(int i = 0; i < provinces.length; i++){
                if(str[0].equals(provinces[i])){
                    SuspectedPatients[i]-=n;//该省份疑似患者人数减少
                    InfectedPatients[i]+=n;//该省份感染患者人数增加
                    AllSP-=n;
                    AllIP+=n;
                    IsProvince[i] = true;
                    break;
                }

            }

        }
        public void RemoveSP(String line){
            String[] str = line.split(" "); //将字符串以空格分割为多个字符串
            int n = Integer.valueOf(str[3].replace("人", ""));//将人前的数字从字符串类型转化为int类型
            for(int i = 0; i < provinces.length; i++){
                if(str[0].equals(provinces[i])){
                    SuspectedPatients[i]-=n;//该省份疑似患者人数减少
                    AllSP-=n;
                    IsProvince[i] = true;
                    break;
                }

            }
        }
    public void WriteFile(String FileName,ArrayList<String> typeparam,ArrayList<String> provinceparam){
        try{

            File file =new File(FileName);
            if(!file.exists()){
                file.createNewFile();
            }
            //FileWriter fileWritter = new FileWriter(file);//直接覆盖原来文件
            BufferedWriter fileWritter= null;
            fileWritter= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")));

            if((provinceparam.size() == 0)&&(typeparam.size() == 0)){
                String result="全国" + " " + String.format("感染患者%d人 疑似患者%d人 治愈%d人 死亡%d人", AllIP,AllSP,AllCURE,AllDEAD)+ "\n";
                fileWritter.write(result);
                for (int i = 0; i < 31; i++) {
                    if (IsProvince[i]) {
                        String st =provinces[i] +" " +String.format("感染患者%d人 疑似患者%d人 治愈%d人 死亡%d人",
                                InfectedPatients[i], SuspectedPatients[i], CurePatients[i], DeadPatients[i])+ "\n";
                        fileWritter.write(st);
                    }
                }
            }
            if((provinceparam.size() > 0)&&(typeparam.size() == 0)){
                for(int i=0;i<provinceparam.size();i++){
                    if(provinceparam.get(i).equals("全国")){
                        String result="全国" + " " + String.format("感染患者%d人 疑似患者%d人 治愈%d人 死亡%d人", AllIP,AllSP,AllCURE,AllDEAD)+ "\n";
                        fileWritter.write(result);
                    }
                }

                for(int j=0;j<31;j++){
                    for(int i=0;i<provinceparam.size();i++){
                        if(provinces[j].equals(provinceparam.get(i))){
                            String st =provinces[j] +" " +String.format("感染患者%d人 疑似患者%d人 治愈%d人 死亡%d人",
                                    InfectedPatients[j], SuspectedPatients[j], CurePatients[j], DeadPatients[j])+ "\n";
                            fileWritter.write(st);
                        }

                    }

                }

            }
            if((provinceparam.size() == 0)&&(typeparam.size() > 0)){
                //fileWritter.write(content);
            }
            if((provinceparam.size() > 0)&&(typeparam.size() > 0)){
                //fileWritter.write(content);
            }
            fileWritter.write("//该文档并非真实数据，仅供测试使用\n");


            fileWritter.flush();

            fileWritter.close();
        }catch(IOException e){

            e.printStackTrace();

        }


    }

}
