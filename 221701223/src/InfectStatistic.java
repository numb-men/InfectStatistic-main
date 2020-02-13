/**
 * InfectStatistic
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
        ArgumentParser parser = new ArgumentParser(args);
        Command command = parser.makeCommand();
        RecordContainer recordContainer = new RecordContainer() {{
            init();
        }};
        FileTools fileTools = parser.makeFileTools();
        fileTools.readFile(recordContainer);

        recordContainer.printRecordsFilterByCommand(command);
    }
}
