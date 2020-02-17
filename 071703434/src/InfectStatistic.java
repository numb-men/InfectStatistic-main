package src;


import java.io.*;
import java.text.Collator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 	疫情统计
 * 
 * @author ZhangYuhui
 * @version 1.0
 */
class InfectStatistic {

    public static void main(String[] args) throws Exception {

    	
    	String[] string= {
    			"list","-log", "C:\\Users\\ThinkPad\\Desktop\\软件工程实践二\\log", "-out", "C:\\Users\\ThinkPad\\Desktop\\软件工程实践二\\output.txt",
        	};
   
    	CommandLine cmd=new CommandLine(string);
    	CommandLineParser parser=new CommandLineParser(cmd.getCommandName(), cmd.getParameters());
    	parser.excuteListCommand();
    
    }
}

/**
 *	命令行
 * 
 * @author ZhangYuhui
 * @version 1.0
 */
class CommandLine{
	
	private String commandName;
	private String[] parameters;
	
	public CommandLine(String[] args) {
		parameters=new String[args.length-1];
		commandName=args[0];
		//System.out.println(commandName);
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
	
	public void excuteCmd() {
		
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
 *	 命令的参数的值
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
 *	 命令的参数的值
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
	 * 	格式化List命令的参数，将字符串数组的参数列表转化为map中的值
	 * 
	 * @param parameters 
	 */
	@Override
	public void formatParameters() {
		
		for(int i=0;i<parameters.length;) {
			String parameter=parameters[i];
			//System.out.println(parameter);
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
		//System.out.println(this.toString());
		
	}
	

	/**
	 * 	判断格式化后的命令参数列表是否符合需求
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
 * 	命令行解析器
 * 
 * @author ZhangYuhui
 * @version 1.0
 */
class CommandLineParser{
	
	private Map<String,ParameterValue> parameterMap=new HashMap<>();
	
	
	public CommandLineParser(String commandName,String[] parameters) {
		if(commandName=="list") {
			parseListCommand(parameters);
		}
	}
	
	public void parseListCommand(String[] parameters) {
		ListParameters listParameters=new ListParameters(parameters);
		listParameters.formatParameters();
//		listParameters.judgeParameters();
		parameterMap=listParameters.getParametersMap();
		//System.out.println(parameterMap);
	}
	
	/**
	 * 	执行list命令
	 * @throws Exception 
	 * 
	 */
	public void excuteListCommand() throws Exception {
		
		String logAddress=(String) parameterMap.get("-log").value;
		LogFileReader logFileReader=new LogFileReader(logAddress);
		
		String content=logFileReader.defaultContent();
	//	System.out.println(content);
		LogContentParaser contentParaser=new LogContentParaser();
		Map<String,DailyInfectItem> resultMap=contentParaser.paraseLogContent(content);
		
		
//		for(Map.Entry<String, DailyInfectItem> entry:resultMap.entrySet()) {
//			System.out.println(entry.getKey()+entry.getValue().getAllResult());
//		}
//		
		
		String outputAddress=(String)parameterMap.get("-out").value;
		ResultOutputter outputter=new ResultOutputter(outputAddress,resultMap);
		//outputter.outputResult(resultMap);
	}
}

/**
 * 	某日某省的感染状况
 * 
 * @author ZhangYuhui
 * @version 1.0
 */
class DailyInfectItem{
	
	private int ip;
	private int sp;
	private int cure;
	private int dead;
	
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
	
	public void setAll(int ip, int sp, int cure, int dead) {
		this.ip = ip;
		this.sp = sp;
		this.cure = cure;
		this.dead = dead;
	}

	
	public String getAllResult() {
		return " 感染患者"+ip+"人"+" 疑似患者"+sp+"人"+ " 治愈"+cure+"人"+" 死亡"+dead+"人";
	}
	
	public String getIpResult() {
		return " 感染患者"+ip+"人";
	}
	
	public String getSpResult() {
		return " 疑似患者"+sp+"人";
	}
	
	public String getCureResult() {
		return " 治愈"+cure+"人";
	}
	
	public String getDeadResult() {
		return " 死亡"+dead+"人";
	}
	
}



/**
 * 	日志文件读取器
 * 
 * @author ZhangYuhui
 * @version 1.0
 */
class LogFileReader{


	private Map<String,String> dailyInfectMap=new HashMap<String,String>();
	String logDirectory;
	private LocalDate endDate;
	private LocalDate startDate;
//	private LocalDate parameterDate;	TODO
	final DateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
	
	public LogFileReader(String logDirectory) {
		this.startDate=this.endDate=null;
		this.logDirectory=logDirectory;
		//getBetweenDate();
		//generateDailyMap();
	}
	
	public Map<String, String> getDailyInfectMap() {
		return dailyInfectMap;
	}
	
	/**
	 * 	获取日志文件数组
	 * 
	 * @param logDirectory 放置日志的目录
	 * @return
	 */
	public File[] getLogFiles(String logDirectory) {
		File logDir=new File(logDirectory);
		if(logDir.exists()&&logDir.isDirectory()) {
			File[] logList=logDir.listFiles();
			
			
			return logList;
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
	public void getDateArrange(File[] logs) {
	
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
	
	/**
	 * 	获取两个日期间隔的所有日期
	 * 
	 * @return	List<String> dateList 两个日期之间的所有日期列表
	 */
	public  List<String> getBetweenDate(){
			
		List<String> dateList=new ArrayList<String>();
		
		long distance=ChronoUnit.DAYS.between(startDate, endDate);
		if(distance<1) {
			dateList.add(startDate.toString());
			return dateList;
		}
		
		Stream.iterate(startDate, d -> {
			return d.plusDays(1);
		}).limit(distance + 1).forEach(f -> {
			dateList.add(f.toString());
		});
		return dateList;
	}
	
	/**
	 * 	生成日期感染情况的map
	 * 
	 * @return	Map<String,String>日期-感染情况map
	 */
	public void generateDailyMap() {
		List<String> dateList=getBetweenDate();
		
		for(int i=0;i<dateList.size();i++) {
			dailyInfectMap.put(dateList.get(i), null);
		}
		
		File logDir=new File(logDirectory);
		if(logDir.exists()&&logDir.isDirectory()) {
			File[] logList=logDir.listFiles();
			
			for(int i=0;i<logList.length;i++) {
				
				if(logList[i].isFile()) {
					String logName=logList[i].getName();
					logName=logName.substring(0, logName.indexOf('.'));
					dailyInfectMap.put(logName, getFileContent(logList[i]));
				}
			}
		}
		//TODO
		//System.out.println(dailyInfectMap.toString());
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
	public static String getFileContent(File file) {
		
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
		
		//System.out.println(sb.toString());
		return sb.toString();
	}
	
	/**
	 * 
	 * 
	 * @return 内容字符串
	 */
	public String defaultContent() {
		File logDir=new File(logDirectory);
		String content="";
		if(logDir.exists()&&logDir.isDirectory()) {
			File[] logList=logDir.listFiles();
			for(int i=0;i<logList.length;i++) {
				content+=getFileContent(logList[i]);
			}
		}else {
			//	TODO:处理日志所在文件目录异常
		
		}
		//System.out.println(content);
		return content;
	}
}

/**
 * 	对日志
 * 
 * @author ZhangYuhui
 * @version 1.0
 */

class LogContentParaser{
	
	//Map<String,DailyInfectItem> resultMap=new HashMap<String, DailyInfectItem>();
	
	public LogContentParaser() {
		for(int i=0;i<provinces.length;i++) {
			infectMap.put(provinces[i], new DailyInfectItem(-1, -1, -1, -1));
		}
	}
	
	
	private Map<String,DailyInfectItem> infectMap=new HashMap<String, DailyInfectItem>();

	String[] provinces= {
			"安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林",
			"江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江",
	};

	
	//用于匹配类型的字符串
    final String IP_ADD_PATTERN = "(.*) 新增 感染患者 (\\d*)人";
    final String SP_ADD_PATTERN = "(.*) 新增 疑似患者 (\\d*)人";
    final String IP_FLOW_PATTERN = "(.*) 感染患者 流入 (.*) (\\d*)人";
    final String SP_FLOW_PATTERN = "(.*) 疑似患者 流入 (.*) (\\d*)人";
    final String DEAD_PATTERN = "(.*) 死亡 (\\d*)人";
    final String CURE_PATTERN = "(.*) 治愈 (\\d*)人";
    final String SP_CONFIRM_PATTERN = "(.*) 疑似患者 确诊感染 (\\d*)人";
    final String SP_EXCLUDE_PATTERN = "(.*) 排除 疑似患者 (\\d*)人";
    
    /**
	 * 	将字符串形式的感染状况转化为每日感染状况的结构
	 * 
	 * @param dailyInfectMap
	 * @throws Exception 
	 */
	public Map<String,DailyInfectItem> paraseLogContent(String content) throws Exception {
			
		//RegExpTextParser contentParser=new RegExpTextParser();
		String[] lines=content.split("\n");
		for(int i=0;i<lines.length;i++) {
			extractType(lines[i]);
		}
		
		return infectMap;

	}
    
    /**
     * 	从文本行中提取四种类型的数目，并存储到map中
     * 
     * @param lastDateMap
     * @param line
     * @throws Exception
     */
    public void extractType(String line) throws Exception {
    	
    	Pattern p1 = Pattern.compile(IP_ADD_PATTERN);
        Pattern p2 = Pattern.compile(SP_ADD_PATTERN);
        Pattern p3 = Pattern.compile(IP_FLOW_PATTERN);
        Pattern p4 = Pattern.compile(SP_FLOW_PATTERN);
        Pattern p5 = Pattern.compile(DEAD_PATTERN);
        Pattern p6 = Pattern.compile(CURE_PATTERN);
        Pattern p7 = Pattern.compile(SP_CONFIRM_PATTERN);
        Pattern p8 = Pattern.compile(SP_EXCLUDE_PATTERN);
        
        
        matchIpAdd(p1,line);
        matchSpAdd(p2,line);
        matchIpFlow(p3,line);
        matchSpFlow(p4, line);
        matchDead(p5, line);
        matchCure(p6, line);
        matchSpConfirm(p7, line);
        matchSpExclude(p8, line);
        
        for(int i=0;i<provinces.length;i++) {
        	if(infectMap.get(provinces[i]).getIp()<0) {
        		infectMap.get(provinces[i]).setAll(0, 0, 0, 0);
        	}
			
		}
        
    }
	
	 /**
     * 	新增感染患者
     * 
     * @param p
     * @param line
     */
    public void matchIpAdd(Pattern p,String line) {
    	//p = Pattern.compile(IP_ADD_PATTERN);
    	Matcher m=p.matcher(line);
    	while(m.find()) {
    		//System.out.println(m.group(1)+thisDateMap.get(m.group(1)).getIp());
    		int addNum=Integer.valueOf(m.group(2));
    		if(infectMap.get(m.group(1)).getIp()<0) {//省份不存在,则创建一个感染项
    			infectMap.get(m.group(1)).setAll(addNum, 0, 0, 0);
    		}else {//省份存在，修改感染项
    			int ip=infectMap.get(m.group(1)).getIp();
    			infectMap.get(m.group(1)).setIp(ip+addNum);
    		}
    		
    		//System.out.println(m.group(1)+thisDateMap.get(m.group(1)).getIp());
    	}
    }
    
    /**
     * 	新增疑似患者
     * 
     * @param p
     * @param line
     */
    public void matchSpAdd(Pattern p,String line) {
    	Matcher m=p.matcher(line);
    	while(m.find()) {
    		int addNum=Integer.valueOf(m.group(2));
    		if(infectMap.get(m.group(1)).getIp()<0) {//省份不存在,则创建一个感染项
    			infectMap.get(m.group(1)).setAll(0, addNum, 0, 0);
    		}else {//省份存在，修改感染项
    			int sp=infectMap.get(m.group(1)).getSp();
    			infectMap.get(m.group(1)).setSp(sp+addNum);
    		}
    	}
    }
    
    /**
     * 	感染患者流动
     * 
     * @param p
     * @param line
     * @throws Exception //流出省不存在
     */
    public void matchIpFlow(Pattern p,String line) throws Exception {
    	Matcher m=p.matcher(line);
    	while(m.find()) {
    		if(infectMap.get(m.group(1)).getIp()<0) {//流出省不存在
    			// TODO:报错
    			throw new Exception();
    		}
    		if(infectMap.get(m.group(2)).getIp()<0) {//流入省不存在
    			infectMap.get(m.group(2)).setAll(0, 0, 0, 0);
    		}
    		
    		int flowNum=Integer.valueOf(m.group(3));
			int orignalNum1=infectMap.get(m.group(1)).getIp();
			int orignalNum2=infectMap.get(m.group(2)).getIp();
			if(orignalNum1-flowNum<0) {
				// TODO:当流出数目大于省份原有数目则报错
				throw new Exception();
			}
			infectMap.get(m.group(1)).setIp(orignalNum1-flowNum);
			infectMap.get(m.group(2)).setIp(orignalNum2+flowNum);
    	}
    	
    }
    
    /**
     * 	疑似患者流动
     * 
     * @param p
     * @param line
     * @throws Exception 
     */
    public void matchSpFlow(Pattern p,String line) throws Exception {
    	Matcher m=p.matcher(line);
    	while(m.find()) {
    		if(infectMap.get(m.group(1)).getIp()<0) {//流出省不存在
    			// TODO:报错
    			throw new Exception();
    		}
    		if(infectMap.get(m.group(2)).getIp()<0) {//流入省不存在
    			infectMap.get(m.group(2)).setAll(0, 0, 0, 0);
    		}
    		
    		int flowNum=Integer.valueOf(m.group(3));
			int orignalNum1=infectMap.get(m.group(1)).getSp();
			int orignalNum2=infectMap.get(m.group(2)).getSp();
			if(orignalNum1-flowNum<0) {
				// TODO:当流出数目大于省份原有数目则报错
				throw new Exception();
			}
			infectMap.get(m.group(1)).setSp(orignalNum1-flowNum);
			infectMap.get(m.group(2)).setSp(orignalNum2+flowNum);
    	}
    }
    
    /**
     * 	患者死亡
     * 
     * @param p
     * @param line
     * @throws Exception 对应省份以前没有患者记录
     */
    public void matchDead(Pattern p,String line) throws Exception {
    	Matcher m=p.matcher(line);
    	while(m.find()) {
    		if(infectMap.get(m.group(1)).getIp()<0) {//省份不存在
    			// TODO:报错
    			throw new Exception();
    		}else {
    			int deadNum=Integer.valueOf(m.group(2));
    			int originDead=infectMap.get(m.group(1)).getDead();
    			int originIp=infectMap.get(m.group(1)).getIp();
    			if(originIp-deadNum<0) {
    				// TODO：如果死亡人数大于原本感染患者人数则抛出异常
    				throw new Exception();
    			}
    			infectMap.get(m.group(1)).setIp(originIp-deadNum);
    			infectMap.get(m.group(1)).setDead(originDead+deadNum);
    		}
    		
    	}
    	
    }
    
    /**
     * 	患者治愈
     * 
     * @param p
     * @param line
     */
    public void matchCure(Pattern p,String line) throws Exception {
    	Matcher m=p.matcher(line);
    	while(m.find()) {
    		if(infectMap.get(m.group(1)).getIp()<0) {//省份不存在
    			// TODO:报错
    			throw new Exception();
    		}else {
    			int cureNum=Integer.valueOf(m.group(2));
    			int originCure=infectMap.get(m.group(1)).getCure();
    			int originIp=infectMap.get(m.group(1)).getIp();
    			if(originIp-cureNum<0) {
    				// TODO：如果治愈人数大于原本感染患者人数则抛出异常
    				throw new Exception();
    			}
    			infectMap.get(m.group(1)).setIp(originIp-cureNum);
    			infectMap.get(m.group(1)).setCure(originCure+cureNum);
    		}
    	}
    }
    
    /**
     * 	疑似患者确认感染
     * 
     * @param p
     * @param line
     * @throws Exception 
     */
    public void matchSpConfirm(Pattern p,String line) throws Exception {
    	Matcher m=p.matcher(line);
    	while(m.find()) {
    		if(infectMap.get(m.group(1)).getIp()<0) {//省份不存在
    			// TODO:报错
    			throw new Exception();
    		}else {
    			
    			int confirmNum=Integer.valueOf(m.group(2));
    			int originIp=infectMap.get(m.group(1)).getIp();
    			int originSp=infectMap.get(m.group(1)).getSp();
    			if(originSp-confirmNum<0) {
    				// TODO：如果确诊患者人数大于原本疑似患者人数则抛出异常
    				throw new Exception();
    			}
    			infectMap.get(m.group(1)).setIp(originIp+confirmNum);
    			infectMap.get(m.group(1)).setSp(originSp-confirmNum);
    		}
    	}
    }
    
    /**
     * 	排除疑似患者
     * 
     * @param p
     * @param line
     * @throws Exception 
     */
    public void matchSpExclude(Pattern p,String line) throws Exception {
    	Matcher m=p.matcher(line);
    	while(m.find()) {
    		if(infectMap.get(m.group(1)).getIp()<0) {//省份不存在
    			// TODO:报错
    			throw new Exception();
    		}else {
    			
    			int  excludeNum=Integer.valueOf(m.group(2));
    			int originSp=infectMap.get(m.group(1)).getSp();
    			if(originSp-excludeNum<0) {
    				// TODO：如果排除疑似患者人数大于原本疑似患者人数则抛出异常
    				throw new Exception();
    			}
    			infectMap.get(m.group(1)).setSp(originSp-excludeNum);
    		}
    	}
    }
    
	
}



/**
 * 	命令结果输出器
 * 
 * @author ZhangYuhui
 * @version 1.0
 */
class ResultOutputter{
	
	Map<String,DailyInfectItem> resultMap=new HashMap<String, DailyInfectItem>();
	Map<String,ParameterValue> listParameterMap=new HashMap<String, ParameterValue>();
	String outPath;
	
	public ResultOutputter(String outPath,Map<String,DailyInfectItem> resultMap,Map<String,ParameterValue> listParameterMap) {
		this.resultMap=resultMap;
		this.listParameterMap=listParameterMap;
		this.outPath=outPath;
	}
	
	public String getFinalResult() {
		
		getTotal();
		getProvinceResult();
		
		StringBuilder sb=new StringBuilder();
		//解决重庆排序与拼音不符问题
		if(resultMap.containsKey("重庆")) {
			resultMap.put("冲庆", resultMap.remove("重庆"));
		}
		List<Map.Entry<String, DailyInfectItem>> list=new ArrayList<Map.Entry<String,DailyInfectItem>>(resultMap.entrySet());
		Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);   
		
		
		Collections.sort(list,new Comparator<Map.Entry<String, DailyInfectItem>>() {
			 public int compare(Map.Entry<String, DailyInfectItem> o1, Map.Entry<String, DailyInfectItem> o2) {
				 return cmp.compare(o1.getKey(), o2.getKey());
			 }
		});
		
		//resultMap.put("重庆", resultMap.remove("冲庆"));
		if(resultMap.containsKey("全国")) {
			sb.append("全国");
			if(listParameterMap.get("-type").value!=null) {
				LinkedList<String> typeList=(LinkedList<String>)listParameterMap.get("-type").value;
				for(int i=0;i<typeList.size();i++) {
					switch(typeList.get(i)) {
						case "ip":
							sb.append(resultMap.get("全国").getIpResult());break;
						case "sp":
							sb.append(resultMap.get("全国").getSpResult());break;
						case "cure":
							sb.append(resultMap.get("全国").getIpResult());break;
						case "dead":
							sb.append(resultMap.get("全国").getSpResult());break;	
						default:break;
					}
				}
			}else {
				
			}
		}
		
		for(int i=0;i<list.size();i++) {
			for(int j=0;j<TYPES.length;j++) {
				sb.append(list.get(i).getKey())
			}
		}
		
		
		return sb.toString();
	}
	
	/**
	 * 	将结果字符串输入到指定路径的文件中
	 * 
	 * @param s
	 */
	public void output(String s) {
		try {
			FileOutputStream out = new FileOutputStream(new File(outPath));
			BufferedOutputStream Buff = new BufferedOutputStream(out);
			Buff.write(s.getBytes());
			Buff.flush();
            Buff.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * 	获得全国的情况并且将它加入到resultMap中
	 * 
	 * @param resultMap
	 */
	public void getTotal() {
		
		StringBuilder sb=new StringBuilder();
		int totalIp=0,totalSp=0,totalCure=0,totalDead=0;
		for(Map.Entry<String, DailyInfectItem> entry:resultMap.entrySet()) {
			totalIp+=entry.getValue().getIp();
			totalSp+=entry.getValue().getSp();
			totalCure+=entry.getValue().getCure();
			totalDead+=entry.getValue().getDead();
		}
		
		resultMap.put("全国", new DailyInfectItem(totalIp, totalSp, totalCure, totalDead));
		
	

	}
	
	/**
	 * 	根据province参数获得相应的结果
	 */
	public void getProvinceResult() {
		
		StringBuilder sb=new StringBuilder();
		if(listParameterMap.get("-province").value!=null) {
			LinkedList<String> provinces=(LinkedList<String>) listParameterMap.get("-province").value;
			resultMap.entrySet().removeIf(m->(!provinces.contains(m.getKey())));
		}
	
	}
	
}


