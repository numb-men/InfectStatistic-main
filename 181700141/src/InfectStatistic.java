import java.io.File;
import java.util.HashSet;

/**
 * InfectStatistic TODO
 *
 * @author 181700141_呼叫哆啦A梦
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
    public static void main(String[] args) {
        int length = args.length;
        if (length == 0)
            return;
        else if (args[0].equals("list")) {
            ListCommand command = new ListCommand();
            try {
                command.dealParameter(args);
                command.carryOutActions();
            } catch (IllegalException e) {
                System.out.println(e);
            } catch (Exception e) {
                System.out.println(e);
            }

        } else {
            System.out.println("不存在" + args[0] + "指令");
        }

    }
}

//当参数或参数值非法时将抛出该异常类
class IllegalException extends Exception {
    // 记录错误原因
    private String message;

    public IllegalException(String tMessage) {
        message = tMessage;
    }

    public String toString() {
        return message;
    }
}

/**
 * 
 * @author 181700141_呼叫哆啦A梦
 * 
 *         处理list命令
 * 
 *         -log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径 。
 * 
 *         -out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径 。
 * 
 *         -date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期之前的所有log文件。
 * 
 *         -type 可选择[ip： infection patients 感染患者，sp： suspected patients
 *         疑似患者，cure：治愈 ，dead：死亡患者]， 使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp
 *         cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况， 不指定该项默认会列出所有情况。
 * 
 *         -province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江。
 */

class ListCommand {
    // 记录日志目录路径
    private String inDirectory = null;
    // 记录输出目录路径
    private String outDirectory = null;
    // 记录日期：date的参数值
    private String date = null;
    // 记录type的值，如果集合为空，默认列出全部情况
    private HashSet<String> type = new HashSet<String>();
    // 记录要列出的省
    private HashSet<String> province = new HashSet<String>();

    private boolean dateIsExist = false;
    private boolean typeIsExist = false;
    private boolean provinceIsExist = false;

    /**
     * 处理list命令的各参数，对各个参数初始化其处理类。
     * 
     * @param 用户输入的命令，含list
     * @throws 如果初步解析list命令如：list命令未提供该参数的执行方法 同一参数出现多次，必须存在的参数不存在时将抛出IllegalException
     */
    public void dealParameter(String[] args) throws IllegalException {
        int l = args.length;
        // 存储参数值
        String[] parameterValues;
        for (int i = 1; i < l; i++) {
            switch (args[i]) {
            case "-log":
                if (inDirectory != null)
                    throw new IllegalException("错误，重复出现 -log参数");
                if (i == l - 1 || args[i + 1].charAt(0) == '-')
                    throw new IllegalException("错误，未提供-log参数值");
                // 给日志目录路径赋值
                inDirectory = args[++i];
                break;
            case "-out":
                if (outDirectory != null)
                    throw new IllegalException("错误，重复出现-out参数");
                if (i == l - 1 || args[i + 1].charAt(0) == '-')
                    throw new IllegalException("错误，未提供-out参数值");
                // 给输出目录路径赋值
                outDirectory = args[++i];
                break;
            case "-date":
                if (dateIsExist)
                    throw new IllegalException("错误，重复出现-date参数");
                if (i < l - 1 && args[i + 1].charAt(0) != '-')
                    date = args[++i];
                dateIsExist = true;
                break;
            case "-type":
                if (typeIsExist)
                    throw new IllegalException("错误，重复出现-type参数");
                for (int j = i + 1; j < l; j++) {
                    if (args[j].charAt(0) == '-')
                        break;
                    i = j;
                    type.add(args[j]);
                }
                typeIsExist = true;
                break;
            case "-province":
                if (provinceIsExist)
                    throw new IllegalException("错误，重复出现-province参数");
                for (int j = i + 1; j < l; j++) {
                    if (args[j].charAt(0) == '-')
                        break;
                    i = j;
                    province.add(args[j]);
                }
                provinceIsExist=true;
                break;
            default:
                if (args[i].charAt(0) == '-')
                    throw new IllegalException("错误，list命令不支持" + args[i] + "参数");
                throw new IllegalException("错误，参数值" + args[i] + "非法");
            }// end of switch
        } // end of for
        if (outDirectory == null || inDirectory == null)
            throw new IllegalException("错误，参数-log及-out要求必须存在");
    }

    // 执行各参数所要求的操作
    public void carryOutActions() throws Exception {
        /*
         * System.out.println("log:" + inDirectory + "\nout:" + outDirectory + "\ndate:"
         * + date + "\ntype:"); for (String s : type) System.out.print("  " + s);
         * System.out.println("\nprovince:\n"); for (String s : province)
         * System.out.print("  " + s);
         */
        File file = new File(inDirectory);
        if (!file.exists() || !file.isDirectory())
            throw new IllegalException("错误，日志目录" + inDirectory + "不存在");

    }

    // 读取目录下的日志文件
    public void handleFile() {

    }

}
