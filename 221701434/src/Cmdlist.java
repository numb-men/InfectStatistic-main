import java.text.SimpleDateFormat;
import java.util.Date;

public class Cmdlist {
    public String args[];
    public String main_Path;
    public String out_Path;
    public Date date;
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
        for(int i=1;i<args.length;i++){
            if (args[i].equals("-out")) {
                a++;
                if (i + 1 > args.length) {
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
                _date++;
                if(i+1>args.length){
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
            else{
                date=new Date(System.currentTimeMillis());
                System.out.println("你的输入格式有误，或者没有输入日期，将按今日日期进行处理");
                return false;
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
        if (!args[i+1].equals("ip")&&!args[i+1].equals("sp")&&!args[i+1].equals("cure")&&!args[i+1].equals("dead")){
            System.out.println("-type指令只能跟随 ip sp cure dead");
            return false;
        }
        for(int j=i+1;j<args.length;j++){
            if(args[j].equals("ip")){
                here.kinds[0].quantify-=j;//根据J来判断输出的优先顺序
                num++;
            }
            else if(args[j].equals("sp")){
                here.kinds[1].quantify-=j;
                num++;
            }
            else if(args[j].equals("dead")){
                here.kinds[2].quantify-=j;
                num++;
            }
            else if(args[j].equals("cure")){
                here.kinds[3].quantify-=j;
                num++;
            }
        }
        if(num==0)
            typenum=0;
        return true;
    }
    public void dealprovince(int x){
        boolean rightprovince=true;
        for(int i=x;i<args.length;i++){
            if (here.map1.containsKey(args[i])==true)
                here.provinces[here.map1.get(args[i])].quantify-=i;
        }
    }
}

