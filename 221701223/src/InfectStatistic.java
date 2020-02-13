/**
 * InfectStatistic
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class Te {
    int value = 0;

    public Te(int i) {
        value = i;
    }

    public void upd(int u) {
        value += u;
    }

    public int getValue() {
        return value;
    }
}

/**
 * @author ybn
 */
public class InfectStatistic {
    /**
     * Main
     *
     * @param args args
     */
    public static void main(String[] args) {
        ArgumentParser parser = new ArgumentParser(args);
        Command command = parser.makeCommand();
        command.dump();
        FileTools fileTools = parser.makeFileTools();
        fileTools.readFile(new RecordContainer() {{
            init();
        }});

//        String filename = "2020-02-05.log.txt";
//        String path = "/Users/ybn/IdeaProjects/InfectStatistic-main/221701223/log";
//        Path p = Paths.get(path);
////
//        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(path))) {
//            for (Path e : stream) {
//                if (e.getFileName().toString().matches(FileTools.LOG_FILTER)) {
//                    System.out.println(e.getFileName().toString());
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
