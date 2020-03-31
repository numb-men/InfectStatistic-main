import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.junit.Test;

/**
 * Lib
 * TODO
 *
 * @author Hkb
 * @version xxx
 * @since 2020.2.10
 */
public class Lib {
	
	
	
	interface Command {
		void execute();
	}
	
	
	public static class Invoker {
		public List<Command> commandList=new ArrayList<Command>();
		
		public void takeCommand(Command command) {
			commandList.add(command);
			command.execute();
		}
	}
	
	public static class ListCommand implements Command{
		//常量定义
		private final List<String> PATIENTS_TYPE=new ArrayList<String>(Arrays.asList("ip","sp","cure","dead"));
		private final List<String> PATIENTS_STATUS=new ArrayList<String>(Arrays.asList("感染患者","疑似患者","治愈","死亡患者"));
		private final List<String> PROVINCE_STR=new ArrayList<String>(Arrays.asList("全国", "安徽","北京", "重庆", "福建","甘肃","广东", "广西", "贵州", "海南",
									"河北", "河南", "黑龙江", "湖北", "湖南", "吉林","江苏", "江西", "辽宁", "内蒙古", "宁夏", 
									"青海", "山东", "山西", "陕西", "上海","四川", "天津", "西藏", "新疆", "云南", "浙江"));
		/*private static final String[] PROVINCE_STR= {"全国", "安徽","北京", "重庆", "福建","甘肃","广东", "广西", "贵州", "海南",
									"河北", "河南", "黑龙江", "湖北", "湖南", "吉林","江苏", "江西", "辽宁", "内蒙古", "宁夏", 
									"青海", "山东", "山西", "陕西", "上海","四川", "天津", "西藏", "新疆", "云南", "浙江"};*/
		//private String[] pattern={""}
		public int[][] data=new int[PROVINCE_STR.size()][PATIENTS_TYPE.size()];
		public String[] args;
		public String logPath;
		public String outPath;
		public String date;
		public List<String> type=new ArrayList<String>();
		public List<String> province=new ArrayList<String>();
		public boolean log_exist=false;
		public boolean out_exist=false;
		public boolean province_exist=false;
		public boolean date_exist=false;
		public boolean date_true=true;
		
		//使用字符串构造ListCommand命令
		@SuppressWarnings("deprecation")
		public ListCommand(String argsStr) {
			//获取默认时间 格式：yyyy-mm-dd
			Date d=new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			date=format.format(d);
			        
			//设置默认type 默认ip:感染患者，sp：疑似患者，cure：治愈 ，dead：死亡患者
			for(String s:PATIENTS_TYPE) {
				type.add(s);
			}
			args=argsStr.split("\\s+");
			int argsLength=args.length;
			for(int i=0;i<argsLength;i++) {
				if(args[i].toLowerCase().equals("-log")&&i<argsLength-1) {
					log_exist=true;
					logPath=args[i+1];
				}
				if(args[i].toLowerCase().equals("-out")&&i<argsLength-1) {
					out_exist=true;
					outPath=args[i+1];
				}
				if(args[i].toLowerCase().equals("-date")&&i<argsLength-1) {
					date_exist=true;
					date=args[i+1];
				}
				if(args[i].toLowerCase().equals("-type")) {
					int j=i+1;
					type.clear();
					while(j<args.length&&!args[j].startsWith("-")) {
						type.add(args[j]);
						j++;
					}
				}
				if(args[i].toLowerCase().equals("-province")) {
					province_exist=true;
					int j=i+1;
					while(j<args.length&&!args[j].startsWith("-")) {
						province.add(args[j]);
						j++;
					}
					
					
				}
			}  
		}
		
		public void isValidDate(String str) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				format.setLenient(false);
				format.parse(str);
			} catch (ParseException e) {
				date_true=false;
			}
		}
		
		
		
		//判断是否为合法的命令格式
		public boolean isTrueCommand() {
			if(!args[0].toLowerCase().equals("list")) return false;
			if(!log_exist||!out_exist) return false;
			if(province_exist&&province.isEmpty()) return false;
			if(type.isEmpty()) return false;
			isValidDate(date);
			if(date_exist&&!date_true) return false;
			return true;
		}
		
		//命令的参数化函数
		public void list(String logPath, String outPath, String date, List<String> province, List<String> type) {
			if(isTrueCommand()) {
				try {
					readFromLogPath(this.logPath);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				outLogLine(outPath,province,type);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//进入文件路径
		public void readFromLogPath(String logPath) throws FileNotFoundException {
			File file = new File(this.logPath);
			 //File [] files = file.listFiles();
			 String[] names = file.list();
			 if(names != null) {
				 String [] completNames = new String[names.length];
				 for(int i = 0;i < names.length;i++) {
					 String fileDate=(String) names[i].subSequence(0, 10);
					 if(date.compareTo(fileDate)>=0) {
					 completNames [i] = this.logPath+names[i];
					 //System.out.println(completNames [i]);
					 readFromFile(completNames[i]);
					 }
				 }
			 }
		}
		
		//从文件中读取
		public void readFromFile(String filePath) throws FileNotFoundException {
	        String[] bufstring=new String[1024];
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			int position=0;
			String line=null;
			try {
				while((line=br.readLine())!=null) {
					bufstring[position]=line;
					//System.out.println(bufstring[position]);
					eachLine(line);
					position++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		//处理每行的数据
		public void eachLine(String line) {
			if(Pattern.matches("(\\S+) 新增 感染患者 (\\d+)人", line)){
				if(!province_exist) addProvince(line);
				handleIp(line);
			}
			if(Pattern.matches("(\\S+) 新增 疑似患者 (\\d+)人", line)) {
				if(!province_exist) addProvince(line);
				handleSp(line);
			}
			
			if(Pattern.matches("(\\S+) 感染患者 流入 (\\S+) (\\d+)人", line)) {
				if(!province_exist) addProvince(line);
				handleIp(line);
			}
			if(Pattern.matches("(\\S+) 疑似患者 流入 (\\S+) (\\d+)人", line)) {
				if(!province_exist) addProvince(line);
				handleSp(line);
			}
			
			if(Pattern.matches("(\\S+) 治愈 (\\d+)人", line)) {
				if(!province_exist) addProvince(line);
				handleCure(line);
			}
			
			if(Pattern.matches("(\\S+) 死亡 (\\d+)人", line)) {
				if(!province_exist) addProvince(line);
				handleDead(line);
			}
			
			if(Pattern.matches("(\\S+) 疑似患者 确诊感染 (\\d+)人", line)) {
				if(!province_exist) addProvince(line);
				handleSp(line);
			}
			if(Pattern.matches("(\\S+) 排除 疑似患者 (\\d+)人", line)) {
				if(!province_exist) addProvince(line);
				handleSp(line);
			}
		}
		
		
		//-province为空时的
		public void addProvince(String line) {
			String[] arr=line.split("\\s+");
			if(!province.contains(arr[0])) {
				province.add(arr[0]);
			}
		}
		
		//获取province在数组中对应位置
		public int getProvinceIndex(String s) {
			return PROVINCE_STR.indexOf(s);
		}
		
		//获取patient在数组中的对应位置
		public int getPatientIndex(String s) {
			return PATIENTS_TYPE.indexOf(s);
		}
		
		//处理感染患者
		public void handleIp(String line) {
			if(Pattern.matches("(\\S+) 新增 感染患者 (\\d+)人", line)) {
				String pattern1="(\\S+) 新增 感染患者 (\\d+)人";
				Pattern r1 = Pattern.compile(pattern1);
				Matcher m1=r1.matcher(line);
				if(m1.find()) {
					data[PROVINCE_STR.indexOf(m1.group(1))][0]+=Integer.parseInt(m1.group(2));
					data[0][0]+=Integer.parseInt(m1.group(2));
				}
			}
			
			if(Pattern.matches("(\\S+) 感染患者 流入 (\\S+) (\\d+)人", line)) {
				String pattern2="(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
				Pattern r2 = Pattern.compile(pattern2);
				Matcher m2=r2.matcher(line);
				if(m2.find()) {
					data[PROVINCE_STR.indexOf(m2.group(1))][0]-=Integer.parseInt(m2.group(3));
					data[PROVINCE_STR.indexOf(m2.group(2))][0]+=Integer.parseInt(m2.group(3));
				}
			}
		}
		
		
		//处理疑似患者
		public void handleSp(String line) {
			if(Pattern.matches("(\\S+) 新增 疑似患者 (\\d+)人", line)) {
				String pattern1="(\\S+) 新增 疑似患者 (\\d+)人";
				Pattern r1 = Pattern.compile(pattern1);
				Matcher m1=r1.matcher(line);
				if(m1.find()) {
					data[PROVINCE_STR.indexOf(m1.group(1))][1]+=Integer.parseInt(m1.group(2));
					data[0][1]+=Integer.parseInt(m1.group(2));
				}
			}
			
			if(Pattern.matches("(\\S+) 疑似患者 流入 (\\S+) (\\d+)人", line)) {
				String pattern2="(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
				Pattern r2 = Pattern.compile(pattern2);
				Matcher m2=r2.matcher(line);
				if(m2.find()) {
					data[PROVINCE_STR.indexOf(m2.group(1))][1]-=Integer.parseInt(m2.group(3));
					data[PROVINCE_STR.indexOf(m2.group(2))][1]+=Integer.parseInt(m2.group(3));
				}
			}
			
			if(Pattern.matches("(\\S+) 疑似患者 确诊感染 (\\d+)人", line)) {
				String pattern3="(\\S+) 疑似患者 确诊感染 (\\d+)人";
				Pattern r3 = Pattern.compile(pattern3);
				Matcher m3=r3.matcher(line);
				if(m3.find()) {
					data[PROVINCE_STR.indexOf(m3.group(1))][1]-=Integer.parseInt(m3.group(2));
					data[0][1]-=Integer.parseInt(m3.group(2));
					data[PROVINCE_STR.indexOf(m3.group(1))][0]+=Integer.parseInt(m3.group(2));
					data[0][0]+=Integer.parseInt(m3.group(2));
				}
			}
			
			if(Pattern.matches("(\\S+) 排除 疑似患者 (\\d+)人", line)) {
				String pattern4="(\\S+) 排除 疑似患者 (\\d+)人";
				Pattern r4 = Pattern.compile(pattern4);
				Matcher m4=r4.matcher(line);
				if(m4.find()) {
					data[PROVINCE_STR.indexOf(m4.group(1))][1]-=Integer.parseInt(m4.group(2));
					data[0][1]-=Integer.parseInt(m4.group(2));
				}
			}
		}
		
		//处理治愈人数
		public void handleCure(String line) {
			if(Pattern.matches("(\\S+) 治愈 (\\d+)人", line)) {
				String pattern1="(\\S+) 治愈 (\\d+)人";
				Pattern r1 = Pattern.compile(pattern1);
				Matcher m1=r1.matcher(line);
				if(m1.find()) {
					data[PROVINCE_STR.indexOf(m1.group(1))][2]+=Integer.parseInt(m1.group(2));
					data[0][2]+=Integer.parseInt(m1.group(2));
					data[PROVINCE_STR.indexOf(m1.group(1))][0]-=Integer.parseInt(m1.group(2));
					data[0][0]-=Integer.parseInt(m1.group(2));
				}
			}
		}
		
		//处理死亡人数的数据
		public void handleDead(String line) {
			if(Pattern.matches("(\\S+) 死亡 (\\d+)人", line)) {
				String pattern1="(\\S+) 死亡 (\\d+)人";
				Pattern r1 = Pattern.compile(pattern1);
				Matcher m1=r1.matcher(line);
				if(m1.find()) {
					data[PROVINCE_STR.indexOf(m1.group(1))][3]+=Integer.parseInt(m1.group(2));
					data[0][3]+=Integer.parseInt(m1.group(2));
					data[PROVINCE_STR.indexOf(m1.group(1))][0]-=Integer.parseInt(m1.group(2));
					data[0][0]-=Integer.parseInt(m1.group(2));
				}
			}
		}
		
		//
		public void outLogLine(String outPath,List<String> province, List<String> type) throws IOException {
			File file = new File(outPath);
			//如果文件不存在，则自动生成文件；
			if(!file.exists()){
				file.createNewFile();
			}
			
			
			BufferedWriter out = new BufferedWriter(new FileWriter(file)); 
			
			//排序  按照拼音顺序进行排序
			Collections.sort(this.province, new Comparator<String>() {  
				@Override  
	        	public int compare(String o1, String o2) {  
					Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);  
					return com.compare(o1, o2);  
					}  
			});
			if(!province_exist) {
				out.write(PROVINCE_STR.get(0)+" ");
				for(String s:type) {
					int patientIndex=getPatientIndex(s);
					out.write(PATIENTS_STATUS.get(patientIndex)+data[0][patientIndex]+"人 ");
				}
				out.write("\r\n");
				
			}
			int provinceIndex;
			for(String proStr:province) {
				provinceIndex=getProvinceIndex(proStr);
				//System.out.println(proStr);
				out.write(proStr+" ");// \r\n即为换行  
				for(String patStr:type) {
					int patientIndex=getPatientIndex(patStr);
					//System.out.println(patientIndex);
					//System.out.println(provinceIndex);
					out.write(PATIENTS_STATUS.get(patientIndex)+data[provinceIndex][patientIndex]+"人 ");
				}
				out.write("\r\n");
			}
	        out.flush(); 
	        out.close(); 
		}
		
		//执行命令
		public void execute() {
			/*if(isTrueCommand()) {
			/*System.out.println(date);
			System.out.println(logPath);
			System.out.println(outPath);
			for(String s:type) {
				System.out.print(s);
			}
			for(String s :args) {
				System.out.println(s);
			}*/
			/*try {
				readFromLogPath(logPath);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			
			System.out.println(data[0][0]+" "+data[0][1]+" "+data[0][2]+" "+data[0][3]);
			System.out.println(data[4][0]+" "+data[4][1]+" "+data[4][2]+" "+data[4][3]);
			System.out.println(data[13][0]+" "+data[13][1]+" "+data[13][2]+" "+data[13][3]);*/
			list(logPath,outPath,date,province,type);
		}
	}
}
