import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
    public static void main(String[] args) throws IOException {
    	FileHandle l=new FileHandle();
    	l.dealFile("D:/testlog","2020-01-27");
    	l.writeFile("C:/Users/V556U/Desktop/a.txt");
    }
}

//命令行类
class CmdHandle {
	private String[] args;
	
	void setCmdArg(String[] arg) {
		args=arg;
	}
}

//文件处理类
class FileHandle {
	private List<LogResult> all=new ArrayList();
	
	//读文件
	public void readFile(String path) throws IOException {
		FileInputStream fin = new FileInputStream(path);
        InputStreamReader reader = new InputStreamReader(fin,"UTF-8");
        BufferedReader buffReader = new BufferedReader(reader);
        String strTmp = "";
        DataHandle dh=new DataHandle();
		while((strTmp = buffReader.readLine())!=null){
        	if(strTmp.startsWith("\uFEFF")){
        	   strTmp = strTmp.replace("\uFEFF","");
        	}//win10无法建立无BOM,用此去掉开头
        	//进行结果统计
        	dh.calResult(strTmp);
        }
		List<LogResult> res=dh.calNationData();
		all.addAll(res);
		buffReader.close();
	}		
	
	//找到日期对应文件进行处理
	public void dealFile(String path,String date) throws IOException {
		String[] list=new File(path).list();
		boolean isEnd=false;
		//按日期排序
		List<String> fileList = Arrays.asList(list);
	    Collections.sort(fileList, new Comparator<String>() {
	    	@Override
	    	public int compare(String o1, String o2) {
	    		return o1.compareTo(o2);
	    	}
	    });
	    //找文件
		StringBuffer dt=new StringBuffer(date);
		dt.append(".log.txt");
		for (String file:fileList) {
			if (file.contentEquals(dt)) isEnd=true; 
			StringBuffer ph=new StringBuffer(path);
			ph.append("/");
			ph.append(file);
			readFile(ph.toString());
			if (isEnd) break;
		}
		DataHandle dh=new DataHandle();
		List<LogResult> res=dh.mergeResult(all);
		all=res;
	}
	
	//写文件(输入参数：路径)
	public void writeFile(String path) throws IOException {
		File file = new File(path);
		if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
		if(!file.exists()) file.createNewFile();
		FileOutputStream fout=new FileOutputStream(file,true);
		OutputStreamWriter writer=new OutputStreamWriter(fout,"UTF-8");
		//这里要写入内容	
		for (LogResult lr:all) {
			String tmp=lr.toString();
			writer.write(tmp);
		}
        writer.flush();
        writer.close();
	}
	
}

//日志结果类
class LogResult {
	private String province;//省份
	private int ip;//感染
	private int sp;//疑似
	private int cure;//治愈
	private int dead;//死亡
	private String[] order={"全国","安徽","北京","重庆","福建","甘肃","广东","广西",
			   "贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西",
			   "辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津",
			   "西藏","新疆","云南","浙江"};//指定的排序顺序
	
	LogResult() {
		ip=sp=cure=dead=0;
		province="";
	}
	
	void setProvince(String pro) {
		province=pro;
	}
	
	String getProvince() {
		return province;
	}
	
	void setIp(int i) {
		ip=i;
	}
	
	int getIp() {
		return ip;
	}
	
	void setSp(int s) {
		sp=s;
	}
	
	int getSp() {
		return sp;
	}
	
	void setCure(int c) {
		cure=c;
	}
	
	int getCure() {
		return cure;
	}
	
	void setDead(int d) {
		dead=d;
	}
	
	int getDead() {
		return dead;
	}
	
	//增加ip,sp,cure,dead
	void sum(int i,int s,int c,int d) {
		ip+=i;
		sp+=s;
		cure+=c;
		dead+=d;
	}
	
	//得到省份在制定顺序中的次序
	int getIndex (String str) {
		for (int i=0;i<order.length;i++) {
			if (str.equals(order[i])) return i;
		}
		return -1;
	}

	//toString（字符串形式输出）
	public String toString() {
		return province+" 感染患者"+ip+"人 疑似患者"+sp+"人 治愈"+cure+"人 死亡"+dead+"人\n";
	}
}

class DataHandle {
	protected Pattern addIp=Pattern.compile("([\\u4e00-\\u9fa5]{0,}+) 新增 感染患者 (\\d+)人");
	protected Pattern addSp=Pattern.compile("([\\u4e00-\\u9fa5]{0,}+) 新增 疑似患者 (\\d+)人");
	protected Pattern addAndSubIp=Pattern.compile("([\\u4e00-\\u9fa5]{0,}+) 感染患者 流入 ([\\u4e00-\\u9fa5]{0,}+) (\\d+)人");
	protected Pattern addAndSubSp=Pattern.compile("([\\u4e00-\\u9fa5]{0,}+) 疑似患者 流入 ([\\u4e00-\\u9fa5]{0,}+) (\\d+)人");
	protected Pattern addDead=Pattern.compile("([\\u4e00-\\u9fa5]{0,}+) 死亡 (\\d+)人");
	protected Pattern addCure=Pattern.compile("([\\u4e00-\\u9fa5]{0,}+) 治愈 (\\d+)人");
	protected Pattern spToIp=Pattern.compile("([\\u4e00-\\u9fa5]{0,}+) 疑似患者 确诊感染 (\\d+)人");
	protected Pattern subSp=Pattern.compile("([\\u4e00-\\u9fa5]{0,}+) 排除 疑似患者 (\\d+)人");
	private List<LogResult> list;
	
	DataHandle () {
		list=new ArrayList<>();
	}
	
	List<LogResult> getList() {
		return list;
	}
	
	//将单行文本转换成LogResult并加入集合
	public void calResult(String str) {
		Handler addIpHandler=new AddIp(addIp);
		addIpHandler.getData(str);
	}
	
	//计算全国数据
	public List<LogResult> calNationData() {
		LogResult all=new LogResult();
		List<LogResult> res=new ArrayList<>();
		all.setProvince("全国");
		for (LogResult lr:list) {
			all.sum(lr.getIp(),lr.getSp(),lr.getCure(),lr.getDead());
		}
		list.add(all);
		res=mergeResult(list);
		return res;
	}
	
	//合并数据(参考)
	public List<LogResult> mergeResult(List<LogResult> list) {
		HashMap<String,LogResult> tempMap=new HashMap<String,LogResult>();
        for (LogResult lr:list) {
            String tmpStr=lr.getProvince();
            if (tempMap.containsKey(tmpStr)) {
                LogResult temp=new LogResult();
                temp.setProvince(lr.getProvince());
                //合并
                temp.setIp(tempMap.get(tmpStr).getIp()+lr.getIp());
                temp.setSp(tempMap.get(tmpStr).getSp()+lr.getSp());
                temp.setCure(tempMap.get(tmpStr).getCure()+lr.getCure());
                temp.setDead(tempMap.get(tmpStr).getDead()+lr.getDead());
                //HashMap不允许key重复，当有key重复时，前面key对应的value值会被覆盖
                tempMap.put(tmpStr,temp);
            } else {
                tempMap.put(tmpStr,lr);
            }
            
        }
        //去重
        List<LogResult> newList=new ArrayList<>();
        for (String temp:tempMap.keySet()) {
            newList.add(tempMap.get(temp));
        }
        List<LogResult> sortList=ListSort(newList); 
        return sortList;
	}
	
	//按省份排序
	public List<LogResult> ListSort (List<LogResult> list) {
		Collections.sort(list,new Comparator<LogResult>() {
			public int compare(LogResult r1,LogResult r2) {
				return r1.getIndex(r1.getProvince())-r2.getIndex(r2.getProvince());
			}
		});
		return list;
	}
	
	//尝试使用责任链模式，抽象类下方有八个类分别代表八种模式及对应的处理方法
	//因为没有查到减少类的方法，所以就这么按照网上的例子写了,不是很懂变通
	//总觉得代码重复率有点高
	abstract class Handler {
		protected Pattern ptn;  //正则表达式
	    protected Handler nextHandler;  // 下一个处理者
	    
	    public abstract LogResult getData(String str); // 提取数据
	}

	//新增感染
	class AddIp extends Handler {

		public AddIp(Pattern p) {
			ptn=p;
			nextHandler=new AddSp(addSp);
		}

		@Override
	    public LogResult getData(String str) {
			Matcher m=ptn.matcher(str);
			boolean rs=m.find();
			if (rs) {
				LogResult lr=new LogResult();
				lr.setProvince(m.group(1));
				lr.setIp(Integer.parseInt(m.group(2)));
				list.add(lr);
				return lr;
			}
			else 
				return nextHandler.getData(str);
		}
		
	}

	//新增疑似
	class AddSp extends Handler {

		public AddSp(Pattern p) {
			ptn=p;
			nextHandler=new AddAndSubIp(addAndSubIp);;
		}

		@Override
		public LogResult getData(String str) {
			// TODO 自动生成的方法存根
			Matcher m=ptn.matcher(str);
			boolean rs=m.find();
			if (rs) {
				LogResult lr=new LogResult();
				lr.setProvince(m.group(1));
				lr.setSp(Integer.parseInt(m.group(2)));
				list.add(lr);
				return lr;
			}
			else 
				return nextHandler.getData(str);
		}
	}
	
	//感染流入
	class AddAndSubIp extends Handler {
		public AddAndSubIp(Pattern p) {
			ptn=p;
			nextHandler=new AddAndSubSp(addAndSubSp);
		}

		@Override
		public LogResult getData(String str) {
			// TODO 自动生成的方法存根
			Matcher m=ptn.matcher(str);
			boolean rs=m.find();
			if (rs) {
				LogResult lr=new LogResult();
				LogResult lr2=new LogResult();
				lr.setProvince(m.group(1));
				int ip=Integer.parseInt(m.group(3));
				lr.setIp(-ip);
				lr2.setProvince(m.group(2));
				lr2.setIp(ip);
				list.add(lr);
				list.add(lr2);
				return lr;
			}
			else 
				return nextHandler.getData(str);
		}
	}
		
	//疑似流入
	class AddAndSubSp extends Handler {
		public AddAndSubSp(Pattern p) {
			ptn=p;
			nextHandler=new AddDead(addDead);
		}

		@Override
		public LogResult getData(String str) {
			// TODO 自动生成的方法存根
			Matcher m=ptn.matcher(str);
			boolean rs=m.find();
			if (rs) {
				LogResult lr=new LogResult();
				LogResult lr2=new LogResult();
				lr.setProvince(m.group(1));
				int sp=Integer.parseInt(m.group(3));
				lr.setSp(-sp);
				lr2.setProvince(m.group(2));
				lr2.setSp(sp);
				list.add(lr);
				list.add(lr2);
				return lr;
			}
			else 
				return nextHandler.getData(str);
		}
	}
	
	//死亡
	class AddDead extends Handler {

		public AddDead(Pattern p) {
			ptn=p;
			nextHandler=new AddCure(addCure);
		}

		@Override
	    public LogResult getData(String str) {
			Matcher m=ptn.matcher(str);
			boolean rs=m.find();
			if (rs) {
				LogResult lr=new LogResult(),lr2=new LogResult();
				int dead=Integer.parseInt(m.group(2));
				String pro=m.group(1);
				lr.setProvince(pro);
				lr.setDead(dead);
				lr2.setProvince(pro);
				lr2.setIp(-dead);
				list.add(lr);
				list.add(lr2);
				return lr;
			}
			else 
				return nextHandler.getData(str);
		}	
	}

	//治愈
	class AddCure extends Handler {

		public AddCure(Pattern p) {
			ptn=p;
			nextHandler=new SpToIp(spToIp);
		}

		@Override
	    public LogResult getData(String str) {
			Matcher m=ptn.matcher(str);
			boolean rs=m.find();
			if (rs) {
				LogResult lr=new LogResult(),lr2=new LogResult();
				int cure=Integer.parseInt(m.group(2));
				String pro=m.group(1);
				lr.setProvince(pro);
				lr.setCure(cure);
				lr2.setProvince(pro);
				lr2.setIp(-cure);
				list.add(lr);
				list.add(lr2);
				return lr;
			}
			else 
				return nextHandler.getData(str);
		}		
	}

	//疑似确诊
	class SpToIp extends Handler {

		public SpToIp(Pattern p) {
			ptn=p;
			nextHandler=new SubSp(subSp);
		}

		@Override
	    public LogResult getData(String str) {
			Matcher m=ptn.matcher(str);
			boolean rs=m.find();
			if (rs) {
				LogResult lr=new LogResult(),lr2=new LogResult();
				int ip=Integer.parseInt(m.group(2));
				String pro=m.group(1);
				lr.setProvince(pro);
				lr.setSp(-ip);
				lr2.setProvince(pro);
				lr2.setIp(ip);
				list.add(lr);
				list.add(lr2);
				return lr;
			}
			else 
				return nextHandler.getData(str);
		}		
	}

	//排除疑似
	class SubSp extends Handler {

		public SubSp(Pattern p) {
			ptn=p;
			nextHandler=null;
		}

		@Override
	    public LogResult getData(String str) {
			Matcher m=ptn.matcher(str);
			boolean rs=m.find();
			if (rs) {
				LogResult lr=new LogResult();
				int sp=Integer.parseInt(m.group(2));
				String pro=m.group(1);
				lr.setProvince(pro);
				lr.setSp(-sp);
				list.add(lr);
				return lr;
			}
			else 
				return null;
		}		
	}

}