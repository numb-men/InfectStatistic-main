import org.junit.Test;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LogReaderTest {

    @Test
    public void readLog() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        List<String> list = LogReader.readLog(format.parse("2020-01-27"),new File("D:\\log"));
        for (String str : list) {
            System.out.println(str);
        }
    }
}
