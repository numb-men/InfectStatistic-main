import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.nio.charset.StandardCharsets;

/**
 * InfectStatistic
 * TODO
 *
 * @author KeVin
 * @version 0.2
 * @since 2020.2.12
 */
class InfectStatistic
{
    public static void main(String[] args)
    {
        String[] x=new String [10];
        x[0]="list";
        x[1]="-date";
        x[2]="2020-1-22";
        x[3]="-log";
        x[4]="F:\\GitHub\\InfectStatistic-main\\log\\";
        x[5]="-out";
        x[6]="F:\\GitHub\\InfectStatistic-main\\result\\ListOut1.txt";
        x[7]="-province";
        x[8]="福建";
        x[9]="安徽";
        try
        {
            if (x.length == 0)
            {
                throw new MyException("不能输入空命令!");
            }
            else if (!x[0].equals("list"))
            {
                throw new MyException("请输入正确命令!");
            }
            else
            {
                Command command = new Command(x);
                command.execute_command();
            }
        }
        catch(MyException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}

//参数类
class Parameter
{
    public String name;
    public boolean is_exist;
    public String param;
    public List<String> param_list;
    public int type;
    Parameter(String name,int type)
    {
        this.name = name;
        this.type = type;
        is_exist = false;
        param = "";
        param_list = new ArrayList<>();
    }
    public void set(String[] args) throws MyException
    {
        if(type == 1)
        {
            int i;
            for(i = 1;i<args.length;i++)
            {
                if(args[i].equals(name))
                {
                    is_exist = true;
                    String param = args[i+1];
                    break;
                }
            }
            if(i == args.length && param.equals(""))
                throw new MyException("log,out和date后必须含有参数");
        }
        if(type == 2)
        {
            for(int i=1;i<args.length;i++)
            {
                if(args[i].equals(name))
                {
                    is_exist = true;
                    i++;
                    while(i<args.length && !args[i].contains("-"))
                    {
                        param_list.add(args[i]);
                        i++;
                    }
                    break;
                }
            }
        }
    }

    public static List<File> get_file_list(File Dir)
    {
        String rule = "\\d{4}-\\{2}-\\d{2}.log.txt";
        List<File> filelist = new ArrayList<>();
        for(File file : Dir.listFiles())
        {
            if(file.getName().matches(rule))
            {
                filelist.add(file);
            }
        }
        return filelist;
    }
}

class Info
{
    public Map<String,List<Integer>> info;
    Info()
    {
        info = new LinkedHashMap<>();
        List<Integer> new_data = new ArrayList<>();
        new_data.add(0); //记录感染人数
        new_data.add(0); //记录疑似感染人数
        new_data.add(0); //记录愈人数
        new_data.add(0); //记录死亡人数
        info.put("全国",new ArrayList<>(new_data));
        info.put("安徽",new ArrayList<>(new_data));
        info.put("澳门",new ArrayList<>(new_data));
        info.put("北京",new ArrayList<>(new_data));
        info.put("重庆",new ArrayList<>(new_data));
        info.put("福建",new ArrayList<>(new_data));
        info.put("甘肃",new ArrayList<>(new_data));
        info.put("广东",new ArrayList<>(new_data));
        info.put("广西",new ArrayList<>(new_data));
        info.put("贵州",new ArrayList<>(new_data));
        info.put("河北",new ArrayList<>(new_data));
        info.put("海南",new ArrayList<>(new_data));
        info.put("河南",new ArrayList<>(new_data));
        info.put("黑龙江",new ArrayList<>(new_data));
        info.put("湖北",new ArrayList<>(new_data));
        info.put("湖南",new ArrayList<>(new_data));
        info.put("吉林",new ArrayList<>(new_data));
        info.put("江苏",new ArrayList<>(new_data));
        info.put("江西",new ArrayList<>(new_data));
        info.put("辽宁",new ArrayList<>(new_data));
        info.put("内蒙古",new ArrayList<>(new_data));
        info.put("宁夏",new ArrayList<>(new_data));
        info.put("青海",new ArrayList<>(new_data));
        info.put("山东",new ArrayList<>(new_data));
        info.put("山西",new ArrayList<>(new_data));
        info.put("陕西",new ArrayList<>(new_data));
        info.put("上海",new ArrayList<>(new_data));
        info.put("四川",new ArrayList<>(new_data));
        info.put("台湾",new ArrayList<>(new_data));
        info.put("天津",new ArrayList<>(new_data));
        info.put("香港",new ArrayList<>(new_data));
        info.put("西藏",new ArrayList<>(new_data));
        info.put("新疆",new ArrayList<>(new_data));
        info.put("云南",new ArrayList<>(new_data));
        info.put("浙江",new ArrayList<>(new_data));
    }

}

class Command
{
    private Parameter log = new Parameter("-log",1);
    private Parameter out = new Parameter("-out",1);
    private Parameter date = new Parameter("-date",1);
    private Parameter province = new Parameter("-province",2);
    private Parameter type = new Parameter("-type",2);
    //存放疫情最新信息
    private Info new_info;

    //记录log参数的目录信息
    private File log_dir;

    public Command(String[] args) throws MyException
    {
        log.set(args);
        out.set(args);
        date.set(args);
        province.set(args);
        type.set(args);
        new_info = new Info();
    }

    public void execute_command() throws MyException
    {
        if(!log.is_exist)
        {
            throw new MyException("必须输入log参数");
        }
        if(!out.is_exist)
        {
            throw new MyException("必须输入out参数");
        }
        if(date.is_exist && date.param == "")//若date后面无参数
        {
            throw new MyException("date后必须输入参数");
        }
        if(date.is_exist && date.param != "")//有date参数且date后参数不为空
        {
            log_dir = new File(log.param);
            if(!log_dir.exists())
            {
                throw new MyException("路径不存在!");
            }
//            execute_date(date.param,log_dir);
        }
    }

//    private void execute_date(String date,File log_dir) throws MyException
//    {
//        Date date_param;
//        BufferedReader reader  = null;
//        List<File> file_list = Parameter.get_file_list(log_dir);
//        DateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
//        try
//        {
//            List<Integer> all_data = new_info.info.get("全国");
//            date_param = date_format.parse(date);
//            for(File file : file_list)
//            {
//                String data_row;
//                Date file_date = date_format.parse(file.getName().substring(0,file.getName().indexOf(',')));
//                if(file_date.compareTo(date_param) > 0)
//                    continue;
//                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
//                while((data_row = reader.readLine()) != null)
//                {
//                    if(data_row.startsWith("//"))
//                        continue;
//                    String[] data = data_row.split(" ");
//                    if(data_row.contains("新增"))
//                    {
//                        List<Integer> p_data = new_info.info.get(data[0]);//获取省份信息
//
//                    }
//                }
//
//            }
//
//        }
//        catch(Exception ex)
//        {
//            throw new MyException(ex.getMessage());
//        }
//        finally
//        {
//        }
//    }



}

class MyException extends Exception //自定义异常类
{
    public MyException()
    {
        super();
    }
    public MyException(String s)
    {
        super(s);
    }

}

