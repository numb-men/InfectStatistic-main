import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
    * Project Name:InfectStatistic-main2
    * File Name:ArgsParseTest.java
    * Package Name:
    * Date:2020年2月12日 下午12:46:54
    * Copyright (c) 2020 Doris. All Rights Reserved.
    *
    */

/**
 * Description: <br/>
 * Date:2020年2月12日 下午12:46:54 <br/>
 * 
 * @author Doris
 * @version
 * @see
 */

public class ArgsParseTest {
    @Test
    public void testParse() {
        String[] args = { "-log", "logname", "-out", "outname", "-type", "sp", "-province", "福建" };
        ArgsParse ap = new ArgsParse();
        ap.parse(args);
        // System.out.println(ap.sprovince);
        assertEquals("logname", ap.slog);
        assertEquals("福建", ap.sprovince);
        assertEquals("outname", ap.sout);
        assertEquals("sp", ap.stype);

    }

}
