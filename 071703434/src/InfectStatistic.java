package src;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

import javax.swing.text.DateFormatter;


/**
 * 疫情统计
 * 
 * @author ZhangYuhui
 * @version 1.0
 */
class InfectStatistic {

    public static void main(String[] args) {

    	String[] string= {
    			"-province","浙江","福建","-type","sp","dead","-log", "D:/log/","-date","2020-01-22", "-out", "D:/output.txt",
        	};
    	
    	//ListParameters lp=new ListParameters(string);
    	//lp.formatParameters();
    	
    	//LogFileReader lfr=new LogFileReader("C:\\Users\\ThinkPad\\Desktop\\软件工程实践二\\log");
    	File f=new File("C:\\Users\\ThinkPad\\Desktop\\软件工程实践二\\log\\2020-01-22.log.txt");
    	
    	LogFileReader.formatFileContent(f);
    }
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
		parameters=new String[args.length];
		commandName=args[0];
		for(int i=1;i<args.length;i++) {
			parameters[i-1]=args[i];
		}
		new CommandLineParser(commandName,parameters);
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
	 * 参数格式化
	 * 
	 * @param parameters
	 */
	public void formatParameters();
	
	/**
	 * 判断参数的正确性：是否包含必要的参数，参数值
	 * 
	 * @throws Exception
	 */
	public void judgeParameters();
	
}

/**
 * 命令的参数的值
 * 
 * @author ZhangYuhui
 * @version 1.0
 */
class ParameterValue{
//	boolean valueRequired;
	boolean multiValue;
	Object value;
	
	public ParameterValue( boolean multiValue, Object value) {
		super();
//		this.valueRequired = valueRequired;
		this.multiValue = multiValue;
		this.value = value;
	}

	@Override
	public String toString() {
		return "	[multiValue=" + multiValue + ", value=" + value+ "]\n";
	}
	
	
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
	
	private Map<String,ParameterValue> listParameterMap=new HashMap<>();
	private String[] parameters;
	
	enum TypeOption {
		ip,sp,cure,dead;
	}

	public ListParameters(String[] parameters){
		this.parameters=parameters;
		listParameterMap.put("-log", new ParameterValue(false,null));
		listParameterMap.put("-out", new ParameterValue(false,null));
		listParameterMap.put("-type", new ParameterValue(true,null));
		listParameterMap.put("-province", new ParameterValue(true,null));
		listParameterMap.put("-date", new ParameterValue(false,null));
	}
	

	/**
	 * 格式化List命令的参数，将字符串数组的参数列表转化为map中的值
	 * 
	 * @param parameters 
	 */
	@Override
	public void formatParameters() {
		
		for(int i=0;i<parameters.length;) {
			String parameter=parameters[i];
			System.out.println(parameter);
			if(listParameterMap.containsKey(parameter)) {//该项为参数
				if(listParameterMap.get(parameter).multiValue) {//参数可多值
					List<String> value=new LinkedList<String>();
					while(++i<parameters.length) {
						if(!listParameterMap.containsKey(parameters[i])) {
							value.add(parameters[i]);
						}else {
							break;
						}
					}
					listParameterMap.get(parameter).value=value;
					
				}else {//该项单值
					if(++i<parameters.length&&(!listParameterMap.containsKey(parameters[i]))){
						listParameterMap.get(parameter).value=parameters[i];
						i++;
					}else {
						//	TODO:参数项未提供参数值，抛出异常
					}
				}
			}else {//该项不为参数
				continue;
			}
		}
		System.out.println(this.toString());
		
	}
	

	/**
	 * 判断格式化后的命令参数列表是否符合需求
	 * 
	 * 
	 */
	@Override
	public void judgeParameters() {
		
		
	}
	
	
	public Map<String,ParameterValue> getParametersMap() {
		return listParameterMap;
	}


	@Override
	public String toString() {
		return "ListParameters [listParameterMap=" + listParameterMap + ", "
				+ "parameters=" + Arrays.toString(parameters)+ "]\n";
	}

}


/**
 * 命令行解析器
 * 
 * @author ZhangYuhui
 * @version 1.0
 */
class CommandLineParser{
	
	private Map<String,ParameterValue> ParameterMap=new HashMap<>();
	
	
	public CommandLineParser(String commandName,String[] parameters) {
		if(commandName=="list") {
			parseListCommand(parameters);
		}
	}
	
	public void parseListCommand(String[] parameters) {
		ListParameters listParameters=new ListParameters(parameters);
		listParameters.formatParameters();
//		listParameters.judgeParameters();
		ParameterMap=listParameters.getParametersMap();
	}
	
	
	public void excuteListCommand() {
		
		String logAddress=(String) ParameterMap.get("-log").value;
		LogFileReader logFileReader=new LogFileReader(logAddress);
		
		
		
		
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
 * 	日志文件读取器
 * 
 * @author ZhangYuhui
 * @version 1.0
 */
class LogFileReader{
	
	String logDirectory;
	Map<LocalDate,File> dateLogFilesMap=new HashMap<LocalDate, File>();
	LocalDate endDate;
	LocalDate startDate;
	LocalDate parameterDate;
	DateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
	
	
	public LogFileReader(String logDirectory) {
		
		this.startDate=this.endDate=null;
		
	}
	
	/**
	 * 	获取日志文件数组
	 * 
	 * @param logDirectory 放置日志的目录
	 * @return
	 */
	public File[] logFiles(String logDirectory) {
		File logDir=new File(logDirectory);
		if(logDir.exists()&&logDir.isDirectory()) {
			File[] logList=logDir.listFiles();
			
			return logList;
//			Date date = null;
//			LocalDate lDate=null;
//			for(int i=0;i<logList.length;i++) {
//				
//				if(logList[i].isFile()) {
//					String logName=logList[i].getName();
//					logName=logName.substring(0, logName.indexOf('.'));
//					
//					try {
//						date=formatter.parse(logName);
//					}catch (Exception e) {
//						// TODO: handle exception
//						e.printStackTrace();
//					}
//					
//					lDate=date2LocalDate(date);
//					//ManageDateArrange(lDate);
//					dateLogFilesMap.put(lDate, logList[i]);
//				}
//			}

		}else {
			//	TODO:处理日志所在文件目录异常
			return null;
		}
	}
	
	/**
	 * 	维护需要读取的日志文件，获得最大日期和最小日期
	 * 
	 * @param lDate
	 */
	public void ManageDateArrange(File[] logs) {
	
		Date date = null;
		LocalDate lDate=null;
		for(int i=0;i<logs.length;i++) {
			String logName=logs[i].getName();
			logName=logName.substring(0, logName.indexOf('.'));
			try {
				date=formatter.parse(logName);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			lDate=date2LocalDate(date);
			if(i==0) {
				endDate=lDate;
				startDate=lDate;
			}else {
				if(lDate.isAfter(endDate)) {
					endDate=lDate;
				}
				if(lDate.isBefore(startDate)) {
					startDate=lDate;
				}
			}
		}
		
	}
	
	
	public void generateDailyMap() {
		
	}
	/**
	 * Date转换成LocalDate
	 * 
	 * @param date
	 * @return
	 */
    public static LocalDate date2LocalDate(Date date) {
        if(null == date) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

	
	/**
	 * 	将文件中的有效信息行读出到一个字符串中存储
	 * 
	 * @param file
	 * @return
	 */
	public static String formatFileContent(File file) {
		
		StringBuilder sb=new StringBuilder();
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String s = null;
			while((s=br.readLine())!=null&&!s.startsWith("//")) {
				sb.append(s+"\n");
			}
			
			br.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	
	
}

/**
 * 	命令结果输出器
 * 
 * @author ZhangYuhui
 * @version 1.0
 */
class ResultOutputter{
	
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




