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
class InfectStatistic {  //主类
    public static void main(String[] args) {
       
    }
}

class  CommandLine {  //命令行类，主要进行命令行结构的参数设置，然后在内部类CmdArgs(命令行解析类)中进行解析命令行参数
	
	String logPath;  //日志文件所在地址
	String outPath;  //输出文件所在地址
	
	//因为-date不设置则默认为所提供日志最新的一天，提前设置时间为当前时间
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	Date d = new Date(System.currentTimeMillis());
	public String date = formatter.format(d);//当前时间设置
	
	//因为-type不指定默认会列出所有情况，所以创建数组
	int[] type = {1,2,3,4};
	public String[] typeStr = {"感染患者", "疑似患者", "治愈", "死亡"};
	
	//设置-province下的信息
	public int[] province = new int[35];  //省份是否输出的判断数组，将值为1的省份输出
	public String[] provinceStr = {"全国", "安徽", "澳门" ,"北京", "重庆", "福建","甘肃","广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", 
			"吉林","江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海","四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江"};
	//全国以及省份顺序排列
	
	public int[][] peopleNumber = new int [35][4];  //记录全国以及每个省份每个类型的人数，初始为0，按照上面顺序排列
	
	
	
 }


