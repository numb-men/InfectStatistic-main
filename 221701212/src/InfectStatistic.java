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
	String cmd4type = null;
	String cmd4provience = null;
	
	public InfectStatistic(int argc, String[] argv) {
		this.argc = argc;
		this.argv = argv;
	}
	
	public void initArgument() {
		getArgument();
		//printCMD();
	}
	
	private void printCMD() {
		System.out.println("The log is:" + dir4log);
		System.out.println("The out is:" + dir4out);
		System.out.println("The data is:" + cmd4data);
		System.out.println("The type is:" + cmd4type);
		System.out.println("The provience is:" + cmd4provience);	
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
				cmd4data = argv[i];
			} else if(argv[i].equals("-provience")) {
				i++;
				cmd4provience = argv[i];
			}
		}
	}
	
	public static void main(String[] args) {
		InfectStatistic IS = new InfectStatistic(args.length, args);
		IS.initArgument();
		//System.out.println("helloworld");
	}
}
