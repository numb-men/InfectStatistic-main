import java.io.File;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

/**
 * Lib
 * TODO
 * 1.HandleFileUtil
 *      a.getFiles
 *      b.readLogFile
 *      c.writeOutFile
 * 2.HandleArgsUtil
 *
 * @author 岳逾先
 * @version 1.1.1
 * @since 1.8
 */
public class Lib {
}

class HandleFileUtil {

    public static Map<String,File> getFiles(String path) {
        File file = new File(path);
        File[] fileList = file.listFiles();
        Map<String, File> fileMap = new HashMap<>();
        if (fileList != null) {
            for (int i = 0; i < fileList.length; ++i) {
                if (fileList[i].isFile()) {
                    String[] temp = fileList[i].toString().split("/");
                    if (!temp[temp.length - 1].matches("2020-[0-1][0-9]-[0-3][0-9].log.txt")) {
                        System.out.println("invalid file : " + temp[temp.length - 1] +" skip");
                        continue;
                    }
                    fileMap.put(fileList[i].toString(), fileList[i]);
                }
            }
        }
        // sorted for file name
        fileMap = fileMap.entrySet().stream()
                .sorted(comparingByValue())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e2, LinkedHashMap::new));
        // debug
        if (fileMap != null) {
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                System.out.println(entry.getKey());
            }
        }
        return fileMap;
    }
}
class HandleArgsUtil {

    public static void handleArgs(String[] args, InfectStatistic infectStatistic) throws ParseException {
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equals("-log")) {
                infectStatistic.setLogFilePath(args[++i]);
            } else if (args[i].equals("-out")) {
                infectStatistic.setOutFilePath(args[++i]);
            } else if (args[i].equals("-date")) {
                infectStatistic.setDate(args[++i]);
            } else if (args[i].equals("-type")) {
                while (i < args.length) {
                    if ((i + 1) >= args.length || args[i + 1].charAt(0) == '-') {
                        break;
                    } else {
                        infectStatistic.addType(args[++i]);
                    }
                }
            } else if (args[i].equals("-province")) {
                while (i < args.length) {
                    if ((i + 1) >= args.length || args[i + 1].charAt(0) == '-') {
                        break;
                    } else {
                        infectStatistic.addProvince(args[++i]);
                    }
                }
            }
        }
    }

    public static void showArgs(InfectStatistic infectStatistic) {
        System.out.println("日志目录位置:" + infectStatistic.getLogFilePath());
        System.out.println("输出文件位置:" + infectStatistic.getOutFilePath());
        System.out.println("统计的最新截至日期:" + infectStatistic.getDate());
        if (infectStatistic.getTypes() != null && infectStatistic.typeNumber() != 0) {
            System.out.println("选择统计的人员类型:");
            for (String tem : infectStatistic.getTypes()) {
                System.out.print(tem + " ");
            }
        }
        if (infectStatistic.getProvinces() != null && infectStatistic.provinceNumber() != 0) {
            System.out.println("选择统计的省份:");
            for (String tem : infectStatistic.getProvinces()) {
                System.out.print(tem + " ");
            }
        }
    }
}

