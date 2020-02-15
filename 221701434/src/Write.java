import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Comparator;
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
    public void writedetail(kind[] kind,province[] province){
        try {
            FileWriter fw = new FileWriter(Path);
            if (num.kinds[0].quantify == 0) {
                if (num.provinces[0].quantify == 0)
                    Writealltxt();
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
            } else {
                if (num.provinces[0].quantify == 0) {
                    for (int i = 0; i < 35; i++) {
                        if (num.province[i] != 0) {
                            fw.write(num.province2[i]+" ");
                            for(int j=0;j<4;j++){
                                if(num.kinds[j].quantify!=0)
                                    fw.write(num.kinds[i].name+num.province_infectpeople[num.provinces[i].num]+"人"+" ");
                                else{
                                    fw.write("\n");
                                    break;
                                }
                            }
                        }
                    }
                }
                else{
                    for(int i=0;i<num.provinces.length;i++){
                        if (num.provinces[i].quantify != 0) {
                            fw.write(num.provinces[i].name+" ");
                            for(int j=0;j<4;j++){
                                if(num.kinds[j].quantify!=0)
                                    fw.write(num.kinds[i].name+num.province_infectpeople[num.provinces[i].num]+"人"+" ");
                                else{
                                    fw.write("\n");
                                    break;
                                }
                            }
                        }
                    }
                }

            }
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }
}
