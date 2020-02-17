import java.io.IOException;
import java.util.*;
import java.io.File;
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
	public static String []province=new String[40];
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
    	for(int i=0;i<40;i++) province[i]=$nothing;
    	
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
	public void Init() {
		//填入省份信息
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
	public static ArrayList<String> getFiles(String path,String date){
		ArrayList<String> files = new ArrayList<String>();
	    File file = new File(path);
	    File[] tempList = file.listFiles();

	    for (int i = 0; i < tempList.length; i++) {
	        if (tempList[i].isFile()) {
//	              System.out.println("文     件：" + tempList[i]);
	            files.add(tempList[i].toString());
	        }
	        if (tempList[i].isDirectory()) {
//	              System.out.println("文件夹：" + tempList[i]);
	        }
	        //得到当前日期之前的文件名
	        File tempFile=new File(files.get(i).trim());
	        String fileName=tempFile.getName().substring(0,10);
	        if(date.compareTo(fileName)>=0){
		        System.out.println(fileName);
	        }
	    }
	    return files;
	}
	public void ReadAll(String log,String out,String date,String [] type,String [] province) throws IOException{
		System.out.println(log);
		System.out.println(out);
		System.out.println(date);
		for(int i=0;i<4;i++) {
			System.out.println(type[i]);
		}
		for(int i=0;i<40;i++) {
			System.out.println(province[i]);
		}
		ArrayList<String> teArrayList=getFiles(log,date);
		for(int i=0;i<teArrayList.size();i++) {
			System.out.println(teArrayList.get(i));
		}
	}
	
}