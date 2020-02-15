/**
 * InfectStatistic
 * TODO
 * 疫情统计
 * @author 221701202_陈如滨
 * @version 1.0
 * @since 2020-2-8
 */


import java.util.Date;
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
    
    //存放输入的日期参数
    public String date;
    
    //依次对应：ip：infection patients 感染患者，sp：suspected patients 疑似患者，cure：治愈 ，dead：死亡是否出现
    public int[] type = {0,0,0,0};

    //依次对应各个省份是否出现，最后一个为全国
    public int[] province = new int[32];
    public String[] province_name = {"安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏"		
    	,"江西","辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"};
    
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
                	if(flag==0) {
                		System.out.println("患者类型输入有误");
                		return false;
                	}
                	
                }
                //检查province参数
                else if(args[i].equals("-province")) {
                	flag=GetProvince(++i);
                	if(flag==0) {
                		System.out.println("地区输入有误");
                		return false;
                	}
                }
                else {
                	System.out.println("未输入任何参数，命令行格式错误");
                	return true;
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
        	int type_num = 0;
            if(i<args.length) {
                for(int j=i;j<args.length;j++) {
                	if(args[j].equals("ip")) {
                		type[0]=1;
                		type_num++;
                	}
                	else if(args[j].equals("sp")) {
                		type[1]=1;
                		type_num++;
                	}
                	else if(args[j].equals("cure")) {
                		type[2]=1;
                		type_num++;
                	}
                	else if(args[j].equals("dead")) {
                		type[3]=1;
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
        	for(int k=0;k<32;k++)
        		province[k]=0;
            if(i<args.length) {
                for(int j=0;j<args.length;j++) {
                	for(int m=0;m<31;m++) {
                		if(args[j].equals(province_name[m])) {
                    		province[m]=1;
                    		province_num++;
                    	}
                    	else if(args[j].equals("全国")) {
                    		province[31]=1;
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

    public static void main(String[] args) {
        InfectStatistic cmd = new InfectStatistic();
        InfectStatistic.CmdArgs cmdargs= cmd.new CmdArgs(args);
        boolean check = cmdargs.CmdArgCheck();     
        if(args.length==0)
            System.out.println("命令行参数为空");
        if(check==false)
            return;
            
    }
}
