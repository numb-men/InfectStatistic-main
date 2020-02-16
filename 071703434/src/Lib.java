package src;

import java.util.*;

public class Lib {

	
	
}

/**
 * 命令行
 * 
 * @author ZhangYuhui
 * @version 1.0
 */
class CommandLine{
	
	private String commandName;
	private String[] parameters;
	
	public CommandLine(String[] args) {
		commandName=args[0];
		for(int i=1;i<args.length;i++) {
			parameters[i-1]=args[i];
		}
	}

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	public String[] getParameters() {
		return parameters;
	}

	public void setParameters(String[] parameters) {
		this.parameters = parameters;
	}

	public boolean isList() {
		return this.commandName=="list";
	}
}


/**
 * 命令行参数列表类接口
 * 
 * @author ZhangYuhui
 * @version 1.0
 */
interface Parameters{
	
	class Parameter{
		String name;
		Object value;
	}

	/**
	 * 命令的参数的值
	 * 
	 * @author ZhangYuhui
	 * @version 1.0
	 */
	class ListParameterValue{
		boolean valueRequired;
		boolean multiValue;
		Object value;
		
		public ListParameterValue(boolean valueRequired, boolean multiValue, Object value) {
			super();
			this.valueRequired = valueRequired;
			this.multiValue = multiValue;
			this.value = value;
		}
	}
	
	/**
	 * 参数格式化
	 * 
	 * @param parameters
	 */
	public void formatParameters(String[] parameters);
	
	/**
	 * 判断参数的正确性：是否包含必要的参数，参数值
	 * 
	 * @throws Exception
	 */
	public void judgeParameters();
	
}


/**
 * List命令行参数列表类
 * 
 * @author ZhangYuhui
 * @version 1.0
 * -log 指定日志目录的位置，该项必会附带。
 * -out 指定输出文件路径和文件名，该项必会附带。
 * -date 指定日期，不设置则默认为所提供文件标识中最新的一天。
 * -type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，
 * 		   使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，
 * 		   不指定该项默认会列出所有情况。
 * -province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江，
 */

class ListParameters implements Parameters{
	
	private Map<String,ListParameterValue> listParameterMap=new HashMap<>();

	enum TypeOption {
		ip,sp,cure,dead;
	}

	public ListParameters(){
		listParameterMap.put("-log", new ListParameterValue(true,false,null));
		listParameterMap.put("-out", new ListParameterValue(true,false,null));
		listParameterMap.put("-type", new ListParameterValue(true,true,null));
		listParameterMap.put("-province", new ListParameterValue(true,true,null));
		listParameterMap.put("-date", new ListParameterValue(true,false,null));
	}
	

	/**
	 * 格式化List命令的参数，将字符串数组的参数列表转化为map中的值
	 * 
	 * @param parameters 
	 */
	@Override
	public void formatParameters(String[] parameters) {
		
		for(int i=0;i<parameters.length;) {
			String parameter=parameters[i];
			if(listParameterMap.keySet().contains(parameter)) {//该项为参数
				
				
			}else {//该项不为参数
				
			}
		}
		
		
	}
	

	/**
	 * 判断格式化后的list命令参数列表是否符合需求
	 * 
	 */
	@Override
	public void judgeParameters() {
		// TODO Auto-generated method stub
		
	}
}


/**
 * 命令行解析器
 * 
 * @author ZhangYuhui
 * @version 1.0
 */
class CommandLineParser{
	
	/**
	 * 解析命令行，将命令中的参数和参数值从string[]中解析出来
	 * 
	 * @param commandLine 命令行
	 */
	public void parseCommandLine(CommandLine commandLine) {
		
		
		String[] parameters=commandLine.getParameters();
		
		for(int i=0;i<parameters.length;i++) {
			String item=parameters[i];
			
			if(item=="list") {
				commandLine.setCommandName(item);
				continue;
			}else {
				//TODO:解析其他命令
			}
			
			//开始解析参数
			
			
			
			
			
		}
	}
}

/**
 * 某日某省的感染状况
 * 
 * @author ZhangYuhui
 * @version 1.0
 */
class DailyInfectItem{
	
	int ip;
	int sp;
	int cure;
	int dead;
	
	public DailyInfectItem(int ip, int sp, int cure, int dead) {
		super();
		this.ip = ip;
		this.sp = sp;
		this.cure = cure;
		this.dead = dead;
	}

	public int getIp() {
		return ip;
	}

	public void setIp(int ip) {
		this.ip = ip;
	}

	public int getSp() {
		return sp;
	}

	public void setSp(int sp) {
		this.sp = sp;
	}

	public int getCure() {
		return cure;
	}

	public void setCure(int cure) {
		this.cure = cure;
	}

	public int getDead() {
		return dead;
	}

	public void setDead(int dead) {
		this.dead = dead;
	}
	
}



/**
 * 日志文件读取器
 * 
 * @author ZhangYuhui
 * @version 1.0
 */
class LogFileReader{
	
	String logAddress;
	
	public void readFile() {
		
	}
	
	
	
}

/**
 * 每个省每日感染情况
 * 
 * @author ZhangYuhui
 * @version 1.0
 */
class InfectItem{
	
}

/**
 * 正则表达式
 * 
 * @author ZhangYuhui
 * @version 1.0
 */
class RegExp{
	
	//用于匹配类型的字符串
    String IP_ADD_PATTERN = "\\W+ 新增 感染患者 \\d+人";
    String SP_ADD_PATTERN = "\\W+ 新增 疑似患者 \\d+人";
    String IP_FLOWIN_PATTERN = "\\W+ 感染患者 流入 \\W+ \\d+人";
    String SP_FLOWIN_PATTERN = "\\W+ 疑似患者 流入 \\W+ \\d+人";
    String DEAD_PATTERN = "\\W+ 死亡 \\d+人";
    String CURE_PATTERN = "\\W+ 治愈 \\d+人";
    String SP_CONFIRM_PATTERN = "\\W+ 疑似患者 确诊感染 \\d+人";
    String SP_EXCLUDE_PATTERN = "\\W+ 排除 疑似患者 \\d+人";
    
    
   
    
	
}


