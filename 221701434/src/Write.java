import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Write {
    public String Path;
    public People num;

    Write(String out_path, People a) {
        this.Path = out_path;
        this.num = a;
    }

    public void Writealltxt() {
        try {
            FileWriter fw = new FileWriter(Path);
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
            fw.flush();
            fw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void writedetail(int[] kind,int[] province,int _type,int _province){
        try {
            FileWriter fw=new FileWriter(Path);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
