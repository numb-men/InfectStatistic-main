import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
    //建立HashMap，把命令参数和参数值分开
    private static HashMap<String, String> parseArgs(String[] args) {
        HashMap<String, String> tmp = new HashMap<String, String>();
        String key = null;
        //判断命令是否为list
        if (!args[0].equals("list")) {
            System.out.println("该命令不存在！");
            return tmp;
        }
        //命令参数和参数值放入HashMap中
        for (int i = 1; i<args.length; i++){
            String arg = args[i];
            if (arg.startsWith("-")){
                if (key == null){
                    key = arg;
                }
                else{
                    tmp.put(key,null);
                    key = arg;
                }
            }
            else {
                tmp.put(key, arg);
                key = null;
            }
        }
       return tmp;
    }
    //命令对应的行为
    private static void func(HashMap<String, String> parseArgs){
        if(parseArgs.containsKey("-log")){
            String path = parseArgs.get("-log");
            readFile(path);//读取文件夹下的文件
        }
        else {
            System.out.println("必须附带log参数！");
            return;
        }
        if(parseArgs.containsKey("-out")){

        }
        else {
            System.out.println("必须附带out参数！");
            return;
        }
        if(parseArgs.containsKey("-date")){

        }
        if(parseArgs.containsKey("-type")){
            String type = parseArgs.get("-type");
            switch (type){
                case "ip":break;
                case "sp":break;
                case "cure":break;
                case "dead":break;
                default:
            }
        }
        if(parseArgs.containsKey("-province")){

        }
    }
    //读取目录下的日志
    public static void readFile(String path){
        File all = new File(path);
        List allPath = getFile(all);
        for(int i = 0; i < allPath.size(); i++){
            File file = new File(String.valueOf(allPath.get(i)));
            String content = txt2String(file);
            //System.out.println(content);
        }
    }
    //txt转String
    public static String txt2String(File file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
                result.append(System.lineSeparator() + s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
    // 读取文件夹下所有文件名
    public static List getFile(File file) {
        List listLocal = new ArrayList<>();
        if (file != null) {
            File[] f = file.listFiles();
            if (f != null) {
                for (int i = 0; i < f.length; i++) {
                    getFile(f[i]);
                    listLocal.add(f[i]);
                }
            }
        }
        return listLocal;
    }
    public static void main(String[] args) {
        String cmdLine = "list -date -log C:/Users/ASUS/Documents/GitHub/InfectStatistic-main/221701429/log -out D:/output.txt";
        args = cmdLine.split(" ");
        HashMap<String, String> parseArgs = parseArgs(args);
        System.out.println(parseArgs);
        func(parseArgs);
    }
}
