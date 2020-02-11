import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Collator;
import java.util.Arrays;

/**
 * InfectStatistic
 * TODO
 *
 * @author 衡天宇
 * @version 1.0
 * 
 */
public class array {
	static int count=0;//已有数据的条数
	
    public static void main(String[] args) throws IOException {   	
    	cmdArgs cmd=new cmdArgs(args);
    	int i=cmd.hasParam("-date");//存命令的索引
    	if(i!=-1) {//有指定日期
    		readLog(args[i+1],true);
    	}  
    }
   
static class line{//统计之后的病例每条的结构
	String location;//地理位置
	int grhz;//感染患者人数
	int yshz;//疑似患者人数
	int recover;//治愈人数
	int dead;//死亡人数
	
	line(String plocation,int pgrhz,int pyshz,int precover,int pdead){
		location=plocation;
		grhz=pgrhz;
		yshz=pyshz;
		recover=precover;
		dead=pdead;
	}
	
	line(){		
	}

	/*
              * 功能：打印一条统计疫情信息
              * 输入参数：无
              *返回值：信息字符串
    */
	String printline() {
		return(location+" 感染患者"+grhz+"人 疑似患者"+yshz+"人 治愈"+recover+"人 死亡"+dead+"人");
	}
}

static class cmdArgs {//获取使用命令
    String[] args;
    
    cmdArgs(String[] passargs) {
        args=passargs;
    }
    
    /*
                     * 功能：判断是否存在某命令
                     * 输入参数：需要查验的命令
                     *返回值：该命令的索引int值，如果没有该命令则返回-1
    */
    int hasParam(String key) {
        for(int i=0;i<args.length;i++) {
        	if(args[i].equals("key")) {
        		return i;
        	}
        }
        return -1;
    }   
}

     /*
              * 功能：判断日期的合法性
              * 输入参数：最新更新日志的时间，待验证日期字符串
              *返回值：true,false
    */
    static boolean isCorrectdate(String lastdate,String date) {
    	if(isBefore(lastdate,date)) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }
    
     /*
     * 功能：比较日期大小
     * 输入参数：两个需要比较的日期字符串
     *返回值：前<后返回true，前>后返回false
    */
    static boolean isBefore(String date1,String date2) {
    	String[] dsp1=date1.split("-");
    	String[] dsp2=date2.split("-");//分割年月日
    	if(dsp1[0].compareTo(dsp2[0])>0) {//年份过大
    		return false;
    	}
    	else if(dsp1[0].equals(dsp2[0])){//年份相等
    		if(dsp1[1].compareTo(dsp2[1])>0) {//月份过大
    			return false;
    		}
    		else if(dsp1[1].equals(dsp2[1])) {//月份相等
    			if(dsp1[2].compareTo(dsp2[2])>0) {//日期过大
    				return false;
    			}
    			else {
    				return true;
    			}
    		}
    		else {
    			return true;
    		}
    	}
    	else {
    		return true;
    	}   		
    }
    
    /*
              * 功能：获取最新日志的时间
              * 输入参数：无
              *返回值：日期字符串
    */
    static String getLastdate() {
    	String date="";
    	File file = new File("d:/log/");
        String[] filename = file.list();//获取所有日志文件名      
    	date=filename[filename.length-1].substring(0,10);    	
    	return date;
    }
    
    /*
     * 功能：获取指定日期文件在所有日志中的索引
     * 输入参数：指定日期字符串
     *返回值：索引int值
    */
    static int findPot(String date) {
    	File file = new File("d:/log/");
        String[] filename = file.list();//获取所有日志文件名      
    	for(int i=0;i<filename.length;i++) {
    		String datecut=filename[i].substring(0,10);//只获取文件名前的日期
    		if(date.equals(datecut)) {
    			return i;
    		}
    	}
    	return -1;   	
    }
        
      /*
     * 功能：读取log文件
     * 输入参数：指定的输出日期，是否指定输出日期
     *返回值：无
    */
    static void readLog(String date,boolean hasDate) throws IOException {
    	line[] all=new line[34];//初始化结果
	    for(int j=0;j<34;j++) {
	    	all[j]=new line();
	    }
    	if(hasDate==true) {
    		if(isCorrectdate(getLastdate(),date)) {//检验输入日期正确性
    			int i=0;//控制日志读取索引
    			File file = new File("d:/log/");
    			String[] filename = file.list();//获取所有日志文件名  			    
    			while(i<=findPot(date)) {   			
					FileInputStream fs=new FileInputStream("d:/log/"+filename[i]);
				    InputStreamReader is=new InputStreamReader(fs);
				    BufferedReader br=new BufferedReader(is);
				    String s="";				    
				    while((s=br.readLine())!=null){//一行一行读
					    String[] sp =s.split(" ");//分隔开的字符串
					    statistics(sp,all);          	
		    	    }
				    br.close();
		    	}
    		}
    		else {//日期不正确
    			System.out.print("输入的日期超出范围，请重新命令！");
    		}
    	}
    	else {//没输入指定日期
    		int i=0;//控制日志读取索引
			File file = new File("d:/log/");
			String[] filename = file.list();//获取所有日志文件名  
			while(i<filename.length) {   			
				FileInputStream fs=new FileInputStream("d:/log/"+filename[i]);
			    InputStreamReader is=new InputStreamReader(fs);
			    BufferedReader br=new BufferedReader(is);
			    String s="";				    
			    while((s=br.readLine())!=null){//一行一行读
				    String[] sp =s.split(" ");//分隔开的字符串
				    statistics(sp,all);          	
	    	    }
			    br.close();
	    	}   		
    	}
    	printtxt(sortline(all));
    }
       
     /*
	     * 功能：统计各地的情况
	     * 输入参数：日志里每行的字符串数组，总的记录数组
	     *返回值：无
    */
    static void statistics(String[] sp,line[] all) {   	
    	String location="";    	
    	location=sp[0];
    	line line1;
    	if(!isExistlocation(location,all)) {//不存在对应该省的记录
    		line1=new line(location,0,0,0,0);//新建数据条   		
    		all[count]=line1;
    		count++;
    	}
    	else {
    		line1=getLine(location,all);//获得原有的数据条
    	}    	
    	if(sp[1].equals("新增")) {
    		if(sp[2].equals("感染患者")) {//获得感染人数
    			line1.grhz+=Integer.valueOf(sp[3].substring(0,sp[3].length()-1));
    		}
    		else {//疑似患者
    			line1.yshz+=Integer.valueOf(sp[3].substring(0,sp[3].length()-1));
    		}
    	}
    	else if(sp[1].equals("死亡")) {
    		line1.dead+=Integer.valueOf(sp[2].substring(0,sp[2].length()-1));
    	}
    	else if(sp[1].equals("治愈")) {
    		line1.recover+=Integer.valueOf(sp[2].substring(0,sp[2].length()-1));
    	}
    	else if(sp[1].equals("疑似患者")) {
    		if(sp[2].equals("确诊感染")){
    			int change=Integer.valueOf(sp[3].substring(0,sp[2].length()-1));//改变人数
    			line1.grhz+=change;
    			line1.yshz-=change; 			
    		}
    		else {//流入情况
    			String tolocation=sp[3];//流入省
    			int change=Integer.valueOf(sp[4].substring(0,sp[4].length()-1));//改变人数
    			line line2;
    	    	if(!isExistlocation(tolocation,all)) {//不存在对应该省的记录
    	    		line2=new line(tolocation,0,0,0,0);//新建数据条
    	    		all[count]=line2;
    	    		count++;
    	    	}
    	    	else {
    	    		line2=getLine(tolocation,all);//获得原有的数据条
    	    	}
    			line1.yshz-=change;
    			line2.yshz+=change;
    		}
    	}
    	else if(sp[1].equals("排除")) {
    		line1.yshz-=Integer.valueOf(sp[3].substring(0,sp[3].length()-1));   		
    	}
    	else {//感染患者流入情况
    		String tolocation=sp[3];//流入省
			int change=Integer.valueOf(sp[4].substring(0,sp[4].length()-1));//改变人数
			line line2;
	    	if(!isExistlocation(tolocation,all)) {//不存在对应该省的记录
	    		line2=new line(tolocation,0,0,0,0);//新建数据条
	    		all[count]=line2;
	    		count++;
	    	}
	    	else {
	    		line2=getLine(tolocation,all);//获得原有的数据条
	    	}
			line1.grhz-=change;
			line2.grhz+=change;   		
    	}
    }
    
     /*
              * 功能：找出指定地址是否已经存在记录
              * 输入参数：省的名字，总的记录数组
              *返回值：true,false
    */
    static boolean isExistlocation(String location,line[] all) {
    	for(int i=0;i<count;i++) {
    		if(location.equals(all[i].location)) {
    			return true;
    		}
    	}
    	return false;    	
    }
    
     /*
              * 功能：找出指定地址的记录
              * 输入参数：省的名字，总的记录数组
              *返回值：一条记录
    */  
    static line getLine(String location,line[] all) {
    	for(int i=0;i<count;i++) {
    		if(location.equals(all[i].location)) {
    			return all[i];
    		}
    	}
    	return null;//不会用到
    }
    
     /*
     * 功能：把所有记录输出到txt文件
     * 输入参数：总的记录数组all
     *返回值：无
    */
    static void printtxt(line[] all) throws IOException {
    	File f = new File("d:\\output.txt");
        BufferedWriter output = new BufferedWriter(new FileWriter(f,true));
        for(int i=0;i<count;i++) {//写入统计数据
        	output.write(all[i].printline() + "\n");
        }
    	output.close();
    }
    
     /*
     * 功能：拼音顺序排序all数组
     * 输入参数：总的记录数组all
     *返回值：排序好的all数组
    */
    static line[] sortline(line[] all) {
    	String[] location=new String[count];
    	line[] result=new line[34];//排序后结果
	    for(int j=0;j<34;j++) {
	    	result[j]=new line();
	    }
    	for(int i=0;i<count;i++) {
    		location[i]=all[i].location;
    	}    	
        Collator cmp = Collator.getInstance(java.util.Locale.CHINA);
        Arrays.sort(location, cmp);
        int j=0;//控制省份拼音顺序索引
        while(j<count) {
        	int i=0;
        	while(i<count) {
	        	if(all[i].location.equals(location[j])) {
	        		result[j]=all[i];
	        		j++;
	        	}
	        	i++;
        	}
        }
        return result;
    }    
}



