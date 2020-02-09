import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class InfectStatisticTest {

    @Test
    void main() throws IOException, ParseException {
        InfectStatistic.main(CmdArgsTest.args3);
    }
}