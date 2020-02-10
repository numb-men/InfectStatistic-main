/**
 * InfectStatistic
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
public class InfectStatistic {
    public static void main(String[] args) {
        ArgumentParser parser = new ArgumentParser(args);
        Command command = parser.makeCommand();
        command.show();
    }
}
