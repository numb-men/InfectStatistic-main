/**
 * InfectStatistic
 *
 * @author xxx
 * @version xxx
 * @since xxx
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
        command.show();
        FileTools fileTools = parser.makeFileTools();
        fileTools.readFile();
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
//        System.out.println(filename.matches("(19|20)[0-9][0-9]-(0[1-9]|1[0-2])-" +
//            "(0[1-9]|[12][0-9]|3[01]).log.txt"));
    }
}
