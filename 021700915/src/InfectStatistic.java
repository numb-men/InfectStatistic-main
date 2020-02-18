import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Province {
	String name;  //省份名称
	int ip;  //感染人数
	int sp;  //疑似人数人数
	int cure;  //治愈人数
	int dead;  //死亡人数
	boolean status;  //省份是否在日志文件出现过
	boolean showstatus;  //-province参数中是否包含该省份
	Province(String name) {
		this.name=name;
		this.ip=0;
		this.sp=0;
		this.cure=0;
		this.dead=0;
		this.status=false;
		this.showstatus=false;
	}
}

public class InfectStatistic {
	
	static String log="";
	static String out="";
	static String date="";
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
		provinceInit();
		readCommand(args);
		readFile(log);
		outputFile(out,type,province);
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
					if(!(args[i].equals("-log")||args[i].equals("-out")
					||args[i].equals("-date")||args[i].equals("-type"))) {
						i++;
						province.append(" "+args[i]);
					}
				}
			}
		}
	}
	
	static void readFile(String path) throws IOException {
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
		
		String p="\\\\";
		String p1=".*新增 感染患者.*";
		String p2=".*新增 疑似患者.*";
		String p3=".*感染患者 流入.*";
		String p4=".*疑似患者 流入.*";
		String p5=".*死亡.*";
		String p6=".*治愈.*";
		String p7=".*疑似患者 确诊感染.*";
		String p8=".*排除 疑似患者.*";
		
		Pattern r=Pattern.compile(p);
		Pattern r1=Pattern.compile(p1);
		Pattern r2=Pattern.compile(p2);
		Pattern r3=Pattern.compile(p3);
		Pattern r4=Pattern.compile(p4);
		Pattern r5=Pattern.compile(p5);
		Pattern r6=Pattern.compile(p6);
		Pattern r7=Pattern.compile(p7);
		Pattern r8=Pattern.compile(p8);
		
		Matcher m=r.matcher(line);
		Matcher m1=r1.matcher(line);
		Matcher m2=r2.matcher(line);
		Matcher m3=r3.matcher(line);
		Matcher m4=r4.matcher(line);
		Matcher m5=r5.matcher(line);
		Matcher m6=r6.matcher(line);
		Matcher m7=r7.matcher(line);
		Matcher m8=r8.matcher(line);
		
		if(m.find()) return;
		else if(m1.find()) {
			String[] str=line.split(" ");
			int index=findProvince(str[0]);
			provincelist[0].ip+=Integer.parseInt(str[3].substring(0, str[3].length()-1));
			provincelist[index].ip+=Integer.parseInt(str[3].substring(0, str[3].length()-1));
			provincelist[index].status=true;
//			System.out.println(provincelist[index].ip);
		}
		
		else if(m2.find()) {
			String[] str=line.split(" ");
			int index=findProvince(str[0]);
			provincelist[0].sp+=Integer.parseInt(str[3].substring(0, str[3].length()-1));
			provincelist[index].sp+=Integer.parseInt(str[3].substring(0, str[3].length()-1));
			provincelist[index].status=true;
//			System.out.println(provincelist[index].sp);
		}
		
		else if(m3.find()) {
			String[] str=line.split(" ");
			int index1=findProvince(str[0]);
			int index2=findProvince(str[3]);
			provincelist[index1].ip-=Integer.parseInt(str[4].substring(0, str[4].length()-1));
			provincelist[index2].ip+=Integer.parseInt(str[4].substring(0, str[4].length()-1));
			provincelist[index1].status=true;
			provincelist[index2].status=true;
//			System.out.println(provincelist[index2].ip);
		}
		
		else if(m4.find()) {
			String[] str=line.split(" ");
			int index1=findProvince(str[0]);
			int index2=findProvince(str[3]);
			provincelist[index1].sp-=Integer.parseInt(str[4].substring(0, str[4].length()-1));
			provincelist[index2].sp+=Integer.parseInt(str[4].substring(0, str[4].length()-1));
			provincelist[index1].status=true;
			provincelist[index2].status=true;
//			System.out.println(provincelist[index2].sp);
		}
		
		else if(m5.find()) {
			String[] str=line.split(" ");
			int index=findProvince(str[0]);
			provincelist[0].ip-=Integer.parseInt(str[2].substring(0, str[2].length()-1));
			provincelist[0].dead+=Integer.parseInt(str[2].substring(0, str[2].length()-1));
			provincelist[index].ip-=Integer.parseInt(str[2].substring(0,str[2].length()-1));
			provincelist[index].dead+=Integer.parseInt(str[2].substring(0,str[2].length()-1));
			provincelist[index].status=true;
//			System.out.println(provincelist[index].dead);
		}
		
		else if(m6.find()) {
			String[] str=line.split(" ");
			int index=findProvince(str[0]);
			provincelist[0].ip-=Integer.parseInt(str[2].substring(0, str[2].length()-1));
			provincelist[0].cure+=Integer.parseInt(str[2].substring(0, str[2].length()-1));
			provincelist[index].ip-=Integer.parseInt(str[2].substring(0,str[2].length()-1));
			provincelist[index].cure+=Integer.parseInt(str[2].substring(0,str[2].length()-1));
			provincelist[index].status=true;
//			System.out.println(provincelist[index].cure);
		}
		
		else if(m7.find()) {
			String[] str=line.split(" ");
			int index=findProvince(str[0]);
			provincelist[0].ip+=Integer.parseInt(str[3].substring(0, str[3].length()-1));
			provincelist[0].sp-=Integer.parseInt(str[3].substring(0, str[3].length()-1));
			provincelist[index].ip+=Integer.parseInt(str[3].substring(0, str[3].length()-1));
			provincelist[index].sp-=Integer.parseInt(str[3].substring(0, str[3].length()-1));
			provincelist[index].status=true;
//			System.out.println(provincelist[index].ip);
		}
		
		else if(m8.find()) {
			String[] str=line.split(" ");
			int index=findProvince(str[0]);
			provincelist[0].sp-=Integer.parseInt(str[3].substring(0, str[3].length()-1));
			provincelist[index].sp-=Integer.parseInt(str[3].substring(0, str[3].length()-1));
			provincelist[index].status=true;
//			System.out.println(provincelist[index].sp);
		}
		
		provincelist[0].status=true;
	}
	static void outputFile(String out,StringBuffer stype,StringBuffer sprovince) throws IOException {
		
		boolean ipstatus=false;
		boolean spstatus=false;
		boolean curestatus=false;
		boolean deadstatus=false;
		boolean haveprovince=false;
		String[] type=stype.toString().split(" ");
		String[] province=sprovince.toString().split(" ");
		
		if(stype.toString().equals("")) {
			ipstatus=true;
			spstatus=true;
			curestatus=true;
			deadstatus=true;
		}
		else {
			for(int i=0;i<type.length;i++) {
				if(type[i].equals("ip")) ipstatus=true;
				if(type[i].equals("sp")) spstatus=true;
				if(type[i].equals("cure")) curestatus=true;
				if(type[i].equals("dead")) deadstatus=true;
			}
		}
		
		if(sprovince.toString().equals("")) haveprovince=false;
		else {
			haveprovince=true;
			for(int i=0;i<province.length;i++) {
				provincelist[findProvince(province[i])].showstatus=true;
			}
		}
		
		File f=new File(out);
        FileOutputStream fop=new FileOutputStream(f);
        OutputStreamWriter writer=new OutputStreamWriter(fop,"UTF-8");
        
//        //test
//        for(int i=0;i<provinces.length;i++) {
//        	if(provincelist[i].status==true) {
//        		writer.append(provincelist[i].name+" ");
//            	writer.append("感染患者"+provincelist[i].ip+"人 ");
//            	writer.append("疑似患者"+provincelist[i].sp+"人 ");
//            	writer.append("治愈"+provincelist[i].cure+"人 ");
//            	writer.append("死亡"+provincelist[i].dead+"人");
//            	writer.append("\n");
//        	}
//        }
//        writer.close();
        
		if(haveprovince==false) {
			for(int i=0;i<provinces.length;i++) {
				if(provincelist[i].status==true) {
					writer.append(provincelist[i].name+" ");
					if(ipstatus==true) writer.append("感染患者"+provincelist[i].ip+"人 ");
					if(spstatus==true) writer.append("疑似患者"+provincelist[i].sp+"人 ");
					if(curestatus==true) writer.append("治愈"+provincelist[i].cure+"人 ");
					if(deadstatus==true) writer.append("死亡"+provincelist[i].dead+"人");
					writer.append("\n");
				}
			}
			writer.append("// 该文档并非真实数据，仅供测试使用");
			writer.flush();
		}
		else {
			for(int i=0;i<province.length;i++) {
				provincelist[findProvince(province[i])].showstatus=true;
			}
			for(int i=0;i<provinces.length;i++) {
				if(provincelist[i].showstatus==true) {
					writer.append(provincelist[i].name+" ");
					if(ipstatus==true) writer.append("感染患者"+provincelist[i].ip+"人 ");
					if(spstatus==true) writer.append("疑似患者"+provincelist[i].sp+"人 ");
					if(curestatus==true) writer.append("治愈"+provincelist[i].cure+"人 ");
					if(deadstatus==true) writer.append("死亡"+provincelist[i].dead+"人");
					writer.append("\n");
				}
			}
			writer.append("// 该文档并非真实数据，仅供测试使用");
			writer.flush();
			writer.close();
		}
		fop.close();
		FileInputStream fip=new FileInputStream(f);
	    InputStreamReader reader=new InputStreamReader(fip,"UTF-8");
	    StringBuffer sb=new StringBuffer();
	        while(reader.ready()) {
	            sb.append((char) reader.read());
	        }
	        System.out.println(sb.toString());
	        reader.close();
	        fip.close();
	}
}
