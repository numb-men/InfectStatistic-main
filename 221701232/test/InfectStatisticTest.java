import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class InfectStatisticTest {

    @Test
    void mainWithArgs1() throws IOException, ParseException {
        InfectStatistic.main(CmdArgsTest.args1);
    }

    @Test
    void mainWithArgs2() throws IOException, ParseException {
        InfectStatistic.main(CmdArgsTest.args2);
    }

    @Test
    void mainWithArgs3() throws IOException, ParseException {
        InfectStatistic.main(CmdArgsTest.args3);
    }
}