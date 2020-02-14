import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Tag;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class InfectStatisticTest {

    final static ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    /**
     * 原例1
     */
    @Test
    public void main1() {
        String data = "list"
                + " -log 221701233\\test\\resources\\log"
                + " -out 221701233\\test\\resources\\result\\listOutput1.txt"
                + " -date 2020-01-22";
        String expected = "全国 感染患者15人 疑似患者22人 治愈2人 死亡1人\n"
                + "福建 感染患者5人 疑似患者7人 治愈0人 死亡0人\n"
                + "湖北 感染患者10人 疑似患者15人 治愈2人 死亡1人\n"
                + "// 该文档并非真实数据，仅供测试使用\n";
        String[] arg = data.split(" ");
        assertDoesNotThrow(() -> InfectStatistic.main(arg));
        assertEquals(expected, actualOutput("221701233\\test\\resources\\result\\listOutput1.txt"));
    }

    /**
     * 原例2
     */
    @Test
    public void main2() {
        String data = "list"
                + " -log 221701233\\test\\resources\\log"
                + " -out 221701233\\test\\resources\\result\\listOutput2.txt"
                + " -date 2020-01-22"
                + " -province 福建 河北";
        String expected = "福建 感染患者5人 疑似患者7人 治愈0人 死亡0人\n"
                + "河北 感染患者0人 疑似患者0人 治愈0人 死亡0人\n"
                + "// 该文档并非真实数据，仅供测试使用\n";

        String[] arg = data.split(" ");
        assertDoesNotThrow(() -> InfectStatistic.main(arg));
        assertEquals(expected, actualOutput("221701233\\test\\resources\\result\\listOutput2.txt"));
    }

    /**
     * 原例3
     */
    @Test
    public void main3() {
        String data = "list"
                + " -log 221701233\\test\\resources\\log"
                + " -out 221701233\\test\\resources\\result\\listOutput3.txt"
                + " -date 2020-01-23"
                + " -type cure dead ip"
                + " -province 全国 浙江 福建";
        String expected = "全国 治愈4人 死亡3人 感染患者42人\n"
                + "福建 治愈1人 死亡0人 感染患者9人\n"
                + "浙江 治愈0人 死亡0人 感染患者0人\n"
                + "// 该文档并非真实数据，仅供测试使用\n";

        String[] arg = data.split(" ");
        assertDoesNotThrow(() -> InfectStatistic.main(arg));
        assertEquals(expected, actualOutput("221701233\\test\\resources\\result\\listOutput3.txt"));
    }

    /**
     * 缺省日期
     */
    @Test
    public void main4() {
        String data = "list"
                + " -log 221701233\\test\\resources\\log"
                + " -out 221701233\\test\\resources\\result\\listOutput4.txt";
        String expected = "全国 感染患者147人 疑似患者317人 治愈27人 死亡21人\n"
                + "福建 感染患者22人 疑似患者38人 治愈3人 死亡0人\n"
                + "湖北 感染患者125人 疑似患者279人 治愈24人 死亡21人\n"
                + "// 该文档并非真实数据，仅供测试使用\n";
        String[] arg = data.split(" ");
        assertDoesNotThrow(() -> InfectStatistic.main(arg));
        assertEquals(expected, actualOutput("221701233\\test\\resources\\result\\listOutput4.txt"));

    }

    /**
     * 缺省类型
     */
    @Test
    public void main5() {
        String data = "list"
                + " -log 221701233\\test\\resources\\log"
                + " -out 221701233\\test\\resources\\result\\listOutput4.txt";
        String expected = "全国 感染患者147人 疑似患者317人 治愈27人 死亡21人\n"
                + "福建 感染患者22人 疑似患者38人 治愈3人 死亡0人\n"
                + "湖北 感染患者125人 疑似患者279人 治愈24人 死亡21人\n"
                + "// 该文档并非真实数据，仅供测试使用\n";
        String[] arg = data.split(" ");
        assertDoesNotThrow(() -> InfectStatistic.main(arg));
        assertEquals(expected, actualOutput("221701233\\test\\resources\\result\\listOutput4.txt"));

    }

    /**
     * 空日志文件夹
     */
    @Test
    public void main6() {
        String data = "list"
                + " -log 221701233\\test\\resources\\emptylog"
                + " -out 221701233\\test\\resources\\result\\listOutput5.txt"
                +" -province 全国 福建 湖北";
        String expected = "全国 感染患者0人 疑似患者0人 治愈0人 死亡0人\n"
                + "福建 感染患者0人 疑似患者0人 治愈0人 死亡0人\n"
                + "湖北 感染患者0人 疑似患者0人 治愈0人 死亡0人\n"
                + "// 该文档并非真实数据，仅供测试使用\n";
        String[] arg = data.split(" ");
        assertDoesNotThrow(() -> InfectStatistic.main(arg));
        assertEquals(expected, actualOutput("221701233\\test\\resources\\result\\listOutput5.txt"));
    }

    /**
     * 缺少list
     */
    @Test
    public void main7() {
        // 更改输出流
        System.setOut(new PrintStream(outContent));

        String data = "-log 221701233\\test\\resources\\log"
                + " -out 221701233\\test\\resources\\result\\listOutput4.txt";

        String[] arg = data.split(" ");
        assertDoesNotThrow(() -> InfectStatistic.main(arg));
        assertEquals("error: Unknown command '-log'", outContent.toString());
        // 重置输出流
        outContent.reset();
        System.setOut(System.out);
    }

    /**
     * 缺少'-log'
     */
    @Test
    public void main8() {
        // 更改输出流
        System.setOut(new PrintStream(outContent));

        String data = "list -out 221701233\\test\\resources\\result\\listOutput4.txt";

        String[] arg = data.split(" ");
        assertDoesNotThrow(() -> InfectStatistic.main(arg));
        assertEquals("error: value of arg \"-log\" is required.", outContent.toString());
        // 重置输出流
        outContent.reset();
        System.setOut(System.out);
    }

    /**
     * 缺少'-out'
     */
    @Test
    public void main9() {
        // 更改输出流
        System.setOut(new PrintStream(outContent));

        String data = "list -log 221701233\\test\\resources\\log";

        String[] arg = data.split(" ");
        assertDoesNotThrow(() -> InfectStatistic.main(arg));
        assertEquals("error: value of arg \"-out\" is required.", outContent.toString());
        // 重置输出流
        outContent.reset();
        System.setOut(System.out);
    }

    /**
     * 从导出文件提取数据
     *
     * @param outFile 导出文件的路径
     * @return
     */
    public static String actualOutput(String outFile) {
        StringBuilder actual = new StringBuilder();
        try (FileReader fr = new FileReader(outFile);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                actual.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return actual.toString();
    }
}
