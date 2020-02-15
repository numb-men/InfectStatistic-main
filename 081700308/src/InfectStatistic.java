import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
		
		static String[] provinces= {
				"全国",
				"安徽",
				"北京",
				"重庆",
				"福建",
				"甘肃",
				"广东",
				"广西",
				"贵州",
				"海南",
				"河北",
				"河南",
				"黑龙江",
				"湖北",
				"湖南",
				"吉林",
				"江苏",
				"江西",
				"辽宁",
				"内蒙古",
				"宁夏",
				"青海",
				"山东",
				"山西",
				"陕西",
				"上海",
				"四川",
				"天津",
				"西藏",
				"新疆",
				"云南",
				"浙江"};
		
        int[] ip=new int[32];
		int[] sp=new int[32];
		int[] cure=new int[32];
		int[] dead=new int[32];
		int[] exist=new int[32];
		int[] required=new int[32];
		InfectStatistic()
		{
			exist[0]=1;
		}

		public  int getIp(int n)
		{
			return ip[n];
		}
		
		
		
		//省份对应下标
		public int proToInt(String p)
		{
			switch(p)
			{
			    case "全国" :
			    	return 0;
			    case "安徽" :
			    	return 1;
			    case "北京" :
			    	return 2;
			    case "重庆" :
			    	return 3;
			    case "福建" :
			    	return 4;
			    case "甘肃" :
			    	return 5;
			    case "广东" :
			    	return 6;
			    case "广西" :
			    	return 7;
			    case "贵州" :
			    	return 8;
			    case "海南" :
			    	return 9;
			    case "河北" :
			    	return 10;
			    case "河南" :
			    	return 11;
			    case "黑龙江" :
			    	return 12;
			    case "湖北" :
			    	return 13;
			    case "湖南" :
			    	return 14;
			    case "吉林" :
			    	return 15;
			    case "江苏" :
			    	return 16;
			    case "江西" :
			    	return 17;
			    case "辽宁" :
			    	return 18;
			    case "内蒙古" :
			    	return 19;
			    case "宁夏" :
			    	return 20;
			    case "青海" :
			    	return 21;
			    case "山东" :
			    	return 22;
			    case "山西" :
			    	return 23;
			    case "陕西" :
			    	return 24;
			    case "上海" :
			    	return 25;
			    case "四川" :
			    	return 26;
			    case "天津" :
			    	return 27;
			    case "西藏" :
			    	return 28;
			    case "新疆" :
			    	return 29;
			    case "云南" :
			    	return 30;
			    case "浙江" :
			    	return 31;
			}
			return 0;
		}
		
		//下标对应省份
		public String intToPro(int n)
		{
			switch(n)
			{
			    case 0 :
			    	return "全国";
			    case 1 :
			    	return "安徽";
			    case 2 :
			    	return "北京";
			    case 3 :
			    	return "重庆";
			    case 4 :
			    	return "福建";
			    case 5 :
			    	return "甘肃";
			    case 6 :
			    	return "广东";
			    case 7 :
			    	return "广西";
			    case 8 :
			    	return "贵州";
			    case 9 :
			    	return "海南";
			    case 10 :
			    	return "河北";
			    case 11 :
			    	return "河南";
			    case 12 :
			    	return "黑龙江";
			    case 13 :
			    	return "湖北";
			    case 14 :
			    	return "湖南";
			    case 15 :
			    	return "吉林";
			    case 16 :
			    	return "江苏";
			    case 17 :
			    	return "江西";
			    case 18 :
			    	return "辽宁";
			    case 19 :
			    	return "内蒙古";
			    case 20 :
			    	return "宁夏";
			    case 21 :
			    	return "青海";
			    case 22 :
			    	return "山东";
			    case 23 :
			    	return "山西";
			    case 24 :
			    	return "陕西";
			    case 25 :
			    	return "上海";
			    case 26 :
			    	return "四川";
			    case 27 :
			    	return "天津";
			    case 28 :
			    	return "西藏";
			    case 29 :
			    	return "新疆";
			    case 30 :
			    	return "云南";
			    case 31 :
			    	return "浙江";
			}
			return "全国";
		}
	
    	
	static String type1=".*新增 感染患者.*";
	static	String type2=".*新增 疑似患者.*";
	static	String type3=".*感染患者 流入.*";
	static	String type4=".*疑似患者 流入.*";
	static	String type5=".*死亡.*";
	static	String type6=".*治愈.*";
	static	String type7=".*疑似患者 确诊感染.*";
	static	String type8=".*排除 疑似患者.*";
		static Pattern pattern8=Pattern.compile(type8);
		Pattern pattern1=Pattern.compile("人");
		Pattern pattern2=Pattern.compile(" ");
		//统计新增感染患者
		public  void addIp(String text)
		{
			
	
			//提取句首省份
			String pro=pattern2.split(text)[0];
			//提取句末人数
			int addip=Integer.parseInt(pattern1.split(pattern2.split(text)[3])[0]);
			//对ip数组进行操作
			ip[0]+=addip;
			ip[proToInt(pro)]+=addip;
			exist[proToInt(pro)]=1;
		}
    
		public  void addSp(String text)
		{
			
			
			//提取句首省份
			String pro=pattern2.split(text)[0];
			//提取句末人数
			int addsp=Integer.parseInt(pattern1.split(pattern2.split(text)[3])[0]);
			//对sp数组进行操作
			sp[0]+=addsp;
			sp[proToInt(pro)]+=addsp;
			exist[proToInt(pro)]=1;
		}
		
		public  void transIp(String text)
		{
			
			
			//提取源省份
			String pro1= pattern2.split(text)[0];
			//提取转移省份
			String pro2= pattern2.split(text)[3];
			//提取句末人数
			int transip=Integer.parseInt(pattern1.split(pattern2.split(text)[4])[0]);
			//对ip数组操作
			ip[proToInt(pro1)]-=transip;
			ip[proToInt(pro2)]+=transip;
			exist[proToInt(pro1)]=1;
			exist[proToInt(pro2)]=1;
		}
		
		public  void transSp(String text)
		{
			//提取源省份
			String pro1= pattern2.split(text)[0];
			//提取转移省份
			String pro2= pattern2.split(text)[3];
			//提取句末人数
			int transsp=Integer.parseInt(pattern1.split(pattern2.split(text)[4])[0]);
			//对sp数组操作
			sp[proToInt(pro1)]-=transsp;
			sp[proToInt(pro2)]+=transsp;
			exist[proToInt(pro1)]=1;
			exist[proToInt(pro2)]=1;
		}
		
		public  void addDead(String text)
		{
			//提取句首省份
			String pro= pattern2.split(text)[0];
			//提取句末人数
			int newdead=Integer.parseInt(	pattern1.split(pattern2.split(text)[2])[0]);
			//对ip dead数组操作
			int d=proToInt(pro);
			dead[0]+=newdead;
			dead[d]+=newdead;
			ip[0]-=newdead;
			ip[d]-=newdead;
			exist[d]=1;
		}
		
		public  void addCure(String text)
		{
			//提取句首省份
			String pro= pattern2.split(text)[0];
			//提取句末人数
			int newcure= Integer.parseInt(	pattern1.split(pattern2.split(text)[2])[0]);
			//对ip cure数组操作
			int d=proToInt(pro);
			cure[0]+=newcure;
			cure[d]+=newcure;
			ip[0]-=newcure;
			ip[d]-=newcure;
			exist[d]=1;
		}
		
		public void spToIp(String text)
		{
			//提取句首省份
			String pro= pattern2.split(text)[0];
			//提取句末人数
			int newip= Integer.parseInt(	pattern1.split(pattern2.split(text)[3])[0]);
			//对ip sp数组操作
			int d=proToInt(pro);
			ip[0]+=newip;
			ip[d]+=newip;
			sp[0]-=newip;
			sp[d]-=newip;
			exist[d]=1;
		}
		
		public void  decSp(String text)
		{
			//提取句首省份
			String pro= pattern2.split(text)[0];
			//提取句末人数
			int decsp= Integer.parseInt( 	pattern1.split(pattern2.split(text)[3])[0]);
			//对sp数组操作
			int d=proToInt(pro);
			sp[0]-=decsp;
			sp[d]-=decsp;
			exist[d]=1;
		}

		public  void readFile(String x)
		{
			String pathname=x;
			try(FileReader reader = new FileReader(pathname);
				BufferedReader br = new BufferedReader(reader)	
			){
				String line;
				while((line=br.readLine())!=null)
				{
					if(line.matches(type1))
						addIp(line);
					else if(line.matches(type2))
						addSp(line);
					else if(line.matches(type3))
						transIp(line);
					else if(line.matches(type4))
						transSp(line);
					else if(line.matches(type5))
						addDead(line);
					else if(line.matches(type6))
						addCure(line);
					else if(line.matches(type7))
						spToIp(line);
					else if(line.matches(type8))
						decSp(line);
					else
						break;
						
						
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		public  void writeFile()
		{
			try {
				File writeName=new File("output.txt");
				writeName.createNewFile();
				try(FileWriter writer = new FileWriter(writeName);
					BufferedWriter out = new BufferedWriter(writer)
					){
					for(int i=0;i<32;i++)
					{
						if(1==exist[i])
						{
							out.write(provinces[i]+" ");
							out.write("感染患者"+ip[i]+"人 ");
							out.write("疑似患者"+sp[i]+"人 ");
							out.write("治愈"+cure[i]+"人 ");
							out.write("死亡"+dead[i]+"人 ");
							out.write("\r\n");
						    out.flush();
						}
					}
					out.write("// 该文档并非真实数据，仅供测试使用\r\n");
					out.flush();
					
				}
			}catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		public static void main(String args[ ])
		{
			 
			 String x="浙江 排除 疑似患者 7人";
			 InfectStatistic a;
			 a=new InfectStatistic();
			
			System.out.print(x.matches(type8));
			a.readFile("D:\\GitHub\\A\\InfectStatistic-main\\081700308\\log\\2020-01-22.log.txt");
			a.readFile("D:\\GitHub\\A\\InfectStatistic-main\\081700308\\log\\2020-01-23.log.txt");
			a.readFile("D:\\GitHub\\A\\InfectStatistic-main\\081700308\\log\\2020-01-27.log.txt");
			a.writeFile();
		       //System.out.print(getIp(0));
		      // System.out.println(AddIp(x));
		}// 方法main结束
		
		
	

}
