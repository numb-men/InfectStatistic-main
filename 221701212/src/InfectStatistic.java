import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * InfectStatistic
 * TODO
 *
 * @author hx_wang
 * @version 1.0
 * @since 2020.02.15
 */
class InfectStatistic {
	int argc;
	int total_ip, total_sp, total_cure, total_death;
	int PROVIENCE_NUM = 31;
	int[] ip;
	int[] sp;
	int[] cure;
	int[] death;
	boolean[] flg;
	String[] argv;
	String[] provience_array;
	String dir4log = null;
	String dir4out = null;
	String cmd4data = null;
	Vector<String> cmd4type;
	Vector<String> cmd4provience;
	List<String> listFile4log;
	List<String> listFile4out;
	boolean flg4provience = false;
	boolean flg4data = false;
	boolean flg4ip = false;
	boolean flg4sp = false;
	boolean flg4cure = false;
	
	public InfectStatistic(int argc, String[] argv) {
		this.argc = argc;
		this.argv = argv;
		flg = new boolean[PROVIENCE_NUM];
		ip = new int[PROVIENCE_NUM];
		sp = new int[PROVIENCE_NUM];
		cure = new int[PROVIENCE_NUM];
		death = new int[PROVIENCE_NUM];
		cmd4type = new Vector<String>();
		cmd4provience = new Vector<String>();
		provience_array = new String[] {"安徽","北京","重庆","福建","甘肃","广东","广西","贵州",
				"海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古",
				"宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"};
		
		for(int i = 0; i < PROVIENCE_NUM; i++) {
			flg[i] = false;
			ip[i] = sp[i] = cure[i] = death[i] = 0;
		}
		total_ip = total_sp = total_cure = total_death = 0;
	}
	
	public void initArgument() {
		getArgument();
		//printCMD();
	}

	public void getArgument() {
		for(int i = 1; i < argc; i++) {
			if(argv[i].equals("-log")) {
				i++;
				dir4log = argv[i];
			} else if(argv[i].equals("-out")) {
				i++;
				dir4out = argv[i];
			} else if(argv[i].equals("-data")) {
				i++;
				cmd4data = argv[i] + ".log.txt";
				flg4data = true;
			} else if(argv[i].equals("-type")) {
				i++;
				cmd4type.add(argv[i]);
			} else if(argv[i].equals("-provience")) {
				i++;
				cmd4provience.add(argv[i]);
			}
		}
	}
	
	private void printCMD() {
		System.out.println("The log is:" + dir4log);
		System.out.println("The out is:" + dir4out);
		System.out.println("The data is:" + cmd4data);
		//System.out.println("The type is:" + cmd4type);
		//System.out.println("The provience is:" + cmd4provience);	
	}
	
	private void statistic() {
		listFile4log = getPath(dir4log);
		Collections.sort(listFile4log);
		for(int i = 0; i < listFile4log.size(); i++) {
			getData(listFile4log.get(i));
		}
		calculate_total();
	}
	
  public List<String> getPath(String path) {
    List<String> files = new ArrayList<String>();
    File file = new File(path);
    File[] tempList = file.listFiles();
    String rexp1 = "((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\.log\\.txt";
    
    for (int i = 0; i < tempList.length; i++) {
    	if (tempList[i].getName().matches(rexp1) == true && tempList[i].isFile()) {
      	if(flg4data == true && tempList[i].getName().compareTo(cmd4data) <= 0) {
          files.add(tempList[i].toString());
          //System.out.println(tempList[i].getName());
      	} else if(flg4data == false) {
          files.add(tempList[i].toString());
          //System.out.println(tempList[i].getName());
      	}
      }
    }
    return files;
}
	
  public void getData(String path) {
  	try {
  		//BufferedReader br = new BufferedReader(new FileReader(path));
  		FileInputStream fis = new FileInputStream(path);   
  		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");   
  		BufferedReader br = new BufferedReader(isr);  
  		String dataLine = br.readLine();
  		
  		while(dataLine != null) {
  			if(!dataLine.startsWith("//")) {
  				headleData(dataLine);
  			}
  			dataLine = br.readLine();
  		}
  	} catch(Exception e) {
  		e.printStackTrace();
  	}
  }
  
	public void headleData(String dataLine) {
		String status1 = "(\\S+) 新增 感染患者 (\\d+)人";
    String status2 = "(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
    String status3 = "(\\S+) 治愈 (\\d+)人";
    String status4 = "(\\S+) 死亡 (\\d+)人";
		String status5 = "(\\S+) 新增 疑似患者 (\\d+)人";
    String status6 = "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
    String status7 = "(\\S+) 疑似患者 确诊感染 (\\d+)人";
    String status8 = "(\\S+) 排除 疑似患者 (\\d+)人";
    
    if(dataLine.matches(status1)) {
    	ip_inc(dataLine);
    } else if(dataLine.matches(status2)) {
    	ip_flow(dataLine);
    } else if(dataLine.matches(status3)) {
    	ip_cure(dataLine);
    } else if(dataLine.matches(status4)) {
    	ip_die(dataLine);
    } else if(dataLine.matches(status5)) {
    	sp_inc(dataLine);
    } else if(dataLine.matches(status6)) {
    	sp_flow(dataLine);
    } else if(dataLine.matches(status7)) {
    	sp_conf(dataLine);
    } else if(dataLine.matches(status8)) {
    	sp_exclude(dataLine);
    }
	}

	public void sp_exclude(String dataLine) {
		String[] str = dataLine.split(" ");
		for(int i = 0; i < PROVIENCE_NUM; i++) {
			if(str[0].equals(provience_array[i])) {
				sp[i] -= Integer.parseInt(str[3].substring(0, str[3].indexOf("人")));
				flg[i] = true;
			}
		}
	}

	public void sp_conf(String dataLine) {
		String[] str = dataLine.split(" ");
		for(int i = 0; i < PROVIENCE_NUM; i++) {
			if(str[0].equals(provience_array[i])) {
				sp[i] -= Integer.parseInt(str[3].substring(0, str[3].indexOf("人")));
				ip[i] += Integer.parseInt(str[3].substring(0, str[3].indexOf("人")));
				flg[i] = true;
			}
		}
	}

	public void sp_flow(String dataLine) {
		String[] str = dataLine.split(" ");
		for(int i = 0; i < PROVIENCE_NUM; i++) {
			if(str[0].equals(provience_array[i])) {
				for(int j = 0; j < PROVIENCE_NUM; j++) {
					if(str[3].equals(provience_array[j])) {
						sp[i] -= Integer.parseInt(str[4].substring(0, str[4].indexOf("人")));
						sp[j] += Integer.parseInt(str[4].substring(0, str[4].indexOf("人")));
						flg[i] = flg[j] = true;
					}
				}
			}
		}
	}

	public void sp_inc(String dataLine) {
		String[] str = dataLine.split(" ");
		for(int i = 0; i < PROVIENCE_NUM; i++) {
			if(str[0].equals(provience_array[i])) {
				sp[i] += Integer.parseInt(str[3].substring(0, str[3].indexOf("人")));
				flg[i] = true;
			}
		}
	}

	public void ip_die(String dataLine) {
		String[] str = dataLine.split(" ");
		for(int i = 0; i < PROVIENCE_NUM; i++) {
			if(str[0].equals(provience_array[i])) {
				ip[i] -= Integer.parseInt(str[2].substring(0, str[2].indexOf("人")));
				death[i] += Integer.parseInt(str[2].substring(0, str[2].indexOf("人")));
				flg[i] = true;
			}
		}
	}

	public void ip_cure(String dataLine) {
		String[] str = dataLine.split(" ");
		for(int i = 0; i < PROVIENCE_NUM; i++) {
			if(str[0].equals(provience_array[i])) {
				ip[i] -= Integer.parseInt(str[2].substring(0, str[2].indexOf("人")));
				cure[i] += Integer.parseInt(str[2].substring(0, str[2].indexOf("人")));
				flg[i] = true;
			}
		}
	}

	public void ip_flow(String dataLine) {
		String[] str = dataLine.split(" ");
		for(int i = 0; i < PROVIENCE_NUM; i++) {
			if(str[0].equals(provience_array[i])) {
				for(int j = 0; j < PROVIENCE_NUM; j++) {
					if(str[3].equals(provience_array[j])) {
						ip[i] -= Integer.parseInt(str[4].substring(0, str[4].indexOf("人")));
						ip[j] += Integer.parseInt(str[4].substring(0, str[4].indexOf("人")));
						flg[i] = flg[j] = true;
					}
				}
			}
		}
	}

	public void ip_inc(String dataLine) {
		String[] str = dataLine.split(" ");
		for(int i = 0; i < PROVIENCE_NUM; i++) {
			if(str[0].equals(provience_array[i])) {
				ip[i] += Integer.parseInt(str[3].substring(0, str[3].indexOf("人")));
				flg[i] = true;
			}
		}
	}

	public void calculate_total() {
		for(int i = 0; i < PROVIENCE_NUM; i++) {
			total_cure += cure[i];
			total_death += death[i];
			total_ip += ip[i];
			total_sp += sp[i];
		}
	}

	public void writeData(String path) {
		try {
			FileOutputStream outputStream = new FileOutputStream(path);   
	    OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream, "utf-8");
	    BufferedWriter writer = new BufferedWriter(outputWriter);
	    String out = "全国 感染患者" + total_ip + "人 疑似患者" + total_sp + "人 治愈" + 
	    							total_cure + "人 死亡" + total_death + "人";
	    writer.write("out");
	    for(int i = 0; i < PROVIENCE_NUM; i++) {
	    	if(flg[i] == true) {
	    		writer.write("\n");
	    		out = provience_array[i] + "感染患者" + ip[i] + "人 疑似患者" + sp[i] + 
	    				"人 治愈" + cure[i] + "人 死亡" + death[i] + "人";
	    		writer.write("out");
	    	}
	    }
	    writer.write("\n");
	    writer.write("// 该文档并非真实数据，仅供测试使用");
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		InfectStatistic IS = new InfectStatistic(args.length, args);
		IS.initArgument();
		IS.statistic();
	}
}
