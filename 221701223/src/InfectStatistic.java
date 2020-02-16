/**
 * InfectStatistic
 *
 * @author ybn
 */
public class InfectStatistic {
    /**
     * Main
     * 程序主入口
     *
     * @param args args
     */
    public static void main(String[] args) {
        ArgumentContainer argumentContainer = ArgumentHandler.getArgumentContainer(args);
        RecordContainer recordContainer = new RecordContainer() {{
            init();
        }};
        FileTools fileTools = parser.makeFileTools();
        fileTools.readFile(recordContainer);

        recordContainer.printRecordsFilterByCommand(command);
    }
}
