import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.*;

/**
 * InfectStatistic
 * TODO
 * @author 221701206_是九啊
 * @version 1.0
 * @since 2020.2.8
 */
public class InfectStatistic 
{
	public static String LogLocation = "D:\\Java\\InfectStatistic-main\\example\\log";
	// LogLoction仅用于本机应用程序测试，命令行测试将注释赋值
	public static String DeadLine = "";
	// DeadLine仅用于本机应用程序测试，命令行测试将注释赋值
	public static String OutputLocation = "D:\\Java\\InfectStatistic-main\\example\\Result\\" + DeadLine + "out.txt";
	// OutputLocation仅用于本机应用程序测试，命令行测试将注释赋值
    public static void main(String[] args) 
    {
    	/*
    	 * List<String> FileName = DateCompareTool.getFileName("2020-01-22");
    	for(int i = 0 ; i < FileName.size(); i++)
    		System.out.println(FileName.get(i));
    	StatisticResult use = new StatisticResult();
    	use.Statistic();
    	use.Statistic();
    	System.out.println(use.StatisticLink.get(0).Comfirmed);
        System.out.println("helloworld");
        */
    	//文件处理测试
    	/*FileHandleTool.HandleFile();*/
    	
    	/*数据获取测试
    	DataGet a = new DataGet();
    	a.getData("湖北省 新增 感染患者 15人");
    	*/
    	//输出测试
    	//if(args[0].equals("list"))
    	/*CommandGet Command = new CommandGet();
    	Command.GetCommand(args);
    	System.out.println(Command.IsDate);
    	System.out.println(Command.Date);
    	System.out.println(Command.IsProvince);
    	for(int i = 0 ; i < Command.Province.length ; i ++)
    		System.out.println(Command.Province[i]);
    	System.out.println(Command.IsType);
    	for(int i = 0 ; i < Command.Type.length ; i ++)
    		System.out.println(Command.Type[i]);*/
    	//OutputControlTool.ProductInfectStatistic();
    	CommandGet Command_execute = new CommandGet();
    	Command_execute.GetCommand(args);
    	InfectSatisticCommandSet Execute = new InfectSatisticCommandSet();
    	Execute.execute(Command_execute);
    }
}

/**
 * InfectSatisticCommandSet
 * @description 命令模式，用于扩展
 * @author 221701206_是九啊
 * @version 1.0
 * @since 2020.2.15
 */
class InfectSatisticCommandSet
{
	Command[] Operations ;
	public InfectSatisticCommandSet()
	{
		Operations = new Command[1]; //数组数量取决于共有几个命令功能
		Operations[0] = new CommandList();
	}
	
	public void execute(CommandGet Command_execute)
	{
		Operations[Command_execute.Command - 1].execute(Command_execute);
	}
}

/**
 * Command
 * @description 命令接口
 * @author 221701206_是九啊
 * @version 1.0
 * @since 2020.2.15
 */
interface Command
{
	public void execute(CommandGet Command_execute);
}

/**
 * CommandList
 * @description List命令
 * @author 221701206_是九啊
 * @version 1.0
 * @since 2020.2.15
 */
class CommandList implements Command
{
	@Override
	public void execute(CommandGet Command_execute) 
	{
		OutputControlTool.ProductInfectStatistic(Command_execute.Date,Command_execute.Log,Command_execute.Out
				                                ,Command_execute.Province,Command_execute.Type);
	}
	
}

/**
 * CommandGet
 * @description 命令行命令类
 * @author 221701206_是九啊
 * @version 1.0
 * @since 2020.2.15
 */
class CommandGet
{
	int Command ; // 用于存储执行的命令，1.list
	boolean IsProvince;
	boolean IsDate;
	boolean IsType;
	String Log;
	String Out;
	String Date;
	String[] Province;
	String[] Type;
	public CommandGet()
	{
		this.Command = 0;
		this.IsProvince = false;
		this.IsDate = false;
		this.IsType = false;
		this.Log = "";
		this.Out = "";
		this.Date = "";
		this.Province = new String[31];
		this.Type = new String[4];
		for(int i = 0; i < this.Province.length; i++)
			Province[i] = "";
		for(int i = 0; i < this.Type.length; i++)
			Type[i] = "";
	}
	
	public void GetCommand(String[] CommandSource)
	{
		int CommandLength = CommandSource.length;
		if(CommandSource[0].equals("list"))
			this.Command = 1;
		else
			System.out.println(CommandSource[0] + " 不能被解析为合法命令");
		for(int i = 1 ; i < CommandLength ; i++)
		{
			if(CommandSource[i].equals("-log"))
			{
				this.Log = CommandSource[i+1];
				i++;
			}

			else if(CommandSource[i].equals("-out"))
			{
				this.Out = CommandSource[i+1];
				i++;
			}

			else if(CommandSource[i].equals("-date"))
			{
				this.IsDate = true;
				this.Date = CommandSource[i+1];
				i++;
			}
			else if(CommandSource[i].equals("-province"))
			{
				this.IsProvince = true;
				int j = 0;
				while(i+1<CommandSource.length&&!Pattern.matches("-.*", CommandSource[i+1]))
				{
					this.Province[j] = CommandSource[i+1];
					i++;
					j++;
				}
			}
			else if(CommandSource[i].equals("-type"))
			{
				this.IsType = true;
				int j = 0;
				while(i+1<CommandSource.length && !Pattern.matches("-.*", CommandSource[i+1]))
				{
					this.Type[j] = CommandSource[i+1];
					i++;
					j++;
				}
			}
			else 
				System.out.println("命令中包含不合法参数" + CommandSource[i]);
		}
	}
}


/**
 * DataCompareTool
 * @description 日期工具类
 * @author 221701206_是九啊
 * @version 1.0
 * @since 2020.2.10
 */
class DateCompareTool
{
	/*
	 * getFileName
	 * @description 根据目标日期筛选出需要处理的文件路径
	 * @author 221701206_是九啊
     * @version 1.0
     * @since 2020.2.12
	 */
	public static List<String> getFileName(String Date,String LogLocation)
	{
		List<String> FileName = new ArrayList<String>();
		String[] FileList=new File(LogLocation).list();
		int MaxMonth = 0;
		int MaxDay = 0;
		//下述判断用于解决是否带 -date参数问题
		if(Date.equals(""))
		{
			for(int i = 0; i < FileList.length; i++)
				FileName.add(LogLocation + "\\" + FileList[i]);
			return FileName;
		}
		else
		{
			boolean DateOutbound = false;
			String[] SplitDate = Date.split("-");
			String MonthDate = SplitDate[1];
			String DayDate = SplitDate[2];
			int TargetMonth = Integer.valueOf(MonthDate);
			int TargetDay = Integer.valueOf(DayDate);
			for(int i = 0;i < FileList.length ;i++)
			{
				String[] SplitFileName = FileList[i].split("-");
				String Month = SplitFileName[1];
				String Day = SplitFileName[2].split(".log")[0];
				int FileMonth = Integer.valueOf(Month);
				int FileDay = Integer.valueOf(Day);
				if(FileMonth > MaxMonth)
					MaxMonth = FileMonth;
				if(FileDay > MaxDay)
					MaxDay = FileDay;
				if(FileMonth < TargetMonth)
					FileName.add(LogLocation + "\\" + FileList[i]);
				if(FileMonth == TargetMonth && FileDay <= TargetDay)
					FileName.add(LogLocation + "\\" + FileList[i]);
			}
			if(TargetMonth > MaxMonth || (TargetMonth == MaxMonth && TargetDay > MaxDay))
				DateOutbound = true;
			if(DateOutbound)
			{
				System.out.println("日期超过范围");
				System.exit(1);
			}
			return FileName;
		}
	}
}

/**
 * FileControlTool
 * TODO
 * @description 文件读取处理工具类
 * @author 221701206_是九啊
 * @version 1.0
 * @since 2020.2.10
 */
class FileHandleTool
{
	public static List<String> OutResult = new ArrayList<String>();

	/**
	 * HandleFile
	 * @description 用于读取文件内容，并调用数据处理和统计方法，按行处理文件
	 * @author 221701206_是九啊
	 * @version 1.0
	 * @since 2020.2.12
	 */
	public static List<String> HandleFile(String DeadLine,String LogLocation,String[] Province, String[] Type)
	{
		List<String> FileNames = DateCompareTool.getFileName(DeadLine,LogLocation);
		StatisticResult Result = new StatisticResult();
		for(int i = 0 ; i < FileNames.size() ; i ++)
		{
			String FilePath = FileNames.get(i);
			try
			{
				FileInputStream Is = new FileInputStream(FilePath);
				InputStreamReader Isr = new InputStreamReader(Is,"utf-8");
				BufferedReader Br = new BufferedReader(Isr);
				String line;
				try
				{
					while((line = Br.readLine()) != null)
					{
						if(line.equals(""))
							continue;
						if(Pattern.matches("//.*", line))
							continue;
						else
						{
							DataGet Use = new DataGet();
							Use.getData(line);
							Result.Statistic(Use);
						}
					}
				}
				catch(IOException e)
				{
					e.printStackTrace();
					System.err.println("读取一行文件错误");
				}
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
				System.err.println("文件路径错误，找不到指定文件");
			} catch (UnsupportedEncodingException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}
		if(Province[0].equals("")&&Type[0].equals(""))
		{
			for(int i = 0 ; i < Result.ProvinceOccur.length; i++)
			{
				if(Result.ProvinceOccur[i])
				{
					OutResult.add(Result.ProvinceIndex.get(i) + " 感染患者" + Result.StatisticLink.get(i).Comfirmed + "人"
							            + " 疑似患者" + Result.StatisticLink.get(i).Suspected + "人"
							            + " 治愈" + Result.StatisticLink.get(i).Healed + "人"
							            + " 死亡" + Result.StatisticLink.get(i).Dead + "人");
				}
			}
			OutResult.add("// 该文档并非真实数据，仅供测试使用");
		}
		if(Province[0].equals("") && !Type[0].equals(""))
		{
			for(int i = 0 ; i < Result.ProvinceOccur.length; i++)
			{
				if(Result.ProvinceOccur[i])
				{
					String Data = Result.ProvinceIndex.get(i);
					for(int j = 0 ; j < 4 ; j ++)
					{
						if(Type[j].equals(""))
							break;
						else if(Type[j].equals("ip"))
							Data += " 感染患者" + Result.StatisticLink.get(i).Comfirmed + "人";
						else if(Type[j].equals("sp"))
							Data += " 疑似患者" + Result.StatisticLink.get(i).Suspected + "人";
						else if(Type[j].equals("cure"))
							Data += " 治愈" + Result.StatisticLink.get(i).Healed + "人";
						else if(Type[j].equals("dead"))
							Data += " 死亡" + Result.StatisticLink.get(i).Dead + "人";
					}
					OutResult.add(Data);
				}
			}
			OutResult.add("// 该文档并非真实数据，仅供测试使用");
		}
		if(!Province[0].equals("") && Type[0].equals(""))
		{
			for(int i = 0 ; i < 32; i++)
				Result.ProvinceOccur[i] = false;
			for(int i = 0 ; i < 32; i++)
			{
				if(Province[i].equals(""))
					break;
				Result.ProvinceOccur[Result.ProvinceIndex.indexOf(Province[i])] = true;
			}
			for(int i = 0 ; i < Result.ProvinceOccur.length; i++)
			{
				if(Result.ProvinceOccur[i])
				{
					OutResult.add(Result.ProvinceIndex.get(i) + " 感染患者" + Result.StatisticLink.get(i).Comfirmed + "人"
							            + " 疑似患者" + Result.StatisticLink.get(i).Suspected + "人"
							            + " 治愈" + Result.StatisticLink.get(i).Healed + "人"
							            + " 死亡" + Result.StatisticLink.get(i).Dead + "人");
				}
			}
			OutResult.add("// 该文档并非真实数据，仅供测试使用");
		}
		
		else
		{
			for(int i = 0 ; i < 32; i++)
				Result.ProvinceOccur[i] = false;
			for(int i = 0 ; i < 32; i++)
			{
				if(Province[i].equals(""))
					break;
				Result.ProvinceOccur[Result.ProvinceIndex.indexOf(Province[i])] = true;
			}
			
			for(int i = 0 ; i < Result.ProvinceOccur.length; i++)
			{
				if(Result.ProvinceOccur[i])
				{
					String Data = Result.ProvinceIndex.get(i);
					for(int j = 0 ; j < 4 ; j ++)
					{
						if(Type[j].equals(""))
							break;
						else if(Type[j].equals("ip"))
							Data += " 感染患者" + Result.StatisticLink.get(i).Comfirmed + "人";
						else if(Type[j].equals("sp"))
							Data += " 疑似患者" + Result.StatisticLink.get(i).Suspected + "人";
						else if(Type[j].equals("cure"))
							Data += " 治愈" + Result.StatisticLink.get(i).Healed + "人";
						else if(Type[j].equals("dead"))
							Data += " 死亡" + Result.StatisticLink.get(i).Dead + "人";
					}
					OutResult.add(Data);
				}
			}
			OutResult.add("// 该文档并非真实数据，仅供测试使用");
		}
		return OutResult;
	}
}

/**
 * DataGet
 * TODO
 * @description 数据处理实体类，用于处理每行记录，获取其中数据
 * @author 221701206_是九啊
 * @version 1.0
 * @since 2020.2.10
 */
class DataGet
{
	int Type;
	/*
	 * Type
	 * @description 每行记录一共可分为八种类型，分别对应数字索引1~8，根据不同类型，最终产生不同数据
	 * 1、<省> 新增 感染患者 n人
	   2、<省> 新增 疑似患者 n人
	   3、<省1> 感染患者 流入 <省2> n人
	   4、<省1> 疑似患者 流入 <省2> n人
	   5、<省> 死亡 n人
	   6、<省> 治愈 n人
	   7、<省> 疑似患者 确诊感染 n人
	   8、<省> 排除 疑似患者 n人
	 */
	String Province1;
	String Province2;
	int Number;
	
	public DataGet()
	{
		this.Type = 0;
		this.Province1 = "";
		this.Province2 = "";
		this.Number = 0;
	}
	
	/**
	 * getData
	 * @description 根据正则表达式匹配每行的模式，获取相应数据用于统计
	 * @author 221701206_是九啊
	 * @version 1.0
	 * @since 2020.2.11
	 */
	public void getData(String line)
	{
		String Type1 = "\\W+ 新增 感染患者 \\d+人";
		String Type2 = "\\W+ 新增 疑似患者 \\d+人";
		String Type3 = "\\W+ 感染患者 流入 \\W+ \\d+人";
		String Type4 = "\\W+ 疑似患者 流入 \\W+ \\d+人";
		String Type5 = "\\W+ 死亡 \\d+人";
		String Type6 = "\\W+ 治愈 \\d+人";
		String Type7 = "\\W+ 疑似患者 确诊感染 \\d+人";
		String Type8 = "\\W+ 排除 疑似患者 \\d+人";
		
		if(Pattern.matches(Type1, line))
			this.Type = 1;
		if(Pattern.matches(Type2, line))
			this.Type = 2;
		if(Pattern.matches(Type3, line))
			this.Type = 3;
		if(Pattern.matches(Type4, line))
			this.Type = 4;
		if(Pattern.matches(Type5, line))
			this.Type = 5;
		if(Pattern.matches(Type6, line))
			this.Type = 6;
		if(Pattern.matches(Type7, line))
			this.Type = 7;
		if(Pattern.matches(Type8, line))
			this.Type = 8;
		
		String[] SplitLine = line.split(" ");
		if(this.Type == 1 || this.Type == 2)
		{
			this.Province1 = SplitLine[0];
			this.Number = Integer.valueOf(SplitLine[3].split("人")[0]);
		}
		
		if(this.Type == 3 || this.Type == 4)
		{
			this.Province1 = SplitLine[0];
			this.Province2 = SplitLine[3];
			this.Number = Integer.valueOf(SplitLine[4].split("人")[0]);
		}
		
		if(this.Type == 5 || this.Type == 6)
		{
			this.Province1 = SplitLine[0];
			this.Number = Integer.valueOf(SplitLine[2].split("人")[0]);
		}
		
		if(this.Type == 7 || this.Type == 8)
		{
			this.Province1 = SplitLine[0];
			this.Number = Integer.valueOf(SplitLine[3].split("人")[0]);
		}
	}
}

/**
 * StatisticResult
 * @description 统计数字实体类
 * @author 221701206_是九啊
 * @version 1.0
 * @since 2020.2.10
 */
class StatisticResult
{
	/**
	 * 统计顺序按各省首字母排序，依次为
	 * 全国0、安徽1、北京2、重庆3、福建4、甘肃5、广东6、广西7、贵州8、海南9、河北10、河南11
     * 黑龙江12、湖北13、湖南14、吉林15、江苏16、江西17、辽宁18、内蒙古19、宁夏20、青海21、山东22、山西23
     * 陕西24、上海25、四川26、天津27、西藏28、新疆29、云南30、浙江31
	 */
	
	boolean[] ProvinceOccur = new boolean[32]; //用于判断省份是否出现过
	List<String> ProvinceIndex = new ArrayList<String>();
	List<PeopleType> StatisticLink = new ArrayList<PeopleType>();
	public StatisticResult()
	{
		for(int i = 0; i < 32 ; i ++)
			ProvinceOccur[i] = false;
		ProvinceOccur[0] = true;
		for(int i = 0; i < 32 ; i ++)
		{
			PeopleType Temp = new PeopleType();
			StatisticLink.add(Temp);
		}
		ProvinceIndex.add("全国");
		ProvinceIndex.add("安徽");
		ProvinceIndex.add("北京");
		ProvinceIndex.add("重庆");
		ProvinceIndex.add("福建");
		ProvinceIndex.add("甘肃");
		ProvinceIndex.add("广东");
		ProvinceIndex.add("广西");
		ProvinceIndex.add("贵州");
		ProvinceIndex.add("海南");
		ProvinceIndex.add("河北");
		ProvinceIndex.add("河南");
		ProvinceIndex.add("黑龙江");
		ProvinceIndex.add("湖北");
		ProvinceIndex.add("湖南");
		ProvinceIndex.add("吉林");
		ProvinceIndex.add("江苏");
		ProvinceIndex.add("江西");
		ProvinceIndex.add("辽宁");
		ProvinceIndex.add("内蒙古");
		ProvinceIndex.add("宁夏");
		ProvinceIndex.add("青海");
		ProvinceIndex.add("山东");
		ProvinceIndex.add("山西");
		ProvinceIndex.add("陕西");
		ProvinceIndex.add("上海");
		ProvinceIndex.add("四川");
		ProvinceIndex.add("天津");
		ProvinceIndex.add("西藏");
		ProvinceIndex.add("新疆");
		ProvinceIndex.add("云南");
		ProvinceIndex.add("浙江");
	}
	
	/**
	 * Statistic
	 * @description 统计，产生结果
	 * @author 221701206_是九啊
	 * @version 1.0
	 * @since 2020.2.12
	 */
	public void Statistic(DataGet data)
	{
		if(data.Type == 1)
		{
			int ProvinceIndex_1 = ProvinceIndex.indexOf(data.Province1);
			StatisticLink.get(ProvinceIndex_1).Comfirmed += data.Number;
			StatisticLink.get(0).Comfirmed += data.Number;
			ProvinceOccur[ProvinceIndex_1] = true;
		}
		if(data.Type == 2)
		{
			int ProvinceIndex_1 = ProvinceIndex.indexOf(data.Province1);
			StatisticLink.get(ProvinceIndex_1).Suspected += data.Number;
			StatisticLink.get(0).Suspected += data.Number;
			ProvinceOccur[ProvinceIndex_1] = true;
		}
		if(data.Type == 3)
		{
			int ProvinceIndex_1 = ProvinceIndex.indexOf(data.Province1);
			int ProvinceIndex_2 = ProvinceIndex.indexOf(data.Province2);
			StatisticLink.get(ProvinceIndex_1).Comfirmed -= data.Number;
			StatisticLink.get(ProvinceIndex_2).Comfirmed += data.Number;
			ProvinceOccur[ProvinceIndex_1] = true;
			ProvinceOccur[ProvinceIndex_2] = true;
		}
		if(data.Type == 4)
		{
			int ProvinceIndex_1 = ProvinceIndex.indexOf(data.Province1);
			int ProvinceIndex_2 = ProvinceIndex.indexOf(data.Province2);
			StatisticLink.get(ProvinceIndex_1).Suspected -= data.Number;
			StatisticLink.get(ProvinceIndex_2).Suspected += data.Number;
			ProvinceOccur[ProvinceIndex_1] = true;
			ProvinceOccur[ProvinceIndex_2] = true;
		}
		if(data.Type == 5)
		{
			int ProvinceIndex_1 = ProvinceIndex.indexOf(data.Province1);
			StatisticLink.get(ProvinceIndex_1).Dead += data.Number;
			StatisticLink.get(0).Dead += data.Number;
			StatisticLink.get(ProvinceIndex_1).Comfirmed -= data.Number;
			StatisticLink.get(0).Comfirmed -= data.Number;
			ProvinceOccur[ProvinceIndex_1] = true;
		}
		if(data.Type == 6)
		{
			int ProvinceIndex_1 = ProvinceIndex.indexOf(data.Province1);
			StatisticLink.get(ProvinceIndex_1).Healed += data.Number;
			StatisticLink.get(0).Healed += data.Number;
			StatisticLink.get(ProvinceIndex_1).Comfirmed -= data.Number;
			StatisticLink.get(0).Comfirmed -= data.Number;
			ProvinceOccur[ProvinceIndex_1] = true;
		}
		if(data.Type == 7)
		{
			int ProvinceIndex_1 = ProvinceIndex.indexOf(data.Province1);
			StatisticLink.get(ProvinceIndex_1).Comfirmed += data.Number;
			StatisticLink.get(0).Comfirmed += data.Number;
			StatisticLink.get(ProvinceIndex_1).Suspected -= data.Number;
			StatisticLink.get(0).Suspected -= data.Number;
			ProvinceOccur[ProvinceIndex_1] = true;
		}
		if(data.Type == 8)
		{
			int ProvinceIndex_1 = ProvinceIndex.indexOf(data.Province1);
			StatisticLink.get(ProvinceIndex_1).Suspected -= data.Number;
			StatisticLink.get(0).Suspected -= data.Number;
			ProvinceOccur[ProvinceIndex_1] = true;
		}
	}
	
	
}

/**
 * OutputControl
 * TODO
 * @description 输出控制工具类
 * @author 221701206_是九啊
 * @version 1.0
 * @since 2020.2.10
 */
class OutputControlTool
{
	/**
	 * ProductInfectStatistic
	 * @description 对结果进行文件输出重定向
	 * @author 221701206_是九啊
	 * @version 1.0
	 * @since 2020.2.13
	 */
	public static void ProductInfectStatistic(String DeadLine, String LogLocation,String ResultLocation,String[] Province,String[] Type)
	{
		File TargetFile = new File(ResultLocation);
		if(TargetFile.exists())
		{
			System.out.println("该日志文档已存在，请修改目标文件名");
			return ;
		}
		else
		{
			try
			{    
				TargetFile.createNewFile();    
			} 
			catch (IOException e) 
			{     
				System.err.println("该目标文件无法新建，请重试");
				e.printStackTrace();    
			}       
		}
		List<String> Result = FileHandleTool.HandleFile(DeadLine,LogLocation,Province,Type);
		try
		{
			OutputStream out = new FileOutputStream(TargetFile);
            BufferedWriter rd = new BufferedWriter(new OutputStreamWriter(out,"utf-8"));
            for(int i = 0; i < Result.size() ; i ++)
    	         rd.write(Result.get(i) + "\n");
	         rd.close();
	         out.close();
		}
		catch(IOException e){
			System.err.println("文件写入错误");
            e.printStackTrace();
        }
		System.out.println(ResultLocation + "文件已生成");
	}
}

/*
 * PeopleType
 * @description 编码是新增实体类，用于存储各省份各类患者人数
 * @author 221701206_是九啊
 * @version 1.0
 * @since 2020.2.10 
 */
class PeopleType
{
	int Comfirmed;
	int Suspected;
	int Healed;
	int Dead;
	
	public PeopleType()
	{
		this.Comfirmed = 0;
		this.Suspected = 0;
		this.Healed = 0;
		this.Dead = 0;
	}
}










