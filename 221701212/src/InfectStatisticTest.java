import junit.framework.TestCase;
import org.junit.Test;

//import static org.junit.Assert.*;

public class InfectStatisticTest extends TestCase {
    @Test
    public void testInfectStatistic1() {
        String[] str = {"list","-log","D:\\log","-out","D:\\log\\testout1.txt"};
        InfectStatistic.main(str);
    }
    public void testInfectStatistic2() {
        String[] str = {"list","-log","D:\\log","-out","D:\\log\\testout2.txt","-date","2020-02-02"};
        InfectStatistic.main(str);
    }
    public void testInfectStatistic3() {
        String[] str = {"list","-log","D:\\log","-out","D:\\log\\testout3.txt","-date","2020-01-27"};
        InfectStatistic.main(str);
    }
    public void testInfectStatistic4() {
        String[] str = {"list","-log","D:\\log","-out","D:\\log\\testout4.txt","-date","2020-01-07"};
        InfectStatistic.main(str);
    }
    public void testInfectStatistic5() {
        String[] str = {"list","-log","D:\\log","-out","D:\\log\\testout5.txt","-type","ip","-date","2020-02-27"};
        InfectStatistic.main(str);
    }
    public void testInfectStatistic6() {
        String[] str = {"list","-log","D:\\log","-out","D:\\log\\testout6.txt","-type","si","sp","-date","2020-02-27"};
        InfectStatistic.main(str);
    }
    public void testInfectStatistic7() {
        String[] str = {"list","-province","湖北","-log","D:\\log","-out","D:\\log\\testout7.txt","-date","2020-02-27"};
        InfectStatistic.main(str);
    }
    public void testInfectStatistic8() {
        String[] str = {"list","-log","D:\\log","-out","D:\\log\\testout8.txt","-province","江苏","全国"};
        InfectStatistic.main(str);
    }
    public void testInfectStatistic9() {
        String[] str = {"list","-log","D:\\log","-out","D:\\log\\testout9.txt","-date","2020-02-01",
                "-province","全国","江西"};
        InfectStatistic.main(str);
    }
    public void testInfectStatistic10() {
        String[] str = {"list","-log","D:\\log","-out","D:\\log\\testout10.txt","-date","2020-03-27"};
        InfectStatistic.main(str);
    }
}