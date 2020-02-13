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

    }
}


/*
 * 解析命令行参数
 */
class CmdArgs{
	//int i; //将args的下表位置定义为全局变量，便于传递
	String[] args; //保存传入的命令行

	String log_path; //日志文件位置
	String out_path; //输出文件位置
	String date; //指定日期

	/*
	 * 指定类型(按顺序分别为ip,sp,cure,dead)
	 * 当数值为1时表示需要列出，为0时无需列出
	 */
	int[] type = {1,1,1,1};

	/*
	 * 指定省份(按省份名称排序（第一位为全国）)
	 */
	int province[] = new int[35];

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
