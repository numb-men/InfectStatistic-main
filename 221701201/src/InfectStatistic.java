
import java.io.File;
import java.text.SimpleDateFormat;

/**
 * InfectStatistic
 * 
 *
 * @author 221701201
 * @version 1.0.0
 * @since 2020.02.13
 */


class InfectStatistic {
    public static void main(String[] args) {
        
        // String[] ProvinceList=
        // {
        //     "全国","安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北","河南","黑龙江",
        //     "湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海",
        //     "四川","天津","西藏","新疆","云南","浙江"
        // };




        System.out.println("test");
    }
}



 /**
 * Province类
 * @description 用于存储各个省份（包括全国）的名称和各类人数
 * @author 2217012001
 * @version 1.0.0
 * @since 2020.2.13
 */
class Province
{
    String name;
    int ip;
    int sp;
    int cure;
    int dead;

    public Province(String name)
    {
        this.name=name;
        this.ip=0;
        this.sp=0;
        this.cure=0;
        this.dead=0;
    }

    //各类人数的增减操作
    public void Add_ip(int num){ip+=num;}
    public void Sub_ip(int num){ip+=num;}

    public void Add_sp(int num){sp+=num;}
    public void Sub_sp(int num){sp+=num;}

    public void Add_cure(int num){cure+=num;}
    public void Sub_cure(int num){cure+=num;}

    public void Add_dead(int num){ip+=num;}
    public void Sub_dead(int num){ip+=num;}

    //各类人数数量的返回
    public int Get_ip(){return ip;}
    public int Get_sp(){return sp;}
    public int Get_cure(){return cure;}
    public int Get_dead(){return dead;}

}


 /**
 * Command类
 * @description 用于对命令进行合法判断和各种解析，暂未实现type功能和province功能
 * @author 2217012001
 * @version 1.0.0
 * @since 2020.2.13
 */
class Command
{
    boolean hasLog;
    String LogPath;

    boolean hasOut;
    String OutPath;

    boolean hasDate;
    String DateName;

    // boolean hasType;
    // String[] TypeName;

    // boolean hasProvince;
    // String[] ProvinceName;

    public Command()
    {
        this.hasLog=false;
        this.LogPath="";

        this.hasOut=false;
        this.OutPath="";

        this.hasDate=false;
        this.DateName="";
    }

    public void Set_Command(String[] Command)
    {
        int i=1;
        int len=Command.length;

        if(!Command[0].equals("list"))//判断1：是否是合法命令
        {
            System.out.println("命令非法");
            System.exit(1);
        }

        for(i=1;i<len;i++)
        {
            if(Command[i].equals("-log"))
            {
                this.hasLog=true;
                this.LogPath=Command[i+1];
                i++;
            }
            else if(Command[i].equals("-out"))
            {
                this.hasOut=true;
                this.OutPath=Command[i+1];
                i++;
            }
            else if(Command[i].equals("-date"))
            {
                this.hasDate=true;
                this.DateName=Command[i+1];
                i++;
            }
            else if(Command[i].equals("-type"))
            {
                System.out.println("抱歉，type功能暂未实现");
                i++;
            }
            else if(Command[i].equals("-province"))
            {
                System.out.println("抱歉，province功能暂未实现");
                i++;
            }
            else    //判断2：是否含有指定命令之外的输入
            {
                System.out.println("输入有误！");
            }
        }

        if(!this.hasLog||!this.hasOut)//判断3：是否含有log和out
        {
            System.out.println("缺少必须携带的-log或-out");
            System.exit(1);
        }
        else if(this.LogPath==""||this.OutPath=="")//判断4：log和out后是否写入路径
        {
            System.out.println("缺少-log或-out后的路径");
            System.exit(1);
        }
        else
        {
            File file=new File(LogPath);
            if(!file.exists())//判断5：log后的路径是否存在
            {
                System.out.println("log路径不存在");
                System.exit(1);
            }
        }

        if(this.hasDate)
        {
            if(!isLegalDate(this.DateName))//判断6：写入的日期是否合法
            {
                System.out.println("输入日期非法");
                System.exit(1);
            }
        }
    }

    public boolean isLegalDate(String DateName)
    {
        boolean result = true;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try{
            format.setLenient(false);
            format.parse(DateName);
        }catch (Exception e){
            result = false;
        }
        return result;
    }

    //各类返回
    public boolean Get_hasLog(){return hasLog;}
    public String Get_LogPath(){return LogPath;}

    public boolean Get_hasOut(){return hasOut;}
    public String Get_OutPath(){return OutPath;}

    public boolean Get_hasDate(){return hasDate;}
    public String Get_DateName(){return DateName;}

}

 


 /**
 * OpenReadFile类
 * @description 用于打开文件并进行操作
 * @author 2217012001
 * @version 1.0.0
 * @since 2020.2.13
 */
class OpenReadFile
{
    String path;
    String date;
    String OutProvince;//流出省份
    String InProvince;//流入省份
    int num;
    String[] content=
    {
        "(\\W+) 新增 感染患者 (\\d+)人",
        "(\\W+) 新增 疑似患者 (\\d+)人",
        "(\\W+) 感染患者 流入 (\\W+) (\\d+)人",
        "(\\W+) 疑似患者 流入 (\\W+) (\\d+)人",
        "(\\W+) 死亡 (\\d+)人",
        "(\\W+) 治愈 (\\d+)人",
		"(\\W+) 疑似患者 确诊感染 (\\d+)人",
		"(\\W+) 排除 疑似患者 (\\d+)人"
    };

    public OpenReadFile(String path,String date)
    {
        this.path=path;
        this.date=date;
        this.OutProvince="";
        this.InProvince="";
        this.num=0;
    }

}





 /**
 * WriteNewFile类
 * @description 用于按照格式输出新的文件
 * @author 2217012001
 * @version 1.0.0
 * @since 2020.2.13
 */
class WriteNewFile
{
    // Province[] ProvinceList;
    // String[] ProvinceName;
    // String[] TypeName;
    File newfile;

    public WriteNewFile(String OutPath)
    {
        this.newfile=new File(OutPath);
        if(!newfile.exists())
        {
            newfile.mkdir();
        }

    }
    public void Write()
    {
        

    }

}


