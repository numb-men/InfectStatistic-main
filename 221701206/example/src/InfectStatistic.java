import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.nio.file.*;

/**
 * InfectStatistic
 * TODO
  *  疫情统计
 * @author 221701206_是九啊
 * @version 1.0
 * @since 2020.2.8
 */
class InfectStatistic {
	public static String LogLocation = "D:\\Java\\InfectStatistic-main\\example\\log";
	// LogLoction变量仅用于本机应用程序测试用，命令行测试将会注释赋值
    public static void main(String[] args) 
    {
    	DataCompareTool.getFileName("");
        System.out.println("helloworld");
    }
}


/**
 * DataCompareTool
 * TODO
 * @description 日期工具类
 * @author 221701206_是九啊
 * @version 1.0
 * @since 2020.2.10
 */
class DataCompareTool
{
	public static List<String> getFileName(String date)
	{
		List<String> FileName = new ArrayList<String>();
		String[] list=new File(InfectStatistic.LogLocation).list();
		for(int i = 0; i < list.length;i++)
		System.out.println(list[i]);
		
		
		return FileName;
		
	}
}

/**
 * FileControlTool
 * TODO
 * @description 文件读取操作工具类
 * @author 221701206_是九啊
 * @version 1.0
 * @since 2020.2.10
 */
class FileControlTool
{
	
}

/**
 * RegexTool
 * TODO
 * @description 正则表达式工具类
 * @author 221701206_是九啊
 * @version 1.0
 * @since 2020.2.10
 */
class RegexTool
{
	
}

/**
 * StaticTool
 * TODO
 * @description 数据统计工具类
 * @author 221701206_是九啊
 * @version 1.0
 * @since 2020.2.10
 */
class StaticTool
{
	
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










