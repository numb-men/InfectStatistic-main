/**
 * InfectStatistic
 * TODO
 *
 * @author 爱学习的水先生
 * @version 1.0
 * @since 2020/2/11
 */


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 抽象命令接口，用于命令模式的设计
 */
interface Command
{
	public void execute(String[] command);
}

/*
 * 请求接收者类,类方法用于执行各种命令
 */
class ReceiveCommand
{
	public void listDate()
	{
		System.out.println("接受到listDate命令，正在执行中......");
	}
	
	public void listType()
	{
		System.out.println("接受到listType命令，正在执行中......");
	}
	
	public void listProvince()
	{
		System.out.println("接受到listProvince命令，正在执行中......");
	}
}

/*
 * 命令发送者控制器类，用于封装命令
 */
class SendCommandController
{
	private Command list;
	
	public SendCommandController(Command list) 
	{
		this.list = list;
	}
	
	/*
	 * list命令执行方法，通过switch+正则选择不同选项的不同执行方法
	 */
	public void list(String[] command)
	{ 
		list.execute(command);
	}
}

/*
 * List命令执行
 */
class List implements Command
{
	private ReceiveCommand rc;
	
	public List() 
	{
		this.rc = new ReceiveCommand();
	}
	
	/*********************
	 * 执行不同选项的list命令，switch区分不同选项执行的函数
	 ********************/
	 public void execute(String[] command)
	{		
		String option = "";
		String readPath = "";
		String outPath = "";
		option = command[1];//存放命令选项
		
		switch(option)
		{
			case "-date":
				readPath = command[4];
				outPath = command[6];
				if(!isRightPath.pathRight(readPath)||!isRightPath.outExist(outPath))
				{
					System.out.println("路径错误");
					break;
				}
				rc.listDate();
				break;
				
			case "-type":
				rc.listType();
				break;
				
			case "-province":
				rc.listProvince();
				break;
				
			default:
				System.out.println("没找到list的 " + option + "选项");
				break;
		}
		
	}
}

/*
 * 路径检测类
 */
class isRightPath
{		
	
	/*检验文件路径是否存在*/
	public static boolean pathRight(String path) 
	{
		File f = new File(path);
		return f.exists();
	}
	
	/*检验计算机输出文件是否存在不存在则创建*/
	public static boolean outExist(String path) 
	{
		File file = new File(path);
		if (!file.exists()) 
		{		
			try 
			{
			    file.createNewFile();
			    
			} 
			catch (IOException e) 
			{			        
			    e.printStackTrace();
			    System.out.println("输出文件路径错误");
			    return false;
			}
		}
		return true;
	}
	
	
}
/*
 * 命令行解析类（包含命令的格式验证，以及命令的执行）
 */
class CommandAnalyze
{
	private String[] command;
	private String wholeCommand;
	private RegularExpression re;//用于命令解析中的正则格式匹配
	
	public CommandAnalyze(String[] args)
	{		
		command = new String[args.length];
		wholeCommand = "";
		
		
		for(int i = 0;i < args.length;i++)
		{
			command[i] = args[i];
			wholeCommand = wholeCommand + args[i] +" ";
		}
		
		
		re = new RegularExpression(wholeCommand);
	}
	
	/*用于解析传入的命令，例如list等，通过switch选择
	 * 
	 */	 
	public void commandAnalysis(SendCommandController sendCommandController) 
	{
		if(command.length == 0)
		{
			System.out.println("没有参数传入");
			return;
		}
		
		/*commandHead用来存取命令，即传入的第一个参数*/
		String commandHead = command[0];
		switch(commandHead)
		{
			case "list":				
				if(!re.isListDate()&&!re.isListType()&&!re.isListProvince())
					return;
				sendCommandController.list(command);
				break;
				
			default:
				System.out.println("找不到 "+commandHead+" 命令");
				return;
				
		}
	}
	
	
	
}

/*
 * 正则表达式工具类
 */
class RegularExpression
{
	private String str = "";
	
	public RegularExpression(String cmd) 
	{
		str = cmd;
	}
	
	/*正则匹配命令*/
	/*判断list -date选项命令格式是否正确*/	 
	public boolean isListDate()
	{
		String cmdCompile = "list\\s+-date\\s+[0-9]{4}-[0-9]{2}-[0-9]{2}\\s+-log\\s+\\S+\\s+-out\\s\\S+\\s*";
		Pattern p = Pattern.compile(cmdCompile);
		Matcher m = p.matcher(str);
		boolean isValid = m.matches();
		return isValid;
	}
	
	/*判断list -type选项格式命令是否正确*/	 
	public boolean isListType()
	{
		String cmdCompile = "list\\s+-type\\s*";
		Pattern p = Pattern.compile(cmdCompile);
		Matcher m = p.matcher(str);
		boolean isValid = m.matches();
		return isValid;
	}
	
	/*判断list -province选项命令格式是否正确*/	 
	public boolean isListProvince()
	{
		String cmdCompile = "list\\s+-province\\s*";
		Pattern p = Pattern.compile(cmdCompile);
		Matcher m = p.matcher(str);
		boolean isValid = m.matches();
		return isValid;
	}
}

public class InfectStatistic 
{	
	
	public static void main(String[] args) 
	{
		
		Command list;//创建命令
		list = new List();//实例化
		CommandAnalyze commandAnalyze = new CommandAnalyze(args);//创建命令解析对象
		
		SendCommandController sendCommandController = new SendCommandController(list);//创建并初始化控制器
		
		commandAnalyze.commandAnalysis(sendCommandController);//命令解析
		
	}

}
