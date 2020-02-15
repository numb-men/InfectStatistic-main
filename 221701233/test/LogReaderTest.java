import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogReaderTest {

    public List<String> fileContent(String filePath) {
        List<String> content = new ArrayList<>();

        try (FileReader fr = new FileReader(filePath);
             BufferedReader bf = new BufferedReader(fr)) {
            String line;
            // 按行读取
            while ((line = bf.readLine()) != null) {
                // 过滤注解
                if (!line.substring(0, 2).equals("//")) {
                    content.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    @Test
    public void readLog() throws Exception {
        List<String> expected = new ArrayList<>();
        expected.addAll(fileContent("D:\\log\\2020-01-22.log.txt"));
        expected.addAll(fileContent("D:\\log\\2020-01-23.log.txt"));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        List<String> list = LogReader.readLog(format.parse("2020-01-23"), new File("D:\\log"));
        for (String str : list) {
            System.out.println(str);
        }

        Assert.assertEquals(list.size(), expected.size());
        assertTrue(list.containsAll(expected));
    }

    @Test
    public void readLogAll() throws Exception {
        List<String> expected = new ArrayList<>();
        expected.addAll(fileContent("D:\\log\\2020-01-22.log.txt"));
        expected.addAll(fileContent("D:\\log\\2020-01-23.log.txt"));
        expected.addAll(fileContent("D:\\log\\2020-01-27.log.txt"));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        List<String> list = LogReader.readLog(format.parse("2020-01-27"), new File("D:\\log"));
        for (String str : list) {
            System.out.println(str);
        }

        Assert.assertEquals(list.size(), expected.size());
        assertTrue(list.containsAll(expected));
    }

    @Test
    public void readLogNow() throws Exception {
        List<String> expected = new ArrayList<>();
        expected.addAll(fileContent("D:\\log\\2020-01-22.log.txt"));
        expected.addAll(fileContent("D:\\log\\2020-01-23.log.txt"));
        expected.addAll(fileContent("D:\\log\\2020-01-27.log.txt"));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        List<String> list = LogReader.readLog(new Date(), new File("D:\\log"));
        for (String str : list) {
            System.out.println(str);
        }

        Assert.assertEquals(list.size(), expected.size());
        assertTrue(list.containsAll(expected));
    }

}
