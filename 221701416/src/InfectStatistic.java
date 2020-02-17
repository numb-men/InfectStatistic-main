import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
/**
 * InfectStatistic
 *
 * @author Hanani
 * @version v1.0.0
 * @since xxx
 */
class InfectStatistic {
	public static String log=new String();
	public static String out=new String();
	public static String date=new String("0000-00-00");
	public static String []type= new String[4];
	public static String []province=new String[31];
    public static void main(String[] args) throws IOException {
    	CoronavirusDetail CD=new CoronavirusDetail();
    	CD.Init();
    	AnalysisCommand(args);
    	CD.ReadAll(log,out,date,type,province);
    }
    //对输入的命令进行处理分解
    public static void AnalysisCommand(String[] command) {
    	int len=command.length;
    	String $list= new String("list");
    	String $log= new String("-log");
    	String $out= new String("-out");
    	String $date= new String("-date");
    	String $type= new String("-type");
    	String $province= new String("-province");
    	String $nothing=new String("nothing");
    	//初始化type和province
    	for(int i=0;i<4;i++) type[i]=$nothing;
    	for(int i=0;i<31;i++) province[i]=$nothing;
    	
    	//list检查
		if(!(command[0].equals($list))) {
			System.out.println("请使用list命令进行操作");
			System.exit(0);
		}
		//获取log目录
		for(int i=1;i<len;i++) {
			if(command[i].equals($log)) {
				log=command[i+1];
				break;
			}
		}
		//获取out目录
		for(int i=1;i<len;i++) {
			if(command[i].equals($out)) {
				out=command[i+1];
				break;
			}
		}
		//获取date数值
		for(int i=1;i<len;i++) {
			if(command[i].equals($date)) {
				date=command[i+1];
				break;
			}
		}
		//获取type的参数
		int typepos=0;
		for(int i=1;i<len;i++) {
			if(command[i].equals($type)) {
				typepos=i;
				break;
			}
		}
		int typeindex=0;
		for(int i=typepos+1;i<len;i++) {
			if(command[i].charAt(0)!='-') {
				type[typeindex++]=command[i];
			}else break;
		}
		//获取province的参数
		int provincepos=0;
		for(int i=1;i<len;i++) {
			if(command[i].equals($province)) {
				provincepos=i;
				break;
			}
		}
		int provinceindex=0;
		for(int i=provincepos+1;i<len;i++) {
			if(command[i].charAt(0)!='-') {
				province[provinceindex++]=command[i];
			}else break;
		}
    }
}
/**
 * CoronavirusDetail
 *
 * @author Hanani
 * @version v1.0.0
 * @since xxx
 */
class CoronavirusDetail{
	Map ProvinceMap= new HashMap();	
	//初始化省份信息
	public void Init() {
		//填入省份信息31个
		String[] ProvinceStr= {
				"安徽"  ,"北京"  ,"重庆"  ,"福建"  ,"甘肃"  ,
				"广东"  ,"广西"  ,"贵州"  ,"海南"  ,"河北"  ,
				"河南"  ,"黑龙江","湖北"  ,"湖南"  ,"吉林"  ,
				"江苏"  ,"江西"  ,"辽宁"  ,"内蒙古","宁夏"  ,
				"青海"  ,"山东"  ,"山西"  ,"陕西"  ,"上海"  ,
				"四川"  ,"天津"  ,"西藏"  ,"新疆"  ,"云南"  ,"浙江"  
		};
		//将省份注入Map中方便后面使用
		for(int i=0;i<ProvinceStr.length;i++) {
			ProvinceMap.put(ProvinceStr[i], Integer.valueOf(i));
		}
	}
	
	//获取指定日期之前的文件名
	public static ArrayList<String> getFilesName(String path,String date){
		ArrayList<String> files = new ArrayList<String>();
		ArrayList<String> beforefiles = new ArrayList<String>();
	    File file = new File(path);
	    File[] tempList = file.listFiles();

	    for (int i = 0; i < tempList.length; i++) {
	        if (tempList[i].isFile()) {
//	              System.out.println("文 件：" + tempList[i]);
	            files.add(tempList[i].toString());
	        }
	        //得到当前日期之前的文件名
	        File tempFile=new File(files.get(i).trim());
	        String fileName=tempFile.getName().substring(0,10);
	        if(date.compareTo(fileName)==0){
	        	beforefiles.add(files.get(i));
	        }
	    }
	    return beforefiles;
	}
	
	//读取所有指定日期之前的文件
	public void ReadAll(String log,String out,String date,String [] type,String [] province) throws IOException{
		//System.out.println(log);
		//System.out.println(out);
		//System.out.println(date);
		//设定正则表达式规则
		String txtString="福建 感染患者 流入 湖北 5人";
		String matString_1="(\\S+) 新增 感染患者 (\\d+)人";
		String splitString_1=" 新增 感染患者 |人";
		String matString_2="(\\S+) 新增 感染患者 (\\d+)人";
		String splitString_2=" 新增 感染患者  |人";
		String matString_3="(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
		String splitString_3=" 感染患者 流入 | |人";
		String matString_4="(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
		String splitString_4=" 疑似患者 流入 | |人";
		String matString_5="(\\S+) 死亡  (\\d+)人";
		String splitString_5=" 死亡  |人";
		String matString_6="(\\S+) 治愈  (\\d+)人";
		String splitString_6=" 治愈  |人";
		String matString_7="(\\S+) 疑似患者 确诊感染 (\\d+)人";
		String splitString_7=" 疑似患者 确诊感染  |人";
		String matString_8="(\\S+) 排除 疑似患者 (\\d+)人";
		String splitString_8=" 排除 疑似患者  |人";
		
		System.out.println(txtString.matches(matString_3));
		String [] reStrings=txtString.split(splitString_3);
		for(int i=0;i<reStrings.length;i++) {
			System.out.println(reStrings[i]);
		}
		
		for(int i=0;i<4;i++) {
			//System.out.println(type[i]);
		}
		for(int i=0;i<31;i++) {
			//System.out.println(province[i]);
		}
		
		//得到需要的文件路径并将其读入
		ArrayList<String> teArrayList=getFilesName(log,date);
		for(int i=0;i<teArrayList.size();i++) {
			System.out.println(teArrayList.get(i));
			BufferedReader inBufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(teArrayList.get(i)), "UTF-8"));
			String nowString;
			while((nowString=inBufferedReader.readLine())!=null) {
				
				String eachstring[]=nowString.split(" ");
				for(String one:eachstring) {
				//	System.out.println(one);
				}
				//System.out.println("+++++");
			}
			//System.out.println(nowString);
			//System.out.println("       ");
		}
	}
}