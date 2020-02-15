/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
/*省份相关信息*/
class Province{
	String name;
	public int ip,sp,cure,dead;
	//感染患者，疑似患者，治愈，死亡患者
}


class InfectStatistic{
	private static final int PROVINCE_NUM = 32;
	private static final String PROVINCE[] ={"全国","安徽","北京","重庆","福建","甘肃","广东","广西","贵州",
				"海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏","青海",
				"山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"
	};
	
	
    public static void main(String[] args)throws IOException{
    	String log = null,out = null,date = null;//记录LOG文件位置，输出位置，输出日期
    	String[] content = new String[1];
    	content[0] = new String("");
    	boolean[] type = new boolean[5];//type[0]用于记录是否有-type命令行参数，1-4用于记录参数种类
    	boolean[] prov = new boolean[PROVINCE_NUM+1];//boolean[PROVINCE_NUM]用于记录是否有输入-province参数，前面的部分记录参数种类
    	
    	Province[] province = new Province[PROVINCE_NUM];
    	initProvince(province);//初始化省份信息
    	
    	if(args.length > 0 && args[0].equals("list")){
    		for(int i = 1;i+1 < args.length;i++){
    			if(args[i].substring(0,1).equals("-")){
    				switch(args[i]){
    					case "-log" :log = args[i+1];break;
    					case "-out" :out = args[i+1];break;
    					case "-date" :date = args[i+1];break;
    					case "-type" :
    						while(i+1 < args.length && !args[i+1].substring(0,1).equals("-")  && !args[i+1].equals(null)){
    							switch(args[i+1]){
    								case "感染患者" :type[0]=true;type[1] = true;break;
    								case "疑似患者" :type[0]=true;type[2] = true;break;
    								case "治愈" :type[0]=true;type[3] = true;break;
    								case "死亡" :type[0]=true;type[4] = true;break;
    							}
    							i++;
    						}
    						break;
    					case "-province" :
    						while(i+1 < args.length && !args[i+1].substring(0,1).equals("-")  && !args[i+1].equals(null)){
    							for(int j = 0;j < PROVINCE_NUM;j++){
    								if(args[i+1].equals(province[j].name)){
    									prov[PROVINCE_NUM] = true;
    									prov[j] = true;
    									j = 32;
    								}
    							}
    							i++;
    						}
    						break;
    				}
    			}
    		}
    		
    		if(log == null || out == null){
    			System.out.println("没有输入log或没有输入out，请重新输入");
    		}
    		else{
    			initProvince(province);
    			List<File> files = searchFiles(new File(log),".log.txt");
    			for(File file :files){
    				String[] file1 = file.getAbsolutePath().split("\\\\");
    				if(date == null || timeCompare(date,file1[file1.length-1].substring(0,10)) > 0){
    					update(file.getAbsolutePath(),province);
    				}
    				
                }
  			}
  			
    		if(!type[0]){//没有输入-type参数时所有类型人员都输出
    			type[1] = true;
				type[2] = true;
				type[3] = true;
				type[4] = true;
    		}
    		
  			for(int i = 1;i < PROVINCE_NUM;i++){
  				province[0].ip += province[i].ip;
  				province[0].sp += province[i].sp;
  				province[0].cure += province[i].cure;
  				province[0].dead += province[i].dead;
  			}//计算全国疫情
  			
  			writeContent(prov,type,province,content);
  			
  			content[0] = content[0] + "// 该文档并非真实数据，仅供测试使用";
  			write(out,content[0]);
  			  	
    	}
    }

    
    /*初始化省份信息*/
    private static void initProvince(Province province[]){
    	for(int i = 0;i < PROVINCE_NUM;i++){
    		province[i] = new Province();
    		province[i].name = PROVINCE[i];
    	}
    	
    }
    
    
    /*读取Log文件后更新省份信息变动*/
	private static void update(String log,Province province[])throws IOException{
		String strLine;
		FileInputStream fstream = new FileInputStream(new File(log));
		InputStreamReader isr = new InputStreamReader(fstream,"UTF-8");
		BufferedReader br = new BufferedReader(isr);//构造一个BufferedReader，里面存放在控制台输入的字节转换后成的字符。
		
		while((strLine = br.readLine()) != null){
			String str[] = strLine.split(" ");//用空格分隔每一行中的记录
		  	if(!str[0].substring(0,2).equals("//")){//忽略以"//"开头的记录行
				int num = 0;//记录变动人数
				int i = 1;//记录变动省份
				int length = str.length;
				str[length - 1] = str[length - 1].trim();
				if(str[length - 1] != null && !"".equals(str[length - 1])){
					for(int k = 0;k < str[length - 1].length();k++){
						if(str[length - 1].charAt(k) >= 48 && str[length - 1].charAt(k) <= 57){
							num = 10*num+str[length - 1].charAt(k) - 48;
						}
					}
				}
				
				while(!str[0].equals(province[i].name) && i < PROVINCE_NUM){
					i++;
				}
				
				if(length == 3){
					if(str[1].equals("治愈")){//xx省份患者治愈
						province[i].ip -= num;
						province[i].cure += num;
					}
					else if(str[1].equals("死亡")){//xx省份患者死亡
						province[i].ip -= num;
						province[i].dead += num;
					}	
				}
				else if(length == 4){
					if(str[1].equals("新增")){
						if(str[2].equals("感染患者")){//xx省份新增感染患者
							province[i].ip += num;
						}
						else if(str[2].equals("疑似患者")){//xx省份新增疑似患者
							province[i].sp += num;
						}
					}
					else if(str[1].equals("疑似患者")){////xx省份疑似患者确诊
						province[i].sp -= num;
						province[i].ip += num;
					}
					else if(str[1].equals("排除")){//xx省份疑似患者排除
						province[i].sp -= num;
					}
				}
				else if(length == 5){
					int j = 1;
					while(!str[3].equals(province[j].name) && j < PROVINCE_NUM){
						j++;
					}
					if(str[1].equals("疑似患者")){//a省疑似患者流入b省
						province[i].sp -= num;
						province[j].sp += num;
					}
					else if(str[1].equals("感染患者")){//a省感染患者流入b省
						province[i].ip -= num;
						province[j].ip += num;
					}
				}
		  	}
		}
	}
	
	
	/*写TXT文件保存到指定位置*/
	public static void write(String out,String content){    
		FileOutputStream fstream = null;
		File file = new File(out);
			try{
				if(file.exists()){
					file.createNewFile();
				}
				fstream = new FileOutputStream(file);
				fstream.write(content.getBytes());
				fstream.flush();
				fstream.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
	}
	
	
	/*查找对应目录下的文件*/
	public static List<File> searchFiles(File folder,String keyWord){
		List<File> result = new ArrayList<File>();
		if(folder.isFile()){
			result.add(folder);
		}
		
		File[] subFolders = folder.listFiles(new FileFilter(){
			public boolean accept(File file){
				if(file.isDirectory()){
					return true;
				}
				if(file.getName().toLowerCase().endsWith(keyWord)){
					return true;
				}
				return false;
			}
		});
 
        if(subFolders != null){
        	for(File file :subFolders){
        		if(file.isFile()){
                    result.add(file);
                } 
            }
        }
        return result;
    }
	
	
	/*比较两个时间前后*/
	public static int timeCompare(String time1,String time2){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c1=Calendar.getInstance();
		Calendar c2=Calendar.getInstance();
		
		try{
			c1.setTime(formatter.parse(time1));
			c2.setTime(formatter.parse(time2));
		}
		catch(ParseException e){
			e.printStackTrace();
		}
		
		int result=c1.compareTo(c2);
		return result;
	}
	
	
	/*生成输入文件的content内容*/
	public static void writeContent(boolean[] prov,boolean[] type,Province[] province,String[] content){
			if(!prov[PROVINCE_NUM]){
				for(int i = 0;i < PROVINCE_NUM;i++){
	 				if(province[i].ip != 0 || province[i].sp != 0 || province[i].cure != 0 || province[i].dead != 0){
	 					content[0] = content[0] + province[i].name + " ";
	 					if(type[1]){
	 						content[0] = content[0] + "感染患者" + province[i].ip + "人" + " ";
	 					}
	 					if(type[2]){
	 						content[0] = content[0] + "疑似患者" + province[i].sp + "人" + " ";
	 					}
	 					if(type[3]){
	 						content[0] = content[0] + "治愈" + province[i].cure + "人" + " ";
	 					}
	 					if(type[4]){
	 						content[0] = content[0] + "死亡" + province[i].dead + "人";
	 					}  	  		    			
	  		    		content[0] = content[0] + "\n";
	  				}
	  			}//插入各省份疫情
			}
			else{
				for(int i = 0;i < PROVINCE_NUM;i++){
	 				if(prov[i]){
	 					content[0] = content[0] + province[i].name + " ";
	 					if(type[1]){
	 						content[0] = content[0] + "感染患者" + province[i].ip + "人" + " ";
	 					}
	 					if(type[2]){
	 						content[0] = content[0] + "疑似患者" + province[i].sp + "人" + " ";
	 					}
	 					if(type[3]){
	 						content[0] = content[0] + "治愈" + province[i].cure + "人" + " ";
	 					}
	 					if(type[4]){
	 						content[0] = content[0] + "死亡" + province[i].dead + "人";
	 					}  	  		    			
	  		    		content[0] = content[0] + "\n";
	  				}
	  			}//插入各省份疫情
			}
	}
}