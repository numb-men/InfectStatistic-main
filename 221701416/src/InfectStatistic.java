import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.FileOutputStream;  
import java.io.PrintStream;
/**
 * InfectStatistic
 *
 * @author Hanani
 * @version v1.0.0
 */

class InfectStatistic {
	
	public static String Log=new String();
	public static String Out=new String();
	static SimpleDateFormat Df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
	static String TString=Df.format(new Date());
	public static String Date=new String(TString);
	public static String []Type= new String[50];
	public static String []Province=new String[50];
	
    public static void main(String[] args) throws IOException {
    	CoronavirusDetail CD=new CoronavirusDetail();
    	CD.Init();
    	AnalysisCommand(args);
    	CD.ReadAll(Log,Out,Date,Type,Province);
    	CD.PrintDetail(Log,Out,Date,Type,Province);
    }
    
    static String $list= new String("list");
	static String $log= new String("-log");
	static String $out= new String("-out");
	static String $date= new String("-date");
	static String $type= new String("-type");
	static String $province= new String("-province");
	static String $nothing=new String("nothing");
	
    //单命令匹配
    public static boolean Match(String Th,String Tar) {
    	if(Tar.equals(Th)) 
    		return true;
    	return false;
    }
    
    //双命令匹配
    public static String MatchTot(String []Th,String Tar,int Len,String Ori) {
    	for(int i=1;i<Len;i++) {
    		if(Th[i].equals(Tar)) {
    			return Th[i+1];
    		}
    	}
    	return Ori;
    }
    
    //多命令匹配
    public static String[] MatchMuch(String []Th,String Tar,int Len,String[] Ori) {
    	
    	String []Now=new String[50];
    	
    	for(int i=0;i<50;i++) Now[i]=$nothing;
    	int Pos=0;
		for(int i=1;i<Len;i++) {
			if(Th[i].equals(Tar)) {
				Pos=i;
				break;
			}
		}
		
		int index=0;
		
		for(int i=Pos+1;i<Len;i++) {
			if(Th[i].charAt(0)!='-') {
				Now[index++]=Th[i];
			}else break;
		}
		
		return Now;
    }
    
    //对输入的命令进行处理分解
    public static void AnalysisCommand(String[] Command) {
    	
    	int Len=Command.length;
    	
    	//初始化type和province
    	for(int i=0;i<50;i++) Type[i]=$nothing;
    	for(int i=0;i<50;i++) Province[i]=$nothing;
    	
    	//list检查
		if(!(Match(Command[0],$list))) {
			System.out.println("请使用list命令进行操作");
			System.exit(0);
		}
		
		//获取log目录,out目录,date数值
		Log=MatchTot(Command,$log,Len,Log);
		Out=MatchTot(Command,$out,Len,Out);
		Date=MatchTot(Command,$date,Len,Date);
		//获取type的参数，province的参数
		Type=MatchMuch(Command,$type,Len,Type);
		Province=MatchMuch(Command,$province,Len,Province);
		
    }
}



/**
 * CoronavirusDetail
 *
 * @author Hanani
 * @version v1.0.0
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
		"ip" , "sp", "cure", "dead"
	};
	String[] TypeStrCn={
			"感染患者" , "疑似患者", "治愈", "死亡"
		};
	int _provincenum=32;
	int _detailnum=4;
	public int [][] Detail=new int[_provincenum][_detailnum];
	
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
		
		for(int i=0;i<_provincenum;i++)
			for(int j=0;j<_detailnum;j++) Detail[i][j]=0;
	}
	
	//输出所需信息
    public void PrintDetail(String Log,String Out,String Date,
    		String [] Type,String [] Province) throws IOException {
    	
    	File file=new File(Out);
    	
    	if(!file.exists()) {
    		file.createNewFile();
    	}
    	
    	PrintStream Ps=new PrintStream(Out);
    	System.setOut(Ps);
    	
    	for(int i=1;i<32;i++) 
    		for(int j=0;j<4;j++) Detail[0][j]+=Detail[i][j];
    	
    	for(int i=0;i<ProvinceStr.length;i++) {
        	for(int j=0;j<Province.length;j++) 
        	if(ProvinceStr[i].equals(Province[j])){
        		System.out.print(ProvinceStr[i]+" ");
        		Integer Pronum=(Integer) ProvinceMap.get(ProvinceStr[i]);
        		int Typecnt=0;
        		for(int k=0;k<4;k++) {
        			if(Type[k].equals($nothing)) Typecnt++;
        		}
        		for(int k=0;k<4;k++) 
        		if((Typecnt==4)||(!Type[k].equals($nothing))){
        			Integer Tynum=(Integer) TypeMap.get(Type[k]);
        			System.out.print(TypeStrCn[Tynum]+Detail[Pronum][Tynum]+" ");
        		}
        		System.out.println();
        	}
    	}
    	
    }
    
	//获取指定日期之前的文件名
	public static ArrayList<String> GetFilesName(String path,String date){
		
		ArrayList<String> Files = new ArrayList<String>();
		ArrayList<String> Beforefiles = new ArrayList<String>();
	    File file = new File(path);
	    File[] tempList = file.listFiles();
	    
	    for (int i = 0; i < tempList.length; i++) {
	        if (tempList[i].isFile()) {
	            Files.add(tempList[i].toString());
	        }
	        //得到当前日期之前的文件名
	        File tempFile=new File(Files.get(i).trim());
	        String fileName=tempFile.getName().substring(0,10);
	        if(date.compareTo(fileName)>=0){
	        	Beforefiles.add(Files.get(i));
	        }
	    }
	    return Beforefiles;
	}
	
	//读取所有指定日期之前的文件
	public void ReadAll(String Log,String Out,String Date,
			String [] Type,String [] Province) throws IOException{
		
		//设定正则表达式规则
		String MatString_1="(\\S+) 新增 感染患者 (\\d+)人";
		String SplitString_1=" 新增 感染患者 |人";
		
		String MatString_2="(\\S+) 新增 疑似患者 (\\d+)人";
		String SplitString_2=" 新增 疑似患者 |人";
		
		String MatString_3="(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
		String SplitString_3=" 感染患者 流入 | |人";
		
		String MatString_4="(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
		String SplitString_4=" 疑似患者 流入 | |人";
		
		String MatString_5="(\\S+) 死亡 (\\d+)人";
		String SplitString_5=" 死亡 |人";
		
		String MatString_6="(\\S+) 治愈 (\\d+)人";
		String SplitString_6=" 治愈 |人";
		
		String MatString_7="(\\S+) 疑似患者 确诊感染 (\\d+)人";
		String SplitString_7=" 疑似患者 确诊感染 |人";
		
		String MatString_8="(\\S+) 排除 疑似患者 (\\d+)人";
		String SplitString_8=" 排除 疑似患者 |人";
		
		for(int i=0;i<4;i++) {
			//System.out.println(type[i]);
		}
		
		for(int i=0;i<31;i++) {
			//System.out.println(province[i]);
		}
		
		//得到需要的文件路径并将其读入
		ArrayList<String> TeArrayList=GetFilesName(Log,Date);
		for(int i=0;i<TeArrayList.size();i++) {
			//System.out.println(teArrayList.get(i));

			BufferedReader InBufferedReader=
					new BufferedReader(new InputStreamReader(
							new FileInputStream(TeArrayList.get(i)), "UTF-8"));
			String NowString;
			while((NowString=InBufferedReader.readLine())!=null) {
				//System.out.print(nowString);
				//System.out.println("???");
				//<省> 新增 感染患者 n人
				if(NowString.matches(MatString_1)) {
					String [] Strings_1=NowString.split(SplitString_1);
					Detail[(Integer) ProvinceMap.get(Strings_1[0])][0]+=
							Integer.parseInt(Strings_1[1]);
				}
				//<省> 新增 疑似患者 n人
				else if(NowString.matches(MatString_2)) {
					String [] Strings_2=NowString.split(SplitString_2);
					Detail[(Integer) ProvinceMap.get(Strings_2[0])][1]+=
							Integer.parseInt(Strings_2[1]);
				}
				//<省1> 感染患者 流入 <省2> n人
				else if(NowString.matches(MatString_3)) {
					String [] Strings_3=NowString.split(SplitString_3);
					Detail[(Integer) ProvinceMap.get(Strings_3[0])][0]-=
							Integer.parseInt(Strings_3[2]);
					Detail[(Integer) ProvinceMap.get(Strings_3[1])][0]+=
							Integer.parseInt(Strings_3[2]);
				}
				//<省1> 疑似患者 流入 <省2> n人
				else if(NowString.matches(MatString_4)) {
					String [] Strings_4=NowString.split(SplitString_4);
					Detail[(Integer) ProvinceMap.get(Strings_4[0])][1]-=
							Integer.parseInt(Strings_4[2]);
					Detail[(Integer) ProvinceMap.get(Strings_4[1])][1]+=
							Integer.parseInt(Strings_4[2]);
				}
				//<省> 死亡 n人
				else if(NowString.matches(MatString_5)) {
					String [] Strings_5=NowString.split(SplitString_5);
					Detail[(Integer) ProvinceMap.get(Strings_5[0])][3]+=
							Integer.parseInt(Strings_5[1]);
					Detail[(Integer) ProvinceMap.get(Strings_5[0])][0]-=
							Integer.parseInt(Strings_5[1]);
				}
				//<省> 治愈 n人
				else if(NowString.matches(MatString_6)) {
					String [] Strings_6=NowString.split(SplitString_6);
					Detail[(Integer) ProvinceMap.get(Strings_6[0])][2]+=
							Integer.parseInt(Strings_6[1]);
					Detail[(Integer) ProvinceMap.get(Strings_6[0])][0]-=
							Integer.parseInt(Strings_6[1]);
				}
				//<省> 疑似患者 确诊感染 n人
				else if(NowString.matches(MatString_7)) {
					String [] Strings_7=NowString.split(SplitString_7);
					Detail[(Integer) ProvinceMap.get(Strings_7[0])][0]+=
							Integer.parseInt(Strings_7[1]);
					Detail[(Integer) ProvinceMap.get(Strings_7[0])][1]-=
							Integer.parseInt(Strings_7[1]);
				}
				//<省> 排除 疑似患者 n人
				else if(NowString.matches(MatString_8)) {
					String [] Strings_8=NowString.split(SplitString_8);
					Detail[(Integer) ProvinceMap.get(Strings_8[0])][1]-=
							Integer.parseInt(Strings_8[1]);
				}
				//System.out.println("+++++");
			}
		}
		
	}
}