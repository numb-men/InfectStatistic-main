/**
 * InfectStatistic
 * TODO
 * 疫情统计
 * @author 221701202_陈如滨
 * @version 1.0
 * @since 2020-2-8
 */

import java.util.Date;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class InfectStatistic {
    //logPath:存放日志路径，outPath:存放输出文件路径
    public String logPath;
    public String outPath;
    
    //获取当前日期
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String dateString = formatter.format(new Date());
    
    //用于转换文件
    public String date;
    
    //依次对应：ip：infection patients 感染患者，sp：suspected patients 疑似患者，cure：治愈 ，dead：死亡是否出现
    public int[] type = {0,0,0,0};
    public String[] type_name= {"感染患者","疑似患者","治愈","死亡"};
    //类型参数个数
    public int type_num;
    public String[] type_order=new String[4];

    //依次对应各个省份是否出现，第一个为全国
    int total=32;
    public int[] province = new int[total];
    public String[] province_name = {"全国","安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏"		
    	,"江西","辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"};
    //依次对应各个省份各个情况的人数，第一个为全国
    public int[][] numOfPeople = new int [total][4];
    //每个省份所有有情况的人数，第一个为全国
    public int[] totalNum=new int [total];
    
    //记录是否有type、province参数
    public boolean typeFlag=false;
    public boolean provinceFlag=false;
    
    /**
    * 解析命令行参数
    */
    class CmdArgs {
        /**
         *读取命令行
         */
        String[] args;
        CmdArgs(String[] args_cmd) {
            args=args_cmd;
        }
        
        /**
         *进行命令行参数检查
         */
        public boolean CmdArgCheck() {
            //检查是否有list
            if(!args[0].equals("list")) {
            	System.out.println("未输入list，命令行格式错误");
                return false;
            }
            int i,flag=0;
            for(i=1;i<args.length;i++) {
                //检查log参数
                if(args[i].equals("-log")) {
                    flag=GetLog(++i);
                    if(flag==0) {
                    	System.out.println("日志路径有误");
                        return false;
                    }
                }
                //检查out参数
                else if(args[i].equals("-out")) {
                    flag=GetOut(++i);
                    if(flag==0) {
                    	System.out.println("输出路径有误");
                    	return false;        
                    }
                        
                }
                //检查date参数
                else if(args[i].equals("-date")) {
                	flag=GetDate(++i);
                    if(flag==0) {
                    	System.out.println("输入日期有误");
                        return false;                        
                    }
                }
                //检查type参数
                else if(args[i].equals("-type")) {
                	flag=GetType(++i);
                	i = i+flag-1;
                	typeFlag=true;
                	if(flag==0) {
                		System.out.println("患者类型输入有误");
                		return false;
                	}
                	
                }
                //检查province参数
                else if(args[i].equals("-province")) {
                	flag=GetProvince(++i);
                	i = i+flag-1;
                	provinceFlag=true;
                	if(flag==0) {
                		System.out.println("地区输入有误");
                		return false;
                	}
                }
                else {
                	System.out.println("未输入任何参数，命令行格式错误");
                	return false;
                }
            }
            return true;
        }

        /**
         *获取log参数
         *返回1表示参数合格，返回0表示参数不合格
         */
        public int GetLog(int i) {
            if(i<args.length) {
                if(args[i].matches("^[A-z]:\\\\(.+?\\\\)*$")) {
                    logPath=args[i];
                    return 1;
                }
                else
                    return 0;    
            }
            else
                return 0;
        }

        /**
         *获取out参数
         *返回1表示参数合格，返回0表示参数不合格
         */
        public int GetOut(int i) {
            if(i<args.length) {
                if(args[i].matches("^[A-z]:\\\\(\\S+)+(\\.txt)$")) {
                    outPath=args[i];
                    return 1;
                }
                else
                    return 0;    
            }
            else
                return 0;
        }
        
        /**
         *获取date参数
         *返回1表示参数合格，返回0表示参数不合格
         */
        public int GetDate(int i) {
            if(i<args.length) {
            	if(IsValid(args[i])==true && timeCompare(args[i],dateString)<0) {
            		date = args[i] + ".log.txt";
            		return 1;
            	}
            	else
            		return 0;
            }
            else
            	return 0;
        }
        /**
         *判断日期是否合法
         */
        public boolean IsValid(String time) {
        	SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        	try {
        		//此处指定日期/时间解析是否不严格，在true是不严格，false时为严格
    			sd.setLenient(false);
    			//从给定字符串的开始解析文本，以生成一个日期
    			sd.parse(time);
    		}
    		catch (Exception e) {
    			return false;
    		}
    		return true;
        }
        /**
         *比较两个日期大小
         *返回-1表示前者小，返回0表示相等，返回1表示前者大
         */
        public int timeCompare(String t1,String t2){
        	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        	Calendar c1 = Calendar.getInstance();
        	Calendar c2 = Calendar.getInstance();
        	try {
        		c1.setTime(formatter.parse(t1));
        		c2.setTime(formatter.parse(t2));
        	}
        	catch(ParseException e) {
        		e.printStackTrace();
        	}
        	int result = c1.compareTo(c2);
        	return result;
        }
        
        /**
         *获取Type参数
         *返回类型参数个数
         */
        public int GetType(int i) {
        	type_num = 0;
            if(i<args.length) {
                for(int j=i;j<args.length;j++) {
                	if(args[j].equals("ip")) {
                		type[0]=1;
                		type_order[type_num]=type_name[0];
                		type_num++;
                	}
                	else if(args[j].equals("sp")) {
                		type[1]=1;
                		type_order[type_num]=type_name[1];
                		type_num++;
                	}
                	else if(args[j].equals("cure")) {
                		type[2]=1;
                		type_order[type_num]=type_name[2];
                		type_num++;
                	}
                	else if(args[j].equals("dead")) {
                		type[3]=1;
                		type_order[type_num]=type_name[3];
                		type_num++;
                	}
                }
            }
            else
                return 0;
            return type_num;
        }
        
        /**
         *获取Province参数
         *返回省份参数个数
         */
        public int GetProvince(int i) {
        	int province_num = 0;
        	for(int k=0;k<total;k++)
        		province[k]=0;
            if(i<args.length) {
                for(int j=0;j<args.length;j++) {
                	for(int m=0;m<total;m++) {
                		if(args[j].equals(province_name[m])) {
                    		province[m]=1;
                    		province_num++;
                    	}
                	}
                }
            }
            else
                return 0;
            return province_num;
        }
    }
    
    class HandleFile {
    	HandleFile(){}
    	
    	//该日志中出现以下几种情况:
    	public String condition1="(\\S+) 新增 感染患者 (\\d+)人";
    	public String condition2="(\\S+) 新增 疑似患者 (\\d+)人";
    	public String condition3="(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
    	public String condition4="(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
    	public String condition5="(\\S+) 死亡 (\\d+)人";
    	public String condition6="(\\S+) 治愈 (\\d+)人";
    	public String condition7="(\\S+) 疑似患者 确诊感染 (\\d+)人";
    	public String condition8="(\\S+) 排除 疑似患者 (\\d+)人";
    	
    	/**
    	 *读取比date参数小的日期的所有文件
    	 */
    	public void LogFile() {
    		//初始化各省份病状人数
    		for(int i=0;i<total;i++)
        		for(int j=0;j<4;j++)
        			numOfPeople[i][j]=0;
    		File file = new File(logPath);
    		File[] files = file.listFiles();
    		for (int i = 0; i < files.length; i++) {
    			File img = files[i];
    			if(img.getName().compareTo(date) <= 0) {
    				ReadFile(img.getName());
    			}
			}
    	}
    	
    	/**
    	 *逐行读取日志文件 
    	 */
    	public void ReadFile(String dateFile){
    		InputStreamReader isr;
    		try {
    			isr = new InputStreamReader(new FileInputStream(logPath+dateFile),"utf-8");
    			BufferedReader read = new BufferedReader(isr);
    			String s = null;
    			while((s=read.readLine())!=null)
    			{
    				if(s.matches(condition1))
    					DoCondition1(s);
    				else if(s.matches(condition2))
    					DoCondition2(s);
    				else if(s.matches(condition3))
    					DoCondition3(s);
    				else if(s.matches(condition4))
    					DoCondition4(s);
    				else if(s.matches(condition5))
    					DoCondition5(s);
    				else if(s.matches(condition6))
    					DoCondition6(s);
    				else if(s.matches(condition7))
    					DoCondition7(s);
    				else if(s.matches(condition8))
    					DoCondition8(s);
    			}
    			read.close();
    			isr.close();
    		}
    		catch(Exception e) {
        		e.printStackTrace();
        	}
    	}
    	
    	/**
    	 *情况1：新增感染患者
    	 */
    	public void DoCondition1(String str) {
    		String [] arr = str.split("\\s+");
    		int n = Integer.parseInt(arr[3].substring(0, arr[3].indexOf("人")));
    		for(int i = 0;i < total;i++) {
    			if(arr[0].equals(province_name[i])) {
    				numOfPeople[i][0] += n;
    				numOfPeople[0][0] += n;
    			}
    		}
    	}
    	/**
    	 *情况2：新增疑似患者
    	 */
    	public void DoCondition2(String str) {
    		String [] arr = str.split("\\s+");
    		int n = Integer.parseInt(arr[3].substring(0, arr[3].indexOf("人")));
    		for(int i = 0;i < total;i++) {
    			if(arr[0].equals(province_name[i])) {
    				numOfPeople[i][1] += n;
    				numOfPeople[0][1] += n;
    			}
    		}
    	}
    	/**
    	 *情况3：感染患者流动
    	 */
    	public void DoCondition3(String str) {
    		String [] arr = str.split("\\s+");
    		int n = Integer.parseInt(arr[4].substring(0, arr[4].indexOf("人")));
    		for(int i = 0;i < total;i++) {
    			if(arr[0].equals(province_name[i]))
    				numOfPeople[i][0] -= n;
    			if(arr[3].equals(province_name[i]))
    				numOfPeople[i][0] += n;
    		}
    	}
    	/**
    	 *情况4：疑似患者流动
    	 */
    	public void DoCondition4(String str) {
    		String [] arr = str.split("\\s+");
    		int n = Integer.parseInt(arr[4].substring(0, arr[4].indexOf("人")));
    		for(int i = 0;i < total;i++) {
    			if(arr[0].equals(province_name[i]))
    				numOfPeople[i][1] -= n;
    			if(arr[3].equals(province_name[i]))
    				numOfPeople[i][1] += n;
    		}
    	}
    	/**
    	 *情况5：新增死亡人口
    	 */
    	public void DoCondition5(String str) {
    		String [] arr = str.split("\\s+");
    		int n = Integer.parseInt(arr[2].substring(0, arr[2].indexOf("人")));
    		for(int i = 0;i < total;i++) {
    			if(arr[0].equals(province_name[i])) {
    				numOfPeople[i][3] += n;
    				numOfPeople[i][0] -= n;
    				numOfPeople[0][3] += n;
    				numOfPeople[0][0] -= n;
    			}
    		}
    	}
    	/**
    	 *情况6：新增治愈人口
    	 */
    	public void DoCondition6(String str) {
    		String [] arr = str.split("\\s+");
    		int n = Integer.parseInt(arr[2].substring(0, arr[2].indexOf("人")));
    		for(int i = 0;i < total;i++) {
    			if(arr[0].equals(province_name[i])) {
    				numOfPeople[i][2] += n;
    				numOfPeople[i][0] -= n;
    				numOfPeople[0][2] += n;
    				numOfPeople[0][0] -= n;
    			}
    		}
    	}
    	/**
    	 *情况7：疑似患者确诊
    	 */
    	public void DoCondition7(String str) {
    		String [] arr = str.split("\\s+");
    		int n = Integer.parseInt(arr[3].substring(0, arr[3].indexOf("人")));
    		for(int i = 0;i < total;i++) {
    			if(arr[0].equals(province_name[i])) {
    				numOfPeople[i][0] += n;
    				numOfPeople[i][1] -= n;
    				numOfPeople[0][0] += n;
    				numOfPeople[0][1] -= n;
    			}
    		}
    	}
    	/**
    	 *情况8：疑似患者被排除
    	 */
    	public void DoCondition8(String str) {
    		String [] arr = str.split("\\s+");
    		int n = Integer.parseInt(arr[3].substring(0, arr[3].indexOf("人")));
    		for(int i = 0;i < total;i++) {
    			if(arr[0].equals(province_name[i])) {
    				numOfPeople[i][1] -= n;
    				numOfPeople[0][1] -= n;
    			}
    		}
    	}
    	
    	/**
    	 *写txt文件
    	 */
    	public void WriteFile() {
    		for(int m=0;m<total;m++) {
    			for(int n=0;n<4;n++)
    				totalNum[m] = 0;
    		}
    		for(int m=0;m<total;m++) {
    			for(int n=0;n<4;n++)
    				totalNum[m] += numOfPeople[m][n];
    		}
    		//检查放置文件的文件是否存在，不存在则创建
    		try {
    			FileOutputStream fos=new FileOutputStream(outPath,true);//true表明会追加内容
    			PrintWriter pw=new PrintWriter(fos);
    			//情况一：没有指明type与province
    			if(typeFlag == false && provinceFlag == false) {
    				for(int i = 0;i < total;i++) {
    					if(totalNum[i]>0) {
    						pw.write(province_name[i]+" ");
    						for(int j = 0;j < 4;j++)
    							pw.write(type_name[j]+numOfPeople[i][j]+"人"+" ");
    						pw.write("\n");
    					}
    				}
    			}
    			//情况二：没有指明type，但指明了province
    			else if(typeFlag==false && provinceFlag==true) {
    				for(int i=0;i<total;i++) {
    					if(province[i]>0) {
    						pw.write(province_name[i]+" ");
    						for(int j = 0;j < 4;j++)
    							pw.write(type_name[j]+numOfPeople[i][j]+"人"+" ");
    						pw.write("\n");
    					}
    				}
    			}
    			//情况三：指明了type，但没指明province
    			else if(typeFlag==true && provinceFlag==false) {
    				for(int i=0;i<total;i++) {
    					if(totalNum[i]>0) {
    						pw.write(province_name[i]+" ");
    						for(int p = 0;p < type_num;p++) {
    							pw.write(type_order[p]);
    							for(int q=0;q<4;q++) {
    								if(type_name[q].equals(type_order[p]))
    									pw.write(numOfPeople[i][q]+"人"+" ");
    							}
    						}
    						pw.write("\n");
    					}
    				}
    			}
    			//情况四：指明了type与province
    			else if(typeFlag==true && provinceFlag==true) {
    				for(int i=0;i<total;i++) {
    					if(province[i]>0) {
    						pw.write(province_name[i]+" ");
    						for(int p = 0;p < type_num;p++) {
    							pw.write(type_order[p]);
    							for(int q=0;q<4;q++) {
    								if(type_name[q].equals(type_order[p]))
    									pw.write(numOfPeople[i][q]+"人"+" ");
    							}
    						}
    						pw.write("\n");
    					}
    				}
    			}
    			pw.flush();
    		} 
    		catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    }

    public static void main(String[] args) {
        InfectStatistic cmd = new InfectStatistic();
        InfectStatistic.CmdArgs cmdargs = cmd.new CmdArgs(args);
        boolean check = cmdargs.CmdArgCheck();     
        if(args.length==0)
            System.out.println("命令行参数为空");
        if(check==false)
            return;
        InfectStatistic.HandleFile hf = cmd.new HandleFile();
        hf.LogFile();
        hf.WriteFile();
    }
}
