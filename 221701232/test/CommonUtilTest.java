import org.junit.jupiter.api.Test;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CommonUtilTest {

    @Test
    void getFiles() throws ParseException {
        InfectStatistic infectStatistic = new InfectStatistic();
        CmdArgs cmdArgs = infectStatistic.getCmdArgs();
        cmdArgs.handleArgs(CmdArgsTest.args1, infectStatistic);
        infectStatistic.setLogFilesMap(CommonUtil.getFiles(cmdArgs.getLogFilePath(),cmdArgs.getDate(),
                InfectStatistic.logFileRegex));
        // Ensure files are read in chronological order
        Map<String, File> fileMap = infectStatistic.getLogFilesMap();
        String preDate = "2000-02-02";
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        Date pre = sd.parse(preDate);
        //assert (now.compareTo(sd.parse(infectStatistic.getDate())) >= 0);// now >= date
        for (Map.Entry<String, File> entry : fileMap.entrySet()) {
            String[] strs = entry.getKey().split("/");
            String[] tempStrs = strs[strs.length - 1].split("\\.");
            Date temp = sd.parse(tempStrs[0]);
            assert(pre.compareTo(temp) < 0);// pre < temp
            pre = temp;
        }
    }

    @Test
    void compareDate() throws ParseException {
        String dateStr1 = "2020-02-08";// dateStr cannot exceed current time
        String dateStr2 = "2020-02-06";// dateStr cannot exceed current time
        // return true if dateStr1 > dateStr2
        assert(CommonUtil.compareDate(dateStr1, dateStr2));
    }

    @Test
    void stringToDate() throws ParseException {
        // string example : yyyy-MM-dd
        // dateStr cannot exceed current time
        String dateStr = "2020-02-05";
        Date date = CommonUtil.stringToDate(dateStr);
        assert(date != null);
    }

    @Test
    void parserStringGetType() {
        // str example : type + number + 人
        // parserStringGetType is subString type
        String str = "感染患者25人";
        String type = CommonUtil.parserStringGetType(str);
        assert(type.equals("感染患者"));
    }

    @Test
    void parserStringToInt() {
        String str = "感染患者25人";
        int number = CommonUtil.parserStringToInt(str);
        assert(number == 25);
    }
}