import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Date;
import java.text.SimpleDateFormat;
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
	static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
	static String tttString=df.format(new Date());
	public static String date=new String(tttString);
	public static String []type= new String[50];
	public static String []province=new String[50];
	
    public static void main(String[] args) throws IOException {
    	CoronavirusDetail CD=new CoronavirusDetail();
    	CD.Init();
    	AnalysisCommand(args);
    	CD.ReadAll(log,out,date,type,province);
    	CD.Printdetail(log,out,date,type,province);
    }
    
    static String $list= new String("list");
	static String $log= new String("-log");
	static String $out= new String("-out");
	static String $date= new String("-date");
	static String $type= new String("-type");
	static String $province= new String("-province");
	static String $nothing=new String("nothing");
    //单命令匹配
    public static boolean match(String th,String tar) {
    	if(tar.equals(th)) 
    		return true;
    	return false;
    }
    //双命令匹配
    public static String matchtot(String []th,String tar,int len,String ori) {
    	for(int i=1;i<len;i++) {
    		if(th[i].equals(tar)) {
    			return th[i+1];
    		}
    	}
    	return ori;
    }
    //多命令匹配
    public static String[] matchmuch(String []th,String tar,int len,String[] ori) {
    	String []now=new String[50];
    	for(int i=0;i<50;i++) now[i]=$nothing;
    	int pos=0;
		for(int i=1;i<len;i++) {
			if(th[i].equals(tar)) {
				pos=i;
				break;
			}
		}
		int index=0;
		for(int i=pos+1;i<len;i++) {
			if(th[i].charAt(0)!='-') {
				now[index++]=th[i];
			}else break;
		}
		if(index!=0) return now;
		else return ori;
    }
    //对输入的命令进行处理分解
    public static void AnalysisCommand(String[] command) {
    	int len=command.length;
    	//初始化type和province
    	for(int i=0;i<50;i++) type[i]=$nothing;
    	for(int i=0;i<50;i++) province[i]=$nothing;
    	
    	//list检查
		if(!(match(command[0],$list))) {
			System.out.println("请使用list命令进行操作");
			System.exit(0);
		}
		//获取log目录,out目录,date数值
		log=matchtot(command,$log,len,log);
		out=matchtot(command,$out,len,out);
		date=matchtot(command,$date,len,date);
		//获取type的参数，province的参数
		type=matchmuch(command,$type,len,type);
		province=matchmuch(command,$province,len,province);
		
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
	String $nothing=new String("nothing");
	Map ProvinceMap= new HashMap();	
	Map TypeMap=new HashMap();
	String[] ProvinceStr= {
			"全国"  ,"安徽"  ,"北京"  ,"重庆"  ,"福建"  ,"甘肃"  ,
			"广东"  ,"广西"  ,"贵州"  ,"海南"  ,"河北"  ,
			"河南"  ,"黑龙江","湖北"  ,"湖南"  ,"吉林"  ,
			"江苏"  ,"江西"  ,"辽宁"  ,"内蒙古","宁夏"  ,
			"青海"  ,"山东"  ,"山西"  ,"陕西"  ,"上海"  ,
			"四川"  ,"天津"  ,"西藏"  ,"新疆"  ,"云南"  ,"浙江" 
	};
	String[] TypeStr={
		"ip" , "sp", "curu", "dead"
	};
	String[] TypeStrCn={
			"感染患者" , "疑似患者", "治愈", "死亡"
		};
	int provincenum=32;
	int detailnum=4;
	public int [][] detail=new int[provincenum][detailnum];
	//初始化省份信息
	public void Init() {
		//填入省份信息31个
		//将省份注入Map中方便后面使用
		for(int i=0;i<ProvinceStr.length;i++) {
			ProvinceMap.put(ProvinceStr[i], Integer.valueOf(i));
		}
		
		for(int i=0;i<TypeStr.length;i++) {
			TypeMap.put(TypeStr[i],Integer.valueOf(i));
		}
		
		for(int i=0;i<provincenum;i++)
			for(int j=0;j<detailnum;j++) detail[i][j]=0;
	}
	
	//输出所需信息
    public void Printdetail(String log,String out,String date,String [] type,String [] province) {
    	for(int i=1;i<32;i++) 
    		for(int j=0;j<4;j++) detail[0][j]+=detail[i][j];
    	for(int i=0;i<ProvinceStr.length;i++) {
        	for(int j=0;j<province.length;j++) if(ProvinceStr[i].equals(province[j])){
        		System.out.print(ProvinceStr[i]+" ");
        		Integer pronum=(Integer) ProvinceMap.get(ProvinceStr[i]);
        		//System.out.print(pronum);
        		int typecnt=0;
        		for(int k=0;k<4;k++) {
        			//System.out.println(type[k]);
        			if(type[k].equals($nothing)) typecnt++;
        		}
        		for(int k=0;k<4;k++) if((typecnt==4)||(!type[k].equals($nothing))){
        			//System.out.println((Integer) TypeMap.get(type[j]));
        			System.out.print(TypeStrCn[(Integer) TypeMap.get(type[k])]+detail[pronum][(Integer) TypeMap.get(type[k])]+" ");
        		}
        		System.out.println();
        	}
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
	        if(date.compareTo(fileName)>=0){
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
		String matString_1="(\\S+) 新增 感染患者 (\\d+)人";
		String splitString_1=" 新增 感染患者 |人";
		
		String matString_2="(\\S+) 新增 疑似患者 (\\d+)人";
		String splitString_2=" 新增 疑似患者 |人";
		
		String matString_3="(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
		String splitString_3=" 感染患者 流入 | |人";
		
		String matString_4="(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
		String splitString_4=" 疑似患者 流入 | |人";
		
		String matString_5="(\\S+) 死亡 (\\d+)人";
		String splitString_5=" 死亡 |人";
		
		String matString_6="(\\S+) 治愈 (\\d+)人";
		String splitString_6=" 治愈 |人";
		
		String matString_7="(\\S+) 疑似患者 确诊感染 (\\d+)人";
		String splitString_7=" 疑似患者 确诊感染 |人";
		
		String matString_8="(\\S+) 排除 疑似患者 (\\d+)人";
		String splitString_8=" 排除 疑似患者 |人";
		
//		System.out.println(txtString.matches(matString_3));
//		String [] reStrings=txtString.split(splitString_3);
//		for(int i=0;i<reStrings.length;i++) {
//			System.out.println(reStrings[i]);
//		}
		
		for(int i=0;i<4;i++) {
			//System.out.println(type[i]);
		}
		for(int i=0;i<31;i++) {
			//System.out.println(province[i]);
		}
		
		//得到需要的文件路径并将其读入
		ArrayList<String> teArrayList=getFilesName(log,date);
		for(int i=0;i<teArrayList.size();i++) {
			//System.out.println(teArrayList.get(i));

			BufferedReader inBufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(teArrayList.get(i)), "UTF-8"));
			String nowString;
			while((nowString=inBufferedReader.readLine())!=null) {
				//System.out.print(nowString);
				//System.out.println("???");
				//<省> 新增 感染患者 n人
				if(nowString.matches(matString_1)) {
					String [] Strings_1=nowString.split(splitString_1);
					detail[(Integer) ProvinceMap.get(Strings_1[0])][0]+=Integer.parseInt(Strings_1[1]);
				}
				//<省> 新增 疑似患者 n人
				else if(nowString.matches(matString_2)) {
					String [] Strings_2=nowString.split(splitString_2);
					detail[(Integer) ProvinceMap.get(Strings_2[0])][1]+=Integer.parseInt(Strings_2[1]);
				}
				//<省1> 感染患者 流入 <省2> n人
				else if(nowString.matches(matString_3)) {
					String [] Strings_3=nowString.split(splitString_3);
					detail[(Integer) ProvinceMap.get(Strings_3[0])][0]-=Integer.parseInt(Strings_3[2]);
					detail[(Integer) ProvinceMap.get(Strings_3[1])][0]+=Integer.parseInt(Strings_3[2]);
				}
				//<省1> 疑似患者 流入 <省2> n人
				else if(nowString.matches(matString_4)) {
					String [] Strings_4=nowString.split(splitString_4);
					detail[(Integer) ProvinceMap.get(Strings_4[0])][1]-=Integer.parseInt(Strings_4[2]);
					detail[(Integer) ProvinceMap.get(Strings_4[1])][1]+=Integer.parseInt(Strings_4[2]);
				}
				//<省> 死亡 n人
				else if(nowString.matches(matString_5)) {
					String [] Strings_5=nowString.split(splitString_5);
					detail[(Integer) ProvinceMap.get(Strings_5[0])][3]+=Integer.parseInt(Strings_5[1]);
					detail[(Integer) ProvinceMap.get(Strings_5[0])][0]-=Integer.parseInt(Strings_5[1]);
				}
				//<省> 治愈 n人
				else if(nowString.matches(matString_6)) {
					String [] Strings_6=nowString.split(splitString_6);
					detail[(Integer) ProvinceMap.get(Strings_6[0])][2]+=Integer.parseInt(Strings_6[1]);
					detail[(Integer) ProvinceMap.get(Strings_6[0])][0]-=Integer.parseInt(Strings_6[1]);
				}
				//<省> 疑似患者 确诊感染 n人
				else if(nowString.matches(matString_7)) {
					String [] Strings_7=nowString.split(splitString_7);
					detail[(Integer) ProvinceMap.get(Strings_7[0])][0]+=Integer.parseInt(Strings_7[1]);
					detail[(Integer) ProvinceMap.get(Strings_7[0])][1]-=Integer.parseInt(Strings_7[1]);
				}
				//<省> 排除 疑似患者 n人
				else if(nowString.matches(matString_8)) {
					String [] Strings_8=nowString.split(splitString_8);
					detail[(Integer) ProvinceMap.get(Strings_8[0])][1]-=Integer.parseInt(Strings_8[1]);
				}
				//System.out.println("+++++");
			}
		}
	}
}