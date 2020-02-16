/**
 * InfectStatistic
 * TODO
 *
 * @author Spike
 * @version 1.0
 * @since 2020/2/12
 */
import java.text.SimpleDateFormat;
import java.util.Date;
class InfectStatistic{
    public static void main(String[] args) {
        System.out.println("It's testing!");
        //test       
        if(args.length!=0) {
        	CmdAnalysis test = new CmdAnalysis(args);
            if(test.isCmdString()) {
                System.out.println("正确命令");
                //test.showAll();
            }else {
            	System.out.println("错误命令");
            }
            readAndWriteLog raw = new readAndWriteLog(test.getLogLocation(),test.getOutLocation(),test.getLogDate(),
            test.getTypeOrder(),test.getProvinceShow());
            raw.showAll();   
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
	private int[] provinceShow = new int[32];	//默认全输出顺序,-1不必输出
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

		int currentIndex = i;
		if(i<cmdString.length) {
			for(;currentIndex < cmdString.length; currentIndex ++) {
				for(int j = 0;j < province.length;j++) {
					if(cmdString[currentIndex].equals(province[j]))
						provinceShow[j] = 1;
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
			if(provinceShow[i] != 0) {
				System.out.println(province[i]);
			}
			
		}
	}
}

/**
 *读取并统计日志文件
 */
class readAndWriteLog{
	private String logLocation;
	private String outLocation;
	private String logDate;
	private int[] typeOrder;
	private int[] provinceShow = new int[32];
	public readAndWriteLog(String logLocation,String outLocation,String logDate,int[] typeOrder,int[] provinceShow) {
		this.logLocation = logLocation;
		this.outLocation = outLocation;
		this.logDate = logDate;
		this.typeOrder = (int[])typeOrder.clone();
		this.provinceShow = (int[])provinceShow.clone();
	}
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
			if(provinceShow[i] != 0) {
				System.out.println(CmdAnalysis.province[i]);
			}
			
		}
	}
	
}