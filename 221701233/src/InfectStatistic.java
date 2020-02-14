/**
 * InfectStatistic
 * TODO
 *
 * @author 张一凡
 * @version 1.0
 * @since 1.0
 */
class InfectStatistic {
    public static void main(String[] args) {
        try {
            CommandReceiver receiver = new CommandReceiver();
            Command command = new CommandFactory().getCommand(args[0], receiver);
            CommandLine commandLine = new CommandLine(args, command);

            commandLine.execute();
        } catch (Exception e) {
            System.out.print("error: " + e.getMessage());
        }
    }
}
