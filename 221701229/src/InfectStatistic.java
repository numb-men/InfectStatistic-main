/**
 * InfectStatistic
 * TODO
 *
 * @author 221701229
 * @version 1.0.0
 * @since 2020
 */
import java.io.*;
import java.util.*;
import java.util.regex.*;

class InfectStatistic {
    public static void main(String[] args) {

                CommandInvoker cmdinvoke=new CommandInvoker(args);

        }
    }


/**
 *命令接口
 */
interface Command{

     void execute(List<ArrayList<String>> arguments);

     String getCmdName();
}

/**
 * 解析命令行命令
 */
class CommandManager {
    private String[] args;  //传入的命令行数组
    private String cmdname;  //当前解析出的命令
    private List<Command> CommandList=new ArrayList<>();  //支持的命令队列
    private List<ArrayList<String>> Arguments=new ArrayList<>();  //命令的各参数及其选项的队列

    CommandManager(String[] args) {
        this.args=args;
        CommandList.add(new list());
        cmdname=args[0];
    }

    public String getCmdName()
    {
        return cmdname;
    }


    public List<Command> getCommandList()
    {
        return CommandList;
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
                ArrayList<String> arg=new ArrayList<>();
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

    public List<ArrayList<String>> getArguments()
    {
        return Arguments;
    }

}

/**
 命令的解析与调用执行
 * */

class CommandInvoker{
    private List<Command> CommandList=new ArrayList<>();  //支持的命令队列
    CommandInvoker(String[] arg)
    {
        CommandList.add(new list());
        CommandManager cm=new CommandManager(arg);
        //match表示开头找到的命令是否存在
        boolean match=false;
        Command cmd=new list();//
        //遍历允许的命令列表
        for (Command command : CommandList) {
            //迭代判断输入的命令是否跟支持的命令匹配
            if (arg[0].equals(command.getCmdName())) {
                cmd=command;
                match = true;
                break;
            }
        }
        if(match)
        {
            //执行命令
            System.out.println("执行命令:"+cmd.getCmdName());
            cmd.execute(cm.getArguments());
        }
        else
        {
            /*
             *
             *找不到命令错误输出
             *
             */
            System.out.println("没有对应的命令！");
            System.exit(1);
        }
    }
    public void addCommand(Command cmd)
    {
        CommandList.add(cmd);
    }



}

/**
 list 命令的实现
 * */
class list implements Command{
    private String cmdname;
    final List<String> provinceorder=Arrays.asList("安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏","青海","山东",
            "山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江");
    private Comparator<String> comp;   //省份比较器
    private String log=null;  //日志文件夹路径
    private String out=null;  //输出文件路径
    private ArrayList type=null;  //指定的输出类型
    private ArrayList<String> province=null;  //指定的省份
    private Set<String> allprovinceSet;//日志中出现的省份
    private ArrayList<String> allprovinceList;//日志中出现的省份
    private String date=null;
    private int[] all={0,0,0,0};
    private HashMap<String,Integer> infected;
    private HashMap<String,Integer> suspected;
    private HashMap<String,Integer> cured;
    private HashMap<String,Integer> died;
    private LogHandle handler;
    list()
    {
        cmdname="list";
        handler=new LogHandle();
        infected=new HashMap<>();
        suspected=new HashMap<>();
        cured=new HashMap<>();
        died=new HashMap<>();
        comp=new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return provinceorder.indexOf(o1)-provinceorder.indexOf(o2);
            }
        };
    }


    //传入参数数组
    public void execute(List<ArrayList<String>> arguments)
    {
        //提取两个必需的参数
        for(ArrayList<String> para:arguments)
        {
            if(para.get(0).equals("-log"))
            {
                log=(String)para.get(1);
                //it.remove();
            }
            else if(para.get(0).equals("-out"))
            {
                out=(String) para.get(1);
                //it.remove();
            }
            else if(para.get(0).equals("-date"))
            {
                date=(String) para.get(1);
            }
            else if(para.get(0).equals("-type"))
            {
                para.remove(0);
                type=para;
            }
            else if(para.get(0).equals("-province"))
            {
                para.remove(0);
                province=para;
            }
        }

        if(log==null||out==null)
        {
            /*
            *
            * 必要参数缺失错误
            *
            * */
            System.out.println("缺少必要参数log或out");
            System.exit(1);
        }

        File logFiles=new File(log);

        if(!logFiles.exists())
        {
            System.out.println("file name"+log);
            /*
            * 文件不存在错误
            *
            * */
            System.out.println("指定的读取文件路径不存在!");
            System.exit(1);
        }
        if(!(new File(out)).exists())
        {
            System.out.println("指定的输出文件路径不存在!");
            System.exit(1);
        }
        File[] files=logFiles.listFiles();
        //遍历提供的文件夹下的日志
        String readbuff=null;
        for (File f:files)
        {
            try
            {
                BufferedReader br=new BufferedReader(new FileReader(f));
                while ((readbuff=br.readLine())!=null)
                {
                    //把读出来的日志行交给下面处理

                    handler.logHandlerList(readbuff,infected,suspected,cured,died);
                }
                //有指定截至日期时读取完文件判断日期
                if(null!=date&&f.getName().equals(date))
                {
                    break;
                }
                br.close();
            }
            catch (FileNotFoundException fe)
            {
                System.out.println("file not found");
                System.exit(1);
            }
            catch (IOException ie)
            {
                System.out.println("null content");
                System.exit(1);
            }
        }
        //处理文件遍历完的结果，按选项输出到指定文件
        // /type ip sp cure dead /province /out

        if(type!=null)
        {
            boolean ip=false;
            boolean sp=false;
            boolean cure=false;
            boolean dead=false;
           type.remove(0);
           Iterator typeit=type.iterator();
           String typ=null;
           //筛选有设定的选项
           while (typeit.hasNext())
           {
               typ=(String)typeit.next();
               if("ip".equals(typ))
               {
                   ip=true;
               }else if("sp".equals(typ))
               {
                   sp=true;
               }else if("cure".equals(typ))
               {
                   cure=true;
               }else if("dead".equals(typ))
               {
                   dead=true;
               }
           }

           try{
               File f=new File(out);
               FileWriter fw=new FileWriter(f);
               int i,s,c,d;
               String prov=null;
           if(null!=province)
           {
               province.sort(comp); //排序
               for (String value : province) {
                   prov = value;
                   //数据不存在则设置为0
                   if (infected.get(prov) == null) i = 0;
                   else i = infected.get(prov);
                   if (suspected.get(prov) == null) s = 0;
                   else s = suspected.get(prov);
                   if (cured.get(prov) == null) c = 0;
                   else c = cured.get(prov);
                   if (died.get(prov) == null) d = 0;
                   else d = died.get(prov);
                   fw.write(prov + " ");
                   if (ip) fw.write("感染患者" + i + "人 ");
                   if (sp) fw.write("疑似患者" + s + "人 ");
                   if (cure) fw.write("治愈" + c + "人 ");
                   if (dead) fw.write("死亡" + d + "人");
                   fw.write('\n');
               }
           }
           else
           {
               allprovinceSet=infected.keySet();
               allprovinceSet.addAll(suspected.keySet());
               allprovinceSet.addAll(cured.keySet());
               allprovinceSet.addAll(died.keySet());
               allprovinceList=new ArrayList<String>(allprovinceSet);
               //计算全国
               //给所有省份按给定的顺序排序
               allprovinceList.sort(comp);
               for (String value : allprovinceList) {
                   prov = value;
                   //数据不存在则设置为0
                   if (infected.get(prov) == null) i = 0;
                   else i = infected.get(prov);
                   if (suspected.get(prov) == null) s = 0;
                   else s = suspected.get(prov);
                   if (cured.get(prov) == null) c = 0;
                   else c = cured.get(prov);
                   if (died.get(prov) == null) d = 0;
                   else d = died.get(prov);
                   fw.write(prov + " ");
                   if (ip) fw.write("感染患者" + i + "人 ");
                   if (sp) fw.write("疑似患者" + s + "人 ");
                   if (cure) fw.write("治愈" + c + "人 ");
                   if (dead) fw.write("死亡" + d + "人");
                   fw.write('\n');
               }
           }
            fw.close();
        }catch (IOException io){
               System.out.println("file error");
           }

        }

        //四项全部输出
        else
        {
            try{
                File f=new File(out);
                FileWriter fw=new FileWriter(f);
                int i,s,c,d;
                String prov=null;
                if(null!=province)
                {

                    province.sort(comp); //排序
                    for (String value : province) {
                        prov = value;
                        //数据不存在则设置为0
                        if (infected.get(prov) == null) i = 0;
                        else i = infected.get(prov);
                        if (suspected.get(prov) == null) s = 0;
                        else s = suspected.get(prov);
                        if (cured.get(prov) == null) c = 0;
                        else c = cured.get(prov);
                        if (died.get(prov) == null) d = 0;
                        else d = died.get(prov);
                        fw.write(prov + " " + "感染患者" + i + "人 " + "疑似患者" + s + "人 " + "治愈" + c + "人 " + "死亡" + d + "人" + '\n');
                    }
                }
                else
                {
                    allprovinceSet=new HashSet<>();
                   allprovinceSet.addAll(infected.keySet());
                   allprovinceSet.addAll(suspected.keySet());
                   allprovinceSet.addAll(cured.keySet());
                   allprovinceSet.addAll(died.keySet());
                   allprovinceList=new ArrayList<String>(allprovinceSet);
                   //计算全国
                   //给所有省份按给定的顺序排序
                    allprovinceList.sort(comp);
                    for (Object o : allprovinceList) {
                        prov = (String) o;
                        //数据不存在则设置为0
                        if (infected.get(prov) == null) i = 0;
                        else i = infected.get(prov);
                        if (suspected.get(prov) == null) s = 0;
                        else s = suspected.get(prov);
                        if (cured.get(prov) == null) c = 0;
                        else c = cured.get(prov);
                        if (died.get(prov) == null) d = 0;
                        else d = died.get(prov);
                        fw.write(prov + " " + "感染患者" + i + "人 " + "疑似患者" + s + "人 " + "治愈" + c + "人 " + "死亡" + d + "人" + '\n');
                    }
                }
                fw.close();
            }catch (IOException io){
                System.out.println("file write error");
            }
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
    private ArrayList<MyPatterns> pat=new ArrayList<>();


    //命令的处理
    LogHandle()
    {
        pat.add(new InfectedPatients());
        pat.add(new SuspectedPatients());
        pat.add(new InfectedGo());
        pat.add(new SuspectedGo());
        pat.add(new PatientsDie());
        pat.add(new PatientsCure());
        pat.add(new SuspectedDiagnosis());
        pat.add(new SuspectedExclude());
    }
    public void logHandlerList(String line,HashMap<String,Integer> infected,HashMap<String,Integer> suspected,HashMap<String,Integer> cured,HashMap<String,Integer> died)
    {
        MyPatterns mypattern;
        for (MyPatterns myPatterns : pat) {
            mypattern = myPatterns;
            //根据日志行跟哪个类的正则匹配选择类执行统计
            if (line.matches(mypattern.getReg())) {
                mypattern.doCount(line, infected, suspected, cured, died);
                break;
            }
        }
    }

}

/**
* 各种正则表达式
*
* */
abstract class MyPatterns
{
    String InfectedPatients="([\\u4e00-\\u9fa5])+ 新增 感染患者 (\\d+)人";//<省> 新增 感染患者 n人
    String SuspectedPatients="([\\u4e00-\\u9fa5])+ 新增 疑似患者 (\\d+)人";//<省> 新增 疑似患者 n人
    String InfectedGo="([\\u4e00-\\u9fa5])+ 感染患者 流入 ([\\u4e00-\\u9fa5])+ (\\d+)人";//<省1> 感染患者 流入 <省2> n人
    String SuspectedGo="([\\u4e00-\\u9fa5])+ 疑似患者 流入 ([\\u4e00-\\u9fa5])+ (\\d+)人";//<省1> 疑似患者 流入 <省2> n人
    String PatientsDie="([\\u4e00-\\u9fa5])+ 死亡 (\\d+)人";//<省> 死亡 n人
    String PatientsCure="([\\u4e00-\\u9fa5])+ 治愈 (\\d+)人";//<省> 治愈 n人
    String SuspectedDiagnosis="([\\u4e00-\\u9fa5])+ 疑似患者 确诊感染 (\\d+)人";//<省> 疑似患者 确诊感染 n人
    String SuspectedExclude="([\\u4e00-\\u9fa5])+ 排除 疑似患者 (\\d+)人";//<省> 排除 疑似患者 n人
    abstract void doCount(String line,HashMap<String,Integer> infected,HashMap<String,Integer> suspected,HashMap<String,Integer> cured,HashMap<String,Integer> died);
    abstract String getReg();
}

class InfectedPatients extends  MyPatterns{
    public String reg="([\\u4e00-\\u9fa5])+ 新增 感染患者 (\\d+)人";//<省> 新增 感染患者 n人
    //根据
    public void doCount(String line,HashMap<String,Integer> infected,HashMap<String,Integer> suspected,HashMap<String,Integer> cured,HashMap<String,Integer> died)
    {
        String[] str=line.split("\\s+");
        int sum;
        //获取对应省份的感染人数
        if(infected.containsKey(str[0]))
        {
            sum=infected.get(str[0]);
        }
        else
        {

            sum=0;
        }
        //把日志行的新增人数加入总数
        sum+=Integer.parseInt(str[3].substring(0,str[3].indexOf("人")));
        infected.put(str[0],sum);
    }

    public String getReg()
    {
        return reg;
    }
}

class SuspectedPatients extends MyPatterns{
    public String reg="([\\u4e00-\\u9fa5])+ 新增 疑似患者 (\\d+)人";//<省> 新增 疑似患者 n人
    //根据
    public void doCount(String line,HashMap<String,Integer> infected,HashMap<String,Integer> suspected,HashMap<String,Integer> cured,HashMap<String,Integer> died)
    {
        String[] str=line.split("\\s+");
        int sum;
        //获取对应省份的疑似人数
        if(suspected.containsKey(str[0]))
        {
            sum=suspected.get(str[0]);
        }
        else
        {
            sum=0;
        }
        //把日志行的新增人数加入总数
        sum+=Integer.parseInt(str[3].substring(0,str[3].indexOf("人")));
        suspected.put(str[0],sum);
    }

    public String getReg()
    {
        return reg;
    }
}

class InfectedGo extends MyPatterns{
    //private Pattern patt=Pattern.compile(InfectedGo);
    public String reg="([\\u4e00-\\u9fa5])+ 感染患者 流入 ([\\u4e00-\\u9fa5])+ (\\d+)人";//<省1> 感染患者 流入 <省2> n人
    //根据
    public void doCount(String line,HashMap<String,Integer> infected,HashMap<String,Integer> suspected,HashMap<String,Integer> cured,HashMap<String,Integer> died)
    {
        String[] str=line.split("\\s+");
        int change=Integer.parseInt(str[4].substring(0,str[4].indexOf("人")));//流动人数
        //流出省份感染人数相应减少
        infected.put(str[0],infected.get(str[0])-change);
        //流入省份感染人数相应增加
        if(infected.containsKey(str[3]))
        {
            infected.put(str[3],change+infected.get(str[3]));
        }
        else
        {
            infected.put(str[3],change);
        }
    }

    public String getReg()
    {
        return reg;
    }
}

class SuspectedGo extends MyPatterns{
    //private Pattern patt=Pattern.compile(SuspectedGo);
    public String reg="([\\u4e00-\\u9fa5])+ 疑似患者 流入 ([\\u4e00-\\u9fa5])+ (\\d+)人";//<省1> 疑似患者 流入 <省2> n人
    //根据
    public void doCount(String line,HashMap<String,Integer> infected,HashMap<String,Integer> suspected,HashMap<String,Integer> cured,HashMap<String,Integer> died)
    {
        String[] str=line.split("\\s+");
        int change=Integer.parseInt(str[4].substring(0,str[4].indexOf("人")));//流动人数
        //流出省份疑似人数相应减少
        suspected.put(str[0],suspected.get(str[0])-change);
        //流入省份疑似人数相应增加
        if(suspected.containsKey(str[3]))
        {
            suspected.put(str[3],change+suspected.get(str[3]));
        }
        else
        {
            suspected.put(str[3],change);
        }
    }

    public String getReg()
    {
        return reg;
    }
}

class PatientsDie extends MyPatterns{
    //private Pattern patt=Pattern.compile(PatientsDie);
    public String reg="([\\u4e00-\\u9fa5])+ 死亡 (\\d+)人";//<省> 死亡 n人
    //根据
    public void doCount(String line,HashMap<String,Integer> infected,HashMap<String,Integer> suspected,HashMap<String,Integer> cured,HashMap<String,Integer> died)
    {
        String[] str=line.split("\\s+");
        int change=Integer.parseInt(str[2].substring(0,str[2].indexOf("人")));//死亡人数
        infected.put(str[0],infected.get(str[0])-change); //感染人数相应减少
        if(died.containsKey(str[0]))
        {
            died.put(str[0],died.get(str[0])+change);
        }
        else
        {
            died.put(str[0],change);
        }


    }

    public String getReg()
    {
        return reg;
    }
}

class PatientsCure extends MyPatterns{
    //private Pattern patt=Pattern.compile(PatientsCure);
    public String reg="([\\u4e00-\\u9fa5])+ 治愈 (\\d+)人";//<省> 治愈 n人
    //根据
    public void doCount(String line,HashMap<String,Integer> infected,HashMap<String,Integer> suspected,HashMap<String,Integer> cured,HashMap<String,Integer> died)
    {
        String[] str=line.split("\\s+");
        int change=Integer.parseInt(str[2].substring(0,str[2].indexOf("人")));//治愈人数
        infected.put(str[0],infected.get(str[0])-change); //感染人数相应减少
        if(cured.containsKey(str[0]))
        {
            cured.put(str[0],cured.get(str[0])+change);
        }
        else
        {
            cured.put(str[0],change);
        }


    }

    public String getReg()
    {
        return reg;
    }
}

class SuspectedDiagnosis extends MyPatterns{
    //private Pattern patt=Pattern.compile(SuspectedDiagnosis);
    public String reg="([\\u4e00-\\u9fa5])+ 疑似患者 确诊感染 (\\d+)人";//<省> 疑似患者 确诊感染 n人
    //根据
    public void doCount(String line,HashMap<String,Integer> infected,HashMap<String,Integer> suspected,HashMap<String,Integer> cured,HashMap<String,Integer> died)
    {
        String[] str=line.split("\\s+");
        int change=Integer.parseInt(str[3].substring(0,str[3].indexOf("人")));
        suspected.put(str[0],suspected.get(str[0])-change);
        if(infected.containsKey(str[0]))
        {
            infected.put(str[0],infected.get(str[0])+change);
        }
        else
        {
            infected.put(str[0],change);
        }
    }

    public String getReg()
    {
        return reg;
    }
}

class SuspectedExclude extends MyPatterns{
   // private Pattern patt=Pattern.compile(SuspectedExclude);
   public String reg="([\\u4e00-\\u9fa5])+ 排除 疑似患者 (\\d+)人";//<省> 排除 疑似患者 n人
    //根据
    public void doCount(String line,HashMap<String,Integer> infected,HashMap<String,Integer> suspected,HashMap<String,Integer> cured,HashMap<String,Integer> died)
    {
        String[] str=line.split("\\s+");
        int change=Integer.parseInt(str[3].substring(0,str[3].indexOf("人")));
        suspected.put(str[0],suspected.get(str[0])-change);
        System.out.println(str[0]+"排除疑似"+change);
    }

    public String getReg()
    {
        return reg;
    }
}
