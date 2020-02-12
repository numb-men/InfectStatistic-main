import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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
	public static String DeadLine = "2020-01-23";
	// DeadLine仅用于本机应用程序测试，命令行测试将注释赋值
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
    	//文件处理模块测试
    	FileHandleTool.HandleFile();
    	
    	/*数据获取模块测试
    	DataGet a = new DataGet();
    	a.getData("湖北省 新增 感染患者 15人");
    	*/
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
	public static List<String> getFileName(String Date)
	{
		List<String> FileName = new ArrayList<String>();
		boolean DateOutbound = false;
		int MaxMonth = 0;
		int MaxDay = 0;
		String[] FileList=new File(InfectStatistic.LogLocation).list();
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
				FileName.add(InfectStatistic.LogLocation + "\\" + FileList[i]);
			if(FileMonth == TargetMonth && FileDay <= TargetDay)
				FileName.add(InfectStatistic.LogLocation + "\\" + FileList[i]);
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
	public static void HandleFile()
	{
		List<String> FileNames = DateCompareTool.getFileName(InfectStatistic.DeadLine);
		for(int i = 0 ; i < FileNames.size() ; i ++)
		{
			String FilePath = FileNames.get(i);
			try
			{
				FileInputStream Is = new FileInputStream(FilePath);
				InputStreamReader Isr = new InputStreamReader(Is);
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
							if(Use.Type == 1 || Use.Type == 2)
								System.out.println(Use.Type + " " + Use.Province1 + " " + Use.Number);
							if(Use.Type == 3 || Use.Type == 4)
								System.out.println(Use.Type + " " + Use.Province1 + " " + Use.Province2 + " " + Use.Number);
							if(Use.Type == 5 || Use.Type == 6)
								System.out.println(Use.Type + " " + Use.Province1 + " " + Use.Number);
							if(Use.Type == 7 || Use.Type == 8)
								System.out.println(Use.Type + " " + Use.Province1 + " " + Use.Number);
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
			}
		}
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
 * TODO
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
	List<PeopleType> StatisticLink = new ArrayList<PeopleType>();
	public StatisticResult()
	{
		for(int i = 0; i < 32 ; i ++)
		{
			PeopleType Temp = new PeopleType();
			StatisticLink.add(Temp);
		}
	}
	public void Statistic()
	{
		//StatisticLink.get(0).Comfirmed += RegexTool.i;
	}
	
	
}

/**
 * OutputControl
 * TODO
 * @description 输出控制类
 * @author 221701206_是九啊
 * @version 1.0
 * @since 2020.2.10
 */
class OutputControl
{
	
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










