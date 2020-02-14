import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * 打包测试
 */
@RunWith(Suite.class)
@SuiteClasses({
        CommandFactoryTest.class,
        ListCheckerTest.class,
        CommandLineTest.class,
        LogReaderTest.class,
        SorterTest.class,
        InfectStatisticTest.class
})
public class SuiteTest {
}
