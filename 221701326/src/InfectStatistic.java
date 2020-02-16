/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.*;

public class InfectStatistic {
	public static void main(String args[]) {
			try {
				@SuppressWarnings("resource")
				BufferedReader in = new BufferedReader(new FileReader("D:\\log.txt"));
	            String str=null;
	            int i=0;
	            while ((str = in.readLine())!= null) {
	                i++;
	                
	                System.out.print("第"+i+"行："+str+"\n");	                	 
	                String pattern = ".*治愈.*";
	                String[]  strs=str.split(" ");
	                boolean isMatch = Pattern.matches(pattern, str);
	                System.out.println("字符串中是否包含了 子字符串? " + isMatch);
	                System.out.println(strs[2].toString());
	                
	                
	                
	            }
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			try {
					String content = "a dog will be write in file";
					File file = new File("D:\\result.txt");
					if(!file.exists()){
						file.createNewFile();
					}
					FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fileWriter);
					bw.write(content);
					bw.close();
					System.out.println("finish");
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
	}
}
class province{
	 String provinces[]= {"全国","安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北","河南",
		            	"黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏","青海","山东",
		            	"山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"};
	
	public int getmark(String province) {
		int mark=0;
		for(mark=0;mark<provinces.length;mark++)
			if(provinces[mark]==province)
				break;
		return mark;
	}
		
	public static void main(String args[]) {
		/*for(int i=0;i<conditions.length;i++)
			for(int j=0;j<4;j++)
		    System.out.println("人数为"+conditions[i][j]);
		province p=new province();
		int num=p.getmark1("福建");
		System.out.println("代号为"+num);*/
		
	}
	
}
class state{
	String pattern1= ".*新增 感染患者.*";
	String pattern2= ".*新增 疑似患者.*";
	String pattern3=".*治愈.*";
	String pattern4=".*死亡.*";	
	String pattern5= ".*感染患者 流入.*";
	String pattern6= ".*疑似患者 流入.*";	
	String pattern7=".*疑似患者 确诊感染.*";
	String pattern8=".*排除 疑似患者.*";
	public int judgetxtline(String textstr) {	
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
	public int judgeprovince(String str){
		province a=new province();
		int num=a.getmark(str);
		return num;
	}
	public void conditions(String textstr) {
		String strs[]=textstr.split(" ");
		String conditions[][]=new String[32][4];
		int choice=this.judgetxtline(textstr);
	}
    /*System.out.println(strs[2].toString());*/ 
	public static void main(String args[]) {
		String list1[][]=new String[2][3];
		for(int i=0;i<list1.length;i++) 
			for(int j=0;j<3;j++)
				list1[i][j]="4人";
		for(int i=0;i<list1.length;i++) 
			for(int j=0;j<3;j++)
				list1[i][j]+="2人";
		System.out.println(list1[1][1]);
			
	}
	
}
