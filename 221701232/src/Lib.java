import java.text.ParseException;

/**
 * Lib
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
public class Lib {
}

class HandleArgsUtil {

    public static void HandleArgs(String[] args, InfectStatistic infectStatistic) throws ParseException {
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

    public static void ShowArgs(InfectStatistic infectStatistic) {
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

