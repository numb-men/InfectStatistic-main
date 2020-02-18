/**
 * InfectStatistic
 * TODO
 *
 * @author 邵研
 * @version 0.1
 * @since xxx
 */
import sun.reflect.generics.tree.Tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class InfectStatistic {
    public static void main(String[] args) {
        ArgParse arg=new ArgParse(args);
        TreeSet<String> logFiles=getFile(arg.logPath);
        String[] provList={"安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"};
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for(int i=0;i<provList.length;i++){
            map.put(provList[i],i);
        }
        TreeSet<String> files = getFile(arg.logPath);
        Iterator i=logFiles.iterator();
        applyLog(i.next().toString(),map);
        /*
        Iterator i=logFiles.iterator();
        while(i.hasNext())
            System.out.println(i.next());*/
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
    private static void applyLog(String path,HashMap<String, Integer> map){
        System.out.println(path);
        File file = null;
        BufferedReader br = null;
        StringBuffer buffer = null;
        String data = "";
        try {
            file = new File(path);
            buffer = new StringBuffer();
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
            br = new BufferedReader(isr);
            int s;
            while ((s = br.read()) != -1) {
                buffer.append((char) s);
            }
            data = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String prov1,prov2;
        int num=0;

        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]{2,3} 新增 疑似患者 \\d+[人]");
        Matcher matcher = pattern.matcher(data);
        while (matcher.find()) {
            String [] arr = (matcher.group().split("\\s+"));
            prov1=arr[0];
            num= Integer.parseInt(arr[3].substring(0,arr[3].length()-1));
            System.out.println("省"+prov1+"人数"+num);
        }
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
class Province{
    public int ip=0,sp=0,cure=0,dead=0;
}
