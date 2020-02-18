import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class Province {
	String name;
	int ip;
	int sp;
	int cure;
	int dead;
	boolean status;
	Province(String name) {
		this.name=name;
		this.ip=0;
		this.sp=0;
		this.cure=0;
		this.cure=0;
		this.dead=0;
		this.status=false;
	}
}

public class InfectStatistic {
	
	static String log=null;
	static String out=null;
	static String date="2020-03-03";
	static StringBuffer type=new StringBuffer("");
	static StringBuffer province=new StringBuffer("");
	static String[] provinces={"全国",
			"安徽","北京","重庆","福建","甘肃","广东",
			"广西","贵州","海南","河北","河南","黑龙江",
			"湖北","湖南","吉林","江苏","江西","辽宁",
			"内蒙古","宁夏","青海","山东","山西","陕西",
			"上海","四川","天津","西藏","新疆","云南","浙江"};
	static Province[] provincelist=new Province[provinces.length];
	
	public static void main(String[] args) throws IOException {
//		readCommand(args);
//		readFile("D:log");
		provinceInit();
		System.out.println(provincelist[0].name);
    }
	
	public static void readCommand(String[] args) {
		if(!args[0].equals("list")) {
			System.out.println("缺少list指令");
			System.exit(0);
		}
		for(int i=0;i<args.length;i++) {
			if(args[i].equals("-log")) {
				log=args[++i];
			}
			if(args[i].equals("-out")) {
				out=args[++i];
			}
			if(args[i].equals("-date")) {
				date=args[++i];
			}
			if(args[i].equals("-type")) {
				type.append(args[++i]);
				for(int j=i+1;j<args.length-1;j++) {
					if(args[j].equals("ip")||args[j].equals("sp")||
					args[j].equals("cure")||args[j].equals("dead")) {
						type.append(" "+args[j]);
					}
				}
			}
			if(args[i].equals("-province")) {
				province.append(args[++i]);
				for(int j=i;j<args.length-1;j++) {
					if(!(args[i].equals("-log")||args[i].equals("-out")||args[i].equals("-date"))) {
						i++;
						province.append(" "+args[i]);
					}
				}
			}
		}
		System.out.println(log);
		System.out.println(out);
		System.out.println(date);
		System.out.println(type);
		System.out.println(province);
	}
	
	public static void readFile(String path) throws IOException {
		File file=new File(path);
        if (file.exists()) {
            File[] files=file.listFiles();
            if (null!=files) {
                for (File file2:files) {
                    if (!file2.isDirectory()) {
                    	FileReader fr=new FileReader(file2);
                    	BufferedReader br=new BufferedReader(fr);
                    	String line="";
                    	String filedate=file2.getName();
                    	filedate=filedate.substring(0, 10);
//                    	System.out.println(filedate);
                    	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                    	Date logdate=null;
                    	Date cmddate=null;
						try {
							logdate = format.parse(filedate);
//							if(date.equals(null)) date="2050-1-1";
							cmddate = format.parse(date);
						} catch (ParseException e) {
							e.printStackTrace();
						}
//                    	System.out.println(logdate.toString());
						if(logdate.compareTo(cmddate)<=0) {
							while((line=br.readLine())!=null) {
//	                    		System.out.println(line);
								statistic(line);
	                    	}
						}
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
            System.exit(0);
        }
	}
	static void provinceInit() {
		for(int i=0;i<provinces.length;i++) {
			provincelist[i]=new Province(provinces[i]);
		}
		
	}
	static int findProvince(String provincename) {
		for(int i=0;i<provinces.length;i++) {
			if(provincelist[i].name.equals(provincename)) {
				return i;
			}
		}
		return 0;
	}
	static void statistic(String line) {
		
		String p1=".*新增 感染患者.*";
		String p2=".*新增 疑似患者.*";
		String p3=".*感染患者 流入.*";
		String p4=".*疑似患者 流入.*";
		String p5=".*死亡.*";
		String p6=".*治愈.*";
		String p7=".*疑似患者 确诊感染.*";
		String p8=".*排除 疑似患者.*";
		
	}
}
