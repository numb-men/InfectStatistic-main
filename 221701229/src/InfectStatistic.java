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
        System.out.println("helloworld");
    }
}

/**
 *命令接口
 */
interface Command{
    public void execute(String args[]);
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
 * 解析命令行参数
 */
class CommandArgs {
    String[] args;
    final String InfectedPatients="([\\u4e00-\\u9fa5])+ 新增 感染患者 (\\d+)人";//<省> 新增 感染患者 n人
    final String SuspectedPatients="([\\u4e00-\\u9fa5])+ 新增 疑似患者 (\\d+)人";//<省> 新增 疑似患者 n人
    final String InfectedGo="([\\u4e00-\\u9fa5])+ 感染患者 流入 ([\\u4e00-\\u9fa5])+ (\\d+)人";//<省1> 感染患者 流入 <省2> n人
    final String SuspectedGo="([\\u4e00-\\u9fa5])+ 疑似患者 流入 ([\\u4e00-\\u9fa5])+ (\\d+)人";//<省1> 疑似患者 流入 <省2> n人
    final String PatientsDie="([\\u4e00-\\u9fa5])+ 死亡 (\\d+)人";//<省> 死亡 n人
    final String PatientsCure="([\\u4e00-\\u9fa5])+ 治愈 (\\d+)人";//<省> 治愈 n人
    final String SuspectedDiagnosis="([\\u4e00-\\u9fa5])+ 疑似患者 确诊感染 (\\d+)人";//<省> 疑似患者 确诊感染 n人
    final String SuspectedExclude="([\\u4e00-\\u9fa5])+ 排除 疑似患者 (\\d+)人";//<省> 排除 疑似患者 n人
    List<Pattern> patternlist=new ArrayList<>();
    /**
     * 传入命令行参数数组构造
     * @param args
     */
    CommandArgs(String[] args) {
        //...
        patternlist.add(Pattern.compile(InfectedPatients));
        patternlist.add(Pattern.compile(SuspectedPatients));
        patternlist.add(Pattern.compile(InfectedGo));
        patternlist.add(Pattern.compile(SuspectedGo));
        patternlist.add(Pattern.compile(PatientsDie));
        patternlist.add(Pattern.compile(PatientsCure));
        patternlist.add(Pattern.compile(SuspectedDiagnosis));
        patternlist.add(Pattern.compile(SuspectedExclude));
    }


}

/**
 命令的创建与调用执行
 * */
class CommandInvoker{

}

/**
 list 命令的实现
 * */
class list implements Command{
    private LogHandle listhandle;
    list(LogHandle loghandle)
    {
        listhandle=loghandle;
    }
    public void execute(String args[])
    {

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