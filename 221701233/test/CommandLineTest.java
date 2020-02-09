import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommandLineTest {

    public String[] args = ("list"
            + " -log D:\\log\\"
            + " -out D:\\ListOut1.txt"
            + " -date 2020-01-22"
            + " -type dead ip sp cure dead"
            + " -province 全国 福建 北京 浙江").split(" ");

    public CommandLine commandLine = new CommandLine(args, new ListCommand(new CommandReceiver()));

    @Test
    public void getValue() {
        String result = commandLine.getValue("-log");

        assertEquals("D:\\log\\", result);
    }

    @Test
    public void getValueNull() {
        String result = commandLine.getValue("-help");

        assertEquals(null, result);
    }

    @Test
    public void getValues() {
        List<String> result = commandLine.getValues("-type");
        List<String> expected = new ArrayList<>();
        expected.add("dead");
        expected.add("ip");
        expected.add("sp");
        expected.add("cure");
        expected.add("dead");

        assertEquals(expected, result);
    }

    @Test
    public void getValuesEmpty() {
        List<String> result = commandLine.getValues("-help");
        List<String> expected = new ArrayList<>();

        assertEquals(expected, result);
    }
}