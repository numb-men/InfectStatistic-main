import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */

class InfectStatistic { 

//主类，有内部类CommandLine(命令行类)，FileDispose(文件处理类)
	
	public static  void main(String[] args) {  //主函数入口，进行命令行参数的解析
		String[] argsStr=args;  //将命令行传递到字符串函数
		isCurrentArgs(argsStr);
	}		
	
	public static boolean isCurrentArgs(String[] args) {  //判断该命令行是否为有效的命令行并且解析出每个部分相应的参数
		if(!args[0].equals("list")) {  //判断命令行开头是否为list，如果不是则输出命令行有误
		        System.out.println("命令行有误");
		        return false;
		    }
		for (int i=1; i<args.length; i++){  //将各部分的内容一一判断并且解析参数,解析有误则停止解析并输出命令行有误
			if(args[i].equals("-log")){  //因为-log为必须附带
				if(i==-1) {
					
				}
			}
		}
		return true;
	}


	
	
	
	public int getDate(int i){//获得指定日期
		return i;  
		
	}
	
	
	public int getLogPath(int i){//获得日志文件所在地址
		return i;  
		
	}
	
	public int getOutPath(int i){//获得输出文件所在地址
		return i;  
		
		
	}
	
	
	
    public int getType(int i){//获得指定类型
		return i;  
		
	}
    
    public int getProvince(int i){//获得指定省份
		return i;  
		
	}
	
	
	class  CommandLine {  //命令行类，主要进行命令行结构的参数设置
		
		CommandLine() {}  //空构造函数
		
		String logPath;  //日志文件所在地址
		String outPath;  //输出文件所在地址
		
		//因为-date不设置则默认为所提供日志最新的一天，提前设置时间为当前时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
		Date d = new Date(System.currentTimeMillis());
		public String date = formatter.format(d);//当前时间设置
		
		//因为-type不指定默认会列出所有情况，所以创建数组来表示类型的情况
		int[] type = {1,2,3,4};  //类型的数组，应用于获得类型方法
		public String[] typeStr = {"感染患者", "疑似患者", "治愈", "死亡"};  //对应于类型数组的类型顺序排列
		
		//设置-province下的信息
		public int[] province = new int[35];  //省份是否输出的判断数组，将值为1的省份输出
		public String[] provinceStr = {"全国", "安徽", "澳门" ,"北京", "重庆", "福建","甘肃","广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", 
				"吉林","江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海","四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江"};
		//全国以及省份顺序排列
		
		public int[][] peopleNumber = new int [35][4];  //记录全国以及每个省份每个类型的人数，初始默认为0，按照上面类型和省份顺序排列
		
	 }
	
	class FileDispose{  //文件处理类，用来输入日志文件，通过解析后的命令行参数对输入的日志进行处理，然后输出到输出文件
		FileDispose(){
		};  //空构造函数
		
	}
	
	


	}




