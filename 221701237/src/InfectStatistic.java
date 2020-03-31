import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.Collator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InfectStatistic
 * TODO
 *
 * @author 卧城听风雨
 * @version 1
 * @since 
 */

class InfectStatistic {
    public static void main(String[] args) {
    	String[] provinceName = {
    			"安徽","北京","重庆","福建","甘肃","广东",
    			"广西","贵州","海南","河北","河南","黑龙江",
    			"湖北","湖南","吉林","江苏","江西","辽宁",
    			"内蒙古","宁夏","青海","山东","山西","陕西",
    			"上海","四川","天津","西藏","新疆","云南","浙江"};
    	province [] provinceList= new province[provinceName.length+1];
    	List<String> list = new ArrayList<String>();
    	Command command;
    	readFile fileoperate;
    	
    	provinceList[0] = new province("全国");
    	provinceList[0].setAppear();
    	for(int i=1;i<provinceName.length+1;i++)//初始化省份数组
    	{
    		provinceList[i] = new province(provinceName[i-1]);
    	}
        for(int i=0;i<args.length;i++)//获取所有附带的命令及参数
        {
        	list.add(args[i]);
        }
        command = new Command(list);//初始化一个命令类
        if(!command.isLegal())//判断命令参数是否非法
        {
        	System.exit(0);
        }
        fileoperate = new readFile(command.getLogContent(),provinceList);//初始化一个文件操作读文件类
        if(command.getDateContent().equals("all"))//判断是否有指定具体日期
        {
        	fileoperate.readLog("all");
        }
        else
        {
        	fileoperate.readLog(command.getDateContent());
        }
        writeFile wr = new writeFile(provinceList,command.getProvince(),command.getType(),
        		command.getProvinceContent(),command.getTypeContent(),command.getOogContent());
        wr.writeResult();
    }
}
//文件操作类-写文件
//provinceList:全国和各个省份的数据统计数组
//isProvince:命令行是否接收-province命令 如果有 isProvince=true反之false
//provinceContent:-province命令后带的参数
//isType:命令行是否接收-type命令 值同isProvince
//typeContent:-type命令后带的参数
//file:输出结果文件
class writeFile
{
	private province [] provinceList;
	private boolean isProvince;
	private List<String> provinceContent;
	private boolean isType;
	private List<String> typeContent;
	private File file;
	public writeFile(province [] provinceList,boolean isProvice,boolean isType,
			List<String> provinceContent,List<String> typeContent,String outPath)
	{
		this.provinceList = provinceList;
		this.isProvince = isProvice;
		this.provinceContent = provinceContent;
		this.isType = isType;
		this.typeContent = typeContent;
		
		String parentPath=null;
		if(outPath.lastIndexOf("\\")<0)//为了测试的时候路径是windows风格还是Linux风格做准备，虽然好像没什么用
		{
			parentPath = outPath.substring(0,outPath.lastIndexOf("/"));
		}
		else
		{
			parentPath = outPath.substring(0,outPath.lastIndexOf("\\"));
		}  	
       	File file1 = new File(parentPath);//file1为file的父文件路径
       	file = new File(outPath);
        if (!file1.exists()) //父目录不存在，则先创建父目录
        {
            file1.mkdirs();// 能创建多级目录
            try 
            {
				file.createNewFile();
			} 
            catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("输出路径有误");
				System.exit(0);
			}
        }
        else
        {
        	  try 
        	  {
  				file.createNewFile();
  			  } 
        	  catch (IOException e) 
        	  {
  				// TODO Auto-generated catch block
  				System.out.println("输出路径有误");
  				System.exit(0);
  			}
        }
	}
	//输入结果到文件方法
	//没有想到什么好的解决办法，就把每种情况都列了出来
	//1.没输入-province命令，没输入-type
	//2.输入-province命令，没输入-type
	//3.输入-province命令，输入-type
	//4.没输入-province命令，输入-type
	//分别与下面的四个判断对应
	//由于嵌套循环较多，若程序性能较差可以考虑修改这方面的代码（虽然我已经想过了）
	public void writeResult()
	{
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file.getAbsoluteFile());
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fileWriter);
		if(isProvince==false&&isType==false)
		{		
			for(int i=0;i<provinceList.length;i++)
	        {
	        	if(provinceList[i].getAppear())
	        	{
	        		try {
						bw.write(provinceList[i].getName()+" "+"感染患者 "+provinceList[i].getIp()
								+" "+"疑似患者 "+provinceList[i].getSp()+" "+"治愈 "+provinceList[i].getCure()
								+" "+"死亡 "+provinceList[i].getDead()+"人");
						bw.newLine();

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        }
			try {
				bw.append("// 该文档并非真实数据，仅供测试使用");
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(isProvince==true&&isType==false)
		{
			for(int i=0;i<provinceList.length;i++)
			{
				for(int n=0;n<provinceContent.size();n++)
				{
					if(provinceList[i].getName().equals(provinceContent.get(n)))
					{
						try {
							bw.write(provinceList[i].getName()+" "+"感染患者"+provinceList[i].getIp()+
									"人 "+"疑似患者"+provinceList[i].getSp()+"人 "+"治愈"+provinceList[i].getCure()
									+"人 "+"死亡"+provinceList[i].getDead()+"人");
							bw.newLine();
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			try {
				bw.append("// 该文档并非真实数据，仅供测试使用");
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(isProvince==true&&isType==true)
		{
			for(int i=0;i<provinceList.length;i++)
			{
				for(int n=0;n<provinceContent.size();n++)
				{
					if(provinceList[i].getName().equals(provinceContent.get(n)))
					{
						try {
							bw.write(provinceList[i].getName()+" ");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						for(int j=0;j<typeContent.size();j++)
						{
							String type = typeContent.get(j);
							if(type.equals("ip"))
							{
								try {
									bw.write("感染患者"+provinceList[i].getIp()+"人 ");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							if(type.equals("sp"))
							{
								try {
									bw.write("疑似患者"+provinceList[i].getSp()+"人 ");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							if(type.equals("cure"))
							{
								try {
									bw.write("治愈"+provinceList[i].getCure()+"人 ");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							if(type.equals("dead"))
							{
								try {
									bw.write("死亡"+provinceList[i].getDead()+"人 ");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						try {
							bw.newLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			try {
				bw.append("// 该文档并非真实数据，仅供测试使用");
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(isProvince==false&&isType==true)
		{
			for(int i=0;i<provinceList.length;i++)
	        {
	        	if(provinceList[i].getAppear())
	        	{
	        		try {
						bw.write(provinceList[i].getName()+" ");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					for(int j=0;j<typeContent.size();j++)
					{
						String type = typeContent.get(j);
						if(type.equals("ip"))
						{
							try {
								bw.write("感染患者"+provinceList[i].getIp()+"人 ");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if(type.equals("sp"))
						{
							try {
								bw.write("疑似患者"+provinceList[i].getSp()+"人 ");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if(type.equals("cure"))
						{
							try {
								bw.write("治愈"+provinceList[i].getCure()+"人 ");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if(type.equals("dead"))
						{
							try {
								bw.write("死亡"+provinceList[i].getDead()+"人 ");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					try {
						bw.newLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	        }
			try {
				bw.append("// 该文档并非真实数据，仅供测试使用");
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
//省份类 用于存储各个全国和各个省份的各种数据
//代码中开辟一个province数组，第0位存放全国，其余省份按排序一次存放
//name:省份的名称
//ip:感染患者数量
//sp:疑似患者数量
//cure:治愈患者数量
//dead:死亡患者数量
//appear:用来判断是否在日志文件中出现该省份
class province
{
	private String name;
	private int ip;
	private int sp;
	private int cure;
	private int dead;
	private boolean appear;
	public province(String name)
	{
		this.name=name;
		ip=0;
		sp=0;
		cure=0;
		dead=0;
		appear=false;
	}
	public void setAppear()
	{
		appear=true;
	}
	public void addIp(int n)
	{
		ip+=n;
	}
	public void subIp(int n)
	{
		ip-=n;
	}
	public void addSp(int n)
	{
		sp+=n;
	}
	public void subSp(int n)
	{
		sp-=n;
	}
	public void addCure(int n)
	{
		cure+=n;
	}
	public void addDead(int n)
	{
		dead+=n;
	}
	public String getName()
	{
		return name;
	}
	public int getIp()
	{
		return ip;
	}
	public int getSp()
	{
		return sp;
	}
	public int getCure()
	{
		return cure;
	}
	public int getDead()
	{
		return dead;
	}
	public boolean getAppear()
	{
		return appear;
	}
}
//文件操作类-读文件
//path:日志文件的路径，-log后带的参数
//files:该路径下的说有文件路径(绝对路径)
//filesName:该路径下所有文件的名称，用于判断输入的日期是否超出范围(仅名称不带后缀)
//file:文件路径打开的文件夹
//tempList:该路径下的所有文件
//provinceList:省份数组
//templates:用于匹配日志文件内容的正则表达式模板
//maxDate:该路径下的日志文件的最大日期
//minDate:该路径下的日志文件的最小日期
class readFile{
	String path;
	ArrayList<String> files;
	ArrayList<String> filesName;
	File file;
	File[] tempList;
	province [] provinceList;
	String [] templates = {
			"(\\S+) 新增 感染患者 (\\d+)人","(\\S+) 新增 疑似患者 (\\d+)人","(\\S+) 感染患者 流入 (\\S+) (\\d+)人",
			"(\\S+) 疑似患者 流入 (\\S+) (\\d+)人","(\\S+) 死亡 (\\d+)人","(\\S+) 治愈 (\\d+)人",
			"(\\S+) 疑似患者 确诊感染 (\\d+)人",
			"(\\S+) 排除 疑似患者 (\\d+)人"
	};
	String maxDate;
	String minDate;
	//构造函数
	//传入参数：1.日志文件路径 如：c:\log\,2.省份数组
	//函数功能：1.判断日志文件路径
	//			2.获取日志文件路径下所有文件的绝对路径
	//			3.获取日志文件路径下所有文件的名称
	public readFile(String path,province [] pList)
	{
		this.path=path;
		provinceList = pList;
		files = new ArrayList<String>();
		filesName = new ArrayList<String>();
		file=new File(path);
		if(!file.exists())
		{
			System.out.println("日志文件路径有误!");
			System.exit(0);
		}
        tempList = file.listFiles();
        if(tempList==null)
        {
        	System.out.println("null");
        }
        for(int i=0;i<tempList.length;i++)
        {
        	String f = tempList[i].toString();
        	files.add(f);
        	filesName.add(f.substring(f.length()-18,f.length()-8));
        }
        if((maxDate = findMaxdate(filesName))==null)
        {
        	System.out.println("寻找最大日期出错");
        }
        if((minDate = findMindate(filesName))==null)
        {
        	System.out.println("寻找最小日期出错");
        }
	}
	//查找最大/最小日期方法
	//传入参数:日期字符串 如{“2020-01-22”，“2020-01-25”，“2020-01-23”}
	//返回值：最大/小日期
	public String findMaxdate(ArrayList<String> filesName)
	{
		String date="0000-00-00";
		for(int i=0;i<filesName.size();i++)
		{
			if(filesName.get(i).compareTo(date)>0)
			{
				date=filesName.get(i);			
			}
		}
		return date;
	}
	public String findMindate(ArrayList<String> filesName)				
	{
		String date="9999-99-99";
		for(int i=0;i<filesName.size();i++)
		{
			if(filesName.get(i).compareTo(date)<0)
			{
				date=filesName.get(i);				
			}
		}
		return date;
	}
	//判单读取日志文件数量
	//传入参数：日期 如：2020-01-22
	//返回值:false 或 true
	//path:-date命令后面带的日期
	//path有两种取值:1.正常日期 ，输入-date后带的符合规范的日期。
	//				2. “all” 字符串，未输入-date命令或者输入-date命令后未指定具体日期时
	//	
	public boolean readLog(String path)
	{
		if(path.equals("all")) //如果path是all直接读取到maxDate日期为止
		{
			readLogFile(maxDate);
			return true;
		}
		else
		{
			if(path.compareTo(maxDate)>0||path.compareTo(minDate)<0)//判断输入的日期是否在日志文件的
			{														//范围中
				System.out.println("日期超出范围");
				System.exit(0);
				return false;
			}
			else
			{
					readLogFile(path);
					return true;
			}		
		}
	}
	//读取日志文件内容的办法
	//传入参数：指定日期(yyyy-mm-dd格式） 如2020-01-22
	//返回值：无
	//注：path.compareTo(filesName.get(i)判断要读取的日志文件日期是否超过输入日期
	public void readLogFile(String path)
	{
		int i=0;
		while(i<filesName.size()&&path.compareTo(filesName.get(i))>=0)
		{
			try {
				FileInputStream fileInputStream = new FileInputStream(files.get(i));
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String text = null;
                try {
					while((text = bufferedReader.readLine()) != null){
					    statisNum(text);//统计数据方法
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
	}
	//查找指定省份的所在provinceList数组中的位置方法
	//传入参数:省份名称 如:福建
	//返回值:传入的省份所在的位置 如:4(福建位于第四个,因为第0个是全国)
	//实现方式:用for循环遍历provinList数组，与其中的name比较，命中则返回值
	public int findLocal(String name)
	{
		for(int i=1;i<provinceList.length;i++)
		{
			if(provinceList[i].getName().equals(name))
			{
				return i;
			}
		}
		return 0;
	}
	//统计日志文件中数据的办法
	//传入参数:日志文件中的一行数据 如：福建 新增 感染患者 2人
	//返回值:无
	//实现方法:单行匹配，将传入的数据与先去准备好的正则表达式模板templates进行比较，若命中则跳转分支进行处理
	//处理结果:更新provinceList中的数据
	public void statisNum(String text)
	{
		text=text.trim();
		if(text.matches(templates[0]))
		{
			String provinceName = getProvince(text,0);
			int num = getNum(text);
			provinceList[findLocal(provinceName)].addIp(num);
			provinceList[findLocal(provinceName)].setAppear();
			provinceList[0].addIp(num);
		}
		else if(text.matches(templates[1]))
		{
			String provinceName = getProvince(text,0);
			int num = getNum(text);
			provinceList[findLocal(provinceName)].addSp(num);
			provinceList[findLocal(provinceName)].setAppear();
			provinceList[0].addSp(num);
		}
		else if(text.matches(templates[2]))
		{
			String provinceName1 = getProvince(text,0);
			String provinceName2 = getProvince(text,3);
			int num = getNum(text);
			provinceList[findLocal(provinceName1)].subIp(num);
			provinceList[findLocal(provinceName1)].setAppear();		
			provinceList[findLocal(provinceName2)].addIp(num);
			provinceList[findLocal(provinceName2)].setAppear();
		}
		else if(text.matches(templates[3]))
		{
			String provinceName1 = getProvince(text,0);
			String provinceName2 = getProvince(text,3);
			int num = getNum(text);
			provinceList[findLocal(provinceName1)].subSp(num);
			provinceList[findLocal(provinceName1)].setAppear();		
			provinceList[findLocal(provinceName2)].addSp(num);
			provinceList[findLocal(provinceName2)].setAppear();
			
		}
		else if(text.matches(templates[4]))
		{
			String provinceName = getProvince(text,0);
			int num = getNum(text);
			int i = findLocal(provinceName);
			provinceList[i].addDead(num);
			provinceList[i].subIp(num);
			provinceList[i].setAppear();
			provinceList[0].subIp(num);
			provinceList[0].addDead(num);
		}
		else if(text.matches(templates[5]))
		{
			String provinceName = getProvince(text,0);
			int num = getNum(text);
			int i = findLocal(provinceName);
			provinceList[i].addCure(num);
			provinceList[i].subIp(num);
			provinceList[i].setAppear();
			provinceList[0].subIp(num);
			provinceList[0].addCure(num);
		}
		else if(text.matches(templates[6]))
		{
			String provinceName = getProvince(text,0);
			int num = getNum(text);
			int i = findLocal(provinceName);
			provinceList[i].addIp(num);
			provinceList[i].subSp(num);
			provinceList[i].setAppear();
			provinceList[0].addIp(num);
			provinceList[0].subSp(num);
		}
		else if(text.matches(templates[7]))
		{
			String provinceName = getProvince(text,0);
			int num = getNum(text);
			int i = findLocal(provinceName);
			provinceList[i].subSp(num);
			provinceList[i].setAppear();
			provinceList[0].subSp(num);
		}
	}
	public String getProvince(String text,int index)
	{
		String [] temp =text.split(" ");
		return temp[index];
	}
	//处理日志文件中每一行中*人的人数方法
	//传入参数:日志文件中的一行数据 如：福建 新增 感染患者 2人
	//返回值:2
	//实现方法:用正则表达式匹配提取数据
	public Integer getNum(String text)
	{
		String regex = "\\d+";
		Pattern pattern =  Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		matcher.find();
		Integer num = Integer.parseInt(matcher.group());
		return num;
	}
}
//命令类
//list：判断是否输入-list
//log：判断是否输入-log
//out：判断是否输入-out
//date：判断是否输入-date
//type：判断是否输入-type
//province：判断是否输入-province
//dateContent：-date命令后带参数
//logContent：-log命令后带参数
//outContent：-out命令后带参数
//provinceContent：-province后带参数
//typeContent：-type后带参数
//command：输入的命令字符串数组
class Command{
	private Boolean list=false;
	private Boolean log=false;
	private Boolean out=false;
	private Boolean date=false;
	private Boolean type=false;
	private Boolean province=false;
	private String dateContent;
	private String logContent;
	private String outContent;
	private List<String> provinceContent = new ArrayList<String>();
	private List<String> typeContent = new ArrayList<String>();
	private List<String> command = new ArrayList<String>();
	
	public List<String> getTypeContent()
	{
		return typeContent;
	}
	public List<String> getProvinceContent()
	{
		return provinceContent;
	}
	public boolean getType()
	{
		return type;
	}
	public boolean getProvince()
	{
		return province;
	}
	public String getLogContent()
	{
		return logContent;
	}
	public String getOogContent()
	{
		return outContent;
	}
	public String getDateContent()
	{
		return dateContent;
	}
	public Command(List<String> list)
	{
		command=list;
		dateContent="all";
		for(int i=0;i<list.size();i++)
		{
			String str = list.get(i);
			switch (str) {
				case "list":
					this.list=true;
					break;
				case "-log":
					this.log=true;
					setLogContent(i+1);
					break;
				case "-out":
					this.out=true;
					setOutContent(i+1);
					break;
				case "-date":
					this.date=true;
					setDateContent(i+1);
					break;
				case "-province":
					this.province=true;
					setProvinceContent(i+1);
					break;
				case "-type":
					this.type=true;
					setTypeContent(i+1);
					break;
					
			}
		}
	}
	//判断各命令参数是否合法
	//输入参数：无
	//返回值:true或false
	//-list必不可少，-date可有可无 日期格式（yyyy-mm-dd）缺一不可
	//-log命名跟后带参数均不可少
	//-out命名跟后带参数均不可少
	public boolean isLegal()
	{
		if(!list)
		{
			System.out.println("-list命令缺失");
			return false;
		}
		if(date)
		{
			String regex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	        Pattern pattern = Pattern.compile(regex);
	        Matcher m = pattern.matcher(dateContent);
	        boolean dateFlag = m.matches();
	        if (!dateFlag) {
	            System.out.println("日期格式错误");
	            return false;//日期格式错误 例如:2020-1-3(正确为2020-01-03）
	        }
	        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	        formatter.setLenient(false);
	        try{  
	            formatter.parse(dateContent);  
	        }catch(Exception e){
	            System.out.println("日期格式错误！");
	            return false;//日期格式错误 例如:2a20-01-03(出现非数字字符)
	        }
		}
		if(log)
		{
			if(logContent==null)
			{
				System.out.println("-log命名参数缺失！");
				return false;//输入-log命令 却未输入参数
			}
		}
		else
		{
			System.out.println("缺失-log命令！");
			return false;//缺少-log命令
		}
		if(out)
		{
			if(outContent==null)
			{
				System.out.println("-out命令参数缺失！");
				return false;//输入-out命令 却未输入参数
			}
		}
		else
		{
			System.out.println("缺失-out命令！");
			return false;//缺失-out命令
		}
		if(type)
		{
			if(typeContent==null)
			{
				System.out.println("-typy命令参数缺失！");
				return false;//输入-type 命令 却不输入参数
			}
			else
			{
				for(int i=0;i<typeContent.size();i++)
				{
					if(!typeContent.get(i).equals("sp")&&!typeContent.get(i).equals("ip")
							&&!typeContent.get(i).equals("cure")&&!typeContent.get(i).equals("dead"))
					{
						System.out.println("-type命令参数错误！");
						return false;//type的参数不是（ip sp cure dead四种之一）
					}
				}
			}
		}
		if(province)
		{
			if(provinceContent==null)
			{
				return false;//输入 -province命令却不输入参数
			}
		}
		return true;
	}
	//或者各命令后带参数方法
	//传入参数：该命令所在数组的位置后一位
	//返回值:无
	//实现方法:传入参数作为起始，不断向后读取，碰到带有“-”开头的字符串时停止
	public void setProvinceContent(int i)
	{
		while(i<command.size()&&!command.get(i).matches("-.*"))
		{
			provinceContent.add(command.get(i));
			i++;
		}
	}
	public void setTypeContent(int i)
	{
		while(i<command.size()&&!command.get(i).matches("-.*"))
		{
			typeContent.add(command.get(i));
			i++;
		}
	}
	public void setDateContent(int i)
	{
		while(i<command.size()&&!command.get(i).matches("-.*"))
		{
			dateContent=command.get(i);
			i++;
		}
	}
	public void setLogContent(int i)
	{
		while(i<command.size()&&!command.get(i).matches("-.*"))
		{
			logContent=command.get(i);
			i++;
		}
	}
	public void setOutContent(int i)
	{
		while(i<command.size()&&!command.get(i).matches("-.*"))
		{
			outContent=command.get(i);
			i++;
		}
	}
}
