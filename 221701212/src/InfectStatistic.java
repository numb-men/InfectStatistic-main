import java.io.File;
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
			//System.out.println(listFile4log.get(i));
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
	
	public static void main(String[] args) {
		InfectStatistic IS = new InfectStatistic(args.length, args);
		IS.initArgument();
		IS.statistic();
	}
}
