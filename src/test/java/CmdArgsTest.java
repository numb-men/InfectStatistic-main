import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * java类简单作用描述
 *
 * @ProjectName: InfectStatistic-main
 * @Description: java类作用描述
 * @Author: horse
 * @CreateDate: 2020/2/14 11:23
 * @UpdateUser: horse
 * @UpdateDate: 2020/2/14 11:23
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2020</p>
 */
public class CmdArgsTest {
    private CmdArgs cmdArgs;

    @Before
    public void setUp(){
        this.cmdArgs= new CmdArgs("list -log D:\\log\\ -out D:\\ListOut1.txt " +
                "-date 2020-01-22 -type ip sp -province 全国 福建");
    }

    /**
     * 测试获取命令
     * */
    @Test
    public void getCmd() {
        assertEquals("list",this.cmdArgs.getCmd());
    }

    /**
     * 测试获取命令的参数对应的值
     * */
    @Test
    public void getArgVal() {
        assertEquals("D:\\log\\",this.cmdArgs.getArgVal("-log"));
        assertEquals("D:\\ListOut1.txt",this.cmdArgs.getArgVal("-out"));
        assertEquals("2020-01-22",this.cmdArgs.getArgVal("-date"));
    }

    /**
     * 测试获取命令的参数对应的值
     * */
    @Test
    public void getArgVals() {
        assertArrayEquals(new String[]{"ip","sp"},this.cmdArgs.getArgVals("-type"));
        assertArrayEquals(new String[]{"全国","福建"},this.cmdArgs.getArgVals("-province"));
    }
}