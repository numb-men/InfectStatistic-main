import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * InfectStatistic
 * TODO
 *
 * @author Spike
 * @version 1.0
 * @since 2020/2/12
 */
class InfectStatistic{
    public static void main(String[] args) {
        System.out.println("It's testing!");       
        if(args.length!=0) {
        	CmdAnalysis cmdObject = new CmdAnalysis(args);
            if(cmdObject.isCmdString()) {
                System.out.println("正确命令");
                //cmdObject.showAll();
            }else {
            	System.out.println("错误命令");
            }
            HandleLog handleObject = new HandleLog(cmdObject.getLogLocation(),cmdObject.getOutLocation(),cmdObject.getLogDate(),
            		cmdObject.getTypeOrder(),cmdObject.getProvinceShow());
            //handleObject.showAll();   
            handleObject.readLog();
        }
        
    }
    
}
/**
 *解析命令行参数
 */
class CmdAnalysis{
	
	private String[] cmdString;
	private String logLocation;
	private String outLocation;
	private String logDate;
	private int[] typeOrder = {0,1,2,3};	//默认全输出顺序,-1不必输出
	static String[] typeString = {"ip","sp","cure","dead"};
	private int[] provinceShow = new int[32];	//默认全输出顺序,-1不必输出,0需要输出
	static String[] province = {"全国", "安徽","北京", "重庆","福建","甘肃",
			"广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林",
			"江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海",
			"四川", "天津", "西藏", "新疆", "云南", "浙江"};
	
	public CmdAnalysis(String []args) {
	    cmdString = args;
	    //设置默认指定日期为一个接近无穷大的日期，这样方便比较，设置默认为全部统计
	    logDate = "9999-12-31";
	    for(int i = 0;i < provinceShow.length;i++)
	    	provinceShow[i] = 0;
	}
	/*
                *判断命令行参数是否正确，若正确则赋值保存
	 */
	public boolean isCmdString() {
		
		boolean mustLog = false;
		boolean mustOut = false;
		if(!cmdString[0].equals("list")) {
			System.out.println("命令行参数缺少list");
			return false;
		}
		for(int i = 0;i < cmdString.length;i++) {
			if(cmdString[i].equals("-log")) {
				mustLog = true;
				//检测日志文件路径合法性
				if(!isLogLocation(++i)) {
					System.out.println("日志文件路径不合法");
					return false;
				}
			}
			if(cmdString[i].equals("-out")) {
				mustOut = true;
				//检测输出路径合法性
				if(!isOutLocation(++i)) {
					System.out.println("输出路径不合法");
					return false;
				}
			}
			if(cmdString[i].equals("-date")) {
				//检测日期合法性
				if(!isCorrectDate(++i)) {
					System.out.println("指定日期不合法");
					return false;
				}
			}
			if(cmdString[i].equals("-type")) {
				if(!isType(++i)) {
					System.out.println("指定类型不合法");
					return false;
				}
			}
			if(cmdString[i].equals("-province")) {
				if(!isProvince(++i)) {
					System.out.println("指定省份不合法");
					return false;
				}
			}
		}
		if(mustLog && mustOut)
			return true;
		else {
			System.out.println("缺少输入-log 或 -out");
			return false;
		}
			
	}
	/*
    	*判断日志目录路径是否正确
	 */
	private boolean isLogLocation(int i) {
		
		if(i<cmdString.length) {
			String regex = "^[A-z]:(/|\\\\)(.+?(/|\\\\))*$";
			if(cmdString[i].matches(regex)) {
				logLocation = cmdString[i];
				return true;
			}else
				return false;
		}else
			return false;
	}
		/*
		  *判断输出路径是否正确
		 */
	private boolean isOutLocation(int i) {
		
		if(i<cmdString.length) {
			String regex = "^[A-z]:(/|\\\\)(.+?(/|\\\\))*(.+\\.txt)$";
			if(cmdString[i].matches(regex)) {
				outLocation = cmdString[i];
				return true;
			}else
				return false;
		}else
			return false;
	}
	/*
	  *判断指定日期是否正确
	 */
	private boolean isCorrectDate(int i) {
		
		if(i<cmdString.length) {
			if(isValidDate(cmdString[i])) {
				logDate = cmdString[i];
				return true;
			}else 
				return false;
		}else
			return false;
	}
	/*
	  *判断指定日期格式是否满足yyyy-MM-dd 字符串是否为数字
	 */
	private boolean isValidDate(String strDate) {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
            format.setLenient(false);
            Date date = format.parse(strDate);
            String[] sArray = strDate.split("-");
            for (String s : sArray) {
                boolean isNum = s.matches("[0-9]+");
                if (!isNum) {
                    return false;
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
        return true;
	}
	/*
	  *判断指定类型是否正确
	 */
	private boolean isType(int i) {

		for(int a = 0;a < typeOrder.length;a++)
			typeOrder[a] = -1;	
		int currentIndex = i;
		if(i<cmdString.length) {
			int t = 0;
			for(;currentIndex < cmdString.length; currentIndex ++) {
				if(cmdString[currentIndex].equals(typeString[0])) {
					if(t < typeOrder.length)
						typeOrder[t] = 0;
					t++;
				}	
				else if(cmdString[currentIndex].equals(typeString[1])) {
					if(t < typeOrder.length)
						typeOrder[t] = 1;
					t++;
				}
				else if(cmdString[currentIndex].equals(typeString[2])) {
					if(t < typeOrder.length)
						typeOrder[t] = 2;
					t++;
				}
				else if(cmdString[currentIndex].equals(typeString[3])) {
					if(t < typeOrder.length)
						typeOrder[t] = 3;
					t++;
				}
				else {
					break;
				}
			}
			if(t > 0 )
				return true;
			else
				return false;
		}else
			return false;
	}
	/*
	  * 判断指定省份是否正确
	 */
	private boolean isProvince(int i) {
		for(int k = 0;k < provinceShow.length;k++)
	    	provinceShow[k] = -1;
		int currentIndex = i;
		if(i<cmdString.length) {
			for(;currentIndex < cmdString.length; currentIndex ++) {
				for(int j = 0;j < province.length;j++) {
					if(cmdString[currentIndex].equals(province[j]))
						provinceShow[j] = 0;
				}
			}
			return true;
		}else
			return false;
	}
	
	public String getLogLocation() {
		return logLocation;
	}
	public String getOutLocation() {
		return outLocation;
	}
	public String getLogDate() {
		return logDate;
	}
	public int[] getTypeOrder() {
		return typeOrder;
	}
	public int[] getProvinceShow() {
		return provinceShow;
	}
	
	/**
	 * 用于测试输入
	 */
	public void showAll() {
		for(int i = 0;i < cmdString.length;i++) {
			System.out.println(cmdString[i]);
		}
		System.out.println(logLocation);
		System.out.println(outLocation);
		System.out.println(logDate);
		for(int i = 0;i < typeOrder.length;i++) {
			if(typeOrder[i] != -1) {
				System.out.println(typeString[typeOrder[i]]);
			}
			
		}
		for(int i = 0;i < provinceShow.length;i++) {
			if(provinceShow[i] != -1) {
				System.out.println(province[i]);
			}
			
		}
	}
}

/**
 *读取并统计日志文件
 */
class HandleLog{
	private String logLocation;
	private String outLocation;
	private String logDate;
	private int[] typeOrder;
	private int[] provinceShow = new int[32];
	public HandleLog(String logLocation,String outLocation,String logDate,int[] typeOrder,int[] provinceShow) {
		this.logLocation = logLocation;
		this.outLocation = outLocation;
		this.logDate = logDate;
		this.typeOrder = (int[])typeOrder.clone();
		this.provinceShow = (int[])provinceShow.clone();
	}
	/*
	 * 进入log目录读取日志文件
	 */
	public void readLog() {
		File file = new File(logLocation); 
		File[] files = file.listFiles();
		for(int i = 0;i < files.length;i++) {
			if(file.isDirectory()) {
				String filePath = files[i].getPath();
				if(filePath.compareTo(logLocation + logDate) <= 0) {
					System.out.println(filePath);
					statistics(filePath);
				}
			}
		}
	}
	/*
	 * 分类别统计：0新增 感染，1新增 疑似，2感染 流入，3疑似 流入，4死亡，5治愈，6确诊感染，7排除
	 */
	private void statistics(String filePath) {
		String handleType[] = {"\\S+ 新增 感染患者 \\d+人","\\S+ 新增 疑似患者 \\d+人",
				"\\S+ 感染患者 流入 \\S+ \\d+人","\\S+ 疑似患者 流入 \\S+ \\d+人",
				"\\S+ 死亡 \\d+人","\\S+ 治愈 \\d+人","\\S+ 疑似患者 确诊感染 \\d+人",
				"\\S+ 排除 疑似患者 \\d+人"};
		BufferedReader reader = null;
		try {
			// 指定读取文件的编码格式，要和写入的格式一致，以免出现中文乱码
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8")); 
			String str = null;
			while ((str = reader.readLine()) != null) {
				// 注释部分 "//"不处理
				if(!str.startsWith("//")) {
					if(str.matches(handleType[0])) {
						increaseIp(str);
					}
					else if(str.matches(handleType[1])){
						increaseSp(str);
					}
					else if(str.matches(handleType[2])){
						ipTransfer(str);
					}
					else if(str.matches(handleType[3])){
						spTransfer(str);
					}
					else if(str.matches(handleType[4])){
						dead(str);
					}
					else if(str.matches(handleType[5])){
						cure(str);
					}
					else if(str.matches(handleType[6])){
						ipDiagnose(str);
					}
					else if(str.matches(handleType[7])){
						exclude(str);
					}
				}
				System.out.println(str);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void increaseIp(String str) {
		
	}
	
	private void increaseSp(String str) {
		
	}
	
	private void ipTransfer(String str) {
		
	}
	
	private void spTransfer(String str) {
		
	}
	
	private void dead(String str) {
		
	}
	
	private void cure(String str) {
		
	}
	
	private void ipDiagnose(String str) {
		
	}
	
	private void exclude(String str) {
		
	}
	
	/**
	 * 用于测试输入
	 */
	public void showAll() {
		System.out.println(logLocation);
		System.out.println(outLocation);
		System.out.println(logDate);
		for(int i = 0;i < typeOrder.length;i++) {
			if(typeOrder[i] != -1) {
				System.out.println(CmdAnalysis.typeString[typeOrder[i]]);
			}
			
		}
		for(int i = 0;i < provinceShow.length;i++) {
			if(provinceShow[i] != -1) {
				System.out.println(CmdAnalysis.province[i]);
			}
			
		}
	}
	
}