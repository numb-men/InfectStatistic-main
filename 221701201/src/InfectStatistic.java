
import java.text.SimpleDateFormat;

/**
 * InfectStatistic
 * 
 *
 * @author 221701201
 * @version 1.0.0
 * @since 2020.02.13
 */



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

    //各类返回
    public String Get_name()
    {
        return name;
    }

    public int Get_ip()
    {
        return ip;
    }

    public int Get_sp()
    {
        return sp;
    }

    public int Get_cure()
    {
        return cure;
    }

    public int Get_dead()
    {
        return dead;
    }
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

        if(!Command[0].equals("list"))//判断是否合法命令
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
            else
            {
                System.out.println("输入有误！");
            }
        }

        if(!this.hasLog||!this.hasOut)//判断是否含有log和out
        {
            System.out.println("缺少必须携带的-log或-out");
            System.exit(1);
        }
        else if(this.LogPath==""||this.OutPath=="")//判断log和out后是否写入路径
        {
            System.out.println("缺少-log或-out后的路径");
            System.exit(1);
        }

        if(this.hasDate)
        {
            if(this.DateName=="")//判断在写入-date命令情况下是否写入具体日期
            {
                System.out.println("缺少具体日期");
                System.exit(1);
            }
            else if(!isLegalDate(this.DateName))//判断写入的日期是否合法
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
    public boolean Get_hasLog()
    {
        return hasLog;
    }

    public String Get_LogPath()
    {
        return LogPath;
    }

    public boolean Get_hasOut()
    {
        return hasOut;
    }

    public String Get_OutPath()
    {
        return OutPath;
    }

    public boolean Get_hasDate()
    {
        return hasDate;
    }

    public String Get_DateName()
    {
        return DateName;
    }

}

 


class InfectStatistic {
    public static void main(String[] args) {
        System.out.println("helloworld");
    }
}




