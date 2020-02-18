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
import java.util.regex.Pattern;

class InfectStatistic {
    public static void main(String[] args) {
        ArgParse arg=new ArgParse(args);
        TreeSet<String> logFiles=getFile(arg.logPath);
        Iterator i=logFiles.iterator();
        while(i.hasNext())
            System.out.println(i.next());

    }
    private static TreeSet<String> getFile(String n){
        TreeSet<String> files=new TreeSet<String>();
        File logPath =new File(n);
        File[] tepList= logPath.listFiles();
        Pattern pattern = Pattern.compile("(\\d){4}-(\\d){2}-(\\d){2}\\.log\\.txt");
        for (int i=0;i<tepList.length;i++){
            if(tepList[i].isFile()&&pattern.matcher(tepList[i].getName()).matches())
                files.add(tepList[i].toString());
        }
        return files;
    }
}

class ArgParse{
    public String command=null;
    public String logPath=null,outPath=null,endDate=null;
    public HashSet types=new HashSet();
    public HashSet provinces=new HashSet();
    ArgParse(String[] args){
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
                        logPath=args[++i];
                        break;
                    case "-out":
                        outPath=args[++i];
                        break;
                    case "-date":
                        endDate=args[++i];
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
