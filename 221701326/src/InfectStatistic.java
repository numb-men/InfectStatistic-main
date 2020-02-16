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
	
	public int getmark1(String province) {
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
	String patterns[]= {"","",};
	
			
}
