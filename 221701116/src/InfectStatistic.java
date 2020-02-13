/**
 * InfectStatistic
 * TODO
 *
 * @author 221701116
 * @version 1.0
 * @since 2020-02-06
 */
class InfectStatistic {
    public static void main(String[] args) {
    	if (args.length == 0) {  
            System.out.println("命令行格式有误");  
            return;
        }
    	CmdArgs cmdargs = new CmdArgs(args);
    	boolean b = cmdargs.extractCmd();
    	if(b == false) {
    		System.out.println("命令行格式有误");
    		return;
    	}
    	FileHandle filehandle = new FileHandle(cmdargs.log_path,
    			cmdargs.out_path,cmdargs.date,cmdargs.type,cmdargs.province);
    	filehandle.getFileList();
    	filehandle.writeTxt();
    }
}


/*
 * 解析命令行参数
 */
class CmdArgs{
	//int i; //将args的下表位置定义为全局变量，便于传递
	String[] args; //保存传入的命令行

	public String log_path; //日志文件位置
	public String out_path; //输出文件位置
	public String date; //指定日期

	/*
	 * 指定类型(按顺序分别为ip,sp,cure,dead)
	 * 当数值为1时表示需要列出，为0时无需列出
	 */
	public int[] type = {1,1,1,1};

	/*
	 * 指定省份(按省份名称排序（第一位为全国）)
	 */
	public int[] province = new int[35];

	CmdArgs(String[] args_str){
		args = args_str;
	}

	/*
	 * 提取命令行中的参数
	 * @return
	 */
	public boolean extractCmd() {
		if(!args[0].equals("list")) //判断命令格式开头是否正确
			return false;

		int i;

		for(i = 1; i < args.length; i++) {
			if(args[i].equals("-log"))
				i = getLogPath(++i);
			else if(args[i].equals("-out"))
				i= getOutPath(++i);
			else if(args[i].equals("-date")) 
				i= getDate(++i);
			else if(args[i].equals("-type"))
				i = getType(++i); 
			else if(args[i].equals("-province"))
				i = getProvince(++i);

			if(i == -1) //说明上述步骤中发现命令行出错
				return false;
		}
		return true;
	}

	/*
	 * 得到日志文件位置
	 * @param i
	 * @return
	 */
	public int getLogPath(int i) {
		if(i < args.length)
		{
			log_path = args[i];
		}
		else
			return -1;
		return i;
	}

	/*
	 * 得到日志文件位置
	 * @param i
	 * @return
	 */
	public int getOutPath(int i) {
		if(i < args.length)
		{
			out_path = args[i];
		}
		else
			return -1;
		return i;
	}

	/*
	 * 得到指定日期
	 * @param i
	 * @return
	 */
	public int getDate(int i) {
		if(i < args.length)
		{
			date = args[i];
		}
		else
			return -1;
		return i;
	}

	/*
	 * 得到指定类型(type参数可能有多个)
	 * @param i
	 * @return int
	 */
	public int getType(int i) {
		int j = i, m = i;

		if(i < args.length)
		{
			for(j = 0; j < 4; j++)
				type[j] = 0;
			while(i<args.length) {
				switch(args[i]) {
					case "ip" :
						type[0] = 1;
						j = i++;
						break;
					case "sp":
						type[1] = 1;
						j = i++;
						break;
					case "cure":
						type[2] = 1;
						j = i++;
						break;
					case "dead":
						type[3] = 1;
						j = i++;
						break;
					default:
						break;
				}
			}
		}
		if(m == i) //说明-type后无正确参数
			return -1;
		return (j - 1); //接下来不为-type的参数，或越界
	}

	/*
	 * 得到指定省份(province参数可能有多个)
	 * @param i
	 * @return int
	 */
	public int getProvince(int i) {
		int j, m = i;

		if(i < args.length)
		{
			//指定省份
		}
		if(m == i) //说明-province后无正确参数
			return -1;
		return i;
	}
}

/*
 * 处理文件（读取目录下的文件，打开文件，读取内容，输出内容到文件）
 */
class FileHandle{
	public String log_path; //日志文件位置
	public String out_path; //输出文件位置
	public String date; //指定日期

	/*
	 * 指定类型(按顺序分别为ip,sp,cure,dead)
	 * 当数值为1时表示需要列出，为0时无需列出
	 */
	public int[] type = {1,1,1,1};

	/*
	 * 指定省份(按省份名称拼音排序（第一位为全国）)
	 * 当数值为1时表示需要列出，为0时无需列出
	 */
	public int[] province = new int[35];
	public String[] fileName = new String[9]; //存储指定日期下的文件名

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

	FileHandle(String log, String out, String d, int[] t, int[] p) {
		log_path = log;
		out_path = out;
		date = d + ".log.txt"; //日志文件名后有.log.txt，在此加上便于比较
		type = t;
		province = p;
	}

	/*
	 * 读取指定路径下的文件名和目录名
	 */
	public void getFileList() {
		File file = new File(log_path);
		File[] fileList = file.listFiles();
		String fileName;

		for (int i = 0; i < fileList.length; i++) {
			fileName = fileList[i].getName();
			if (fileName.compareTo(date) <= 0) {
				readTxt(log_path + "\\" + fileName);
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

            while ((lineTxt = bfr.readLine()) != null) {
            	if(!lineTxt.startsWith("//"))
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

    	if(isMatch1) { //新增 感染患者处理
    		addIP(string);
    	} else if(isMatch2){ //新增 疑似患者处理
    		addSP(string);
    	} else if(isMatch3){ //新增 治愈患者处理
    		addCure(string);
    	} else if(isMatch4){ //新增 死亡患者处理
    		addDead(string);
    	} else if(isMatch5){ //感染患者 流入处理
    		flowIP(string);
    	} else if(isMatch6){ //疑似患者 流入处理
    		flowSP(string);
    	} else if(isMatch7){ //疑似患者 确诊感染处理
    		diagnosisSP(string);
    	} else if(isMatch8){ //排除 疑似患者处理
    		removeSP(string);
    	}
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
    		if(str_arr[0].equals(province_str[i])) //第一个字符串为流出省份
    			people[i][0] -= n; //该省份感染患者人数减少
    		if(str_arr[3].equals(province_str[i])) //第四个字符串为流入省份
    			people[i][0] += n; //该省份感染患者人数增加
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
    		if(str_arr[0].equals(province_str[i])) //第一个字符串为流出省份
    			people[i][1] -= n; //该省份疑似患者减少
    		if(str_arr[3].equals(province_str[i])) //第四个字符串为流入省份
    			people[i][1] += n; //该省份疑似患者增加
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
    			break;
    		}
    	}
    }


    /*
	 * 输出文件内容
	 */
    public void writeTxt() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 