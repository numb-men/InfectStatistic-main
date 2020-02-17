import java.io.IOException;
import java.util.regex.Pattern;

class Province {
	String name;
	int ip;
	int sp;
	int cure;
	int dead;
	boolean status;
	Province(String name) {
		this.name=name;
		this.ip=0;
		this.sp=0;
		this.cure=0;
		this.cure=0;
		this.dead=0;
		this.status=false;
	}
}

public class InfectStatistic {
	
	static String log=null;
	static String out=null;
	static String date=null;
	static String type=null;
	static StringBuffer province=new StringBuffer("");
	
	public static void main(String[] args) throws IOException {
		readCommand(args);
    }
	
	static void readCommand(String[] args) {
		if(!args[0].equals("list")) {
			System.out.println("缺少list指令");
			System.exit(0);
		}
		for(int i=0;i<args.length;i++) {
			if(args[i].equals("-log")) {
				log=args[++i];
			}
			if(args[i].equals("-out")) {
				out=args[++i];
			}
			if(args[i].equals("-date")) {
				date=args[++i];
			}
			if(args[i].equals("-province")) {
				province.append(args[++i]);
				for(int j=i;j<args.length-1;j++) {
					if(!(args[i].equals("-log")||args[i].equals("-out")||args[i].equals("-date"))) {
						i++;
						province.append(" "+args[i]);
					}
				}
			}
		}
//		System.out.println(log);
//		System.out.println(out);
//		System.out.println(date);
//		System.out.println(province);
	}
}
