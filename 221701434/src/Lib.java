

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lib
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
public class Lib {
}
class Read {
    public String path;
    public People num;
    Date now;
    Read(String path,People a,Date date){
        this.path=path;
        this.num=a;
        this.now=date;
    }
    // Date now = new Date(System.currentTimeMillis());//获取当前日期

    public void getFiles() {
        String Log_Path=path;
        File files = new File(Log_Path);
        File[] filelist = files.listFiles();//获取目录下的所有文件
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//规范日期格式
        String today = sdf.format(now);
        for (int i = 0; i < filelist.length; i++) {
            if (filelist[i].getName().compareTo(today+".log.txt") <= 0)
                ReadLog(Log_Path + filelist[i].getName());
        }
    }

    public void ReadLog(String Log_Path) {
        try {
            File file = new File(Log_Path);//传入log的路径
            String strline=null;//用来存储读出的一行
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            while((strline=bufferedReader.readLine())!=null){//一次读一行
                Dealtxt(strline);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Dealtxt(String a){
        num.province[0]++;
        String addinfect = "(\\S+) 新增 感染患者 (\\d+)人";
        String adddoubt = "(\\S+) 新增 疑似患者 (\\d+)人";
        String addcure = "(\\S+) 治愈 (\\d+)人";
        String adddie = "(\\S+) 死亡 (\\d+)人";
        String changeinfect = "(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
        String changedoubt = "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
        String doubttoinfect = "(\\S+) 疑似患者 确诊感染 (\\d+)人";
        String doubttoclear = "(\\S+) 排除 疑似患者 (\\d+)人";
        Pattern r1=Pattern.compile(addinfect);
        Matcher m1=r1.matcher(a);
        Pattern r2=Pattern.compile(adddoubt);
        Matcher m2=r2.matcher(a);
        Pattern r3=Pattern.compile(addcure);
        Matcher m3=r3.matcher(a);
        Pattern r4=Pattern.compile(adddie);
        Matcher m4=r4.matcher(a);
        Pattern r5=Pattern.compile(changeinfect);
        Matcher m5=r5.matcher(a);
        Pattern r6=Pattern.compile(changedoubt);
        Matcher m6=r6.matcher(a);
        Pattern r7=Pattern.compile(doubttoinfect);
        Matcher m7=r7.matcher(a);
        Pattern r8=Pattern.compile(doubttoclear);
        Matcher m8=r8.matcher(a);
        if(m1.find()) {
            num.province_infectpeople[num.map1.get(m1.group(1))]+=Integer.valueOf(m1.group(2));//该省份
            num.province_infectpeople[0]+=Integer.valueOf(m1.group(2));//全国
            num.province[num.map1.get(m1.group(1))]++;//此次出现的省份+1，表示日志中出现的省份
        }
        else if(m2.find()){
            num.province_doubtpeople[num.map1.get(m2.group(1))]+=Integer.valueOf(m2.group(2));
            num.province_doubtpeople[0]+=Integer.valueOf(m2.group(2));
            num.province[num.map1.get(m2.group(1))]++;
        }
        else if(m3.find()){
            num.province_curepeople[num.map1.get(m3.group(1))]+=Integer.valueOf(m3.group(2));
            num.province_infectpeople[num.map1.get(m3.group(1))]-=Integer.valueOf(m3.group(2));
            num.province_curepeople[0]+=Integer.valueOf(m3.group(2));
            num.province_infectpeople[0]-=Integer.valueOf(m3.group(2));
            num.province[num.map1.get(m3.group(1))]++;

        }
        else if(m4.find()){
            num.province_deadpeople[num.map1.get(m4.group(1))]+=Integer.valueOf(m4.group(2));
            num.province_deadpeople[0]+=Integer.valueOf(m4.group(2));
            num.province_infectpeople[num.map1.get(m4.group(1))]-=Integer.valueOf(m4.group(2));
            num.province_infectpeople[0]-=Integer.valueOf(m4.group(2));
            num.province[num.map1.get(m4.group(1))]++;
        }
        else if(m5.find()){
            num.province_infectpeople[num.map1.get(m5.group(1))]-=Integer.valueOf(m5.group(3));
            num.province_infectpeople[num.map1.get(m5.group(2))]+=Integer.valueOf(m5.group(3));
            num.province[num.map1.get(m5.group(1))]++;
            num.province[num.map1.get(m5.group(2))]++;

        }
        else if(m6.find()){
            num.province_doubtpeople[num.map1.get(m6.group(1))]-=Integer.valueOf(m6.group(3));
            num.province_doubtpeople[num.map1.get(m6.group(2))]+=Integer.valueOf(m6.group(3));
            num.province[num.map1.get(m6.group(1))]++;
            num.province[num.map1.get(m6.group(2))]++;

        }
        else if(m7.find()){
            num.province_doubtpeople[num.map1.get(m7.group(1))]-=Integer.valueOf(m7.group(2));
            num.province_infectpeople[num.map1.get(m7.group(1))]+=Integer.valueOf(m7.group(2));
            num.province_doubtpeople[0]-=Integer.valueOf(m7.group(2));
            num.province_infectpeople[0]+=Integer.valueOf(m7.group(2));
            num.province[num.map1.get(m7.group(1))]++;

        }
        else if(m8.find()){
            num.province_doubtpeople[num.map1.get(m8.group(1))]-=Integer.valueOf(m8.group(2));
            num.province_doubtpeople[0]-=Integer.valueOf(m8.group(2));
            num.province[num.map1.get(m8.group(1))]++;
        }
    }
}
 class People {
    public int[] province=new int[35];
    public int[] province_deadpeople=new int[35];
    public int[] province_curepeople=new int[35];
    public int[] province_doubtpeople=new int[35];
    public int[] province_infectpeople=new int[35];
    kind[] kinds=new kind[4];
    province[] provinces=new province[35];
    String[] province2={
            "全国", "安徽", "澳门" ,"北京",
            "重庆", "福建","甘肃","广东",
            "广西", "贵州", "海南", "河北",
            "河南", "黑龙江", "湖北", "湖南",
            "吉林","江苏", "江西", "辽宁",
            "内蒙古", "宁夏", "青海", "山东",
            "山西", "陕西", "上海","四川",
            "台湾", "天津", "西藏", "香港",
            "新疆", "云南", "浙江"
    };
    String[] kindss={
            "感染患者","疑似患者","治愈","死亡"
    };
    Map<String,Integer> map1=new HashMap<String, Integer>();//创建哈希表
    //Map<Integer,String> map2=new HashMap<Integer, String>();
    People() {
        for (int i = 0; i < 35; i++) {
            province[i] = 0;
            province_curepeople[i] = 0;
            province_deadpeople[i] = 0;
            province_doubtpeople[i] = 0;
            province_infectpeople[i] = 0;
            provinces[i]=new province();
            provinces[i].num=i;
            provinces[i].name=province2[i];
        }
        for(int i=0;i<4;i++){
            kinds[i]=new kind();
            kinds[i].num=i;
            kinds[i].name=kindss[i];
        }
        for(int i=0;i<35;i++){//将省份以及对应数字连接起来
            map1.put(province2[i],i);// K V
            // map2.put(province[i],province2[i]);
        }

    }
}
class province implements Comparator<province> {
    int num;
    String name;
    int quantify=0;
    public int compare(province o1,province o2){
        return -(o1.quantify-o2.quantify);
    }
}
class Cmdlist {
    public String args[];
    public String main_Path;
    public String out_Path;
    public Date date=new Date();
    String date2;
    People here=new People();//记录指令指示的省份
    int typenum=0;//查看type后面跟随的指令个数
    int _date=0;
    int _province=0;
    int _type=0;
    Cmdlist(String args[]) {
        this.args = args;
    }

    public boolean isright() {
        boolean bool_log=true;
        boolean bool_out=true;
        boolean bool_date=true;
        if (!args[0].equals("list")) {
            System.out.println("命令行应该由list开头");
            return false;
        }
        int b=0;//判断命令行中有没有log
        int a=0;//判断命令行中有没有out
        for (int i = 1; i < args.length; i++) {//判断输入路径
            if (args[i].equals("-log")) {
                b++;
                if(i+1>args.length){
                    System.out.println("必须输入路径");
                    return false;
                }
                bool_log = makedir(args[i + 1]);
                if (bool_log == false) {
                    System.out.println("你输入的路径不对");
                    return false;
                }
            }
        }
        if(b==0){
            System.out.println("必须输入log参数");
            return false;
        }
        for(int i=1;i<args.length;i++){
            if (args[i].equals("-out")) {
                a++;
                if (i + 1 >= args.length) {
                    System.out.println("必须输入路径");
                    return false;
                }
                bool_out = makedir2(args[i + 1]);
                if (bool_out == false) {
                    System.out.println("你输出的路径不对");
                    return false;
                }
            }
        }
        if(a==0){
            System.out.println("必须输入out参数");
            return false;
        }
        for(int i=1;i<args.length;i++){
            if (args[i].equals("-date")){
                _date++;
                if(i+1==args.length){
                    date=new Date(System.currentTimeMillis());
                    System.out.println("您没有输入日期，将按照今天日期进行处理");
                    return true;
                }
                bool_date=makedate(args[i+1]);
            }
        }
        return true;
    }

    public boolean makedir(String path) {
        if (path.matches("^[A-z]:\\\\(.+?\\\\)*$")) {
            main_Path=path;
            return true;
        }
        else
            return false;
    }
    public boolean makedir2(String path){
        if(path.matches("^[A-z]:\\\\(\\S+)+(\\.txt)$")){
            out_Path=path;
            return true;
        }
        else
            return false;
    }
    public boolean makedate(String date2){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date3=new Date(System.currentTimeMillis());
            if(date2.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                Date date1 = format.parse(date2);
                if (date2.equals(format.format(date1))) {
                    date = date1;
                    return true;
                }
                else{
                    date = new Date(System.currentTimeMillis());
                    System.out.println("您没有输入具体日期或输入格式有误，将按照今天的日期计算");
                    return true;
                }
            }
            else{
                date = new Date(System.currentTimeMillis());
                System.out.println("您没有输入具体日期或输入格式有误，将按照今天的日期计算");
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }
    public boolean isrighttype(){
        boolean typeright=true;
        for(int i=0;i<args.length;i++){
            if(args[i].equals("-type")){
                _type++;
                typenum++;
                if(i+1<=args.length)
                    typeright=dealtype(i);
                else
                    typenum=0;
            }
        }
        return true;
    }
    public boolean isrightprovince(){
        for(int i=0;i<args.length;i++){
            if(args[i].equals("-province")){
                _province++;
                if(i+1<=args.length) {
                    dealprovince(i+1);
                }
                else{
                    System.out.println("-province命令必须指明省份");
                    return false;
                }
            }
        }
        return true;
    }
    public boolean dealtype(int i){
        int num=0;
        int k=10;
        if (!args[i+1].equals("ip")&&!args[i+1].equals("sp")&&!args[i+1].equals("cure")&&!args[i+1].equals("dead")){
            System.out.println("-type指令只能跟随 ip sp cure dead");
            return false;
        }
        for(int j=i+1;j<args.length;j++){
            if(args[j].equals("ip")){
                here.kinds[0].quantify+=k;//根据J来判断输出的优先顺序
                k--;
                num++;
            }
            else if(args[j].equals("sp")){
                here.kinds[1].quantify+=k;
                k--;
                num++;
            }
            else if(args[j].equals("dead")){
                here.kinds[3].quantify+=k;
                k--;
                num++;
            }
            else if(args[j].equals("cure")){
                here.kinds[2].quantify+=k;
                k--;
                num++;
            }
        }
        if(num==0)
            typenum=0;
        return true;
    }
    public void dealprovince(int x){
        int k=100;
        boolean rightprovince=true;
        for(int i=x;i<args.length;i++){
            if (here.map1.containsKey(args[i])==true)
                here.provinces[here.map1.get(args[i])].quantify+=k;

        }
    }
}
class Write {
    public String Path;
    public People num;

    Write(String out_path, People a) {
        this.Path = out_path;
        this.num = a;
    }

    public void writedetail(kind[] kind,province[] province){
        try {
            FileWriter fw = new FileWriter(Path);
            if (num.kinds[0].quantify == 0) {
                if (num.provinces[0].quantify == 0)
                    for(int i=0;i<35;i++){
                        if(num.province[i]!=0){
                            fw.write(num.province2[i]+" ");
                            fw.write("感染患者"+num.province_infectpeople[i]+"人"+" ");
                            fw.write("疑似患者"+num.province_doubtpeople[i]+"人"+" ");
                            fw.write("治愈"+num.province_curepeople[i]+"人"+" ");
                            fw.write("死亡"+num.province_deadpeople[i]+"人");
                            fw.write("\n");
                        }
                    }
                else {
                    for (int i = 0; i < num.provinces.length; i++) {
                        if (num.provinces[i].quantify != 0) {
                            fw.write(num.provinces[i].name + " ");
                            fw.write("感染患者" + num.province_infectpeople[num.provinces[i].num] + "人" + " ");
                            fw.write("疑似患者" + num.province_doubtpeople[num.provinces[i].num] + "人" + " ");
                            fw.write("治愈" + num.province_curepeople[num.provinces[i].num] + "人" + " ");
                            fw.write("死亡" + num.province_deadpeople[num.provinces[i].num] + "人");
                            fw.write("\n");
                        }
                    }
                }
            }
            else {
                if (num.provinces[0].quantify == 0) {
                    for (int i = 0; i < 35; i++) {
                        if (num.province[i] != 0) {
                            fw.write(num.province2[i]+" ");
                            for(int j=0;j<4;j++) {
                                if (num.kinds[j].quantify != 0) {
                                    fw.write(num.kinds[j].name);
                                    if (num.kinds[j].name == "感染患者")
                                        fw.write(num.province_infectpeople[num.provinces[i].num] + "人" + " ");
                                    else if (num.kinds[j].name == "疑似患者")
                                        fw.write(num.province_doubtpeople[num.provinces[i].num] + "人" + " ");
                                    else if (num.kinds[j].name == "治愈")
                                        fw.write(num.province_curepeople[num.provinces[i].num] + "人" + " ");
                                    else if (num.kinds[j].name == "死亡")
                                        fw.write(num.province_deadpeople[num.provinces[i].num] + "人" + " ");
                                }
                            }
                            fw.write("\n");
                        }
                    }
                }
                else{
                    for(int i=0;i<num.provinces.length;i++){
                        if (num.provinces[i].quantify != 0) {
                            fw.write(num.provinces[i].name+" ");
                            for(int j=0;j<4;j++){
                                if(num.kinds[j].quantify!=0){
                                    fw.write(num.kinds[j].name);
                                    if (num.kinds[j].name == "感染患者")
                                        fw.write(num.province_infectpeople[num.provinces[i].num] + "人" + " ");
                                    else if (num.kinds[j].name == "疑似患者")
                                        fw.write(num.province_doubtpeople[num.provinces[i].num] + "人" + " ");
                                    else if (num.kinds[j].name == "治愈")
                                        fw.write(num.province_curepeople[num.provinces[i].num] + "人" + " ");
                                    else if (num.kinds[j].name == "死亡")
                                        fw.write(num.province_deadpeople[num.provinces[i].num] + "人" + " ");
                                }

                            }
                            fw.write("\n");
                        }
                    }
                }

            }
            fw.flush();
            fw.close();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }
}
class kind implements Comparator<kind> {
    int num;
    String name;
    int quantify=0;
    public int compare(kind o1,kind o2) {//自定义排序逻辑
        return -(o1.quantify-o2.quantify);//以传入的Cell的横坐标由小到大进行排序
    }
}


