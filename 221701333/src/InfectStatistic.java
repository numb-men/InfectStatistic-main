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
//System.out.println("在这里停止了");
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
	
	public void listDate(ListCommand listCommand)
	{
		System.out.println("接受到listDate命令，正在执行中......");
	}
	
	public void listType(ListCommand listCommand)
	{
		System.out.println("接受到listType命令，正在执行中......");
	}
	
	public void listProvince(ListCommand listCommand)
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
		
		
		ListCommand listCommand = new ListCommand(command);
		
		switch(listCommand.getOption())
		{
			case "-date":
				if(!listCommand.isPathRight()) 
				{
					
					return;
				}		
				
				rc.listDate(listCommand);
				break;
				
			case "-type":
				
				rc.listType(listCommand);
				break;
				
			case "-province":
				if(!listCommand.isPathRight()) 
				{
					return;
				}
				rc.listProvince(listCommand);
				break;
				
			default:
				System.out.println("没找到list的 " + listCommand.getOption() + "选项");
				break;
		}
		
	}
}

/*
 * 路径类
 */
class Path
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
 * list命令实体类，用于命令解析后存取命令实体
 */
class ListCommand{
	private String option;
	private ArrayList<String> parameter;
	private String readPath;
	private String outPath;
	private String[] cmd;
	
	public ListCommand(String[] command) 
	{
		cmd = new String[command.length];
		for(int i = 0;i < command.length;i++)
		{
			cmd[i] = command[i];		
		}
		
		option = command[1];
		switch(command[1])
		{
		case "-date":
		{			
			this.dateParameter();			
			this.logParameter();
			this.outParameter();
			break;
		}
		
		case "-type":
		{
			this.typeParameter();			
			//this.logParameter();
			//this.outParameter();
			break;
		}
		
		case "-province":
		{
			this.provinceParameter();			
			this.logParameter();
			this.outParameter();
			break;
		}
		
		default:
			break;
		}
	}
	
	
	
	public String getOption() {
		return option;
	}
	
	public ArrayList<String> getParameter() {
		return parameter;
	}
	
	public String getReadPath() {
		return readPath;
	}
	
	public String getOutPath() {
		return outPath;
	}
	
	/*验证命令中路径是否正确*/
	public boolean isPathRight() 
	{
		if(!Path.pathRight(readPath)||!Path.outExist(outPath))
		{
			return false;			
		}
		return true;
	}
	
	public void logParameter()
	{
		String start = "-log";
		int i = 0;
		while(!start.equals(cmd[i]))
		{
			i++;
		}
		readPath = cmd[i+1];
		
	}
	
	public void outParameter()
	{
		String start = "-out";
		int i = 0;
		while(!start.equals(cmd[i]))
		{
			i++;
		}
		outPath = cmd[i+1];
	}
	
	public void dateParameter()
	{
		int i = 2;
		parameter = new ArrayList<String>();
		String end = "-log";
		
		/*读取date参数，后可在此进行验证参数格式*/
		while(!end.equals(cmd[i]) && i < cmd.length)
		{					
			parameter.add(cmd[i]);
			i++;
		}
	}
	
	public void typeParameter()
	{
		int i = 2;
		parameter = new ArrayList<String>();
		String end = "-log";
		
		/*读取type参数，后可在此进行验证参数格式*/
		while(!end.equals(cmd[i]) && i < cmd.length)
		{					
			parameter.add(cmd[i]);
			i++;
		}
		
	}
	
	public void provinceParameter()
	{
		int i = 2;
		parameter = new ArrayList<String>();
		String end = "-log";
		
		/*读取province参数，后可在此进行验证参数格式*/
		while(!end.equals(cmd[i]) && i < cmd.length)
		{					
			parameter.add(cmd[i]);
			i++;
		}
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
			System.out.println("没有命令传入");
			return;
		}
		
		/*commandHead用来存取命令，即传入的第一个参数*/
		String commandHead = command[0];
		switch(commandHead)
		{
			case "list":				
				if(!re.isListDate()&&!re.isListType()&&!re.isListProvince())
				{
					System.out.println("此list命令格式不正确");
					return;
				}
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
		String cmdCompile = "list\\s+-date\\s+[0-9]{4}-[0-9]{2}-[0-9]{2}\\s+-log\\s+\\S+\\s+-out\\s+\\S+\\s*";
		Pattern p = Pattern.compile(cmdCompile);
		Matcher m = p.matcher(str);
		boolean isValid = m.matches();
		return isValid;
	}
	
	/*判断list -type选项格式命令是否正确*/	 
	public boolean isListType()
	{
		String cmdCompile = "list\\s+-type\\s+.*-log\\s+\\S+\\s+-out\\s+\\S+\\s*";
		Pattern p = Pattern.compile(cmdCompile);
		Matcher m = p.matcher(str);
		boolean isValid = m.matches();
		return isValid;
	}
	
	/*判断list -province选项命令格式是否正确*/	 
	public boolean isListProvince()
	{
		String cmdCompile = "list\\s+-province\\s+.*-log\\s+\\S+\\s+-out\\s+\\S+\\s*";
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
