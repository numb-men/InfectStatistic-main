/**
 * InfectStatistic
 * TODO
 *
 * @author 爱学习的水先生
 * @version 1.0
 * @since 2020/2/11
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
	private int[][] data;
	private String[] provinceName;
	
	public ReceiveCommand() 
	{
		data = new int[4][32];
		for(int i = 0;i < 4;i++) 
		{
			for(int j = 0;j < 32;j++)
			{
				data[i][j] = 0;
			}
		}
		provinceName = new String[] {
				"中国","安徽","北京","重庆","福建","甘肃","广东","广西",
				"贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林",
				"江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西",
				"陕西","上海","四川","天津","西藏","新疆","云南","浙江"
		};
	}
	
	public int getProvinceId(String province)
	{
		for(int i = 0;i < 32;i++)
		{
			if(province.equals(this.provinceName[i]))
			{
				return i;
			}
		}
		return -1;
	}
	
	/*用于读取文件*/
	public void readFile(String path) 
	{
		int num;
		RegularExpression regularExpression = new RegularExpression("");
		File file=new File(path);//创建文件对象
        String encoding="UTF-8";//设置读取文件的编码格式
        if(file.isFile()&&file.exists()){//判断文件是否存在
            try {
                FileInputStream fisr=new FileInputStream(file);            
                InputStreamReader isr=new InputStreamReader(fisr,encoding);//封装文件输入流，并设置编码方式                
                BufferedReader br=new BufferedReader(isr);               
                String txt=null;
                while((txt=br.readLine())!=null){//按行读取文件，每次读取一行               	
                	String[] splited = txt.split("\\s+");
                	String number;
                	int number1;
                	int number2;
                	switch(regularExpression.fileMatch(txt))
                	{
                	case 1:                		
                    	number = regularExpression.getSubUtilSimple(splited[splited.length-1]);
                    	num = Integer.parseInt(number);
                    	number1 = this.getProvinceId(splited[0]);
                    	data[0][number1] = data[0][number1] + num;
                    	System.out.println("case 1's num:"+num);
                		break;
                		
                	case 2:                		
                    	number = regularExpression.getSubUtilSimple(splited[splited.length-1]);
                    	num = Integer.parseInt(number);
                    	number1 = this.getProvinceId(splited[0]);
                    	data[1][number1] = data[1][number1] + num;
                    	System.out.println("case 2's num:"+num);
                		break;
                		
                	case 3:
                		number = regularExpression.getSubUtilSimple(splited[splited.length-1]);
                    	num = Integer.parseInt(number);
                    	number1 = this.getProvinceId(splited[0]);
                    	number2 = this.getProvinceId(splited[3]);
                    	data[0][number1] = data[0][number1] - num;
                    	data[0][number2] = data[0][number2] + num;
                    	System.out.println("case 3's num:"+num);
                		break;
                		
                	case 4:
                		number = regularExpression.getSubUtilSimple(splited[splited.length-1]);
                    	num = Integer.parseInt(number);
                    	number1 = this.getProvinceId(splited[0]);
                    	number2 = this.getProvinceId(splited[3]);
                    	data[1][number1] = data[1][number1] - num;
                    	data[1][number2] = data[1][number2] + num;
                    	System.out.println("case 4's num:"+num);
                		break;
                		
                	case 5:
                		number = regularExpression.getSubUtilSimple(splited[splited.length-1]);
                    	num = Integer.parseInt(number);
                    	number1 = this.getProvinceId(splited[0]);
                    	data[0][number1] = data[0][number1] - num;
                    	data[3][number1] = data[3][number1] + num;
                    	System.out.println("case 5's num:"+num);
                		break;
                		
                	case 6:
                		number = regularExpression.getSubUtilSimple(splited[splited.length-1]);
                    	num = Integer.parseInt(number);
                    	number1 = this.getProvinceId(splited[0]);
                    	data[0][number1] = data[0][number1] - num;
                    	data[2][number1] = data[2][number1] + num;
                    	System.out.println("case 6's num:"+num);
                		break;
                		
                	case 7:
                		number = regularExpression.getSubUtilSimple(splited[splited.length-1]);
                    	num = Integer.parseInt(number);
                    	number1 = this.getProvinceId(splited[0]);
                    	data[1][number1] = data[1][number1] - num;
                    	data[0][number1] = data[0][number1] + num;
                    	System.out.println("case 7's num:"+num);
                		break;
                		
                	case 8:
                		number = regularExpression.getSubUtilSimple(splited[splited.length-1]);
                    	num = Integer.parseInt(number);
                    	number1 = this.getProvinceId(splited[0]);
                    	data[1][number1] = data[1][number1] - num;    
                    	System.out.println("case 8's num:"+num);
                		break;
                		
                	default:
                		break;
                		
                	}
                }
                fisr.close();
                isr.close();
                br.close();
            } catch (FileNotFoundException e) {                
                e.printStackTrace();
            }catch (UnsupportedEncodingException e) {              
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
	
	public void listDate(ListCommand listCommand)
	{
		System.out.println("接受到listDate命令，正在执行中......");
		this.readFile("E:\\Git\\GitLocal\\InfectStatistic-main\\example\\log\\2020-01-22.log.txt");
		
		for(int i = 1;i < 32;i++) {
			System.out.print(provinceName[i]+" :");
			for(int j = 0;j < 4;j++) {
				System.out.print(data[j][i]+" ");
			}
			System.out.println("\n");
		}
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
				if(!listCommand.isPathRight()) 
				{					
					return;
				}	
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
 * 日期类
 */
class Time
{
	private String time;
	
	public Time(String time) 
	{
		this.time = time;
	}
	
	/*日期比较函数*/
	public static boolean timeCompare(String commandTime,String logTime) 
	{
		return true;
	}
}
/*
 * list命令实体类，用于命令解析后存取命令实体
 */
class ListCommand
{
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
	private String type1 = "\\W+ 新增 感染患者 [0-9]+人\\s*";
	private String type2 = "\\W+ 新增 疑似患者 [0-9]+人\\s*";
	private String type3 = "\\W+ 感染患者 流入 \\W+ [0-9]+人\\s*";
	private String type4 = "\\W+ 疑似患者 流入 \\W+ [0-9]+人\\s*";
	private String type5 = "\\W+ 死亡 [0-9]+人\\s*";
	private String type6 = "\\W+ 治愈 [0-9]+人\\s*";
	private String type7 = "\\W+ 疑似患者 确诊感染 [0-9]+人\\s*";
	private String type8 = "\\W+ 排除 疑似患者 [0-9]+人\\s*";
		
	
	public RegularExpression(String cmd) 
	{
		str = cmd;
	}
	
	/*验证字符串是否匹配*/
	public boolean isMatch(String compile,String str)
	{		
		Pattern p = Pattern.compile(compile);
		Matcher m = p.matcher(str);
		boolean isValid = m.matches();
		return isValid;
	}
	
	/*文件行匹配*/
	public int fileMatch(String str)
	{
		if(this.isMatch(type1, str))
			return 1;
		else if(this.isMatch(type2, str))
			return 2;
		else if(this.isMatch(type3, str))
			return 3;
		else if(this.isMatch(type4, str))
			return 4;
		else if(this.isMatch(type5, str))
			return 5;
		else if(this.isMatch(type6, str))
			return 6;
		else if(this.isMatch(type7, str))
			return 7;
		else if(this.isMatch(type8, str))
			return 8;
		else 
			return -1;
	}
	
	/*用于截取人数变化数值*/
	public String getSubUtilSimple(String soap){  
		String rgex = "(.*?)人";
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式  
        Matcher m = pattern.matcher(soap);  
        while(m.find()){  
            return m.group(1);  
        }  
        return "";  
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