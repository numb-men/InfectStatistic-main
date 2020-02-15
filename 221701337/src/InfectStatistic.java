import java.io.*;
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
        String[] x=new String [11];
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
        x[10]="-type";
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
                    param = args[i+1];
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

    public static List<File> get_file_list(String dir) throws MyException
    {
        File Dir = new File(dir);
        if(!Dir.exists())
        {
            throw new MyException("路径不存在!");
        }
        String rule = "\\d{4}-\\d{2}-\\d{2}.log.txt";
        List<File> file_list = new ArrayList<>();
        for(File file : Dir.listFiles())
        {
            if(file.getName().matches(rule))
            {
                file_list.add(file);
            }
        }
        return file_list;
    }
}

class Info
{
    public Map<String,List<Integer>> info;
    public List<String> out_province;//记录最后需要输出的省份
    public List<String> out_type;//记录最后需要输出的类型

    Info()
    {
        out_province = new ArrayList<>();
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

    public void add_province(String province)
    {
        if(out_province.contains(province))
            return;
        out_province.add(province);
    }

    public void add_type(String type)
    {
        if(out_type.contains(type))
            return;
        out_type.add(type);
    }

    public void Infected(String province,int Count)
    {
        List<Integer> province_list = info.get(province);
        List<Integer> Country_list = info.get("全国");
        int province_count = province_list.get(0);
        province_count = province_count + Count;

        int country_count = Country_list.get(0);
        country_count = country_count + Count;

        province_list.set(0,province_count);
        Country_list.set(0,country_count);
    }

    public void Suspected(String province,int Count)
    {
        List<Integer> province_list = info.get(province);
        List<Integer> Country_list = info.get("全国");
        int province_count = province_list.get(1);
        province_count = province_count + Count;

        int country_count = Country_list.get(1);
        country_count = country_count + Count;

        province_list.set(1,province_count);
        Country_list.set(1,country_count);
    }

    public void Inflow(String out,String in,int Count,int flag)
    {
        List<Integer> out_list = info.get(out);
        List<Integer> in_list = info.get(in);
        int out_count = out_list.get(flag);
        out_count = out_count - Count;
        int in_count = in_list.get(flag);
        in_count = in_count + Count;

        out_list.set(flag,out_count);
        in_list.set(flag,in_count);
    }

    public void Cured(String province,int Count)
    {
        List<Integer> province_list = info.get(province);
        List<Integer> country_list = info.get("全国");
        int cure_count = province_list.get(2);
        int cure_country_count = country_list.get(2);
        int infectd_count = province_list.get(0);
        int infectd_country_count = country_list.get(0);

        cure_count = cure_count + Count;
        province_list.set(2,cure_count);
        cure_country_count = cure_country_count + Count;
        country_list.set(2,cure_country_count);
        infectd_count = infectd_count - Count;
        province_list.set(0,infectd_count);
        infectd_country_count = infectd_country_count - Count;
        country_list.set(0,infectd_country_count);
    }

    public void Dead(String province,int Count)
    {
        List<Integer> province_list = info.get(province);
        List<Integer> country_list = info.get("全国");
        int dead_count = province_list.get(3);
        int dead_country_count = province_list.get(3);
        int infected_count = province_list.get(0);
        int infected_country_count = country_list.get(0);

        dead_count = dead_count + Count;
        province_list.set(3,dead_count);
        dead_country_count = dead_country_count + Count;
        country_list.set(3,dead_country_count);
        infected_count = infected_count - Count;
        province_list.set(0,infected_count);
        infected_country_count = infected_country_count - Count;
        country_list.set(0,infected_country_count);
    }

    public void Diagnosis(String province,int Count)
    {
        List<Integer> province_list = info.get(province);
        List<Integer> country_list = info.get("全国");
        int infected_count = province_list.get(0);
        int infected_country_count = country_list.get(0);
        int suspected_count = province_list.get(1);
        int suspected_country_count = country_list.get(1);

        infected_count = infected_count + Count;
        province_list.set(0,infected_count);
        infected_country_count = infected_country_count + Count;
        country_list.set(0,infected_country_count);

        suspected_count = suspected_count - Count;
        province_list.set(1,suspected_count);
        suspected_country_count = suspected_country_count - Count;
        country_list.set(1,suspected_country_count);
    }

    public void Exclude(String province,int Count)
    {
        List<Integer> province_list = info.get(province);
        List<Integer> country_list = info.get("全国");
        int suspected_count = province_list.get(1);
        int suspected_country_count = country_list.get(1);

        suspected_count = suspected_count - Count;
        province_list.set(1,suspected_count);
        suspected_country_count = suspected_country_count - Count;
        country_list.set(1,suspected_country_count);
    }

    public void doOut()
    {
        for(String province : info.keySet())
        {
            if(!out_province.contains(province))
                continue;
            List<Integer> data = info.get(province);
        }
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
            List<File> file_list = Parameter.get_file_list(log.param);
            execute_date(date.param,file_list,date.is_exist);
            new_info.doOut();
        }
        if(!date.is_exist)
        {
            List<File> file_list = Parameter.get_file_list(log.param);
            execute_date(date.param,file_list,date.is_exist);
        }
        if(type.is_exist)
        {
            execute_type(type.param_list);
        }
    }

    private void execute_date(String date,List<File> file_list,boolean exist) throws MyException
    {
        Date date_param;
        BufferedReader reader  = null;
        DateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            date_param = date_format.parse(date);
            Date date_now = date_format.parse(date_format.format(new Date()).toString());
            if(date_param.compareTo(date_now) > 0)
                throw new MyException("输入时间大于系统当前时间");
            if(!exist)
                date_param = date_now;
            for(File file : file_list)
            {
                String data_row;
                Date file_date = date_format.parse(file.getName().substring(0,file.getName().indexOf('.')));
                if(file_date.compareTo(date_param) > 0)
                    continue;
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
                while((data_row = reader.readLine()) != null)
                {
                    if(data_row.startsWith("//"))
                        continue;
                    String[] data = data_row.split(" ");
                    new_info.add_province(data[0]);
                    if(data_row.contains("新增"))
                    {
                        int count = Integer.parseInt(data[3].substring(0,data[3].length() - 1));
                        if(data[2].equals("感染患者"))
                        {
                            new_info.Infected(data[0],count);
                        }
                        if(data[2].equals("疑似患者"))
                        {
                            new_info.Suspected(data[0],count);
                        }
                    }
                    if(data_row.contains("流入"))
                    {
                        int count = Integer.parseInt(data[4].substring(0,data[4].length() - 1));
                        if(data[1].equals("感染患者"))
                        {
                            new_info.Inflow(data[0],data[3],count,0);
                        }
                        if(data[1].equals("疑似患者"))
                        {
                            new_info.Inflow(data[0],data[3],count,1);
                        }
                    }
                    if(data_row.contains("治愈"))
                    {
                        int count = Integer.parseInt(data[2].substring(0,data[2].length() - 1));
                        new_info.Cured(data[0],count);
                    }
                    if(data_row.contains("死亡"))
                    {
                        int count = Integer.parseInt(data[2].substring(0,data[2].length() - 1));
                        new_info.Dead(data[0],count);
                    }
                    if (data_row.contains("确诊感染"))
                    {
                        int count = Integer.parseInt(data[3].substring(0,data[3].length() - 1));
                        new_info.Diagnosis(data[0],count);
                    }
                    if (data_row.contains("排除"))
                    {
                        int count = Integer.parseInt(data[3].substring(0,data[3].length() - 1));
                        new_info.Exclude(data[0],count);
                    }
                }

            }

        }
        catch(Exception ex)
        {
            throw new MyException(ex.getMessage());
        }
        finally
        {
            try
            {
                if (reader != null)
                {
                    reader.close();
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public void execute_type(List<String> type_param) throws MyException
    {
        if(type_param.size() == 0)
            return;
        for(String type : type_param)
        {
            switch (type)
            {
                case "ip":
                    new_info.add_type("ip");
                    break;
                case "sp":
                    new_info.add_type("sp");
                    break;
                case "cure":
                    new_info.add_type("cure");
                    break;
                case "dead":
                    new_info.add_type("dead");
                    break;
                default:
                    throw new MyException("-type请输入正确参数(ip,sp,cure,dead)");
            }
        }
    }

    public void execute_province(List<String> province_param) throws MyException
    {
        for(String province : province_param)
        {
            switch (province)
            {
            }
        }
    }



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

