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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
 * 抽象命令接口，用于命令模式的设计
 */
interface Command
{
	public void execute(ListCommand listCommand);
}

/*
 * 请求接收者类,类方法用于执行各种命令
 */
class ReceiveCommand
{
	private int[][] data;//存储各个省份疫情出具
	private String[] provinceName;//存取省份
	private String[] state;//存取type选项值
	private boolean[] ifChanged;//存取日志中的省份
	
	public ReceiveCommand() 
	{
		data = new int[4][32];
		ifChanged = new boolean[32];
		
		for(int i = 0;i < 4;i++) 
		{
			for(int j = 0;j < 32;j++)
			{
				data[i][j] = 0;
			}
		}
		
		ifChanged[0] = true;
		for(int i = 1;i < 32;i++)
		{
			ifChanged[i] = false;
		}
		
		provinceName = new String[] {
				"全国","安徽","北京","重庆","福建","甘肃","广东","广西",
				"贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林",
				"江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西",
				"陕西","上海","四川","天津","西藏","新疆","云南","浙江"
		};
		
		state = new String[] {"ip","sp","cure","dead"};
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
	
	public int[] getProvinceIds(ArrayList<String> provinceParameter)
	{
		int size = provinceParameter.size();
		int swap;
		int[] ids= new int[size];
		
		for(int i = 0;i < size;i++)
		{
			ids[i] = this.getProvinceId(provinceParameter.get(i));
		}
		
		Arrays.sort(ids);
		return ids;
	}
	
	public int[] getState(ArrayList<String> typeParameter)
	{
		int size = typeParameter.size();
		int[] type = new int[size];
		
		
		for(int i = 0;i < size;i++)
		{
			for(int j = 0;j < 4;j++)
			{				
				if(this.state[j].equals(typeParameter.get(i)))
				{
					type[i] = j;
					break;
				}					
			}
			
		}
		
		
		return type;
	}
	
	/*用于读取单个文件*/
	public void readFile(String path) 
	{
		int num;//存取数据修改
		RegularExpression regularExpression = new RegularExpression();
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
                    	ifChanged[number1] = true;
                		break;
                		
                	case 2:                		
                    	number = regularExpression.getSubUtilSimple(splited[splited.length-1]);
                    	num = Integer.parseInt(number);
                    	number1 = this.getProvinceId(splited[0]);
                    	data[1][number1] = data[1][number1] + num;   
                    	ifChanged[number1] = true;
                		break;
                		
                	case 3:
                		number = regularExpression.getSubUtilSimple(splited[splited.length-1]);
                    	num = Integer.parseInt(number);
                    	number1 = this.getProvinceId(splited[0]);
                    	number2 = this.getProvinceId(splited[3]);
                    	data[0][number1] = data[0][number1] - num;
                    	data[0][number2] = data[0][number2] + num;     
                    	ifChanged[number1] = true;
                    	ifChanged[number2] = true;
                		break;
                		
                	case 4:
                		number = regularExpression.getSubUtilSimple(splited[splited.length-1]);
                    	num = Integer.parseInt(number);
                    	number1 = this.getProvinceId(splited[0]);
                    	number2 = this.getProvinceId(splited[3]);
                    	data[1][number1] = data[1][number1] - num;
                    	data[1][number2] = data[1][number2] + num;  
                    	ifChanged[number1] = true;
                    	ifChanged[number2] = true;
                		break;
                		
                	case 5:
                		number = regularExpression.getSubUtilSimple(splited[splited.length-1]);
                    	num = Integer.parseInt(number);
                    	number1 = this.getProvinceId(splited[0]);
                    	data[0][number1] = data[0][number1] - num;
                    	data[3][number1] = data[3][number1] + num;  
                    	ifChanged[number1] = true;
                		break;
                		
                	case 6:
                		number = regularExpression.getSubUtilSimple(splited[splited.length-1]);
                    	num = Integer.parseInt(number);
                    	number1 = this.getProvinceId(splited[0]);
                    	data[0][number1] = data[0][number1] - num;
                    	data[2][number1] = data[2][number1] + num;
                    	ifChanged[number1] = true;
                		break;
                		
                	case 7:
                		number = regularExpression.getSubUtilSimple(splited[splited.length-1]);
                    	num = Integer.parseInt(number);
                    	number1 = this.getProvinceId(splited[0]);
                    	data[1][number1] = data[1][number1] - num;
                    	data[0][number1] = data[0][number1] + num;
                    	ifChanged[number1] = true;
                		break;
                		
                	case 8:
                		number = regularExpression.getSubUtilSimple(splited[splited.length-1]);
                    	num = Integer.parseInt(number);
                    	number1 = this.getProvinceId(splited[0]);
                    	data[1][number1] = data[1][number1] - num;   
                    	ifChanged[number1] = true;
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
	
	/*读取路径下所有文件*/
	public void readAllFile(String path)
	{
		ArrayList<File> files = FileOperate.getFiles(path);
		for(int i = 0;i < files.size();i++)
		{
			this.readFile(files.get(i).toString());
		}
	}
	
	/*根据日期读取路径下文件*/
	public void readSomeFile(ArrayList<String> parameter,String path)
	{
		ArrayList<File> files = FileOperate.getFiles(path);
		String logTime = "";
		for(int i = 0;i < files.size();i++)
		{
			logTime = RegularExpression.getFileName(files.get(i).getName());
			if(Time.timeCompare(parameter.get(0), logTime))
				this.readFile(files.get(i).getPath());
		}
	}
	
	/*统计全国数据*/
	public void chinaDate()
	{
		for(int i = 0;i < 4;i++)
		{
			for(int j = 1;j < 32;j++)
			{
				this.data[i][0] = this.data[i][0] + this.data[i][j];
			}
		}
	}
	
	public void outPutAll(ListCommand listCommand)
	{
		try 
		{
			FileWriter fw = new FileWriter(listCommand.getOutPath(),true);	
			/*输出所有结果*/
			for(int i = 0;i < 32;i++) {
				if(!ifChanged[i])
					continue;
				fw.write(provinceName[i]);
				for(int j = 0;j < 4;j++) {
					switch(j)
					{
					case 0:
						fw.write(" 感染患者");
						break;
					case 1:
						fw.write(" 疑似患者");
						break;
					case 2:
						fw.write(" 治愈");
						break;
					case 3:
						fw.write(" 死亡");
						break;
					default:
						break;
					}
					fw.write(data[j][i]+"人");
				}
				if(i < 31)
				{
					fw.write("\n");					
				}
			}
			fw.close();
		}	
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	public void outPutType(ListCommand listCommand)
	{
		if(listCommand.getTypeParameter().size() == 0)
		{
			this.outPutAll(listCommand);
			return;
		}
		
		int[] type = this.getState(listCommand.getTypeParameter());
		
		try 
		{
			FileWriter fw = new FileWriter(listCommand.getOutPath(),true);	
			for(int i = 0;i < 32;i++) {
				if(!ifChanged[i])
					continue;
				fw.write(provinceName[i]);
				for(int j = 0;j < type.length;j++) {
					switch(type[j])
					{
					case 0:
						fw.write(" 感染患者");						
						break;
					case 1:
						fw.write(" 疑似患者");		
						break;
					case 2:
						fw.write(" 治愈");		
						break;
					case 3:
						fw.write(" 死亡");		
						break;
					default:
						break;
					}
					fw.write(data[type[j]][i]+"人");		
					
				}
				if(i < 31)
				{
					fw.write("\n");					
				}
			}
			fw.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void outPutProvince(ListCommand listCommand) 
	{
		if(listCommand.getProvinceParameter().size() == 0)
		{
			System.out.println("省份选项参数不能为空");
			return;
		}
		int[] ids = this.getProvinceIds(listCommand.getProvinceParameter());
		
		try 
		{
			FileWriter fw = new FileWriter(listCommand.getOutPath(),true);	
			for(int i = 0;i < ids.length;i++) {
				fw.write(provinceName[ids[i]]);			
				for(int j = 0;j < 4;j++) {
					switch(j)
					{
					case 0:
						fw.write(" 感染患者");	
						break;
					case 1:
						fw.write(" 疑似患者");	
						break;
					case 2:
						fw.write(" 治愈");	
						break;
					case 3:
						fw.write(" 死亡");	
						break;
					default:
						break;
					}
					fw.write(data[j][ids[i]]+"人");					
					
				}
				if(i < ids.length - 1)
				{
					fw.write("\n");
				}
			}
			fw.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void outPutBoth(ListCommand listCommand)
	{
		if(listCommand.getProvinceParameter().size() == 0)
		{
			System.out.println("省份选项参数不能为空");
			return;
		}
		
		if(listCommand.getTypeParameter().size() == 0)
		{
			this.outPutProvince(listCommand);
			return;
		}
		int[] type = this.getState(listCommand.getTypeParameter());
		int[] ids = this.getProvinceIds(listCommand.getProvinceParameter());
		
		try
		{
			FileWriter fw = new FileWriter(listCommand.getOutPath(),true);	
			for(int i = 0;i < ids.length;i++) {
				fw.write(provinceName[ids[i]]);
				for(int j = 0;j < type.length;j++) {
					switch(type[j])
					{
					case 0:
						fw.write(" 感染患者");
						break;
					case 1:
						fw.write(" 疑似患者");
						break;
					case 2:
						fw.write(" 治愈");
						break;
					case 3:
						fw.write(" 死亡");
						break;
					default:
						break;
					}
					fw.write(data[type[j]][ids[i]]+"人");
					
				}
				if(i < ids.length - 1)
				{
					fw.write("\n");				
				}
			}
			fw.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}
	/*输出函数*/
	public void outPut(ListCommand listCommand)
	{
		this.chinaDate();
		if(listCommand.getOption()[1]&&!listCommand.getOption()[2])
		{
			this.outPutType(listCommand);		
		}
		else if(!listCommand.getOption()[1]&&listCommand.getOption()[2])
		{
			this.outPutProvince(listCommand);
		}
		else if(listCommand.getOption()[1]&&listCommand.getOption()[2])
		{
			this.outPutBoth(listCommand);
		}
		else//只有date命令输出
		{
			this.outPutAll(listCommand);
		}
	}
	
	public void list(ListCommand listCommand)
	{
		if(listCommand.getOption()[0] && listCommand.getDateParameter().size() != 0) 
		{
			
			this.readSomeFile(listCommand.getDateParameter(),listCommand.getReadPath());
		}
		else
		{
			this.readAllFile(listCommand.getReadPath());
		}
		
		this.outPut(listCommand);
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
	
	public void list(ListCommand listCommand)
	{ 
		list.execute(listCommand);
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
	
	
	 public void execute(ListCommand listCommand)
	{		
		rc.list(listCommand);
	}
}

/*
 * 命令行解析类（包含命令的格式验证，以及命令的执行）
 */
class CommandAnalyze
{
	public String[] cmd;
	public String wholeCmd;
	
	public CommandAnalyze(String[] args)
	{
		wholeCmd = "";
		cmd = new String[args.length];
		
		for(int i = 0;i < args.length;i++)
		{
			cmd[i] = args[i];
			wholeCmd = wholeCmd + args[i];
			if(i != args.length - 1)
				wholeCmd = wholeCmd + " ";
		}
	}
	
	public void commandAnalysis(SendCommandController sendCommandController)
	{
		switch(cmd[0])
		{
		case "list":
		{
			if(!RegularExpression.isListRight(wholeCmd))
			{
				System.out.println("list命令格式错误");
				return;
			}
			ListCommand listCommand = new ListCommand(cmd);
			if(listCommand.getOption()[0])
				listCommand.dateParameter();
			if(listCommand.getOption()[1])
				listCommand.typeParameter();
			if(listCommand.getOption()[2])
				listCommand.provinceParameter();
			listCommand.logParameter();
			listCommand.outParameter();
			sendCommandController.list(listCommand);
			break;
		}
		
		default:
			System.out.println("没找到 "+cmd[0]+" 命令");
		}
	}
}


/*
 * list命令实体类，用于命令解析后存取命令实体
 */
class ListCommand
{
	private boolean[] option;//存取三个选项<date,type,province>存在则置1
	private String[] options;
	private ArrayList<String> dateParameter;
	private ArrayList<String> typeParameter;
	private ArrayList<String> provinceParameter;
	private String readPath;
	private String outPath;
	private String[] cmd;
	
	public ListCommand(String[] command) 
	{	
		
		cmd = new String[command.length];
		option = new boolean[] {false,false,false};
		options = new String[] {"-date","-type","-province"};
		dateParameter = new ArrayList<String>();
		typeParameter = new ArrayList<String>();
		provinceParameter = new ArrayList<String>();
		
		for(int i = 0;i < command.length;i++)
		{
			cmd[i] = command[i];
			if(command[i].equals("-date"))
			{
				option[0] = true;
			}
			if(command[i].equals("-type"))
			{
				option[1] = true;
			}
			if(command[i].equals("-province"))
			{
				option[2] = true;			
			}
		}		
		
	}
	
	
	
	public boolean[] getOption() {
		return option;
	}
	
	public ArrayList<String> getDateParameter() {
		return dateParameter;
	}
	
	public ArrayList<String> getTypeParameter() {
		return typeParameter;
	}
	
	public ArrayList<String> getProvinceParameter() {
		return provinceParameter;
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
		if(!FileOperate.pathRight(readPath)||!FileOperate.outExist(outPath))
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
		int i = 0;
		String start = "-date";
		String compile = "-\\w+";
		
		/*读取date参数，后可在此进行验证参数格式*/
		while(!start.equals(cmd[i]) && i < cmd.length)
		{					
			i++;
		}
		
		/*如果选项是最后一个，则无参数，直接返回*/
		if(i == cmd.length - 1)
			return;
		
		i++;
		while(i < cmd.length && !RegularExpression.isMatch(compile, cmd[i]))
		{
			dateParameter.add(cmd[i]);
			i++;
		}		
	}
	
	public void typeParameter()
	{
		int i = 0;
		String start = "-type";
		String compile = "-\\w+";
		
		/*读取type参数，后可在此进行验证参数格式*/
		while(!start.equals(cmd[i]) && i < cmd.length)
		{					
			i++;
		}
		
		/*如果选项是最后一个，则无参数，直接返回*/
		if(i == cmd.length - 1)
			return;
		
		i++;
		while(i < cmd.length && !RegularExpression.isMatch(compile, cmd[i]))
		{
			typeParameter.add(cmd[i]);
			i++;
		}		
	}
	
	public void provinceParameter()
	{
		int i = 0;
		String start = "-province";
		String compile = "-\\w+";
		
		
		/*读取province参数，后可在此进行验证参数格式*/
		while(!start.equals(cmd[i]) && i < cmd.length)
		{					
			i++;
		}
		
		/*如果选项是最后一个，则无参数，直接返回*/
		if(i == cmd.length - 1)
			return;
		
		i++;
		while(i < cmd.length && !RegularExpression.isMatch(compile, cmd[i]))
		{
			provinceParameter.add(cmd[i]);
			i++;
		}		
	}
	
	
}

/*
 * 文件操作类
 */
class FileOperate
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
	
	/*获取指定路径下的所有文件（不包括文件夹）*/
	public static ArrayList<File> getFiles(String path) {
	 	ArrayList<File> files = new ArrayList<File>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                files.add(tempList[i]);
                //文件名，不包含路径
                //String fileName = tempList[i].getName();
            }
            if (tempList[i].isDirectory()) {
                continue;
            }
        }
        return files;
    }
	
	
}

/*
 * 日期类
 */
class Time
{	
	/*日期比较函数*/
	public static boolean timeCompare(String commandTime,String logTime) 
	{			
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
            Date dt1 = df.parse(commandTime);
            Date dt2 = df.parse(logTime);
            if (dt1.getTime() > dt2.getTime()) {	                
                return true;
            } else if (dt1.getTime() < dt2.getTime()) {	                
                return false;
            } else {
                return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }		
		return false;
	}
}

/*
 * 正则表达式工具类
 */
class RegularExpression
{
	private String type1 = "\\W+ 新增 感染患者 [0-9]+人\\s*";
	private String type2 = "\\W+ 新增 疑似患者 [0-9]+人\\s*";
	private String type3 = "\\W+ 感染患者 流入 \\W+ [0-9]+人\\s*";
	private String type4 = "\\W+ 疑似患者 流入 \\W+ [0-9]+人\\s*";
	private String type5 = "\\W+ 死亡 [0-9]+人\\s*";
	private String type6 = "\\W+ 治愈 [0-9]+人\\s*";
	private String type7 = "\\W+ 疑似患者 确诊感染 [0-9]+人\\s*";
	private String type8 = "\\W+ 排除 疑似患者 [0-9]+人\\s*";
	
	/*正则匹配验证list命令格式是否正确*/
	public static boolean isListRight(String str) 
	{
		String cmdCompile = "list(\\s+-\\w+\\s+\\S*)*\\s+-log\\s+\\S+(\\s+-\\w+\\s+\\S*)*\\s+-out\\s+\\S+(\\s+-\\w+\\s+\\S*)*\\s*";
		Pattern p = Pattern.compile(cmdCompile);
		Matcher m = p.matcher(str);
		boolean isValid = m.matches();
		return isValid;
	}
	
	/*验证字符串是否匹配*/
	public static boolean isMatch(String compile,String str)
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
	
	/*正则匹配截取人数变化数值*/
	public String getSubUtilSimple(String soap)
	{  
		String rgex = "(.*?)人";
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式  
        Matcher m = pattern.matcher(soap);  
        while(m.find())
        {  
            return m.group(1);  
        }  
        return "";  
    }  
	
	/*正则匹配获取文件名的日期*/ 	
	public static String getFileName(String soap)	 
	{
		String rgex = "(.*?).log.txt";
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式  
        Matcher m = pattern.matcher(soap);  
        while(m.find())
        {  
            return m.group(1);  
        }  
        return "";  
	}
}

public class InfectStatistic {

	public static void main(String[] args) {
		
		Command list;//创建命令
		list = new List();//实例化
		
		CommandAnalyze commandAnalyze = new CommandAnalyze(args);//创建命令解析对象
		
		SendCommandController sendCommandController = new SendCommandController(list);//创建并初始化控制器
		
		commandAnalyze.commandAnalysis(sendCommandController);//命令解析
		
	}

}
