import junit.framework.TestCase;
import org.junit.Test;

//import static org.junit.Assert.*;

public class InfectStatisticTest extends TestCase {
    @Test
    public void testInfectStatistic1() {
        String[] str = {"list","-log","D:\\log","-out","D:\\out\\output1.txt"};
        InfectStatistic.main(str);
    }
    public void testInfectStatistic2() {
        String[] str = {"list","-log","D:\\log","-out","D:\\out\\output2.txt","-date","2020-01-22"};
        InfectStatistic.main(str);
    }
    public void testInfectStatistic3() {
        String[] str = {"list","-log","D:\\log","-out","D:\\out\\output3.txt","-date","2020-01-23"};
        InfectStatistic.main(str);
    }
    public void testInfectStatistic4() {
        String[] str = {"list","-log","D:\\log","-out","D:\\out\\output4.txt","-date","2020-01-27"};
        InfectStatistic.main(str);
    }
    public void testInfectStatistic5() {
        String[] str = {"list","-log","D:\\log","-out","D:\\out\\output5.txt","-date","2020-01-27","-type","sp"};
        InfectStatistic.main(str);
    }
    public void testInfectStatistic6() {
        String[] str = {"list","-log","D:\\log","-out","D:\\out\\output6.txt","-date","2020-01-27","-type","sp","ip"};
        InfectStatistic.main(str);
    }
    public void testInfectStatistic7() {
        String[] str = {"list","-log","D:\\log","-out","D:\\out\\output7.txt","-date","2020-01-27",
                "-province","湖北"};
        InfectStatistic.main(str);
    }
    public void testInfectStatistic8() {
        String[] str = {"list","-log","D:\\log","-out","D:\\out\\output8.txt","-type","ip","sp",
                "-province","全国","福建"};
        InfectStatistic.main(str);
    }
    public void testInfectStatistic9() {
        String[] str = {"list","-log","D:\\log","-out","D:\\out\\output9.txt","-date","2020-01-27",
                "-province","全国","浙江"};
        InfectStatistic.main(str);
    }
    public void testInfectStatistic10() {
        String[] str = {"list","-log","D:\\log","-out","D:\\out\\output10.txt","-date","2020-01-22",
                "-province","全国","浙江","安徽","湖北","-type","dead","cure","sp","ip"};
        InfectStatistic.main(str);
    }
}
