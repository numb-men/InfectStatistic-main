/**
 * InfectStatistic
 *
 * @author XTG-L
 * @version 0.2
 * @since 2020-2-9
 */
class InfectStatistic {
    public static void main(String[] args) {
        CmdArgs cmd=new CmdArgs(args);
        System.out.println(cmd.argVals("log")[0]);
    }
}

class CmdArgs {
    String[] args;
    String noUseInStart="//";

    /**
     * 传入命令行参数数组构造
     * @param args
     */
    CmdArgs(String[] args) {
        this.args=args;
    }

    /**
     * 传入命令构造，可以设置无用的前缀，如`groovy Lib`
     * @param args
     * @param noUseInStart
     */
    CmdArgs(String[] args, String noUseInStart) {
        this.args=args;
        this.noUseInStart=noUseInStart;
    }

    /**
     * 获取命令
     * @return
     */
    String getCmd() {
        String cmd=new String(args[0]);
        for(int i=1; i<args.length;i++) {
            cmd+=" "+args[i];
        }
        return cmd;
    }

    /**
     * 获取某个命令行参数的值，多个值只会返回第一个
     * @param key
     * @return
     */
    String argVal(String key) {
        String[] values=argVals(key);
        String value=values[0];
        return value;
    }

    /**
     * 获取某个命令行参数的值，返回列表
     * @param key
     * @return
     */
    String[] argVals(String key) {
        if(!hasValue(key)) {
            return null;
        }
        String[] values=null;
        String cmd=getCmd();
        String[] splitedCmd=cmd.split("-");
        for (String string : splitedCmd) {
            if(string.contains(key)) {
                String s=string.replaceAll(key+" ", "");
                values=s.split("[\\s]+");
            }
        }
        return values;
    }

    /**
     * 判断该命令是否有对应的参数
     * @param key
     * @return
     */
    boolean hasValue(String key) {
        String cmd=getCmd();
        String[] splitedCmd=cmd.split("-");
        for (String string : splitedCmd) {
            if(string.contains(key)) {
                if(string.trim().length()>key.length()) {
                    return true;
                } else {
                    break;
                }
            }
        }
        return false;
    }
}