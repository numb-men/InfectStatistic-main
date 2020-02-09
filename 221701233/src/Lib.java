import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Lib
 * TODO
 *
 * @author ZYF
 * @version 1.0
 * @since 1.0
 */
public class Lib {
}

/**
 * 命令工厂
 */
class CommandFactory {

    /**
     * 通过反射执行相应方法获取命令对象
     *
     * @param name     命令名
     * @param receiver 命令接受者
     * @return 相应方法执行后获取的命令对象
     * @throws Exception 不存在相应命令对象
     */
    public Command getCommand(String name, CommandReceiver receiver) throws Exception {
        return (Command) this.getClass().getMethod(name.toLowerCase(), Class.forName("CommandReceiver"))
                .invoke(this, receiver);
    }

    public Command list(CommandReceiver receiver) {
        return new ListCommand(receiver);
    }

    // 在此处扩展命令...
}

/**
 * 命令接口
 */
interface Command {

    void execute(CommandLine commandLine) throws Exception;

}

/**
 * list命令
 */
class ListCommand implements Command {

    private CommandReceiver receiver;

    private ListChecker checker;

    public ListCommand(CommandReceiver receiver) {
        checker = new ListChecker();
        this.receiver = receiver;
    }

    @Override
    public void execute(CommandLine commandLine) throws Exception {
        checker.check(commandLine);
        receiver.list(checker.getCmd());
    }

}

/**
 * 命令参数解析
 */
class CommandLine {
    private String[] args;

    private Command command;

    CommandLine(String[] args, Command command) {
        this.args = args;
        this.command = command;
    }

    /**
     * 获取某个参数的一个值
     *
     * @param arg 选项名称
     * @return 参数 {@code arg} 的值，不存在返回 {@code null}
     */
    String getValue(String arg) {
        int index = indexOf(arg);

        if (index != -1) {
            if (index + 1 < args.length && args[index + 1].charAt(0) != '-') {
                return args[index + 1];
            }
        }

        return null;
    }

    /**
     * 获取某个参数的所有值
     *
     * @param arg 选项名称
     * @return 参数 {@code arg} 的值列表
     */
    List<String> getValues(String arg) {

        List<String> values = new ArrayList<>();

        int index = indexOf(arg);

        if (index != -1) {
            for (int i = index + 1; i < args.length && args[i].charAt(0) != '-'; i++) {
                values.add(args[i]);
            }
        }

        return values;
    }

    /**
     * 获取参数的索引
     *
     * @param arg 选项名称
     * @return 该选项的索引值；若不存在返回 -1
     */
    private int indexOf(String arg) {
        for (int i = 1; i < args.length; i++) {
            if (args[i].equalsIgnoreCase(arg)) {
                return i;
            }
        }

        return -1;
    }

    public void execute() throws Exception {
        command.execute(this);
    }
}

/**
 * list命令检查器，检查完毕后生成最终的命令格式
 */
class ListChecker {

    HashMap<String, Object> cmd;

    ListChecker() {
        cmd = new HashMap<>();
    }

    public void check(CommandLine line) throws Exception {
        checkLog(line.getValue("-log"));
        checkOut(line.getValue("-out"));
        checkDate(line.getValue("-date"));
        checkType(line.getValues("-type"));
        checkProvince(line.getValues("-province"));
    }

    /**
     * 最终的命令格式
     *
     * @return
     */
    public HashMap<String, Object> getCmd() {
        return cmd;
    }

    /**
     * 检查日志路径
     *
     * @param receiverPath
     * @throws Exception
     */
    public void checkLog(String receiverPath) throws Exception {

        if (receiverPath == null) {
            throw new Exception("value of arg \"-log\" is required.");
        }

        File receiverDir = new File(receiverPath);

        if (!receiverDir.isDirectory()) {
            throw new Exception("\"" + receiverPath + "\" is not a directory.");
        }

        cmd.put("-log", receiverDir);
    }

    /**
     * 检查导出路径
     *
     * @param outPath
     * @throws Exception
     */
    public void checkOut(String outPath) throws Exception {

        if (outPath == null) {
            throw new Exception("value of arg \"-out\" is required.");
        }

        File outFile = new File(outPath);
        outFile.createNewFile();

        if (!outFile.isFile()) {
            throw new Exception("\"" + outPath + "\" is not a file.");
        }

        cmd.put("-out", outFile);
    }

    /**
     * 检查日期格式
     *
     * @param dateStr
     * @throws Exception
     */
    public void checkDate(String dateStr) throws Exception {

        Date defaultDate = new Date();
        if (dateStr == null) {
            cmd.put("-date", defaultDate);
            return;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        Date argDate = format.parse(dateStr);

        if (argDate.after(defaultDate)) {
            throw new Exception(format.format(argDate) + " is later than current time：" + format.format(defaultDate));
        }

        cmd.put("-date", argDate);
    }

    /**
     * 检查人员类型
     *
     * @param argTypes
     * @throws Exception
     */
    public void checkType(List<String> argTypes) throws Exception {
        List<String> defaultTypes = new ArrayList<>();
        defaultTypes.add("ip");
        defaultTypes.add("sp");
        defaultTypes.add("cure");
        defaultTypes.add("dead");

        if (argTypes.size() == 0) {
            cmd.put("-type", defaultTypes);
            return;
        }

        for (String type : argTypes) {
            if (!defaultTypes.contains(type)) {
                throw new Exception("Unknown type：" + type);
            }
        }

        cmd.put("-type", argTypes);
    }

    /**
     * 检查省份
     *
     * @param line
     */
    public void checkProvince(List<String> line) {
        cmd.put("-province", line);
    }
}

/**
 * 命令接收者
 */
class CommandReceiver {

    /**
     * list命令
     *
     * @param cmd 检查器生成的命令格式
     */
    public void list(HashMap<String, Object> cmd) {
        // TODO 根据命令参数统计疫情
    }

    // 扩展命令...
}
