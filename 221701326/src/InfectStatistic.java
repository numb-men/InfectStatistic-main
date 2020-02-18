import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.*;



public class InfectStatistic {
	public static void main(String args[]) throws IOException {
		int conditions [][]=new int [32][4];
		commandline cmd=new commandline();
		fileoperate fo=new fileoperate();
		if (args.length == 0) {
			System.out.println("主函数参数不为空");
			return;
		}
		if (cmd.isList(args[0])) {
			cmd.analysisCommand(args);// 命令分析并存储
			 fo.findFile(new File(cmd.logpath), conditions);
			 fo.writetoFile(new File(cmd.targetpath), conditions);
			return;
		} else {
			System.out
				.println("只接受list命令 相关参数：-log -out ");
			System.out.println("暂不支持其他命令");
			return;
		}	 	 				 
	}
}

class state{
	 String provinces[]= {"全国","安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北","河南",
                          "黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏","青海","山东",
        	              "山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"};
	 String pattern1= ".*新增 感染患者.*";
	 String pattern2= ".*新增 疑似患者.*";
	 String pattern3=".*治愈.*";
	 String pattern4=".*死亡.*";	
	 String pattern5= ".*感染患者 流入.*";
	 String pattern6= ".*疑似患者 流入.*";	
	 String pattern7=".*疑似患者 确诊感染.*";
	 String pattern8=".*排除 疑似患者.*";
	public  int judgetxtline(String textstr) {	
		int type=0;
		if(Pattern.matches(pattern1,textstr)) 
		    type=1;
		else if(Pattern.matches(pattern2,textstr)) 
		    type=2;
		else if(Pattern.matches(pattern3,textstr)) 
			type=3;
		else if(Pattern.matches(pattern4,textstr)) 
			type=4;
		else if(Pattern.matches(pattern5,textstr)) 
			type=5;
		else if(Pattern.matches(pattern6,textstr)) 
			type=6;
		else if(Pattern.matches(pattern7,textstr)) 
			type=7;
		else if(Pattern.matches(pattern8,textstr)) 
			type=8;
		else {
			System.out.println("该行日志文本文本格式错误!");
			System.exit(1);
		}
		return type;
	}
	public  void  conditions(String textstr,int conditions[][]) {
		String strs[]=textstr.split("\\s+");
		int choice=judgetxtline(textstr);		
		int province1 = 0,province2 = 0;
		switch(choice) {
		case 1:{	
			int mark = 0;
			for(int i=0;i<32;i++) {
				if(strs[0].equals(provinces[i])) {
					mark=i;
					break;
				}	
				else
					continue;
			}			
			province1=mark;
			String ipstr=strs[3].substring(0,strs[3].length()-1);
			int ipnum=Integer.parseInt(ipstr);
			for(int i=0;i<32;i++) {
				if(i==province1) {
					conditions[i][0]+=ipnum;
					break;
				}					
				else
					continue;
			}
			
			break;	
		}
		case 2:{
			int mark = 0;
			for(int i=0;i<32;i++) {
				if(strs[0].equals(provinces[i])) {
					mark=i;
					break;
				}	
				else
					continue;
			}		
			province1=mark;
			String spstr=strs[3].substring(0,strs[3].length()-1);
			int spnum=Integer.parseInt(spstr);
			for(int i=0;i<conditions.length;i++) {
				if(i==province1) {
					conditions[i][1]+=spnum;
					break;
				}
					
				else
					continue;
			}
			break;	
		}
		case 3:{
			int mark = 0;
			for(int i=0;i<32;i++) {
				if(strs[0].equals(provinces[i])) {
					mark=i;
					break;
				}	
				else
					continue;
			}		
			province1=mark;
			String curestr=strs[2].substring(0,strs[2].length()-1);
			int curenum=Integer.parseInt(curestr);
			for(int i=0;i<conditions.length;i++) {
				if(i==province1) {
					conditions[i][2]+=curenum;
					conditions[i][0]-=curenum;
					break;
				}
					
				else
					continue;
			}
			break;	
		}
		case 4:{
			int mark = 0;
			for(int i=0;i<32;i++) {
				if(strs[0].equals(provinces[i])) {
					mark=i;
					break;
				}	
				else
					continue;
			}		
			province1=mark;
			String deadstr=strs[2].substring(0,strs[2].length()-1);
			int deadnum=Integer.parseInt(deadstr);
			for(int i=0;i<conditions.length;i++) {
				if(i==province1) {
					conditions[i][3]+=deadnum;
					conditions[i][0]-=deadnum;
					break;
				}
					
				else
					continue;
			}
			break;	
		}
		case 5:{
			int mark1=0;
			int mark2=0;
			for(int i=0;i<32;i++) {
				if(strs[0].equals(provinces[i])) {
					mark1=i;
				}	
				if(strs[3].equals(provinces[i])) {
					mark2=i;
				}	
			}		
			province1=mark1;
			province2=mark2;
			String ipstr=strs[4].substring(0,strs[4].length()-1);
			int ipnum=Integer.parseInt(ipstr);
			for(int i=0;i<conditions.length;i++) {
				if(i==province1) 
					conditions[i][0]-=ipnum;
				else if(i==province2)
					conditions[i][0]+=ipnum;
				else
					continue;
			}
			break;	
		}
		case 6:{
			int mark1=0;
			int mark2=0;
			for(int i=0;i<32;i++) {
				if(strs[0].equals(provinces[i])) {
					mark1=i;
					
				}	
				if(strs[3].equals(provinces[i])) {
					mark2=i;
					
				}	
			}		
			province1=mark1;
			province2=mark2;
			String spstr=strs[4].substring(0,strs[4].length()-1);
			int spnum=Integer.parseInt(spstr);
			for(int i=0;i<conditions.length;i++) {
				if(i==province1) 
					conditions[i][1]-=spnum;
				else if(i==province2)
					conditions[i][1]+=spnum;
				else
					continue;
			}
			break;	
		}
		case 7:{
			int mark = 0;
			for(int i=0;i<32;i++) {
				if(strs[0].equals(provinces[i])) {
					mark=i;
					break;
				}	
				else
					continue;
			}		
			province1=mark;
			String ipstr=strs[3].substring(0,strs[3].length()-1);
			int ipnum=Integer.parseInt(ipstr);
			for(int i=0;i<conditions.length;i++) {
				if(i==province1) {
					conditions[i][0]+=ipnum;
					conditions[i][1]-=ipnum;
					break;
				}			
				else
					continue;
			}
			break;	
		}
		case 8:{
			int mark = 0;
			for(int i=0;i<32;i++) {
				if(strs[0].equals(provinces[i])) {
					mark=i;
					break;
				}	
				else
					continue;
			}		
			province1=mark;
			String spstr=strs[3].substring(0,strs[3].length()-1);
			int spnum=Integer.parseInt(spstr);
			for(int i=0;i<conditions.length;i++) {
				if(i==province1) {
					conditions[i][1]-=spnum;
					break;
				}
					
				else
					continue;
		}
		}
		}
	}   

}
class fileoperate{
	
	public void findFile(File dir,int conditions[][]) throws IOException{
	    File[] dirFiles = dir.listFiles();
	    state status=new state();
	    for(File temp : dirFiles){
	    //查找指定的文件
	    if(temp.isFile() && temp.getAbsolutePath().endsWith(".txt") ){    
	    BufferedReader in = new BufferedReader(new FileReader(temp));
        String str=null;                
        while ((str = in.readLine())!=null) {
        	if(str.startsWith("//"))
        		break;                           	                  		
    		status.conditions(str, conditions); 
    		System.out.println(temp.isFile() + " " + temp.getAbsolutePath());
    		}
        in.close();
        }
    }
}
	

    public  void writetoFile(File file,int conditions[][]) throws IOException{
    	state status=new state();
    	int count=0;
		if(!file.exists()){
			file.createNewFile();
		}
		FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fileWriter);
		for(int k=0;k<32;k++) {
			if(k==0) {
				for(int p=0;p<4;p++) {					
					for(int q=1;q<32;q++)
						count+=conditions[q][p];
					conditions[k][p]=count;
					count=0;
				}
			}
			String write=status.provinces[k]+" 感染患者"+conditions[k][0]+"人"+
						         " 疑似患者"+conditions[k][1]+"人"+" 治愈"+conditions[k][2]+"人"+
						         " 死亡"+conditions[k][3]+"人"+"\n";
			bw.write(write);
		}
		bw.write("//该文档并非真实数据，仅供测试使用");
		bw.close();
		System.out.println("finish");
	}
	

}
class commandline {
	String logpath;
	String targetpath;
	void analysisCommand(String[] args) {
		// TODO Auto-generated method stub
		int test = 0;
		for (int i = 1; i < args.length; i++) {
			if (isCommand(args[i])) {
				test = 0;
				if (isLog(args[i])) {
					test = 1;
				} else if (isOut(args[i])) {
					test = 2;
				} 
			} else {
				if (test == 1) {
					File file = new File(args[i]);
					if (!file.exists()) {
						System.out.println("-log文件名错误，'" + args[i] + "'文件不存在");
						System.exit(0);
					}
					if (!file.isDirectory()) {
						System.out.println("-log文件名错误，'" + args[i] + "'不是一个目录文件");
						System.exit(0);
					}
					logpath=args[i];
				} 
				else if (test == 2) {
					File file = new File(args[i]);
					if (!file.exists()) {
						try {
							file.createNewFile();// 创建日志文件
						} catch (IOException e) {
							// TODO Auto-generated catch block
							System.out.println(
								"-out错误，'" + args[i] + "'不存在并且无法创建该文件");
							e.printStackTrace();
						}
					}
					targetpath=args[i];
				}
			}
		}
}
	boolean isCommand(String args) {
		if (args.charAt(0) == '-') {
			return true;
		}
		return false;
	}

	boolean isList(String args) {
		if (args.equals("list")) {
			return true;
		}
		return false;
	}

	boolean isLog(String args) {
		if (args.equals("-log")) {
			return true;
		}
		return false;
	}

	boolean isOut(String args) {
		if (args.equals("-out")) {
			return true;
		}
		return false;
	}
}
