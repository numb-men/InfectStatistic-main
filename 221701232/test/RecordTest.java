import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RecordTest {

    private static String testPath = "/home/yyx/JavaWorkspace/InfectStatistic-main/221701232/result/test.txt";
    @Test
    void compareTo() {
        Record r1 = new Record();
        r1.setAll("福建", 10, 20, 0, 10);
        Record r2 = new Record();
        r2.setAll("福建", 10, 20, 0, 10);
        assert(r1.compareTo(r2));
    }

    @Test
    void writeRecordToFile() throws IOException {
        Record r1 = new Record();
        r1.setAll("福建", 10, 20, 0, 10);
        BufferedWriter bw = null;
        BufferedReader br = null;
        Record r2 = new Record();
        try {
            // write one record to file
            bw = CommonUtil.newBufferWriter(testPath);
            r1.writeRecordToFile(bw, null);
            // read result from file
            br = CommonUtil.newBufferReader(testPath);
            String line = br.readLine();
            CommonUtil.readOneLineOfOutFile(r2, line);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bw.close();
            br.close();
        }
        r1.showRecordMessage();
        r2.showRecordMessage();
        assert(r1.compareTo(r2));
    }
}