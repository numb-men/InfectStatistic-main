import java.util.ArrayList;
import java.util.HashMap;

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

    public static void main(String args[]) {
        String cmdLine = "list -date -log D:/log/ -out D:/output.txt";
        args = cmdLine.split(" ");
        HashMap<String, String> parseArgs = parseArgs(args);
        System.out.println(parseArgs);
    }
}
