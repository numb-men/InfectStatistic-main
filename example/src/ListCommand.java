import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;

//对应于Concentrate

public class ListCommand implements Command{
	//常量定义
	private static final String[] PATIENTS_TYPE= {"ip","sp","cure","dead"};
	private static final String[] PROVINCE_STR= {"全国", "安徽", "澳门" ,"北京", "重庆", "福建","甘肃","广东", "广西", "贵州", "海南",
								"河北", "河南", "黑龙江", "湖北", "湖南", "吉林","江苏", "江西", "辽宁", "内蒙古", "宁夏", 
								"青海", "山东", "山西", "陕西", "上海","四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江"};
	//private String[] pattern={""}
	
	public String[] args;
	public String logPath;
	public String outPath;
	public String date;
	public List<String> type=new ArrayList<String>();
	
	public boolean log_exist=false;
	public boolean out_exist=false;
	
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
			if(args[i].toLowerCase().equals("-date")&&i<argsLength-1)
				date=args[i+1];
			if(args[i].toLowerCase().equals("-type")) {
				int j=i+1;
				type.clear();
				while(j<args.length&&!args[j].startsWith("-")) {
					type.add(args[j]);
					j++;
				}
			}
		}  
	}
	
	
	//判断是否为合法的命令格式
	public boolean isTrueCommand() {
		if(!args[0].toLowerCase().equals("list")) return false;
		if(!log_exist||!out_exist) return false;
		
		else return true;
	}
	
	//命令的参数化函数
	public void list(String logPath, String outPath, String date, List<String> province, List<String> type) {
		
	}
	
	//
	public void readFromLogPath(String logPath) throws FileNotFoundException {
		File file = new File(this.logPath);
		 File [] files = file.listFiles();
		 String[] names = file.list();
		 if(names != null) {
			 String [] completNames = new String[names.length];
			 for(int i = 0;i < names.length;i++) {
				 String fileDate=(String) names[i].subSequence(0, 10);
				 if(date.compareTo(fileDate)>0) {
				 completNames [i] = this.logPath+names[i];
				 System.out.println(completNames [i]);
				 readFromFile(completNames[i]);
				 }
			 }
		 }
	}
	
	//
	public void readFromFile(String filePath) throws FileNotFoundException {
        String[] bufstring=new String[1024];
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		int position=0;
		String line=null;
		try {
			while((line=br.readLine())!=null) {
				bufstring[position]=line;
				System.out.println(bufstring[position]);
				eachLine(line);
				position++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<position;i++) {
			
		}
		
	}
	
	//处理每行的数据
	public void eachLine(String line) {
		if(Pattern.matches("(\\S+) 新增 感染患者 (\\d+)人", line)){
			handleIp(line);
		}
		if(Pattern.matches("(\\S+) 新增 疑似患者 (\\d+)人", line)) {
			handleSp(line);
		}
		
		if(Pattern.matches("(\\S+) 感染患者 流入 (\\S+) (\\d+)人", line)) {
			handleIp(line);
		}
		if(Pattern.matches("(\\S+) 疑似患者 流入 (\\S+) (\\d+)人", line)) {
			handleSp(line);
		}
		
		if(Pattern.matches("(\\S+) 治愈 (\\d+)人", line)) {
			handleCure(line);
		}
		
		if(Pattern.matches("(\\S+) 死亡 (\\d+)人", line)) {
			handleDead(line);
		}
		
		if(Pattern.matches("(\\S+) 疑似患者 确诊感染 (\\d+)人", line)) {
			handleSp(line);
		}
		if(Pattern.matches("(\\S+) 排除 疑似患者 (\\d+)人", line)) {
			handleSp(line);
		}
	}
	
	public void handleIp(String line) {
		if(Pattern.matches("(\\S+) 新增 感染患者 (\\d+)人", line)) {
			String pattern1="(\\S+) 新增 感染患者 (\\d+)人";
			Pattern r1 = Pattern.compile(pattern1);
			Matcher m1=r1.matcher(line);
			if(m1.find()) {
				System.out.println(m1.group(2));
			}
		}
		
		if(Pattern.matches("(\\S+) 感染患者 流入 (\\S+) (\\d+)人", line)) {
			String pattern2="(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
			Pattern r2 = Pattern.compile(pattern2);
			Matcher m2=r2.matcher(line);
			if(m2.find()) {
				System.out.println(m2.group(3));
			}
		}
	}
	
	public void handleSp(String line) {
		if(Pattern.matches("(\\S+) 新增 疑似患者 (\\d+)人", line)) {
			String pattern1="(\\S+) 新增 疑似患者 (\\d+)人";
			Pattern r1 = Pattern.compile(pattern1);
			Matcher m1=r1.matcher(line);
			if(m1.find()) {
				System.out.println(m1.group(2));
			}
		}
		
		if(Pattern.matches("(\\S+) 疑似患者 流入 (\\S+) (\\d+)人", line)) {
			String pattern2="(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
			Pattern r2 = Pattern.compile(pattern2);
			Matcher m2=r2.matcher(line);
			if(m2.find()) {
				System.out.println(m2.group(3));
			}
		}
		
		if(Pattern.matches("(\\S+) 疑似患者 确诊感染 (\\d+)人", line)) {
			String pattern3="(\\S+) 疑似患者 确诊感染 (\\d+)人";
			Pattern r3 = Pattern.compile(pattern3);
			Matcher m3=r3.matcher(line);
			if(m3.find()) {
				System.out.println(m3.group(2));
			}
		}
		
		if(Pattern.matches("(\\S+) 排除 疑似患者 (\\d+)人", line)) {
			String pattern4="(\\S+) 排除 疑似患者 (\\d+)人";
			Pattern r4 = Pattern.compile(pattern4);
			Matcher m4=r4.matcher(line);
			if(m4.find()) {
				System.out.println(m4.group(2));
			}
		}
	}
	
	public void handleCure(String line) {
		if(Pattern.matches("(\\S+) 治愈 (\\d+)人", line)) {
			String pattern1="(\\S+) 治愈 (\\d+)人";
			Pattern r1 = Pattern.compile(pattern1);
			Matcher m1=r1.matcher(line);
			if(m1.find()) {
				System.out.println(m1.groupCount());
			}
		}
	}
	
	public void handleDead(String line) {
		if(Pattern.matches("(\\S+) 死亡 (\\d+)人", line)) {
			String pattern1="(\\S+) 死亡 (\\d+)人";
			Pattern r1 = Pattern.compile(pattern1);
			Matcher m1=r1.matcher(line);
			if(m1.find()) {
				System.out.println(m1.group(2));
			}
		}
	}
	
	//执行命令
	public void execute() {
		if(isTrueCommand()) {
		/*System.out.println(date);
		System.out.println(logPath);
		System.out.println(outPath);
		for(String s:type) {
			System.out.print(s);
		}
		for(String s :args) {
			System.out.println(s);
		}*/
		try {
			readFromLogPath(logPath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
}
