import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Read {
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
        System.out.println(now);
        System.out.println(today);
        for (int i = 0; i < filelist.length; i++) {
            if (filelist[i].getName().compareTo(today) <= 0)
                ReadLog(Log_Path + filelist[i].getName());
        }
    }

    public void ReadLog(String Log_Path) {
        try {
            File file = new File(Log_Path);//传入log的路径
            String strline=null;//用来存储读出的一行
            BufferedReader bufferedReader=new BufferedReader(new FileReader(file));
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
