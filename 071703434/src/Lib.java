package src;

import com.sun.jdi.connect.Connector.Argument;

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
 * 命令行参数
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

class Parameters{
	private boolean log;
	private boolean out;
	private boolean date;
	private boolean type;
	private boolean province;
	
	private String logValue;
	private String outValue;
	private String dateValue;
	private TypeOption[] typeValue;
	private String[] provinceValue;
	
	enum TypeOption {
		ip,sp,cure,dead;
	}
	
	/**
	 * 判断参数的正确性
	 * 
	 * @throws Exception
	 */
	public void judgeParameters() throws Exception{
		if(!(log&&out)) {
			throw new Exception("必要参数缺少，请确认后重新输入 ");
		}
		
	}
	
	public boolean isLog() {
		return log;
	}
	
	public void setLog(boolean log) {
		this.log = log;
	}
	
	public boolean isOut() {
		return out;
	}
	
	public void setOut(boolean out) {
		this.out = out;
	}
	
	public boolean isDate() {
		return date;
	}
	
	public void setDate(boolean date) {
		this.date = date;
	}
	
	public boolean isType() {
		return type;
	}
	
	public void setType(boolean type) {
		this.type = type;
	}
	
	public boolean isProvince() {
		return province;
	}
	
	public void setProvince(boolean province) {
		this.province = province;
	}
	
	public String getLogValue() {
		return logValue;
	}
	public void setLogValue(String logValue) {
		this.logValue = logValue;
	}
	
	public String getOutValue() {
		return outValue;
	}
	
	public void setOutValue(String outValue) {
		this.outValue = outValue;
	}
	
	public String getDateValue() {
		return dateValue;
	}
	
	public void setDateValue(String dateValue) {
		this.dateValue = dateValue;
	}
	
	public TypeOption[] getTypeValue() {
		return typeValue;
	}
	
	public void setTypeValue(TypeOption[] typeValue) {
		this.typeValue = typeValue;
	}
	
	public String[] getProvinceValue() {
		return provinceValue;
	}
	
	public void setProvinceValue(String[] provinceValue) {
		this.provinceValue = provinceValue;
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
class DailyInfecResult{
	
	int ip;
	int sp;
	int cure;
	int dead;
	
	public DailyInfecResult(int ip, int sp, int cure, int dead) {
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


