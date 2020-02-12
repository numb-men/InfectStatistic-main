/**
 * InfectStatistic
 * TODO
 *
 * @author 221701229
 * @version 1.0
 * @since 2020
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.*;
import java.util.List;
class InfectStatistic {
    public static void main(String[] args) {
        String s="list -date 2020-01-22 -log D:/log/ -out D:/output.txt";
        String[] arg=s.split("\\s+");
        for(String ss:arg){
            System.out.println(ss);
        }
    }
}

/**
 *命令接口
 */
interface Command{
     String InfectedPatients="([\\u4e00-\\u9fa5])+ 新增 感染患者 (\\d+)人";//<省> 新增 感染患者 n人
     String SuspectedPatients="([\\u4e00-\\u9fa5])+ 新增 疑似患者 (\\d+)人";//<省> 新增 疑似患者 n人
     String InfectedGo="([\\u4e00-\\u9fa5])+ 感染患者 流入 ([\\u4e00-\\u9fa5])+ (\\d+)人";//<省1> 感染患者 流入 <省2> n人
     String SuspectedGo="([\\u4e00-\\u9fa5])+ 疑似患者 流入 ([\\u4e00-\\u9fa5])+ (\\d+)人";//<省1> 疑似患者 流入 <省2> n人
     String PatientsDie="([\\u4e00-\\u9fa5])+ 死亡 (\\d+)人";//<省> 死亡 n人
     String PatientsCure="([\\u4e00-\\u9fa5])+ 治愈 (\\d+)人";//<省> 治愈 n人
     String SuspectedDiagnosis="([\\u4e00-\\u9fa5])+ 疑似患者 确诊感染 (\\d+)人";//<省> 疑似患者 确诊感染 n人
     String SuspectedExclude="([\\u4e00-\\u9fa5])+ 排除 疑似患者 (\\d+)人";//<省> 排除 疑似患者 n人
     void execute(String args[]);
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
class CommandArgs {
    String[] args;
    Command cmd;
    List<Command> CommandList=new ArrayList<>();
    /**
     * 传入命令行参数数组构造
     * @param args
     */
    CommandArgs(String[] args) {
        this.args=args;
        CommandList.add(new list());
    }

    //添加新的命令
    public void addCommand(Command cmd)
    {
        CommandList.add(cmd);
    }

    //获取命令
    public void getCommand()
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

        }
        else ;//错误输出
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
}

/**
 list 命令的实现
 * */
class list implements Command{
    final String cmdname="list";
    private LogHandle listhandle;
    List<Pattern> patternlist=new ArrayList<>();
    list()
    {
        patternlist.add(Pattern.compile(InfectedPatients));
        patternlist.add(Pattern.compile(SuspectedPatients));
        patternlist.add(Pattern.compile(InfectedGo));
        patternlist.add(Pattern.compile(SuspectedGo));
        patternlist.add(Pattern.compile(PatientsDie));
        patternlist.add(Pattern.compile(PatientsCure));
        patternlist.add(Pattern.compile(SuspectedDiagnosis));
        patternlist.add(Pattern.compile(SuspectedExclude));
    }
    public void execute(String args[])
    {

    }
    public String getCmdName()
    {
        return cmdname;
    }
}

/**
 命令的具体实现
 * */
class LogHandle{
    //命令的处理
    public void logHandler(LogLine line)
    {

    }

}