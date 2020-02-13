/**
 * InfectStatistic
 * TODO
 *
 * @author 221701229
 * @version 1.0
 * @since 2020
 */
import java.util.*;
import java.util.regex.*;
import java.io.File;

class InfectStatistic {
    public static void main(String[] args) {
                /*String s="list -date 2020-01-22 -log D:/log/ -out D:/output.txt";
                String[] sl=s.split("\\s+");
                for (String ss:sl)
                {
                    System.out.println(ss);
                }
                CommandManager cm=new CommandManager(sl);
                cm.getArguments();*/

        }
    }


/**
 *命令接口
 */
interface Command{

     void execute(String[] args);

     String getCmdName();
}


/**
 *不同类型日志行
 */
class LogLine {

    private Pattern reg;
    private String logline;
    private LogHandle handle;
    LogLine(String arg,Pattern pattern)
    {
        logline=arg;
        reg=pattern;
    }


}

/**
 * 解析命令行命令
 */
class CommandManager {
    private String[] args;  //传入的命令行数组
    private Command cmd;  //当前解析出的命令
    private List<Command> CommandList=new ArrayList<>();  //支持的命令队列
    private List<List> Arguments=new ArrayList<>();  //命令的各参数及其选项的队列

    CommandManager(String[] args) {
        this.args=args;
        CommandList.add(new list());
    }

    //添加新的命令
    public void addCommand(Command cmd)
    {
        CommandList.add(cmd);
    }

    //获取命令
    public void setCommand()
    {
        //match表示开头找到的命令是否存在
        boolean match=false;
        //遍历允许的命令列表
        Iterator it=CommandList.iterator();
        while (it.hasNext())
        {
            cmd=(Command)it.next();
            //迭代判断输入的命令是否跟支持的命令匹配
            if(args[0].equals(cmd.getCmdName()))
            {
                match=true;
                break;
            }
        }
        if(match)
        {
            //执行命令
            cmd.execute(args);
        }
        else
        {
            /*
            *
            *错误输出
            *
            */
        }
    }

    public Command getCommand()
    {
        return cmd;
    }

    //解析出参数和对应的选项
    public void setArguments()
    {
        int len=args.length;//命令解析数组长度
        for(int i=1;i<len;)
        {
            //找到以'-'开头的命令
            if(args[i].startsWith("-"))
            {
                List<String> arg=new ArrayList<>();
                //System.out.println("find arg"+args[i]);
                //把参数加入给每个参数创建的数组
                arg.add(args[i]);
                i++;
                //把对应参数的所有选项加入对应数组
                while (i<len&&!args[i].startsWith("-"))
                {
                    //System.out.println("find opt"+args[i]);
                    arg.add(args[i]);
                    i++;
                }

                //把参数及选项队列加入
                Arguments.add(arg);
            }
        }
    }

    public List getArguments()
    {
        return Arguments;
    }

}

/**
 命令的解析与调用执行
 * */
class CommandInvoker{
    String[] args;
        //从初始命令中解析出字符串数组
        public static void getCommandArgs(String cmd)
        {
            String [] spString = cmd.split("\\s+");
        }
        CommandManager cm=new CommandManager(args);

}

/**
 list 命令的实现
 * */
class list implements Command{
    final String cmdname="list";
    private LogHandle listhandle;
    private String log=null;  //日志文件夹路径
    private String out=null;  //输出文件路径
    private HashMap<String,ArrayList> provienceMap=new HashMap<>();  //省份和各项数据的对应
    List<Pattern> patternlist=new ArrayList<>();
    list()
    {

    }

    public void execute(String[] args)
    {
        CommandManager cm=new CommandManager(args);
        cm.setArguments();
        List Arguments=cm.getArguments();
        Iterator it=Arguments.iterator();
        //提取两个必需的参数
        while (it.hasNext())
        {
            if(((ArrayList)it.next()).get(0).equals("-log"))
            {
                log=(String) ((ArrayList)it.next()).get(1);
                it.remove();
            }
            else if(((ArrayList)it.next()).get(0).equals("-out"))
            {
                out=(String) ((ArrayList)it.next()).get(1);
                it.remove();
            }
        }
        if(log==null||out==null)
        {
            /*
            *
            * 参数缺失错误
            *
            * */
        }

        File logFiles=new File(log);
        File[] files=logFiles.listFiles();
        //遍历提供的文件夹下的日志
        for(File f:files)
        {

        }

    }

    public String getCmdName()
    {
        return cmdname;
    }
}

/**
 对日志的处理
 * */
class LogHandle{
    //命令的处理
    public void logHandler(LogLine line)
    {

    }

}

interface MyPatterns
{
    String InfectedPatients="([\\u4e00-\\u9fa5])+ 新增 感染患者 (\\d+)人";//<省> 新增 感染患者 n人
    String SuspectedPatients="([\\u4e00-\\u9fa5])+ 新增 疑似患者 (\\d+)人";//<省> 新增 疑似患者 n人
    String InfectedGo="([\\u4e00-\\u9fa5])+ 感染患者 流入 ([\\u4e00-\\u9fa5])+ (\\d+)人";//<省1> 感染患者 流入 <省2> n人
    String SuspectedGo="([\\u4e00-\\u9fa5])+ 疑似患者 流入 ([\\u4e00-\\u9fa5])+ (\\d+)人";//<省1> 疑似患者 流入 <省2> n人
    String PatientsDie="([\\u4e00-\\u9fa5])+ 死亡 (\\d+)人";//<省> 死亡 n人
    String PatientsCure="([\\u4e00-\\u9fa5])+ 治愈 (\\d+)人";//<省> 治愈 n人
    String SuspectedDiagnosis="([\\u4e00-\\u9fa5])+ 疑似患者 确诊感染 (\\d+)人";//<省> 疑似患者 确诊感染 n人
    String SuspectedExclude="([\\u4e00-\\u9fa5])+ 排除 疑似患者 (\\d+)人";//<省> 排除 疑似患者 n人
    void doCount();
}

class InfectedPatients implements MyPatterns{
        private Pattern patt=Pattern.compile(InfectedPatients);

    //根据
    public void doCount()
    {



    }
}