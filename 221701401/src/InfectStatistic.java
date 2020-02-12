import java.io.*;

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
    	/*LogHandle l=new LogHandle();
    	l.findFile("D:\\testlog","2020-01-22");*/
    }
}

//日志处理类（八字没一撇）
class LogHandle {
	//private String pathname;//路径名
	
	//读文件
	public void readFile(String path) throws IOException {
		FileInputStream fin = new FileInputStream(path);
        InputStreamReader reader = new InputStreamReader(fin,"UTF-8");
        BufferedReader buffReader = new BufferedReader(reader);
        String strTmp = "";
        while((strTmp = buffReader.readLine())!=null){
            System.out.println(strTmp);
            //这里要进行文件内容处理
            //calResult(strTmp);
        }
        buffReader.close();
	}
	
	//找到日期对应文件
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
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if(!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fout=new FileOutputStream(file,true);
		OutputStreamWriter writer=new OutputStreamWriter(fout,"UTF-8");
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