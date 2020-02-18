/**
 * InfectStatistic
 * TODO
 *
 * @author _LMG
 * @version 1.0
 * @since 2020/2/18
 */
package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;

class InfectStatistic {
    //命令执行参数初始化
	public static String logc = new String();
	public static String outc = new String();
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static String datec = new String("0000-00-00");
	static String dateFormat = sdf.format(new Date());
	public static String []typec = new String[4];
	public static String []provincec = new String[32];
	
	//省份数量和类型数量
	public static int typenum = 4;
	public static int provincenum = 32;
	//总人数
	public static int [][] totaltype = new int [provincenum][typenum];
	
	//存放省信息
    static Map pmap = new HashMap();
    static String [] prov= {"全国","安徽","北京","重庆","福建",
    		"甘肃","广东"," 广西","贵州"," 海南",
    		"河北","河南","黑龙江","湖北","湖南",
    		"吉林","江苏","江西","辽宁","内蒙古",
    		"宁夏","青海","山东","山西","陕西","上海",
    		"四川","天津","西藏","新疆","云南","浙江"};
    
    //存放患者类型信息
    static Map tmap = new HashMap();
    static String [] typeName = {"感染患者","疑似患者","治愈患者","死亡患者"};
    static String [] typeId = {"ip","sp","cure","dead"};
	
    //初始化map信息
    public static void InitMessage() {
    	for(int i = 0;i < prov.length;i++) {
    		pmap.put(prov[i], Integer.valueOf(i));
    	}
    	for(int i = 0;i < typeId.length;i++) {
    		tmap.put(typeId[i],Integer.valueOf(i));
    	}
    	for(int i = 0;i < provincenum;i++) {
    		for(int j = 0;j <typenum;j++) 
    			totaltype[i][j] = 0;
    	}
    }
    
    //分析整个命令行给全局变量赋值
    public static void CmdAnalyze(String s[]) {
    	int len = s.length;
    	int a,b,c,d;
    	a = b = c = d = 0;
    	String c1 = new String("-log");
    	String c2 = new String("-out");
    	String c3 = new String("-date");
    	String c4 = new String("-type");
    	String c5 = new String("-province");
    	for(int i = 0;i < 4;i++) typec[i] = "undefined";
    	for(int i = 0;i < 32;i++) provincec[i] = "undefined";
    	for(int i = 1;i < len;i++) {
    		if(s[i].equals(c1)) {
    			logc = s[i+1];
    		}
    		else if(s[i].equals(c2)) {
    			outc = s[i+1];
    		}
    		else if(s[i].equals(c3)){
    			datec = s[i+1];
    		}
    		else if(s[i].equals(c4)) {
    			a = i;
    			for(int j = a + 1;j < len;j++) {
    				if(s[j].charAt(0) != '-') {
    					typec[b++] = s[j];
    				}
    				else break;
    			}
    		}
    		else if(s[i].equals(c5)) {
    			c = i;
    			for(int k = c + 1;k < len;k++) {
    				if(s[k].charAt(0)!='-') {
    					provincec[d++] = s[k];
    				}
    				else break;
    			}
    		}
    		
    	}
    }
    
    //读取文件内容
    public static ArrayList<String> ReadTxt(File file) {
    	ArrayList<String> as = new ArrayList<String>();
    	StringBuilder result = new StringBuilder();
    	try {
    		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
    		String s = null;
    		while ((s = br.readLine()) != null) {
    			as.add(s);
    		}
    		br.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return as;
    }
    
    //获取指定日期所有文件名
    public static ArrayList<String> GetTxt(String path,String date){
    	//先获取指定日期之前的所有文件名
    	File file1 = new File(path);
        File[] templist = file1.listFiles();
        ArrayList<String> ls1 = new ArrayList<String>();
        ArrayList<String> ls2 = new ArrayList<String>();
        for(int i = 0 ; i < templist.length;i++){
			if(templist[i].isFile()) {
				ls1.add(templist[i].toString());
			}
			File file2 = new File(ls1.get(i).trim());
			String fileName = file2.getName().substring(0,10);
			if(datec.compareTo(fileName) >= 0) {
				ls2.add(ls1.get(i));
			}
        }
        return ls2;
    }
    //执行命令
    public static void ExcuteCommand(String log,String out,String date,String []type,String []province) throws IOException{
    	//正则表达式匹配文件内容
		String smatch1 = "^[\\u4e00-\\u9fa5]*\\s(新增)\\s(感染患者)\\s(\\d+)人?";
		String sorigin1 = " 新增 感染患者 |人";
		String smatch2 = "^[\\u4e00-\\u9fa5]*\\s(新增)\\s(疑似患者)\\s(\\d+)人?";
		String sorigin2 = " 新增 疑似患者 |人";
		String smatch3 = "^[\\u4e00-\\u9fa5]*\\s(感染患者)\\s(流入)\\s[\\u4e00-\\u9fa5]*\\s(\\d+)人?";
		String sorigin3 = " 感染患者 流入 | |人";
		String smatch4 = "^[\\u4e00-\\u9fa5]*\\s(疑似患者)\\s(流入)\\s[\\u4e00-\\u9fa5]*\\s(\\d+)人?";
		String sorigin4 = " 疑似患者 流入 | |人";
		String smatch5 = "^[\\u4e00-\\u9fa5]*\\s(死亡)\\s(\\d+)人?";
		String sorigin5 = " 死亡 |人";
		String smatch6 = "^[\\u4e00-\\u9fa5]*\\s(治愈)\\s(\\d+)人?";;
		String sorigin6 = " 治愈 |人";
		String smatch7 = "^[\\u4e00-\\u9fa5]*\\s(疑似患者)\\s(确诊感染)\\s(\\d+)人?";
		String sorigin7 = " 疑似患者 确诊感染 |人";
		String smatch8 = "^[\\u4e00-\\u9fa5]*\\s(排除)\\s(疑似患者)\\s(\\d+)人?";
		String sorigin8 = " 排除 疑似患者 |人";
    
		//读入文件路径
		ArrayList<String> filePath = GetTxt(log,date);
		for(int i=0;i<filePath.size();i++) {
			BufferedReader inBufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(filePath.get(i)), "UTF-8"));
			String es;
			while((es=inBufferedReader.readLine())!=null) {
				//A 新增 感染患者 x人
				if(es.matches(smatch1)) {
					String [] s1 = es.split(sorigin1);
					totaltype[(Integer) pmap.get(s1[0])][0] += Integer.parseInt(s1[1]);
				}
				//A 新增 疑似患者 x人
				else if(es.matches(smatch2)) {
					String [] s2 = es.split(sorigin2);
					totaltype[(Integer) pmap.get(s2[0])][1] += Integer.parseInt(s2[1]);
				}
				//A 感染患者 流入 B x人
				else if(es.matches(smatch3)) {
					String [] s3 = es.split(sorigin3);
					totaltype[(Integer) pmap.get(s3[0])][0] -= Integer.parseInt(s3[2]);
					totaltype[(Integer) pmap.get(s3[1])][0] += Integer.parseInt(s3[2]);
				}
				//A 疑似患者 流入 B x人
				else if(es.matches(smatch4)) {
					String [] s4 = es.split(sorigin4);
					totaltype[(Integer) pmap.get(s4[0])][1] -= Integer.parseInt(s4[2]);
					totaltype[(Integer) pmap.get(s4[1])][1] += Integer.parseInt(s4[2]);
				}
				//A 死亡 x人
				else if(es.matches(smatch5)) {
					String [] s5 = es.split(sorigin5);
					totaltype[(Integer) pmap.get(s5[0])][3] += Integer.parseInt(s5[1]);
					totaltype[(Integer) pmap.get(s5[0])][0] -= Integer.parseInt(s5[1]);
				}
				//A 治愈 x人
				else if(es.matches(smatch6)) {
					String [] s6 = es.split(sorigin6);
					totaltype[(Integer) pmap.get(s6[0])][2] += Integer.parseInt(s6[1]);
					totaltype[(Integer) pmap.get(s6[0])][0] -= Integer.parseInt(s6[1]);
				}
				//a 疑似患者 确诊感染 x人
				else if(es.matches(smatch7)) {
					String [] s7 = es.split(sorigin7);
					totaltype[(Integer) pmap.get(s7[0])][0] += Integer.parseInt(s7[1]);
					totaltype[(Integer) pmap.get(s7[0])][1] -= Integer.parseInt(s7[1]);
				}
				//A 排除 疑似患者 x人
				else if(es.matches(smatch8)) {
					String [] s8 = es.split(sorigin8);
					totaltype[(Integer) pmap.get(s8[0])][1] -= Integer.parseInt(s8[1]);
				}
			}
		}
    }
    
    //打印信息
    public static void PrintTxt(String log,String out,String date,String [] type,String [] province) throws IOException {
    	File file=new File(out);
    	if(!file.exists()) {
    		file.createNewFile();
    	}
    	PrintStream ps=new PrintStream(out);
    	//System.setOut(ps);
    	for(int i=1;i<32;i++) 
    		for(int j=0;j<4;j++) totaltype[0][j]+=totaltype[i][j];
    	//System.out.println('1');
    	for(int i=0;i<prov.length;i++) {
        	for(int j=0;j<province.length;j++) if(prov[i].equals(province[j])){
        		System.out.print(prov[i]+" ");
        		Integer pronum=(Integer) pmap.get(prov[i]);
        		int typecnt=0;
        		for(int k=0;k<4;k++) {
        			if(type[k].equals("undefined")) typecnt++;
        		}
        		for(int k=0;k<4;k++) if((typecnt==4)||(!type[k].equals("undefined"))){
        			Integer tynum=(Integer) tmap.get(type[k]);
        			System.out.print(typeName[tynum]+totaltype[pronum][tynum]+" ");
        		}
        		System.out.println();
        	}
    	}
    }
    
    public static void main(String[] args) throws IOException {
    	InitMessage();
    	CmdAnalyze(args);
    	/*for(String s : args)
    		//System.out.println(s);
    	System.out.println(logc);
    	System.out.println(outc);
    	for(String s : typec)
    		System.out.println(s);
    	for(String s : provincec)
    		System.out.println(s);*/
    	ExcuteCommand(logc,outc,datec,typec,provincec);
    	PrintTxt(logc,outc,datec,typec,provincec);
    	for(int i = 0;i < provincenum;i++) {
    		for(int j = 0;j < typenum;j++) {
    			System.out.println(totaltype[i][j]);
    		}
    	}
    }
}
