/**
 * InfectStatistic
 * TODO
 *
 * @author 暮笑
 * @version 1.0
 * @since 2020-02-06
 */

import java.io.File;
import java.io.*;
import java.util.regex.*;
import java.util.Date;
import java.text.SimpleDateFormat;

class InfectStatistic {
	public String log_path; //日志文件位置
	public String out_path; //输出文件位置
	/*
	 * 指定日期
	 * 将指定日期默认设置为当前日期（即启动程序的该天日期）
	 */
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Date d = new Date(System.currentTimeMillis());
	public String date = formatter.format(d); //指定日期
	
	/*
	 * 指定类型(按顺序分别为ip,sp,cure,dead)
	 * 当数值不为0时表示需要列出，为0时无需列出
	 * 默认顺序为1,2,3,4，即全部列出并按照上述顺序
	 */
	public int[] type = {1,2,3,4};
	/*
	 * 类型列表(按顺序分别为ip,sp,cure,dead)
	 */
	public String[] type_str = {"感染患者", "疑似患者", "治愈", "死亡"};
	
	/*
	 * 指定省份(按省份名称排序（第一位为全国）)
	 * 当数值为1时表示需要列出，为0时无需列出
	 * 默认第一位为-1,其他全为0,代表未指定省份
	 */
	public int[] province = new int[35];
	
	/*
	 * 省份列表(按省份名称拼音排序（第一位为全国）)
	 */
	public String[] province_str = {"全国", "安徽", "澳门" ,"北京", "重庆", "福建","甘肃",
			"广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林",
			"江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海",
			"四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江"};
	/*
	 * 人数统计(一维代表省份排序，二维表示类型排序)
	 * 按省份名称拼音排序（第一位为全国）
	 * 类型按顺序分别为ip,sp,cure,dead
	 */
	public int[][] people = new int[35][4]; 
	
	/*
	 * 解析命令行参数
	 */
	class CmdArgs{
		String[] args; //保存传入的命令行
		
		CmdArgs(String[] args_str){
			args = args_str;
			province[0] = -1; //将全国指定位置为-1
		}
		
		/*
		 * 提取命令行中的参数
		 * @return boolean
		 */
		public boolean extractCmd() {
			if(!args[0].equals("list")) {//判断命令格式开头是否正确
				System.out.println("命令行格式有误——开头非list错误");
				return false;
			}
			
			int i;
			
			for(i = 1; i < args.length; i++) {
				if(args[i].equals("-log")) { //读取到-log参数
					i = getLogPath(++i);
					if(i == -1) { //说明上述步骤中发现命令行出错
						System.out.println("命令行格式有误——日志路径参数错误");
						return false;
					}
				} else if(args[i].equals("-out")) { //读取到-out参数
					i= getOutPath(++i);
					if(i == -1) { //说明上述步骤中发现命令行出错
						System.out.println("命令行格式有误——输出路径参数错误");
						return false;
					}
				} else if(args[i].equals("-date")) { //读取到-date参数
					i= getDate(++i);
					if(i == -1) { //说明上述步骤中发现命令行出错
						System.out.println("命令行格式有误——日期参数错误");
						return false;
					}
				} else if(args[i].equals("-type")) { //读取到-type参数
					i = getType(++i); 
					if(i == -1) { //说明上述步骤中发现命令行出错
						System.out.println("命令行格式有误——指定类型参数错误");
						return false;
					}
				} else if(args[i].equals("-province")) { //读取到-province参数
					i = getProvince(++i);
					if(i == -1) { //说明上述步骤中发现命令行出错
						System.out.println("命令行格式有误——指定省份参数错误");
						return false;
					}
				} else { //未读取到以上参数
					System.out.println("命令行格式有误——输入非以上参数错误");
					return false;
				}
			}
			return true;
		}
		
		/*
		 * 得到日志文件位置
		 * @param i
		 * @return int
		 */
		public int getLogPath(int i) {
			if(i < args.length) { //当下标未越界
				if(args[i].matches("^[A-z]:\\\\(.+?\\\\)*$")) //判断字符串是不是文件目录路径
					log_path = args[i];
				else
					return -1;
			} else
				return -1;
			return i;
		}
		
		/*
		 * 得到日志文件位置
		 * @param i
		 * @return int
		 */
		public int getOutPath(int i) {
			if(i < args.length) { //当下标未越界
				if(args[i].matches("^[A-z]:\\\\(\\S+)+(\\.txt)$")) //判断字符串是不是txt文件路径
					out_path = args[i];
				else
					return -1;
			} else
				return -1;
			return i;
		}
		
		/*
		 * 得到指定日期
		 * @param i
		 * @return int
		 */
		public int getDate(int i) {
			if(i < args.length) { //当下标未越界
				if(isValidDate(args[i])) { //判断是否是合法日期格式：yyyy-MM-dd
					if(date.compareTo(args[i]) >= 0) //判断日期是否超过当前日期
						date = args[i] + ".log.txt";
					else
						return -1;
				} else
					return -1;
			} else
				return -1;
			return i;
		}
		
		/*
		 * 判断是否是合法日期格式：yyyy-MM-dd
		 * @param strDate
		 */
		public boolean isValidDate(String strDate) {
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	        try {
	            //设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期
	        	//比如2018-02-29会被接受，并转换成2018-03-01 
	            format.setLenient(false);
	            format.parse(strDate);
	            
	            //判断传入的yyyy年-MM月-dd日 字符串是否为数字
	            String[] sArray = strDate.split("-");
	            for (String s : sArray) {
	                boolean isNum = s.matches("[0-9]+");
	                if (!isNum)
	                    return false;
	            }
	        } catch (Exception e) {
	            return false;
	        }
	        return true;
	    }
		
		/*
		 * 得到指定类型(type参数可能有多个)
		 * @param i
		 * @return int
		 */
		public int getType(int i) {
			int j, m = i; 

			if(i < args.length) { //当下标未越界
				for(j = 0; j < 4; j++) //将默认指定全置为0
					type[j] = 0;
				j = 1; //j用来指定类型的顺序输出
				while(i<args.length) {
					if(args[i].equals("ip")) {
						type[0] = j;
						i++;
						j++;
					} else if(args[i].equals("sp")) {
						type[1] = j;
						i++;
						j++;
					} else if(args[i].equals("cure")) {
						type[2] = j;
						i++;
						j++;
					} else if(args[i].equals("dead")) {
						type[3] = j;
						i++;
						j++;
					} else
						break;
				}
			}
			if(m == i) //说明-type后无正确参数
				return -1;
			return (i - 1); //接下来不为-type的参数，或越界
		}
		
		/*
		 * 得到指定省份(province参数可能有多个)
		 * @param i
		 * @return int
		 */
		public int getProvince(int i) {
			int j, m = i;

			if(i < args.length){
				province[0] = 0; //取消未指定状态标记
				while(i<args.length) {
					for(j = 0; j < province_str.length; j++) {
						if(args[i].equals(province_str[j])) { //如果参数找到对应省份
							province[j] = 1; //指定该省份需要输出
							i++;
							break;
						}
					}
				}
			}
			if(m == i) //说明-province后无正确参数
				return -1;
			return (i - 1); //接下来不为province的参数，或越界
		}
		
	}
	
	/*
	 * 处理文件（读取目录下的文件，打开文件，读取内容，输出内容到文件）
	 */
	class FileHandle{
		
		
		FileHandle() {} //空构造函数
		
		/*
		 * 读取指定路径下的文件名和目录名
		 */
		public void getFileList() {
			File file = new File(log_path);
			File[] fileList = file.listFiles();
			String fileName;
	 
			for (int i = 0; i < fileList.length; i++) {
				fileName = fileList[i].getName();
				if (fileName.compareTo(date) <= 0) { //如果该文件的日期小于指定日期
					readTxt(log_path + fileName);
				}
			}
		}
		
		
		/*
		 * 读取文件内容
		 * @param filePath
		 */
	    public void readTxt(String filePath) {
	        try {
	            BufferedReader bfr = new BufferedReader(new InputStreamReader(
	            		new FileInputStream(new File(filePath)), "UTF-8"));
	            String lineTxt = null;
	            
	            while ((lineTxt = bfr.readLine()) != null) { //按行读取文本内容
	            	if(!lineTxt.startsWith("//")) //遇到“//”不读取
	            		textProcessing(lineTxt);
	            }
	            bfr.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    /*
	     * 文本处理
	     * @param string
	     */
	    public void textProcessing(String string) {
	    	String pattern1 = "(\\S+) 新增 感染患者 (\\d+)人";
	    	String pattern2 = "(\\S+) 新增 疑似患者 (\\d+)人";
	    	String pattern3 = "(\\S+) 治愈 (\\d+)人";
	    	String pattern4 = "(\\S+) 死亡 (\\d+)人";
	    	String pattern5 = "(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
	    	String pattern6 = "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
	    	String pattern7 = "(\\S+) 疑似患者 确诊感染 (\\d+)人";
	    	String pattern8 = "(\\S+) 排除 疑似患者 (\\d+)人";
	    	boolean isMatch1 = Pattern.matches(pattern1, string);
	    	boolean isMatch2 = Pattern.matches(pattern2, string);
	    	boolean isMatch3 = Pattern.matches(pattern3, string);
	    	boolean isMatch4 = Pattern.matches(pattern4, string);
	    	boolean isMatch5 = Pattern.matches(pattern5, string);
	    	boolean isMatch6 = Pattern.matches(pattern6, string);
	    	boolean isMatch7 = Pattern.matches(pattern7, string);
	    	boolean isMatch8 = Pattern.matches(pattern8, string);
	    	
	    	if(isMatch1) //新增 感染患者处理
	    		addIP(string);
	    	else if(isMatch2) //新增 疑似患者处理
	    		addSP(string);
	    	else if(isMatch3) //新增 治愈患者处理
	    		addCure(string);
	    	else if(isMatch4) //新增 死亡患者处理
	    		addDead(string);
	    	else if(isMatch5) //感染患者 流入处理
	    		flowIP(string);
	    	else if(isMatch6) //疑似患者 流入处理
	    		flowSP(string);
	    	else if(isMatch7) //疑似患者 确诊感染处理
	    		diagnosisSP(string);
	    	else if(isMatch8) //排除 疑似患者处理
	    		removeSP(string);
	    }
	    
	    /*
	     * 新增 感染患者处理
	     * @param string
	     */
	    public void addIP(String string) {
	    	String[] str_arr = string.split(" "); //将字符串以空格分割为多个字符串
	    	int i;
	    	int n = Integer.valueOf(str_arr[3].replace("人", ""));//将人前的数字从字符串类型转化为int类型
	    	
	    	for(i = 0; i < province_str.length; i++) {
	    		if(str_arr[0].equals(province_str[i])) { //第一个字符串为省份
	    			people[0][0] += n; //全国感染患者人数增加
	    			people[i][0] += n; //该省份感染患者人数增加
	    			if(province[0] == -1) //省份处于未指定状态
	    				province[i] = 1; //需要将该省份列出
	    			break;
	    		}
	    	}
	    }
	    
	    /*
	     * 新增 疑似患者处理
	     * @param string
	     */
	    public void addSP(String string) {
	    	String[] str_arr = string.split(" "); //将字符串以空格分割为多个字符串
	    	int i;
	    	int n = Integer.valueOf(str_arr[3].replace("人", ""));//将人前的数字从字符串类型转化为int类型
	    	
	    	for(i = 0; i < province_str.length; i++) {
	    		if(str_arr[0].equals(province_str[i])) { //第一个字符串为省份
	    			people[0][1] += n; //全国疑似患者人数增加
	    			people[i][1] += n; //该省份疑似患者人数增加
	    			if(province[0] == -1) //省份处于未指定状态
	    				province[i] = 1; //需要将该省份列出
	    			break;
	    		}
	    	}
	    }
	    
	    /*
	     * 新增 治愈患者处理
	     * @param string
	     */
	    public void addCure(String string) {
	    	String[] str_arr = string.split(" "); //将字符串以空格分割为多个字符串
	    	int i;
	    	int n = Integer.valueOf(str_arr[2].replace("人", ""));//将人前的数字从字符串类型转化为int类型
	    	
	    	for(i = 0; i < province_str.length; i++) { 
	    		if(str_arr[0].equals(province_str[i])) { //第一个字符串为省份
	    			people[0][2] += n; //全国治愈人数增加
	    			people[0][0] -= n; //全国感染患者人数减少
	    			people[i][2] += n; //该省份治愈人数增加
	    			people[i][0] -= n; //该省份感染患者人数减少
	    			if(province[0] == -1) //省份处于未指定状态
	    				province[i] = 1; //需要将该省份列出
	    			break;
	    		}
	    	}
	    }
	    
	    /*
	     * 新增 死亡患者处理
	     * @param string
	     */
	    public void addDead(String string) {
	    	String[] str_arr = string.split(" "); //将字符串以空格分割为多个字符串
	    	int i;
	    	int n = Integer.valueOf(str_arr[2].replace("人", ""));//将人前的数字从字符串类型转化为int类型
	    	
	    	for(i = 0; i < province_str.length; i++) {
	    		if(str_arr[0].equals(province_str[i])) { //第一个字符串为省份
	    			people[0][3] += n; //全国死亡人数增加
	    			people[0][0] -= n; //全国感染患者人数减少
	    			people[i][3] += n; //该省份死亡人数增加
	    			people[i][0] -= n; //该省份感染患者人数减少
	    			if(province[0] == -1) //省份处于未指定状态
	    				province[i] = 1; //需要将该省份列出
	    			break;
	    		}
	    	}
	    }
	    
	    /*
	     * 感染患者 流入处理
	     * @param string
	     */
	    public void flowIP(String string) {
	    	String[] str_arr = string.split(" "); //将字符串以空格分割为多个字符串
	    	int i;
	    	int n = Integer.valueOf(str_arr[4].replace("人", ""));//将人前的数字从字符串类型转化为int类型
	    	
	    	for(i = 0; i < province_str.length; i++) {
	    		if(str_arr[0].equals(province_str[i])) { //第一个字符串为流出省份
	    			people[i][0] -= n; //该省份感染患者人数减少
	    			if(province[0] == -1) //省份处于未指定状态
	    				province[i] = 1; //需要将该省份列出
	    		}
	    		if(str_arr[3].equals(province_str[i])) { //第四个字符串为流入省份
	    			people[i][0] += n; //该省份感染患者人数增加
	    			if(province[0] == -1) //省份处于未指定状态
	    				province[i] = 1; //需要将该省份列出
	    		}
	    	}
	    }
	    
	    /*
	     * 疑似患者 流入处理
	     * @param string
	     */
	    public void flowSP(String string) {
	    	String[] str_arr = string.split(" "); //将字符串以空格分割为多个字符串
	    	int i;
	    	int n = Integer.valueOf(str_arr[4].replace("人", ""));//将人前的数字从字符串类型转化为int类型
	    	
	    	for(i = 0; i < province_str.length; i++) {
	    		if(str_arr[0].equals(province_str[i])) { //第一个字符串为流出省份
	    			people[i][1] -= n; //该省份疑似患者减少
	    			if(province[0] == -1) //省份处于未指定状态
	    				province[i] = 1; //需要将该省份列出
	    		}
	    		if(str_arr[3].equals(province_str[i])) { //第四个字符串为流入省份
	    			people[i][1] += n; //该省份疑似患者增加
	    			if(province[0] == -1) //省份处于未指定状态
	    				province[i] = 1; //需要将该省份列出
	    		}
	    	}
	    	
	    }
	    
	    /*
	     * 疑似患者 确诊感染处理
	     * @param string
	     */
	    public void diagnosisSP(String string) {
	    	String[] str_arr = string.split(" "); //将字符串以空格分割为多个字符串
	    	int i;
	    	int n = Integer.valueOf(str_arr[3].replace("人", ""));//将人前的数字从字符串类型转化为int类型
	    	
	    	for(i = 0; i < province_str.length; i++) {
	    		if(str_arr[0].equals(province_str[i])) { //第一个字符串为省份
	    			people[0][1] -= n; //全国疑似患者人数减少
	    			people[0][0] += n; //全国感染患者人数增加
	    			people[i][1] -= n; //该省份疑似患者人数减少
	    			people[i][0] += n; //该省份感染患者人数增加
	    			if(province[0] == -1) //省份处于未指定状态
	    				province[i] = 1; //需要将该省份列出
	    			break;
	    		}
	    	}
	    }
	    
	    /*
	     * 排除 疑似患者处理
	     * @param string
	     */
	    public void removeSP(String string) {
	    	String[] str_arr = string.split(" "); //将字符串以空格分割为多个字符串
	    	int i;
	    	int n = Integer.valueOf(str_arr[3].replace("人", ""));//将人前的数字从字符串类型转化为int类型
	    	
	    	for(i = 0; i < province_str.length; i++) {
	    		if(str_arr[0].equals(province_str[i])) { //第一个字符串为省份
	    			people[i][1] -= n; //该省份疑似患者人数减少
	    			people[0][1] -= n; //全国疑似患者人数减少
	    			if(province[0] == -1) //省份处于未指定状态
	    				province[i] = 1; //需要将该省份列出
	    			break;
	    		}
	    	}
	    }
	    
	    
	    /*
		 * 输出文件内容
		 */
	    public void writeTxt() {
	    	FileWriter fwriter = null;
	    	int i, j, k;
	        try {
	        	fwriter = new FileWriter(out_path);
	        	if(province[0] == -1) //省份处于未指定状态
					province[0] = 1; //将全国以及省份改为指定状态（即列出日志文件中出现的省份）
	        	for(i = 0; i < province_str.length; i++) { //遍历省份查找需要列出的省份
	        		if(province[i] == 1) { //该省份需要列出
	        			fwriter.write(province_str[i] + " ");
	        			for(j = 0; j < type.length; j++) {
	        				for(k = 0; k < type.length; k++) {
	        					if(type[k] == j+1) { //即该省份需要列出且按照参数顺序
	        						fwriter.write(type_str[k] + people[i][k] + "人 ");
	        						break;
	        					}
	        				}
	        			}
	        			fwriter.write("\n");
	        		}
	        	}
	        	fwriter.write("// 该文档并非真实数据，仅供测试使用");
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                fwriter.flush();
	                fwriter.close();
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            }
	        }
	    }
	}
	
    public static void main(String[] args) {
    	if (args.length == 0) {  
            System.out.println("命令行格式有误——未输入参数");  
            return;
        }
    	InfectStatistic infectStatistic = new InfectStatistic();
    	InfectStatistic.CmdArgs cmdargs = infectStatistic.new CmdArgs(args);
    	boolean b = cmdargs.extractCmd();
    	if(b == false) {
    		return;
    	}
    	InfectStatistic.FileHandle filehandle = infectStatistic.new FileHandle();
    	filehandle.getFileList();
    	filehandle.writeTxt();
    }
}