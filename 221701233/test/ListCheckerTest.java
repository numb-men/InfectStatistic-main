import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class ListCheckerTest {

    public ListChecker checker = new ListChecker();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void check() {
        String[] args = ("list"
                + " -log D:\\log\\"
                + " -out D:\\ListOut1.txt"
                + " -date 2020-01-22"
                + " -type dead ip sp cure dead"
                + " -province 全国 福建 北京 浙江").split(" ");
        Command command = new ListCommand(new CommandReceiver());
        CommandLine line = new CommandLine(args, command);
        assertDoesNotThrow(() -> checker.check(line));
    }

    @Test
    public void checkLog() {
        assertDoesNotThrow(() -> checker.checkLog("D:\\log"));
    }

    @Test
    public void checkLogError() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage("\"D:\\log.txt\" is not a directory.");
        checker.checkLog("D:\\log.txt");
    }

    @Test
    public void checkLogNull() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage("value of arg \"-log\" is required.");
        checker.checkLog(null);
    }

    @Test
    public void checkOut() {
        assertDoesNotThrow(() -> checker.checkOut("D:\\ListOut.txt"));
    }

    @Test
    public void checkOutError() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage("\"D:\\log\" is not a file.");
        checker.checkOut("D:\\log");
    }

    @Test
    public void checkOutNull() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage("value of arg \"-out\" is required.");
        checker.checkOut(null);
    }


    @Test
    public void checkDate() {
        assertDoesNotThrow(() -> checker.checkDate("2020-2-9"));
    }

    @Test
    public void checkDateError1() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage("Unparseable date: \"2020-2-30\"");
        checker.checkDate("2020-2-30");
    }

    @Test
    public void checkDateError2() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage("Unparseable date: \"2020 2-3\"");
        checker.checkDate("2020 2-3");
    }

    @Test
    public void checkType() {
        List<String> types = new ArrayList<>();
        types.add("ip");
        types.add("sp");
        types.add("cure");
        types.add("dead");
        assertDoesNotThrow(() -> checker.checkType(types));
    }

    @Test
    public void checkTypeError() throws Exception {
        List<String> types = new ArrayList<>();
        types.add("ip");
        types.add("ss");

        thrown.expect(Exception.class);
        thrown.expectMessage("Unknown type：ss");
        checker.checkType(types);
    }

    @Test
    public void checkTypeNull() throws Exception {
        List<String> types = new ArrayList<>();

        assertDoesNotThrow(()->checker.checkType(types));
    }
}
