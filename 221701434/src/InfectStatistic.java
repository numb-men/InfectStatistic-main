import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
    public static void main(String[] args) {
        //People a=new People();
        //  Read b=new Read("E:\\github\\InfectStatistic-main\\221701434\\log\\",a);
        // Write c=new Write("E:\\github\\InfectStatistic-main\\221701434\\123.txt",a);
        //b.getFiles();
        //c.Writealltxt();
       Cmdlist a=new Cmdlist(args);
       boolean go=true;//程序运行判断
       go=a.isright();
       if(go==false)
           return;
       go=a.isrighttype();
        if(go==false)
            return;
        go=a.isrightprovince();
        if(go==false)
            return;
        Read b=new Read(a.main_Path,a.here,a.date);
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");//规范日期格式
       //  String Today = sdf.format(a.date);
       // System.out.println(Today);
        b.getFiles();
        Arrays.sort(a.here.kinds, new Comparator<kind>() {
            @Override
            public int compare(kind o1, kind o2) {
                return -(o1.quantify-o2.quantify);
            }
        });
        Arrays.sort(a.here.provinces, new Comparator<province>() {
            @Override
            public int compare(province o1, province o2) {
                return -(o1.quantify-o2.quantify);
            }
        });
        for(int i=0;i<a.here.kinds.length;i++){
        }
        Write c=new Write(a.out_Path,a.here);
        c.writedetail(a.here.kinds,a.here.provinces);
        System.out.println("成功");
    }
}
