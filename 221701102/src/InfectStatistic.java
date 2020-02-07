import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
/**
 * InfectStatistic
 * TODO
 *
 * @author Lan
 * @version v 1.0
 * @since 2020/2/5
 */
class InfectStatistic {
	/*存储读取的命令行参数值*/
	private static String log = null; 
    private static String out = null; 
    private static String date = null; 
    private static ArrayList<String> type = new ArrayList();
    private static ArrayList<String> province = new ArrayList();
    
    /*参数值的相关判断*/
    private static boolean hasLog = false;
    private static boolean hasOut = false;
    private static boolean hasDate = false;
    private static boolean hasType = false;
    private static boolean hasProvince = false;
    
    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
    public static void main(String[] args) {
    	parseArguments(args);
    	printArgs();
    }
    
    /**
     * 输出命令行参数
     * 用于测试
     */
    private static void printArgs() {
		System.out.println("log:" + log);
		System.out.println("out:" + out);
		System.out.println("date:" + date);
		System.out.println("type:");
		System.out.println(type);
		System.out.println("province:");
		System.out.println(province);
	}

	/**
     * 解析命令行参数
     * @param args 
     */
    public static void parseArguments(String[] args) {
    	int length = args.length;
    	if(length > 0) {
    		//判断是否有list命令
        	if(!args[0].equals("list")) {
        		System.out.println("不是list命令！");
        		System.exit(0);
        	}
        	else {
        		for(int i = 1; i < length; i++) {
        			switch(args[i]) {
        				case "-log":
        					if(i+1 < length && isValue(args[i+1])) {
        						hasLog = true;
        						log = args[++i];
        					}
        					break;
        				case "-out":
        					if(i+1 < length && isValue(args[i+1])) {
        						hasOut = true;
        						out = args[++i];
        					}
        					break;
        				case "-date":
        					if(i+1 < length && isValue(args[i+1])) {
        						hasDate = true;
        						date = args[++i];
        					}
        					break;
        				case "-type":
        					while(i+1 < length && isValue(args[i+1])) {
        						if(isValidType(args[i+1])) {
        							type.add(args[i+1]);
        							hasType = true;
        							i++;
        						}
        						else {
        							System.out.println(args[i] + "是无效命令值！");
                        			System.exit(0);
        						}
        					}
                			break;
        				case "-province":
        					while(i+1 < length && isValue(args[i+1])) {
        						province.add(args[i+1]);
        						hasProvince = true;
        						i++;
        					}
                			break;
                		default:
                			System.out.println(args[i] + "是无效命令！");
                			System.exit(0);
        			}
            	}
        	}
        } 
    	else {
        	System.out.println("请输入命令行参数！"); 
        	System.exit(0);
        }
    	checkArgs();
    }

    /**
     * 参数的异常处理
     * 包括必须参数是否附带，参数值是否合法
     */
    private static void checkArgs() {
    	if(!hasLog) {
    		System.out.println("请输入\"-log\"参数！"); 
        	System.exit(0);
    	}
    	if(hasLog) {
    		File directory = new File(log);
            if(!directory.exists()) {
                directory.mkdirs();
            }
    	}
    	if(!hasOut) {
    		System.out.println("请输入\"-out\"参数！"); 
        	System.exit(0);
    	}
    	if(!hasDate) {
    		Date current = new Date(System.currentTimeMillis());
    		date = formatter.format(current);
    	}
    	if(hasDate) {
    		if(!isValidDate(date)) {
    			System.out.println("-date参数值无效！"); 
            	System.exit(0);
    		}
    	}
	}

	/**
     * 判断参数是否有参数值
     * @param string
     * @return
     */
	private static boolean isValue(String arg) {
		if(arg.equals("-log") || arg.equals("-out") || arg.equals("-date") || arg.equals("-type") || 
			arg.equals("-province")) {
			return false;
		}
		return true;	
	}
	
	/**
     * 判断Type参数值是否有效
     * @param string
     * @return
     */
    private static boolean isValidType(String type) {
		if(type.equals("ip") || type.equals("sp") || type.equals("cure") || type.equals("dead")) {
			return true;
		}
		return false;
	}

    /**
     * 判断日期值格式是否有效，是否超出范围
     * TODO 
     * -date不会提供在日志最晚一天后的日期，若提供应给与日期超出范围错误提示。
     * @param date2
     * @return
     */
	private static boolean isValidDate(String str) {
		try {
			Date strDate = (Date)formatter.parse(str);
			Date now = new Date();
			//日期是否超出今天
			if(now.compareTo(strDate) < 0) {
				return false;
			}
			//日期是否符合格式
			if(str.equals(formatter.format(strDate))) {
				return true;
			}
		} catch(Exception e) {
			return false;
		}
		return false;
	}
}