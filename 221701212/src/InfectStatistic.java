import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
		//listFile4log = new List<String>();
		//listFile4out = new List<String>();
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
				cmd4data = argv[i];
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
		for(int i = 0; i < listFile4log.size(); i++) {
			System.out.println(listFile4log.get(i));
		}
	}
	
  public static List<String> getPath(String path) {
    List<String> files = new ArrayList<String>();
    File file = new File(path);
    File[] tempList = file.listFiles();

    for (int i = 0; i < tempList.length; i++) {
        if (tempList[i].isFile()) {
            files.add(tempList[i].toString());
            //文件名，不包含路径
            //String fileName = tempList[i].getName();
        }
        if (tempList[i].isDirectory()) {
            //这里就不递归了，
        }
    }
    return files;
}
	
	public static void main(String[] args) {
		InfectStatistic IS = new InfectStatistic(args.length, args);
		IS.initArgument();
		IS.statistic();
		//System.out.println("helloworld");
	}
}
