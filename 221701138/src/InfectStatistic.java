/**
 * InfectStatistic
 * TODO
 *
 * @author _LMG
 * @version xxx
 * @since xxx
 */
package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
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
	int typenum = 4;
	int provincenum = 32;
	//总人数
	public int [][] totaltype = new int [provincenum][typenum];
	
	//存放省信息
    Map pmap = new HashMap();
    String [] prov= {"全国","安徽","北京","重庆","福建",
    		"甘肃","广东"," 广西","贵州"," 海南",
    		"河北","河南","黑龙江","湖北","湖南",
    		"吉林","江苏","江西","辽宁","内蒙古",
    		"宁夏","青海","山东","山西","陕西","上海",
    		"四川","天津","西藏","新疆","云南","浙江"};
    
    //存放患者类型信息
    Map tmap = new HashMap();
    String [] typeName = {"感染患者","疑似患者","治愈患者","死亡患者"};
    String [] typeId = {"ip","sp","cure","dead"};
	
    //初始化信息
    public void InitMessage() {
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
    	for(int i=0;i<4;i++) typec[i]="undefined";
    	for(int i=0;i<31;i++) provincec[i]="undefined";
    	for(int i = 1;i < len;i++) {
    		if(s.equals("c1")) {
    			logc = s[i+1];
    		}
    		else if(s.equals("c2")) {
    			outc = s[i+1];
    		}
    		else if(s.equals("c3")){
    			datec = s[i+1];
    		}
    		else if(s.equals("c4")) {
    			a = i;
    			for(int j = a + 1;j < len;j++) {
    				if(s[i].charAt(0)!='-') {
    					typec[b++] = s[i];
    				}
    				else break;
    			}
    		}
    		else if(s.equals("c5")) {
    			c = i;
    			for(int k = c + 1;k < len;k++) {
    				if(s[i].charAt(0)!='-') {
    					provincec[d++] = s[i];
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
    
    //返回所有文件内容
    public static ArrayList<String> GetTxt(String path,String date){
    	File file1 = new File(path);
        File templist[] = file1.listFiles();
        ArrayList<String> ls = new ArrayList<String>();
        ArrayList<String> lt = new ArrayList<String>();
        for(int i = 0 ; i < templist.length;i++){
			if(templist[i].isFile()) {
				ls.add(templist[i].toString());
			}
        }
        for(String s : ls) {
        	File file2 = new File(s);
        	lt=ReadTxt(file2);
        	for(int j = 0 ; j < lt.size();j++){	
    			//System.out.print(lt.get(j)+"\n");
    		}
        }
        return lt;
    }
    //执行命令
    public static void ExcuteCommand(String log,String out,String date,String []type,String []province){
    	ArrayList<String> lt = GetTxt(log,date);
    	
    }
    
    public static void main(String[] args) {
    	
    	System.out.println("1");
    }
}
