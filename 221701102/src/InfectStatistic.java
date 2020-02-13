import java.io.*;
import java.util.*;
import java.util.regex.*;

import org.junit.Test;

import java.text.*;
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
	private static ArrayList<String> dateList = new ArrayList();
	private static HashMap<String, Province> provinceMap = new HashMap<String, Province>();

	/*参数值的相关判断*/
	private static boolean hasLog = false;
	private static boolean hasOut = false;
	private static boolean hasDate = false;
	private static boolean hasType = false;
	private static boolean hasProvince = false;

	//日期格式
	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) {
		parseArguments(args);
		readDirectory();
		readFileList();
		getTotal();
		System.out.println(provinceMap);
	}

	/**
	 * 读取文件目录下的所有文件，筛选符合条件的日志文件
	 */
	private static void readDirectory() {
		File file = new File(log);
		File[] fs = file.listFiles();
		String lastDate = null; //存储文件夹中的最晚日期
		for(File f : fs) {
			if(!f.isDirectory()) {
				String fileName = f.getName();
				String fileDate = fileName.substring(0, fileName.indexOf("."));
				//判断是否为符合条件的日期
				Pattern p = Pattern.compile("\\d{4}\\-\\d{2}\\-\\d{2}.log.txt");
				Matcher m = p.matcher(fileName);
				if(!m.matches()) {
					continue;
				}
				if(lastDate == null) {
					lastDate = fileDate;
				}
				else {
					if(fileDate.compareTo(lastDate) > 0) {
						lastDate = fileDate;
					}
				}
				if(date.compareTo(fileDate) >= 0) {
					dateList.add(fileDate);
				}
			}
		}
		if(date.compareTo(lastDate) > 0) {
			System.out.println("日期超出范围！");
			System.exit(0);
		}
	}

	/**
	 * 读取符合条件的文件
	 */
	private static void readFileList() {
		String fileName = null;
		for(String date : dateList) {
			fileName = log + date +".log.txt";
			readFile(fileName);
		}
	}

	/**
	 * 根据文件名读取文件
	 * @param fileName
	 */
	private static void readFile(String fileName) {
		try {
			File file = new File(fileName);
			if(file.isFile() && file.exists()) {
				BufferedReader reader;
				try {
					reader = new BufferedReader(new FileReader(file));
					String line = reader.readLine();
					while(line != null) {
						if(!line.matches("[/]+.*")) {
							getMatch(line);
						}
						line = reader.readLine();
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch(Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
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
	 * 利用责任链模式进行正则字符串比对
	 * @param str
	 */
	public static void getMatch(String str) {
		//初始化责任链
	    LogHandler ipHandler = new IPHandler();
	    LogHandler spHandler = new SPHandler();
	    LogHandler cipHandler = new ChangeIPHandler();
	    LogHandler cspHandler = new ChangeSPHandler();
	    LogHandler nipHandler = new NewIPHandler();
	    LogHandler rspHandler = new RemoveSPHandler();
	    LogHandler cureHandler = new CureHandler();
	    LogHandler deadHandler = new DeadHandler();
	    
	    ipHandler.setNextHandler(spHandler);
	    spHandler.setNextHandler(cipHandler);
	    cipHandler.setNextHandler(cspHandler);
	    cspHandler.setNextHandler(nipHandler);
	    nipHandler.setNextHandler(rspHandler);
	    rspHandler.setNextHandler(cureHandler);
	    cureHandler.setNextHandler(deadHandler);
	    
	    //处理事件    
	    ipHandler.handleLog(str, provinceMap);
	}
	
	/**
	 * 得到全国的疫情情况
	 */
	private static void getTotal() {
		Iterator<Map.Entry<String, Province>> iterator = provinceMap.entrySet().iterator();
		Province pro = null;
		int ip = 0;
		int sp = 0;
		int cure = 0;
		int dead = 0;
		while(iterator.hasNext()) {
			Map.Entry<String, Province> entry = iterator.next();
			pro = entry.getValue();
			ip += pro.getIp();
			sp += pro.getSp();
			cure += pro.getCure();
			dead += pro.getDead();
		}
		Province total = new Province("全国");
		total.setIp(ip);
		total.setSp(sp);
		total.setCure(cure);
		total.setDead(dead);
		provinceMap.put("全国", total);
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
	 * 
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


/**
 * Province类
 * 存储省份及疫情人数情况
 *
 * @author Lan
 * @version v 1.0
 */
class Province {
	String name;
	int ip;
	int sp;
	int cure;
	int dead;
	
	public Province(String name) {
		this.name = name;
		ip = 0;
		sp = 0;
		cure = 0;
		dead = 0;
	}
	
	public Province(String name, int ip, int sp, int cure, int dead) {
		this.name = name;
		this.ip = ip;
		this.sp = sp;
		this.cure = cure;
		this.dead = dead;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	@Override
	//湖北 感染患者10人 疑似患者15人 治愈2人 死亡1人
	public String toString() {
		return getName() + " " + "感染患者" + getIp() + "人 疑似患者" + getSp() + "人 治愈" + getCure() + "人 死亡" + getDead() + "人";
	}
}

/**
 * LogHandle
 * 不同类型的日志行
 * 
 * @author Lan
 * @version v 1.0
 */
abstract class LogHandler {

	//下一个责任链成员
    protected LogHandler nextHandler;

    public LogHandler getNextHandler() {
        return nextHandler;
    }
    
    public void setNextHandler(LogHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract void handleLog(String str, HashMap<String, Province> provinceMap);
}

/**
 * <省> 新增 感染患者 n人
 * 
 * @author Lan
 * @version v 1.0
 */class IPHandler extends LogHandler {
	protected LogHandler nextHandler;
	
    @Override
    public void handleLog(String str, HashMap<String, Province> provinceMap) {
    	Pattern pattern = Pattern.compile("(.*?) 新增 感染患者 (.*?)人");
		Matcher matcher = pattern.matcher(str);
		boolean find = false;
		if(matcher.find()) {
			String pro = matcher.group(1);
			int cnt = Integer.parseInt(matcher.group(2));
			//TODO:需要判断-province是否含有此省份
			if(!provinceMap.containsKey(pro)) {
				provinceMap.put(pro, new Province(pro));	
			}
			Province province = provinceMap.get(pro);
			province.setIp(province.getIp() + cnt);
			provinceMap.put(pro, province);	
		}
		if(find == false) {
			if(getNextHandler() != null) {
				getNextHandler().handleLog(str, provinceMap);
			}
		}
    }
}

/**
 * <省> 新增 疑似患者 n人
 * 
 * @author Lan
 * @version v 1.0
 */
class SPHandler extends LogHandler {
	protected LogHandler nextHandler;
	
    @Override
    public void handleLog(String str, HashMap<String, Province> provinceMap) {
    	Pattern pattern = Pattern.compile("(.*?) 新增 疑似患者 (.*?)人");
    	Matcher matcher = pattern.matcher(str);
		if(matcher.find()) {
			String pro = matcher.group(1);
			int cnt = Integer.parseInt(matcher.group(2));
			if(!provinceMap.containsKey(pro)) {
				provinceMap.put(pro, new Province(pro));
			}
			Province province = provinceMap.get(pro);
			province.setSp(province.getSp() + cnt);
			provinceMap.put(pro, province);
		}
		else {
			if(getNextHandler() != null) {
				getNextHandler().handleLog(str, provinceMap);
            }
        }
    }
}

/**
 * <省1> 感染患者 流入 <省2> n人
 * 
 * @author Lan
 * @version v 1.0
 */
class ChangeIPHandler extends LogHandler {
	protected LogHandler nextHandler;
	
    @Override
    public void handleLog(String str, HashMap<String, Province> provinceMap) {
    	Pattern pattern = Pattern.compile("(.*?) 感染患者 流入 (.*?) (.*?)人");
    	Matcher matcher = pattern.matcher(str);
		if(matcher.find()) {
			String pro1 = matcher.group(1);
			String pro2 = matcher.group(2);
			int cnt = Integer.parseInt(matcher.group(3));
			if(!provinceMap.containsKey(pro1)) {
				provinceMap.put(pro1, new Province(pro1));
			}
			Province province1 = provinceMap.get(pro1);
			province1.setIp(province1.getIp() - cnt);
			provinceMap.put(pro1, province1);
			if(!provinceMap.containsKey(pro2)) {
				provinceMap.put(pro2, new Province(pro2));
			}
			Province province2 = provinceMap.get(pro2);
			province2.setIp(province2.getIp() + cnt);
			provinceMap.put(pro2, province2);
		}
		else {
			if(getNextHandler() != null) {
				getNextHandler().handleLog(str, provinceMap);
            }
        }
    }
}

/**
 * <省1> 疑似患者 流入 <省2> n人
 * 
 * @author Lan
 * @version v 1.0
 */
class ChangeSPHandler extends LogHandler {
	protected LogHandler nextHandler;
	
    @Override
    public void handleLog(String str, HashMap<String, Province> provinceMap) {
    	Pattern pattern = Pattern.compile("(.*?) 疑似患者 流入 (.*?) (.*?)人");
    	Matcher matcher = pattern.matcher(str);
		if(matcher.find()) {
			String pro1 = matcher.group(1);
			String pro2 = matcher.group(2);			
			int cnt = Integer.parseInt(matcher.group(3));
			if(!provinceMap.containsKey(pro1)) {
				provinceMap.put(pro1, new Province(pro1));
			}
			Province province1 = provinceMap.get(pro1);
			province1.setSp(province1.getSp() - cnt);
			provinceMap.put(pro1, province1);
			if(!provinceMap.containsKey(pro2)) {
				provinceMap.put(pro2, new Province(pro2));	
			}
			Province province2 = provinceMap.get(pro2);
			province2.setSp(province2.getSp() + cnt);
			provinceMap.put(pro2, province2);
		}
		else {
			if(getNextHandler() != null) {
				getNextHandler().handleLog(str, provinceMap);
            }
        }
    }
}

/**
 * <省> 疑似患者 确诊感染 n人
 * 
 * @author Lan
 * @version v 1.0
 */
class NewIPHandler extends LogHandler {
	protected LogHandler nextHandler;
	
    @Override
    public void handleLog(String str, HashMap<String, Province> provinceMap) {
    	Pattern pattern = Pattern.compile("(.*?) 疑似患者 确诊感染 (.*?)人");
    	Matcher matcher = pattern.matcher(str);
		if(matcher.find()) {
			String pro = matcher.group(1);
			int cnt = Integer.parseInt(matcher.group(2));
			if(!provinceMap.containsKey(pro)) {
				provinceMap.put(pro, new Province(pro));	
			}
			Province province = provinceMap.get(pro);
			province.setSp(province.getSp() - cnt);
			province.setIp(province.getIp() + cnt);
			provinceMap.put(pro, province);
		}
		else {
			if(getNextHandler() != null) {
				getNextHandler().handleLog(str, provinceMap);
            }
        }
    }
}

/**
 * <省> 排除 疑似患者 n人
 * 
 * @author Lan
 * @version v 1.0
 */
class RemoveSPHandler extends LogHandler {
	protected LogHandler nextHandler;
	
    @Override
    public void handleLog(String str, HashMap<String, Province> provinceMap) {
    	Pattern pattern = Pattern.compile("(.*?) 排除 疑似患者 (.*?)人");
    	Matcher matcher = pattern.matcher(str);
		if(matcher.find()) {
			String pro = matcher.group(1);
			int cnt = Integer.parseInt(matcher.group(2));
			if(!provinceMap.containsKey(pro)) {
				provinceMap.put(pro, new Province(pro));
			}
			Province province = provinceMap.get(pro);
			province.setSp(province.getSp() - cnt);
			provinceMap.put(pro, province);
		}
		else {
			if(getNextHandler() != null) {
				getNextHandler().handleLog(str, provinceMap);
            }
        }
    }
}

/**
 * <省> 治愈 n人
 * 
 * @author Lan
 * @version v 1.0
 */
class CureHandler extends LogHandler {
	protected LogHandler nextHandler;
	
    @Override
    public void handleLog(String str, HashMap<String, Province> provinceMap) {
    	Pattern pattern = Pattern.compile("(.*?) 治愈 (.*?)人");
    	Matcher matcher = pattern.matcher(str);
		if(matcher.find()) {
			String pro = matcher.group(1);
			int cnt = Integer.parseInt(matcher.group(2));
			if(!provinceMap.containsKey(pro)) {
				provinceMap.put(pro, new Province(pro));
			}
			Province province = provinceMap.get(pro);
			province.setIp(province.getIp() - cnt);
			province.setCure(province.getCure() + cnt);
			provinceMap.put(pro, province);
		}
		else {
			if(getNextHandler() != null) {
				getNextHandler().handleLog(str, provinceMap);
            }
        }
    }
}

/**
 * <省> 死亡 n人
 * 
 * @author Lan
 * @version v 1.0
 */
class DeadHandler extends LogHandler {
	protected LogHandler nextHandler;
	
    @Override
    public void handleLog(String str, HashMap<String, Province> provinceMap) {
    	Pattern pattern = Pattern.compile("(.*?) 死亡 (.*?)人");
    	Matcher matcher = pattern.matcher(str);
		if(matcher.find()) {
			String pro = matcher.group(1);
			int cnt = Integer.parseInt(matcher.group(2));
			if(!provinceMap.containsKey(pro)) {
				provinceMap.put(pro, new Province(pro));
			}
			Province province = provinceMap.get(pro);
			province.setIp(province.getIp() - cnt);
			province.setDead(province.getDead() + cnt);
			provinceMap.put(pro, province);
		}
		else {
			if(getNextHandler() != null) {
				getNextHandler().handleLog(str, provinceMap);
            }
        }
    }
}