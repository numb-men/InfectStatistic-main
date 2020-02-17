/**
 * InfectStatistic
 * TODO
 *
 * @author 邵研
 * @version 0.1
 * @since xxx
 */
import java.util.HashSet;
import java.util.Iterator;

class InfectStatistic {
    public static void main(String[] args) {
        if (args.length==0){
            System.out.println("至少要输入一个命令");
            System.exit(0);
        }
        String logpath=null,outpath=null,enddate=null;
        HashSet types=new HashSet();
        HashSet provinces=new HashSet();
        System.out.println(args.length);
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
                    case "-province":
                        while(i+1<args.length && !args[i+1].startsWith("-")){
                            provinces.add(args[++i]);
                        }
                }
            }
        }

        System.out.println("输入路径"+logpath);
        System.out.println("输出路径"+outpath);
        System.out.println("日期"+enddate);
        System.out.println("类别");
        Iterator it=types.iterator();
        while(it.hasNext())
        {
            Object o=it.next();
            System.out.println(o);
        }
        System.out.println("省份");
        it=provinces.iterator();
        while(it.hasNext())
        {
            Object o=it.next();
            System.out.println(o);
        }
    }
}
