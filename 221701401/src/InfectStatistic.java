import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

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
    	/*FileHandle l=new FileHandle();
    	l.findFile("D:\\testlog","2020-01-22");*/
    }
}

//文件处理类（八字没一撇）
class FileHandle {
	//private String pathname;//路径名
	
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
        	//这里进行结果统计
        	List<LogResult> list=dh.calResult(strTmp);
        }
        buffReader.close();
	}
	
	//找到日期对应文件(需修改为找到日期之前的文件)
	public void findFile(String path,String date) throws IOException {
		String[] list=new File(path).list();
		StringBuffer dt=new StringBuffer(date);
		dt.append(".log.txt");
		for (String file:list) {
			if (file.contentEquals(dt)) {
				StringBuffer ph=new StringBuffer(path);
				ph.append("\\");
				ph.append(dt);
				readFile(ph.toString());				
			}
		}
	}
	
	//写文件(输入参数：路径和处理后的字符串)
	public void writeFile(String path,String result) throws IOException {
		File file = new File(path);
		if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
		if(!file.exists()) file.createNewFile();
		FileOutputStream fout=new FileOutputStream(file,true);
		OutputStreamWriter writer=new OutputStreamWriter(fout,"UTF-8");
		//这里要写入内容
        //writer.write("zdfkjsdlf");
        writer.flush();
        writer.close();
	}
	
}

//日志结果类
class LogResult {
	private String province;//省份
	private int ip;
	private int sp;
	private int cure;
	private int dead;
	
	LogResult() {
		ip=sp=cure=dead=0;
		province="";
	}
	
	void setProvince (String pro) {
		province=pro;
	}
	
	String getProvince () {
		return province;
	}
	
	void setIp (int i) {
		ip=i;
	}
	
	int getIp () {
		return ip;
	}
	
	void setSp (int s) {
		sp=s;
	}
	
	int getSp () {
		return sp;
	}
	
	void setCure (int c) {
		cure=c;
	}
	
	int getCure () {
		return cure;
	}
	
	void setDead (int d) {
		dead=d;
	}
	
	int getDead () {
		return dead;
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
	//protected String typeSkip=".*/.*";//匹配//开头的行，方便过滤
	private List<LogResult> list;
	
	DataHandle () {
		list=new ArrayList<>();
	}
	
	//将单行文本转换成LogResult并加入集合
	public List<LogResult> calResult(String str) {
		Handler addIpHandler=new AddIp(addIp);
		addIpHandler.getData(str);
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
				lr.setIp(-sp);
				lr2.setProvince(m.group(2));
				lr2.setIp(sp);
				list.add(lr);
				list.add(lr2);
				return lr;
			}
			else 
				return nextHandler.getData(str);
		}
	}
	
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




