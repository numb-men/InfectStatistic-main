/**
 * InfectStatistic
 * TODO
 *
 * @author 邵研
 * @version 0.1
 * @since xxx
 */
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

class InfectStatistic {
    public static void main(String[] args) {
        argparse arg=new argparse(args);
        TreeSet<String> logpath=getfile();

    }
    private static TreeSet<String> getfile(){
        TreeSet<String> files=new TreeSet<String>();
        File logpath =new File("log");
        File[] teplist= logpath.listFiles();
        for (int i=0;i<teplist.length;i++){
            if(teplist[i].isFile())
                files.add(teplist[i].toString());
        }
        /*files.add("log\\2020-01-25.log.txt");
        Iterator i=files.iterator();
        while(i.hasNext())
            System.out.println(i.next());*/
        return files;
    }
}

class argparse{
    public String command=null;
    public String logpath=null,outpath=null,enddate=null;
    public HashSet types=new HashSet();
    public HashSet provinces=new HashSet();
    argparse(String[] args){
        if (args.length==0){
            System.out.println("至少要输入一个命令");
            System.exit(0);
        }
        HashSet types=new HashSet();
        HashSet provinces=new HashSet();

        if(args[0].equals("list")){
            for(int i=1;i<args.length;i++){
                switch (args[i]){
                    case "-log":
                        logpath=args[++i];
                        break;
                    case "-out":
                        outpath=args[++i];
                        break;
                    case "-date":
                        enddate=args[++i];
                        break;
                    case "-type":
                        while(i+1<args.length && !args[i+1].startsWith("-")){
                            types.add(args[++i]);
                        }
                        break;
                    case "-province":
                        while(i+1<args.length && !args[i+1].startsWith("-")){
                            provinces.add(args[++i]);
                        }
                        break;
                }
            }
        }
    }
}
