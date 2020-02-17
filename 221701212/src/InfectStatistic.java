import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
	int[] ip;
	int[] sp;
	int[] cure;
	int[] death;
	String[] argv;
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
		ip = new int[32];
		sp = new int[32];
		cure = new int[32];
		death = new int[32];
		cmd4type = new Vector<String>();
		cmd4provience = new Vector<String>();
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
  		BufferedReader br = new BufferedReader(new FileReader(path));
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
		String status2 = "(\\S+) 新增 疑似患者 (\\d+)人";
    String status3 = "(\\S+) 治愈 (\\d+)人";
    String status4 = "(\\S+) 死亡 (\\d+)人";
    String status5 = "(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
    String status6 = "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
    String status7 = "(\\S+) 疑似患者 确诊感染 (\\d+)人";
    String status8 = "(\\S+) 排除 疑似患者 (\\d+)人";
    
    if(dataLine.matches(status1)) {
    	
    } else if(dataLine.matches(status2)) {
    	
    } else if(dataLine.matches(status3)) {
    	
    } else if(dataLine.matches(status4)) {
    	
    } else if(dataLine.matches(status5)) {
    	
    } else if(dataLine.matches(status6)) {
    	
    } else if(dataLine.matches(status7)) {
    	
    } else if(dataLine.matches(status8)) {
    	
    }
	}

	public static void main(String[] args) {
		InfectStatistic IS = new InfectStatistic(args.length, args);
		IS.initArgument();
		IS.statistic();
	}
}
