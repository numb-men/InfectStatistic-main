/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
    public static void main(String[] args) {
        try {
            CommandReceiver receiver = new CommandReceiver();
            Command command = new CommandFactory().getCommand(args[0], receiver);
            CommandLine commandLine = new CommandLine(args, command);

            commandLine.execute();
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
    }
}
