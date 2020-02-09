import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class CommandFactoryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getCommand() {
        CommandFactory factory = new CommandFactory();
        CommandReceiver receiver = new CommandReceiver();

        assertDoesNotThrow(()->factory.getCommand("list", receiver));
    }

    @Test
    public void getCommandError() throws Exception {
        CommandFactory factory = new CommandFactory();
        CommandReceiver receiver = new CommandReceiver();

        thrown.expect(NoSuchMethodException.class);
        thrown.expectMessage("CommandFactory.help(CommandReceiver)");
        factory.getCommand("help", receiver);
    }
}