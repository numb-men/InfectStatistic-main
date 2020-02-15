import java.text.SimpleDateFormat;
import java.util.Date;

public class Cmdlist {
    String args[];
    String main_Path;
    String out_Path;
    Date date;

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
                if(i+1>=args.length){
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
        for(int i=1;i<args.length;i++){
            if (args[i].equals("-date")){
                if(i+1>=args.length){
                    System.out.println("您没有输入日期，将按照今天日期进行处理");
                    return false;
                }
                bool_date=makedate(args[i+1]);
                if(bool_date==false){
                    System.out.println("你输入的日期格式不符合yyyy-mm-dd，将按照今天的日期进行处理");
                    return false;
                }
            }
        }
        if(b==0){
            System.out.println("必须输入log参数");
            return false;
        }
        if(a==0){
            System.out.println("必须输入out参数");
            return false;
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
            Date date1 = format.parse(date2);
            if(date2.equals(format.format(date1))){
                date=date1;
                return true;
            }
            else
                return false;
        } catch (Exception e) {
            return false;
        }

    }
    public void isrighttype(){

    }
}

