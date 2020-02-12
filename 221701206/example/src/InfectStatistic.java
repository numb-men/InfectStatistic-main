import java.util.ArrayList;
import java.util.List;
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
    	
    	FileHandleTool.HandleFile();
    }
}


/**
 * DataCompareTool
 * @description 日期工具类
 * @author 221701206_�ǾŰ�
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
			System.out.println("���ڳ�����Χ");
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
						else
							System.out.println(line);
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
 * RegexTool
 * TODO
 * @description 正则表达筛选工具类
 * @author 221701206_是九啊
 * @version 1.0
 * @since 2020.2.10
 */
class RegexTool
{
	static int i = 1;
}

/**
 * StaticTool
 * TODO
 * @description 统计数字实体类
 * @author 221701206_�ǾŰ�
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
		StatisticLink.get(0).Comfirmed += RegexTool.i;
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










