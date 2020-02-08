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
        CommandManager manager = new CommandManager();
        manager.register("list", new ListCommand());
        try {
            manager.invoke(args);
        } catch (CommandInvokeErrorException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
